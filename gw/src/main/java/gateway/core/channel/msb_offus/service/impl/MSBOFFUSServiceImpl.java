package gateway.core.channel.msb_offus.service.impl;

import gateway.core.channel.PaymentGate;
import gateway.core.channel.msb_offus.MSBOFFUSClientRequest;
import gateway.core.channel.msb_offus.dto.MSBOFFUSConstant;
import gateway.core.channel.msb_offus.request.CashinRequest;
import gateway.core.channel.msb_offus.request.CreateOrderRequest;
import gateway.core.channel.msb_offus.request.CreateRefundRequest;
import gateway.core.channel.msb_offus.request.GetOrderRequest;
import gateway.core.channel.msb_offus.request.GetTransactionRefundRequest;
import gateway.core.channel.msb_offus.request.NotificationOrderRequest;
import gateway.core.channel.msb_offus.request.UpdateTransactionRequest;
import gateway.core.channel.msb_offus.response.CashinResponse;
import gateway.core.channel.msb_offus.response.CreateOrderResponse;
import gateway.core.channel.msb_offus.response.RawTransactionOutput;
import gateway.core.channel.msb_offus.response.ResponseCreateRefund;
import gateway.core.channel.msb_offus.response.ResponseData;
import gateway.core.channel.msb_offus.response.ResponseRefundData;
import gateway.core.channel.msb_offus.service.MSBOFFUSService;
import gateway.core.dto.PGResponse;
import gateway.core.util.HttpUtil;
import gateway.core.util.PGSecurity;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

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

@Service
public class MSBOFFUSServiceImpl extends PaymentGate implements MSBOFFUSService {

    private static final Logger logger = LogManager.getLogger(MSBOFFUSServiceImpl.class);
    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private CommonPGResponseService commonPGResponseService;
    @Autowired
    private PaymentService paymentService;

