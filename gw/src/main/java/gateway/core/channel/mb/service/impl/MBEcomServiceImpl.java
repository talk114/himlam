package gateway.core.channel.mb.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.mb.dto.request.*;
import gateway.core.channel.mb.dto.response.MBRefundResponse;
import gateway.core.channel.mb.service.MBEcomService;
import gateway.core.dto.PGResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import gateway.core.channel.mb.dto.MBConst;
import gateway.core.channel.mb.dto.response.MBGetTokenResponse;
import gateway.core.channel.mb.dto.response.MBPaymentEcomResponse;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.AddTransactionGWResponse;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.*;
import vn.nganluong.naba.utils.RequestUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

@Service
public class MBEcomServiceImpl extends PaymentGate implements MBEcomService {

    private static final Logger logger = LogManager.getLogger(MBEcomServiceImpl.class);
    private static final String SERVICE_NAME = "ECOM";
    private static String requestRefund = "";

    private final PgLogChannelFunctionService pgLogChannelFunctionService;
    private final ChannelFunctionService channelFunctionService;
    private final PaymentService paymentService;
    private final CommonLogService commonLogService;
    private final CommonPGResponseService commonPGResponseService;

    @Autowired
    public MBEcomServiceImpl(PgLogChannelFunctionService pgLogChannelFunctionService,
                             ChannelFunctionService channelFunctionService,
                             PaymentService paymentService,
                             CommonLogService commonLogService,
                             CommonPGResponseService commonPGResponseService){
        this.pgLogChannelFunctionService = pgLogChannelFunctionService;
        this.channelFunctionService = channelFunctionService;
        this.paymentService = paymentService;
        this.commonLogService = commonLogService;
        this.commonPGResponseService = commonPGResponseService;
    }

