package gateway.core.channel.vccb.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.vccb.VCCBClientRequest;
import gateway.core.channel.vccb.VCCBSecurity;
import gateway.core.channel.vccb.dto.VCCBConstants;
import gateway.core.channel.vccb.dto.req.*;
import gateway.core.channel.vccb.dto.res.*;
import gateway.core.channel.vccb.dto.res.DataFileRequest;
import gateway.core.channel.vccb.service.VCCBService;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.PGUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.RequestUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class VCCBServiceImpl extends PaymentGate implements VCCBService {

    private static final Logger logger = LogManager.getLogger(VCCBServiceImpl.class);

    @Autowired
    private CommonLogService commonLogService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommonPGResponseService commonPGResponseService;

    /**
     * Kiem tra so du tai khoan
     * @param channelFunction
     * @param inputStr
     * @return
     * @throws Exception
     */
    public PGResponse CheckBalance(ChannelFunction channelFunction, String inputStr) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_BALANCE, true));
        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        RootRequest req = buildCommonParam(input);

        CheckBalanceRequest dataReq = new CheckBalanceRequest();
        dataReq.setAccountNo(input.getBankAccountNumber());

        req.setData(objectMapper.writeValueAsString(dataReq));

        // TODO: sig to data and add to checkBalanceRequest
        String rawData = req.getRequestID() + "|" + req.getClientCode() + "|" + req.getClientUserID() + "|"
                + req.getTime() + "|" + req.getData();

        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        req.setSignature(signature);
        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());

        PGResponse pgResponse = process(req, endpointUrl, CheckBalanceResponse.class,
                VCCBConstants.FUNCTION_CODE_CHECK_BALANCE);
        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_BALANCE, false));
        return pgResponse;
    }

    /**
     * Truy van tai khoan IBFT on-us
     * @param channelFunction
     * @param request
     * @return
     * @throws Exception
     */
    public PGResponse CheckCardVCCB(ChannelFunction channelFunction, String request) throws Exception {

        logger.info(commonLogService.createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                VCCBConstants.FUNCTION_CODE_CHECK_CARD_VCCB, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest baseReq = new RootRequest();

        baseReq = buildCommonParam(input);

        CheckAccountRequest checkAccountRequest = new CheckAccountRequest();
        checkAccountRequest.setCardNo(input.getCardNumber());
        checkAccountRequest.setOnus("1");
        checkAccountRequest.setBankCode(input.getBankCode());
        checkAccountRequest.setDescription(input.getDescription());
        baseReq.setData(objectMapper.writeValueAsString(checkAccountRequest));
        String data = objectMapper.writeValueAsString(checkAccountRequest);

        String rawData = baseReq.getRequestID() + "|" + baseReq.getClientCode() + "|" + baseReq.getClientUserID() + "|"
                + baseReq.getTime() + "|" + data;

        // TODO: sign data and and to request before call bank api
        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        baseReq.setSignature(signature);

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        PGResponse pgResponse = process(baseReq, endpointUrl, CheckAccountResponse.class,
                VCCBConstants.FUNCTION_CODE_CHECK_CARD_VCCB);
        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_CARD_VCCB, false));
        return pgResponse;
    }

    /**
     * Truy van tai khoan IBFT off-us
     * @param channelFunction
     * @param request
     * @return
     * @throws Exception
     */
    public PGResponse CheckBankAccVCCB(ChannelFunction channelFunction, String request) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_BANK_ACC_VCCB, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest baseReq = new RootRequest();

        baseReq = buildCommonParam(input);

        CheckAccountRequest checkAccountRequest = new CheckAccountRequest();
        checkAccountRequest.setAccountNo(input.getBankAccountNumber());
        checkAccountRequest.setBankCode(input.getBankCode());
        checkAccountRequest.setOnus("1");
        checkAccountRequest.setDescription(input.getDescription());
        baseReq.setData(objectMapper.writeValueAsString(checkAccountRequest));
        String data = objectMapper.writeValueAsString(checkAccountRequest);

        String rawData = baseReq.getRequestID() + "|" + baseReq.getClientCode() + "|" + baseReq.getClientUserID() + "|"
                + baseReq.getTime() + "|" + data;

        // TODO: sign data and and to request before call bank api
        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        baseReq.setSignature(signature);

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        PGResponse pgResponse = process(baseReq, endpointUrl, CheckAccountResponse.class,
                VCCBConstants.FUNCTION_CODE_CHECK_BANK_ACC_VCCB);

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_BANK_ACC_VCCB, false));
        return pgResponse;

    }

    public PGResponse CheckCardIBFT(ChannelFunction channelFunction, String request) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_CARD_IBFT, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest baseReq = new RootRequest();

        baseReq = buildCommonParam(input);

        CheckAccountRequest checkAccountRequest = new CheckAccountRequest();
        checkAccountRequest.setCardNo(input.getCardNumber());
        checkAccountRequest.setOnus("0");
        checkAccountRequest.setBankCode(input.getBankCode());
        checkAccountRequest.setDescription(input.getDescription());
        baseReq.setData(objectMapper.writeValueAsString(checkAccountRequest));
        String data = objectMapper.writeValueAsString(checkAccountRequest);

        String rawData = baseReq.getRequestID() + "|" + baseReq.getClientCode() + "|" + baseReq.getClientUserID() + "|"
                + baseReq.getTime() + "|" + data;

        // TODO: sign data and and to request before call bank api
        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        baseReq.setSignature(signature);

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        PGResponse pgResponse = process(baseReq, endpointUrl, CheckAccountResponse.class, VCCBConstants.FUNCTION_CODE_CHECK_CARD_IBFT);

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_CARD_IBFT, false));
        return pgResponse;
    }

    /**
     * Truy van tai khoan IBFT off-us
     *
     * @param request
     * @return
     * @throws java.io.IOException
     *
     */
    public PGResponse CheckBankAccIBFT(ChannelFunction channelFunction, String request) throws Exception {
        // TODO mappv: Khong co funtion test
        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_BANK_ACC_IBFT, true));


        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest baseReq = new RootRequest();

        baseReq = buildCommonParam(input);

        CheckAccountRequest checkAccountRequest = new CheckAccountRequest();
        checkAccountRequest.setAccountNo(input.getBankAccountNumber());
        checkAccountRequest.setBankCode(input.getBankCode());
        checkAccountRequest.setOnus("0");
        checkAccountRequest.setDescription(input.getDescription());
        baseReq.setData(objectMapper.writeValueAsString(checkAccountRequest));
        String data = objectMapper.writeValueAsString(checkAccountRequest);

        String rawData = baseReq.getRequestID() + "|" + baseReq.getClientCode() + "|" + baseReq.getClientUserID() + "|"
                + baseReq.getTime() + "|" + data;

        // TODO: sign data and and to request before call bank api
        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        baseReq.setSignature(signature);

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        PGResponse pgResponse = process(baseReq, endpointUrl, CheckAccountResponse.class,
                VCCBConstants.FUNCTION_CODE_CHECK_BANK_ACC_IBFT);

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_BANK_ACC_IBFT, true));
        return pgResponse;
    }

    /**
     * chuyển khoản IBFT
     *
     * @param request
     * @return
     * @throws Exception
     *
     */
    public PGResponse TransferCardVCCB(ChannelFunction channelFunction, String request) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_VCCB, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest baseReq = buildCommonParam(input);

        TransferRequest transferRequest = new TransferRequest();
        String amountString = String.format("%.0f", input.getAmount());
        transferRequest.setAmount(amountString);
        transferRequest.setBankCode(input.getBankCode());
        transferRequest.setCardNo(input.getCardNumber());
        transferRequest.setDescription(input.getDescription());
        transferRequest.setFeeModel("B2C");
        transferRequest.setOnus("1");
        String data = objectMapper.writeValueAsString(transferRequest);

        String rawData = baseReq.getRequestID() + "|" + baseReq.getClientCode() + "|" + baseReq.getClientUserID() + "|"
                + baseReq.getTime() + "|" + data;
        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        baseReq.setData(data);
        baseReq.setSignature(signature);

        // Check order id (merchant id) is exist or not
        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(input.getTransId());
        String[] paramsLog;
        if (paymentToCheckExist != null) {
            paramsLog = new String[]{"Transaction id (trace id) already exist ("
                    + input.getTransId() + ")"};
            logger.info(commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                    VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_VCCB, false, false, true, paramsLog));

            logger.info(
                    commonLogService.createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE,
                            VCCBConstants.SERVICE_NAME_IBFT,
                            VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_VCCB, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
        }

        // Create payment data
        PaymentDTO paymentDTO = createPaymentData(channelFunction, input, input.getCardNumber());

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        PGResponse pgResponse = process(baseReq, endpointUrl, TransferResponse.class,
                VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_VCCB);

        updatePaymentAfterCreated(paymentDTO, pgResponse, VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_VCCB);

        return pgResponse;
    }

    /**
     * Lưu thông tin khởi tạo giao dịch vào database
     *
     * @param channelFunction
     * @param req
     */
    private PaymentDTO createPaymentData(ChannelFunction channelFunction, DataRequest req, String accountOrCardNo) throws
            JsonProcessingException {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        // paymentDTO.setChannelTransactionType(req.getPaymentType());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + objectMapper.writeValueAsString(req));
        paymentDTO.setAccountNo(req.getBankAccountNumber());
        paymentDTO.setCardNo(req.getCardNumber());
        paymentDTO.setMerchantCode(req.getMerchantId());
        // setBankCode unsetted
        paymentDTO.setMerchantTransactionId(req.getTransId());
        paymentDTO.setAmount(String.format("%.0f", req.getAmount()));
        paymentDTO.setDescription(req.getDescription());

        paymentService.createPayment(paymentDTO);
        return paymentDTO;
    }

    /**
     * chuyển khoản IBFT on-us
     *
     * @param request
     * @return
     * @throws Exception
     *
     */
    public PGResponse TransferBankAccVCCB(ChannelFunction channelFunction, String request) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_VCCB, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest baseReq = new RootRequest();

        baseReq = buildCommonParam(input);
        String amountString = String.format("%.0f", input.getAmount());
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAmount(amountString);
        transferRequest.setBankCode(input.getBankCode());
        transferRequest.setAccountNo(input.getBankAccountNumber());
        transferRequest.setDescription(input.getDescription());
        transferRequest.setFeeModel("B2C");
        transferRequest.setOnus("1");
        String data = objectMapper.writeValueAsString(transferRequest);

        String rawData = baseReq.getRequestID() + "|" + baseReq.getClientCode() + "|" + baseReq.getClientUserID() + "|"
                + baseReq.getTime() + "|" + data;
        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        baseReq.setData(data);
        baseReq.setSignature(signature);

        // Check order id (merchant id) is exist or not
        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(input.getTransId());
        String[] paramsLog;
        if (paymentToCheckExist != null) {
            paramsLog = new String[]{"Transaction id (trace id) already exist ("
                    + input.getTransId() + ")"};
            logger.info(commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                    VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_VCCB, false, false, true, paramsLog));

            logger.info(
                    commonLogService.createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE,
                            VCCBConstants.SERVICE_NAME_IBFT,
                            VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_VCCB, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
        }

        // Create payment data
        PaymentDTO paymentDTO = createPaymentData(channelFunction, input, input.getBankAccountNumber());

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());

        PGResponse pgResponse = process(baseReq, endpointUrl, TransferResponse.class,
                VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_VCCB);

        updatePaymentAfterCreated(paymentDTO, pgResponse, VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_VCCB);

        return pgResponse;
    }

    private void updatePaymentAfterCreated(PaymentDTO paymentDTO, PGResponse pgResponse, String pgFunction) throws
            JsonProcessingException {
        String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);

        // TODO Test
