package gateway.core.channel.mb_qrcode.dto;

import com.google.common.collect.ImmutableMap;

public class MBBankVNPayOffUsConstant {

    public static final String SECRET_KEY = "V3uDvRYJO9lSksacArvj95w1i1ytms94ISagyVvexcgxriq8UxJhZIxGryES8xMS";//"kIYKGjzslyGUwF5KHxhPEYQnFJAnaHc2mT0aFdajTYUgnGuApYAmEFqzPBEgXKwm"; // thông tin này do ngân hàng cấp  MB_NL1
    public static final String USER_NAME = "MB_NGLUONG ";//"MB_NL1"; // thông tin này do ngân hàng cấp
    public static final String MERCHANT_SHORT_NAME = "NGLUONG";//"MB_NL1"; // thông tin này do ngân hàng cấp
//    public static final String ACCESS_KEY = "NL1Accesskey"; // thông tin này do ngân hàng cấp
    public static final String ACCESS_KEY = "MilitaryBank@1994"; // thông tin này do ngân hàng cấp - update
    public static final String TERMINAL_ID = "NGLUONG1"; // thông tin này do ngân hàng cấp - update
    public static final String URL_API_LIST_MID = "http://118.70.170.147:811/public/api/mid/v1.0/list";
    public static final String URL_API_SYNCHRONIZE_MERCHANT = "http://118.70.170.147:811/ public/api/mid/v1.0/synchronize";

//    public static final String URL_API_CREAT_QRCODE = "https://103.12.105.232:8763/mms-payment-service/public/api/payment/v1.0/createqr";//"http://118.70.170.147:8051/payment/public/api/payment/v1.0/createqr";
    public static final String URL_API_CREAT_QRCODE = "https://zuulin.mbbank.com.vn:8704/public/payment-service/payment/v1.0/createqr";//"http://118.70.170.147:8051/payment/public/api/payment/v1.0/createqr";-update new

//    public static final String URL_API_CHECK_ORDER = "https://103.12.105.232:8763/mms-payment-service/public/api/payment/v1.0/checkOrder";//"http://118.70.170.147:8051/payment/public/api/payment/v1.0/checkOrder";
    public static final String URL_API_CHECK_ORDER = "https://zuulin.mbbank.com.vn:8704/public/payment-service/payment/v1.0/checkOrder";//"http://118.70.170.147:8051/payment/public/api/payment/v1.0/checkOrder";-update new
    public static final String URL_API_CALL_NL = "https://www.nganluong.vn/api/mbqrcode/index";
    public static final String URL_API_CALL_NL_DS = "https://www.nganluong.vn/api/mbqrcode/compareTransaction";
    public static final String USER_PASS_NGAN_LUONG = "adminNL:admin@NL2019";

    public static final String CHANNEL_CODE = "MBBANK_QRCODE_OFFUS";
    public static final String FUNCTION_CODE_CREATE_QR_CODE = "createQRCode";
    public static final String FUNCTION_CODE_CHECK_ORDER = "checkOrder";

    static ImmutableMap<Integer, String> mappingError = ImmutableMap.<Integer, String>builder()
            .put(200, "SUCCESS")
            .put(201, "PARAM_INVALID")
            .put(1, "CANNOT_CONNECT_TO_HOST")
            .put(101, "PERMISSION_DENIED")
            .put(205, "PARAM_IS_WRONG_FORMAT")
            .put(500, "UNKNOWN_ERROR")
            .put(99, "CANNOT VERIFY CHECK SUM OF MB")
            .put(0, "SUCCESS")
            .put(1, "Data is not in format")
            .put(7, "Merchant is not exist")
            .put(11, "Merchant is not active")
            .put(21, "Terminal is not exist")
            .put(24, "Terminal is inactive")
            .put(29, "Qrcode is not active")
            .put(32, "These Qrcode dont have the same terminal")
            .put(31, "These Qrcode dont have the same merchant")
            .put(31, "These Qrcode dont have the same merchant")
            .put(3, "Bank code is not exist")
            .put(4, "Mobile is invalid")
            .put(6, "Pay date is not in format")
            .put(12, "False checkSum")
            .put(14, "Invalid Transaction")
            .put(16, "Invalid Amount")
            .put(18, "Invalid ResponseCode")
            .put(23, "Transaction duplicate")
            .put(25, "MessageType is invalid")
            .put(96, "System is maintaining")
            .put(99, "Internal error")
            .put(64, "Internal error")
            .put(64, "Voucher is locked.")
            .put(69, "Voucher is out of date.")
            .build();

    public static String getMessage(int errorCode) {
        return mappingError.get(errorCode);
    }
}
