package gateway.core.channel.msb_qr.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.msb_qr.ApiClient;
import gateway.core.channel.msb_qr.dto.MsbConstants;
import gateway.core.channel.msb_qr.dto.req.BaseRequest;
import gateway.core.channel.msb_qr.dto.req.CheckOrderReq;
import gateway.core.channel.msb_qr.dto.req.CreateQrCodeReq;
import gateway.core.channel.msb_qr.dto.res.CheckOrderRes;
import gateway.core.channel.msb_qr.dto.res.CreateQrCodeRes;
import gateway.core.channel.msb_qr.service.MSBQRCodeService;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.LogConst;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.repository.ChannelFunctionRepository;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.service.PgLogChannelFunctionService;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class MSBQRCodeServiceImpl extends PaymentGate implements MSBQRCodeService
{

    private static final Logger logger = LogManager.getLogger(MSBQRCodeServiceImpl.class);
    private static final String SERVICE_NAME = "MSB QRCODE";

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
    public PGResponse QrCodePayment(Integer channelId, PaymentAccount paymentAccount, String inputStr) {
        try {
            logger.info(commonLogService.createContentLogStartEndFunction(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
                    MsbConstants.FUNCTION_CODE_QR_CODE_PAYMENT, true));
            WriteInfoLog("VIMO_MC/NGL - MSB QRCODE PAYMENT");
            DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);

            CreateQrCodeReq req = new CreateQrCodeReq();
            req.setAppId(paymentAccount.getProviderId()); // ChannelCommonConfig.MSB_QRCode_PROVIDER_ID
            req.setMerchantName(paymentAccount.getMerchantName()); // ChannelCommonConfig.MSB_QRCode_MERCHANT_NAME
            req.setMerchantCode(paymentAccount.getMerchantId()); // ChannelCommonConfig.MSB_QRCode_MERCHANT_ID

            req.setBillNumber(input.getBillNo());
            req.setTerminalId(input.getTerminalId());
            req.setPayType(input.getPayMethod()); // Kiểu Qr

            req.setAmount(df.format(input.getAmount()));
            req.setTipAndFee(String.format("%.0f", input.getFeeAmount()));
            req.setExpDate(input.getExpireDate());
            req.setDesc(input.getDescription());

            String paymethod = input.getPayMethod();
            String indexCheckLog = "";
            String merchantTransactionId = StringUtils.EMPTY;
//		// 01 : QR đơn hàng (QR Online – cổng thanh toán trực tuyến)
            if (MsbConstants.QR_TYPE_ORDER_ONLINE.equals(paymethod)) {
                req.setTxnId(input.getTransId());
                indexCheckLog = input.getTransId();
                merchantTransactionId = input.getTransId();
            }
//		// 02 : Qr Sản phẩm
            else if (MsbConstants.QR_TYPE_PRODUCT.equals(paymethod)) {
                req.setProductId(input.getProductId());
                req.setBillNumber(input.getBillNo());
                indexCheckLog = input.getProductId() + "|" + input.getBillNo();

                merchantTransactionId = input.getProductId();
            }
//		// 03 : QR Hóa đơn (QR Offline)
            else if (MsbConstants.QR_TYPE_ORDER_OFFLINE.equals(paymethod)) {
                req.setBillNumber(input.getBillNo());
                indexCheckLog = input.getBillNo();
                merchantTransactionId = input.getBillNo();
            }
// 04 : QR Billing
            else if (MsbConstants.QR_TYPE_BILLING.equals(paymethod)) {
                req.setConsumerID(input.getUserId());
                req.setPurpose(input.getPurpose());
                indexCheckLog = input.getUserId() + "|" + input.getPurpose();
                merchantTransactionId = input.getUserId() + "|" + input.getPurpose();
            }

            // default
            req.setMerchantType("0102"); // test 0102
            req.setCcy("704");
            req.setMasterMerCode("970426");
            req.setServiceCode("02");
            req.setCountryCode("VN");

            String checkSumReq = PGSecurity.md5(req.rawData(paymentAccount.getEncryptKey())); // ChannelCommonConfig.MSB_QRCode_ENCRYP_KEY
            req.setChecksum(checkSumReq);
            WriteInfoLog("2. MSB QR REQ - QrCodePayment API:", objectMapper.writeValueAsString(req));
            String typeAPI = "QrCodePayment";

            // Check order id (merchant id) is exist or not
            Payment paymentToCheckExist = paymentService
                    .findByMerchantTransactionId(merchantTransactionId);
            String[] paramsLog;
            if (paymentToCheckExist != null) {

                paramsLog = new String[] { "Merchant transaction id (trace id) already exist ("
                        + merchantTransactionId + ")" };
                logger.info(commonLogService.createContentLog(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
                        MsbConstants.FUNCTION_CODE_QR_CODE_PAYMENT, false, false, true, paramsLog));

                logger.info(commonLogService.createContentLogStartEndFunction(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
                        MsbConstants.FUNCTION_CODE_QR_CODE_PAYMENT, false));
                return (PGResponse) commonPGResponseService.returnBadRequets_TransactionExist().getBody();
            }

            // Create payment data
            PaymentDTO paymentDTO = createPaymentData(channelId, req);

            // TODO: call api MSB
            PGResponse pgResponse = process(paymentAccount, req, MsbConstants.CREATE_QRCODE_URL_SUFFIX, indexCheckLog, typeAPI);
            String responseStr = objectMapper.writeValueAsString(pgResponse.getData());
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);

            if (StringUtils.equals(pgResponse.getErrorCode(), MsbConstants.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());

            }
            else {
                paymentDTO.setPgTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
            }
            paymentService.updateTransactionStatusAfterCreatedPayment(paymentDTO);

            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
                    MsbConstants.FUNCTION_CODE_QR_CODE_PAYMENT, false));

            return pgResponse;

        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse CheckQrOrder(PaymentAccount paymentAccount, String inputStr) {
        try {
            WriteInfoLog("NGANLUONG - MSB CHECK ORDER");
            DataRequest input = objectMapper.readValue(inputStr, DataRequest.class);
            if (paymentService.findByMerchantTransactionId(input.getBillNo()) == null){
                return commonPGResponseService.returnBadRequest_TransactionNotExist().getBody();
            }

            CheckOrderReq req = new CheckOrderReq();
            req.setMerchantId(paymentAccount.getMerchantId()); // ChannelCommonConfig.MSB_QRCode_MERCHANT_ID
            req.setTerminalId(input.getTerminalId());
            req.setPayType(input.getPayMethod());
            req.setBillNumber(input.getBillNo());
            WriteInfoLog("2. MSB QR REQ - CheckQrOrder API: ", objectMapper.writeValueAsString(req));
            String typeAPI = "CheckQrOrder";
            String res = ApiClient.sendRequest(paymentAccount.getUrlApi(), objectMapper.writeValueAsString(req), null, input.getBillNo(), typeAPI);
            // ChannelCommonConfig.MSB_QRCode_URL_API_CALL_MSB
//        String res = "";

            // TODO Test (Uncomment line above):
//            CheckOrderRes checkOrderRes = new CheckOrderRes();
//            checkOrderRes.setBillNumber(input.getBillNo());
//            checkOrderRes.setErrorCode("00");
//            checkOrderRes.setSettle(true);
//            String res = objectMapper.writeValueAsString(checkOrderRes);
            // END TODO Test

            WriteInfoLog("3. MSB QR RES - CheckQrOrder API: ", res);
            // Parse Response
            CheckOrderRes qrCodeRes = objectMapper.readValue(res, CheckOrderRes.class);
            PGResponse pgRes = new PGResponse();
            pgRes.setStatus(true);
            pgRes.setData(res);
            pgRes.setErrorCode(qrCodeRes.getErrorCode());
            pgRes.setMessage(qrCodeRes.getMessage());

            PaymentDTO paymentDTO = new PaymentDTO();
            // TODO Tracer Id phía Ngân Lượng là BillNo hay là gì?
            paymentDTO.setMerchantTransactionId(input.getBillNo());

            paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + objectMapper.writeValueAsString(req));
            paymentDTO.setRawResponse(LogConst.LOG_CONTENT_PREFIX_STATUS_TRANSACTION + res);

            String[] paramsLog = null;
            if (StringUtils.equals(pgRes.getErrorCode(), MsbConstants.API_RESPONSE_STATUS_CODE_SUCCESS)) {
                if (qrCodeRes.isSettle()){
                    paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                }
                else {
                    paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.FAILED.code());
                }
                paramsLog = new String[] { paymentDTO.getRawResponse() };
                logger.info(commonLogService.createContentLog(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
                        MsbConstants.FUNCTION_CODE_CHECK_QR_ORDER, true, false, true, paramsLog));
            }
            else {
                paymentDTO.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
                pgLogChannelFunctionService.writeLogChannelFunction(MsbConstants.CHANNEL_CODE,
                        MsbConstants.FUNCTION_CODE_CHECK_QR_ORDER, false);
                paramsLog = new String[] { paymentDTO.getRawResponse() };
                logger.info(commonLogService.createContentLog(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
                        MsbConstants.FUNCTION_CODE_CHECK_QR_ORDER, false, false, true, paramsLog));
            }
            paymentService.updateChannelTransactionStatusPayment(paymentDTO);

            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
                    MsbConstants.FUNCTION_CODE_CHECK_QR_ORDER, false));

            //return objectMapper.writeValueAsString(pgRes);
            return pgRes;
        } catch (Exception e) {
            // Write log end function
            logger.info(commonLogService.createContentLogStartEndFunction(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
                    MsbConstants.FUNCTION_CODE_CHECK_QR_ORDER, false));
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public String CompleteQrPayment(PaymentAccount paymentAccount, String data) {
        try {
            WriteInfoLog("3. MSB QR REQ - msbCompleteVMQrPayment API: ", data);
            // convert JSON string to Map
            Map<String, String> map = objectMapper.readValue(data, Map.class);
            Map<String, Object> mapData = new HashMap<>();
            mapData.put("params", map.get("body"));
//            String result = HttpUtil.send(paymentAccount.getUrlConfirm(), mapData, null);
            String result = "";
            // ChannelCommonConfig.MSB_QRCode_URL_API_CALL_NGL

            WriteInfoLog("3. MSB QR RES - msbCompleteVMQrPayment API: ", result);
            return "";
        } catch (IOException e) {
            // Write log end function
//            logger.info(commonLogService.createContentLogStartEndFunction(MsbConstants.CHANNEL_CODE, SERVICE_NAME,
//                    MsbConstants.FUNCTION_CODE_COMPLETE_QR_PAYMENT, false));
        }
        return "";


    }

    private PGResponse process(PaymentAccount paymentAccount, BaseRequest req, String api, String indexCheckLog, String typeAPI)
            throws IOException, NoSuchAlgorithmException,
            KeyManagementException {

        String res = ApiClient
                .sendRequest(paymentAccount.getUrlApi(), objectMapper.writeValueAsString(req), null, indexCheckLog, typeAPI);
        //  ChannelCommonConfig.MSB_QRCode_URL_API_CALL_MSB
        WriteInfoLog("3. MSB QR RES - QrCodePayment API: ", res);


        // Parse Response
        CreateQrCodeRes qrCodeRes = objectMapper.readValue(res, CreateQrCodeRes.class);


        PGResponse pgRes = new PGResponse();
        pgRes.setStatus(true);

        // TODO: validate checksum
//		String checkSumRes = PGSecurity.md5(qrCodeRes.rawData(getPaymentAccount().getEncrypKey()));
//		if (qrCodeRes.getCode().equals("00") && !checkSumRes.equalsIgnoreCase(qrCodeRes.getChecksum())) {
//			pgRes.setErrorCode("06");
//			pgRes.setMessage("MSB Qr Payment Checksum Invalid");
//		} else {
        pgRes.setErrorCode(qrCodeRes.getCode());
        pgRes.setMessage(qrCodeRes.getMessage());
//		}
//        qrCodeRes.setCode(null);
//        qrCodeRes.setMessage(null);
//        qrCodeRes.setChecksum(null);

        pgRes.setData(objectMapper.writeValueAsString(qrCodeRes));
        //return objectMapper.writeValueAsString(pgRes);
        return pgRes;
    }

    /**
     * Lưu thông tin khởi tạo giao dịch vào database
     * @param channelId
     * @param req
     */
    private PaymentDTO createPaymentData(Integer channelId, CreateQrCodeReq req) throws
            JsonProcessingException {

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelId);
        paymentDTO.setAmount(String.valueOf(req.getAmount()));
        paymentDTO.setChannelTransactionType(req.getPayType());
        paymentDTO.setDescription(req.getDesc());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + objectMapper.writeValueAsString(req));

        String paymethod = req.getPayType();
//		// 01 : QR đơn hàng (QR Online – cổng thanh toán trực tuyến)
        if (MsbConstants.QR_TYPE_ORDER_ONLINE.equals(paymethod)) {
            paymentDTO.setMerchantTransactionId(req.getTxnId());
        }
//		// 02 : Qr Sản phẩm
        else if (MsbConstants.QR_TYPE_PRODUCT.equals(paymethod)) {
            paymentDTO.setMerchantTransactionId(req.getProductId());
        }
//		// 03 : QR Hóa đơn (QR Offline)
        else if (MsbConstants.QR_TYPE_ORDER_OFFLINE.equals(paymethod)) {
            paymentDTO.setMerchantTransactionId(req.getBillNumber());
        }
// 04 : QR Billing
        else if (MsbConstants.QR_TYPE_BILLING.equals(paymethod)) {
            paymentDTO.setMerchantTransactionId(req.getConsumerID() + "|" + req.getPurpose());
        }

        paymentService.createPayment(paymentDTO);
        return paymentDTO;
    }
}