//        PGResponse pgResponse = new PGResponse();
//        pgResponse.setStatus(true);
//        pgResponse.setErrorCode("222|1");
        // end Test

        // Update status payment
        // Get errorCode
        String suffixErrorCode = StringUtils.split(pgResponse.getErrorCode(), '|')[1];

        if (StringUtils.equals(pgResponse.getErrorCode(),
                VCCBConstants.API_RESPONSE_STATUS_CODE_SUCCESS)) {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            // TODO
            paymentDTO.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        } else if (StringUtils.equals(suffixErrorCode,
                VCCBConstants.API_RESPONSE_STATUS_CODE_PENDING)) {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        } else {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
        }
        paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        pgFunction, false));
    }

    public PGResponse TransferCardIBFT(ChannelFunction channelFunction, String request) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_IBFT, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest baseReq = new RootRequest();

        baseReq = buildCommonParam(input);

        TransferRequest transferRequest = new TransferRequest();
        String amountString = String.format("%.0f", input.getAmount());
        transferRequest.setAmount(amountString);
        transferRequest.setCardNo(input.getCardNumber());
        transferRequest.setBankCode(input.getBankCode());
        transferRequest.setDescription(input.getDescription());
        transferRequest.setFeeModel("B2C");
        transferRequest.setOnus("0");
        String data = objectMapper.writeValueAsString(transferRequest);

        String rawData = baseReq.getRequestID() + "|" + baseReq.getClientCode() + "|" + baseReq.getClientUserID() + "|"
                + baseReq.getTime() + "|" + data;
        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        baseReq.setData(data);
        baseReq.setSignature(signature);

        // Check order id (merchant id) is exist or not
        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(input.getTransId());
        String[] paramsLog;
        if (paymentToCheckExist != null) {
            paramsLog = new String[]{"Transaction id (trace id) already exist ("
                    + input.getTransId() + ")"};
            logger.info(commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                    VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_IBFT, false, false, true, paramsLog));

            logger.info(
                    commonLogService.createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE,
                            VCCBConstants.SERVICE_NAME_IBFT,
                            VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_IBFT, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
        }

        // Create payment data
        PaymentDTO paymentDTO = createPaymentData(channelFunction, input, input.getCardNumber());

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());

        PGResponse pgResponse = process(baseReq, endpointUrl, TransferResponse.class,
                VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_IBFT);

        updatePaymentAfterCreated(paymentDTO, pgResponse, VCCBConstants.FUNCTION_CODE_TRANSFER_CARD_IBFT);

        return pgResponse;
    }

    /**
     * chuyển khoản IBFT on-us
     *
     * @param request
     * @return
     * @throws Exception
     *
     */
    public PGResponse TransferBankAccIBFT(ChannelFunction channelFunction, String request) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_IBFT, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest baseReq = new RootRequest();

        baseReq = buildCommonParam(input);
        String amountString = String.format("%.0f", input.getAmount());
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setAmount(amountString);
        transferRequest.setBankCode(input.getBankCode());
        transferRequest.setAccountNo(input.getBankAccountNumber());
        transferRequest.setDescription(input.getDescription());
        transferRequest.setFeeModel("B2C");
        transferRequest.setOnus("0");
        String data = objectMapper.writeValueAsString(transferRequest);

        String rawData = baseReq.getRequestID() + "|" + baseReq.getClientCode() + "|" + baseReq.getClientUserID() + "|"
                + baseReq.getTime() + "|" + data;
        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);
        baseReq.setData(data);
        baseReq.setSignature(signature);

        // Check order id (merchant id) is exist or not
        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(input.getTransId());
        String[] paramsLog;
        if (paymentToCheckExist != null) {
            paramsLog = new String[]{"Transaction id (trace id) already exist ("
                    + input.getTransId() + ")"};
            logger.info(commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                    VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_IBFT, false, false, true, paramsLog));

            logger.info(
                    commonLogService.createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE,
                            VCCBConstants.SERVICE_NAME_IBFT,
                            VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_IBFT, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
        }

        // Create payment data
        PaymentDTO paymentDTO = createPaymentData(channelFunction, input, input.getBankAccountNumber());

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());

        PGResponse pgResponse = process(baseReq, endpointUrl, TransferResponse.class,
                VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_IBFT);

        updatePaymentAfterCreated(paymentDTO, pgResponse, VCCBConstants.FUNCTION_CODE_TRANSFER_BANK_ACC_IBFT);

        return pgResponse;
    }

    /**
     * Kiểm tra số dư TKĐBTT IBFT
     *
     * @param request
     * @return
     * @throws Exception
     *
     */
    public PGResponse CheckBalanceBankAccIBFT(ChannelFunction channelFunction, String request) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_BALANCE_BANK_ACC_IBFT, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest dataReq = new RootRequest();
        CheckBalanceRequest checkBalanceRequest = new CheckBalanceRequest();
        checkBalanceRequest.setAccountNo(input.getBankAccountNumber());

        dataReq = buildCommonParam(input);
        dataReq.setData(objectMapper.writeValueAsString(checkBalanceRequest));
        String rawData = dataReq.getRequestID() + "|" + dataReq.getClientCode() + "|" + dataReq.getClientUserID() + "|"
                + dataReq.getTime() + "|" + dataReq.getData();

        String signature = VCCBSecurity.sign(rawData);
        dataReq.setSignature(signature);

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());

        // VCCBConstants.BASE_URL_API
        PGResponse pgResponse = process(dataReq, endpointUrl, CheckBalanceBankAccIBFTResp.class,
                VCCBConstants.FUNCTION_CODE_CHECK_BALANCE_BANK_ACC_IBFT);
        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_CHECK_BALANCE_BANK_ACC_IBFT, false));
        return pgResponse;
    }

    /**
     * Tải tập tin đối soát
     *
     * @param <T>
     * @param request
     * @return
     * @throws Exception
     *
     */
    public <T extends ApiResponse> PGResponse UploadReconciliationNGLA(ChannelFunction channelFunction, String request) throws
            Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        VCCBConstants.FUNCTION_CODE_UPLOAD_RECONCILIATION_NGLA, true));

        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest dataReq = new RootRequest();
        dataReq = buildCommonParam(input);

        List<DataFileRequest> dataFiles = objectMapper.readValue(input.getData(),
                new TypeReference<List<DataFileRequest>>() {
                });

        UploadReconciliationReq uploadReconciliationReq = new UploadReconciliationReq();
        uploadReconciliationReq.setData("");
        uploadReconciliationReq.setDescription("");
        uploadReconciliationReq.setProcessingDate(Integer.parseInt(input.getProcessDate()));

        String data = objectMapper.writeValueAsString(uploadReconciliationReq);
        String rawData = dataReq.getRequestID() + "|" + dataReq.getClientCode() + "|" + dataReq.getClientUserID() + "|"
                + dataReq.getTime() + "|" + data;

        String signature = VCCBSecurity.sign(rawData);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("requestID", dataReq.getRequestID()));
        params.add(new BasicNameValuePair("clientCode", dataReq.getClientCode()));
        params.add(new BasicNameValuePair("clientUserID", dataReq.getClientUserID()));
        params.add(new BasicNameValuePair("time", dataReq.getTime()));
        params.add(new BasicNameValuePair("data", data));
        params.add(new BasicNameValuePair("signature", signature));

        final String fileName = "NGLA_" + input.getProcessDate() + ".txt";

        // final String dirPath = "/data/upload/live/doisoat/vccb";
        String dirPath = channelFunction.getConfig();

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = dirPath + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }

        String zipFileName = file.getName().concat(".zip");
        String zipName = zipFileName.replace(".txt", "");
        FileOutputStream fosW = null;
        FileOutputStream fos = null;

        ZipOutputStream zos = null;
        try {
            // fosW = new FileOutputStream(dirPath + zipName);
            fosW = new FileOutputStream(dirPath + File.separator + zipName);
            fos = new FileOutputStream(file);
            String dataToZip = ConvertDataFileString(dataFiles, input.getProcessDate());
            try {
                fos.write(dataToZip.getBytes());
            } catch (IOException e) {
                WriteInfoLog(" WRITE FILE - " + fileName + "    - ERROR: " + e.getMessage());
            }
            zos = new ZipOutputStream(fosW);
            zos.putNextEntry(new ZipEntry(file.getName()));
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            zos.write(bytes);
            zos.closeEntry();
            zos.finish();
        } catch (IOException | NoSuchAlgorithmException | ParseException e) {
            WriteInfoLog(" WRITE FILE - " + fileName + "    - ERROR: " + e.getMessage());
        } finally {
            zos.close();
            fosW.close();
            fos.close();
        }
        File fileToPost = new File(dirPath + File.separator + zipName);

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        String response = POST(endpointUrl, params, fileToPost);
        WriteInfoLog("RUN DOI SOAT VCCB - TIME: " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()) + "  | FILE - " + fileName +
                "  | DATA RESPONSE - " + response);

        RootResponse rootResponse = objectMapper.readValue(response, RootResponse.class);
        return getPgResponse(rootResponse, VCCBConstants.FUNCTION_CODE_UPLOAD_RECONCILIATION_NGLA);
    }

    private PGResponse getPgResponse(RootResponse rootResponse, String functionCode) throws Exception {
        PGResponse pgResponse = new PGResponse();
        pgResponse.setStatus(true);
        pgResponse.setData("responseID:" + rootResponse.getResponseID());
        pgResponse.setErrorCode(rootResponse.getStatus() + "|" + rootResponse.getErrorCode());
        pgResponse.setMessage(VCCBConstants.ERROR_MESSAGE.get(rootResponse.getErrorCode()));
        // if (!VCCBSecurity.verifySign(rootResponse.getSig(), rootResponse.rawData()))
        logger.info(commonLogService
                .createContentLogStartEndFunction(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        functionCode, false));
        return pgResponse;
    }

    public PGResponse UploadReconciliationNGLB(ChannelFunction channelFunction, String request) throws Exception {
        DataRequest input = objectMapper.readValue(request, DataRequest.class);
        RootRequest dataReq = new RootRequest();
        dataReq = buildCommonParam(input);

        List<DataFileRequest> dataFiles = objectMapper.readValue(input.getData(),
                new TypeReference<List<DataFileRequest>>() {
                });

        UploadReconciliationReq uploadReconciliationReq = new UploadReconciliationReq();
        uploadReconciliationReq.setData("");
        uploadReconciliationReq.setDescription("");
        uploadReconciliationReq.setProcessingDate(Integer.parseInt(input.getProcessDate()));

        // String data = objectMapper.writeValueAsString(dataFiles);
        String data = objectMapper.writeValueAsString(uploadReconciliationReq);
        String rawData = dataReq.getRequestID() + "|" + dataReq.getClientCode() + "|" + dataReq.getClientUserID() + "|"
                + dataReq.getTime() + "|" + data;

        //VCCBSecurity.initParam(getPaymentAccount());
        String signature = VCCBSecurity.sign(rawData);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("requestID", dataReq.getRequestID()));
        params.add(new BasicNameValuePair("clientCode", dataReq.getClientCode()));
        params.add(new BasicNameValuePair("clientUserID", dataReq.getClientUserID()));
        params.add(new BasicNameValuePair("time", dataReq.getTime()));
        params.add(new BasicNameValuePair("data", data));
        params.add(new BasicNameValuePair("signature", signature));

        final String fileName = "NGLB_" + input.getProcessDate() + ".txt";
        // final String dirPath = "/data/upload/live/doisoat/vccb";
        String dirPath = channelFunction.getConfig();
        //final String dirPath = "/data/upload/uat/doisoat/vccb";

        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = dirPath + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            file.createNewFile();
        }

        String zipFileName = file.getName().concat(".zip");
        String zipName = zipFileName.replace(".txt", "");
        FileOutputStream fosW = null;
        FileOutputStream fos = null;

        ZipOutputStream zos = null;
        try {
            // fosW = new FileOutputStream(dirPath + zipName);
            fosW = new FileOutputStream(dirPath + File.separator + zipName);
            fos = new FileOutputStream(file);
            String dataToZip = ConvertDataFileString(dataFiles, input.getProcessDate());
            try {
                fos.write(dataToZip.getBytes());
            } catch (Exception e) {
                WriteInfoLog(" WRITE FILE - " + fileName + "    - ERROR: " + e.getMessage());
            }
            zos = new ZipOutputStream(fosW);
            zos.putNextEntry(new ZipEntry(file.getName()));
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            zos.write(bytes);
            zos.closeEntry();
            zos.finish();
        } catch (IOException | NoSuchAlgorithmException | ParseException e) {
            WriteInfoLog(" WRITE FILE - " + fileName + "    - ERROR: " + e.getMessage());
        } finally {
            zos.close();
            fosW.close();
            fos.close();
        }
        File fileToPost = new File(dirPath + File.separator + zipName);

        String endpointUrl = RequestUtil.createUrlChannelFunction(channelFunction.getHost(), channelFunction.getPort(),
                channelFunction.getUrl());
        String response = POST(endpointUrl, params, fileToPost);
        WriteInfoLog("RUN DOI SOAT VCCB - TIME: " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()) + "  | FILE - " + fileName +
                "  | DATA RESPONSE - " + response);

        RootResponse rootResponse = objectMapper.readValue(response, RootResponse.class);
