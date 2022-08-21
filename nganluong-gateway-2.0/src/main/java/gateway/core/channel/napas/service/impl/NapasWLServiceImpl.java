package gateway.core.channel.napas.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.napas.ClientRequest;
import gateway.core.channel.napas.dto.NapasConstans;
import gateway.core.channel.napas.dto.NapasPayV3Security;
import gateway.core.channel.napas.dto.WLConfig;
import gateway.core.channel.napas.dto.obj.Error;
import gateway.core.channel.napas.dto.obj.MerchantOrderInfo;
import gateway.core.channel.napas.dto.request.*;
import gateway.core.channel.napas.dto.response.*;
import gateway.core.channel.napas.service.NapasService;
import gateway.core.channel.napas.service.NapasWLService;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.*;
import vn.nganluong.naba.utils.RequestUtil;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Service
public class NapasWLServiceImpl extends PaymentGate implements NapasWLService {
    private static Integer channelId = -1;
    private static String channelName = "";
    private static final Logger logger = LogManager.getLogger(NapasService.class);
    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private CommonPGResponseService commonPGResponseService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PgFunctionService pgFunctionService;
    @Autowired
    private PaymentAccountService paymentAccountService;
    @Autowired
    private ChannelService channelService;


    @Override
    public PGResponse purchaseOtpWL(ChannelFunction channelFunction, PaymentAccount paymentAccount, String req) {
        String[] paramsLog;
        try {
            PurchaseOtpReq purchaseOtpReq = objectMapper.readValue(req, PurchaseOtpReq.class);
            WLConfig wlConfig = WLConfig.getConfigService(purchaseOtpReq.getMerchantCode());
            // Write log start function
            paramsLog = new String[]{"START : (" + req + ")" + "MID : " + objectMapper.writeValueAsString(wlConfig)};
            logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                    NapasConstans.FUNTION_PURCHASE_OTP_WL, true, true, false, paramsLog));
            String transId = purchaseOtpReq.getTransaction_id();
            String orderId = purchaseOtpReq.getOrder_id();
            if (org.springframework.util.StringUtils.isEmpty(orderId) || org.springframework.util.StringUtils.isEmpty(transId)) {
                paramsLog = new String[]{"Trans ID or Order ID is not empty"};
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                        NapasConstans.FUNTION_PURCHASE_OTP_WL, false, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
            } else {
                Payment checkTrans = paymentService.findByMerchantTransactionId(transId);
                Payment checkOrder = paymentService.findByChannelTransactionId(orderId);
                if (checkTrans != null) {
                    paramsLog = new String[]{"Transaction id already exist (" + transId + ")"};
                    logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                            NapasConstans.FUNTION_PURCHASE_OTP_WL, false, false, true, paramsLog));
                    return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
                }
                if (checkOrder != null) {
                    paramsLog = new String[]{"Order id already exist (" + orderId + ")"};
                    logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                            NapasConstans.FUNTION_PURCHASE_OTP_WL, false, false, true, paramsLog));
                    return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
                }
            }

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("apiOperation", "PURCHASE_OTP");
            bodyRequest.put("channel", purchaseOtpReq.getChannel());

            JSONObject order = new JSONObject();
            order.put("amount", purchaseOtpReq.getOrder_amount());
            order.put("currency", purchaseOtpReq.getOrder_currency());
            if (purchaseOtpReq.getSubmerchant() != null) {
                JSONObject submerchant = new JSONObject();
                submerchant.put("name", purchaseOtpReq.getSubmerchant().getName());
                submerchant.put("code", purchaseOtpReq.getSubmerchant().getCode());
                submerchant.put("referenceId", purchaseOtpReq.getSubmerchant().getReferenceId());
                bodyRequest.put("submerchant", submerchant);
            } else {
                return commonPGResponseService.returnBadGatewayWithCause("Submerchant Empty").getBody();
            }

            bodyRequest.put("order", order);

            JSONObject sourceOfFunds = new JSONObject();
            sourceOfFunds.put("type", purchaseOtpReq.getFund_type());
            JSONObject provided = new JSONObject();
            JSONObject card = new JSONObject();
            card.put("nameOnCard", purchaseOtpReq.getName_on_card());
            card.put("issueDate", purchaseOtpReq.getIssue_date());
            card.put("number", purchaseOtpReq.getCard_number());
            provided.put("card", card);
            sourceOfFunds.put("provided", provided);
            bodyRequest.put("sourceOfFunds", sourceOfFunds);

            JSONObject inputParameters = new JSONObject();
            inputParameters.put("clientIP", purchaseOtpReq.getClient_ip());
            inputParameters.put("deviceId", purchaseOtpReq.getDevice_id());
            inputParameters.put("environment", purchaseOtpReq.getEnvironment());
            inputParameters.put("cardScheme", purchaseOtpReq.getCard_scheme());
            inputParameters.put("enable3DSecure", String.valueOf(purchaseOtpReq.isEnable_3d_secure()));
            bodyRequest.put("inputParameters", inputParameters);

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
            //verify auth
            String accessToken = getTokenApi(0, NapasConstans.AUTH_KEY, wlConfig.getMid(),
                    wlConfig.getClientSecret(), wlConfig.getUser(), wlConfig.getPass());
            if (StringUtils.isNotEmpty(accessToken)) {
//                WriteInfoLog("AccessToken = " + accessToken);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
                headers.add("Authorization", "Bearer " + accessToken);

                HttpEntity<String> entity = new HttpEntity<>(bodyRequest.toString(), headers);
                String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                        channelFunction.getPort(), channelFunction.getUrl());
                ///build uri
                UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                        .pathSegment(wlConfig.getMid(), "order", orderId, "transaction", transId);
                //send request
                paramsLog = new String[]{"REQUEST - URL: " + builderUri.toUriString() + "- BODY: [" + bodyRequest.toString() + "]"};
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                        NapasConstans.FUNTION_PURCHASE_OTP_WL, true, true, false, paramsLog));
                ResponseEntity<PurchaseOtpRes> response = restTemplate.exchange(builderUri.toUriString(), HttpMethod.PUT, entity, PurchaseOtpRes.class);
                PurchaseOtpRes responseBody = response.getBody();
                paramsLog = new String[]{"RESPONSE : " + objectMapper.writeValueAsString(responseBody)};
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                        NapasConstans.FUNTION_PURCHASE_OTP_WL, true, false, true, paramsLog));

                //buil response
                PGResponse pgResponse = new PGResponse();
                pgResponse.setStatus(true);
                String result = response.getBody().getResult();
                if (StringUtils.isNotEmpty(result) && (result.equals("SUCCESS"))) {
                    pgResponse.setChannelMessage(responseBody.getResponse().getGatewayCode());
                    pgResponse.setErrorCode(response.getBody().getResult());
                } else if (StringUtils.isNotEmpty(result) && !(result.equals("SUCCESS"))) {
                    if (responseBody.getResponse() != null) {
                        pgResponse.setChannelMessage(responseBody.getResponse().getGatewayCode());
                    } else {
                        pgResponse.setErrorCode(result);
                        pgResponse.setChannelErrorCode(responseBody.getError().getCause());
                        pgResponse.setChannelMessage(responseBody.getError().getExplanation());
                    }
                    pgResponse.setErrorCode(response.getBody().getResult());
                } else {
                    pgResponse.setErrorCode("ERROR");
                    pgResponse.setChannelErrorCode(response.getBody().getPaymentResult().getError().getCause());
                    pgResponse.setChannelMessage(response.getBody().getPaymentResult().getError().getExplanation());
                }
                pgResponse.setData(responseBody);
                paramsLog = new String[]{"END  " + objectMapper.writeValueAsString(pgResponse)};

                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL,
                        NapasConstans.SERVICE_NAME_WL, NapasConstans.FUNTION_PURCHASE_OTP_WL, true, true, false, paramsLog));
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                        NapasConstans.FUNTION_PURCHASE_OTP_WL, true, true, false, paramsLog));
                return pgResponse;
            } else {
                return commonPGResponseService.returnBadGatewayWithCause("AccessToken Incorrect").getBody();
            }
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse refundDomesticWL(ChannelFunction channelFunction, String inputStr) throws
            IOException, NoSuchAlgorithmException, KeyManagementException {
        String paramsLog[];

        // Write log start function
        paramsLog = new String[]{"START REFUND TRANSACTION NAPAS WL (" + inputStr + ")"};
        logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL,
                NapasConstans.SERVICE_NAME_WL, NapasConstans.FUNTION_REFUND_DOMESTIC_WL,
                true, true, false, paramsLog));

        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        WLConfig wlConfig = WLConfig.getConfigService(input.getMerchantCode());

        String orderId = input.getRefTransId();
        String transId = input.getTransId();

        Payment paymentTocheckExist = paymentService.findByMerchantTransactionId(transId);
        if (paymentTocheckExist != null) {
            paramsLog = new String[]{" TRANSACTION ID ALREADY EXIST (" + transId + ")"};
            logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                    NapasConstans.FUNTION_REFUND_DOMESTIC_WL, false, false, true, paramsLog));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
        }
        MerchantOrderInfo orderInfo = new MerchantOrderInfo();
        orderInfo.setAmount(df.format(input.getAmount()));
        orderInfo.setCurrency(input.getCurrencyCode());

        RefundDomesticReq req = new RefundDomesticReq();
        req.setApiOperation("REFUND_DOMESTIC");
        req.setTransaction(orderInfo);
        req.setChannel(input.getChannel());

        String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                channelFunction.getPort(), channelFunction.getUrl());
        String uriBuilder = UriComponentsBuilder.fromHttpUrl(url).
                pathSegment(wlConfig.getMid(), "order", orderId, "transaction", transId).toUriString();

        //create payment refund
        Payment paymentRef = createPaymentData(transId, orderId, channelFunction, objectMapper.writeValueAsString(req));
        paymentRef.setRevertStatus(1);
        paymentRef.setAmount(new BigDecimal(input.getAmount()));
        paymentRef.setChannelTransactionId(orderId);
        paramsLog = new String[]{"RESPONSE CREATE PAYMENT REFUND NAPAS " + " (" + objectMapper.writeValueAsString(paymentRef) + ")"};
        logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                NapasConstans.FUNTION_REFUND_DOMESTIC_WL, true, true, false, paramsLog));
        //verify auth
        String accessToken = getTokenApi(0, NapasConstans.AUTH_KEY, wlConfig.getMid(),
                wlConfig.getClientSecret(), wlConfig.getUser(), wlConfig.getPass());
        PGResponse pgResponse = processReq(uriBuilder, objectMapper.writeValueAsString(req), MediaType.APPLICATION_JSON,
                HttpMethod.PUT.name(), new TypeReference<RefundDomesticRes>() {
                }, NapasConstans.FUNTION_REFUND_DOMESTIC_WL, accessToken, wlConfig.getMid());
        paramsLog = new String[]{"END REFUND TRANSACTION NAPAS: " + objectMapper.writeValueAsString(pgResponse)};
        logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL,
                NapasConstans.SERVICE_NAME_WL, NapasConstans.FUNTION_REFUND_DOMESTIC_WL,
                true, true, false, paramsLog));
        paymentRef.setRawResponse(objectMapper.writeValueAsString(pgResponse.getData()));
        if (pgResponse.getErrorCode().equals("SUCCESS")) {
            paymentRef.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentRef.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
//            paymentRef.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        } else if (pgResponse.getErrorCode().equals("ERROR")) {
            paymentRef.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            paymentRef.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
//            paymentRef.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
        } else {
            paymentRef.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            paymentRef.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
//            paymentRef.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        }
        paymentService.updatePayment(paymentRef);
        return pgResponse;
    }

    @Override
    public PGResponse queryTransDomesticWL(ChannelFunction channelFunction, String inputStr) throws
            IOException, NoSuchAlgorithmException, KeyManagementException {
        PGResponse pgResponse = new PGResponse();
        String[] paramsLog;

        // Write log start function
        paramsLog = new String[]{"START QUERY TRANSACTION NAPAS (" + inputStr + ")"};
        logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL,
                NapasConstans.SERVICE_NAME_WL, NapasConstans.FUNTION_QUERY_TRANSACTION_WL,
                true, true, false, paramsLog));

        QueryTransDomesticReq req = objectMapper.readValue(inputStr, QueryTransDomesticReq.class);

        WLConfig wlConfig = WLConfig.getConfigService(req.getMerchantCode());

        String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                channelFunction.getPort(), channelFunction.getUrl());
        String uriBuilder = UriComponentsBuilder.fromHttpUrl(url).
                pathSegment(wlConfig.getMid(), "order", req.getOrderId(), "domestic").toUriString();
        //verify auth
        String accessToken = getTokenApi(0, NapasConstans.AUTH_KEY, wlConfig.getMid(),
                wlConfig.getClientSecret(), wlConfig.getUser(), wlConfig.getPass());
        pgResponse = processReq(uriBuilder, null, MediaType.APPLICATION_JSON, HttpMethod.GET.name(),
                new TypeReference<QueryTransDomesticRes>() {
                }, NapasConstans.FUNTION_QUERY_TRANSACTION_WL, accessToken, wlConfig.getMid());
