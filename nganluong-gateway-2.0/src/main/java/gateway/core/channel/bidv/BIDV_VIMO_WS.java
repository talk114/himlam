package gateway.core.channel.bidv;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import gateway.core.channel.bidv.dto.BIDVConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom.JDOMException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import gateway.core.channel.bidv.dto.Message8583;
import gateway.core.channel.bidv.dto.req.BIDVConfirmLinkCardReq;
import gateway.core.channel.bidv.dto.req.VimoConfirmLinkCardReq;
import gateway.core.channel.bidv.dto.req.VimoTopupReq;
import gateway.core.channel.bidv.dto.res.VimoConfirmLinkCardRes;
import gateway.core.channel.bidv.dto.res.VimoTopupRes;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGBeanUtils;
import gateway.core.util.PGSecurity;
import gateway.core.util.PGUtil;

@Component
public class BIDV_VIMO_WS {

    private String vimoUrl = "http://10.0.21.21/vimo_api/gateway_call.php";
    private String vimoUrlUserPass = "vimobidv:53XBm#Dv#k5E6123";
    private String vimoAuthenKey = "G7##aaff4v#k5E6123";

    private String encryptKey = "bidv_vimo_123";

    static PGBeanUtils beanUtil;
    static ObjectMapper mapper;
    private static final Logger LOG = LogManager.getLogger(BIDV_VIMO_WS.class);

    static {
        beanUtil = new PGBeanUtils();
        mapper = new ObjectMapper();
        //LOG = Logger.getLogger(BIDV_VIMO_WS.class);
    }

