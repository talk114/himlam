package gateway.core.channel.cybersouce.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.cybersouce.dto.CybersourceConfig;
import gateway.core.channel.cybersouce.request.*;
import gateway.core.channel.cybersouce.response.*;
import gateway.core.channel.cybersouce.service.CybersouceService;
import gateway.core.channel.cybersouce.util.AlepayStringUtils;
import gateway.core.channel.cybersouce.util.CybersourceUtil;
import gateway.core.channel.cybersouce.util.CybsUtil;
import gateway.core.dto.PGRequest;
import gateway.core.dto.PGResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.service.*;
import vn.nganluong.naba.utils.Constant;
import vn.nganluong.naba.utils.CybersourceError;
import vn.nganluong.naba.utils.RequestUtil;
import io.jsonwebtoken.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;

/**
 * @author TaiND
 */
@Service
public class CybersourceServiceImpl extends PaymentGate implements CybersouceService {

    private final CybersourceUtil cybersourceUtil;
    private final PaymentService paymentService;
    private final CommonLogService commonLogService;
    private final CommonPGResponseService commonPGResponseService;
    private final PgFunctionService pgFunctionService;
    private final PgUserService pgUserService;

    @Autowired
    public CybersourceServiceImpl(CybersourceUtil cybersourceUtil,
                                  PaymentService paymentService,
                                  CommonLogService commonLogService,
                                  CommonPGResponseService commonPGResponseService,
                                  PgFunctionService pgFunctionService,
                                  PgUserService pgUserService){
        this.cybersourceUtil = cybersourceUtil;
        this.paymentService = paymentService;
        this.commonLogService = commonLogService;
        this.commonPGResponseService = commonPGResponseService;
        this.pgFunctionService = pgFunctionService;
        this.pgUserService = pgUserService;
    }

    private static final Logger logger = LogManager.getLogger(CybersourceServiceImpl.class);
    private static final String PREFIX_TRANSACTION_CODE = "alp";

