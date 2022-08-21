package gateway.core.channel.vcb_ib.dto;

import com.google.common.collect.ImmutableMap;

public class VCBIbConstants {

    public static final String  SERVICE_NAME = "IB";

    // PATH KEY TEST:
    public static final String GW_PATH_PRIVATE_KEY = "vcb_ecom_key_live/private_key.xml";
	public static final String GW_PATH_PUBLIC_KEY = "vcb_ecom_key_live/public_key.xml";
	public static final String VCB_ECOM_PATH_PUBLIC_KEY = "vcb_ecom_key_live/public_key_VCB.xml";
        
    //public static final String GW_PATH_PRIVATE_KEY = "vcb_ib_key/private_key.xml";//"vcb_ib_key/private_key_GW.xml";
    //public static final String GW_PATH_PUBLIC_KEY = "vcb_ib_key/public_key.xml";//"vcb_ib_key/public_key_GW.xml";
    //public static final String VCB_ECOM_PATH_PUBLIC_KEY = "vcb_ib_key/public_key_VCB.xml";

    // URL call VCB TEST
//    public static final String URL_API_CALL_VCB = "http://192.168.200.87:9091/EcomV2/PaymentServiceV2.asmx";
    //LIVE ECOM ATM
    public static final String URL_API_CALL_VCB = "http://192.168.200.234:9091/EcommerceService/PaymentServiceV2.asmx";//"http://192.168.200.234:9091/EcommerceService_UAT/Payment_Service_Card_V3.asmx";

    //public static final String URL_API_CALL_VCB = "http://192.168.200.87:9091/EcomV3/Payment_Service_Card_V3.asmx";
//    public static final String VCB_IB_PARTNER_ID = "NLVN_IB";
//    public static final String VCB_IB_PASSWORD = "NLVN#123*abdsf";
//    public static final String VCB_IB_MERCHANT_ID = "NLVN_IB";
	public static final String VCB_IB_PARTNER_ID = "NLVN_IB";
	public static final String VCB_IB_PASSWORD = "NLVN#123*abdsf";//"NLVN#be4S9w1R6ep1belbyfIA8M0i&5zQHONGyS3";
	public static final String VCB_IB_MERCHANT_ID = "NLVN_IB";

    public static final String TRANS_SUCCESS = "1";
    private static final String MISSING_PARAM = "2";
    private static final String WRONG_TRANS_ID = "3";
    private static final String REFUND_AMOUNT_INVALID = "5";
    private static final String REFUND_CURR_INVALID = "6";
    private static final String ORIG_TRANS_FAIL_OR_REFUNED = "7";
    private static final String REFUND_ERROR = "8";
    private static final String EXCEPTION = "09";
    private static final String WRONG_PARTNER_INFO = "11";

    private static final String WRONG_AMOUNT = "12";
    private static final String AMOUNT_NOT_IN_RANGE_VALID = "13";
    private static final String EMPRY_SIGN_PUBKEY = "14";
    private static final String CANNOT_FIND_TRANSID = "15";
    private static final String TRANS_FAIL = "16";
    private static final String NOT_AVAILABLE_BALANCE = "17";
    private static final String ACC_STATUS_INVALID = "18";
    private static final String ACC_CANNOT_DETERMINE = "19";
    private static final String REQUEST_MESSAGE_ERROR = "20";
    private static final String INIT_TRANS_ERROR = "21";
    private static final String BRANCH_CANNOT_DETERMINE = "22";
    private static final String VERIFY_OTP_FAIL = "23";
    private static final String TRANS_DATE_NOT_DETERMINE = "24";
    private static final String TRANS_PAID_BEFORE = "25";
    private static final String MOBILE_SMS_CANNOT_DETERMINE = "26";
    private static final String CURRENCY_ACC_CANNOT_PAY = "27";
    private static final String TRANS_NOT_PAY = "28";
    private static final String PAY_FAIL = "31";
    private static final String EXCEPTION_2 = "32";
    private static final String WRONG_TRANS_ID_2 = "33";
    private static final String TRANS_VALID = "40";
    private static final String SYSTEM_MAINTAIN = "50";
    private static final String CARD_LOCKED_OR_INVALID = "61";
    private static final String CARD_INFO_WRONG = "62";
    private static final String ACCEPT_VCB_CONNECT_24_ONLY = "63";
    private static final String DONT_HAVE_IB_ACC = "64";
    private static final String WRONG_MERCHANT_ID = "65";
    private static final String WRONG_VALID_AMOUNT_PAYMENT = "66";
    private static final String WRONG_CURRENCY = "67";
    private static final String DECRYPT_ERROR = "68";
    private static final String VERIFY_SIGNATURE_ERROR = "69";
    private static final String WRONG_SIGNATURE = "70";
    private static final String VERIFY_SIGNATURE_EXCEPTION = "98";
    private static final String UNKNOWN_ERROR = "99";
    private static final String INVALID_OTP = "73";
    private static final String SIGNUP_SMS = "71";
    private static final String CANNOT_SEND_OTP = "72";

