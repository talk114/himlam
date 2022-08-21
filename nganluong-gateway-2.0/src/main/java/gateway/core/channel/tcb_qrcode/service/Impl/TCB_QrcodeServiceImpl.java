package gateway.core.channel.tcb_qrcode.service.Impl;

import com.github.mervick.aes_everywhere.legacy.Aes256;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.tcb_qrcode.TCB_QrCodeSecurity;
import gateway.core.channel.tcb_qrcode.dto.TCB_QrcodeConstants;
import gateway.core.channel.tcb_qrcode.dto.req.*;
import gateway.core.channel.tcb_qrcode.dto.res.*;
import gateway.core.channel.tcb_qrcode.service.TCB_QrcodeService;
import gateway.core.dto.PGResponse;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class TCB_QrcodeServiceImpl extends PaymentGate implements TCB_QrcodeService {
    private static final Logger logger = LogManager.getLogger(TCB_QrcodeServiceImpl.class);
    @Autowired
    private CommonLogService commonLogService;

    private static final String SERVICE_NAME = "ECOM";

    @Autowired
    private CommonPGResponseService commonPGResponseService;

    @Autowired
    private PaymentService paymentService;


    @Override
    public PGResponse GetQrBankCode(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        WriteInfoLog("Request GetQrBankCode " +inputStr);

        GetQrBankCodeReq getQrBankCodeReq = new GetQrBankCodeReq();
        getQrBankCodeReq.setService_type_code(TCB_QrcodeConstants.Service_type_code);
        getQrBankCodeReq.setService_code(TCB_QrcodeConstants.Service_code);
        getQrBankCodeReq.setMethod_code(TCB_QrcodeConstants.Method_code);
        BaseTCBQrCodeReq baseTCBQrCodeReq = new BaseTCBQrCodeReq();
        baseTCBQrCodeReq.setFnc(TCB_QrcodeConstants.GET_QR_BANK_CODE_ACTION);
        baseTCBQrCodeReq.setApp_id(TCB_QrcodeConstants.AppID);
        String data = Aes256.encrypt(objectMapper.writeValueAsString(getQrBankCodeReq),TCB_QrcodeConstants.Secret_key);

        WriteInfoLog("data " +data);
        String dataChecksum = TCB_QrcodeConstants.AppID + data + TCB_QrcodeConstants.Secret_key;
        baseTCBQrCodeReq.setData(data);
        String checksum = TCB_QrCodeSecurity.MD5(dataChecksum);

        WriteInfoLog("checksum " +checksum);

        baseTCBQrCodeReq.setChecksum(checksum);
        String body = objectMapper.writeValueAsString(baseTCBQrCodeReq);

        WriteInfoLog("body " +body);
        URL url = new URL(TCB_QrcodeConstants.Url_GetQrBankCode);
        String response = callApi(url,body);

        System.out.println("Response" +response);




        GetQrBankCodeRes getQrBankCodeRes = objectMapper.readValue(response,GetQrBankCodeRes.class);

        Object[] arrayData = getQrBankCodeRes.getData();

        Object o = arrayData[0];
        System.out.println(o);

        PGResponse pGResponse = new PGResponse();
        pGResponse.setStatus(true);
        pGResponse.setErrorCode(getQrBankCodeRes.getError_code());
        pGResponse.setMessage(getQrBankCodeRes.getError_message());


        return  null;



    }

    private String callApi(URL url, String body) throws  Exception {

        String userCredentials = TCB_QrcodeConstants.Username + ":" + TCB_QrcodeConstants.Password;
        String basicAuth = "Basic " + Base64.encodeBase64String(userCredentials.getBytes());

        SSLContext ssl_ctx = SSLContext.getInstance("TLS");
        TrustManager[] trust_mgr = get_trust_mgr();
        ssl_ctx.init(null, trust_mgr, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String string, SSLSession ssls) {
                return true;
            }
        });

        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", basicAuth);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        try (DataOutputStream wr = new DataOutputStream((con.getOutputStream()))) {
            wr.writeBytes(body);
            wr.flush();
        }

        StringBuilder response = new StringBuilder();

        InputStream is  = con.getInputStream();;
        int responseCode = con.getResponseCode();

        WriteInfoLog("responseCode " + responseCode);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(is))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }
        return response.toString();

    }

    @Override
    public PGResponse CreateQrCode(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {


        WriteInfoLog("Request CreateQrCode " +inputStr);

        CreateQRCodeReq req = objectMapper.readValue(inputStr,CreateQRCodeReq.class);
        String[] paramsLog = null;
        if (StringUtils.isBlank(req.getMerchant_request_id())) {
            paramsLog = new String[]{"requestID is empty"};
            logger.info(commonLogService.createContentLog(TCB_QrcodeConstants.CHANNEL_CODE, SERVICE_NAME,
                    TCB_QrcodeConstants.FUNCTION_CODE_CREATE_QR_CODE, false, false, true, paramsLog));

            logger.info(commonLogService.createContentLogStartEndFunction(TCB_QrcodeConstants.CHANNEL_CODE, SERVICE_NAME,
                    TCB_QrcodeConstants.FUNCTION_CODE_CREATE_QR_CODE, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
        } else {
            Payment paymentTocheckExist = paymentService
                    .findByMerchantTransactionId(req.getMerchant_request_id());
            if (paymentTocheckExist != null) {

                paramsLog = new String[]{"requestID already exist ("
                        + req.getMerchant_request_id() + ")"};
                logger.info(commonLogService.createContentLog(TCB_QrcodeConstants.CHANNEL_CODE, SERVICE_NAME,
                        TCB_QrcodeConstants.FUNCTION_CODE_CREATE_QR_CODE, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(TCB_QrcodeConstants.CHANNEL_CODE, SERVICE_NAME,
                        TCB_QrcodeConstants.FUNCTION_CODE_CREATE_QR_CODE, false));
                return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
            }
        }

        if (StringUtils.isNotEmpty(validateCreateQrCode(req))) {
            return createErrorResponse(validateCreateQrCode(req));
        }

        PaymentDTO paymentDTO = createPaymentReq(req);


        req.setService_type_code(TCB_QrcodeConstants.Service_type_code);
        req.setService_code(TCB_QrcodeConstants.Service_code);
        req.setMethod_code(TCB_QrcodeConstants.Method_code);
        req.setMerchant_id(TCB_QrcodeConstants.MerchantID);
        req.setUrl_notify(TCB_QrcodeConstants.Url_Notify);

        WriteInfoLog("dataEncode " +req);


        String data = Aes256.encrypt(objectMapper.writeValueAsString(req),TCB_QrcodeConstants.Secret_key);
        String test = Aes256.decrypt(data,TCB_QrcodeConstants.Secret_key);
        String dataChecksum = TCB_QrcodeConstants.AppID + data + TCB_QrcodeConstants.Secret_key;

        String checksum = TCB_QrCodeSecurity.MD5(dataChecksum);

        WriteInfoLog("data " +data);

        WriteInfoLog("checksum " +checksum);

        BaseTCBQrCodeReq baseTCBQrCodeReq = new BaseTCBQrCodeReq();
        baseTCBQrCodeReq.setFnc(TCB_QrcodeConstants.CREATE_QR_CODE_ACTION);
        baseTCBQrCodeReq.setApp_id(TCB_QrcodeConstants.AppID);
        baseTCBQrCodeReq.setData(data);
        baseTCBQrCodeReq.setChecksum(checksum);

        String body = objectMapper.writeValueAsString(baseTCBQrCodeReq);

        WriteInfoLog("body " +body);
        URL url = new URL(TCB_QrcodeConstants.Url_CreateQrCode);
        String response = callApi(url,body);
        WriteInfoLog("Response CreateQRCode "+response);

        PGResponse pgResponse = new PGResponse();

        BaseTCBQrcodeRes baseTCBQrcodeRes = objectMapper.readValue(response,BaseTCBQrcodeRes.class);
        if(baseTCBQrcodeRes.getData() != null) {
            Object object = baseTCBQrcodeRes.getData();

            String checksumRes = TCB_QrCodeSecurity.MD5(TCB_QrcodeConstants.AppID + objectMapper.writeValueAsString(object) + TCB_QrcodeConstants.Secret_key);
            System.out.println(checksumRes);
            CreateQRCodeRes createQRCodeRes = objectMapper.readValue(objectMapper.writeValueAsString(object), CreateQRCodeRes.class);

            pgResponse.setStatus(true);
            pgResponse.setErrorCode(baseTCBQrcodeRes.getError_code());
            pgResponse.setMessage(baseTCBQrcodeRes.getError_message());
            pgResponse.setData(createQRCodeRes.getQrcode_image());
            pgResponse.setDescription(objectMapper.writeValueAsString(object));

            WriteInfoLog("TCB_QRCODE Response CreateQRCode" + objectMapper.writeValueAsString(pgResponse));
        }else {

            pgResponse.setStatus(true);
            pgResponse.setErrorCode(baseTCBQrcodeRes.getError_code());
            pgResponse.setMessage(baseTCBQrcodeRes.getError_message());
            WriteInfoLog("TCB_QRCODE Response CreateQRCode" + objectMapper.writeValueAsString(pgResponse));
        }

        return pgResponse;
    }

    private PaymentDTO createPaymentReq(CreateQRCodeReq req) throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(22);
        paymentDTO.setAmount(String.valueOf(req.getAmount()));
        paymentDTO.setMerchantTransactionId(req.getMerchant_request_id());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + objectMapper.writeValueAsString(req));
        paymentService.createPayment(paymentDTO);
        return paymentDTO;
    }

    @Override
    public PGResponse CheckPayment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {

        WriteInfoLog("Request CheckPayment " +inputStr);
        CheckPaymentReq checkPaymentReq = objectMapper.readValue(inputStr,CheckPaymentReq.class);

        String data = Aes256.encrypt(objectMapper.writeValueAsString(checkPaymentReq),TCB_QrcodeConstants.Secret_key);
        String dataChecksum = TCB_QrcodeConstants.AppID + data + TCB_QrcodeConstants.Secret_key;
        String checksum = TCB_QrCodeSecurity.MD5(dataChecksum);
        BaseTCBQrCodeReq baseTCBQrCodeReq = new BaseTCBQrCodeReq();
        baseTCBQrCodeReq.setFnc(TCB_QrcodeConstants.CHECK_PAYMENT_ACTION);
        baseTCBQrCodeReq.setApp_id(TCB_QrcodeConstants.AppID);
        baseTCBQrCodeReq.setData(data);
        baseTCBQrCodeReq.setChecksum(checksum);

        String body = objectMapper.writeValueAsString(baseTCBQrCodeReq);

        WriteInfoLog("body " +body);
        URL url = new URL(TCB_QrcodeConstants.Url_CheckPayment);
        String response = callApi(url,body);

        BaseTCBQrcodeRes baseTCBQrcodeRes = objectMapper.readValue(response,BaseTCBQrcodeRes.class);
        Object object =  baseTCBQrcodeRes.getData();

        CheckPaymentRes checkPaymentRes = objectMapper.readValue(objectMapper.writeValueAsString(object),CheckPaymentRes.class);
        PGResponse pgResponse = new PGResponse();
        pgResponse.setStatus(true);
        pgResponse.setErrorCode(baseTCBQrcodeRes.getError_code());
        pgResponse.setMessage(baseTCBQrcodeRes.getError_message());
        pgResponse.setData(objectMapper.writeValueAsString(object));

        WriteInfoLog("TCB_QRCODE Response CheckPayment" + objectMapper.writeValueAsString(pgResponse));

        return pgResponse;


    }

    @Override
    public String NotifyTCB_QRCODE(String notify) throws Exception {
        NotifyQrCodeRes notifyQrCodeRes = objectMapper.readValue(notify,NotifyQrCodeRes.class);
        WriteInfoLog("TCB_QRCODE Notify Request " +notify);

        PGResponse pgResponse = new PGResponse();
        DataResponse dataResponse = notifyQrCodeRes.getData();

        String data =  objectMapper.writeValueAsString(dataResponse);
        String checksumRes = TCB_QrCodeSecurity.MD5(TCB_QrcodeConstants.AppID + data + TCB_QrcodeConstants.Secret_key);
        if(checksumRes.equals(notifyQrCodeRes.getChecksum())){
            WriteInfoLog("VERIFY CHECKSUM TRUE");
        }else {
            WriteErrorLog("VERIFY CHECKSUM FALSE");
            JSONObject res = new JSONObject();
            res.put("ErrorCode", "-6");
            res.put("Message", "Sai checksum");
            return res.toString();
        }
        // call Ngan luong
        Map<String, Object> map = new HashMap<>();
        map.put("params", data);
        String nganluongRes = HttpUtil.send(TCB_QrcodeConstants.Url_CallWeb, map, null);

        WriteInfoLog("TCB_QRCODE Notify Response " +nganluongRes);



        return nganluongRes;
    }


    public static PGResponse createErrorResponse(String message){
        PGResponse resp = new PGResponse();
        resp.setStatus(false);
        resp.setErrorCode("01");
        resp.setDescription(message);
        try {
            WriteInfoLog("TCB_QRCODE SendOrder CheckValidate" +resp.getDescription());
            return resp;
        } catch (Exception ex) {
            logger.info(ex.getMessage());
        }
        return null;
    }
    public static String validateCreateQrCode(CreateQRCodeReq createQRCodeReq){
        StringBuilder errors = new StringBuilder();
        String fullName = createQRCodeReq.getCustomer_fullname().replaceAll("[^a-zA-Z0-9]+","0");
        createQRCodeReq.setCustomer_fullname(fullName);

        if(!regexCheck(createQRCodeReq.getCustomer_email(),"^(.+)@(\\S+)$")){
            errors.append("Email is required;");
        }
        if (!regexCheck(createQRCodeReq.getCustomer_mobile(),"^[0-9]{1,10}$")) {
            errors.append("Mobile is required;");
        }
        if(fullName.length()>25||StringUtils.isEmpty(fullName)){
            errors.append("Fullname is required;");
        }
        if(createQRCodeReq.getAmount()<0){
            errors.append("Amount is required");
        }

        return errors.toString();

    }

    private static boolean regexCheck(String textValue, String patternValue){
        Pattern pattern = Pattern.compile(patternValue);
        Matcher matcher = pattern.matcher(textValue);
        return matcher.find();
    }

    public static TrustManager[] get_trust_mgr() {
        TrustManager[] certs = new TrustManager[] { new X509TrustManager() {

            @Override
            public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string)
                    throws CertificateException {
            }

            @Override
            public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string)
                    throws CertificateException {

            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }
        } };
        return certs;
    }
}
