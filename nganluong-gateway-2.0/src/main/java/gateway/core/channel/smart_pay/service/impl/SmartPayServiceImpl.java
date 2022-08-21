package gateway.core.channel.smart_pay.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.base.Strings;
import gateway.core.channel.PaymentGate;
import gateway.core.channel.smart_pay.SmartPayClientRequest;
import gateway.core.channel.smart_pay.SmartPaySecurity;
import gateway.core.channel.smart_pay.dto.SmartPayConstants;
import gateway.core.channel.smart_pay.dto.req.*;
import gateway.core.channel.smart_pay.dto.res.*;
import gateway.core.channel.smart_pay.service.SmartPayService;
import gateway.core.dto.PGResponse;
import gateway.core.dto.request.DataRequest;
import gateway.core.util.HttpUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import static gateway.core.channel.smart_pay.dto.SmartPayConstants.URL_CALL_NGAN_LUONG;
import static gateway.core.channel.smart_pay.dto.SmartPayConstants.USER_PASS_NGAN_LUONG;

@Service
public class SmartPayServiceImpl extends PaymentGate implements SmartPayService {
    private static final Logger logger = LogManager.getLogger(SmartPayServiceImpl.class);
    /**
     * create Order, init payment process
     *
     * @param request
     * @return
     * @throws java.io.IOException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public PGResponse createOrder(String request) throws IOException, IllegalArgumentException, IllegalAccessException {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);

        // build common request
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        buildCommonRequest(createOrderRequest, dataRequest);
        createOrderRequest.setOrderNo(dataRequest.getOrderCode());
        createOrderRequest.setAmount(dataRequest.getAmount().intValue());
        createOrderRequest.setDesc(dataRequest.getDescription());
        createOrderRequest.setNotifyUrl(SmartPayConstants.URL_CALL_BACK);
        createOrderRequest.setRequestType("qrcode");
        createOrderRequest.setCreated(convertDateTimeToPattern("yyyy-MM-dd'T'HH:mm:ss", dataRequest.getTransTime()));
        // signature
        JSONObject json = new JSONObject(createOrderRequest);
        Map<String, Object> map = json.toMap();
        map.put("requestId", dataRequest.getTransId());
        String signature = SmartPaySecurity.genSign(SmartPayConstants.HASH_KEY, dataForSignature(map));
        createOrderRequest.setSignature(signature);

        return process(createOrderRequest, SmartPayConstants.BASE_URL_CALL_API + "/qr/createorder", CreateOrderResponse.class, dataRequest.getTransId(), "createOrder");
    }

    /**
     * query order
     *
     * @param request
     * @return
     * @throws java.io.IOException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public PGResponse queryOrder(String request) throws IOException, IllegalAccessException {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);

        // build request
        QueryOrderRequest queryOrderRequest = new QueryOrderRequest();
        buildCommonRequest(queryOrderRequest, dataRequest);
        queryOrderRequest.setPrepayId(dataRequest.getPrepayId());
        queryOrderRequest.setOrderNo(dataRequest.getOrderCode());
        //TODO: create signature
        JSONObject json = new JSONObject(queryOrderRequest);
        Map<String, Object> map = json.toMap();
        map.put("requestId", dataRequest.getTransId());
        String signature = SmartPaySecurity.genSign(SmartPayConstants.HASH_KEY, dataForSignature(map));
        queryOrderRequest.setSignature(signature);
        return process(queryOrderRequest, SmartPayConstants.BASE_URL_CALL_API + "/check/queryorder", QueryOrderResponse.class, dataRequest.getTransId(), "queryOrder");
    }

    /**
     * 6: Notification: after completing payment Smart pay will send
     * notification to Ngan Luong
     *
     * @param request
     * @return
     * @throws java.io.IOException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public String notification(String request) throws IOException, IllegalAccessException {
        // TODO
        WriteInfoLog("IN NOTIFICATION METHOD SMART_PAY CALL BACK: " + request);
        NotificationRequest notificationRequest = objectMapper.readValue(request, NotificationRequest.class);
        // TODO
        WriteInfoLog("IN NOTIFICATION METHOD SMART_PAY CALL BACK: " + objectMapper.writeValueAsString(notificationRequest));
        //TODO: verify signature
        JSONObject json = new JSONObject(notificationRequest);
        Map<String, Object> map = json.toMap();
        map.put("requestId", notificationRequest.getRequestId());
        String signatureVerify = SmartPaySecurity.genSign(SmartPayConstants.HASH_KEY, dataForVerifySignature(map));
        //TODO: Neu verify dc signature thi check trang thai cua giao dich
        if (signatureVerify.equals(notificationRequest.getSignature()) || true) {
            //TODO: Neu callback cua smart_pay status = PROCESSING thi goi api queryOrder de check status
            PGResponse response = null;
            String statusNotification = notificationRequest.getStatus();
            int timesQueryOrder = 0;
            QueryOrderResponse queryOrderResponse = null;
            if (!statusNotification.equals("PROCESSING")) { // Trường hợp mà trạng thái của giao dịch là: OPEN, PAYED, REFUND, PROCESSING, PAY_ERROR thì call luôn Ngân Lượng
                return callBackToNganLuong(notificationRequest, "0", "Thành công", notificationRequest.getRequestId());
            }
            while (statusNotification.equals("PROCESSING")) {
                DataRequest dataQueryOrder = new DataRequest();
                dataQueryOrder.setTransId("NGLA" + System.currentTimeMillis());
                dataQueryOrder.setOrderCode(notificationRequest.getOrderNo());
                WriteInfoLog("TIMES QUERY ORDER: " + timesQueryOrder + " REQUEST: " + objectMapper.writeValueAsString(dataQueryOrder));
                PGResponse responseQueryOrder = queryOrder(objectMapper.writeValueAsString(dataQueryOrder));
//                String responseQueryOrder = "{\"status\":true,\"error_code\":\"OK\",\"message\":\"The request is processed successfully\",\"data\":{\"transId\":\"20200228000000037\",\"status\":\"PAYED\",\"amount\":50000,\"created\":\"2020-02-28T17:06:37\",\"orderNo\":\"NGLA1582884397330\",\"signature\":\"3ED8D38E0836CDDA3C1F42819087A659A9DFEE64E59807BD3E362F8CB0B287FF\"}}";
                WriteInfoLog("TIMES QUERY ORDER: " + timesQueryOrder + " RESPONSE: " + objectMapper.writeValueAsString(responseQueryOrder));
                //response = objectMapper.readValue(responseQueryOrder, PGResponse.class);
                statusNotification = objectMapper.readValue(objectMapper.writeValueAsString(responseQueryOrder.getData()), QueryOrderResponse.class).getStatus();
                queryOrderResponse = objectMapper.readValue(objectMapper.writeValueAsString(responseQueryOrder.getData()), QueryOrderResponse.class);
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    logger.info(ExceptionUtils.getStackTrace(e));
                }
                timesQueryOrder++;
            }
            //TODO: CALL NGAN LUONG
            return callBackToNganLuong(queryOrderResponse, "0", "Thành Công", notificationRequest.getRequestId());
        } else {
            //TODO: CALL NGAN LUONG KHI KHONG VERIFY SIGNATURE
            return callBackToNganLuong(null, "1", "Sai Signature", notificationRequest.getRequestId());
        }
    }

    /**
     * 7: send OTP
     *
     * @param request
     * @return
     * @throws java.io.IOException
     * @throws java.lang.IllegalAccessException
     */
    @Override
    public PGResponse sendOTP(String request) throws IOException, IllegalAccessException {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);