    @Override
    public PGResponse createOrder(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START CREATE ORDER MSB OFFUS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER, true, true, false, paramsLog));
            // build data send partner
            CreateOrderRequest request = objectMapper.readValue(inputStr, CreateOrderRequest.class);
            request.setmId(MSBOFFUSConstant.MERCHANT_ID);
            request.settId(MSBOFFUSConstant.TERMINAL_ID);
            request.setCurrency("VND");
            DateFormat dateFormat = new SimpleDateFormat("yyMMddhhssmm");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            request.setTransDate(dateFormat.format(cal.getTime()));
            String secureCode = PGSecurity.sha256(MSBOFFUSConstant.ACCESS_CODE + request.getTransDate() + request.getTransId() + MSBOFFUSConstant.MERCHANT_ID + request.getAmount() + request.getBillNumber() + request.getCurrency());
            request.setSecureHash(secureCode);

            // create payment
            Payment payment = createPaymentData(channelFunction, request);
            paramsLog = new String[]{"RESPONSE CREATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(payment) + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER, true, true, false, paramsLog));
            // send partner
            String requestPartners = objectMapper.writeValueAsString(request);
            paramsLog = new String[]{"REQUEST TO MSB OFFUS: Message - " + " (" + requestPartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER, true, true, false, paramsLog));
            String responsePartners = MSBOFFUSClientRequest.sendRequest(MSBOFFUSConstant.MSB_OFFUS_CREATE_ORDER_URL, requestPartners, MSBOFFUSConstant.METHOD);
            paramsLog = new String[]{"RESPONSE MSB OFFUS: Message - " + " (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER, true, true, false, paramsLog));
            // build response
            ResponseData res = objectMapper.readValue(responsePartners, ResponseData.class);
            // update payment
            Payment paymentUpdate = updatePaymentData(payment, res);
            paramsLog = new String[]{"RESPONSE UPDATE PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(paymentUpdate) + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER, true, true, false, paramsLog));

            pgResponse.setStatus(true);
            pgResponse.setErrorCode(res.getCode());
            pgResponse.setMessage(MSBOFFUSConstant.getErrorMessage(Integer.parseInt(res.getCode())));
            pgResponse.setData(objectMapper.writeValueAsString(res));

            // Write log end function
            paramsLog = new String[]{"END CREATE ORDER MSB OFFUS (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER, true, true, false, paramsLog));
            return pgResponse;

        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse createOrderEcom(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START CREATE ORDER ECOM MSB OFFUS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER_ECOM, true, true, false, paramsLog));

            //send to partner
            String responsePartners = MSBOFFUSClientRequest.sendRequest(MSBOFFUSConstant.MSB_OFFUS_CREATE_ORDER_ECOM_URL, inputStr, MSBOFFUSConstant.METHOD);

            paramsLog = new String[]{"RESPONSE CREATE ORDER ECOM MSB OFFUS: Message - " + " (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER_ECOM, true, true, false, paramsLog));

            ResponseData res = objectMapper.readValue(responsePartners, ResponseData.class);
            pgResponse.setStatus(true);
            pgResponse.setErrorCode(res.getCode());
            pgResponse.setMessage(MSBOFFUSConstant.getErrorMessage(Integer.parseInt(res.getCode())));
            pgResponse.setData(objectMapper.writeValueAsString(res));

            // Write log end function
            paramsLog = new String[]{"END CREATE ORDER ECOM MSB OFFUS (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_ORDER_ECOM, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse getTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START GET TRANSACTION MSB OFFUS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION, true, true, false, paramsLog));
            // get payment
            GetOrderRequest request = objectMapper.readValue(inputStr, GetOrderRequest.class);
            Payment payment = paymentService.findByChannelTransactionId(request.getTransId());
            paramsLog = new String[]{"RESPONSE GET PAYMENT GATEWAY " + " (" + objectMapper.writeValueAsString(payment) + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION, true, true, false, paramsLog));

            //send to partner
            request.setMerchantId(MSBOFFUSConstant.MERCHANT_ID);
            request.setTransId(payment.getChannelTransactionId());
            CreateOrderRequest requestHash = objectMapper.readValue(payment.getRawRequest().replaceAll("\\*\\* Add transaction:", ""), CreateOrderRequest.class);
            String secureCode = PGSecurity.sha256(MSBOFFUSConstant.ACCESS_CODE + requestHash.getTransDate() + request.getTransId() + MSBOFFUSConstant.MERCHANT_ID + requestHash.getAmount() + requestHash.getBillNumber() + requestHash.getCurrency());
            request.setSecureHash(secureCode);

            String requestPartners = objectMapper.writeValueAsString(request);

            paramsLog = new String[]{"REQUEST GET TRANSACTION MSB OFFUS: Message - " + " (" + requestPartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION, true, true, false, paramsLog));

            String responsePartners = MSBOFFUSClientRequest.sendRequest(MSBOFFUSConstant.MSB_OFFUS_GET_TRANSACTION_URL, requestPartners, MSBOFFUSConstant.METHOD);

            paramsLog = new String[]{"RESPONSE GET TRANSACTION MSB OFFUS: Message - " + " (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION, true, true, false, paramsLog));

            ResponseData res = objectMapper.readValue(responsePartners, ResponseData.class);

            pgResponse.setStatus(true);
            pgResponse.setErrorCode(res.getCode());
            pgResponse.setMessage(MSBOFFUSConstant.getErrorMessage(Integer.parseInt(res.getCode())));
            pgResponse.setData(objectMapper.writeValueAsString(res));
            // Write log end function
            paramsLog = new String[]{"END GET TRANSACTION MSB OFFUS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }
    
    @Override
    public PGResponse getTransactionByCashin(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START GET TRANSACTION NL (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION_NL, true, true, false, paramsLog));
            // get payment
            CashinRequest request = objectMapper.readValue(inputStr, CashinRequest.class);
            Payment payment = paymentService.findByMerchantTransactionId(request.getId());

            ResponseData responseData = objectMapper.readValue(payment.getRawResponse().replaceAll("\\*\\* Add transaction:", ""), ResponseData.class);
            RawTransactionOutput requestHash = objectMapper.readValue(responseData.getData(), RawTransactionOutput.class);
            
            CashinResponse res = new CashinResponse(requestHash.getId(), payment.getMerchantTransactionId(), payment.getChannelTransactionId());
            
            pgResponse.setStatus(true);
            pgResponse.setErrorCode("00");
            pgResponse.setMessage("Thanh Cong");
            pgResponse.setData(objectMapper.writeValueAsString(res));
            // Write log end function
            paramsLog = new String[]{"END GET TRANSACTION NL (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION_NL, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public NotificationOrderRequest msbOffUsCallbackNL(PaymentAccount paymentAccount, String requestNotification) throws IOException {
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START CALLBACK TO NL MSB OFFUS (" + requestNotification + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_NOTIFICATION_ORDER, false, false, true, paramsLog));
            if (requestNotification != null) {
                NotificationOrderRequest request = objectMapper.readValue(requestNotification, NotificationOrderRequest.class);

                // gọi sang api Ngân Lượng
                Map<String, Object> params = new HashMap<>();
                params.put("id", request.getId());
                params.put("transDate", request.getTransDate());
                params.put("transId", request.getTransId());
                params.put("merchantName", request.getMerchantName());
                params.put("amount", request.getAmount());
                params.put("billNumber", request.getBillNumber());
                params.put("currency", request.getCurrency());
                params.put("message", request.getMessage());
                params.put("status", request.getStatus());

                Map<String, Object> mapData = new HashMap<>();
                mapData.put("params", requestNotification);
                //TEST
//                String response = HttpUtil.send(MSBOFFUSConstant.NL_NOTIFICATION_MSB_OFFUS, mapData, null);
                //LIVE
                String response = HttpUtil.send(MSBOFFUSConstant.NL_NOTIFICATION_MSB_OFFUS, params, MSBOFFUSConstant.USER_PASS_NGAN_LUONG);
                paramsLog = new String[]{"END CALLBACK TO NL MSB OFFUS (" + response + ")"};
                logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                        MSBOFFUSConstant.FUNCTION_NOTIFICATION_ORDER, false, false, true, paramsLog));
                return request;
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    private String convertParamUrl(NotificationOrderRequest request) {
        String url = "";
        if (request != null) {
            url += "?id=" + request.getId() + "&transDate" + request.getTransDate() + "&transId=" + request.getTransId() + "&amount=" + request.getAmount()
                    + "&billNumber=" + request.getBillNumber() + "&currency=" + request.getCurrency() + "&merchantName=" + request.getMerchantName()
                    + "&mId=" + request.getmId() + "&terminalId=" + request.getTerminalId() + "&status=" + request.getStatus() + "&message=" + request.getMessage()
                    + "&payDate=" + request.getPayDate() + "&returnUrl=" + request.getReturnUrl() + "&secureHash=" + request.getSecureHash();
        }
        return url;
    }

    private Payment createPaymentData(ChannelFunction channelFunction, CreateOrderRequest request) throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setChannelId(channelFunction.getChannel().getId());
        paymentDTO.setRawRequest(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + objectMapper.writeValueAsString(request));
        paymentDTO.setMerchantCode(request.getmId());
        paymentDTO.setMerchantName(request.getMerchantName());
        paymentDTO.setMerchantTransactionId(request.getBillNumber());
        paymentDTO.setAmount(request.getAmount());
        paymentDTO.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        Payment payment = paymentService.createPaymentDto(paymentDTO);
        return payment;
    }

    private Payment updatePaymentData(Payment payment, ResponseData response) throws Exception {
        CreateOrderResponse createOrderResponse = objectMapper.readValue(response.getData(), CreateOrderResponse.class);

        String responseStr = objectMapper.writeValueAsString(response);

        payment.setChannelTransactionId(createOrderResponse.getTransId());
        payment.setChannelStatus(response.getCode());
        payment.setChannelMessage(response.getDesc());
        payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        payment.setRawResponse(LogConst.LOG_CONTENT_PREFIX_ADD_TRANSACTION + responseStr);

        return paymentService.updatePayment(payment);
    }

    @Override
    public PGResponse createReversalTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START CREATE REVERSAL MSB OFFUS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_REFUND_TRANSACTION, true, true, false, paramsLog));
            // get param
            CreateRefundRequest request = objectMapper.readValue(inputStr, CreateRefundRequest.class);
            request.setTransactionType(MSBOFFUSConstant.TRANSACTION_TYPE);
            request.setTid(MSBOFFUSConstant.TERMINAL_ID);
            request.setMid(MSBOFFUSConstant.MERCHANT_ID);

            String requestPartners = objectMapper.writeValueAsString(request);
            paramsLog = new String[]{"REQUEST CREATE REFUND MSB OFFUS: Message - " + " (" + requestPartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_REFUND_TRANSACTION, true, true, false, paramsLog));
            // call to refund
            String responsePartners = MSBOFFUSClientRequest.sendRequestRefund(MSBOFFUSConstant.MSB_OFFUS_CREAT_REFFUND, requestPartners);
            paramsLog = new String[]{"RESPONSE CREATE REFUND MSB OFFUS: Message - " + " (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_REFUND_TRANSACTION, true, true, false, paramsLog));

            ResponseCreateRefund res = objectMapper.readValue(responsePartners, ResponseCreateRefund.class);

            pgResponse.setStatus(true);
            pgResponse.setErrorCode(res.getCode());
            pgResponse.setMessage(MSBOFFUSConstant.getErrorMessage(Integer.parseInt(res.getCode())));
            pgResponse.setData(objectMapper.writeValueAsString(res));

            // Write log end function
            paramsLog = new String[]{"END CREATE REVERSAL MSB OFFUS (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_REFUND_TRANSACTION, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse getInquiryRefundTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START GET TRANSACTION REFUND MSB OFFUS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION_REFUND, true, true, false, paramsLog));
            // get param
            GetTransactionRefundRequest request = objectMapper.readValue(inputStr, GetTransactionRefundRequest.class);
            request.setTransactionType(MSBOFFUSConstant.TRANSACTION_TYPE);

            String requestPartners = objectMapper.writeValueAsString(request);
            paramsLog = new String[]{"REQUEST GET TRANSACTION REFUND MSB OFFUS: Message - " + " (" + requestPartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION_REFUND, true, true, false, paramsLog));
            // call to get refund transaction
            String responsePartners = MSBOFFUSClientRequest.sendRequestRefund(MSBOFFUSConstant.MSB_OFFUS_GET_TRANSACTION_REFFUND, requestPartners);
            paramsLog = new String[]{"RESPONSE GET TRANSACTION REFUND MSB OFFUS: Message - " + " (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION_REFUND, true, true, false, paramsLog));

            ResponseRefundData res = objectMapper.readValue(responsePartners, ResponseRefundData.class);

            pgResponse.setStatus(true);
            pgResponse.setErrorCode(res.getCode());
            pgResponse.setMessage(res.getMessage());
            pgResponse.setData(objectMapper.writeValueAsString(res));

            // Write log end function
            paramsLog = new String[]{"END GET TRANSACTION REFUND MSB OFFUS (" + responsePartners + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_GET_TRANSACTION_REFUND, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse updateTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            PGResponse pgResponse = new PGResponse();
            // Write log start function
            paramsLog = new String[]{"START UPDATE TRANSACTION MSB OFFUS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_REFUND_TRANSACTION, true, true, false, paramsLog));
            // get trabnsaction
            UpdateTransactionRequest request = objectMapper.readValue(inputStr, UpdateTransactionRequest.class);
            // update
            Payment payment = paymentService.findByMerchantTransactionId(request.getTransId());
            payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
            paymentService.updatePayment(payment);

            paramsLog = new String[]{"RESPONSE UPDATE TRANSACTION MSB OFFUS: Message - " + " (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_REFUND_TRANSACTION, true, true, false, paramsLog));

            pgResponse.setStatus(true);
            pgResponse.setErrorCode("00");
            pgResponse.setMessage("SUCCESS");

            // Write log end function
            paramsLog = new String[]{"END CREATE REVERSAL MSB OFFUS (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBOFFUSConstant.CHANNEL_CODE, MSBOFFUSConstant.SERVICE_NAME,
                    MSBOFFUSConstant.FUNCTION_CREATE_REFUND_TRANSACTION, true, true, false, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }
}