//		String responseID = rootResponse.getResponseID();
//		res.setBankTrasId(rootResponse.getResponseID());
        return getPgResponse(rootResponse, VCCBConstants.FUNCTION_CODE_UPLOAD_RECONCILIATION_NGLB);
    }

    public static String POST(String url, List<NameValuePair> params, File file) throws JsonProcessingException {
        String demo = objectMapper.writeValueAsString(params);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            FileBody uploadFilePart = new FileBody(file);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("file", new FileInputStream(file), ContentType.APPLICATION_OCTET_STREAM,
                    file.getName());

            for (NameValuePair param : params) {
                builder.addTextBody(param.getName(), param.getValue());
            }

            httpPost.setEntity(builder.build());
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream inputStream = entity.getContent()) {
                    String result = IOUtils.toString(inputStream, "UTF-8");
                    return result;
                }
            }

        } catch (FileNotFoundException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            WriteInfoLog(" WRITE FILE - " + file.getName() + "    - ERROR: " + e.getMessage());
        } catch (ClientProtocolException e) {
            WriteInfoLog(" WRITE FILE - " + file.getName() + "    - ERROR: " + e.getMessage());
            logger.info(ExceptionUtils.getStackTrace(e));
        } catch (IOException e) {
            WriteInfoLog(" WRITE FILE - " + file.getName() + "    - ERROR: " + e.getMessage());
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private String ConvertDataFileString(List<DataFileRequest> dataFiles, String processDate)
            throws NoSuchAlgorithmException, JsonProcessingException, ParseException {
        String data = "ClientCode,TransactionID,TransactionDate,CardNo,CardInd,BusinessType,Amount,CurrencyCode,Description,BankTransactionID,ReversalIndicator,TransactionStatus,TransactionType,CheckSum"
                + "\r\n";

        for (int i = 0; i < dataFiles.size(); i++) {
            data += dataFiles.get(i).getClientCode().replaceAll("\\n", "").replaceAll("\\r", "") + "," + dataFiles.get(i).getTransactionID().replaceAll("\\n", "").replaceAll("\\r", "") + ","
                    + dataFiles.get(i).getTransactionDate().replaceAll("\\n", "").replaceAll("\\r", "") + "," + dataFiles.get(i).getCardNo().replaceAll("\\n", "").replaceAll("\\r", "") + ","
                    + dataFiles.get(i).getCardInd().replaceAll("\\n", "").replaceAll("\\r", "") + "," + dataFiles.get(i).getBusinessType().replaceAll("\\n", "").replaceAll("\\r", "") + ","
                    + dataFiles.get(i).getAmount().replaceAll("\\n", "").replaceAll("\\r", "") + "," + dataFiles.get(i).getCurrencyCode().replaceAll("\\n", "").replaceAll("\\r", "") + ","
                    + dataFiles.get(i).getDescription().replaceAll("\\n", "").replaceAll("\\r", "") + "," + dataFiles.get(i).getBankTransactionID().replaceAll("\\n", "").replaceAll("\\r", "") + ","
                    + dataFiles.get(i).getReversalIndicator().replaceAll("\\n", "").replaceAll("\\r", "") + "," + dataFiles.get(i).getTransactionStatus().replaceAll("\\n", "").replaceAll("\\r", "") + ","
                    + dataFiles.get(i).getTransactionType().replaceAll("\\n", "").replaceAll("\\r", "") + ","
                    + DigestUtils.md5Hex(dataFiles.get(i).dataToString().replaceAll("\\n", "").replaceAll("\\r", "")) + "\r\n";
            WriteInfoLog("dataString: " + dataFiles.get(i).dataToString());
        }
        data += DigestUtils.md5Hex(processDate + dataFiles.size());
        return data;
    }

    private RootRequest buildCommonParam(DataRequest input) {
        RootRequest req = new RootRequest();
        req.setRequestID(input.getTransId());
        req.setClientCode(input.getMerchantId());
        req.setClientUserID(getClientCodeRandom());
        req.setTime(input.getTransTime());
        return req;
    }

    private String getClientCodeRandom() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMdHHmmss");
        String formatDateTime = now.format(formatter);
        return formatDateTime;
    }

    private <T extends ApiResponse> PGResponse process(RootRequest baseRequest, String api, Class<T> resClass,
                                                       String... pgFunctionCode)
            throws Exception {
        JSONObject json = new JSONObject(baseRequest);

        Map<String, Object> map = json.toMap();
        String[] paramsLog = new String[]{baseRequest.getRequestID(), objectMapper.writeValueAsString(map)};
//        logger.info(commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
//                StringUtils.join(pgFunctionCode), true, true, false, paramsLog));

        commonLogService.logInfoWithTransId(logger, baseRequest.getRequestID(),
                commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        StringUtils.join(pgFunctionCode), true, true, false, paramsLog));

        String responseApi = VCCBClientRequest.callApi(map, api);
        // System.out.printf(responseApi);
        paramsLog = new String[]{baseRequest.getRequestID(), responseApi};
