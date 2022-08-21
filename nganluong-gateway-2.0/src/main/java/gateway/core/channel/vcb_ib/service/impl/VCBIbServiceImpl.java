package gateway.core.channel.vcb_ib.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.vcb_ib.VCBIbSecurity;
import gateway.core.channel.vcb_ib.dto.VCBIbConstants;
import gateway.core.channel.vcb_ib.dto.req.*;
import gateway.core.channel.vcb_ib.dto.res.*;
import gateway.core.channel.vcb_ib.service.VCBIbService;
import gateway.core.channel.vcb_ib.ws_client.Payment_Service_Card_V3Locator;
import gateway.core.channel.vcb_ib.ws_client.Payment_Service_Card_V3SoapStub;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.HttpUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;

import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class VCBIbServiceImpl extends PaymentGate implements VCBIbService {

    private static final Logger logger = LogManager.getLogger(VCBIbServiceImpl.class);

    @Autowired
    private CommonLogService commonLogService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private CommonPGResponseService commonPGResponseService;

    /**
     * Hàm gọi tới VCB để xác minh trạng thái thanh toán của giao dịch
     *
     * @param inputStr
     * @return
     * @throws Exception
     */
    @Override
    public PGResponse query(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws
            Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                        VCBIbConstants.FUNCTION_CODE_QUERY, true));

        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId(input.getTransId());

        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        String[] paramsLog;

        if (paymentToCheckExist == null) {
            paramsLog = new String[]{"Transaction id (trace id) not exist ("
                    + paymentDTO.getMerchantTransactionId() + ")"};
            logger.info(commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                    VCBIbConstants.FUNCTION_CODE_QUERY, false, false, true, paramsLog));

            logger.info(commonLogService
                    .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                            VCBIbConstants.FUNCTION_CODE_QUERY, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
        }
        paymentDTO
                .setRawRequest(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + objectMapper.writeValueAsString(input));

        QueryRequest queryRequest = new QueryRequest();
        buildCommonRequest(input, queryRequest, paymentAccount);
        queryRequest.setQueryTranId(input.getQueryTransId());
        Payment_Service_Card_V3SoapStub proxy = getProxy();
        String vcbRes = proxy.query(buildRootRequest(queryRequest, VCBIbConstants.FUNCTION_CODE_QUERY));
        PGResponse pgResponse = buildResponse(queryRequest, vcbRes, QueryResponse.class,
                VCBIbConstants.FUNCTION_CODE_QUERY);

        updatePaymentStatus(paymentDTO, pgResponse);

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                        VCBIbConstants.FUNCTION_CODE_QUERY, false));

        return pgResponse;
    }

    /**
     * Hàm VCB sẽ xác minh Partner và khởi tạo giao dịch
     *
     * @param inputStr
     * @return
     * @throws Exception
     */
    @Override
    public PGResponse verifyPayment(ChannelFunction channelFunction, PaymentAccount paymentAccount,
                                    String inputStr) throws Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                        VCBIbConstants.FUNCTION_CODE_VERIFY_PAYMENT, true));

        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        // Check order id (merchant id) is exist or not
        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(input.getTransId());
        String[] paramsLog;
        if (paymentToCheckExist != null) {
            paramsLog = new String[]{"Transaction id (trace id) already exist ("
                    + input.getTransId() + ")"};
            logger.info(commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                    VCBIbConstants.FUNCTION_CODE_VERIFY_PAYMENT, false, false, true, paramsLog));

            logger.info(
                    commonLogService.createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE,
                            VCBIbConstants.SERVICE_NAME,
                            VCBIbConstants.FUNCTION_CODE_VERIFY_PAYMENT, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
        }

        // Create payment data
        PaymentDTO paymentDTO = createPaymentData(channelFunction, input);

        VerifyPaymentRequest verifyPaymentRequest = new VerifyPaymentRequest();
        buildCommonRequest(input, verifyPaymentRequest, paymentAccount);
        verifyPaymentRequest.setLanguage(input.getLanguage());
        verifyPaymentRequest.setClientIp(input.getClientIp());
        verifyPaymentRequest.setAddInfo(input.getAddInfo());
        Payment_Service_Card_V3SoapStub proxy = getProxy();
        String vcbResponse = proxy
                .verifyPayment(buildRootRequest(verifyPaymentRequest, VCBIbConstants.FUNCTION_CODE_VERIFY_PAYMENT));

        PGResponse pgResponse = buildResponse(verifyPaymentRequest, vcbResponse, VerifyPaymentResponse.class,
                VCBIbConstants.FUNCTION_CODE_VERIFY_PAYMENT);

        updatePaymentAfterCreated(paymentDTO, pgResponse);
        logger.info(commonLogService
                .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                        VCBIbConstants.FUNCTION_CODE_VERIFY_PAYMENT, false));
        return pgResponse;
    }

    /**
     * VCB sẽ xác minh giao dịch refund trên nguyên tắc đã tồn tại giao dịch đó.
     * Số tiền refund sẽ phụ thuộc vào Merchant
     *
     * @param channelFunction
     * @param paymentAccount
     * @param inputStr
     * @return
     * @throws Exception
     */
    @Override
    public PGResponse refund(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws
            Exception {

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                        VCBIbConstants.FUNCTION_CODE_REFUND, true));

        DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setMerchantTransactionId(input.getTransId());

        Payment paymentToCheckExist = paymentService
                .findByMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        String[] paramsLog;

        if (paymentToCheckExist == null) {
            paramsLog = new String[]{"Transaction id (trace id) not exist ("
                    + paymentDTO.getMerchantTransactionId() + ")"};
            logger.info(commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                    VCBIbConstants.FUNCTION_CODE_QUERY, false, false, true, paramsLog));

            logger.info(commonLogService
                    .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                            VCBIbConstants.FUNCTION_CODE_QUERY, false));
            return (PGResponse) commonPGResponseService.returnBadRequets_TransactionEmpty().getBody();
        }
        paymentDTO
                .setRawRequest(LogConst.LOG_CONTENT_PREFIX_REVERT_TRANSACTION + objectMapper.writeValueAsString(input));

        RefundRequest refundRequest = new RefundRequest();
        buildCommonRequest(input, refundRequest, paymentAccount);
        refundRequest.setRefundSources("IB");
        refundRequest.setRefundTranID(input.getRefTransId());
        Payment_Service_Card_V3SoapStub proxy = getProxy();
        String vcbResponse = proxy.refund(buildRootRequest(refundRequest, VCBIbConstants.FUNCTION_CODE_REFUND));
        PGResponse pgResponse = buildResponse(refundRequest, vcbResponse, RefundResponse.class,
                VCBIbConstants.FUNCTION_CODE_REFUND);

        refundPaymentStatus(paymentDTO, pgResponse);

        logger.info(commonLogService
                .createContentLogStartEndFunction(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                        VCBIbConstants.FUNCTION_CODE_REFUND, false));

        return pgResponse;
    }

    /**
     * Khi khách hàng thanh toán thành công VCB callback
     * @param paymentAccount
     * @param request
     * @throws IOException
     */
    @Override
    public void vcbCallback(PaymentAccount paymentAccount, String request) throws IOException {
        try {
            byte[] byteArray = Base64.decodeBase64(request.getBytes());
            String decodedString = new String(byteArray);
            VcbCallbackRequest vcbCallbackRequest = objectMapper.readValue(decodedString, VcbCallbackRequest.class);
//            WriteInfoLog("  2. DATA VCB CALLBACK === " + vcbCallbackRequest.getPartnerTranId() + objectMapper
//                    .writeValueAsString(vcbCallbackRequest));
            String[] paramsLog = new String[]{vcbCallbackRequest.getPartnerTranId(), "2. DATA VCB CALLBACK",
                    objectMapper
                            .writeValueAsString(vcbCallbackRequest)};
            commonLogService.logInfoWithTransId(logger, vcbCallbackRequest.getPartnerTranId(),
                    commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                            VCBIbConstants.FUNCTION_CODE_CALLBACK, true, true, false, paramsLog));

            if (vcbCallbackRequest != null) {
                if ((vcbCallbackRequest.getData() != null && !Strings.isNullOrEmpty(vcbCallbackRequest.getData()))
                        && (vcbCallbackRequest.getPartnerTranId() != null && !Strings
                        .isNullOrEmpty(vcbCallbackRequest.getPartnerTranId()))
                        && (vcbCallbackRequest.getSignature() != null && !Strings
                        .isNullOrEmpty(vcbCallbackRequest.getSignature()))) {
                    // verify signature
                    String dataForVerify = vcbCallbackRequest.getPartnerTranId() + vcbCallbackRequest.getData();
//                    if (VCBIbSecurity.verifyVCB(dataForVerify, vcbCallbackRequest.getSignature())) {
                    // giải mã data
                    VCBCallbackResponse vcbCallbackResponse = objectMapper
                            .readValue(VCBIbSecurity.decryptAES2(vcbCallbackRequest.getData()),
                                    VCBCallbackResponse.class);
//                    WriteInfoLog("  3. DATA VCB CALLBACK === " + vcbCallbackRequest.getPartnerTranId() + objectMapper
//                            .writeValueAsString(vcbCallbackResponse));
                    paramsLog = new String[]{vcbCallbackRequest.getPartnerTranId(), "3. DATA VCB CALLBACK",
                            objectMapper
                                    .writeValueAsString(vcbCallbackResponse)};
                    commonLogService.logInfoWithTransId(logger, vcbCallbackRequest.getPartnerTranId(),
                            commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                                    VCBIbConstants.FUNCTION_CODE_CALLBACK, true, true, false, paramsLog));
                    // gọi sang api Ngân Lượng

                    Map<String, Object> params = new HashMap<>();
                    params.put("errCode", vcbCallbackResponse.getErrCode());
                    params.put("errDesc", vcbCallbackResponse.getErrDesc());
                    params.put("VcbTranId", vcbCallbackResponse.getVCBTranID());
                    params.put("PartnerId", vcbCallbackResponse.getPartnerId());
                    params.put("trans_id", vcbCallbackRequest.getPartnerTranId());
//                        String userPass = "nl35:NLDev@2019";
                    String userPass = null;
                    //TEST
//                        String response = HttpUtil.send("https://sandbox.nganluong.vn:8088/nl35/vietcombank3_return.php", params, userPass);
                    //LIVE
                    String response = HttpUtil.send(paymentAccount.getUrlConfirm(), params, userPass);
//                    WriteInfoLog(" 4. RESPONSE NGAN LUONG === " + vcbCallbackRequest.getPartnerTranId() + response);
                    paramsLog = new String[]{vcbCallbackRequest.getPartnerTranId(), response};
                    commonLogService.logInfoWithTransId(logger, vcbCallbackRequest.getPartnerTranId(),
                            commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                                    VCBIbConstants.FUNCTION_CODE_CALLBACK, true, false, true, paramsLog));
                    //System.out.println(response);
//                    }
                }
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
    }

    private String buildRootRequest(BaseRequest data, String pgFunctionCode) throws JsonProcessingException, Exception {
        RootRequest rootReq = new RootRequest();
        rootReq.setPartnerId(VCBIbConstants.VCB_IB_PARTNER_ID);
        rootReq.setTransactionID(data.getTransactionId());
        rootReq.setData(VCBIbSecurity.encryptAES2(objectMapper.writeValueAsString(data)));

        String rawData = rootReq.getPartnerId() + rootReq.getTransactionID() + rootReq.getData();
        rootReq.setSignature(VCBIbSecurity.sign(rawData));
        // TODO
        //WriteInfoLog("2.1 GATEWAY REQUEST ",  " | " + data.getTransactionId() + " | " + objectMapper.writeValueAsString(data));
        //WriteInfoLog("2.2 REQUEST TO VCB IB", " | " + data.getTransactionId() + " | " + objectMapper.writeValueAsString(rootReq));
        String[] paramsLog = new String[]{data.getTransactionId(), objectMapper.writeValueAsString(data),
                objectMapper.writeValueAsString(rootReq)};
        commonLogService.logInfoWithTransId(logger, data.getTransactionId(),
                commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                        pgFunctionCode, true, true, false, paramsLog));

        return objectMapper.writeValueAsString(rootReq);
    }

    private void buildCommonRequest(DataRequest dataRequest, BaseRequest baseRequest, PaymentAccount paymentAccount) {
        // TODO
        baseRequest.setTransactionId(dataRequest.getTransactionId());
        baseRequest.setMerchantId(paymentAccount.getMerchantId()); // VCBIbConstants.VCB_IB_MERCHANT_ID
        baseRequest.setAmount(df.format(dataRequest.getAmount()));
        baseRequest.setCurrencyCode("VND");
        baseRequest.setPartnerId(paymentAccount.getUsername()); // VCBIbConstants.VCB_IB_PARTNER_ID
        baseRequest.setPartnerPassword(paymentAccount.getPassword()); // VCBIbConstants.VCB_IB_PASSWORD
    }

    private Payment_Service_Card_V3SoapStub getProxy() throws MalformedURLException, ServiceException {
        Payment_Service_Card_V3Locator locator = new Payment_Service_Card_V3Locator();

        //URL url = new URL(getPaymentChannel().getUrl());
        URL url = new URL(VCBIbConstants.URL_API_CALL_VCB);
        Payment_Service_Card_V3SoapStub stub = (Payment_Service_Card_V3SoapStub) locator
                .getPayment_Service_Card_V3Soap(url);
        return stub;
    }

    private <T extends BaseResponse> PGResponse buildResponse(BaseRequest req, String res, Class<T> claszzRes,
                                                              String pgFunctionCode) throws Exception {
        // WriteInfoLog("3. VCB IB RESPONSE === ", " | " + req.getTransactionId() + " | " + res);
        String[] paramsLog = new String[]{req.getTransactionId(), res, ""};

        RootResponse rootRes = objectMapper.readValue(res, RootResponse.class);
        PGResponse pgRes = new PGResponse();
        if (rootRes != null) {
            String dataDecryptAES2 = VCBIbSecurity.decryptAES2(rootRes.getData());
            // WriteInfoLog("3.2 VCB IB RESPONSE DECRYPT === ", " | " + req.getTransactionId() + " | " + dataDecryptAES2);
            paramsLog[2] = dataDecryptAES2;

            T vcbRes = objectMapper.readValue(dataDecryptAES2, claszzRes);
            if (vcbRes.getVCBTranID() != null) {
                vcbRes.setVCBTranID(req.getTransactionId());
            }
            if (rootRes.getSignature() != null && !Strings.isNullOrEmpty(rootRes.getSignature())) {
                String rawData = rootRes.getPartnerId() + rootRes.getTransactionID() + rootRes.getData();
//                if (VCBIbSecurity.verifyVCB(rawData, rootRes.getSignature())) {
                pgRes.setStatus(true);
                pgRes.setErrorCode(vcbRes.getErrCode());
                pgRes.setMessage(VCBIbConstants.getResMsg(vcbRes.getErrCode()));
                pgRes.setData(objectMapper.writeValueAsString(vcbRes));

//                } else {
//                    PGResponse pgRes = new PGResponse();
//                    pgRes.setStatus(true);
//                    pgRes.setErrorCode("69");
//                    pgRes.setMessage(VCBIbConstants.getResMsg("69"));
//                    pgRes.setData(objectMapper.writeValueAsString(vcbRes));
//                    return objectMapper.writeValueAsString(pgRes);
//                }
            }
        } else {
            pgRes = commonPGResponseService.returnChannelBadRequestPrefix();
        }

        commonLogService.logInfoWithTransId(logger, req.getTransactionId(),
                commonLogService.createContentLog(VCBIbConstants.CHANNEL_CODE, VCBIbConstants.SERVICE_NAME,
                        pgFunctionCode, true, false, true, paramsLog));

        return pgRes;
    }

    /**
     * Lưu thông tin khởi tạo giao dịch vào database
     *
     * @param channelFunction
     * @param req
     */
    private PaymentDTO createPaymentData(ChannelFunction channelFunction, DataRequest req) throws
            JsonProcessingException {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + objectMapper.writeValueAsString(req));
        paymentDTO.setMerchantTransactionId(req.getTransId());
        paymentDTO.setAmount(String.format("%.0f", req.getAmount()));
        paymentDTO.setDescription(req.getBenAddInfo());
        paymentDTO.setClientRequestId(req.getClientIp());

        paymentService.createPayment(paymentDTO);
        return paymentDTO;
    }

    private void updatePaymentAfterCreated(PaymentDTO paymentDTO, PGResponse pgResponse) throws
            JsonProcessingException {
        String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);

        // Update status payment
        if (StringUtils.equals(pgResponse.getErrorCode(),
                VCBIbConstants.TRANS_SUCCESS)) {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        } else {
            paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
        }

        paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);
    }

    private void updatePaymentStatus(PaymentDTO paymentDTO, PGResponse pgResponse) throws
            JsonProcessingException {
        String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
        paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + responseStr);

        // Update status payment
        if (StringUtils.equals(pgResponse.getErrorCode(),
                VCBIbConstants.TRANS_SUCCESS)) {
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
        } else {
            paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
        }
        paymentService.updateChannelTransactionStatusPayment(paymentDTO);
    }

    private void refundPaymentStatus(PaymentDTO paymentDTO, PGResponse pgResponse) throws
            JsonProcessingException {

        if (StringUtils.equals(pgResponse.getErrorCode(),
                VCBIbConstants.TRANS_SUCCESS)) {
            String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_REVERT_TRANSACTION + responseStr);
            paymentDTO.setChannelRevertStatus(PaymentConst.EnumPaymentRevertStatus.REVERTED.code());
            paymentService.updateChannelTransactionStatusPayment(paymentDTO);
        }
    }
}