//        }

        paramsLog = new String[]{"END QUERY TRANSACTION NAPAS: " + objectMapper.writeValueAsString(pgResponse)};
        logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL,
                NapasConstans.SERVICE_NAME_WL, NapasConstans.FUNTION_QUERY_TRANSACTION_WL,
                true, true, false, paramsLog));

        return pgResponse;

    }

    @Override
    public String returnUrlPurchaseOtpWL(String napasResult) {
        String[] paramsLog = new String[]{"RETURN URL RESULT : " + napasResult};
        logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                NapasConstans.FUNTION_RETURN_URL_WL, true, true, false, paramsLog));
        try {
            ResultFromNapasReq req;
            if (!napasResult.contains("napasResult")) throw new Exception("REQUEST FAIL");

            JSONObject jsonObject = new JSONObject(napasResult);
            String body = jsonObject.getString("napasResult");
            req = objectMapper.readValue(body, ResultFromNapasReq.class);

            String result = new String(Base64.decodeBase64(req.getData()));

            PurchaseOtpRes purchaseOtpRes = objectMapper.readValue(result, PurchaseOtpRes.class);
            WLConfig wlConfig = WLConfig.getConfigService(purchaseOtpRes.getPaymentResult().getMerchantId());
            String checksum = NapasPayV3Security.checksumSha256(req.getData(), wlConfig.getClientSecret());
            if (!checksum.equalsIgnoreCase(req.getChecksum())) {
                throw new Exception("CHECKSUM INVALID");
            }


//            Payment payment = paymentService.findByChannelTransactionId(purchaseOtpRes.getOrder().getId());
            Payment payment = paymentService.findByMerchantTransactionId(purchaseOtpRes.getPaymentResult().getOrder().getId());
            if(payment == null) throw new Exception("TRANSACTION DOSE NOT EXIST");
            paramsLog = new String[]{"RETURN URL RESULT : SUCCESS " + "AND METHOD ENDED"};
            logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                    NapasConstans.FUNTION_RETURN_URL_WL, true, true, false, paramsLog));
            return "true";
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "false";
    }

    @Override
    public PGResponse ipnPurchaseOtpWL(String napasResult) throws Exception {
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START GET RESULT PURCHASE OTP :" + napasResult};
            logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                    NapasConstans.FUNTION_IPN_WL, true, true, false, paramsLog));
            ResultFromNapasReq req;
            String reqApp = napasResult;
            if (!napasResult.contains("napasResult")) {
                req = objectMapper.readValue(napasResult, ResultFromNapasReq.class);
            } else {
                napasResult = URLDecoder.decode(napasResult);
                napasResult = napasResult.substring(napasResult.indexOf("{"));
                req = objectMapper.readValue(napasResult, ResultFromNapasReq.class);
            }
            String result = new String(Base64.decodeBase64(req.getData()));
            PurchaseOtpRes purchaseOtpRes = objectMapper.readValue(result, PurchaseOtpRes.class);
            WLConfig wlConfig = WLConfig.getConfigService(purchaseOtpRes.getPaymentResult().getMerchantId());
            // validate check sum
            String checksumWL = NapasPayV3Security.checksumSha256(req.getData(), wlConfig.getClientSecret());
            if (!checksumWL.equalsIgnoreCase(req.getChecksum())) {
                paramsLog = new String[]{"CHECKSUM NAPAS FAILED" + napasResult};
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                        NapasConstans.FUNTION_IPN_WL, true, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause("NAPAS NOTIFY WRONG CHECKSUM").getBody();
            } else {
                paramsLog = new String[]{"RESULT PURCHASE OTP DECODE : " + objectMapper.writeValueAsString(result)};
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                        NapasConstans.FUNTION_IPN_WL, true, false, true, paramsLog));
                if(channelId == -1){
                    Channel channel = channelService.findByName("NAPAS_WL");
                    channelId = channel.getId();
                    channelName = channel.getName();
                }
                String orderId = purchaseOtpRes.getPaymentResult().getOrder().getId();
                PaymentDTO paymentCheck = new PaymentDTO();
                paymentCheck.setRawResponse(result);
                paymentCheck.setChannelId(channelId);
                paymentCheck.setMerchantCode(channelName);
                paymentCheck.setMerchantName(channelName);
                paymentCheck.setMerchantTransactionId(orderId);
                paymentCheck.setChannelTransactionId(orderId);
                paymentCheck.setChannelStatus(purchaseOtpRes.getPaymentResult().getResponse().getGatewayCode());
                paymentCheck.setChannelMessage(purchaseOtpRes.getPaymentResult().getResponse().getMessage());
                paymentCheck.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                paymentCheck.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                paymentCheck.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                paymentCheck.setAmount(purchaseOtpRes.getPaymentResult().getTransaction().getAmount());
                paymentService.createPayment(paymentCheck);
                paramsLog = new String[]{"RESPONSE UPDATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(paymentCheck) + ")"};
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                            NapasConstans.FUNTION_IPN_WL, true, true, false, paramsLog));
                //call to NGANLUONG
                PaymentAccount paymentAccount = paymentAccountService.getPaymentAccountByMerchantCode(paymentCheck.getMerchantCode());
                String url = paymentAccount.getUrlConfirm();
                String resApp = notifyResultToNGL(reqApp, url);
                paramsLog = new String[]{"RESPONSE APP (" + resApp + ")"};
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                            NapasConstans.FUNTION_IPN_WL, false, false, true, paramsLog));
                PGResponse pgResponse = new PGResponse();
                pgResponse.setStatus(true);
                return pgResponse;
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            paramsLog = new String[]{"ERROR: " + e.getMessage()};
            logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                    NapasConstans.FUNTION_IPN_WL, true, true, false, paramsLog));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    private String notifyResultToNGL(String data, String url) {
        String[] paramsLog;
        try {
            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.add("Accept", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(data, headers);
            ResponseEntity<String> response;
            try {
                paramsLog = new String[]{"NOTIFY TO APP - URL: " + url + "- DATA: " + data};
                logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                        NapasConstans.FUNTION_IPN_WL, true, true, false, paramsLog));
                response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                return response.getBody();
            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                response = new ResponseEntity<>(exx.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
                return response.getBody();
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            return e.getMessage();
        }
    }

    private String getTokenApi(int timeLogin, String authKey, String merchantId, String secretEncyptKey, String user, String pass)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        GetTokenReq req = new GetTokenReq();
        req.setGrantType(authKey);
        req.setClientId(merchantId);
        req.setClientSecret(secretEncyptKey);
        req.setUserName(user);
        req.setPassword(pass);

        WriteInfoLog("NAPAS GET TOKEN REQUEST TIME: ", objectMapper.writeValueAsString(req));
        ApiResponse apiRes = ClientRequest.sendRequest(NapasConstans.URL_GET_TOKEN_NAPAS,
                req.formatGetReq(), null, MediaType.APPLICATION_FORM_URLENCODED, "POST");
        WriteInfoLog("NAPAS GET TOKEN RESPONSE TIME: " + timeLogin, objectMapper.writeValueAsString(apiRes));
        GetTokenRes res = objectMapper.readValue(apiRes.getResponse(), GetTokenRes.class);

        if (StringUtils.isNotBlank(res.getAccessToken())) {
            return res.getAccessToken();
        }
        return "";
    }

    private <T extends BaseResponse> PGResponse processReq(String url, String req, String mediaType, String method,
                                                           TypeReference<T> objRes, String function, String accessToken, String mid)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        String[] paramsLog;

        // dang nhap
