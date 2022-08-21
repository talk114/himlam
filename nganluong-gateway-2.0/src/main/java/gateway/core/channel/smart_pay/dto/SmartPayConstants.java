package gateway.core.channel.smart_pay.dto;

import com.google.common.collect.ImmutableMap;


public class SmartPayConstants {

    public static final String BASE_URL_CALL_API = "https://mch.paysmart.com.vn/v1.0";//"https://gateway.paysmart.com.vn/v1.0/order";
    public static final String URL_CALL_BACK = "https://gateway02.nganluong.vn/PaymentGateway/restful/apiVM/smartPayCallback";
    public static final String URL_CALL_NGAN_LUONG = "https://www.nganluong.vn/api/smartpay/index";
    public static final String USER_PASS_NGAN_LUONG = "adminNL:admin@NL2019";

    public static final String HASH_KEY = "S2jNuY3NjQg9rP8FL8QjUvjqg3en4En8";//"X8wewEcgXuQr9xXM";
    public static final String CHANNEL = "00000004";//"qrpayment";00000004
    public static final String SUB_CHANNEL = "NLJSC";//"00256058"; NL123

    public static final String CHANNEL_CODE = "SMART_PAY";
    public static final String FUNCTION_CODE_CREATE_ORDER = "createOrder";
    public static final String FUNCTION_CODE_QUERY_ORDER = "queryOrder";
    public static final String FUNCTION_CODE_SEND_OTP = "sendOTP";
    public static final String FUNCTION_CODE_VERIFY_OTP = "verifyOTP";

    private static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
            .put("OK", "The request is processed successfully")
            .put("ERR_INVALID_REQUEST", "Invalid request")
            .put("ERR_INVALID_REQUEST_DATA", "Invalid request data")
            .put("ERR_INVALID_FORMAT_DATE", "Invalid format field created.")
            .put("ERR_INVALID_TIME", "Invalid time.")
            .put("ERR_INVALID_SIGN", "Invalid signature.")
            .put("ERR_INVALID_ORDER_NO", "Invalid orderNo.")
            .put("ERR_INVALID_URL", "Invalid url.")
            .put("ERR_INVALID_AMOUNT", "Invalid amount.")
            .put("ERR_REQUEST_ID_DUPLICATE", "RequestId duplicate.")
            .put("ERR_ORDER_NO_DUPLICATE", "OrderNo duplicate.")
            .put("ERR_DUPLICATE", "RequestId duplicate.")
            .put("ERR_INVALID_CHANNEL", "Channel info is invalid.")
            .put("ERR_INVALID_SUB_CHANNEL", "SubChannel info is invalid.")
            .put("ERR_TIMEOUT", "The request is expired.")
            .put("ERR_OTP_UNAUTHORIZED", "Unauthorized OTP")
            .put("ERR_OTP_MISMATCH", "Invalid OTP")
            .put("ERR_OTP_EXCEED_MAX_ATTEMPS", "Max attempts exceeded")
            .put("ERR_OTP_EXPIRED", "Expired")
            .put("ERR_VERIFY_SIGNATURE_BANK", "Cannot verify signature of bank")
            .put("ERR_REQUEST_TO_BANK", "cannot request to bank")
            .put("ERR_SYSTEM", "Error system.")
            .build();

    public static String getMessageErrorCode(String errorCode) {
        return ERROR_MESSAGE.get(errorCode);
    }

}