    @Override
    public PGResponse authorizeCard(String inputStr) throws Exception {
        AuthorizeCard authorizeCard = objectMapper.readValue(inputStr, AuthorizeCard.class);

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_AUTHOIZATION_CARD, true));
        cybersourceUtil.validAuthorizeCard(authorizeCard);

        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", CybersourceConfig.MERCHANT_ID);
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(authorizeCard.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : authorizeCard.getTransactionReferenceId())
        );

        cybersourceUtil.addBillAddress(request, authorizeCard.getBillAddress());

        request.put("purchaseTotals", null);
        request.put("purchaseTotals.currency", authorizeCard.getOrderCurrency());

        cybersourceUtil.addCard(request, authorizeCard.getCard());

        request.put("subscription", null);
        request.put("subscription.title", "Create Customer Profile");
        request.put("subscription.paymentMethod", "credit card");
        request.put("recurringSubscriptionInfo", null);
        request.put("recurringSubscriptionInfo.frequency", "on-demand");
        if (authorizeCard.getAmount() > 0) {
            request.put("purchaseTotals.grandTotalAmount", String.valueOf(authorizeCard.getAmount()));
            request.put("ccAuthService", null);
            request.put("ccAuthService_run", "true");
            request.put("ccCaptureService", null);
            request.put("ccCaptureService_run", "true");
        }
        if (!authorizeCard.isDisable3DSecure()) {
            request.put("payerAuthEnrollService", null);
            request.put("payerAuthEnrollService_run", "true");
        }
        request.put("paySubscriptionCreateService", null);
        request.put("paySubscriptionCreateService_run", "true");

        String[] paramsLog = new String[]{"Request: [" + request + "]"};
        commonLogService.logInfoWithTransId(logger, authorizeCard.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_AUTHOIZATION_CARD, true, true, false, paramsLog));

        PGResponse response = cybersourceUtil.createCustomerProfile(request);

        paramsLog = new String[]{response.getData().toString()};
        commonLogService.logInfoWithTransId(logger, authorizeCard.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_AUTHOIZATION_CARD, true, false, true, paramsLog));

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_AUTHOIZATION_CARD, false));
        return response;
    }

    @Override
    public PGResponse authorizeCard3D(String inputStr) throws Exception {
        AuthorizeCard3D authorizeCard3D = objectMapper.readValue(inputStr, AuthorizeCard3D.class);

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_AUTHOIZATION_CARD_3D, true));
        cybersourceUtil.validAuthorizeCard3D(authorizeCard3D);

        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", CybersourceConfig.MERCHANT_ID);
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(authorizeCard3D.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : authorizeCard3D.getTransactionReferenceId())
        );
        cybersourceUtil.addBillAddress(request, authorizeCard3D.getBillAddress());

        request.put("purchaseTotals", null);
        request.put("purchaseTotals.currency", authorizeCard3D.getOrderCurrency());

        cybersourceUtil.addCard(request, authorizeCard3D.getCard());
        request.put("subscription", null);
        request.put("subscription.title", "Alepay Create Customer Profile");
        request.put("subscription.paymentMethod", "credit card");
        request.put("recurringSubscriptionInfo", null);
        request.put("recurringSubscriptionInfo.frequency", "on-demand");
        request.put("payerAuthValidateService", null);
        request.put("payerAuthValidateService_run", "true");
        request.put("payerAuthValidateService.signedPARes", CybsUtil.signedPARes(authorizeCard3D.getSignedPARes()));
        request.put("paySubscriptionCreateService", null);
        request.put("paySubscriptionCreateService_run", "true");

        String[] paramsLog = new String[]{"Request: [" + request.toString() + "]"};
        commonLogService.logInfoWithTransId(logger, authorizeCard3D.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_AUTHOIZATION_CARD_3D, true, true, false, paramsLog));

        PGResponse response = cybersourceUtil.createCustomerProfile(request);

        paramsLog = new String[]{response.getData().toString()};
        commonLogService.logInfoWithTransId(logger, authorizeCard3D.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_AUTHOIZATION_CARD_3D, true, false, true, paramsLog));

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_AUTHOIZATION_CARD_3D, false));
        return response;
    }

    @Override
    public PGResponse authorize(String inputStr, ChannelFunction channelFunction) throws Exception {
        Authorize authorize = objectMapper.readValue(inputStr, Authorize.class);
        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_PAYMENT, true));

        cybersourceUtil.validAuthorize(authorize);
        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", CybersourceConfig.MERCHANT_ID);
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(authorize.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : authorize.getTransactionReferenceId())
        );
        String aggregatorID = null;
        if (authorize.getCard().getCardType().toString().equals("VISA")) {
            aggregatorID = "10078767";
        } else if (authorize.getCard().getCardType().toString().equals("MASTERCARD")) {
            aggregatorID = "258116";
        }
        // InvoiceHeader added merchant name
        request.put("invoiceHeader", null);
        request.put("invoiceHeader.merchantDescriptor", authorize.getMerchantDescriptor());
        if (authorize.getGatewayMID() == 11) {
            request.put("invoiceHeader.merchantDescriptorContact", "0988888888");
            request.put("invoiceHeader.merchantDescriptorAlternate", "alepay.vn");
        } else {
            request.put("invoiceHeader.merchantDescriptorContact", null);
            request.put("invoiceHeader.merchantDescriptorAlternate", null);
        }
        String removeVN = AlepayStringUtils.removeDiacritical(authorize.getMerchantName());
        String merchantName = removeVN.length() > 20 ? removeVN.substring(0, 19) : removeVN;
        String authenCode = authorize.getAuthenCode().length() > 10 ? authorize.getAuthenCode().substring(0, 9) : authorize.getAuthenCode();
        request.put("invoiceHeader.merchantDescriptorStreet", authenCode + "_" + authorize.getMerchantType() + "_" + merchantName + "_NL");
        if (authorize.getGatewayMID() == 11) {
            request.put("invoiceHeader.merchantDescriptorCity", "HaNoi");
            request.put("invoiceHeader.merchantDescriptorCountry", "VN");
            request.put("invoiceHeader.submerchantID", authorize.getSubMtid());
        } else {
            request.put("invoiceHeader.merchantDescriptorCity", null);
            request.put("invoiceHeader.merchantDescriptorState", null);
            request.put("invoiceHeader.merchantDescriptorPostalCode", null);
            request.put("invoiceHeader.merchantDescriptorCountry", null);
        }
        cybersourceUtil.addBillAddress(request, authorize.getBillAddress());

        request.put("purchaseTotals", null);
        request.put("purchaseTotals.currency", authorize.getOrderCurrency());
        request.put("purchaseTotals.grandTotalAmount", String.valueOf(authorize.getPaymentAmount()));

        cybersourceUtil.addCard(request, authorize.getCard());

        if (authorize.getGatewayMID() == 11) {
            request.put("merchantDefinedData", null);
            request.put("merchantDefinedData.mddField", authorize.getSubMtid());
            request.put("merchantDefinedData.mddField_id", "5");
            request.put("merchantDefinedData.mddField2", authorize.getMerchantDescriptor());
            request.put("merchantDefinedData.mddField2_id", "6");
        }

        request.put("ccAuthService", null);
        request.put("ccAuthService_run", "true");
        request.put("ccAuthService.aggregatorID", aggregatorID);
        request.put("ccAuthService.aggregatorName", "NL ALEPAY");

        request.put("ccCaptureService", null);
        request.put("ccCaptureService_run", "true");
        if (!authorize.isDisable3DSecure()) {
            request.put("payerAuthEnrollService", null);
            request.put("payerAuthEnrollService_run", "true");
//            if (authorize.getMerchantId() == 1827L) {
//                request.put("payerAuthEnrollService.merchantName", authorize.getMerchantDescriptor());
//            }
        }
        String[] paramsLog = new String[]{"Request: [" + request + "]"};
        commonLogService.logInfoWithTransId(logger, authorize.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_PAYMENT, true, true, false, paramsLog));
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setAmount(String.valueOf(authorize.getPaymentAmount()));
        paymentDTO.setMerchantTransactionId(authorize.getTransactionReferenceId());
        paymentDTO.setAccountNo(authorize.getCard().getCardNumber());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + StringUtils.join(paramsLog));
        paymentDTO.setDescription(authorize.getMerchantDescriptor());
        paymentDTO.setMerchantName(authorize.getMerchantName());
        paymentService.createPayment(paymentDTO);

        PGResponse response = cybersourceUtil.runTransaction(request, Constant.AUTH);

        paramsLog = new String[]{response.getData().toString()};
        commonLogService.logInfoWithTransId(logger, authorize.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_PAYMENT, true, false, true, paramsLog));

        ObjectMapper mapperObj = new ObjectMapper();
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + mapperObj.writeValueAsString(response));
        paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_PAYMENT, false));
        return response;
    }

    @Override
    public PGResponse authorize3D(String inputStr) throws Exception {
        Authorize3D authorize3D = objectMapper.readValue(inputStr, Authorize3D.class);

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_PAYMENT_3D, true));
        String[] paramsLog;
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId(authorize3D.getTransactionReferenceId());
        if (StringUtils.isBlank(authorize3D.getTransactionReferenceId())) {
            paramsLog = new String[]{"Client transaction is empty"};
            logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_PAYMENT_3D, false, false, true, paramsLog));

            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_PAYMENT_3D, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
        }

        Payment paymentTocheckExist = paymentService
                .findByMerchantTransactionId(authorize3D.getTransactionReferenceId());
        if (paymentTocheckExist == null) {

            paramsLog = new String[]{"Client transaction id (trace id) not exist ("
                    + authorize3D.getTransactionReferenceId() + ")"};

            commonLogService.logInfoWithTransId(logger, authorize3D.getTransactionReferenceId(),
                    commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                            CybersourceConfig.FUNCTION_PAYMENT_3D, false, false, true, paramsLog));

            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_PAYMENT_3D, false));
            return commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();
        }

        cybersourceUtil.validAuthorize3D(authorize3D);
        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", CybersourceConfig.MERCHANT_ID);
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(authorize3D.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : authorize3D.getTransactionReferenceId())
        );

        String aggregatorID = null;
        if (authorize3D.getCard().getCardType().toString().equals("VISA")) {
            aggregatorID = "10078767";
        } else if (authorize3D.getCard().getCardType().toString().equals("MASTERCARD")) {
            aggregatorID = "258116";
        }

        request.put("invoiceHeader", null);
        request.put("invoiceHeader.merchantDescriptor", authorize3D.getMerchantDescriptor());
        if (authorize3D.getGatewayMID() == 11) {
            request.put("invoiceHeader.merchantDescriptorContact", "0988888888");
            request.put("invoiceHeader.merchantDescriptorAlternate", "alepay.vn");
        } else {
            request.put("invoiceHeader.merchantDescriptorContact", null);
            request.put("invoiceHeader.merchantDescriptorAlternate", null);
        }
        String removeVN = AlepayStringUtils.removeDiacritical(authorize3D.getMerchantName());
        String merchantName = removeVN.length() > 20 ? removeVN.substring(0, 19) : removeVN;
        String authenCode = authorize3D.getAuthenCode().length() > 10 ? authorize3D.getAuthenCode().substring(0, 9) : authorize3D.getAuthenCode();
        request.put("invoiceHeader.merchantDescriptorStreet", authenCode + "_" + authorize3D.getMerchantType() + "_" + merchantName + "_NL");
        if (authorize3D.getGatewayMID() == 11) {
            request.put("invoiceHeader.merchantDescriptorCity", "HaNoi");
            request.put("invoiceHeader.merchantDescriptorCountry", "VN");
            request.put("invoiceHeader.submerchantID", authorize3D.getSubMtid());
        } else {
            request.put("invoiceHeader.merchantDescriptorCity", null);
            request.put("invoiceHeader.merchantDescriptorState", null);
            request.put("invoiceHeader.merchantDescriptorPostalCode", null);
            request.put("invoiceHeader.merchantDescriptorCountry", null);
        }

        cybersourceUtil.addBillAddress(request, authorize3D.getBillAddress());

        request.put("purchaseTotals", null);
        request.put("purchaseTotals.currency", authorize3D.getOrderCurrency());
        request.put("purchaseTotals.grandTotalAmount", String.valueOf(authorize3D.getPaymentAmount()));

        cybersourceUtil.addCard(request, authorize3D.getCard());

        if (authorize3D.getGatewayMID() == 11) {
            request.put("merchantDefinedData", null);
            request.put("merchantDefinedData.mddField", authorize3D.getSubMtid());
            request.put("merchantDefinedData.mddField_id", "5");
            request.put("merchantDefinedData.mddField2", authorize3D.getMerchantDescriptor());
            request.put("merchantDefinedData.mddField2_id", "6");
        }

        request.put("ccAuthService", null);
        request.put("ccAuthService_run", "true");
        request.put("ccAuthService.aggregatorID", aggregatorID);
        request.put("ccAuthService.aggregatorName", "NL ALEPAY");

        request.put("ccCaptureService", null);
        request.put("ccCaptureService_run", "true");
        request.put("payerAuthValidateService", null);
        request.put("payerAuthValidateService_run", "true");
        request.put("payerAuthValidateService_authenticationTransactionID", authorize3D.getReferenceID());

