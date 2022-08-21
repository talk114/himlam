package gateway.core.channel.msb_onus.service.impl;

import gateway.core.channel.PaymentGate;
import gateway.core.channel.msb_onus.dto.MSBONUSConstant;
import gateway.core.channel.msb_onus.request.CreateOrderReq;
import gateway.core.channel.msb_onus.request.GetTransactionReq;
import gateway.core.channel.msb_onus.request.NotifyTransStatusReq;
import gateway.core.channel.msb_onus.request.VerifyTransactionReq;
import gateway.core.channel.msb_onus.response.DataCreateOrderRes;
import gateway.core.channel.msb_onus.response.GetTransactionRes;
import gateway.core.channel.msb_onus.response.MSBResponse;
import gateway.core.channel.msb_onus.service.MSBONUSService;
import gateway.core.dto.PGResponse;
import gateway.core.util.PGSecurity;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.entities.PaymentAccount;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonPGResponseService;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.RequestUtil;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author sonln
 */

@Service
public class MSBONUSServiceImpl extends PaymentGate implements MSBONUSService {
    private static final Logger logger = LogManager.getLogger(MSBONUSServiceImpl.class);

    @Autowired
    private CommonPGResponseService commonPGResponseService;
    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private PaymentService paymentService;

    @Override
    public PGResponse createOrder(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START: (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                    MSBONUSConstant.FUNCTION_CREATE_TRANSACTION, true, true, false, paramsLog));
            CreateOrderReq createOrderReq = objectMapper.readValue(inputStr, CreateOrderReq.class);
            Payment checkExits = paymentService.findByMerchantTransactionId(createOrderReq.getTransId());
            if (checkExits != null) {
                paramsLog = new String[]{"Transaction id already exist (" + createOrderReq.getTransId() + ")"};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_CREATE_TRANSACTION, false, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
            }
            String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                    channelFunction.getPort(), channelFunction.getUrl());

            switch (createOrderReq.getServiceType()) {
                case MSBONUSConstant.SERVICE_TYPE_TT:
                    createOrderReq.setmId(MSBONUSConstant.MERCHANT_ID_TT);
                    createOrderReq.settId(MSBONUSConstant.TERMINAL_ID_TT);
                    break;
                case MSBONUSConstant.SERVICE_TYPE_DT:
                    createOrderReq.setmId(MSBONUSConstant.MERCHANT_ID_DT);
                    createOrderReq.settId(MSBONUSConstant.TERMINAL_ID_DT);
                    break;
                case MSBONUSConstant.SERVICE_TYPE_HCC:
                    createOrderReq.setmId(MSBONUSConstant.MERCHANT_ID_HCC);
                    createOrderReq.settId(MSBONUSConstant.TERMINAL_ID_HCC);
                    break;
                default:
                    return commonPGResponseService.returnBadGatewayWithCause("NO SERVICE").getBody();
            }
            String secureCode = PGSecurity.sha256(MSBONUSConstant.ACCESS_CODE +
                    createOrderReq.getTransDate() + createOrderReq.getTransId() + createOrderReq.getmId() +
                    createOrderReq.getAmount() + createOrderReq.getBillNumber() + createOrderReq.getCurrency());
            createOrderReq.setSecureHash(secureCode);
            JSONObject cardInfo = new JSONObject()
                    .put("paymentType", createOrderReq.getPaymentType())
                    .put("cardNumber", createOrderReq.getCardNumber())
                    .put("cardName", createOrderReq.getCardName())
                    .put("releaseMonth", createOrderReq.getReleaseMonth())
                    .put("releaseYear", createOrderReq.getReleaseYear());

            //create payment
            logger.debug("CREATE DTO SAVE DATABASE");
            PaymentDTO payment = new PaymentDTO();
            payment.setChannelId(channelFunction.getChannel().getId());
            payment.setPgFunctionId(channelFunction.getId());
            payment.setMerchantCode(paymentAccount.getMerchantId());
            payment.setMerchantName(paymentAccount.getMerchantName());
            payment.setMerchantTransactionId(createOrderReq.getTransId());
            payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
            payment.setCardNo(createOrderReq.getCardNumber());
            payment.setAmount(createOrderReq.getAmount());
            payment.setRawRequest(objectMapper.writeValueAsString(createOrderReq));
            paymentService.createPayment(payment);

            createOrderReq.setCardName(null);
            createOrderReq.setCardNumber(null);
            createOrderReq.setReleaseMonth(null);
            createOrderReq.setReleaseYear(null);
            createOrderReq.setPaymentType(null);

            JSONObject request = new JSONObject(objectMapper.writeValueAsString(createOrderReq)).put("cardInfo", cardInfo);

            paramsLog = new String[]{"REQUEST - URL: " + url + "- BODY: [" + request.toString() + "]"};
            logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                    MSBONUSConstant.FUNCTION_CREATE_TRANSACTION, true, true, false, paramsLog));

            String response = sendRequest(url, HttpMethod.POST, request.toString());

            paramsLog = new String[]{"RESPONSE : " + response};
            logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                    MSBONUSConstant.FUNCTION_CREATE_TRANSACTION, true, false, true, paramsLog));

            MSBResponse msbResponse = objectMapper.readValue(response, MSBResponse.class);
            if (msbResponse.getData() != null) {
                String channelTransId = new JSONObject(msbResponse.getData()).getString("transId");
                Payment paymentUpdate = paymentService.findByMerchantTransactionId(payment.getMerchantTransactionId());
                paymentUpdate.setChannelTransactionId(channelTransId);
                paymentUpdate.setChannelStatus(msbResponse.getCode());
                paymentUpdate.setChannelMessage(msbResponse.getDesc());
                payment.setRawResponse(response);
                paymentService.updatePayment(paymentUpdate);
            }
            PGResponse pgResponse = buildResSuccess(msbResponse, DataCreateOrderRes.class);
            paramsLog = new String[]{"END : " + objectMapper.writeValueAsString(pgResponse)};
            logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                    MSBONUSConstant.FUNCTION_CREATE_TRANSACTION, true, false, true, paramsLog));
            return pgResponse;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse verifyTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START:  (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                    MSBONUSConstant.FUNCTION_VERIFY_TRANSACTION, true, true, false, paramsLog));
            VerifyTransactionReq req = objectMapper.readValue(inputStr, VerifyTransactionReq.class);
            Payment paymentCheck = paymentService.findByChannelTransactionId(req.getTransId());
            if (paymentCheck == null) {
                paramsLog = new String[]{"Transaction id not exist (" + paymentCheck + ")"};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_VERIFY_TRANSACTION, false, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
            } else {
                CreateOrderReq requestHash = objectMapper.readValue(paymentCheck.getRawRequest(), CreateOrderReq.class);
                String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                        channelFunction.getPort(), channelFunction.getUrl());
                String secureCode = PGSecurity.sha256(MSBONUSConstant.ACCESS_CODE +
                        req.getTransId() + requestHash.getmId() + requestHash.gettId() + req.getOtp());
                req.setSecureHash(secureCode);
                req.setmId(requestHash.getmId());
                req.settId(requestHash.gettId());

                paramsLog = new String[]{"REQUEST - URL: " + url + "- BODY: [" + objectMapper.writeValueAsString(req) + "]"};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_VERIFY_TRANSACTION, true, true, false, paramsLog));

                String response = sendRequest(url, HttpMethod.POST, objectMapper.writeValueAsString(req));

                paramsLog = new String[]{"RESPONSE : " + response};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_VERIFY_TRANSACTION, true, false, true, paramsLog));

                JSONObject msbResponse = new JSONObject(response);
                if (msbResponse.getString("code").equals("00")
                        && msbResponse.getString("transId").equals(paymentCheck.getChannelTransactionId())
                        && Long.valueOf(msbResponse.getString("amount")).equals(paymentCheck.getAmount().toBigInteger().longValue())) {
                    paymentCheck.setChannelTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                    paymentCheck.setPgTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code());
                    paymentService.updatePayment(paymentCheck);

                }
                PGResponse pgResponse = new PGResponse();
                pgResponse.setStatus(true);
                pgResponse.setErrorCode(msbResponse.getString("code"));
                pgResponse.setMessage(msbResponse.has("message") ? msbResponse.getString("message") : "");
                paramsLog = new String[]{"END : " + objectMapper.writeValueAsString(pgResponse)};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_VERIFY_TRANSACTION, true, false, true, paramsLog));
                return pgResponse;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse getTransaction(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) {
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START GET TRANSACTION  (" + inputStr + ")"};
            logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                    MSBONUSConstant.FUNCTION_GET_TRANSACTION, true, true, false, paramsLog));
            GetTransactionReq req = objectMapper.readValue(inputStr, GetTransactionReq.class);
            Payment paymentCheck = paymentService.findByChannelTransactionId(req.getTransId());
            if (paymentCheck == null) {
                paramsLog = new String[]{"Transaction id not exist (" + paymentCheck + ")"};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_GET_TRANSACTION, false, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
            } else {
                String url = RequestUtil.createUrlChannelFunction(channelFunction.getHost(),
                        channelFunction.getPort(), channelFunction.getUrl());
//                Payment payment = paymentService.findByChannelTransactionId(req.getTransId());
                CreateOrderReq requestHash = objectMapper.readValue(paymentCheck.getRawRequest(), CreateOrderReq.class);
                String secureCode = PGSecurity.sha256(MSBONUSConstant.ACCESS_CODE +
                        requestHash.getTransDate() + req.getTransId() + requestHash.getmId() +
                        requestHash.getAmount() + requestHash.getBillNumber() + requestHash.getCurrency());
                req.setMerchantId(requestHash.getmId());
                req.setSecureHash(secureCode);

                paramsLog = new String[]{"REQUEST - URL: " + url + "- BODY: [" + objectMapper.writeValueAsString(req) + "]"};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_GET_TRANSACTION, true, true, false, paramsLog));

                String response = sendRequest(url, HttpMethod.POST, objectMapper.writeValueAsString(req));

                paramsLog = new String[]{"RESPONSE : " + response};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_GET_TRANSACTION, true, false, true, paramsLog));

                MSBResponse msbResponse = objectMapper.readValue(response, MSBResponse.class);
                PGResponse pgResponse = buildResSuccess(msbResponse, GetTransactionRes.class);
                paramsLog = new String[]{"END : " + objectMapper.writeValueAsString(pgResponse)};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_GET_TRANSACTION, true, false, true, paramsLog));
                return pgResponse;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    @Override
    public PGResponse notifyTransStatus(ChannelFunction channelFunction, PaymentAccount paymentAccount, String data) {
        String[] paramsLog;
        try {
            paramsLog = new String[]{"START UPDATE TRANSATUS  (" + data + ")"};
            logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                    MSBONUSConstant.FUNCTION_NOTI_TRANSACTION_STATUS, true, true, false, paramsLog));
            NotifyTransStatusReq notifyTransStatusReq = objectMapper.readValue(data, NotifyTransStatusReq.class);
            Payment paymentUpdate = paymentService.findByChannelTransactionId(notifyTransStatusReq.getTransId());
            if (paymentUpdate != null) {
                Integer status;
                if (notifyTransStatusReq.isSuccess()) {
                    status = PaymentConst.EnumBankStatus.SUCCEEDED.code();
                } else {
                    status = PaymentConst.EnumBankStatus.FAILED.code();
                }
                paymentUpdate.setMerchantTransactionStatus(status);
                paymentService.updatePayment(paymentUpdate);
                paramsLog = new String[]{"UPDATE STATUS TRANS SUCCESS (" + paymentUpdate + ")"};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_NOTI_TRANSACTION_STATUS, false, false, true, paramsLog));
                return commonPGResponseService.returnGatewayRequestSuccessPrefix();
            } else {
                paramsLog = new String[]{"Transaction id not exist (" + paymentUpdate + ")"};
                logger.info(commonLogService.createContentLog(MSBONUSConstant.CHANNEL_CODE, MSBONUSConstant.SERVICE_NAME,
                        MSBONUSConstant.FUNCTION_NOTI_TRANSACTION_STATUS, false, false, true, paramsLog));
                return commonPGResponseService.returnBadGatewayWithCause(paramsLog[0]).getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return commonPGResponseService.returnBadGatewayWithCause(e.getMessage()).getBody();
        }
    }

    private String sendRequest(String url, HttpMethod method, String bodyRequest)
            throws KeyManagementException, NoSuchAlgorithmException {
        RestTemplate restTemplate = new RestTemplate(RequestUtil.createRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(bodyRequest, headers);
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(url, method, entity, String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException exx) {
            response = new ResponseEntity<>(exx.getResponseBodyAsString(), HttpStatus.BAD_REQUEST);
            return response.getBody();
        }


    }


    private <T> PGResponse buildResSuccess(MSBResponse msbResponse, Class<T> data) throws IOException {
        PGResponse pgResponse = new PGResponse();
        pgResponse.setErrorCode(msbResponse.getCode());
        if (msbResponse.getDesc() == null) {
            pgResponse.setMessage(MSBONUSConstant.getErrorMessage(Integer.parseInt(pgResponse.getErrorCode())));
        } else {
            pgResponse.setMessage(msbResponse.getDesc());
        }
        if (data != null && StringUtils.isNotEmpty(msbResponse.getData())) {
            T dataObject = objectMapper.readValue(msbResponse.getData(), data);
            pgResponse.setData(dataObject);
        }
        pgResponse.setStatus(true);
        return pgResponse;
    }
}