        // build request
        SendOTPRequest sendOTPRequest = new SendOTPRequest();
        buildCommonRequest(sendOTPRequest, dataRequest);
        sendOTPRequest.setPhone(dataRequest.getCustomerPhone());

        //signature
        JSONObject json = new JSONObject(sendOTPRequest);
        Map<String, Object> map = json.toMap();
        map.put("requestId", dataRequest.getTransId());
        String signature = SmartPaySecurity.genSign(SmartPayConstants.HASH_KEY, dataForSignature(map));
        sendOTPRequest.setSignature(signature);
        return process(sendOTPRequest, SmartPayConstants.BASE_URL_CALL_API + "/qr/otp/send", SendOTPResponse.class, dataRequest.getTransId(), "sendOTP");
    }

    /**
     * 7.2 Verify OTP
     *
     */
    @Override
    public PGResponse verifyOTP(String request) throws IOException, IllegalAccessException {
        DataRequest dataRequest = objectMapper.readValue(request, DataRequest.class);
        VerifyOTPRequest verifyOTPRequest = new VerifyOTPRequest();
        buildCommonRequest(verifyOTPRequest, dataRequest);
        verifyOTPRequest.setOtp(dataRequest.getOtp());
        verifyOTPRequest.setPhone(dataRequest.getCustomerPhone());
        verifyOTPRequest.setTransId(dataRequest.getTransIdOTP());

        //signature
        JSONObject json = new JSONObject(verifyOTPRequest);
        Map<String, Object> map = json.toMap();
        map.put("requestId", dataRequest.getTransId());
        String signature = SmartPaySecurity.genSign(SmartPayConstants.HASH_KEY, dataForSignature(map));
        verifyOTPRequest.setSignature(signature);
        return process(verifyOTPRequest, SmartPayConstants.BASE_URL_CALL_API + "/qr/otp/verify", VerifyOTPResponse.class, dataRequest.getTransId(), "verifyOTP");
    }

    private <T extends BaseResponse> PGResponse process(SmartPayBaseRequest request, String api, Class<T> resClass, String requestId, String func) throws IOException, IllegalAccessException {
        JSONObject json = new JSONObject(request);
        WriteInfoLog("SMART PAY REQUEST", objectMapper.writeValueAsString(request));
        Map<String, Object> map = json.toMap();
        // call Api
        String response = SmartPayClientRequest.callApi(objectMapper.writeValueAsString(request), api, requestId);
        WriteInfoLog("SMART PAY RESPONSE " + map.get("requestId") + response);
        RootResponse rootResponse = null;
        T res = null;
        if (!Strings.isNullOrEmpty(response)) {
            rootResponse = objectMapper.readValue(response, RootResponse.class);
            if (rootResponse.getData() != null) {
                res = objectMapper.readValue(objectMapper.writeValueAsString(rootResponse.getData()), resClass);
                // verify signature
                addDataToMap(res, map);
                map.put("requestId", rootResponse.getRequestId());
                map.put("func", func);
                String signature = SmartPaySecurity.genSign(SmartPayConstants.HASH_KEY, dataForVerifySignature(map));
                if (signature.equals(res.getSignature())) {
                    return buildResponse(res, rootResponse.getCode(), true);
                } else { // neu khong verify dc signature
                    return buildResponse(res, "ERR_VERIFY_SIGNATURE_BANK", true);
                }
            } else {
                return buildResponse(request, rootResponse.getCode(), true);
            }
        }
        return buildResponse(res, "ERR_REQUEST_TO_BANK", false);
    }

    private String callBackToNganLuong(Object data, String errorCode, String errorMessage, String notifyRequestId) throws IOException {
        CallNganLuongRequest callNganLuongRequest = new CallNganLuongRequest();
        callNganLuongRequest.setErrorCode(errorCode);
        callNganLuongRequest.setMessage(errorMessage);
        callNganLuongRequest.setData(data);
        Map<String, Object> params = new HashMap<>();
        params.put("data", objectMapper.writeValueAsString(callNganLuongRequest));
        WriteInfoLog("CALL BACK NGAN LUONG REQUEST- " + notifyRequestId + " " + objectMapper.writeValueAsString(callNganLuongRequest) + "###########");
        String nganLuongResponse = HttpUtil.send(URL_CALL_NGAN_LUONG, params, USER_PASS_NGAN_LUONG);
        WriteInfoLog("######################## CALL-BACK NGAN LUONG RESPONSE- " + notifyRequestId + ": " + nganLuongResponse + " ########################");
//            CallNganLuongResponse nganLuongResponse = new CallNganLuongResponse("Call back to Ngan Luong success", "00");
        return nganLuongResponse;
    }

    private PGResponse buildResponse(Object data, String errorCode, boolean status) throws JsonProcessingException {
        PGResponse pgResponse = new PGResponse();
        pgResponse.setStatus(status);
        pgResponse.setErrorCode(errorCode);
        pgResponse.setMessage(SmartPayConstants.getMessageErrorCode(errorCode));
        pgResponse.setData(data);
        //return objectMapper.writeValueAsString(pgResponse);
        return pgResponse;
    }

    private void buildCommonRequest(SmartPayBaseRequest request, DataRequest dataRequest) {
//        request.setChannel(dataRequest.getChannel());
//        request.setSubChannel(dataRequest.getSubChannel());
        request.setChannel(SmartPayConstants.CHANNEL);
        request.setSubChannel(SmartPayConstants.SUB_CHANNEL);
//        request.setOrderNo(dataRequest.getOrderCode());
    }

    private String convertDateTimeToPattern(String pattern, String time) {
        Date d = new Date(Long.parseLong(time) * 1000);
        DateFormat f = new SimpleDateFormat(pattern);
        return f.format(d);
    }

    private Map<String, Object> addDataToMap(Object data, Map<String, Object> map) throws IllegalAccessException {
        for (Field field : data.getClass().getDeclaredFields()) {
            field.setAccessible(true); // You might want to set modifier to public first.
            Object value = field.get(data);
            if (value != null) {
                map.put(field.getName(), value);
            }
        }
        return map;
    }

    private Map<String, Object> dataForSignature(Map<String, Object> map) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> mapData = new TreeMap<>();
        if (map.containsKey("amount")) {
            mapData.put("amount", DEFAULT_VALUE);
        }
        mapData.put("channel", SmartPayConstants.CHANNEL);
        if (map.containsKey("orderNo")) {
            mapData.put("orderNo", DEFAULT_VALUE);
        }
        if (map.containsKey("prepayId")) {
            mapData.put("prepayId", DEFAULT_VALUE);
        }
        mapData.put("requestId", DEFAULT_VALUE);
        mapData.put("subChannel", SmartPayConstants.SUB_CHANNEL);
        if (map.containsKey("transId")) {
            mapData.put("transId", DEFAULT_VALUE);
        }

        // loop all fields of class
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
            if (mapData.containsKey(pair.getKey())) {
                mapData.put(String.valueOf(pair.getKey()), pair.getValue());
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return mapData;
    }

    private Map<String, Object> dataForVerifySignature(Map<String, Object> map) throws IllegalArgumentException, IllegalAccessException {
        Map<String, Object> mapData = new TreeMap<>();
        if (map.containsKey("amount")) {
            mapData.put("amount", DEFAULT_VALUE);
        }
        mapData.put("channel", SmartPayConstants.CHANNEL);
        if (map.containsKey("orderNo")) {
            mapData.put("orderNo", DEFAULT_VALUE);
        }
        if (map.containsKey("prepayId") && !map.get("func").equals("queryOrder")) {//!map.get("func").equals("queryOrder")
            mapData.put("prepayId", DEFAULT_VALUE);
        }
        mapData.put("requestId", DEFAULT_VALUE);
        mapData.put("subChannel", SmartPayConstants.SUB_CHANNEL);
        if (map.containsKey("transId")) {
            mapData.put("transId", DEFAULT_VALUE);
        }

        // loop all fields of class
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
//            System.out.println(pair.getKey() + " = " + pair.getValue());
            if (mapData.containsKey(pair.getKey())) {
                mapData.put(String.valueOf(pair.getKey()), pair.getValue());
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return mapData;
    }
}