//        request.put("payerAuthValidateService.signedPARes", CybsUtil.signedPARes(authorize3D.getSignedPARes()));

        paramsLog = new String[]{"Request: [" + request + "]"};
        commonLogService.logInfoWithTransId(logger, authorize3D.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_PAYMENT_3D, true, true, false, paramsLog));

        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + StringUtils.join(paramsLog));

        PGResponse response = cybersourceUtil.runTransaction(request, Constant.AUTH);

        paramsLog = new String[]{response.getData().toString()};
        commonLogService.logInfoWithTransId(logger, authorize3D.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_PAYMENT_3D, true, false, true, paramsLog));

        ObjectMapper mapperObj = new ObjectMapper();
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + mapperObj.writeValueAsString(response));
        paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        paymentService.updateChannelTransactionStatusPayment(paymentDTO);

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_PAYMENT_3D, false));
        return response;
    }

    @Override
    public PGResponse cancelAuthorizeCard(String inputStr) throws Exception {
        CancelAuthorizeCard profile = objectMapper.readValue(inputStr, CancelAuthorizeCard.class);

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_CANCEL_AUTHOIZATION, true));

        cybersourceUtil.validDeleteProfile(profile);

        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", CybersourceConfig.MERCHANT_ID);
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(profile.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : profile.getTransactionReferenceId())
        );

        request.put("recurringSubscriptionInfo", null);
        request.put("recurringSubscriptionInfo.subscriptionID", profile.getSubscriptionId());
        request.put("paySubscriptionDeleteService", null);
        request.put("paySubscriptionDeleteService_run", "true");

        String[] paramsLog = new String[]{"Request: [" + request + "]"};
        commonLogService.logInfoWithTransId(logger, profile.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_CANCEL_AUTHOIZATION, true, true, false, paramsLog));

        PGResponse response = cybersourceUtil.runTransaction(request, Constant.VOID_AUTHORIZE_CARD);

        paramsLog = new String[]{response.getData().toString()};
        commonLogService.logInfoWithTransId(logger, profile.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_CANCEL_AUTHOIZATION, true, false, true, paramsLog));

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_CANCEL_AUTHOIZATION, false));
        return response;
    }

    @Override
    public PGResponse checkEnrollment(String inputStr) throws Exception {
        CheckEnrollment checkEnrollment = objectMapper.readValue(inputStr, CheckEnrollment.class);

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_CHECK_ENROLLMENT, true));

        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", CybersourceConfig.MERCHANT_ID);
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(checkEnrollment.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : checkEnrollment.getTransactionReferenceId())
        );
        cybersourceUtil.addBillAddress(request, checkEnrollment.getBillAddress());
        request.put("purchaseTotals", null);
        request.put("purchaseTotals.currency", checkEnrollment.getOrderCurrency());
        request.put("purchaseTotals.grandTotalAmount", String.valueOf(checkEnrollment.getGrandTotalAmount()));
        cybersourceUtil.addCard(request, checkEnrollment.getCard());
        request.put("payerAuthEnrollService", null);
        request.put("payerAuthEnrollService_run", "true");
        request.put("payerAuthEnrollService.referenceID", checkEnrollment.getReferenceID());

        String[] paramsLog = new String[]{"Request: [" + request + "]"};
        commonLogService.logInfoWithTransId(logger, checkEnrollment.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_CHECK_ENROLLMENT, true, true, false, paramsLog));

        PGResponse response = cybersourceUtil.checkEnrollment(request);

        paramsLog = new String[]{response.getData().toString()};
        commonLogService.logInfoWithTransId(logger, checkEnrollment.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_CHECK_ENROLLMENT, true, false, true, paramsLog));

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_CHECK_ENROLLMENT, false));
        return response;
    }

    @Override
    public PGResponse createTransaction(ChannelFunction channelFunction, String inputStr) throws Exception {
        SSPCreateTransactionReq sspCreateTransactionReq = objectMapper.readValue(inputStr, SSPCreateTransactionReq.class);

        String[] paramsLog;
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_CREATE_TRANSACTION, true));
            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("callback", sspCreateTransactionReq.getCallback());

            JSONObject paymentDetails = new JSONObject();
            paymentDetails.put("orderNumber", sspCreateTransactionReq.getOrderNumber());
            paymentDetails.put("recurring", sspCreateTransactionReq.getRecurring());
            paymentDetails.put("allowedBrands", sspCreateTransactionReq.getAllowedBrands());

            JSONObject service = new JSONObject();
            service.put("id", sspCreateTransactionReq.getServiceId());
            paymentDetails.put("service", service);

            JSONObject protocol = new JSONObject();
            protocol.put("type", sspCreateTransactionReq.getProtocolType());
            protocol.put("version", sspCreateTransactionReq.getProtocolVersion());
            paymentDetails.put("protocol", protocol);

            JSONObject amount = new JSONObject();
            amount.put("option", sspCreateTransactionReq.getAmountOption());
            amount.put("currency", sspCreateTransactionReq.getAmountCurrency());
            amount.put("total", sspCreateTransactionReq.getAmountTotal());
            paymentDetails.put("amount", amount);

            JSONObject merchant = new JSONObject();
            merchant.put("name", sspCreateTransactionReq.getMerchantName());
            merchant.put("urk", sspCreateTransactionReq.getMerchantUrl());
            merchant.put("reference", sspCreateTransactionReq.getMerchantReference());
            paymentDetails.put("merchant", merchant);

            bodyRequest.put("paymentDetails", paymentDetails);

            HttpEntity<String> entity = new HttpEntity<>(bodyRequest.toString(), headers);
            String resourceUrl
                    = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(), channelFunction.getUrl());
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);
            ObjectMapper mapperObj = new ObjectMapper();

            CreateTransactionRes responseBody;
            try {
                paramsLog = new String[]{"URL: " + builderUri.toUriString() + ", body: [" + bodyRequest + "]"};
                logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_SSP_CREATE_TRANSACTION, true, true, false, paramsLog));

                ResponseEntity<CreateTransactionRes> response = restTemplate.exchange(builderUri.toUriString(),
                        HttpMethod.POST, entity, CreateTransactionRes.class);
                responseBody = response.getBody();
            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), CreateTransactionRes.class);
            }

            paramsLog = new String[]{responseBody.getResultCode(), responseBody.toString()};
            logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_CREATE_TRANSACTION, true, false, true, paramsLog));

            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            pgResponse.setData(responseBody);
            pgResponse.setErrorCode(responseBody.getResultCode());
            pgResponse.setMessage(responseBody.getResultMessage());
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_CREATE_TRANSACTION, false));
            return pgResponse;
        } catch (RestClientException | NoSuchAlgorithmException | KeyManagementException | IOException e) {
            logger.error(e.getMessage(), e);
            return (PGResponse) commonPGResponseService.returnChannelBadRequest(ExceptionUtils.getMessage(e)).getBody();
        }
    }

    @Override
    public PGResponse CBSDecodePaymentCredential(ChannelFunction channelFunction, String inputStr) throws Exception {
        RetrievePaymentCredentialReq retrieveReq = objectMapper.readValue(inputStr, RetrievePaymentCredentialReq.class);

        String[] paramsLog;
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL, true));

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<>();
            mapParams.add("serviceId", CybersourceConfig.SERVICE_ID_CBS_DECODE);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(retrieveReq.getRefId()).queryParams(mapParams);

            RetrievePaymentCredentialRes responseBody;
            ObjectMapper mapperObj = new ObjectMapper();
            PGResponse pgResponse = new PGResponse();
            try {
                paramsLog = new String[]{"URL: " + builderUri.toUriString()};
                logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL, true, true, false, paramsLog));

                ResponseEntity<RetrievePaymentCredentialRes> response
                        = restTemplate.exchange(builderUri.toUriString(), HttpMethod.GET, entity, RetrievePaymentCredentialRes.class);
                responseBody = response.getBody();

                paramsLog = new String[]{responseBody.getResultCode(), responseBody.getResultMessage()};
                logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL, true, false, true, paramsLog));

                String data3DS = responseBody.get_3DS().getData();
                String encryptedPaymentData = getEncryptedPaymentDataCaseCyberDecode(data3DS);

                Map<String, Object> jsonObj = new ObjectMapper().convertValue(responseBody, Map.class);
                jsonObj.put("encryptedPayment_Data", encryptedPaymentData);
                jsonObj.put("encryptedPayment_descriptor", "RklEPUNPTU1PTi5TQU1TVU5HLklOQVBQLlBBWU1FTlQ=");
                jsonObj.put("paymentNetworkToken_transactionType", "1");
                jsonObj.put("ccAuthService_commerceIndicator", "internet");
                jsonObj.put("paymentSolution", "008");
                pgResponse.setData(jsonObj);

            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), RetrievePaymentCredentialRes.class);
                pgResponse.setData(responseBody);
            }
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL, false));
            pgResponse.setStatus(true);
            pgResponse.setErrorCode(responseBody.getResultCode());
            pgResponse.setMessage(responseBody.getResultMessage());
            return pgResponse;

        } catch (RestClientException | NoSuchAlgorithmException | KeyManagementException | IOException e) {
            logger.error(e.getMessage(), e);
            return (PGResponse) commonPGResponseService.returnChannelBadRequest(ExceptionUtils.getMessage(e)).getBody();
        }

    }

    @Override
    public PGResponse NLDecodePaymentCredential(ChannelFunction channelFunction, String inputStr) throws Exception {
        RetrievePaymentCredentialReq retrieveReq = objectMapper.readValue(inputStr, RetrievePaymentCredentialReq.class);

        String[] paramsLog;
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL_DECODE, true));

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();
            mapParams.add("serviceId", CybersourceConfig.SERVICE_ID_NL_DECODE);

            String resourceUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl)
                    .pathSegment(retrieveReq.getRefId()).queryParams(mapParams);

            RetrievePaymentCredentialRes responseBody;
            ObjectMapper mapperObj = new ObjectMapper();
            PGResponse pgResponse = new PGResponse();
            try {
                paramsLog = new String[]{"URL: " + builderUri.toUriString()};
                logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL_DECODE, true, true, false, paramsLog));

                ResponseEntity<RetrievePaymentCredentialRes> response
                        = restTemplate.exchange(builderUri.toUriString(), HttpMethod.GET, entity, RetrievePaymentCredentialRes.class);
                responseBody = response.getBody();

                paramsLog = new String[]{responseBody.getResultCode(), responseBody.getResultMessage()};
                logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL_DECODE, true, false, true, paramsLog));

                //Decode payment data
                String data3DS = responseBody.get_3DS().getData();
                ClassLoader classLoader = CybersourceServiceImpl.class.getClassLoader();
                String privateKeyFilePath = Objects.requireNonNull(classLoader.getResource("ssp/rsapriv.der")).getFile();

                String encPayload = getDecodeJWStoJWE(data3DS);
                String plainText = getDecryptedData(encPayload, privateKeyFilePath);

                Map<String, Object> jsonObj = new ObjectMapper().convertValue(responseBody, Map.class);
                jsonObj.put("DecryptPayment_Data", plainText);

                pgResponse.setData(jsonObj);

            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), RetrievePaymentCredentialRes.class);
                pgResponse.setData(responseBody);
            }
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL_DECODE, false));
            pgResponse.setStatus(true);
            pgResponse.setErrorCode(responseBody.getResultCode());
            pgResponse.setMessage(responseBody.getResultMessage());
            return pgResponse;

        } catch (RestClientException | NoSuchAlgorithmException | KeyManagementException | IOException e) {
            logger.error(e.getMessage(), e);
            return (PGResponse) commonPGResponseService.returnChannelBadRequest(ExceptionUtils.getMessage(e)).getBody();
        }

    }

    @Override
    public PGResponse notifyPaymentResult(ChannelFunction channelFunction, String inputStr) throws Exception {
        NotifyPaymentResultReq notifyPaymentResultReq = objectMapper.readValue(inputStr, NotifyPaymentResultReq.class);

        String[] paramsLog;
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_NOTIFY_PAYMENT_RESULT, true));

            RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            JSONObject bodyRequest = new JSONObject();
            bodyRequest.put("timestamp", notifyPaymentResultReq.getTimestamp());

            JSONObject payment = new JSONObject();
            payment.put("reference", notifyPaymentResultReq.getReference());
            payment.put("status", notifyPaymentResultReq.getStatus());
            payment.put("service", notifyPaymentResultReq.getService());
            payment.put("provider", notifyPaymentResultReq.getProvider());

            JSONObject merchant = new JSONObject();
            merchant.put("name", notifyPaymentResultReq.getMerchantName());
            merchant.put("reference", notifyPaymentResultReq.getMerchantReference());
            payment.put("merchant", merchant);

            bodyRequest.put("payment", payment);

            HttpEntity<String> entity = new HttpEntity<>(bodyRequest.toString(), headers);
            String resourceUrl
                    = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(), channelFunction.getUrl());
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(resourceUrl);
            ObjectMapper mapperObj = new ObjectMapper();

            NotifyPaymentResultRes responseBody;
            try {
                paramsLog = new String[]{"URL: " + builderUri.toUriString() + ", body: [" + bodyRequest.toString() + "]"};
                logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_SSP_NOTIFY_PAYMENT_RESULT, true, true, false, paramsLog));

                ResponseEntity<NotifyPaymentResultRes> response
                        = restTemplate.exchange(builderUri.toUriString(), HttpMethod.POST, entity, NotifyPaymentResultRes.class);
                responseBody = response.getBody();

                paramsLog = new String[]{responseBody.getResultCode(), responseBody.getResultMessage()};
                logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_SSP_NOTIFY_PAYMENT_RESULT, true, false, true, paramsLog));

            } catch (HttpClientErrorException | HttpServerErrorException exx) {
                responseBody = mapperObj.readValue(exx.getResponseBodyAsString(), NotifyPaymentResultRes.class);
            }

            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            pgResponse.setData(responseBody);
            pgResponse.setErrorCode(responseBody.getResultCode());
            pgResponse.setMessage(responseBody.getResultMessage());
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_NOTIFY_PAYMENT_RESULT, false));
            return pgResponse;
        } catch (RestClientException | NoSuchAlgorithmException | KeyManagementException | IOException e) {
            logger.error(e.getMessage(), e);
            return (PGResponse) commonPGResponseService.returnChannelBadRequest(ExceptionUtils.getMessage(e)).getBody();
        }
    }

    @Override
    public PGResponse healthCheck() {
        String[] paramsLog;
        PGResponse pgResponse = new PGResponse();
        String healthcheckAddress = CybersourceConfig.URL_SSP_HEALTH_CHECK;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> healthcheckResponse = null;
        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_SSP_HEALTH_CHECK, true));
        try {
            MultiValueMap<String, String> mapParams = new LinkedMultiValueMap<String, String>();
            mapParams.add("paymentGatewayAddress", "https://alepay-samsum.nganluong.vn");
            UriComponentsBuilder builderUri = UriComponentsBuilder.fromHttpUrl(healthcheckAddress).queryParams(mapParams);

            paramsLog = new String[]{"URL: " + builderUri.toUriString()};
            logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_HEALTH_CHECK, true, true, false, paramsLog));

            healthcheckResponse = restTemplate.exchange(builderUri.toUriString(), HttpMethod.HEAD,
                    null, Void.class);

            paramsLog = new String[]{healthcheckResponse.getStatusCode().toString()};
            logger.info(commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_SSP_HEALTH_CHECK, true, false, true, paramsLog));

        } catch (RestClientException e) {
            pgResponse.setData(healthcheckResponse);
            pgResponse.setStatus(true);
            pgResponse.setMessage(HttpStatus.BAD_GATEWAY.getReasonPhrase());
            pgResponse.setErrorCode(HttpStatus.BAD_GATEWAY.toString());
            return pgResponse;
        }

        if (healthcheckResponse.getStatusCode() != HttpStatus.NO_CONTENT) {
            pgResponse.setData(healthcheckResponse);
            pgResponse.setStatus(true);
            pgResponse.setMessage(HttpStatus.BAD_GATEWAY.getReasonPhrase());
            pgResponse.setErrorCode(HttpStatus.BAD_GATEWAY.toString());
            return pgResponse;
        }

        pgResponse.setData(healthcheckResponse);
        pgResponse.setStatus(true);
        pgResponse.setErrorCode(CybersourceError.SUCCESS.getCode());
        pgResponse.setMessage(CybersourceError.SUCCESS.getMessage());
        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_SSP_HEALTH_CHECK, false));
        return pgResponse;
    }

    private String getDecodeJWStoJWEHeader(String data3DS) {
        String delims = "[.]";
        String[] tokens = data3DS.split(delims);
        String base64Payload = tokens[0];
        String base64 = base64Payload.replace('-', '+').replace('_', '/');
        byte[] encPayload = Base64.getUrlDecoder().decode(base64);
        return new String(encPayload, StandardCharsets.UTF_8);
    }

    private String getEncryptedPaymentDataCaseCyberDecode(String data3DS) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String headerStr = getDecodeJWStoJWEHeader(data3DS);
        Map<String, String> map = mapper.readValue(headerStr, Map.class);
        String kid = map.get("kid");

        //build structure
        JSONObject structure = new JSONObject();
        structure.put("publicKeyHash", Base64.getUrlEncoder().encodeToString(kid.getBytes()));
        structure.put("version", "100");
        structure.put("data", Base64.getUrlEncoder().encodeToString(data3DS.getBytes()));

        // encode structure
        String encodeStructure = Base64.getUrlEncoder().encodeToString(structure.toString().getBytes());
        return encodeStructure;
    }

    private String getDecodeJWStoJWE(String data3DS) {
        String delims = "[.]";
        String[] tokens = data3DS.split(delims);
        String base64Payload = tokens[1];
        String base64 = base64Payload.replace('-', '+').replace('_', '/');
        byte[] encPayload = Base64.getUrlDecoder().decode(base64);
        return new String(encPayload, StandardCharsets.UTF_8);
    }

    private String getDecryptedData(String encPayload, String privateKeyFilePath) {
        String delims = "[.]";
        String[] tokens = encPayload.split(delims);
        Base64.Decoder urlDecoder = Base64.getUrlDecoder();
        byte[] encKey = urlDecoder.decode(tokens[1]);
        byte[] iv = urlDecoder.decode(tokens[2]);
        byte[] cipherText = urlDecoder.decode(tokens[3]);
        byte[] tag = urlDecoder.decode(tokens[4]);
        byte[] plainText = new byte[cipherText.length];
        try {
            // Read private key file
            File privateKeyFile = new File(privateKeyFilePath);
            DataInputStream dis = new DataInputStream(new FileInputStream(privateKeyFile));
            byte[] privKeyBytes = new byte[(int) privateKeyFile.length()];
            dis.read(privKeyBytes);
            dis.close();
            // Set private key spec
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = keyFactory.generatePrivate(spec);
            Cipher decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] plainEncKey = decryptCipher.doFinal(encKey);
            final Cipher aes128Cipher = Cipher.getInstance("AES/GCM/NoPadding");
            final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(16 * Byte.SIZE, iv);
            final SecretKeySpec keySpec = new SecretKeySpec(plainEncKey, "AES");
            aes128Cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
            int offset = aes128Cipher.update(cipherText, 0, cipherText.length, plainText, 0);
            aes128Cipher.update(tag, 0, tag.length, plainText, offset);
            aes128Cipher.doFinal(plainText, offset);
        } catch (Exception e) {
        }
        return new String(plainText);

    }


    @Override
    public PGResponse createRequestJwt(String inputStr, String channel) throws Exception {
        JwtInput jwtInput = objectMapper.readValue(inputStr, JwtInput.class);
        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNTION_CREATE_REQUEST_JWT, true));
        String[] paramsLog = new String[]{"CREATE_JWT_REQUEST: [" + jwtInput.toString() + "]"};
        commonLogService.logInfoWithTransId(logger, jwtInput.getJti(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNTION_CREATE_REQUEST_JWT, true, true, false, paramsLog));

        long currencyCode = 0;
        if (jwtInput.getOrderObject().getCurrencyCode().equals("VND")) {
            currencyCode = 704;
        }
        if (jwtInput.getOrderObject().getCurrencyCode().equals("USD")) {
            currencyCode = 840;
        }
        Map order = new HashMap();
        order.put("OrderNumber", jwtInput.getOrderObject().getOrderNumber());
        order.put("Amount", jwtInput.getOrderObject().getAmount());
        order.put("CurrencyCode", currencyCode);
        Map data = new HashMap();
        data.put("OrderDetails", order);

        //get Key theo Mid Cybs
        ApiKey3ds2 apiKey3ds2 = getApiKey(channel);
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = apiKey3ds2.getApiKeySecretBytes().getBytes();
        String API_IDENTIFIER = apiKey3ds2.getAPI_IDENTIFIER();
        String ORG_UNIT = apiKey3ds2.getORG_UNIT();


        Key signingKey = new SecretKeySpec(apiKeySecretBytes,
                signatureAlgorithm.getJcaName());
        JwtBuilder builder = Jwts.builder()
                .setId(jwtInput.getJti())
                .setIssuedAt(now)
                .setIssuer(API_IDENTIFIER)
                .claim("OrgUnitId", ORG_UNIT)
                .claim("Payload", data)
