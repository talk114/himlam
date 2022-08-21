package gateway.core.channel.tcb.service.impl;

import gateway.core.channel.PaymentGate;
import gateway.core.channel.tcb.dto.JavaSecutityEncrypt_V2_1;
import gateway.core.channel.tcb.dto.JavaSignSHA256_V2_1;
import gateway.core.channel.tcb.dto.TCBConstants;
import gateway.core.channel.tcb.dto.TCBUtils;
import gateway.core.channel.tcb.request.FunTransferRequest;
import gateway.core.channel.tcb.request.InqListBankInfoRequest;
import gateway.core.channel.tcb.request.UpdateStsTransactionReq;
import gateway.core.channel.tcb.response.*;
import gateway.core.channel.tcb.service.TCBService;
import gateway.core.dto.PGResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.RequestUtil;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author taind
 */
@Service
public class TCBServiceImpl extends PaymentGate implements TCBService {

    private static final Logger logger = LogManager.getLogger(TCBServiceImpl.class);
    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private CommonPGResponseService commonPGResponseService;
    @Autowired
    private PaymentService paymentService;

    @Override
    public PGResponse InqListBankInfo(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START GET LIST BANK INFO TCB (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME, TCBConstants.FUNCTION_LIST_BANK_INFO, true, true, false, paramsLog));

            InqListBankInfoRequest request = objectMapper.readValue(inputStr, InqListBankInfoRequest.class);
            // convert time long to datetime
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String transTime = sdf.format(calendar.getTime());
            String requestApi = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.techcombank.com.vn/services/bank/collection/v1\">\n"
                    + "   <soapenv:Header/>\n"
                    + "   <soapenv:Body>\n"
                    + "      <v1:InqBnkInfReq>\n"
                    + "         <v1:ReqGnlInf>\n"
                    + "            <v1:Id>" + request.getTransId() + "</v1:Id>\n"
                    + "            <v1:TxId>" + request.getTransId() + "</v1:TxId>\n"
                    + "            <v1:CreDtTm>" + transTime + "</v1:CreDtTm>\n"
                    + "            <v1:Desc>" + request.getDescription() + "</v1:Desc>\n"
                    + "            <v1:Sgntr>\n"
                    + "               <v1:Sgntr1/>\n"
                    + "            </v1:Sgntr>\n"
                    + "         </v1:ReqGnlInf>\n"
                    + "         <v1:Envt>\n"
                    + "            <v1:SrcPty>\n"
                    + "               <v1:Nm>" + TCBConstants.PARTNER_ID + "</v1:Nm>\n"
                    + "            </v1:SrcPty>\n"
                    + "            <v1:TrgtPty>\n"
                    + "               <v1:Nm>H2H</v1:Nm>\n"
                    + "            </v1:TrgtPty>\n"
                    + "            <v1:Rqstr>\n"
                    + "               <v1:Nm/>\n"
                    + "            </v1:Rqstr>\n"
                    + "         </v1:Envt>\n"
                    + "         <v1:ReqInf>\n"
                    + "            <v1:BkID>ALL</v1:BkID>\n"
                    + "         </v1:ReqInf>\n"
                    + "      </v1:InqBnkInfReq>\n"
                    + "   </soapenv:Body>\n"
                    + "</soapenv:Envelope>";

