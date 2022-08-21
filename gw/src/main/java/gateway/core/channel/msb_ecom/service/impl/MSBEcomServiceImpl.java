package gateway.core.channel.msb_ecom.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.msb_ecom.MSBEcomClientRequest;
import gateway.core.channel.msb_ecom.dto.MSBEcomConstant;
import gateway.core.channel.msb_ecom.dto.req.*;
import gateway.core.channel.msb_ecom.dto.res.*;
import gateway.core.channel.msb_ecom.service.MSBEcomService;
import gateway.core.dto.PGResponse;
import gateway.core.util.PGSecurity;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.repository.ChannelFunctionRepository;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.service.PgLogChannelFunctionService;
import vn.nganluong.naba.utils.RequestUtil;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Service
public class MSBEcomServiceImpl extends PaymentGate implements MSBEcomService {

    private static final Logger logger = LogManager.getLogger(MSBEcomServiceImpl.class);
    private static final String SERVICE_NAME = "MSB ECOM";

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelFunctionRepository channelFunctionRepository;

    @Autowired
    private CommonPGResponseService commonPGResponseService;

    @Autowired
    private CommonLogService commonLogService;

    @Autowired
    private PgLogChannelFunctionService pgLogChannelFunctionService;

    @Override
    public PGResponse createPayment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_CREATE_PAYMENT, true));

            RootRequest body = objectMapper.readValue(inputStr, RootRequest.class);

            CreatePaymentRequest request = new CreatePaymentRequest();
            // set tham số tĩnh
            request.setCurrency(MSBEcomConstant.CCY);
            request.setPaymentType(MSBEcomConstant.PAYMENT_TYPE);
            request.setMerchantID(paymentAccount.getMerchantId()); // MSBEcomConstant.MERCHANT_ID

            request.setCardName(body.getCardName());
            request.setCardNumber(body.getCardNumber());
            request.setReleaseMonth((long) body.getReleaseMonth());
            request.setReleaseYear((long) body.getReleaseYear());

            // set tham số thanh toán
            request.setMerchTxnRef(body.getTransId());
            request.setOrderInfo(body.getOrderInfo());
            request.setAmount(String.format("%.0f", body.getAmount()));

            // Check order id (merchant id) is exist or not
            Payment paymentToCheckExist = paymentService
                    .findByMerchantTransactionId(body.getTransId());
            String[] paramsLog;
            if (paymentToCheckExist != null) {
                paramsLog = new String[]{"Transaction id (trace id) already exist ("
                        + body.getTransId() + ")"};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_CREATE_PAYMENT, false, false, true, paramsLog));

                logger.info(
                        commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                                MSBEcomConstant.FUNCTION_CODE_CREATE_PAYMENT, false));
                return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
            }

            // Create payment data
            PaymentDTO paymentDTO = createPaymentData(channelFunction, request);
            // set chuỗi mã hóa
            // PaymentAccount.EncryptKey là access code
            request.setSecureHash(PGSecurity.sha256(request.buildDataRaw(paymentAccount.getEncryptKey())));

            // WriteInfoLog("1. MSB ECOM REQ - CreatePayment API:",  body.getTransId() + PGUtil.CHARACTER + objectMapper.writeValueAsString(request));
            paramsLog = new String[]{objectMapper.writeValueAsString(request)};
            logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_CREATE_PAYMENT, true, true, false, paramsLog));


            String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                    channelFunction.getUrl());

            PGResponse pgResponse = process(request, body, endpointUrl,
                    MediaType.APPLICATION_JSON, MSBEcomConstant.METHOD_POST,
                    new TypeReference<CreatePaymentResponse>() {
                    });

            String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);

            if (NumberUtils.compare(Integer.parseInt(pgResponse.getErrorCode()),
                    MSBEcomConstant.API_RESPONSE_STATUS_CODE_SUCCESS) == 0) {
                paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            } else {
                paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            }
            paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_CREATE_PAYMENT, false));

            return pgResponse;

        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse verifyTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                        String inputStr) {
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, true));

            RootRequest body = objectMapper.readValue(inputStr, RootRequest.class);

            VerifyTransactionRequest request = new VerifyTransactionRequest();
            // set tham số tĩnh
            request.setMerchantID(paymentAccount.getMerchantId()); // MSBEcomConstant.MERCHANT_ID

            request.setTransId(body.getRequestTransId());
            request.setOtp(body.getOtp());

            // Check order id (merchant id) is exist or not
            Payment paymentToCheckExist = paymentService
                    .findByMerchantTransactionId(body.getRequestTransId());
            String[] paramsLog;
            if (paymentToCheckExist == null) {
                paramsLog = new String[]{"Transaction id (trace id) not exist ("
                        + body.getTransId() + ")"};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, false, false, true, paramsLog));

                logger.info(
                        commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                                MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, false));
                return commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();
            }

            // set chuỗi mã hóa
            // PaymentAccount.EncryptKey là access code
            request.setSecureHash(PGSecurity.sha256(request.buildDataRaw(paymentAccount.getEncryptKey())));

            // WriteInfoLog("1. MSB ECOM REQ - VerifyTransaction API:",  body.getTransId() + PGUtil.CHARACTER + objectMapper.writeValueAsString(request));
            paramsLog = new String[]{objectMapper.writeValueAsString(request)};
            logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, true, true, false, paramsLog));

            String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                    channelFunction.getUrl());
            PGResponse pgResponse = process(request, body,
                    endpointUrl, MediaType.APPLICATION_JSON,
                    MSBEcomConstant.METHOD_POST, new TypeReference<VerifyTransactionResponse>() {
                    });

            String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setMerchantTransactionId(body.getTransId());
            paymentDTO.setRawRequest(
                    LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + objectMapper.writeValueAsString(request));
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + responseStr);
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());

            if (NumberUtils.compare(Integer.parseInt(pgResponse.getErrorCode()),
                    MSBEcomConstant.API_RESPONSE_STATUS_CODE_SUCCESS) == 0) {

                paramsLog = new String[]{paymentDTO.getRawResponse()};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, true, false, true, paramsLog));
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(MSBEcomConstant.CHANNEL_CODE,
                        MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, false);
                paramsLog = new String[]{paymentDTO.getRawResponse()};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, false, false, true, paramsLog));
            }
            paymentService.updateChannelTransactionStatusPayment(paymentDTO);

            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, false));
            return pgResponse;
        } catch (Exception e) {
            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_VERIFY_TRANSACTION, false));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse resendOTP(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {

        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, true));

            RootRequest body = objectMapper.readValue(inputStr, RootRequest.class);

            ResendOTPRequest request = new ResendOTPRequest();
            // set tham số tĩnh
            request.setMerchantId(paymentAccount.getMerchantId()); // MSBEcomConstant.MERCHANT_ID

            request.setTransId(body.getTransId());

            // Check order id (merchant id) is exist or not
            Payment paymentToCheckExist = paymentService
                    .findByMerchantTransactionId(body.getTransId());
            String[] paramsLog;
            if (paymentToCheckExist == null) {
                paramsLog = new String[]{"Transaction id (trace id) not exist ("
                        + body.getTransId() + ")"};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, false, false, true, paramsLog));

                logger.info(
                        commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                                MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, false));
                return commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();
            }

            // set chuỗi mã hóa
            // PaymentAccount.EncryptKey là access code
            request.setSecureHash(PGSecurity.sha256(request.buildDataRaw(paymentAccount.getEncryptKey())));

            // WriteInfoLog("1. MSB ECOM REQ - resendOTP API:",  body.getTransId() + PGUtil.CHARACTER + objectMapper.writeValueAsString(request));
            paramsLog = new String[]{objectMapper.writeValueAsString(request)};
            logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, true, true, false, paramsLog));

            String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                    channelFunction.getUrl());
            PGResponse pgResponse = process(request, body, endpointUrl,
                    MediaType.APPLICATION_JSON, MSBEcomConstant.METHOD_POST, new TypeReference<ResendOTPResponse>() {
                    });

            String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setMerchantTransactionId(body.getTransId());
            paymentDTO.setRawRequest(
                    LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + objectMapper.writeValueAsString(request));
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_CONFIRM_TRANSACTION + responseStr);
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());

            if (NumberUtils.compare(Integer.parseInt(pgResponse.getErrorCode()),
                    MSBEcomConstant.API_RESPONSE_STATUS_CODE_SUCCESS) == 0) {

                paramsLog = new String[]{paymentDTO.getRawResponse()};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, true, false, true, paramsLog));
            } else {
                pgLogChannelFunctionService.writeLogChannelFunction(MSBEcomConstant.CHANNEL_CODE,
                        MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, false);
                paramsLog = new String[]{paymentDTO.getRawResponse()};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, false, false, true, paramsLog));
            }
            paymentService.updateChannelTransactionStatusPayment(paymentDTO);

            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, false));
            return pgResponse;
        } catch (Exception e) {
            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_RESEND_OTP, false));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse inquiryTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                         String inputStr) {
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, true));

            RootRequest body = objectMapper.readValue(inputStr, RootRequest.class);

            InquiryTransactionRequest request = new InquiryTransactionRequest();
            // set tham số tĩnh
            request.setMerchantId(paymentAccount.getMerchantId());

            request.setTransId(body.getRequestTransId());

            // Check order id (merchant id) is exist or not
            Payment paymentToCheckExist = paymentService
                    .findByMerchantTransactionId(body.getRequestTransId());
            String[] paramsLog;
            if (paymentToCheckExist == null) {
                paramsLog = new String[]{"Transaction id (trace id) not exist ("
                        + body.getTransId() + ")"};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, false, false, true, paramsLog));

                logger.info(
                        commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                                MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, false));
                return commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();
            }

            // set chuỗi mã hóa
            // PaymentAccount.EncryptKey là access code
            request.setSecureHash(PGSecurity.sha256(request.buildDataRaw(paymentAccount.getEncryptKey())));

            // WriteInfoLog("1. MSB ECOM REQ - inquiryTransaction API:",  body.getTransId() + PGUtil.CHARACTER + objectMapper.writeValueAsString(request));
            paramsLog = new String[]{objectMapper.writeValueAsString(request)};
            logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, true, true, false, paramsLog));

            String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                    channelFunction.getUrl());
            PGResponse pgResponse = process(request, body,
                    endpointUrl, MediaType.APPLICATION_JSON,
                    MSBEcomConstant.METHOD_POST, new TypeReference<InquiryTransactionResponse>() {
                    });

            String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setMerchantTransactionId(body.getTransId());
            paymentDTO.setRawRequest(
                    LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + objectMapper.writeValueAsString(request));
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + responseStr);

            if (NumberUtils.compare(Integer.parseInt(pgResponse.getErrorCode()),
                    MSBEcomConstant.API_RESPONSE_STATUS_CODE_SUCCESS) == 0) {
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());

                paramsLog = new String[]{paymentDTO.getRawResponse()};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, true, false, true, paramsLog));
            } else {
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());

                pgLogChannelFunctionService.writeLogChannelFunction(MSBEcomConstant.CHANNEL_CODE,
                        MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, false);
                paramsLog = new String[]{paymentDTO.getRawResponse()};
                logger.info(commonLogService.createContentLog(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                        MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, false, false, true, paramsLog));
            }
            paymentService.updateChannelTransactionStatusPayment(paymentDTO);

            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, false));
            return pgResponse;
        } catch (Exception e) {
            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MSBEcomConstant.CHANNEL_CODE, SERVICE_NAME,
                    MSBEcomConstant.FUNCTION_CODE_INQUIRY_TRANSACTION, false));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    protected <T extends BaseResponse> PGResponse process(BaseRequest baseReq, RootRequest rootReq,
                                                          String action, String mediaType, String method,
                                                          TypeReference<T> baseRes)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {

        RootResponse rootRes = MSBEcomClientRequest.sendRequest(action, rootReq, baseReq, mediaType, method);

        //parse response data
        T res = objectMapper.readValue(rootRes.getBodyRes(), baseRes);
        PGResponse pgRes = new PGResponse();
        pgRes.setData(rootRes.getBodyRes());
        pgRes.setStatus(true);
        pgRes.setErrorCode(res.getCode());
        pgRes.setMessage(MSBEcomConstant.getErrorMessage(Integer.parseInt(res.getCode())));
        // return objectMapper.writeValueAsString(pgRes);
        return pgRes;
    }

    /**
     * Lưu thông tin khởi tạo giao dịch vào database
     *
     * @param channelFunction
     * @param req
     */
    private PaymentDTO createPaymentData(ChannelFunction channelFunction, CreatePaymentRequest req) throws
            JsonProcessingException {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setChannelTransactionType(req.getPaymentType());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + objectMapper.writeValueAsString(req));
        // paymentDTO.setAccountNo(req.getCardNumber());
        paymentDTO.setCardNo(req.getCardNumber());
        paymentDTO.setMerchantCode(req.getMerchantID());

        paymentDTO.setMerchantTransactionId(req.getMerchTxnRef());
        paymentDTO.setAmount(req.getAmount());
        paymentDTO.setDescription(req.getOrderInfo());

        paymentService.createPayment(paymentDTO);
        return paymentDTO;
    }

}
