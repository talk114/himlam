package vn.nganluong.naba.utils;

import java.io.Serializable;

public enum CybersourceError implements Serializable {

    SUCCESS("00", "Thành công"),
    CURRENCY_EMPTY("01", "Loại tiền tệ không được để trống"),
    CARD_INFO_EMPTY("02", "Thông tin thẻ không được để trống"),
    CUSTOMER_INFO_EMPTY("03", "Thông tin khách hàng không được để trống"),
    CARD_NUMBER_EMPTY("04", "Số thẻ không được để trống"),
    MONTH_EXPIRED_EMPTY("05", "Tháng hết hạn của thẻ không được để trống"),
    YEAR_EXPIRED_EMPTY("06", "Tháng hết hạn của thẻ không được để trống"),
    CARD_TYPE_EMPTY("07", "Loại thẻ không được để trống"),
    NAME_EMPTY("08", "Tên không được để trống"),
    FULLNAME_EMPTY("09", "Họ tên không được để trống"),
    EMAIL_EMPTY("10", "Email không được để trống"),
    CITY_EMPTY("11", "Thành phố không được để trống"),
    COUNTRY_EMPTY("12", "Quốc gia không được để trống"),
    PHONENUMBER_EMPTY("13", "Số điện thoại không được để trống"),
    STREET_EMPTY("14", "Đường phố không được để trống"),
    AUTHENTICATION_CODE("15", "Mã xác thực không được để trống"),
    AMOUNT_INVALID("16", "Số tiền thanh toán không hợp lệ"),
    CODE_LINKED_CARD_EMPTY("17", "Mã liên kết không để trống"),
    ERROR_SYSTEM("99", "Hệ thống thẻ quốc tế đang bảo trì. Bạn vui lòng quay lại sau ít phút nữa!"),
    ;
    
    private final String code;
    private final String message;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    private CybersourceError(String code, String message) {
        this.code = code;
        this.message = message;
    }
}