            paramsLog = new String[]{"REQUEST TO TCB ONUS: Message - " + " (" + requestApi + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_LIST_BANK_INFO, true, true, false, paramsLog));
            // call to TCB
            String responsePartners = httpsPost(TCBConstants.URL_WSDL, requestApi);

            paramsLog = new String[]{"RESPONSE TCB ONUS: Message - " + " (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_LIST_BANK_INFO, true, true, false, paramsLog));

            JSONObject xmlToJson = TCBUtils.xmlToJson(responsePartners);
            JSONObject object1 = xmlToJson.getJSONObject("soapenv:Envelope");
            JSONObject object2 = object1.getJSONObject("S:Body");
            JSONObject object3 = object2.has("v1:InqBnkInfRspn") ? object2.getJSONObject("v1:InqBnkInfRspn") : null;
            JSONArray arrObject1 = object3.has("v1:BkInfRcrd") ? object3.getJSONArray("v1:BkInfRcrd") : null;
            JSONObject object4 = object3.has("v1:RspnSts") ? object3.getJSONObject("v1:RspnSts") : null;
            JSONObject object5 = object3.has("v1:RspnInf") ? object3.getJSONObject("v1:RspnInf") : null;

            List<BkInfRcrdResponse> listDatas = new ArrayList<>();
            if (arrObject1 != null) {
                for (int i = 0; i < arrObject1.length(); i++) {
                    JSONObject obj = arrObject1.getJSONObject(i);
                    BkInfRcrdResponse rs = new BkInfRcrdResponse();
                    rs.setIsCentralizedBank(obj.get("v1:IsCentralizedBank").toString());
                    rs.setBkCd(obj.get("v1:BkCd").toString());
                    rs.setBrnchNm(obj.get("v1:BrnchNm").toString());
                    rs.setCITAD(obj.get("v1:CITAD").toString());
                    rs.setBkNm(obj.get("v1:BkNm").toString());
                    rs.setActvSts(obj.get("v1:ActvSts").toString());
                    rs.setStatPrvc(obj.get("v1:StatPrvc").toString());
                    listDatas.add(rs);
                }
            }
            RspnStsResponse stsResponse = new RspnStsResponse();
            RspnInfResponse infResponse = new RspnInfResponse();
            if (object4 != null) {
                stsResponse = new RspnStsResponse(object4);
            }
            if (object5 != null) {
                infResponse = new RspnInfResponse(object5);
            }

            InqListBankInfoResponse res = new InqListBankInfoResponse(infResponse, stsResponse, listDatas);

            pgResponse.setStatus(true);
            pgResponse.setErrorCode("000");
            pgResponse.setMessage("Success");
            pgResponse.setData(objectMapper.writeValueAsString(res));
            // Write log end function
            paramsLog = new String[]{"END GET LIST BANK TCB ONUS (" + objectMapper.writeValueAsString(res) + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME, TCBConstants.FUNCTION_LIST_BANK_INFO, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

//    private String requestApi(String url, String input) throws Exception {
//        ClassLoader classLoader = TCBServiceImpl.class.getClassLoader();
//
//        FileInputStream fis = new FileInputStream(Objects.requireNonNull(classLoader.getResource(TCBConstants.PRIVATE_KEY_CCA)).getFile());
//        X509Certificate ca = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new BufferedInputStream(fis));
//
//        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//        ks.load(null, null);
//        ks.setCertificateEntry(Integer.toString(1), ca);
//
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//        tmf.init(ks);
//
//        String auth = TCBConstants.USER_NAME + ":" + TCBConstants.PASSWORD;
//        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.UTF_8));
//        String authHeaderValue = "Basic " + new String(encodedAuth);
//
//        OkHttpClient client = new OkHttpClient().newBuilder().build();
//
//        MediaType mediaType = MediaType.parse("application/xml");
//
//        RequestBody body = RequestBody.create(mediaType, input);
//        Request request = new Request.Builder()
//                .url(url)
//                .method("POST", body)
//                .addHeader("Authorization", authHeaderValue)
//                .addHeader("Content-Type", "application/xml")
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        return response.body().string();
//    }

    @Override
    public PGResponse FundTransfer(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START FUN TRANSFER TCB (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME, TCBConstants.FUNCTION_FUN_TRANSFER, true, true, false, paramsLog));

            FunTransferRequest request = objectMapper.readValue(inputStr, FunTransferRequest.class);
            // convert time long to datetime
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String transTime = sdf.format(calendar.getTime());

            String inputDecryipt = "<ReqInf xmlns=\"http://www.techcombank.com.vn/services/bank/collection/v1\">"
                    + "<TxTp>" + TCBConstants.CARD_TYPE + "</TxTp>"
                    + "<TxDt>" + transTime + "</TxDt>"
                    + "<TxAmt>" + request.getAmount() + "</TxAmt>"
                    + "<Desc>" + request.getDescription() + "</Desc>"
                    + "<FrAcct><AcctId>" + request.getFromAccountNumber() + "</AcctId><AcctTitl>" + request.getFromAccountName() + "</AcctTitl></FrAcct>"
                    + "<ToAcct><AcctId>" + request.getToAccountNumber() + "</AcctId><AcctTitl>" + request.getToAccountName() + "</AcctTitl><FIData><CITAD>" + request.getCitad() + "</CITAD></FIData></ToAcct></ReqInf>";

            paramsLog = new String[]{"REQUEST DATA ENCRYPT: Message - " + " (" + inputDecryipt + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_FUN_TRANSFER, true, true, false, paramsLog));

            String dataEncrypt = JavaSecutityEncrypt_V2_1.encrypt_AES256(inputDecryipt, TCBConstants.PUBLIC_KEY_MAHOA_PEM);

            paramsLog = new String[]{"REQUEST DATA ENCRYPT: Message - " + " (" + dataEncrypt + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_FUN_TRANSFER, true, true, false, paramsLog));

            String sSource = "FundTransfer" + request.getTransId() + request.getToAccountNumber() + request.getToAccountName() + transTime + request.getAmount();
            String sign01 = JavaSignSHA256_V2_1.signData_SHA256(sSource, TCBConstants.PRIVATE_KEY_01_PEM);
            String sign02 = JavaSignSHA256_V2_1.signData_SHA256(sSource, TCBConstants.PRIVATE_KEY_02_PEM);

            String inputEncrypt = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.techcombank.com.vn/services/bank/collection/v1\">\n"
                    + "   <soapenv:Header/>\n"
                    + "   <soapenv:Body>\n"
                    + "      <v1:XferReq xmlns=\"http://www.techcombank.com.vn/services/bank/collection/v1\"> <v1:ReqGnlInf>\n"
                    + "            <v1:Id>" + request.getTransId() + "</v1:Id>\n"
                    + "            <v1:TxId>" + request.getTransId() + "</v1:TxId>\n"
                    + "            <v1:CreDtTm>" + transTime + "</v1:CreDtTm>\n"
                    + "            <v1:PmtTp>" + request.getPaymentType() + "</v1:PmtTp>\n"
                    + "            <v1:Desc>" + request.getDescription() + "</v1:Desc>\n"
                    + "            <v1:Sgntr>\n"
                    + "               <v1:Sgntr1 user=\"NGLUONG1\">" + sign01 + "</v1:Sgntr1>\n"
                    + "               <v1:Sgntr2 user=\"NGLUONG2\">" + sign02 + "</v1:Sgntr2>\n"
                    + "            </v1:Sgntr>\n"
                    + "         </v1:ReqGnlInf>\n"
                    + "         <v1:Envt>\n"
                    + "            <v1:TrgtPty>\n"
                    + "               <v1:Nm>H2H</v1:Nm>\n"
                    + "            </v1:TrgtPty>\n"
                    + "            <v1:SrcPty>\n"
                    + "               <v1:Nm>" + TCBConstants.PARTNER_ID + "</v1:Nm>\n"
                    + "            </v1:SrcPty>\n"
                    + "            <v1:Rqstr>\n"
                    + "               <v1:Nm>" + TCBConstants.CHANNEL_CODE + "</v1:Nm>\n"
                    + "            </v1:Rqstr>\n"
                    + "         </v1:Envt>\n"
                    + "         <v1:ReqInf>" + dataEncrypt + "</v1:ReqInf>\n"
                    + "      </v1:XferReq>\n"
                    + "   </soapenv:Body>\n"
                    + "</soapenv:Envelope>";

            paramsLog = new String[]{"REQUEST FUN TRANSFER: Message - " + " (" + inputEncrypt + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_FUN_TRANSFER, true, true, false, paramsLog));

            // create payment
            Payment payment = createPaymentData(channelFunction, request);
            paramsLog = new String[]{"RESPONSE CREATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(payment) + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_FUN_TRANSFER, true, true, false, paramsLog));
            // call to TCB
            String responsePartners = httpsPost(TCBConstants.URL_WSDL, inputEncrypt);
            paramsLog = new String[]{"RESPONSE FUN TRANSFER: Message - " + " (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_FUN_TRANSFER, true, true, false, paramsLog));

            JSONObject xmlToJson = TCBUtils.xmlToJson(responsePartners);
            JSONObject object1 = xmlToJson.getJSONObject("soapenv:Envelope");
            JSONObject object2 = object1.getJSONObject("soapenv:Body");
            JSONObject object3 = object2.has("ns:XferRspn") ? object2.getJSONObject("ns:XferRspn") : null;
            JSONObject object4 = object3.has("ns:RspnSts") ? object3.getJSONObject("ns:RspnSts") : null;
            JSONObject object5 = object3.has("ns:TxInf") ? object3.getJSONObject("ns:TxInf") : null;
            JSONObject object6 = object3.has("ns:RspnInf") ? object3.getJSONObject("ns:RspnInf") : null;

            FunTransferRspnStsResponse rspnSts = new FunTransferRspnStsResponse();
            FunTransferTxInfResponse txInf = new FunTransferTxInfResponse();
            FunTransferRspnInfResponse rspnInf = new FunTransferRspnInfResponse();
            String[] split = null;
            if (object3 != null) {
                if (object4 != null) {
                    rspnSts = new FunTransferRspnStsResponse(object4);
                    split = rspnSts.getAddtlStsRsnInf().split("\\|");
                }
                if (object5 != null) {
                    txInf = new FunTransferTxInfResponse(object5);
                }
                if (object6 != null) {
                    rspnInf = new FunTransferRspnInfResponse(object6);
                }
            }
            FunTransferResponse funTransfer = new FunTransferResponse(rspnSts, txInf, rspnInf);

            // update payment
            if (split[0].toString().equals("000")) {
                Payment paymentUpdate = updatePaymentData(payment, funTransfer);
                paramsLog = new String[]{"RESPONSE UPDATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(paymentUpdate) + ")"};
                logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                        TCBConstants.FUNCTION_FUN_TRANSFER, true, true, false, paramsLog));
            }
            pgResponse.setStatus(true);
            pgResponse.setErrorCode(split[0].toString());
            pgResponse.setMessage(TCBConstants.getErrorMessage(split[0].toString()));
            pgResponse.setData(objectMapper.writeValueAsString(funTransfer));
            // Write log end function
            paramsLog = new String[]{"END FUN TRANSFER TCB ONUS (" + objectMapper.writeValueAsString(funTransfer) + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME, TCBConstants.FUNCTION_FUN_TRANSFER, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    private Payment createPaymentData(ChannelFunction channelFunction, FunTransferRequest request) throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + objectMapper.writeValueAsString(request));
        paymentDTO.setMerchantCode(TCBConstants.PARTNER_ID);
        paymentDTO.setMerchantName(TCBConstants.PARTNER_ID);
        paymentDTO.setMerchantTransactionId(request.getTransId());
        paymentDTO.setAmount(request.getAmount());
        paymentDTO.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        Payment payment = paymentService.createPaymentDto(paymentDTO);
        return payment;
    }

    private Payment updatePaymentData(Payment payment, FunTransferResponse funTransfer) throws Exception {
        String responseStr = objectMapper.writeValueAsString(funTransfer);

        String[] split = funTransfer.getRspnSts().getAddtlStsRsnInf().split("\\|");

        payment.setChannelTransactionId(funTransfer.getTxInf().getRefId());
        payment.setChannelStatus(split[0].toString());
        payment.setChannelMessage(TCBConstants.getErrorMessage(split[0].toString()));
        payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        payment.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);

        return paymentService.updatePayment(payment);
    }

    @Override
    public PGResponse InqTransactionStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START GET TRANSACTION STATUS TCB (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME, TCBConstants.FUNCTION_GET_TRANSACTION_STATUS, true, true, false, paramsLog));

            InqListBankInfoRequest request = objectMapper.readValue(inputStr, InqListBankInfoRequest.class);
            // convert time long to datetime
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(request.getDate());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String transTime = sdf.format(calendar.getTime());
            String requestApi = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:v1=\"http://www.techcombank.com.vn/services/bank/collection/v1\">\n"
                    + "   <soapenv:Header />\n"
                    + "   <soapenv:Body>\n"
                    + "      <InqTxStsReq xmlns=\"http://www.techcombank.com.vn/services/bank/collection/v1\">\n"
                    + "         <ReqGnlInf>\n"
                    + "            <Id>" + request.getTransId() + "</Id>\n"
                    + "            <TxId>" + request.getTransId() + "</TxId>\n"
                    + "            <CreDtTm>" + transTime + "</CreDtTm>\n"
                    + "         </ReqGnlInf>\n"
                    + "         <Envt>\n"
                    + "            <TrgtPty>\n"
                    + "               <Nm>H2H</Nm>\n"
                    + "            </TrgtPty>\n"
                    + "            <SrcPty>\n"
                    + "               <Nm>" + TCBConstants.PARTNER_ID + "</Nm>\n"
                    + "            </SrcPty>\n"
                    + "            <Rqstr>\n"
                    + "               <Nm>" + TCBConstants.CHANNEL_CODE + "</Nm>\n"
                    + "            </Rqstr>\n"
                    + "         </Envt>\n"
                    + "         <ReqInf>\n"
                    + "         <ReqTxId>" + request.getTransId() + "</ReqTxId>\n"
                    + "            <Desc>" + request.getDescription() + "</Desc>\n"
                    + "         </ReqInf>\n"
                    + "      </InqTxStsReq>\n"
                    + "   </soapenv:Body>\n"
                    + "</soapenv:Envelope>";
            paramsLog = new String[]{"REQUEST GET TRANSACTION STATUS: Message - " + " (" + requestApi + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_GET_TRANSACTION_STATUS, true, true, false, paramsLog));
            // call to TCB
            String responsePartners = httpsPost(TCBConstants.URL_WSDL, requestApi);

            paramsLog = new String[]{"RESPONSE TCB ONUS: Message - " + " (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.FUNCTION_GET_TRANSACTION_STATUS, true, true, false, paramsLog));

            JSONObject xmlToJson = TCBUtils.xmlToJson(responsePartners);
            JSONObject object1 = xmlToJson.getJSONObject("soapenv:Envelope");
            JSONObject object2 = object1.getJSONObject("soapenv:Body");
            JSONObject object3 = object2.has("v1:InqTxStsRspn") ? object2.getJSONObject("v1:InqTxStsRspn") : null;
            JSONObject object4 = object3.has("v1:TxRcrd") ? object3.getJSONObject("v1:TxRcrd") : null;
            JSONObject object5 = object4.has("v1:ResResults") ? object4.getJSONObject("v1:ResResults") : null;

            InqTransactionStatusResponse res = null;
            if (object3 != null && object4 != null) {
                res = new InqTransactionStatusResponse(object5);
            }
            if (res != null) {
                pgResponse.setStatus(true);
                pgResponse.setErrorCode(res.getTxnSts());
                pgResponse.setMessage(res.getTxnDes());
                pgResponse.setData(objectMapper.writeValueAsString(res));
            }
            // Write log end function
            paramsLog = new String[]{"END GET LIST BANK TCB ONUS (" + objectMapper.writeValueAsString(res) + ")"};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME, TCBConstants.FUNCTION_GET_TRANSACTION_STATUS, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse InqBatchTxnStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String updateTransStatus(String req, ChannelFunction channelFunction) throws Exception {
        String[] paramsLog;
        final String stsReceive_Success = "RCCF";
        final String stsReceive_Error = "RCER";
        final String detailSts_Sucees = "000";
        final String detailSts_Error = "001";
        final String detailSts_ValidateFail = "002";
        String resForTCB = "";
        try {
            paramsLog = new String[]{"START : " + req};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));

            JSONObject xmltoJsonObj = XML.toJSONObject(req);
            // TODO: get info transaction status
            JSONObject updateStatusReq = xmltoJsonObj.getJSONObject("soapenv:Envelope")
                    .getJSONObject("soapenv:Body").getJSONObject("v1:UpdateStatusReq");
            UpdateStsTransactionReq updateStsTransactionReq = new UpdateStsTransactionReq(updateStatusReq);
            // TODO: verify sign
            String sSource = "UpdateStatus" + TCBConstants.PARTNER_ID + "." + updateStsTransactionReq.getTransId() + updateStsTransactionReq.getChannelPayment()
                    + updateStsTransactionReq.getTransStatus() + updateStsTransactionReq.getStatusCodeTransaction();
            String pubKeyPath = TCBConstants.PUBLIC_KEY_DECRYPT_UPDATESTS;
            String sSign = updateStsTransactionReq.getSign();
            if (!JavaSignSHA256_V2_1.verifySign_SHA256(sSource, pubKeyPath, sSign)) {
                paramsLog = new String[]{"Verify Sign Failed"};
                logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                        TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));
                resForTCB = buildResponseTCB(updateStsTransactionReq.getId(), updateStsTransactionReq.getTxId(), stsReceive_Error, detailSts_ValidateFail);
            } else {
                // TODO: push transaction status to merchant
                String urlApp = channelFunction.getUrl();
                paramsLog = new String[]{"PUSH TO APP - URL : " + urlApp + "BODY : " + objectMapper.writeValueAsString(updateStsTransactionReq)};
                logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                        TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));

                String responseApp = sendReqToApp(urlApp, HttpMethod.POST, objectMapper.writeValueAsString(updateStsTransactionReq));