//        logger.info(commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
//                StringUtils.join(pgFunctionCode), true, false, true, paramsLog));

        commonLogService.logInfoWithTransId(logger, baseRequest.getRequestID(),
                commonLogService.createContentLog(VCCBConstants.CHANNEL_CODE, VCCBConstants.SERVICE_NAME_IBFT,
                        StringUtils.join(pgFunctionCode), true, false, true, paramsLog));

        RootResponse baseResponse = objectMapper.readValue(responseApi, RootResponse.class);
        T res = null;
        if (baseResponse.getData() != null) {
            res = objectMapper.readValue(baseResponse.getData(), resClass);
            res.setBankTrasId(baseResponse.getResponseID());
        }
        if (!VCCBSecurity.verifySign(baseResponse.getSig(), baseResponse.rawData())) {
            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            pgResponse.setErrorCode("-1|2009");
            pgResponse.setMessage(VCCBConstants.ERROR_MESSAGE.get("2009"));
            pgResponse.setData(res);
            return pgResponse;
        } else {
            PGResponse pgResponse = new PGResponse();
            pgResponse.setStatus(true);
            pgResponse.setErrorCode(baseResponse.getStatus() + "|" + baseResponse.getErrorCode());
            pgResponse.setMessage(VCCBConstants.ERROR_MESSAGE.get(baseResponse.getErrorCode()));
            pgResponse.setData(res);
            return pgResponse;
        }
    }
}
