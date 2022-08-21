package gateway.core.channel.msb_ecom.dto;

import com.google.common.collect.ImmutableMap;

public class MSBEcomConstant {

    public static final String CHANNEL_CODE = "MSB_ECOM";
    public static final String FUNCTION_CODE_CREATE_PAYMENT = "MSB_ECOM_CREATE_PAYMENT";
    public static final String FUNCTION_CODE_VERIFY_TRANSACTION = "MSB_ECOM_VERIFY_TRANSACTION";
    public static final String FUNCTION_CODE_RESEND_OTP = "MSB_ECOM_RESEND_OTP";
    public static final String FUNCTION_CODE_INQUIRY_TRANSACTION = "MSB_ECOM_INQUIRY_TRANSACTION";


    public static final String CCY = "VND";
    public static final String PAYMENT_TYPE = "ATM";

    // public static final String ACCESS_CODE = "123456789";
    // public static final String MERCHANT_ID = "011000000000002";
    public static final String METHOD_POST = "POST";

    public static final String MSB_ECOM_CREATE_PAYMENT_URL = "http://103.89.122.10:8080/v1/api/msb/acq-hub/ecom-process/create-payment";
    public static final String MSB_ECOM_VERIFY_TRANSACTION_URL = "http://103.89.122.10:8080/v1/api/msb/acq-hub/ecom-process/verify-transaction";
    public static final String MSB_ECOM_RESEND_OTP_URL = "http://103.89.122.10:8080/v1/api/msb/acq-hub/ecom-process/resend-otp";
    public static final String MSB_ECOM_INQUIRY_TRANSACTION_URL = "http://103.89.122.10:8080/v1/api/msb/acq-hub/ecom-process/inquiry-transaction";

    public static final int API_RESPONSE_STATUS_CODE_SUCCESS = 0;
    static final ImmutableMap<Integer, String> ERROR_MESSAGE_VI = ImmutableMap.<Integer, String>builder()
            .put(0, "Giao dịch thành công")
            .put(1, "Giao dịch không thành công, Ngân hàng từ chối giao dịch")
            .put(3, "Giao dịch không thành công, Mã đơn vị không tồn tại")
            .put(4, "Giao dịch không thành công, Không đúng access code")
            .put(5, "Giao dịch không thành công, Số tiền không hợp lệ")
            .put(6, "Giao dịch không thành công, Mã tiền tệ không tồn tại")
            .put(7, "Giao dịch không thành công, Lỗi không xác định")
            .put(11, "Thông tin thẻ không hợp lệ")
            .put(12, "Giao dịch không thành công, Thẻ chưa đăng ký sử dụng dịch vụ")
            .put(13, "Giao dịch không thành công, Ngày phát hành/Hết hạn không đúng")
            .put(15, "Giao dịch không thành công, Vượt quá hạn mức thanh toán")
            .put(51, "Giao dịch không thành công, Số tiền không đủ để thanh toán")
            .put(99, "Giao dịch không thành công, Người sử dụng hủy giao dịch")
            .build();
    static final ImmutableMap<Integer, String> ERROR_MESSAGE_EN = ImmutableMap.<Integer, String>builder()
            .put(0, "Approved ")
            .put(1, "Bank Declined Transaction")
            .put(3, "Merchant is not exist")
            .put(4, "Invalid access code")
            .put(5, "Invalid amount")
            .put(6, "Invalid currency code")
            .put(7, "Unspecified Failure")
            .put(11, "Card Not Registed Service(internet banking)")
            .put(12, "Invalid card date")
            .put(13, "Exist Amount")
            .put(15, "Invalid Card Info")
            .put(51, "Insufficient fund")
            .put(99, "User cancel transaction")
            .build();

    public static String getErrorMessage(int errorCode) {
        if (ERROR_MESSAGE_VI.containsKey(errorCode)) {
            return ERROR_MESSAGE_VI.get(errorCode);
        } else {
            return errorCode + " Lỗi không xác định";
        }
    }
}