//                String responseApp = "RCCF";

                // TODO: update transaction status on GW
                Payment paymentUpdate = paymentService.findByMerchantTransactionId(updateStsTransactionReq.getTransId());
                if (paymentUpdate != null) {
                    paymentUpdate.setRawResponse(req);
                    if (updateStsTransactionReq.getTransStatus().contains("COMPLETED")) {
                        paymentUpdate.setChannelStatus(updateStsTransactionReq.getTransStatus());
                        paymentUpdate.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                        paymentUpdate.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                    } else {
                        paymentUpdate.setChannelStatus(updateStsTransactionReq.getTransStatus());
                        paymentUpdate.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                        paymentUpdate.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                    }
                    if (responseApp.equals(stsReceive_Success)) {
                        paymentUpdate.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                    } else {
                        paymentUpdate.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                    }
                    paymentService.updatePayment(paymentUpdate);
                    paramsLog = new String[]{"RESPONSE UPDATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(paymentUpdate) + ")"};
                    logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                            TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));
                    resForTCB = buildResponseTCB(updateStsTransactionReq.getId(), updateStsTransactionReq.getTxId(), stsReceive_Success, detailSts_Sucees);
                } else {
                    paramsLog = new String[]{"OrderId not found " + " (" + updateStsTransactionReq.getTransId() + ")"};
                    logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                            TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));
                    resForTCB = buildResponseTCB(updateStsTransactionReq.getId(), updateStsTransactionReq.getTxId(), stsReceive_Error, detailSts_Error);
                }
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            paramsLog = new String[]{"ERROR  : " + e.getMessage()};
            logger.error(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));
            return e.getMessage();
        }
        paramsLog = new String[]{"END  : " + resForTCB.replace("\n", "")};
        logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));
        return resForTCB;
    }


    public static String httpsPost(String url, String input) throws Exception {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(new TrustAllStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                createSslCustomContext(), //for you this is builder.build()
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
        );
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory())
                .register("https", sslsf)
                .build();

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(2000);//max connection
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();
        HttpPost req = new HttpPost(url);

        req.setEntity(new StringEntity(input));
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(TCBConstants.USER_NAME, TCBConstants.PASSWORD);
        req.addHeader(new BasicScheme().authenticate(creds, req, null));
        CloseableHttpResponse response = null;

        response = httpclient.execute(req);
        HttpEntity entity = response.getEntity();
        System.out.println("AAAA: " + response.getStatusLine());
        /*EntityUtils.consume(entity);
            System.out.println( entity.toString());*/
 /*String responseString1 = new BasicResponseHandler().handleResponse(response);
            System.out.println("Response : " + responseString1);*/
        String responseString = EntityUtils.toString(entity, "UTF-8");
        System.out.println("BBBB: Response String: " + responseString);
        return responseString;

    }

    public static SSLContext createSslCustomContext() throws Exception {
        // Client keystore
        KeyStore cks = KeyStore.getInstance(TCBConstants.CLIENT_KEYSTORE_TYPE);

        ClassLoader classLoader = TCBServiceImpl.class.getClassLoader();
        cks.load(new FileInputStream(Objects.requireNonNull(classLoader.getResource(TCBConstants.CLIENT_KEYSTORE_PATH)).getFile()), TCBConstants.CLIENT_KEYSTORE_PASS.toCharArray());

        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(cks, TCBConstants.CLIENT_KEYSTORE_PASS.toCharArray()) // load client certificate
                .build();
        return sslcontext;
    }

    private String sendReqToApp(String url, HttpMethod method, String bodyRequest) throws NoSuchAlgorithmException, KeyManagementException {
        String[] paramsLog;
        RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(bodyRequest, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, method, entity, String.class);
            paramsLog = new String[]{"RESPONSE APP  : " + response.getBody()};
            logger.info(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));
            JSONObject jsonObject = new JSONObject(response.getBody());
            return jsonObject.getString("updateTransactionSts");
        } catch (Exception exx) {
            exx.printStackTrace();
            paramsLog = new String[]{"ERROR  : " + exx.getMessage()};
            logger.error(commonLogService.createContentLog(TCBConstants.CHANNEL_CODE, TCBConstants.SERVICE_NAME,
                    TCBConstants.UPDATE_TRANSACTION_STATUS, true, true, false, paramsLog));
            return "RCER";
        }

    }

    private String buildResponseTCB(String id, String txId, String stsReceive, String detailSts) {

        String response = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "    <soapenv:Header/>\n" +
                "    <soapenv:Body>\n" +
                "        <v1:UpdateStatusRspn \n" +
                "xmlns:v1=\"http://www.techcombank.com.vn/services/bank/collection/v1\">\n" +
                "            <v1:RspnInf>\n" +
                "                <v1:Id>" + id + "</v1:Id>\n" +
                "                <v1:TxId>" + txId + "</v1:TxId>\n" +
                "                <v1:CreDtTm>" + LocalDateTime.now() + "</v1:CreDtTm>\n" +
                "                <v1:PmtTp/>\n" +
                "                <v1:Desc/>\n" +
                "                <v1:HeaderHashStr/>\n" +
                "                <v1:Sgntr>\n" +
                "                    <v1:Sgntr1/>\n" +
                "                    <v1:Sgntr2/>\n" +
                "                </v1:Sgntr>\n" +
                "            </v1:RspnInf>\n" +
                "            <v1:RspnSts>\n" +
                "                <v1:Sts>" + stsReceive + "</v1:Sts>\n" +
                "                <v1:AddtlStsRsnInf>" + detailSts + "</v1:AddtlStsRsnInf>\n" +
                "            </v1:RspnSts>\n" +
                "        </v1:UpdateStatusRspn>\n" +
                "    </soapenv:Body>\n" +
                "</soapenv:Envelope>";
        return response;
    }
}