    public String ConfirmLinkCard(String input) {

        String responseCode;
        String responseDesc;
        String redirectrUrl = "";
        String moreInfoVimo = "";
        StringBuilder sb = new StringBuilder();
        LOG.info("############################ 1. BIDV CONFIRM LINK CARD  ############################\n" + input);
        try {
            String requestStr = "<ConfirmLinkCardReq>" + input + "</ConfirmLinkCardReq>";
            JAXBContext jaxbContext = JAXBContext.newInstance(BIDVConfirmLinkCardReq.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            BIDVConfirmLinkCardReq bidvReq = (BIDVConfirmLinkCardReq) jaxbUnmarshaller
                    .unmarshal(new StringReader(requestStr));

            // Validate signature
            String dataBeforeSign = bidvReq.dataBeforeSignature();

            String verifySecureCode = BIDVSecurity.md5(dataBeforeSign, getEncryptKey());

            if (!verifySecureCode.equals(bidvReq.getSecureCode())) {
                responseCode = "007";
                responseDesc = "Ma bao mat khong dung";
            } else {
                // Call Vimo
                VimoConfirmLinkCardReq vimoReq = new VimoConfirmLinkCardReq();
                beanUtil.copyProperties(vimoReq, bidvReq);
                if (!"000".equals(vimoReq.getResultCode())) {
                    vimoReq.setBankAccNumber("ERROR");
                }

                String vimoResultStr = this.callVimo(mapper.writeValueAsString(vimoReq),
                        BIDVConstants.CONFIRM_LINK_CARD);
                LOG.info("############################ 2. VIMO RESPONSE  ############################\n"
                        + vimoResultStr);
                VimoConfirmLinkCardRes vimoResult = mapper.readValue(vimoResultStr, VimoConfirmLinkCardRes.class);

                responseCode = "00".equals(vimoResult.getResponseCode()) ? "000" : "104";
                responseDesc = "00".equals(vimoResult.getResponseCode()) ? "Liên kết thành công" : "Liên kết thất bại";
                redirectrUrl = vimoResult.getRedirectUrl();
            }

        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            responseCode = "104";
            responseDesc = ex.getMessage();
        }

        String[] responseArray = { responseCode, responseDesc, redirectrUrl, moreInfoVimo };
        String secureCode = "";
        try {
            secureCode = BIDVSecurity.md5(responseArray, getEncryptKey());
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            responseCode = "104";
            responseDesc = ex.getMessage();
        }
        sb.append("<Response_Code>").append(responseCode).append("</Response_Code>");
        sb.append("<Response_Desc>").append(responseDesc).append("</Response_Desc>");
        sb.append("<Redirect_Url>").append(redirectrUrl).append("</Redirect_Url>");
        sb.append("<More_Info>").append(moreInfoVimo).append("</More_Info>");
        sb.append("<Secure_Code>").append(secureCode).append("</Secure_Code>");
        LOG.info("############################ 3. RESPONSE TO BIDV  ############################\n" + sb.toString());
        return sb.toString();
    }

    public String GetInfo(String input) {
        LOG.info("############################ 1. BIDV GETINFO ############################\n" + input);

        VimoTopupRes vimoRes = null;
        String amountBidv = "0";
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Message8583.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Message8583 bidvReq = (Message8583) jaxbUnmarshaller.unmarshal(new StringReader(input));

            amountBidv = parseAmountBIDV(bidvReq.getAmount());
            // verify signature BIDV
            String dataReq = bidvReq.dataBeforeSignature();
            boolean signatureReqVal = BIDVSecurity.verify(dataReq, bidvReq.getSignature());
            if (!signatureReqVal) {
                LOG.error("BIDV_VIMO_WS GETINFO SIGNATURE ERROR");
                return buildErrorResponse(BIDVConstants.GET_INFO_RES_HEADER, "BIDV_VIMO_WS GETINFO SIGNATURE ERROR",
                        "007");
            }

            // call vimo
            VimoTopupReq vimoReq = new VimoTopupReq();
            vimoReq.setTransId(bidvReq.getAuditNumber());
            vimoReq.setCustomerId(bidvReq.getCustomerId());
            vimoReq.setRequestDateTime(bidvReq.getRequestDateTime());
            vimoReq.setAmount(amountBidv);

            String vimoResStr = callVimo(mapper.writeValueAsString(vimoReq), BIDVConstants.GET_INFO);
            vimoRes = mapper.readValue(vimoResStr, VimoTopupRes.class);

            // create response BIDV
            Message8583 bidvRes = new Message8583();
            beanUtil.copyProperties(bidvRes, bidvReq);
            bidvRes.setMti(BIDVConstants.GET_INFO_RES_HEADER);
            bidvRes.setRequestDateTime(getReqDateTime()); // MMDDhhmmss
            bidvRes.setAreaCode(bidvRes.getServiceCode());
            bidvRes.setCurrencyCode(BIDVConstants.CURRENCY_VND);
            bidvRes.setCustomerInfo(vimoRes.getData().getFullName() + "|" + vimoRes.getData().getAddress());
            bidvRes.setResponseCode(BIDVConstants.VIMO_BIDV_GET_INFO.get(vimoRes.getErrorCode()));
            bidvRes.setBillTransId("[" + vimoRes.getData().getBillId() + "|" + "|" + amountBidv
                    + "| Topup vimo, khach hang: " + bidvReq.getCustomerId() + ", So tien nap: " + amountBidv + "]");
            // create signature
            String dataRes = bidvRes.dataBeforeSignature();
            LOG.info("DATA BEFORE SIGNATURE: \n" + dataRes);
            bidvRes.setSignature(BIDVSecurity.sign(dataRes));
            return buildResponseBIDV(bidvRes);
        } catch (Exception e) {
            try {
                LOG.error("BIDV_VIMO_WS ERROR Line " + PGUtil.getLineNumber() + "\n" + e.getMessage());
                return buildErrorResponse(BIDVConstants.GET_INFO_RES_HEADER, e.getMessage(),
                        BIDVConstants.VIMO_BIDV_GET_INFO.get(vimoRes.getErrorCode()));
            } catch (JAXBException e1) {
                LOG.error("BIDV_VIMO_WS ERROR Line " + PGUtil.getLineNumber() + "\n" + e1.getMessage());
                return "";
            }
        }
    }