    private String getAccessToken() {
        logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                MBConst.FUNCTION_CODE_ACCESS_TOKEN, true));

        RestTemplate restTemplate = null;
        try {
            restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                    MBConst.FUNCTION_CODE_ACCESS_TOKEN, false);

            String[] paramsLog = new String[]{e.getMessage()};
            logger.error(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
        }

        ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelCode(
                MBConst.FUNCTION_CODE_ACCESS_TOKEN, MBConst.CHANNEL_CODE);

        if (channelFunction == null) {
            pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                    MBConst.FUNCTION_CODE_ACCESS_TOKEN, false);

            String[] paramsLog = new String[]{MBConst.CHANNEL_NOT_CONFIG_MSG};
            logger.error(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
            return null;
        }

        restTemplate.getInterceptors()
                .add(new BasicAuthorizationInterceptor(channelFunction.getUser(), channelFunction.getPassword()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> mapBody = new LinkedMultiValueMap<String, String>();
        mapBody.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(mapBody,
                headers);

        // post request
        ResponseEntity<MBGetTokenResponse> response = restTemplate
                .exchange(RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                        channelFunction.getUrl()), HttpMethod.POST, entity, MBGetTokenResponse.class);

        MBGetTokenResponse getTokenResponse = response.getBody();

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                    MBConst.FUNCTION_CODE_ACCESS_TOKEN, true);

            String[] paramsLog = new String[]{getTokenResponse.getAccess_token()};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ACCESS_TOKEN, true, false, true, paramsLog));

        } else {
            pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                    MBConst.FUNCTION_CODE_ACCESS_TOKEN, false);

            String[] paramsLog = new String[]{"Resonse httpstatus: " + response.getStatusCode()};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ACCESS_TOKEN, false, false, true, paramsLog));
        }

        logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                MBConst.FUNCTION_CODE_ACCESS_TOKEN, false));
        return getTokenResponse.getAccess_token();
    }

    @Override
    public PGResponse addTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount,
            String inputStr) throws Exception {
        String channelFucntionId = MBConst.FUNCTION_CODE_ADD_TRANSACTION;
        String[] paramsLog = null;
        PaymentDTO paymentDTO = new PaymentDTO();
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ADD_TRANSACTION, true));

            MBCreateTransactionMBRequets transactionInfo = objectMapper.readValue(inputStr, MBCreateTransactionMBRequets.class);

            if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
                paramsLog = new String[]{"Client transaction is empty"};
                logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
                return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
            } else {
                Payment paymentTocheckExist = paymentService
                        .findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
                if (paymentTocheckExist != null) {

                    paramsLog = new String[]{"Client transaction id (trace id) already exist ("
                        + transactionInfo.getClient_transaction_id() + ")"};
                    logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                            MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));

                    logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                            MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
                    return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
                }
            }

            String accessToken = getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
                return commonPGResponseService.returnBadGateway().getBody();
            }

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String clientMessageId = RandomStringUtils.randomAlphanumeric(24);
            String transactionId = createTransactionIdFromClientTransactionId(transactionInfo.getClient_transaction_id());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Accept", "application/json");
            headers.add("clientMessageId", clientMessageId);
            headers.add("transactionId", transactionId);

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("accountName", transactionInfo.getCard_name());
            bodyRequest.put("accountNumber", transactionInfo.getCard_no());
            bodyRequest.put("amount", transactionInfo.getAmount());
            bodyRequest.put("currency", MBConst.CCY);
            bodyRequest.put("dateOpen", transactionInfo.getDate_open());
            bodyRequest.put("fee", transactionInfo.getFee());
            bodyRequest.put("merchant", transactionInfo.getMerchant());
            bodyRequest.put("mobile", transactionInfo.getMobile());
            bodyRequest.put("paymentDetails", "Thanh toan phieu thu " + transactionInfo.getClient_transaction_id()
                    + " Cong Ngan Luong");
            bodyRequest.put("serviceType", transactionInfo.getService_type());

            HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/paymentgw/request
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);

            // add infor to log
            bodyRequest.put("client_transaction_id", transactionInfo.getClient_transaction_id());

            ObjectMapper mapperObj = new ObjectMapper();
            paramsLog
                    = new String[]{"URL: " + builderUri.toUriString() + ", header: [" + headers.get("transactionId") + "]"
                        + ", body: [" + bodyRequest.toString() + "]"};

            commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                    commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                            MBConst.FUNCTION_CODE_ADD_TRANSACTION, true, true, false, paramsLog));

            // Create payment before call api into channel
            // Add payment to database:
            paymentDTO.setChannelId(channelFunction.getChannel().getId());
            paymentDTO.setAmount(transactionInfo.getAmount());
            paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
            paymentDTO.setAccountNo(transactionInfo.getCard_no());
            // paymentDTO.setCardNo(transactionInfo.getCard_no());
            paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + StringUtils.join(paramsLog));
            paymentDTO.setDescription(transactionInfo.getPayment_description());
            paymentDTO.setChannelTransactionId(transactionId);
            paymentDTO.setMerchantCode(transactionInfo.getMerchant());

            paymentService.createPayment(paymentDTO);

            MBPaymentEcomResponse responseBody = new MBPaymentEcomResponse();
            try {
                ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
                        HttpMethod.POST, entity, MBPaymentEcomResponse.class);
                responseBody = response.getBody();
            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
            }

            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + mapperObj.writeValueAsString(responseBody));

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);

            AddTransactionGWResponse dataObject = new AddTransactionGWResponse();
            jsonObject.setChannelMessage(StringUtils.join(responseBody.getError_desc()));
            jsonObject.setChannelErrorCode(responseBody.getError_code());
            dataObject.setTransaction_id(transactionInfo.getClient_transaction_id());

            if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
                PGResponse prefixResult = commonPGResponseService.returnGatewayRequestSuccessPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());
                dataObject.setRequest_id(responseBody.getData().getRequestId());
                pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                        MBConst.FUNCTION_CODE_ADD_TRANSACTION, true);

                paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                paymentDTO.setClientRequestId(responseBody.getData().getRequestId());
                if (responseBody.getData().getWay4_reference_number() != null) {
                    paymentDTO.setCardType(PaymentConst.EnumBankStatus.EnumCardType.CREDIT.code());
                } else {
                    paymentDTO.setCardType(PaymentConst.EnumBankStatus.EnumCardType.DEBIT.code());
                }

                paramsLog
                        = new String[]{paymentDTO.getRawResponse()};

                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_ADD_TRANSACTION, true, false, true, paramsLog));
            } else {
                paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                PGResponse prefixResult = commonPGResponseService.returnChannelBadRequestPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                        MBConst.FUNCTION_CODE_ADD_TRANSACTION, false);

                paramsLog = new String[]{paymentDTO.getRawResponse()};
                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
            }
            jsonObject.setData(dataObject);
            paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
            return jsonObject;

        } catch (RestClientException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            paymentDTO.setRawResponse(ExceptionUtils.getMessage(e));
            paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

            paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
            return (PGResponse) commonPGResponseService.returnChannelBadRequest(ExceptionUtils.getMessage(e)).getBody();

        } catch (Exception e) {

            paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ADD_TRANSACTION, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_ADD_TRANSACTION, false));
            return commonPGResponseService.returnBadGateway().getBody();
        }
    }

    @Override
    public PGResponse confirmTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount,
            String inputStr) throws Exception {
        String channelFucntionId = MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION;
        String[] paramsLog = null;
        PaymentDTO paymentDTO = new PaymentDTO();
        MBConfirmTransactionMBRequets transactionInfo = objectMapper.readValue(inputStr, MBConfirmTransactionMBRequets.class);
        paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, true));

            if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
                paramsLog = new String[]{"Client transaction is empty"};
                logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
                return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
            }

            Payment paymentTocheckExist = paymentService
                    .findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
            if (paymentTocheckExist == null) {

                paramsLog = new String[]{"Client transaction id (trace id) not exist ("
                    + transactionInfo.getClient_transaction_id() + ")"};

                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
                return commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();
            }

            String accessToken = getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
                return commonPGResponseService.returnBadGateway().getBody();
            }

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String clientMessageId = RandomStringUtils.randomAlphanumeric(24);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Accept", "application/json");
            headers.add("clientMessageId", clientMessageId);
            headers.add("transactionId", paymentTocheckExist.getChannelTransactionId());

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("otp", transactionInfo.getOtp());
            bodyRequest.put("requestId", paymentTocheckExist.getClientRequestId());

            HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/paymentgw/request
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);

            ObjectMapper mapperObj = new ObjectMapper();
            paramsLog = new String[]{"URL: " + builderUri.toUriString() + ", body: [" + bodyRequest.toString() + "]"};
            commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                    commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                            MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, true, true, false, paramsLog));

            paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + StringUtils.join(paramsLog));

            MBPaymentEcomResponse responseBody = new MBPaymentEcomResponse();

            try {
                ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
                        HttpMethod.POST, entity, MBPaymentEcomResponse.class);
                responseBody = response.getBody();
            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
            }

            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + mapperObj.writeValueAsString(responseBody));

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);

            AddTransactionGWResponse dataObject = new AddTransactionGWResponse();
            jsonObject.setChannelMessage(StringUtils.join(responseBody.getError_desc()));
            jsonObject.setChannelErrorCode(responseBody.getError_code());

            if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
                PGResponse prefixResult = commonPGResponseService.returnGatewayRequestSuccessPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());
                dataObject.setRequest_id(responseBody.getData().getRequestId());
                dataObject.setFt(responseBody.getData().getFt());
                pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                        MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, true);

                if (StringUtils.isNotBlank(dataObject.getFt()) && StringUtils.contains(dataObject.getFt(), "FT")) {
                    paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
                    paymentDTO.setChannelTransactionSeq(dataObject.getFt());
                } else {
                    paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
                }

                paymentDTO.setClientRequestId(responseBody.getData().getRequestId());

                if (responseBody.getData().getWay4_reference_number() != null) {
                    paymentDTO.setCardType(PaymentConst.EnumBankStatus.EnumCardType.CREDIT.code());
                } else {
                    paymentDTO.setCardType(PaymentConst.EnumBankStatus.EnumCardType.DEBIT.code());
                }

                paramsLog = new String[]{paymentDTO.getRawResponse() + ", CARD TYPE: " + (paymentDTO.getCardType() == 1
                    ? PaymentConst.EnumBankStatus.EnumCardType.CREDIT.name()
                    : PaymentConst.EnumBankStatus.EnumCardType.DEBIT.name())};

                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, true, false, true, paramsLog));

            } else {
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
                PGResponse prefixResult = commonPGResponseService.returnChannelBadRequestPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                        MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false);

                paramsLog = new String[]{paymentDTO.getRawResponse()};

                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));
            }

            paymentService.updateChannelTransactionStatusPayment(paymentDTO);

            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
            jsonObject.setData(dataObject);
            return jsonObject;

        } catch (RestClientException e) {
            pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            paymentDTO.setRawResponse(e.getMessage());
            paymentService.updateChannelTransactionStatusPayment(paymentDTO);

            paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));
            return (PGResponse) commonPGResponseService.returnChannelBadRequest(ExceptionUtils.getMessage(e)).getBody();

        } catch (Exception e) {
            paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_CONFIRM_TRANSACTION, false));
            return commonPGResponseService.returnBadGateway().getBody();
        }
    }

    @Override
    public PGResponse revertTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount,
            String inputStr) throws Exception {
        String channelFucntionId = MBConst.FUNCTION_CODE_REVERT_TRANSACTION;
        String[] paramsLog = null;
        PaymentDTO paymentDTO = new PaymentDTO();
        MBRevertTransactionMBRequets transactionInfo = objectMapper
                .readValue(inputStr, MBRevertTransactionMBRequets.class);
        paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_REVERT_TRANSACTION, true));

            if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
                paramsLog = new String[]{"Client transaction is empty"};
                logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
                return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
            }

            Payment paymentTocheckExist = paymentService
                    .findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
            if (paymentTocheckExist == null) {

                paramsLog = new String[]{"Client transaction id (trace id) not exist ("
                    + transactionInfo.getClient_transaction_id() + ")"};
                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
                return commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();
            }

            String accessToken = getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
                return commonPGResponseService.returnBadGateway().getBody();
            }

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String clientMessageId = RandomStringUtils.randomAlphanumeric(24);
            String transactionId = RandomStringUtils.randomAlphanumeric(12);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Accept", "application/json");
            headers.add("clientMessageId", clientMessageId);
            headers.add("transactionId", transactionId);

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("amount", transactionInfo.getAmount());
            bodyRequest.put("originRequestTransactionId", paymentTocheckExist.getChannelTransactionId());

            HttpEntity<String> entity = new HttpEntity<String>(bodyRequest.toString(), headers);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/paymentgw/request
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);

            ObjectMapper mapperObj = new ObjectMapper();
            paramsLog = new String[]{"URL: " + builderUri.toUriString() + ", body: [" + bodyRequest.toString() + "]"};

            commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                    commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                            MBConst.FUNCTION_CODE_REVERT_TRANSACTION, true, true, false, paramsLog));

            paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_REVERT_TRANSACTION + StringUtils.join(paramsLog));
            MBPaymentEcomResponse responseBody = new MBPaymentEcomResponse();

            try {
                ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
                        HttpMethod.POST, entity, MBPaymentEcomResponse.class);
                responseBody = response.getBody();
            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
            }

            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_REVERT_TRANSACTION + mapperObj.writeValueAsString(responseBody));

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);

            AddTransactionGWResponse dataObject = new AddTransactionGWResponse();
            jsonObject.setChannelMessage(StringUtils.join(responseBody.getError_desc()));
            jsonObject.setChannelErrorCode(responseBody.getError_code());

            if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
                PGResponse prefixResult = commonPGResponseService.returnGatewayRequestSuccessPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());
                dataObject.setRequest_id(responseBody.getData().getRequestId());

                if (StringUtils.isEmpty(jsonObject.getChannelMessage())) {
                    jsonObject.setChannelMessage("Revert success");
                }

                pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                        MBConst.FUNCTION_CODE_REVERT_TRANSACTION, true);

                paymentDTO.setChannelRevertStatus(PaymentConst.EnumPaymentRevertStatus.REVERTED.code()); // TODO
                paymentDTO.setClientRequestId(responseBody.getData().getRequestId());

                paramsLog = new String[]{paymentDTO.getRawResponse()};
                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_REVERT_TRANSACTION, true, false, true, paramsLog));
            } else {
                PGResponse prefixResult = commonPGResponseService.returnChannelBadRequestPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                        MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false);

                paramsLog = new String[]{paymentDTO.getRawResponse()};
                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));
            }

            paymentService.updateChannelTransactionStatusPayment(paymentDTO);

            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
            jsonObject.setData(dataObject);
            return jsonObject;

        } catch (RestClientException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

            paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));
            return (PGResponse) commonPGResponseService.returnChannelBadRequest(e.getMessage()).getBody();
        } catch (Exception e) {
            paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_REVERT_TRANSACTION, false));
            return commonPGResponseService.returnBadGateway().getBody();
        }
    }

    @Override
    public PGResponse statusTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount,
            String inputStr) throws Exception {
        String channelFucntionId = MBConst.FUNCTION_CODE_STATUS_TRANSACTION;
        String[] paramsLog = null;
        PaymentDTO paymentDTO = new PaymentDTO();

        MBStatusTransactionMBRequets transactionInfo = objectMapper.readValue(inputStr, MBStatusTransactionMBRequets.class);
        paymentDTO.setMerchantTransactionId(transactionInfo.getClient_transaction_id());
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_STATUS_TRANSACTION, true));

            if (StringUtils.isBlank(transactionInfo.getClient_transaction_id())) {
                paramsLog = new String[]{"Client transaction is empty"};
                logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
                return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
            }

            Payment paymentTocheckExist = paymentService
                    .findByMerchantTransactionId(transactionInfo.getClient_transaction_id());
            if (paymentTocheckExist == null) {

                paramsLog = new String[]{"Client transaction id (trace id) not exist ("
                    + transactionInfo.getClient_transaction_id() + ")"};
                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(), commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
                return commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();
            }

            String accessToken = getAccessToken();

            if (StringUtils.isBlank(accessToken)) {
                logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                        MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
                return commonPGResponseService.returnBadGateway().getBody();
            }

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            String clientMessageId = RandomStringUtils.randomAlphanumeric(24);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Accept", "application/json");
            headers.add("clientMessageId", clientMessageId);
            headers.add("transactionId", "P3-" + paymentTocheckExist.getChannelTransactionId());

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();
            mapParams.add("transactionId", "P3-" + paymentTocheckExist.getChannelTransactionId());

            HttpEntity<?> entity = new HttpEntity<>(headers);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            // https://api-sandbox.mbbank.com.vn/ms/ewallet/v1.0/transaction/status
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl).queryParams(mapParams);

            ObjectMapper mapperObj = new ObjectMapper();
            paramsLog = new String[]{"URL: " + builderUri.toUriString() + ", data: Header: ["
                + mapperObj.writeValueAsString(headers) + "], body: []"};
            commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                    commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                            MBConst.FUNCTION_CODE_STATUS_TRANSACTION, true, true, false, paramsLog));

            MBPaymentEcomResponse responseBody;
            try {
                ResponseEntity<MBPaymentEcomResponse> response = restTemplate.exchange(builderUri.toUriString(),
                        HttpMethod.GET, entity, MBPaymentEcomResponse.class);
                responseBody = response.getBody();
            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), MBPaymentEcomResponse.class);
            }

            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + mapperObj.writeValueAsString(responseBody));

            PGResponse jsonObject = new PGResponse();
            jsonObject.setStatus(true);

            AddTransactionGWResponse dataObject = new AddTransactionGWResponse();
            jsonObject.setChannelMessage(StringUtils.join(responseBody.getError_desc()));
            jsonObject.setChannelErrorCode(responseBody.getError_code());

            if (StringUtils.equals(responseBody.getError_code(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {
                PGResponse prefixResult = commonPGResponseService.returnGatewayRequestSuccessPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());
                dataObject.setRequest_id(responseBody.getData().getRequestId());
                dataObject.setFt(StringUtils.join(responseBody.getData().getRoot_id(), ','));
                pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                        MBConst.FUNCTION_CODE_STATUS_TRANSACTION, true);

                if (StringUtils.isNotBlank(dataObject.getFt()) && StringUtils.contains(dataObject.getFt(), "FT")) {
                    paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                    paymentDTO.setChannelTransactionSeq(dataObject.getFt());
                } else {
                    paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                }

                paymentDTO.setClientRequestId(responseBody.getData().getRequestId());

                paramsLog = new String[]{paymentDTO.getRawResponse()};
                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_STATUS_TRANSACTION, true, false, true, paramsLog));

            } else {
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                PGResponse prefixResult = commonPGResponseService.returnChannelBadRequestPrefix();
                jsonObject.setErrorCode(prefixResult.getErrorCode());
                jsonObject.setMessage(prefixResult.getMessage());

                pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE,
                        MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false);

                paramsLog = new String[]{paymentDTO.getRawResponse()};
                commonLogService.logInfoWithTransId(logger, transactionInfo.getClient_transaction_id(),
                        commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                                MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));
            }

            paymentService.updateChannelTransactionStatusPayment(paymentDTO);

            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
            jsonObject.setData(dataObject);
            return jsonObject;

        } catch (RestClientException e) {

            pgLogChannelFunctionService.writeLogChannelFunction(MBConst.CHANNEL_CODE, channelFucntionId, false);

            paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));
            return (PGResponse) commonPGResponseService.returnChannelBadRequest(e.getMessage()).getBody();

        } catch (Exception e) {

            paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
            logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
            return commonPGResponseService.returnBadGateway().getBody();
        }
    }

    @Override
    public PGResponse refund(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception {
        logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                MBConst.FUNCTION_CODE_STATUS_TRANSACTION, true));
        MBRefundTransactionMBRequest refund = objectMapper.readValue(inputStr, MBRefundTransactionMBRequest.class);
        //replace transaction id by transaction id in payment
        Payment payment = this.getTransactionIdRefund(refund.getTransactionId());
        String transactionIdRefund = payment != null ? payment.getChannelTransactionId() : null;
        String ftNumber = payment != null ? payment.getChannelTransactionSeq() : null;
        if ( transactionIdRefund == null || ftNumber == null) {
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
            return PGResponse.getInstanceWhenError(PGResponse.TRANSACTION_NOT_EXIST);
        }
        refund.setTransactionId(transactionIdRefund);
        refund.setFtNumber(ftNumber);

        if(StringUtils.isEmpty(refund.getTransactionId())){
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
        }

        MBRefundResponse responseBody = null;
        String clientMessageId = RandomStringUtils.randomAlphanumeric(24);

        String transactionId = MBConst.TRANSACTION_ID + MBConst.TRANSACTION_TYPE +  RandomStringUtils.randomAlphanumeric(8);

        try {
            Response response = this.httpRequest(this.getAccessToken(),
                    refund,
                    channelFunction,
                    clientMessageId,
                    transactionId);
            String responseString = response.body().string();
            logger.info("Response body: " + responseString);
            responseBody = objectMapper.readValue(responseString, MBRefundResponse.class);
            logger.info(commonLogService.createContentLogStartEndFunction(MBConst.CHANNEL_CODE, SERVICE_NAME,
                    MBConst.FUNCTION_CODE_STATUS_TRANSACTION, false));
            return PGResponse.builder()
                    .status(true)
                    .errorCode(responseBody != null ? PGResponse.SUCCESS : PGResponse.FAIL)
                    .channelMessage(responseBody != null ? StringUtils.join(responseBody.getErrorDesc()) : "error")
                    .channelErrorCode(responseBody != null ? responseBody.getErrorCode() : "error")
                    .data(responseString)
                    .build();
        } catch (Exception e){
            logger.error(e.getMessage());
            logger.error(e.getStackTrace()[0]);
            return PGResponse.builder()
                    .status(false)
                    .errorCode(PGResponse.FAIL)
                    .data(e.getMessage())
                    .build();
        } finally {
            if( responseBody != null ){
                this.refundPaymentSave(responseBody, clientMessageId, transactionId);
            }
        }
    }

    private Response httpRequest(String accessToken,
                                 MBRefundTransactionMBRequest refund,
                                 ChannelFunction channelFunction,
                                 String clientMessageId,
                                 String transactionId) throws IOException {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS);

        OkHttpClient client = builder.build();
        okhttp3.MediaType mediaType = okhttp3.MediaType.parse("application/json");
        String bodyRequest = objectMapper.writeValueAsString(refund);
        RequestBody body = RequestBody.create(mediaType, bodyRequest);

        String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                channelFunction.getPort(), channelFunction.getUrl());
        Request request = new Request.Builder()
                .url(resourceUrl)
                .post(body)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/json")
                .addHeader("clientMessageId", clientMessageId)
                .addHeader("transactionId", transactionId)
                .build();
        requestRefund = request.toString() +", body: " +bodyRequest;
        logger.info(request.toString() +", body: " +bodyRequest);
        return client.newCall(request).execute();
    }

    private Payment getTransactionIdRefund(String merchantTransactionId) {
        if (merchantTransactionId == null || merchantTransactionId.length() == 0){
            throw new IllegalArgumentException("Transaction is not exist");
        }
        Payment payment = paymentService
                .findByMerchantTransactionId(merchantTransactionId);
        return payment != null ? payment : null;
    }

    private void refundPaymentSave(MBRefundResponse responseBody, String clientMessageId ,String transactionId){
        PaymentDTO paymentDTO = new PaymentDTO();

        paymentDTO.setChannelId(MBConst.CHANNEL_ID);
        paymentDTO.setClientRequestId(clientMessageId);
        paymentDTO.setMerchantTransactionId(transactionId);
        paymentDTO.setChannelMessage("MB refund");
        paymentDTO.setRawRequest(requestRefund);
        paymentDTO.setRawResponse(responseBody.toString());
        if (StringUtils.equals(responseBody.getErrorCode(), MBConst.MB_RESPONSE_ERROR_CODE_SUCCESS)) {

            paymentDTO.setChannelTransactionId(responseBody.getDataTransactionRefundId());
            paymentDTO.setChannelTransactionSeq(responseBody.getDataFtNumber());
            paymentDTO.setAmount(responseBody.getDataAmount()+"");
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());

        } else {
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
        }
        requestRefund = "";
        paymentService.createPayment(paymentDTO);
    }

    private String createTransactionIdFromClientTransactionId(String clientTransactionId) {

        if (StringUtils.length(clientTransactionId) > 10) {
            return StringUtils.upperCase(MBConst.TRANSACTION_ID + MBConst.TRANSACTION_TYPE + RandomStringUtils.randomAlphanumeric(8));
        } else {
           return MBConst.TRANSACTION_ID + clientTransactionId + StringUtils
                    .upperCase(RandomStringUtils.randomAlphanumeric(10 - StringUtils.length(clientTransactionId)));
        }
    }
}