    public static final String CHANNEL_CODE = "VCB_IB";
    public static final String FUNCTION_CODE_QUERY = "query";
    public static final String FUNCTION_CODE_VERIFY_PAYMENT = "verifyPayment";
    public static final String FUNCTION_CODE_REFUND = "refund";
    public static final String FUNCTION_CODE_CALLBACK = "vcbIbCallback";

    static final ImmutableMap<String, String> MAP_ERROR = ImmutableMap.<String, String>builder()
            .put(TRANS_SUCCESS, "Giao dịch thành công.")
            .put(MISSING_PARAM, "Tham số không hợp lệ.")
            .put(WRONG_TRANS_ID, "Sai mã giao dịch.")
            .put(REFUND_AMOUNT_INVALID, "Số tiền hoàn lớn hơn số tiền thanh toán.")
            .put(REFUND_CURR_INVALID, "Đơn vị tiền hoàn khác đơn vị tiền thanh toán.")
            .put(ORIG_TRANS_FAIL_OR_REFUNED, "Giao dịch gốc thất bại hoặc đã được hoàn.")
            .put(REFUND_ERROR, "Hoàn tiền thất bại.")
            .put(EXCEPTION, "Lỗi hệ thống.")
            .put(WRONG_PARTNER_INFO, "Sai thông tin đối tác.")
            .put(WRONG_AMOUNT, "Số tiền không hợp lệ.")
            .put(AMOUNT_NOT_IN_RANGE_VALID, "Số tiền giao dịch không nằm trong khoảng quy định.")
            .put(EMPRY_SIGN_PUBKEY, "Lỗi hệ thống.")
            .put(CANNOT_FIND_TRANSID, "Không tìm thấy giao dịch.")
            .put(TRANS_FAIL, "Giao dịch thất bại.")
            .put(NOT_AVAILABLE_BALANCE, "Số dư tài khoản không đủ.")
            .put(ACC_STATUS_INVALID, "Trạng thái tài khoản không hợp lệ.")
            .put(ACC_CANNOT_DETERMINE, "Không xác định được số tài khoản.")
            .put(REQUEST_MESSAGE_ERROR, "Giao dịch thất bại.")
            .put(INIT_TRANS_ERROR, "Giao dịch thất bại.")
            .put(BRANCH_CANNOT_DETERMINE, "Không xác định được chi nhánh ứng với TK.")
            .put(VERIFY_OTP_FAIL, "Xác thực OTP thất bại.")
            .put(TRANS_DATE_NOT_DETERMINE, "Ngày giao dịch không xác định.")
            .put(TRANS_PAID_BEFORE, "Giao dịch được được thanh toán trước đó.")
            .put(MOBILE_SMS_CANNOT_DETERMINE, "Không xác định được số điện thoại mặc định nhận OTP.")
            .put(CURRENCY_ACC_CANNOT_PAY, "Loại ngoại tệ ứng với tài khoản không được chấp nhận thanh toán.")
            .put(TRANS_NOT_PAY, "Giao dịch chưa được thanh toán.")
            .put(PAY_FAIL, "Thanh toán không thành công.")
            .put(EXCEPTION_2, "Lỗi hệ thống.")
            .put(WRONG_TRANS_ID_2, "Mã giao dịch không chính xác.")
            .put(TRANS_VALID, "Giao dịch hợp lệ.")
            .put(SYSTEM_MAINTAIN, "Hệ thống VCB đang bảo trì dịch vụ.")
            .put(CARD_LOCKED_OR_INVALID, "Thẻ đang bị khóa hoặc trạng thái thẻ không hợp lệ.")
            .put(CARD_INFO_WRONG, "Thông tin thẻ không đúng.")
            .put(ACCEPT_VCB_CONNECT_24_ONLY, "Chỉ chấp nhận thẻ Vietcombank Connect24.")
            .put(DONT_HAVE_IB_ACC, "Tài khoản chưa đăng ký Internet Banking,")
            .put(WRONG_MERCHANT_ID, "Sai mã merchant.")
            .put(WRONG_VALID_AMOUNT_PAYMENT, "Số tiền thanh toán không hợp lệ.")
            .put(WRONG_CURRENCY, "Đơn vị tiền tệ không chính xác.")
            .put(DECRYPT_ERROR, "Lỗi giải mã.")
            .put(VERIFY_SIGNATURE_ERROR, "Lỗi xác thực chữ ký số.")
            .put(WRONG_SIGNATURE, "Chữ ký số sai.")
            .put(VERIFY_SIGNATURE_EXCEPTION, "Lỗi xác thực chữ ký số.")
            .put(UNKNOWN_ERROR, "Lỗi không xác định.")
            .put(INVALID_OTP, "OTP không chính xác.")
            .put(SIGNUP_SMS, "Chưa đăng ký SMS")
            .put(CANNOT_SEND_OTP, "Không thể lấy, gửi OTP.")
            .build();

    public static String getResMsg(String errorCode) {
        if (MAP_ERROR.containsKey(errorCode)) {
            return errorCode + ": " + MAP_ERROR.get(errorCode);
        } else {
            return "Giao dịch không thành công. Vui lòng liên hệ ngân hàng để biết thêm chi tiết.";
        }
    }

}