    public String Topup(String input) {
        LOG.info("############################ 1. BIDV TOPUP ############################\n" + input);

        VimoTopupRes vimoRes = null;
        String amountBidv = "";
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Message8583.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Message8583 bidvReq = (Message8583) jaxbUnmarshaller.unmarshal(new StringReader(input));
            amountBidv = parseAmountBIDV(bidvReq.getAmount());
            // verify signature
            String dataReq = bidvReq.dataBeforeSignature();
            boolean signatureReqVal = BIDVSecurity.verify(dataReq, bidvReq.getSignature());
            if (!signatureReqVal) {
                LOG.error("BIDV_VIMO_WS TOPUP SIGNATURE ERROR");
                return buildErrorResponse(BIDVConstants.TOPUP_RES_HEADER, "BIDV_VIMO_WS TOPUP SIGNATURE ERROR", "007");
            }

            // call vimo
            VimoTopupReq vimoReq = new VimoTopupReq();
            vimoReq.setTransId(bidvReq.getAuditNumber());
            vimoReq.setCustomerId(bidvReq.getCustomerId());
            vimoReq.setRequestDateTime(bidvReq.getRequestDateTime());
            vimoReq.setAmount(amountBidv);
            vimoReq.setBillId(bidvReq.getBillTransId());

            String vimoResStr = callVimo(mapper.writeValueAsString(vimoReq), BIDVConstants.TOPUP);
            vimoRes = mapper.readValue(vimoResStr, VimoTopupRes.class);

            // create response
            Message8583 bidvRes = new Message8583();
            beanUtil.copyProperties(bidvRes, bidvReq);
            bidvRes.setMti(BIDVConstants.TOPUP_RES_HEADER);
            bidvRes.setRequestDateTime(getReqDateTime()); // from vimo
            bidvRes.setResponseCode(BIDVConstants.VIMO_BIDV_TOPUP.get(vimoRes.getErrorCode()));
            // create signature
            String dataRes = bidvRes.dataBeforeSignature();
            bidvRes.setSignature(BIDVSecurity.sign(dataRes));
            return buildResponseBIDV(bidvRes);
        } catch (Exception e) {
            try {
                LOG.error("BIDV_VIMO_WS ERROR Line " + PGUtil.getLineNumber() + "\n" + e.getMessage());
                return buildErrorResponse(BIDVConstants.TOPUP_RES_HEADER, e.getMessage(),
                        BIDVConstants.VIMO_BIDV_TOPUP.get(vimoRes.getErrorCode()));
            } catch (JAXBException e1) {
                LOG.error("BIDV_VIMO_WS ERROR Line " + PGUtil.getLineNumber() + "\n" + e1.getMessage());
                return "";
            }
        }
    }

    private String buildResponseBIDV(Message8583 response) throws JAXBException {
        JAXBContext jaxbContextRes = JAXBContext.newInstance(Message8583.class);
        Marshaller jaxbMarshallerRes = jaxbContextRes.createMarshaller();
        jaxbMarshallerRes.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter swRes = new StringWriter();
        jaxbMarshallerRes.marshal(response, swRes);
        return swRes.toString();
    }

    private String buildErrorResponse(String mti, String message, String errorCode) throws JAXBException {
        Message8583 response = new Message8583();
        response.setMti(mti);
        response.setResponseCode(errorCode);

        JAXBContext jaxbContextRes = JAXBContext.newInstance(Message8583.class);
        Marshaller jaxbMarshallerRes = jaxbContextRes.createMarshaller();
        jaxbMarshallerRes.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter swRes = new StringWriter();
        jaxbMarshallerRes.marshal(response, swRes);
        return swRes.toString();
    }

    private String parseAmountBIDV(String amount) {
        return Integer.valueOf(amount.substring(0, amount.length() - 2)).toString();
    }

    private String callVimo(String req, String fnc)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException,
            IllegalBlockSizeException, BadPaddingException, IOException {

        LOG.info("=============== Param call Vimo: " + req + "\n UserNamePass: " + vimoUrlUserPass);
        Map<String, Object> map = new HashMap<>();
        String data = PGSecurity.encrypt3DES(req, getVimoAuthenKey());
        map.put("func", fnc);
        map.put("params",  URLEncoder.encode(data, "UTF-8"));
        map.put("checksum", PGSecurity.md5(fnc, data));
        map.put("partner", "BIDV");
        String result = HttpUtil.send(vimoUrl, map, getVimoUrlUserPass());
        return result;
    }

    private static String getReqDateTime() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMddHHmmss");
        return df.format(date);
    }

    public String getVimoUrl() {
        return vimoUrl;
    }

    public void setVimoUrl(String vimoUrl) {
        this.vimoUrl = vimoUrl;
    }

    public String getVimoUrlUserPass() {
        return vimoUrlUserPass;
    }

    public void setVimoUrlUserPass(String vimoUrlUserPass) {
        this.vimoUrlUserPass = vimoUrlUserPass;
    }

    public String getVimoAuthenKey() {
        return vimoAuthenKey;
    }

    public void setVimoAuthenKey(String vimoAuthenKey) {
        this.vimoAuthenKey = vimoAuthenKey;
    }

    public String getEncryptKey() {
        return encryptKey;
    }

    public void setEncryptKey(String encryptKey) {
        this.encryptKey = encryptKey;
    }

    public static void main(String args[])
            throws JDOMException, IOException, JAXBException, InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {

        BIDV_VIMO_WS f1 = new BIDV_VIMO_WS();
        String confirm = "<Bank_Code>970418</Bank_Code><Bank_Name>BIDV</Bank_Name><Service_Id>083001</Service_Id><Merchant_Id>083001</Merchant_Id><Trans_Id>VM150667367454</Trans_Id><Result_Code>000</Result_Code><Bank_trans_id>214797</Bank_trans_id><Payer_id>981601988666999</Payer_id><Secure_Code>341851d8005579402bc78521f5552b27</Secure_Code>";
        // System.out.println(f1.ConfirmLinkCard(confirm));

        // VimoTopupReq infoReq = new VimoTopupReq();
        // infoReq.setCustomerId("0988666999");
        // infoReq.setTransId(RandomUtil.randomDigitString(6));
        // infoReq.setRequestDateTime("20170720");
        // infoReq.setAmount("100000");
        // String res = f1.callVimo(mapper.writeValueAsString(infoReq),
        // "GetInfo");
        // VimoTopupRes infoRes = mapper.readValue(res, VimoTopupRes.class);
        // System.out.println(res);

        // VimoTopupReq topupReq = new VimoTopupReq();
        // topupReq.setCustomerId("0988666999");
        // topupReq.setTransId(RandomUtil.randomDigitString(6));
        // topupReq.setRequestDateTime("20170720");
        // topupReq.setAmount("100000");
        // topupReq.setBillId("8798");
        // String billres = f1.callVimo(mapper.writeValueAsString(topupReq),
        // "Topup");
        // VimoTopupRes billRes = mapper.readValue(billres, VimoTopupRes.class);
        // System.out.println(billres);

        // bankCode + "|" + bankName + "|" + serviceId + "|" + merchantId + "|"
        // + transactionId + "|" + resultCode + "|" + bankTransId;
        // 970418|BIDV|083001|083001|VM150113756495|000|211471
        String data = "bidvvidmo123|970418|BIDV|083001|083001|148697518468337|000|0821010400 - 170208104717|198109";
        // System.out.println(PGSecurity.md5(data));

        // String arrayResponse[] = { rootOutput.getServiceId(),
        // rootOutput.getMerchantId(), rootOutput.getTrandate(),
        // rootOutput.getTransId(), rootOutput.getResponseCode(),
        // rootOutput.getResponseTxnCode(),
        // rootOutput.getList(), rootOutput.getMoreInfo(),
        // rootOutput.getRedirectUrl() };
        // {"status":true,"error_code":"000","message":"Giao dịch thành
        // công","data":"{\"list\":\"\",\"service_id\":\"083001\",
        // \"merchant_id\":\"083001\",\"transaction_date\":\"20170727133924\",\"transaction_id\":\"VM150113756495\",\"response_code\":\"000\",\"bank_transaction_id\":\"\",
        // \"more_info\":\"\",\"redirect_url\":\"https://119.17.209.164:10042/EWALLET/SecureCodeServlet?secureCode=57566569314C48724E4B4936756E33396E6E637078673D3D\"}"}
        String linkRes = "083001|083001|20170727133924|VM150113756495|000||||https://119.17.209.164:10042/EWALLET/SecureCodeServlet?secureCode=57566569314C48724E4B4936756E33396E6E637078673D3D";
        // System.out.println(BIDVSecurity.md5(linkRes, ENCRYPT_KEY));

        String message = "XIN CHAO CON GA";
        StringWriter sw = new StringWriter();
        JAXB.marshal(message, sw);
        System.out.println(sw.toString());
    }

}