//        String accessToken = getTokenApi(0, NapasConstans.AUTH_KEY, NapasConstans.MERCHANT_ID,
//                NapasConstans.CLIENT_SECRET_KEY, NapasConstans.USER_NAME, NapasConstans.PASSWORD);
        ApiResponse apiRes = null;
        paramsLog = new String[]{"REQUEST " + function + " - URL: " + url + "- BODY: [" + req + "]" + "- AccessToken" + accessToken};
        logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                function, true, true, false, paramsLog));
        if (accessToken != null && !accessToken.isEmpty()) {
            apiRes = ClientRequest.sendRequest(url, req, accessToken, mediaType, method);
        }
        paramsLog = new String[]{"RESPONSE " + function + ":" + objectMapper.writeValueAsString(apiRes)};
        logger.info(commonLogService.createContentLog(NapasConstans.CHANNEL_CODE_WL, NapasConstans.SERVICE_NAME_WL,
                function, true, false, true, paramsLog));
        if (apiRes.getHttpStatus() == HttpURLConnection.HTTP_OK) {
            PGResponse pgResponse = new PGResponse();
            JSONObject jsonObject = new JSONObject(apiRes.getResponse());
            String resultRes = jsonObject.getString("result");
            if (resultRes.equals("ERROR")) {
                pgResponse.setStatus(true);
                pgResponse.setErrorCode(resultRes);
                Error error = objectMapper.readValue(jsonObject.getJSONObject("error").toString(), Error.class);
                pgResponse.setData(error);
            } else {
                T napasRes = objectMapper.readValue(apiRes.getResponse(), objRes);
                napasRes.setMerchantId(mid);
                pgResponse.setStatus(true);
                pgResponse.setErrorCode(napasRes.getResult());
                pgResponse.setData(napasRes);
            }
            return pgResponse;
        } else if (apiRes.getHttpStatus() == HttpURLConnection.HTTP_UNAUTHORIZED
                || apiRes.getHttpStatus() == HttpURLConnection.HTTP_FORBIDDEN) {
            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(false);
            pgResponse.setErrorCode("FAIL");
            pgResponse.setMessage(objectMapper.writeValueAsString(apiRes));
            return pgResponse;
        }
        return commonPGResponseService.returnBadGatewayWithCause("Cannot request Napas").getBody();
    }

    private Payment createPaymentData(String transId, String orderId, ChannelFunction channelFunction,
                                      String request) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + request);
        paymentDTO.setMerchantTransactionId(transId);
        paymentDTO.setChannelTransactionId(orderId);
        paymentDTO.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        return paymentService.createPaymentDto(paymentDTO);
    }

    private Payment createPaymentNapas(ChannelFunction channelFunction, String transId, String orderId, String request) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + request);
        paymentDTO.setMerchantTransactionId(transId);
        paymentDTO.setChannelTransactionId(orderId);
        paymentDTO.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        try {
            paymentDTO.setPgFunctionId(pgFunctionService.findPgFunctionByCode(channelFunction.getCode()).getId());
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return paymentService.createPaymentDto(paymentDTO);
    }
}
