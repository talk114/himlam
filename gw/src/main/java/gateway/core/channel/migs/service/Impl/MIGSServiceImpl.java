package gateway.core.channel.migs.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.migs.config.MIGSConfig;
import gateway.core.channel.migs.entity.BaseRequest;
import gateway.core.channel.migs.entity.input.Config;
import gateway.core.channel.migs.request.*;
import gateway.core.channel.migs.response.AuthenResponse;
import gateway.core.channel.migs.response.CheckEnrollmentRes;
import gateway.core.channel.migs.response.ProcessAcsResultRes;
import gateway.core.channel.migs.response.TransactionResponse;
import gateway.core.channel.migs.service.MIGSService;
import gateway.core.channel.migs.service.schedule.Payment_Rec;
import gateway.core.dto.PGResponse;
import gateway.core.util.aspect.Loggable;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
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

import java.io.IOException;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author sonln
 */
@Service
public class MIGSServiceImpl extends PaymentGate implements MIGSService {
    private static final Logger logger = LogManager.getLogger(MIGSServiceImpl.class);
    @Autowired
    private Config config;
    @Autowired
    private CommonPGResponseService commonPGResponseService;
    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private CommonPGResponseService commonResponseService;

    /**
     * 3DS2
     */
    @Loggable
    @Override
    public PGResponse initiateAuthentication(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws JsonProcessingException {
        String[] paramsLog;
        PGResponse pgResponse = new PGResponse();
        BaseRequest baseRequest = new BaseRequest();
        try {
            paramsLog = new String[]{"START INITIATE AUTHENTICATION MIGS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, true, true, false, paramsLog));

            InitiateAuthenticationReq authentication = objectMapper.readValue(inputStr, InitiateAuthenticationReq.class);
            String transId = authentication.getTransactionId();
            String orderId = authentication.getOrder().getId();
            if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(transId)) {
                paramsLog = new String[]{"Trans ID or Order ID is not empty"};
                logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                        MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, false, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
            } else {
                Payment checkTrans = paymentService.findByMerchantTransactionId(transId);
                Payment checkOrder = paymentService.findByChannelTransactionId(orderId);
                if (checkTrans != null) {
                    paramsLog = new String[]{"Transaction id already exist (" + transId + ")"};
                    logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                            MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, false, false, true, paramsLog));
                    return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
                }
                if (checkOrder != null) {
                    paramsLog = new String[]{"Order id already exist (" + orderId + ")"};
                    logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                            MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, false, false, true, paramsLog));
                    return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
                }
            }
            baseRequest.setOrderId(orderId);
            baseRequest.setTransactionId(transId);
            String url = getRequestUrl(config, baseRequest);
            if (StringUtils.isEmpty(url)) {
                return commonPGResponseService.returnBadGatewayWithCause("URL request Invalid").getBody();
            }
            authentication.getOrder().setId(null);
            authentication.setTransactionId(null);

            String bodyReq = objectMapper.writeValueAsString(authentication);
            // Create payment before call api into channel
            Payment payment = createPaymentData(transId, orderId, channelFunction, bodyReq);
            paramsLog = new String[]{"RESPONSE CREATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(payment) + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, true, true, false, paramsLog));

            paramsLog = new String[]{"REQUEST - URL: " + url + "- BODY: [" + bodyReq + "]"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, true, true, false, paramsLog));

            String responseString = requestApi(url, HttpMethod.PUT, bodyReq, config);

            paramsLog = new String[]{"RESPONSE : " + responseString};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, true, false, true, paramsLog));

            JSONObject response = new JSONObject(responseString);
            String result = response.getString("result");
            if (!"ERROR".equalsIgnoreCase(result)) {
                AuthenResponse initAuthenResponse = objectMapper.readValue(responseString, AuthenResponse.class);
                pgResponse.setStatus(true);
                String errorCode = response.getJSONObject("response").getString("gatewayCode");
                pgResponse.setErrorCode(errorCode);
                pgResponse.setMessage(MIGSConfig.getErrorMessage(errorCode));
                pgResponse.setData(initAuthenResponse);

                // update payment after call api into channel
                Payment paymentUpdate = updatePaymentData(payment, initAuthenResponse, errorCode, responseString);
                paramsLog = new String[]{"RESPONSE UPDATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(paymentUpdate) + ")"};
                logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                        MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, true, true, false, paramsLog));
            } else {
                JSONObject error = response.getJSONObject("error");
                pgResponse.setStatus(true);
                pgResponse.setErrorCode("99");
                pgResponse.setMessage(error.getString("cause"));
                pgResponse.setData("Gateway request fail: " + error.getString("explanation"));
            }

            paramsLog = new String[]{"END : " + objectMapper.writeValueAsString(pgResponse)};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_INITIATE_AUTHENTICATION, true, false, true, paramsLog));

            return pgResponse;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Loggable
    @Override
    public PGResponse authenticatePayer(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws IOException, NoSuchAlgorithmException, KeyManagementException {
        String[] paramsLog;
        PGResponse pgResponse = new PGResponse();
        BaseRequest baseRequest = new BaseRequest();
        Payment paymentTocheckExist;
        try {
            paramsLog = new String[]{"START AUTHENTICATE PAYER MIGS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_AUTHENTICATE_PAYER, true, true, false, paramsLog));

            PayerAuthenticationReq payerAuthenticationReq = objectMapper.readValue(inputStr, PayerAuthenticationReq.class);
            String transId = payerAuthenticationReq.getTransactionId();
            String orderId = payerAuthenticationReq.getOrder().getId();
            if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(transId)) {
                paramsLog = new String[]{"Trans ID or Order ID is not empty"};
                logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                        MIGSConfig.FUNTION_AUTHENTICATE_PAYER, false, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
            } else {
                paymentTocheckExist = paymentService.findByMerchantTransactionId(transId);
                if (paymentTocheckExist == null) {
                    paramsLog = new String[]{"Transaction id not exist (" + transId + ")"};
                    logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                            MIGSConfig.FUNTION_AUTHENTICATE_PAYER, false, false, true, paramsLog));
                    return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
                }
                if (!paymentTocheckExist.getChannelTransactionId().equals(orderId)) {
                    paramsLog = new String[]{"Order ID is not the same as the Initiate Authentication (" + orderId + ")"};
                    logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                            MIGSConfig.FUNTION_AUTHENTICATE_PAYER, false, false, true, paramsLog));
                    return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
                }
            }
            baseRequest.setTransactionId(transId);
            baseRequest.setOrderId(orderId);
            String url = getRequestUrl(config, baseRequest);
            if (StringUtils.isEmpty(url)) {
                return commonPGResponseService.returnBadGatewayWithCause("URL request Invalid").getBody();
            }
            payerAuthenticationReq.getOrder().setId(null);
            payerAuthenticationReq.setTransactionId(null);

            String bodyReq = objectMapper.writeValueAsString(payerAuthenticationReq);
            paymentTocheckExist.setRawRequest(bodyReq);
            paramsLog = new String[]{"REQUEST - URL: " + url + "- BODY: [" + bodyReq + "]"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_AUTHENTICATE_PAYER, true, true, false, paramsLog));
            String responseString = requestApi(url, HttpMethod.PUT, bodyReq, config);
            paymentTocheckExist.setRawResponse(responseString);

            paramsLog = new String[]{"RESPONSE : " + responseString};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_AUTHENTICATE_PAYER, true, false, true, paramsLog));

            JSONObject response = new JSONObject(responseString);
            String result = response.getString("result");
            if (!"ERROR".equalsIgnoreCase(result)) {
                AuthenResponse authenPayerResponse = objectMapper.readValue(responseString, AuthenResponse.class);
                pgResponse.setStatus(true);
                String errorCode = response.getJSONObject("response").getString("gatewayCode");
                pgResponse.setErrorCode(errorCode);
                pgResponse.setMessage(MIGSConfig.getErrorMessage(errorCode));
                pgResponse.setData(authenPayerResponse);

                // update payment after call api into channel
                Payment paymentUpdate = updatePaymentData(paymentTocheckExist, authenPayerResponse, errorCode, responseString);
                paramsLog = new String[]{"RESPONSE UPDATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(paymentUpdate) + ")"};
                logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                        MIGSConfig.FUNTION_AUTHENTICATE_PAYER, true, true, false, paramsLog));

            } else {
                JSONObject error = response.getJSONObject("error");
                pgResponse.setStatus(true);
                pgResponse.setErrorCode("99");
                pgResponse.setMessage(error.getString("cause"));
                pgResponse.setData("Gateway request fail: " + error.getString("explanation"));
            }

            paramsLog = new String[]{"END : " + objectMapper.writeValueAsString(pgResponse)};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_AUTHENTICATE_PAYER, true, false, true, paramsLog));

            return pgResponse;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Loggable
    @Override
    public PGResponse pay3DS2(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        String[] paramsLog;
        PGResponse pgResponse = new PGResponse();
        BaseRequest baseRequest = new BaseRequest();
        try {
            paramsLog = new String[]{"START PAY 3DS2 MIGS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS2, true, true, false, paramsLog));

            Payment3DS2Req payment = objectMapper.readValue(inputStr, Payment3DS2Req.class);

            baseRequest.setOrderId(payment.getTransaction().getReference());
            baseRequest.setTransactionId("1");
            String url = getRequestUrl(config, baseRequest);
            if (StringUtils.isEmpty(url)) {
                return commonPGResponseService.returnBadGatewayWithCause("URL request Invalid").getBody();
            }
            payment.setTransactionId(null);

            String bodyReq = objectMapper.writeValueAsString(payment);
            paramsLog = new String[]{"REQUEST - URL: " + url + "- BODY: [" + bodyReq + "]"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS2, true, true, false, paramsLog));

            String responseString = requestApi(url, HttpMethod.PUT, bodyReq, config);

            paramsLog = new String[]{"RESPONSE : " + responseString};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS2, true, false, true, paramsLog));

            JSONObject response = new JSONObject(responseString);
            if (!response.has("error")) {
                TransactionResponse transactionResponse = parseTransactionResponse(responseString);
                pgResponse.setStatus(true);
                String errorCode = response.getJSONObject("response").getString("gatewayCode");
                pgResponse.setErrorCode(errorCode);
                pgResponse.setMessage(MIGSConfig.getErrorMessage(errorCode));
                pgResponse.setData(transactionResponse);
            } else {
                JSONObject error = response.getJSONObject("error");
                pgResponse.setStatus(true);
                pgResponse.setErrorCode("99");
                pgResponse.setMessage(error.getString("cause"));
                pgResponse.setData("Gateway request fail: " + error.getString("explanation"));
            }

            paramsLog = new String[]{"END PAY 3DS2 MIGS : " + objectMapper.writeValueAsString(pgResponse)};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS2, true, false, true, paramsLog));

            return pgResponse;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    /**
     * 3DS1
     */
    @Loggable
    @Override
    public PGResponse check3DSEnrollment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        PGResponse pgResponse = new PGResponse();
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START CHECK 3DS ENROLLMENT (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_CHECK_3DS_ENROLLMENT, true, true, false, paramsLog));

            Check3DSEnrollmentReq check3DSEnrollmentReq = objectMapper.readValue(inputStr, Check3DSEnrollmentReq.class);
            String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            //https://test-gateway.mastercard.com/api/rest/version/59/merchant/165-M-123456/3DSecureId/0610112
            String builderUri = UriComponentsBuilder.fromHttpUrl(url)
                    .pathSegment("version", String.valueOf(config.getApiVersion()),
                            "merchant", config.getMerchantId(),
                            "3DSecureId", check3DSEnrollmentReq.get_3DSecureId())
                    .toUriString();

            JSONObject authenticationRedirect = new JSONObject().put("responseUrl", check3DSEnrollmentReq.getResponseUrl());
            JSONObject secure = new JSONObject().put("authenticationRedirect", authenticationRedirect);

            JSONObject order = new JSONObject()
                    .put("amount", check3DSEnrollmentReq.getOrderAmount())
                    .put("currency", check3DSEnrollmentReq.getOrderCurrency());

            JSONObject session = new JSONObject().put("id", check3DSEnrollmentReq.getSessionId());

            JSONObject body = new JSONObject();
            body.put("3DSecure", secure);
            body.put("apiOperation", check3DSEnrollmentReq.getApiOperation());
            body.put("order", order);
            body.put("session", session);

            paramsLog = new String[]{"REQUEST - URL: " + builderUri + "- BODY: [" + body.toString() + "]"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_CHECK_3DS_ENROLLMENT, true, true, false, paramsLog));

            String responseStr = requestApi(builderUri, HttpMethod.PUT, body.toString(), config);

            paramsLog = new String[]{"RESPONSE : " + responseStr};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_CHECK_3DS_ENROLLMENT, true, false, true, paramsLog));

            JSONObject response = new JSONObject(responseStr);
            if (!response.has("error")) {
                CheckEnrollmentRes checkEnrollmentRes = objectMapper.readValue(responseStr, CheckEnrollmentRes.class);
                String veResEnrolled = response.getJSONObject("3DSecure").getString("veResEnrolled");
                checkEnrollmentRes.setVeResEnrolled(veResEnrolled);
                pgResponse.setStatus(true);
                String errorCode = response.getJSONObject("response").getString("gatewayRecommendation");
                pgResponse.setErrorCode(errorCode);
                pgResponse.setMessage(MIGSConfig.getErrorMessage(errorCode));
                pgResponse.setData(checkEnrollmentRes);
            } else {
                JSONObject error = response.getJSONObject("error");
                pgResponse.setStatus(true);
                pgResponse.setErrorCode("99");
                pgResponse.setMessage(error.getString("cause"));
                pgResponse.setData("Gateway request fail: " + error.getString("explanation"));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        paramsLog = new String[]{"END CHECK 3DS ENROLLMENT : " + objectMapper.writeValueAsString(pgResponse)};
        logger.error(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                MIGSConfig.FUNTION_CHECK_3DS_ENROLLMENT, true, false, true, paramsLog));
        return pgResponse;
    }

    @Loggable
    @Override
    public PGResponse processAcsResult(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        PGResponse pgResponse = new PGResponse();
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START FUNTION PROCESS ACS RESULT (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PROCESS_ACS_RESULT, true, true, false, paramsLog));

            ProcessAcsResultReq processAcsResultReq = objectMapper.readValue(inputStr, ProcessAcsResultReq.class);

            String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            String builderUri = UriComponentsBuilder.fromHttpUrl(url)
                    .pathSegment("version", String.valueOf(config.getApiVersion()),
                            "merchant", config.getMerchantId(),
                            "3DSecureId", processAcsResultReq.get_3DSecureId())
                    .toUriString();

            String bodyReq = new JSONObject()
                    .put("apiOperation", processAcsResultReq.getApiOperation())
                    .put("3DSecure", new JSONObject().put("paRes", processAcsResultReq.getPaRes()))
                    .toString();

            paramsLog = new String[]{"REQUEST - URL: " + builderUri + "- BODY: [" + bodyReq + "]"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PROCESS_ACS_RESULT, true, true, false, paramsLog));

            String responseStr = requestApi(builderUri, HttpMethod.POST, bodyReq, config);

            paramsLog = new String[]{"RESPONSE : " + responseStr};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PROCESS_ACS_RESULT, true, false, true, paramsLog));

            JSONObject response = new JSONObject(responseStr);
            if (!response.has("error")) {
                ProcessAcsResultRes data = objectMapper.readValue(responseStr, ProcessAcsResultRes.class);
                pgResponse.setStatus(true);
                String errorCode = response.getJSONObject("response").getString("gatewayRecommendation");
                pgResponse.setErrorCode(errorCode);
                pgResponse.setMessage(MIGSConfig.getErrorMessage(errorCode));
                pgResponse.setData(data);
            } else {
                JSONObject error = response.getJSONObject("error");
                pgResponse.setStatus(true);
                pgResponse.setErrorCode("99");
                pgResponse.setMessage(error.getString("cause"));
                pgResponse.setData("Gateway request fail: " + error.getString("explanation"));
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        paramsLog = new String[]{"END FUNTION PROCESS ACS RESULT : " + objectMapper.writeValueAsString(pgResponse)};
        logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                MIGSConfig.FUNTION_PROCESS_ACS_RESULT, true, false, true, paramsLog));
        return pgResponse;
    }

    @Loggable
    @Override
    public PGResponse pay3DS1(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        String[] paramsLog;
        PGResponse pgResponse = new PGResponse();
        BaseRequest baseRequest = new BaseRequest();
        try {
            paramsLog = new String[]{"START PAY 3DS1 MIGS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS1, true, true, false, paramsLog));

            Payment3DS1Req payment = objectMapper.readValue(inputStr, Payment3DS1Req.class);

            String orderId = payment.getOrderId();
            String transId = payment.getTransId();
            if (StringUtils.isEmpty(orderId) || StringUtils.isEmpty(transId)) {
                paramsLog = new String[]{"Trans ID or Order ID is not empty"};
                logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                        MIGSConfig.FUNTION_PAY_3DS1, false, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
            } else {
                Payment checkTrans = paymentService.findByMerchantTransactionId(transId);
                Payment checkOrder = paymentService.findByChannelTransactionId(orderId);
                if (checkTrans != null) {
                    paramsLog = new String[]{"Transaction id already exist (" + transId + ")"};
                    logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                            MIGSConfig.FUNTION_PAY_3DS1, false, false, true, paramsLog));
                    return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
                }
                if (checkOrder != null) {
                    paramsLog = new String[]{"Order id already exist (" + orderId + ")"};
                    logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                            MIGSConfig.FUNTION_PAY_3DS1, false, false, true, paramsLog));
                    return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
                }
            }
            String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            String builderUri = UriComponentsBuilder.fromHttpUrl(url)
                    .pathSegment("version", String.valueOf(config.getApiVersion()),
                            "merchant", config.getMerchantId(),
                            "order", payment.getOrderId(),
                            "transaction", payment.getTransId())
                    .toUriString();

            String bodyReq = new JSONObject()
                    .put("apiOperation", payment.getApiOperation())
                    .put("3DSecureId", payment.get_3DSecureId())
                    .put("order", new JSONObject().put("reference", payment.getOrderReference()))
                    .put("session", new JSONObject().put("id", payment.getSessionId()))
                    .put("transaction", new JSONObject().put("reference", payment.getTransReference()))
                    .toString();

            // Create payment before call api into channel
            Payment payment3ds1 = createPaymentData(transId, orderId, channelFunction, bodyReq);
            payment3ds1.setMerchantName(paymentAccount.getMerchantName());
            payment3ds1.setMerchantCode(paymentAccount.getMerchantId());

            paramsLog = new String[]{"RESPONSE CREATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(payment) + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS1, true, true, false, paramsLog));

            paramsLog = new String[]{"REQUEST - URL: " + builderUri + "- BODY: [" + bodyReq + "]"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS1, true, true, false, paramsLog));

            String responseString = requestApi(builderUri, HttpMethod.PUT, bodyReq, config);

            paramsLog = new String[]{"RESPONSE : " + responseString};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS1, true, false, true, paramsLog));

            JSONObject response = new JSONObject(responseString);
            if (!response.has("error")) {
                TransactionResponse transactionResponse = this.parseTransactionResponse(responseString);
                pgResponse.setStatus(true);
                String errorCode = response.getJSONObject("response").getString("gatewayCode");
                pgResponse.setErrorCode(errorCode);
                pgResponse.setMessage(MIGSConfig.getErrorMessage(errorCode));
                pgResponse.setData(transactionResponse);
                //creata data reconciliation
                Payment_Rec payment_rec = parseResponseToPayment(responseString);
                JSONObject jsonPayRec = new JSONObject(payment_rec);
                // update payment after call api into channel
                payment3ds1.setDescription(jsonPayRec.toString());
                Payment paymentUpdate = updatePayment3DS1(payment3ds1, transactionResponse, errorCode, responseString);
                paramsLog = new String[]{"RESPONSE UPDATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(paymentUpdate) + ")"};
                logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                        MIGSConfig.FUNTION_PAY_3DS1, true, true, false, paramsLog));

            } else {
                JSONObject error = response.getJSONObject("error");
                pgResponse.setStatus(true);
                pgResponse.setErrorCode("99");
                pgResponse.setMessage(error.getString("cause"));
                try {
                    pgResponse.setData("Gateway request fail: " + error.getString("explanation") + " " + error.getString("field"));
                } catch (Exception e) {
                    pgResponse.setData("Gateway request fail: " + error.getString("explanation"));
                    logger.error(ExceptionUtils.getStackTrace(e));
                }
                payment3ds1.setRawResponse(responseString);
                paymentService.updatePayment(payment3ds1);
            }

            paramsLog = new String[]{"END PAY 3DS1 MIGS : " + objectMapper.writeValueAsString(pgResponse)};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_PAY_3DS1, true, false, true, paramsLog));

            return pgResponse;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Loggable
    @Override
    public String resultCheckEnroll(String paRes) {
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START GET RESULT CHECK 3DS :" + paRes};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_RESULT_CHECK_3DS, true, true, false, paramsLog));
            paRes = URLDecoder.decode(paRes);
            paramsLog = new String[]{"RESULT CHECK 3DS DECODE : " + objectMapper.writeValueAsString(paRes)};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_RESULT_CHECK_3DS, true, false, true, paramsLog));
            paramsLog = new String[]{"END RESULT CHECK CHECK 3DS "};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE,
                    MIGSConfig.SERVICE_NAME, MIGSConfig.FUNTION_RESULT_CHECK_3DS, true, true, false, paramsLog));

            return paRes;

        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return "";
        }
    }

    @Loggable
    @Override
    public PGResponse refundTrans(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        try {

            PGResponse pgResponse = new PGResponse();
            String[] paramsLog;
            RefundRequest request = objectMapper.readValue(inputStr, RefundRequest.class);

            paramsLog = new String[]{"START REFUND TRANSACTION MIGS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE,
                    MIGSConfig.FUNTION_REFUND_TRANSACTION, MIGSConfig.FUNTION_REFUND_TRANSACTION,
                    true, true, false, paramsLog));
            String transId = request.getTransId();
            String orderId = request.getOrderId();
            Payment paymentTocheckExist = paymentService.findByMerchantTransactionId(request.getTransId());
            if (paymentTocheckExist != null) {
                paramsLog = new String[]{"(" + transId + ")"};
                logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                        MIGSConfig.FUNTION_REFUND_TRANSACTION, false, false, true, paramsLog));
                return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
            }

            String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            String builderUri = UriComponentsBuilder.fromHttpUrl(url)
                    .pathSegment("version", String.valueOf(config.getApiVersion()),
                            "merchant", config.getMerchantId(),
                            "order", orderId,
                            "transaction", transId)
                    .toUriString();
            JSONObject bodyReq = new JSONObject().put("apiOperation", request.getApiOperation());
            JSONObject transaction = new JSONObject()
                    .put("amount", request.getAmount())
                    .put("currency", request.getCurrency());
            bodyReq.put("transaction", transaction);

            //create payment refund
            Payment paymentRef = createPaymentData(transId, orderId, channelFunction, bodyReq.toString());
            paymentRef.setChannelTransactionId(orderId);
            paymentRef.setMerchantName(paymentAccount.getMerchantName());
            paymentRef.setMerchantCode(paymentAccount.getMerchantId());

            paramsLog = new String[]{"RESPONSE CREATE PAYMENT REFUND MIGS " + " (" + objectMapper.writeValueAsString(paymentRef) + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_REFUND_TRANSACTION, true, true, false, paramsLog));

            paramsLog = new String[]{"REQUEST REFUND TRANSACTION MIGS - URL: " + builderUri + "- BODY: [" + bodyReq + "]"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_REFUND_TRANSACTION, true, true, false, paramsLog));

            String responseString = requestApi(builderUri, HttpMethod.PUT, bodyReq.toString(), config);

            paramsLog = new String[]{"RESPONSE REFUND TRANSACTION MIGS : " + responseString};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_REFUND_TRANSACTION, true, false, true, paramsLog));

            pgResponse.setStatus(true);
            JSONObject object = new JSONObject(responseString);
            pgResponse.setData(object.toMap());

            if (object.toMap().get("result").equals("SUCCESS")) {
                Payment_Rec payment_rec = parseResponseToPayment(responseString);
                pgResponse.setErrorCode("00");
                pgResponse.setMessage("SUCCESS");
                paymentRef.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                paymentRef.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                paymentRef.setDescription(objectMapper.writeValueAsString(payment_rec));
                if (NumberUtils.isParsable(request.getAmount())) {
                    if (request.getAmount().length() <= 12) {
                        paymentRef.setAmount(NumberUtils.createBigDecimal(request.getAmount()));
                    }
                }
                paymentRef.setRevertStatus(1);
            }
            paymentRef.setRawResponse(responseString);
            paymentService.updatePayment(paymentRef);
            paramsLog = new String[]{"RESPONSE UPDATE PAYMENT REFUND MIGS " + " (" + objectMapper.writeValueAsString(paymentRef) + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_REFUND_TRANSACTION, true, true, false, paramsLog));

            paramsLog = new String[]{"END REFUND TRANSACTION MIGS: " + objectMapper.writeValueAsString(pgResponse)};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE,
                    MIGSConfig.SERVICE_NAME, MIGSConfig.FUNTION_REFUND_TRANSACTION,
                    true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Loggable
    @Override
    public PGResponse queryTrans(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        try {
            PGResponse pgResponse = new PGResponse();
            String[] paramsLog;

            // Write log start function
            paramsLog = new String[]{"START QUERY TRANSACTION MIGS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE,
                    MIGSConfig.SERVICE_NAME, MIGSConfig.FUNTION_QUERY_TRANSACTION,
                    true, true, false, paramsLog));

            String orderId = new JSONObject(inputStr).getString("orderId");
            String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            String builderUri = UriComponentsBuilder.fromHttpUrl(url)
                    .pathSegment("version", String.valueOf(config.getApiVersion()),
                            "merchant", config.getMerchantId(),
                            "order", orderId)
                    .toUriString();

            paramsLog = new String[]{"REQUEST QUERY TRANSACTION MIGS - URL: " + builderUri};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_QUERY_TRANSACTION, true, true, false, paramsLog));

            String responseString = requestApi(builderUri, HttpMethod.GET, null, config);

            paramsLog = new String[]{"RESPONSE QUERY TRANSACTION MIGS : " + responseString};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE, MIGSConfig.SERVICE_NAME,
                    MIGSConfig.FUNTION_QUERY_TRANSACTION, true, false, true, paramsLog));


            pgResponse.setStatus(true);
            pgResponse.setErrorCode("00");
            pgResponse.setMessage("SUCCESS");
            JSONObject object = new JSONObject(responseString);
            pgResponse.setData(object.toMap());

            paramsLog = new String[]{"END QUERY TRANSACTION MIGS: " + objectMapper.writeValueAsString(pgResponse)};
            logger.info(commonLogService.createContentLog(MIGSConfig.CHANNEL_CODE,
                    MIGSConfig.SERVICE_NAME, MIGSConfig.FUNTION_QUERY_TRANSACTION,
                    true, true, false, paramsLog));

            return pgResponse;
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    private String getRequestUrl(Config config, BaseRequest request) {
        String url = config.getGatewayHost() + "/version/" + config.getApiVersion() + "/merchant/" + config.getMerchantId();
        if (StringUtils.isEmpty(request.getTransactionId()) || StringUtils.isEmpty(request.getOrderId())) {
            return "";
        } else {
            url += "/order/" + request.getOrderId();
            url += "/transaction/" + request.getTransactionId();
        }
        return url;
    }

    private TransactionResponse parseTransactionResponse(String response) {
        try {
            TransactionResponse transactionResponse = new TransactionResponse();
            JSONObject json = new JSONObject(response);
            JSONObject secure = (JSONObject) json.get("3DSecure");
            JSONObject order = (JSONObject) json.get("order");
            JSONObject trans = (JSONObject) json.get("transaction");
            JSONObject res = (JSONObject) json.get("response");
            JSONObject card = json.getJSONObject("sourceOfFunds").getJSONObject("provided").getJSONObject("card");
            transactionResponse.setResult(String.valueOf(json.get("result")));
            transactionResponse.setGatewayCode(String.valueOf(res.get("gatewayCode")));
            transactionResponse.setAcquirerCode(String.valueOf(res.get("acquirerCode")));
            transactionResponse.setAcquirerMessage(String.valueOf(res.get("acquirerMessage")));
            transactionResponse.setOrderAmount(String.valueOf(order.get("amount")));
            transactionResponse.setOrderCurrency(String.valueOf(order.get("currency")));
            transactionResponse.setOrderId(String.valueOf(order.get("id")));
            transactionResponse.setTransDatetime(String.valueOf(json.get("timeOfRecord")));
            transactionResponse.setAuthCode(trans.has("authorizationCode") ? String.valueOf(trans.get("authorizationCode")) : null);
            transactionResponse.setAccountNumber(String.valueOf(card.get("number")));
            transactionResponse.setRetRefNumber(String.valueOf(trans.get("receipt")));

            transactionResponse.setAcsEci(String.valueOf(secure.get("acsEci")));
            transactionResponse.setResponse(response);
            return transactionResponse;

        } catch (Exception e) {
            logger.error("Unable to parse pay response");
            throw e;
        }
    }

    private String requestApi(String url, HttpMethod method, String bodyRequest, Config config) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
        restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(
                config.getApiUsername(), config.getApiPassword()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(bodyRequest, headers);
        ResponseEntity<String> response;

        try {
            response = restTemplate.exchange(url, method, entity, String.class);
            String responseString = response.getBody();
            logger.info("RESPONSE BODY: " + responseString);
            return responseString;
        } catch (HttpClientErrorException | HttpServerErrorException exx) {
            response = new ResponseEntity<>(exx.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
            String responseString = response.getBody();
            logger.error(exx.getMessage());
            logger.error("RESPONSE BODY ERROR: "+ responseString);
            return responseString;
        }
    }

    private Payment createPaymentData(String transId, String orderId, ChannelFunction channelFunction,
                                      String request) {
        PaymentDTO paymentDTO = new PaymentDTO();
//        paymentDTO.setMerchantCode();
//        paymentDTO.setMerchantName();
//        paymentDTO.setAmount();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + request);
        paymentDTO.setMerchantTransactionId(transId);
        paymentDTO.setChannelTransactionId(orderId);
        paymentDTO.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        return paymentService.createPaymentDto(paymentDTO);
    }

    private Payment updatePaymentData(Payment payment, AuthenResponse response,
                                      String errorCode, String resString) {
        payment.setChannelTransactionId(response.getOrder().getId());
        payment.setChannelStatus(errorCode);
        payment.setChannelMessage(MIGSConfig.getErrorMessage(errorCode));
        payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        if (NumberUtils.isParsable(response.getOrder().getAmount())) {
            if (response.getOrder().getAmount().length() <= 12) {
                payment.setAmount(NumberUtils.createBigDecimal(response.getOrder().getAmount()));
            }
        }
        payment.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + resString);
        return paymentService.updatePayment(payment);
    }

    private Payment updatePayment3DS1(Payment payment, TransactionResponse response, String errorCode, String resString) {
        payment.setChannelStatus(errorCode);
        payment.setChannelMessage(MIGSConfig.getErrorMessage(errorCode));
        payment.setChannelTransactionId(response.getOrderId());
        if (errorCode.equals("APPROVED")) {
            payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
//            payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        } else {
            payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
//            payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
        }
        if (NumberUtils.isParsable(response.getOrderAmount())) {
            if (response.getOrderAmount().length() <= 12) {
                payment.setAmount(NumberUtils.createBigDecimal(response.getOrderAmount()));
            }
        }
        payment.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + resString);
        return paymentService.updatePayment(payment);
    }
    private Payment_Rec parseResponseToPayment(String response){

        JSONObject json = new JSONObject(response);
        JSONObject sourceOfFunds = (JSONObject) json.get("sourceOfFunds");
        JSONObject transaction = (JSONObject) json.get("transaction");
        JSONObject order = (JSONObject) json.get("order");
        JSONObject provided = (JSONObject) sourceOfFunds.get("provided");
        JSONObject card = (JSONObject) provided.get("card");
        JSONObject acquirer = (JSONObject) transaction.get("acquirer");

        Payment_Rec payment = new Payment_Rec.PaymentRecBuilder()
                .merchantId(String.valueOf(json.get("merchant")))
                .terminalId(String.valueOf(transaction.get("terminal")))
                .transType(String.valueOf(card.get("fundingMethod")))
                .transDatetime(String.valueOf(json.get("timeOfRecord")))
                .authCode(transaction.has("authorizationCode") ? String.valueOf(transaction.get("authorizationCode")) :"AUTH_CODE")
                .accountNumber(String.valueOf(card.get("number")))
                .cardType(String.valueOf(card.get("brand")))
                .currency(String.valueOf(order.get("currency")))
                .amount(String.valueOf(transaction.get("amount")))
                .retRefNumber(String.valueOf(transaction.get("receipt")))
                .orderId(String.valueOf(order.get("id")))
                .build();
        return payment;
    }
}