//                .claim("ConfirmUrl", jwtInput.getConfirmUrl())
//                .claim("ReferenceId", jwtInput.getReferenceId())
                .claim("ObjectifyPayload", true)
                .signWith(signatureAlgorithm, signingKey);
        if (CybersourceConfig.TTLMILLIS > 0) {
            long expMillis = nowMillis + CybersourceConfig.TTLMILLIS;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        String jwt = builder.compact();
        PGResponse response = new PGResponse();
        response.setData(jwt);
        response.setStatus(true);
        response.setErrorCode(CybersourceError.SUCCESS.getCode());
        response.setMessage(CybersourceError.SUCCESS.getMessage());

        paramsLog = new String[]{"JWT_REPONSE: [" + response.getData().toString() + "]"};
        commonLogService.logInfoWithTransId(logger, jwtInput.getJti(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNTION_CREATE_REQUEST_JWT, true, false, true, paramsLog));
        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNTION_CREATE_REQUEST_JWT, false));

        return response;
    }

    @Override
    public PGResponse payerAuthenticationSetup3ds(String inputStr, String channel) throws Exception {
        startStopMethodLog(CybersourceConfig.FUNCTION_SETUP_3DS2,true);

        Authentication3ds authentication3ds = objectMapper.readValue(inputStr, Authentication3ds.class);
        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", getMID(channel));
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(authentication3ds.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : authentication3ds.getTransactionReferenceId())
        );
        cybersourceUtil.addInvoiceHeader(request, authentication3ds.getInvoiceHeader(), channel, authentication3ds.getCard());
        cybersourceUtil.addBillAddress(request, authentication3ds.getBillAddress());
        cybersourceUtil.addCurrency(request,authentication3ds.getOrderCurrency());
        cybersourceUtil.addCard(request, authentication3ds.getCard());
        cybersourceUtil.addMerchantDefinedDataField(request,
                                                 authentication3ds.getMerchantDefinedDataField5(),
                                                 authentication3ds.getMerchantDefinedDataField6());

        request.put("subscription", null);
        request.put("subscription.title", "Create Customer Profile");
        request.put("subscription.paymentMethod", "credit card");
        request.put("recurringSubscriptionInfo", null);
        request.put("recurringSubscriptionInfo.frequency", "on-demand");

        if(authentication3ds.getGrandTotalAmount()==0){
            request.put("purchaseTotals.grandTotalAmount", Long.toString(authentication3ds.getGrandTotalAmount()));
            request.put("ccAuthService", null);
            request.put("ccAuthService_run", "true");
            cybersourceUtil.addAuthServiceDependChannel(request, channel);

        }
        if(authentication3ds.getGrandTotalAmount()>0){
            request.put("purchaseTotals.grandTotalAmount", Long.toString(authentication3ds.getGrandTotalAmount()));
            request.put("ccAuthService", null);
            request.put("ccAuthService_run", "true");
            cybersourceUtil.addAuthServiceDependChannel(request, channel);
            request.put("ccCaptureService", null);
            request.put("ccCaptureService_run", "true");
        }
        if(authentication3ds.getTurnOn3ds() != null && authentication3ds.getTurnOn3ds().equals("Y")) {
            request.put("payerAuthEnrollService", null);
            request.put("payerAuthEnrollService_run", "true");
            request.put("payerAuthEnrollService.referenceID", authentication3ds.getReferenceID());

        }
        Payment payment = paymentService.findByMerchantTransactionId(authentication3ds.getTransactionReferenceId());
        if(payment != null){
            request.put("payerAuthValidateService", null);
            request.put("payerAuthValidateService_run", "true");
            request.put("payerAuthValidateService.authenticationTransactionID", authentication3ds.getAuthenticationTransactionID());
        }
        request.put("paySubscriptionCreateService", null);
        request.put("paySubscriptionCreateService_run", "true");

        Map<String, String> maskRequest = cybersourceUtil.getLogRequest(request);
        String[] paramsLog = new String[]{"REQUEST_TOKEN: [" + maskRequest.toString() + "]"};

        printLogWithTransaction(authentication3ds.getTransactionReferenceId(),
                CybersourceConfig.FUNCTION_SETUP_3DS2,true,paramsLog);

        PGResponse response = cybersourceUtil.createCustomerProfile3ds2(request);

        paramsLog = new String[]{"RESPONSE_TOKEN: [" + response.getData().toString() + "]"};
        printLogWithTransaction(authentication3ds.getTransactionReferenceId(),
                CybersourceConfig.FUNCTION_SETUP_3DS2,false,paramsLog);

        startStopMethodLog(CybersourceConfig.FUNCTION_SETUP_3DS2,false);
        createPayment(inputStr,response,channel,"request");
        return response;
    }

    @Override
    public PGResponse authorizeSubscription3D(String inputStr, String channel) throws Exception{

        startStopMethodLog(CybersourceConfig.AUTHORIZE_SUBSCRIPTION_3D, true);

        AuthorizeSubscription3D authorization = objectMapper.readValue(inputStr, AuthorizeSubscription3D.class);
        if("".equals(authorization.getAmount()) || Long.parseLong(authorization.getAmount())<0){
            return PGResponse.getInstanceWhenError(PGResponse.DATA_INVALID);
        }
        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", getMID(channel));
        request.put("merchantReferenceCode",
                 (StringUtils.isEmpty(authorization.getOrderNumber())
                        ? UUID.randomUUID().toString() : authorization.getOrderNumber())
        );
        cybersourceUtil.addInvoiceHeader(request, authorization.getInvoiceHeader(), channel, authorization.getCard());
        cybersourceUtil.addCurrency(request,authorization.getOrderCurrency());
        request.put("purchaseTotals.grandTotalAmount", authorization.getAmount());

        request.put("recurringSubscriptionInfo", null);
        request.put("recurringSubscriptionInfo.subscriptionID", authorization.getSubscriptionId());

        cybersourceUtil.addMerchantDefinedDataField(request, authorization.getMerchantDefinedDataField5(), authorization.getMerchantDefinedDataField6());

        request.put("ccAuthService", null);
        request.put("ccAuthService_run", "true");
        cybersourceUtil.addAuthServiceDependChannel(request, channel);
        request.put("ccCaptureService", null);
        request.put("ccCaptureService_run", "true");

        if(authorization.getTurnOn3ds() != null && authorization.getTurnOn3ds().equals("Y")) {
            request.put("payerAuthEnrollService", null);
            request.put("payerAuthEnrollService_run", "true");
            request.put("payerAuthEnrollService.referenceID", authorization.getReferenceID());
        }
        Payment payment = paymentService.findByMerchantTransactionId(authorization.getOrderNumber());
        if(payment != null){
            request.put("payerAuthValidateService", null);
            request.put("payerAuthValidateService_run", "true");
            request.put("payerAuthValidateService.authenticationTransactionID", authorization.getAuthenticationTransactionID());
        }
        String[] paramsLog = new String[]{"CHECK_ENROLLMENT3DS2_REQUEST_TOKEN: [" + request + "]"};
        commonLogService.logInfoWithTransId(logger, authorization.getOrderNumber(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.AUTHORIZE_SUBSCRIPTION_3D, true, true, false, paramsLog));
        PGResponse response = cybersourceUtil.authorizeSubscription3D(request);


        paramsLog = new String[]{"CHECK_ENROLLMENT3DS2_RESPONSE_TOKEN: [" + response.getData().toString() + "]"};
        commonLogService.logInfoWithTransId(logger, authorization.getOrderNumber(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.AUTHORIZE_SUBSCRIPTION_3D, true, false, true, paramsLog));

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.AUTHORIZE_SUBSCRIPTION_3D, false));
        createPayment(inputStr,response,channel,"payment");
        return response;
    }

    @Override
    public PGResponse checkEnrollment3ds(String inputStr, String channel) throws Exception {
        CheckEnrollment checkEnrollment = objectMapper.readValue(inputStr, CheckEnrollment.class);
        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_CHECK_ENROLLMENT_3DS2, true));

        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", getMID(channel));
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(checkEnrollment.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : checkEnrollment.getTransactionReferenceId())
        );
        cybersourceUtil.addInvoiceHeader(request, checkEnrollment.getInvoiceHeader(), channel, checkEnrollment.getCard());
        cybersourceUtil.addBillAddress(request, checkEnrollment.getBillAddress());
        request.put("purchaseTotals", null);
        request.put("purchaseTotals.currency", checkEnrollment.getOrderCurrency());
        cybersourceUtil.addCard(request, checkEnrollment.getCard());

        cybersourceUtil.addMerchantDefinedDataField(request, checkEnrollment.getMerchantDefinedDataField5(), checkEnrollment.getMerchantDefinedDataField6());

        if (checkEnrollment.getGrandTotalAmount() > 0) {
            request.put("purchaseTotals.grandTotalAmount", String.valueOf(checkEnrollment.getGrandTotalAmount()));
            request.put("ccAuthService", null);
            request.put("ccAuthService_run", "true");
            cybersourceUtil.addAuthServiceDependChannel(request, channel);
            request.put("ccCaptureService", null);
            request.put("ccCaptureService_run", "true");
        }
        request.put("payerAuthEnrollService", null);
        request.put("payerAuthEnrollService_run", "true");
        request.put("payerAuthEnrollService.referenceID", checkEnrollment.getReferenceID());
        // request.put("payerAuthEnrollService.acsWindowSize", "3");
        // request.put("payerAuthEnrollService.returnURL", "nganluong.vn");

        Map<String, String> maskRequest = cybersourceUtil.getLogRequest(request);
        String[] paramsLog = new String[]{"CHECK_ENROLLMENT3DS2_REQUEST: [" + maskRequest.toString() + "]"};
        commonLogService.logInfoWithTransId(logger, checkEnrollment.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_CHECK_ENROLLMENT_3DS2, true, true, false, paramsLog));

        PGResponse response = cybersourceUtil.checkEnrollment3ds2(request);
        paramsLog = new String[]{"CHECK_ENROLLMENT3DS2_REPONSE: [" + response.getData().toString() + "]"};
        commonLogService.logInfoWithTransId(logger, checkEnrollment.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_CHECK_ENROLLMENT_3DS2, true, false, true, paramsLog));

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_CHECK_ENROLLMENT_3DS2, false));

        //luu db payment
        //cast data object sang json
        String rp = objectMapper.writeValueAsString(response.getData());

        //map data object sang Object Respone
        Response rs = objectMapper.readValue(objectMapper.writeValueAsString(response.getData()), Response.class);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(10);
        paymentDTO.setPgFunctionId(pgFunctionService.findPgFunctionByCode(CybersourceConfig.FUNCTION_CHECK_ENROLLMENT_3DS2).getId());
        paymentDTO.setMerchantCode(channel);
        paymentDTO.setMerchantName(pgUserService.findByCode(channel).getName());
        paymentDTO.setMerchantTransactionId(checkEnrollment.getTransactionReferenceId());
        paymentDTO.setAmount(String.valueOf(checkEnrollment.getGrandTotalAmount()));
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_RESPONE_ENROLLMENT_TRANSACTION + objectMapper.writeValueAsString(maskRequest));
        paymentDTO.setTimeCreated(cal.getTime());
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_RESPONE_ENROLLMENT_TRANSACTION + objectMapper.writeValueAsString(response.getData()));
        paymentDTO.setChannelTransactionId(LogConst.LOG_CONTENT_PREFIX_RESPONE_ENROLLMENT_TRANSACTION + rs.getPaymentTransactionId());
        if (rs.getResponseCode().equals(CybersourceConfig.TRANSACTION_SUCCESS)) {
            setTransactionStatus(rs, paymentDTO);
        }
        paymentService.createPayment(paymentDTO);
        return response;
    }

    @Override
    public PGResponse decodeJWT(String inputStr, String channel) throws Exception {

        return null;
    }

    @Override
    public PGResponse validate3Ds2(String inputStr, String channel) throws Exception {
        ValidateAuthentication3ds authorize3D = objectMapper.readValue(inputStr, ValidateAuthentication3ds.class);

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_VALIDATE_3DS, true));

        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", getMID(channel));
        request.put("merchantReferenceCode",
//                PREFIX_TRANSACTION_CODE +
                        (StringUtils.isEmpty(authorize3D.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : authorize3D.getTransactionReferenceId())
        );
        cybersourceUtil.addInvoiceHeader(request, authorize3D.getInvoiceHeader(), channel, authorize3D.getCard());
        cybersourceUtil.addBillAddress(request, authorize3D.getBillAddress());
        request.put("purchaseTotals", null);
        request.put("purchaseTotals.currency", authorize3D.getOrderCurrency());
        cybersourceUtil.addCard(request, authorize3D.getCard());

        cybersourceUtil.addMerchantDefinedDataField(request, authorize3D.getMerchantDefinedDataField5(), authorize3D.getMerchantDefinedDataField6());


        if (authorize3D.getGrandTotalAmount() > 0) {
            request.put("purchaseTotals.grandTotalAmount", String.valueOf(authorize3D.getGrandTotalAmount()));
            request.put("ccAuthService", null);
            request.put("ccAuthService_run", "true");
            cybersourceUtil.addAuthServiceDependChannel(request, channel);
            request.put("ccCaptureService", null);
            request.put("ccCaptureService_run", "true");
        }
        request.put("payerAuthValidateService", null);
        request.put("payerAuthValidateService_run", "true");
        request.put("payerAuthValidateService.authenticationTransactionID", authorize3D.getAuthenticationTransactionID());


        Payment payment = paymentService.findByMerchantTransactionId(authorize3D.getTransactionReferenceId());
        if(payment != null && payment.getPgTransactionStatus() == 1){
            PGResponse response = new PGResponse();
            response.setStatus(false);
            response.setErrorCode("21");
            response.setMessage("TransactionId đã được sử dụng trong đơn hàng đã được thanh toán");

            String param = objectMapper.writeValueAsString(response);
            String[] paramsLog = new String[]{"VALIDATE_3DS2_REPONSE: [" + param + "]"};
            commonLogService.logInfoWithTransId(logger, authorize3D.getTransactionReferenceId(),
                    commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                            CybersourceConfig.FUNCTION_VALIDATE_3DS, true, false, true, paramsLog));
            logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                    CybersourceConfig.FUNCTION_VALIDATE_3DS, false));
            return response;
        }
        Map<String, String> maskRequest = cybersourceUtil.getLogRequest(request);
        String[] paramsLog = new String[]{"VALIDATE_3DS2_REQUEST: [" + maskRequest.toString() + "]"};
        commonLogService.logInfoWithTransId(logger, authorize3D.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_VALIDATE_3DS, true, true, false, paramsLog));

        PGResponse response = cybersourceUtil.runTransaction3ds2(request, Constant.AUTH);

        paramsLog = new String[]{"VALIDATE_3DS2_REPONSE: [" + response.getData().toString() + "]"};
        commonLogService.logInfoWithTransId(logger, authorize3D.getTransactionReferenceId(),
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        CybersourceConfig.FUNCTION_VALIDATE_3DS, true, false, true, paramsLog));

        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_VALIDATE_3DS, false));

        //update payment
        String rp = objectMapper.writeValueAsString(response.getData());
        //map data object sang Object Respone
        Response rs = objectMapper.readValue(objectMapper.writeValueAsString(response.getData()), Response.class);
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_RESPONE_VALIDATE_TRANSACTION + objectMapper.writeValueAsString(response.getData()));
        paymentDTO.setMerchantTransactionId(authorize3D.getTransactionReferenceId());
        paymentDTO.setChannelTransactionId(LogConst.LOG_CONTENT_PREFIX_RESPONE_VALIDATE_TRANSACTION + rs.getPaymentTransactionId());
        paymentDTO.setChannelId(10);
        paymentDTO.setPgFunctionId(pgFunctionService.findPgFunctionByCode(CybersourceConfig.FUNCTION_CHECK_ENROLLMENT_3DS2).getId());
        paymentDTO.setMerchantCode(channel);
        paymentDTO.setMerchantName(pgUserService.findByCode(channel).getName());
        paymentDTO.setMerchantTransactionId(authorize3D.getTransactionReferenceId());
        paymentDTO.setAmount(String.valueOf(authorize3D.getGrandTotalAmount()));
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_RESPONE_VALIDATE_TRANSACTION + objectMapper.writeValueAsString(maskRequest));
        if (rs.getResponseCode().equals("100")) {
            setTransactionStatus(rs, paymentDTO);
        }
        if (paymentService.findByMerchantTransactionId(paymentDTO.getMerchantTransactionId()) == null) {
            paymentService.createPayment(paymentDTO);
        } else {
            paymentService.updateChannelTransactionStatusPayment(paymentDTO);
        }
        return response;
    }

    private void setTransactionStatus(Response rs, PaymentDTO paymentDTO) {
        if (rs.getEci() != null) {
            switch (rs.getEci()) {
                case "05":
                case "02":
                case "01":
                case "06":
                    paymentDTO.setPgTransactionStatus(1);
                    break;
                case "07":
                case "00":
                    paymentDTO.setPgTransactionStatus(4);
                    break;
                default:
                    paymentDTO.setPgTransactionStatus(0);
            }
        } else {
            paymentDTO.setPgTransactionStatus(0);
        }
    }

    @Override
    public PGResponse validateJWT(PGRequest pgRequest, String channel) {
        String res;
        String API_KEY = CybersourceConfig.apiKey3ds2Map.get(channel).getApiKeySecretBytes();
        try {
            Claims claims = (Claims) Jwts.parser()
                    .setSigningKey(API_KEY.getBytes())
                    .parse(pgRequest.getData())
                    .getBody();
            System.out.println(claims);
            res = "SUCCESS";
        } catch (Exception se) {
            res = "FAILER VALIDATE JWT";
        }
        return PGResponse.builder()
                .status(true)
                .data(res)
                .errorCode(CybersourceError.SUCCESS.getCode())
                .message(CybersourceError.SUCCESS.getMessage()).build();
    }

    @Override
    public PGResponse deleteToken(String inputStr, String channel) throws Exception {
        startStopMethodLog(CybersourceConfig.DELETE_TOKEN, true);

        CancelAuthorizeCard cancelAuthorizeCard = objectMapper.readValue(inputStr, CancelAuthorizeCard.class);

        if (cancelAuthorizeCard.getSubscriptionId() == null || "".equals(cancelAuthorizeCard.getSubscriptionId())){
            return PGResponse.getInstanceWhenError(PGResponse.DATA_INVALID);
        }
        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID", getMID(channel));
        request.put("merchantReferenceCode",
                PREFIX_TRANSACTION_CODE + (StringUtils.isEmpty(cancelAuthorizeCard.getTransactionReferenceId())
                        ? UUID.randomUUID().toString() : cancelAuthorizeCard.getTransactionReferenceId())
        );
        request.put("recurringSubscriptionInfo", null);
        request.put("recurringSubscriptionInfo.subscriptionID", cancelAuthorizeCard.getSubscriptionId());
        request.put("paySubscriptionDeleteService", null);
        request.put("paySubscriptionDeleteService_run", "true");

        printLogWithTransaction(cancelAuthorizeCard.getTransactionReferenceId(),
                CybersourceConfig.DELETE_TOKEN,
                true,
                new String[]{"DELETE_TOKEN: [" + request + "]"});

        PGResponse response = cybersourceUtil.deleteToken(request); /* send request */

        printLogWithTransaction(cancelAuthorizeCard.getTransactionReferenceId(),
                CybersourceConfig.DELETE_TOKEN,
                false,
                new String[]{"DELETE_TOKEN: [" + response.getData().toString() + "]"});

        startStopMethodLog(CybersourceConfig.DELETE_TOKEN,false);
        createPayment(inputStr,response,channel,"delete");
        return response;
    }

    private ApiKey3ds2 getApiKey(String channel) {
        return CybersourceConfig.apiKey3ds2Map.get(channel);
    }

    private String getMID(String channel) {
        switch (channel) {
            case "NL_CYBS_VTB":
                return CybersourceConfig.MERCHANT_ID_VTB;

            case "NL_CYBS_STB":
                return CybersourceConfig.MERCHANT_ID_STB;

            case "NL_CYBS_STB_TRAVEL":
                return CybersourceConfig.MERCHANT_ID_STB_TRAVEL;

            case "NL_CYBS_STB_CBS":
                return CybersourceConfig.MERCHANT_ID_STB_CBS;

            case "NL_CYBS_STB_TIKI":
                return CybersourceConfig.MERCHANT_ID_STB_TIKI;

            case "NL_CYBS_STB_BH":
                return CybersourceConfig.MERCHANT_ID_STB_BH;

            case "NL_CYBS_STB_VISA":
                return CybersourceConfig.MERCHANT_ID_STB_VISA;

            case "NL_CYBS_STB_DVC1":
                return CybersourceConfig.MERCHANT_ID_STB_DVC1;

            case "NL_CYBS_STB_DVC2":
                return CybersourceConfig.MERCHANT_ID_STB_DVC2;

            case "NL_CYBS_VCB":
                return CybersourceConfig.MERCHANT_ID_VCB;

            case "NL_CYBS_VCB_SGD":
                return CybersourceConfig.MERCHANT_ID_VCB_SGD;

            case "NL_CYBS_VCB_TH":
                return CybersourceConfig.MERCHANT_ID_VCB_TH;

            case "NL_CYBS_VCB_ST":
                return CybersourceConfig.MERCHANT_ID_VCB_ST;

            case "NL_CYBS_VCB_OTHERS":
                return CybersourceConfig.MERCHANT_ID_VCB_OTHERS;

            case "NL_CYBS_SCB_ALOTHERS":
                return CybersourceConfig.MERCHANT_ID_SCB_ALOTHER;

            case "NL_CYBS_SCB_VANTAI":
                return CybersourceConfig.MERCHANT_ID_SCB_VANTAICONG;
            case "NL_CYBS_STB_BAOHIEM":
                return CybersourceConfig.MERCHANT_ID_STB_BAOHIEM;
            case "NL_CYBS_VCB_BAOHIEM":
                return CybersourceConfig.MERCHANT_ID_VCB_BAOHIEM;
            default:
                return "";
        }
    }

    private void createPayment(String request, PGResponse pgResponse, String channel, String method) throws Exception{
        String response = objectMapper.writeValueAsString(pgResponse.getData());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        PaymentDTO payment = new PaymentDTO();
        payment.setChannelId(10);
        payment.setMerchantCode(channel);
        payment.setMerchantName(getMID(channel));
        payment.setRawRequest(request);
        payment.setRawResponse(response);
        payment.setTimeCreated(cal.getTime());
        SubResponse subResponse = objectMapper.readValue(response, SubResponse.class);
        switch(method){
            case "request":
                Authentication3ds authentication3ds = objectMapper.readValue(request, Authentication3ds.class);
                payment.setMerchantTransactionId(authentication3ds.getTransactionReferenceId());
                payment.setVirtualAccountNo(subResponse.getData().getSubscriptionId());
                payment.setDescription(authentication3ds.getKeepSubscript());
                payment.setPgFunctionId(pgFunctionService.findPgFunctionByCode(CybersourceConfig.FUNCTION_SETUP_3DS2).getId());
                Payment paymentDB = paymentService.findByMerchantTransactionId(authentication3ds.getTransactionReferenceId());
                if(paymentDB != null){
                    paymentService.updatePayment(payment);
                    return;
                }
                break;
            case "payment":
                AuthorizeSubscription3D authorizeSubscription3D = objectMapper.readValue(request, AuthorizeSubscription3D.class);
                payment.setMerchantTransactionId(authorizeSubscription3D.getOrderNumber());
                payment.setAmount(authorizeSubscription3D.getAmount());
                payment.setPgFunctionId(pgFunctionService.findPgFunctionByCode(CybersourceConfig.AUTHORIZE_SUBSCRIPTION_3D).getId());
                if (CybersourceConfig.TRANSACTION_SUCCESS.equals(pgResponse.getChannelErrorCode())){
                    payment.setPgTransactionStatus(1);
                }
                payment.setPgFunctionId(pgFunctionService.findPgFunctionByCode(CybersourceConfig.FUNCTION_SETUP_3DS2).getId());
                Payment paymentDB2 = paymentService.findByMerchantTransactionId(authorizeSubscription3D.getOrderNumber());
                if(paymentDB2 != null){
                    paymentService.updatePayment(payment);
                    return;
                }
                break;
            case "delete":
                CancelAuthorizeCard cancel = objectMapper.readValue(request, CancelAuthorizeCard.class);
                payment.setMerchantTransactionId(cancel.getTransactionReferenceId());
                payment.setPgFunctionId(pgFunctionService.findPgFunctionByCode(CybersourceConfig.DELETE_TOKEN).getId());
                break;
            case "convert":
                Transaction2Profile transaction = objectMapper.readValue(request, Transaction2Profile.class);
                payment.setMerchantTransactionId(transaction.getTransactionReferenceId());
                payment.setVirtualAccountNo(subResponse.getData().getSubscriptionId());
                payment.setDescription("Y");
                payment.setPgFunctionId(pgFunctionService.findPgFunctionByCode(CybersourceConfig.FUNCTION_CONVERT_TRANSACTION_TO_PROFILE).getId());
        }

         paymentService.createPayment(payment);
    }


    @Override
    public PGResponse convertTransaction2Profile(String inputStr, String channel) throws Exception {
        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_CONVERT_TRANSACTION_TO_PROFILE, true));
        Transaction2Profile profile = objectMapper.readValue(inputStr, Transaction2Profile.class);
        if(profile.getPaymentTransactionId() == null || "".equals(profile.getPaymentTransactionId())){
            return PGResponse.getInstanceWhenError(PGResponse.DATA_INVALID);
        }
        final Map<String, String> request = new LinkedHashMap<>();
        request.put("merchantID",getMID(channel));
        request.put("merchantReferenceCode",profile.getTransactionReferenceId());

        request.put("recurringSubscriptionInfo", null);
        request.put("recurringSubscriptionInfo.frequency", "on-demand");
        request.put("paySubscriptionCreateService", null);
        request.put("paySubscriptionCreateService_run", "true");
        request.put("paySubscriptionCreateService.paymentRequestID", profile.getPaymentTransactionId());


        printLogWithTransaction(profile.getTransactionReferenceId(),
                CybersourceConfig.FUNCTION_CONVERT_TRANSACTION_TO_PROFILE,
                true,
                new String[]{"VALIDATE_3DS2_REQUEST: [" + request + "]"});
        PGResponse response = cybersourceUtil.convertTransaction(request);

        printLogWithTransaction(profile.getTransactionReferenceId(),
                CybersourceConfig.FUNCTION_CONVERT_TRANSACTION_TO_PROFILE,
                false,
                new String[]{"VALIDATE_3DS2_REPONSE: [" + response.getData().toString() + "]"});


        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                CybersourceConfig.FUNCTION_VALIDATE_3DS, false));
        createPayment(inputStr,response,channel,"convert");
        return response;
    }
    private void startStopMethodLog(String function, boolean isStartMethod){
        logger.info(commonLogService.createContentLogStartEndFunction(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                function, isStartMethod));
    }
    private void printLogWithTransaction(String transaction, String function, boolean isRequest, String[] logs){
        commonLogService.logInfoWithTransId(logger, transaction,
                commonLogService.createContentLog(CybersourceConfig.CHANNEL_CODE, CybersourceConfig.SERVICE_NAME,
                        function, true, isRequest, !isRequest, logs));
    }
}
