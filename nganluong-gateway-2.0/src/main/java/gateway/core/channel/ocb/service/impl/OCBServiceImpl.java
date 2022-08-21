package gateway.core.channel.ocb.service.impl;

import gateway.core.channel.PaymentGate;
import gateway.core.channel.cybersouce.service.impl.CybersourceServiceImpl;
import gateway.core.channel.ocb.dto.*;
import gateway.core.channel.ocb.dto.error.ErrorResponse;
import gateway.core.channel.ocb.dto.request.*;
import gateway.core.channel.ocb.dto.validate.PaymentRequestValidation;
import gateway.core.channel.ocb.service.OCBService;
import gateway.core.dto.PGResponse;
import gateway.core.util.aspect.Loggable;
import okhttp3.MediaType;
import okhttp3.*;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.ChannelFunctionService;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.RequestUtil;
import vn.nganluong.naba.utils.ResponseUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OCBServiceImpl extends PaymentGate implements OCBService {
    private static final Logger logger = LogManager.getLogger(OCBService.class);
    private static final String SERVICE_NAME = "PAYMENT";

    private final CommonPGResponseService commonPGResponseService;
    private final CommonLogService commonLogService;
    private final PaymentService paymentService;
    private final ChannelFunctionService channelFunctionService;

    @Autowired
    public OCBServiceImpl(CommonPGResponseService commonPGResponseService,
                          CommonLogService commonLogService,
                          PaymentService paymentService,
                          ChannelFunctionService channelFunctionService){
        this.commonPGResponseService = commonPGResponseService;
        this.commonLogService = commonLogService;
        this.paymentService = paymentService;
        this.channelFunctionService = channelFunctionService;

    }

    /**
     * File được lưu ở src/main/resources
     * khi cập nhật file *.crt hoặc *.key, chỉ việc ghi đè lên file đó.
     */
    private static final String CERTIFICATE_PATH = "OCB/Certificate.crt";
    private static final String PRIVATE_KEY_PATH = "OCB/privatekey.key";


    @Loggable
    @Override
    public PGResponse paymentStep1(ChannelFunction channelFunction, PaymentAccount paymentAccount, String requestData) {
        startOrEndMethodLogger(OCBConstants.FUNCTION_PAYMENT_STEP_1, true);

        try {
            PaymentStep1Req requestObject = objectMapper.readValue(requestData, PaymentStep1Req.class);

            if(requestObject.getTrace().getClientTransId() == null){
                return PGResponse.getInstanceWhenError(PGResponse.TRANSACTION_NULL);
            }
//            if(PaymentRequestValidation.checkStep1DTO(requestObject) !=null ){
//                return PGResponse.getInstanceWhenError(PGResponse.DATA_INVALID);
//            }

            JSONObject bodyReq = new JSONObject(requestObject);
            String url = RequestUtil.
                    createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(), channelFunction.getUrl());

            if(isTransactionExist(requestObject)){
                return PGResponse.getInstanceWhenError(PGResponse.TRANSACTION_EXIST);
            }

            String bodyRequest = bodyReq.toString();
            String signature = this.getDigitalSignature(bodyRequest);
            Response response = requestApi(url, signature, bodyRequest);    /* call http request */
            String responseStr = response.body().string();
            logger.info("Response Body: " + responseStr);
            logInfoWithTransId(requestObject.getTrace().getClientTransId(),
                    OCBConstants.FUNCTION_PAYMENT_STEP_1,
                    false,
                    responseStr);

            int httpCode = response.code();
            if(httpCode >= 200 && httpCode < 300){
                saveTransaction(channelFunction,requestObject, bodyRequest,responseStr);
            }

            startOrEndMethodLogger(OCBConstants.FUNCTION_PAYMENT_STEP_1, false);
            return pgResponseBuilder(response,responseStr);
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            String[] paramsLog = new String[] { e.getMessage() };
            logger.error(commonLogService.createContentLog(
                    OCBConstants.CHANNEL_CODE, SERVICE_NAME,
                    OCBConstants.FUNCTION_PAYMENT_STEP_1,
                    false,
                    true,
                    false,
                    paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Loggable
    @Override
    public PGResponse paymentStep2(ChannelFunction channelFunction, PaymentAccount paymentAccount, String input) {
        startOrEndMethodLogger(OCBConstants.FUNCTION_PAYMENT_STEP_2, true);
        try {
            PaymentStep2Req requestObject = objectMapper.readValue(input, PaymentStep2Req.class);

            if(requestObject.getTrace().getClientTransId() == null){
                return PGResponse.getInstanceWhenError(PGResponse.TRANSACTION_NULL);
            }
            if(PaymentRequestValidation.checkStep2DTO(requestObject) !=null ){
                return PGResponse.getInstanceWhenError(PGResponse.DATA_INVALID);
            }

            String url = RequestUtil.
                    createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(), channelFunction.getUrl());
            String bodyRequest = objectMapper.writeValueAsString(requestObject);
            String signature = getDigitalSignature(bodyRequest);
            Response response = requestApi(url, signature, bodyRequest);

            String responseString = response.body().string();
            PGResponse pgResponse = pgResponseBuilder(response,responseString); /* http request */
            logger.info("Response Body: " + responseString);
            this.saveTransactionResponse(requestObject,input,responseString); /* update payment */

            logInfoWithTransId(requestObject.getTrace().getClientTransId(),
                    OCBConstants.FUNCTION_PAYMENT_STEP_2,
                    false,
                    responseString);
            startOrEndMethodLogger(OCBConstants.FUNCTION_PAYMENT_STEP_2, false);
            return pgResponse;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            String[] paramsLog = new String[] { e.getMessage() };
            logger.error(commonLogService.createContentLog(
                    OCBConstants.CHANNEL_CODE, SERVICE_NAME,
                    OCBConstants.FUNCTION_PAYMENT_STEP_2,
                    false,
                    true,
                    false,
                    paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }

    }

    @Loggable
    @Override
    public PGResponse statusPayment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String input) {
        PGResponse pgResponse = new PGResponse();
        try {
            PaymentStatusReq req = objectMapper.readValue(input, PaymentStatusReq.class);
            if(req.getTrace().getClientTransId() == null){
                pgResponse.setStatus(false);
                pgResponse.setErrorCode("22");
                pgResponse.setMessage("client transaction id not empty");
                return pgResponse;
            }

            if(paymentService.findByMerchantTransactionId(req.getData().getPartnerTransId())==null){
                pgResponse.setStatus(false);
                pgResponse.setErrorCode("23");
                pgResponse.setMessage("Client transaction Id does not exist");
                return pgResponse;
            }


            if(PaymentRequestValidation.checkTransactionStatusDTO(req) !=null ){
                pgResponse.setStatus(false);
                pgResponse.setErrorCode("98");
                pgResponse.setMessage(PaymentRequestValidation.checkTransactionStatusDTO(req));
                return pgResponse;
            }

            JSONObject bodyReq = new JSONObject(req);
            String url = RequestUtil.
                    createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(), channelFunction.getUrl());
            String requestBody = bodyReq.toString();
            String sign = getDigitalSignature(requestBody);
            Response response = requestApi(url, sign, requestBody);
            String responseString = response.body().string();
            pgResponse = pgResponseBuilder(response,responseString);
            logger.info("RESPONSE BODY: " + responseString);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            String[] paramsLog = new String[] { e.getMessage() };
            logger.error(commonLogService.createContentLog(
                    OCBConstants.CHANNEL_CODE, SERVICE_NAME,
                    OCBConstants.FUNCTION_PAYMENT_STEP_1,
                    false,
                    true,
                    false,
                    paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
        return pgResponse;
    }

    /** Call back OCB to confirm transaction.
     *
     */
    private Response statusPayment(String transactionId){
        Response response = null;
        ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelId(OCBConstants.FUNCTION_PAYMENT_STATUS,OCBConstants.OCB_CHANNEL_ID);
        String url = RequestUtil.
                createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(), channelFunction.getUrl());

        PaymentStatusReq statusCheckReq = new PaymentStatusReq();
        statusCheckReq.setTrace(new Trace("nl0000000000000"+getClientTimestamp(),getClientTimestamp()));
        statusCheckReq.setData(new DataStep3(transactionId, OCBConstants.PARTNER_CODE));

        JSONObject bodyReq = new JSONObject(statusCheckReq);
        String jsonBody = bodyReq.toString();
        String sign = getDigitalSignature(jsonBody);
        try {
            response = requestApi(url,sign,jsonBody);
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return response;
    }

    @Loggable
    @Override
    public PGResponse resendOTP(ChannelFunction channelFunction, PaymentAccount paymentAccount, String request) {
        PGResponse pgResponse = new PGResponse();
        try{
            ResendOTP req = objectMapper.readValue(request,ResendOTP.class);
            if(req.getTrace().getClientTransId() == null){
                pgResponse.setStatus(false);
                pgResponse.setErrorCode("22");
                pgResponse.setMessage("client transaction id not empty");
                return pgResponse;
            }
            String url = RequestUtil.
                    createUrlChannelFunction(channelFunction.getHost(),channelFunction.getPort(), channelFunction.getUrl());
            String requestBody = objectMapper.writeValueAsString(req);
            String sign = getDigitalSignature(requestBody);
            Response response = requestApi(url,sign,requestBody);
            String responseString = response.body().string();
            pgResponse = pgResponseBuilder(response,responseString);
        } catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            String[] paramsLog = new String[] { e.getMessage() };
            logger.error(commonLogService.createContentLog(
                    OCBConstants.CHANNEL_CODE, SERVICE_NAME,
                    OCBConstants.FUNCTION_RESEND_OTP,
                    false,
                    true,
                    false,
                    paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
        return pgResponse;
    }

    @Loggable
    @Override
    public PGResponse checkTransHistory(ChannelFunction channelFunction, PaymentAccount paymentAccount, String request) {
        PGResponse pgResponse = new PGResponse();
        try{
            TransHistory req = objectMapper.readValue(request,TransHistory.class);
            if(req.getTrace().getClientTransId() == null){
                pgResponse.setStatus(false);
                pgResponse.setErrorCode("22");
                pgResponse.setMessage("client transaction id not empty");
                return pgResponse;
            }

            req.setTrace(new Trace(req.getTrace().getClientTransId(),getClientTimestamp()));
            String url = RequestUtil.
                    createUrlChannelFunction(channelFunction.getHost(),channelFunction.getPort(),channelFunction.getUrl());
            String requestBody = req.toString();
            String sign = getDigitalSignature(requestBody);
            Response response = requestApi(url,sign,requestBody);
            String responseString = response.body().string();
            pgResponse = pgResponseBuilder(response,responseString);
            logger.info("Response body: "+ responseString);
            return pgResponse;
        }catch(Exception e){
            String[] paramsLog = new String[] { e.getMessage() };
            logger.error(commonLogService.createContentLog(
                    OCBConstants.CHANNEL_CODE, SERVICE_NAME,
                    OCBConstants.FUNCTION_TRANSACTION_HISTORY,
                    false,
                    true,
                    false,
                    paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    /**
     * Cung cấp certificate để gửi kèm với request
     *
     * @return String
     * string certificate without header and footer
     */

    private String getCertificate() {
        return getContextInFile(getAbsoluteFilePath(CERTIFICATE_PATH));
    }

    /**
     * @param payload json string
     * @return string digitel signature with SHA256withRSA encode algorithm
     */

    private String getDigitalSignature(String payload) {
        String header = "{\"alg\":\"RS256\"}";

        byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);
        byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8);

        String stringToSign = Base64UrlEncode(headerBytes) + "." + Base64UrlEncode(payloadBytes);

        byte[] byteToSign = stringToSign.getBytes(StandardCharsets.UTF_8);

        String privateKey = getContextInFile(getAbsoluteFilePath(PRIVATE_KEY_PATH));
        byte[] keyBytes = Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8));

        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = keyFactory.generatePrivate(spec);

            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            privateSignature.initSign(privKey);
            privateSignature.update(byteToSign);
            byte[] signature = privateSignature.sign();

            return Base64UrlEncode(signature);
        } catch (Exception e) {
            logger.error(e.getMessage());
            logger.error(ExceptionUtils.getStackTrace(e));
            return "";
        }
    }

    /**
     * Đính kèm vào trường "clientTimestamp", method lấy thời gian hệ thống tại thời điểm được gọi.
     * *
     * *          yyyy:   năm hiện tại
     * *          MM:     tháng hiện tại theo định dạng chuôi số
     * *          dd:     ngày hiện thại
     * *          HH:     giờ hiện tại theo định dạng 24h
     * *          mm:     phút hiện tại
     * *          SSS:    mili giây hiện tại
     *
     * @return String
     * yyyyMMddHHmmssSSS
     */
    private String getClientTimestamp() {
        String pattern = "yyyyMMddHHmmssSSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String timestamp = simpleDateFormat.format(new Date());
        return timestamp;
    }

    private String getAbsoluteFilePath(String relativePath) {
        ClassLoader classLoader = CybersourceServiceImpl.class.getClassLoader();
        String absoluteFilePath = Objects.requireNonNull(classLoader.getResource(relativePath).getFile());
        return absoluteFilePath;
    }

    private String getContextInFile(String absolutePath) {
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(absolutePath));
            StringBuilder context = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                context.append(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return context.toString().replace("-----BEGIN CERTIFICATE-----", "")
                    .replace("-----END CERTIFICATE-----", "")
                    .replace("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replace("-----END RSA PRIVATE KEY-----", "")
                    .replace("\n", "")
                    .replace("null", "");
        } catch (IOException e) {
            e.getMessage();
        }
        return null;
    }

    private static String Base64UrlEncode(byte[] input) {
        String output = Base64.getEncoder().encodeToString(input);
        output = output.split("=")[0];
        output = output.replace("+", "-")
                .replace("/", "_");
        return output;
    }

    private String getAccessToken() {
        try {
            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);

            Map<String, String> mapBody = new HashedMap<>();
            mapBody.put("client_id", OCBConstants.CLIENT_ID);
            mapBody.put("client_secret", OCBConstants.CLIENT_SECRET);
            mapBody.put("grant_type", OCBConstants.GRANT_TYPE);
            mapBody.put("scope", OCBConstants.SCOPE);
            mapBody.put("username", OCBConstants.USERNAME);
            mapBody.put("password", OCBConstants.PASSWORD);
            String requestBody = getParameterString(mapBody);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            String url = OCBConstants.GET_TOKEN_URL;
            ResponseEntity<String> response;

            response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            JSONObject json = new JSONObject(response.getBody());

            return json.getString("access_token");

        } catch (HttpClientErrorException | HttpServerErrorException | NoSuchAlgorithmException | KeyManagementException e){
            logger.error(e.getMessage());
            return "";
        }
    }

    private Response requestApi(String url, String sign, String bodyRequest) throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS);

        OkHttpClient client = builder.build();

        okhttp3.MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, bodyRequest);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("x-ibm-client-id", OCBConstants.CLIENT_ID)
                .addHeader("x-ibm-client-secret", OCBConstants.CLIENT_SECRET)
                .addHeader("authorization", MessageFormat.format("Bearer {0}", this.getAccessToken()))
                .addHeader("x-signature", sign)
                .addHeader("content-type", "application/json")
                .addHeader("accept", "application/json")
                .addHeader("x-client-certificate", getCertificate())
                .build();
        logger.info("URL: "+url+", Headers: ["+request.headers() + "], Request Body: "+bodyRequest);
        Response response = client.newCall(request).execute();
        return response;
    }

    private String getParameterString(Map<String, String> map) {
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            paramString.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return paramString.substring(0, paramString.length() - 1);
    }

    private PGResponse pgResponseBuilder(Response response, String responseStr){
        PGResponse pgResponse = new PGResponse();
        if(response.code() < 300) {
            pgResponse.setStatus(true);
            pgResponse.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_SUCCESS);
            pgResponse.setChannelMessage("SUCCESS");
        }else {
            pgResponse.setStatus(false);
            pgResponse.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_REQUEST_FAIL);
        }

        pgResponse.setChannelErrorCode(Integer.toString(response.code()));
        pgResponse.setData(responseStr);

        if(Integer.toString(response.code()).equals("417")){
            String jsonError = pgResponse.getData().toString();
            ErrorResponse errorResponse = null;
            try {
                errorResponse = objectMapper.readValue(jsonError, ErrorResponse.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            pgResponse.setStatus(false);
            pgResponse.setErrorCode(ResponseUtil.ERROR_CODE_GATEWAY_DATA_INVALID);
            pgResponse.setChannelErrorCode(errorResponse.getError().getCode());
            pgResponse.setChannelMessage(errorResponse.getError().getDetails());
        }
        return pgResponse;
    }

    private boolean isTransactionExist(PaymentStep1Req request){
        Payment obj = paymentService.findByMerchantTransactionId(request.getTrace().getClientTransId());
        return obj != null;
    }

    private void saveTransaction(ChannelFunction channelFunction,PaymentStep1Req request,String requestData, String responseData){
        PaymentDTO paymentDTO = PaymentDTO.builder()
                .channelId(channelFunction.getChannel().getId())
                .merchantCode("OCB")
                .merchantName("OCB")
                .merchantTransactionId(request.getTrace().getClientTransId())
                .amount(String.valueOf(request.getData().getTransferAmount()))
                .accountNo(request.getData().getCardNumber())
                .description(request.getData().getTransferDescription())
                .rawRequest("***Step 1***:"+requestData)
                .rawResponse(responseData).build();
        paymentService.createPayment(paymentDTO);
    }

    private void saveTransactionResponse(BaseRequest request,String requestData, String response){
        if (paymentService.findByMerchantTransactionId(request.getTrace().getClientTransId())==null){
            return;
        }
        Payment payment = paymentService.findByMerchantTransactionId(request.getTrace().getClientTransId());

        payment.setRawRequest(payment.getRawRequest() + "***Step 2***:"+requestData);
        payment.setRawResponse(payment.getRawResponse() + "***Step 2***:"+response);

        //write response step 2
        Map<String,String> mapStep2 = responseStringToMap(response);
        payment.setChannelTransactionId(mapStep2.get("bankRefNo"));
        payment.setChannelTransactionSeq(mapStep2.get("bankTransactionId"));

        //Double check Payment status
        Response responseCheckStatus = statusPayment(mapStep2.get("clientTransId"));
        payment.setRawRequest(payment.getRawRequest() + "***Status***:"+"request");
        try {
            String checkStatus = responseCheckStatus.body().string();
            logger.info("saveTransactionResponse Body: " + checkStatus);
            payment.setRawResponse(payment.getRawResponse() + "***Status***:"+checkStatus);
            Map<String,String> statusCheckMap = responseStringToMap(checkStatus);
            if(statusCheckMap.get("transactionStatus").equals("SUCCESS")){
                payment.setChannelTransactionStatus(1);
                payment.setPgTransactionStatus(1);
                payment.setMerchantTransactionStatus(1);
            }
        } catch (IOException e) {
                    e.printStackTrace();
        }
        paymentService.updatePayment(payment);
    }

    private Map<String,String> responseStringToMap(String response){
        Map<String,String> map = new HashMap<>();

        String flagSplit = response.contains("data") ? "data:" : "error:";
        String []something =  response.replace("{","").replace("}","").replace("\"","").replace("trace:","").split(flagSplit);
        if(something.length > 1){
            String[] input = something[0].concat(something[1]).split(",");
            for(String s: input){
                if(s.split(":").length > 1)
                    map.put(s.split(":")[0],s.split(":")[1]);
            }
        }
        return map;
    }
    private void startOrEndMethodLogger(String function, boolean isStartMethod){
        logger.info(commonLogService.createContentLogStartEndFunction(
                OCBConstants.CHANNEL_CODE,SERVICE_NAME,function,isStartMethod));
    }

    private void logInfoWithTransId(String transactionId, String function,boolean isRequest, String logBody){
        commonLogService.logInfoWithTransId(logger,transactionId,
                commonLogService.createContentLog(OCBConstants.CHANNEL_CODE, OCBConstants.SERVICE_NAME,
                        function,
                        true,
                        isRequest,
                        !isRequest,
                        new String[]{logBody}));
    }
}
