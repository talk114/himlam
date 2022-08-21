package gateway.core.channel.msb_onus.dto;

import com.google.common.collect.ImmutableMap;

public class MSBONUSConstant {
    public static final String CHANNEL_CODE = "MSB_ECOM_ONUS";
    public static final String SERVICE_NAME = "MSB ECOM";
    public static final String METHOD = "POST";

    public static final String TRANSACTION_TYPE = "ECOM";
    public static final String SERVICE_TYPE_TT = "TT";
    public static final String MERCHANT_ID_TT = "011000000869650";
    public static final String TERMINAL_ID_TT = "R0000003";

    public static final String SERVICE_TYPE_DT = "DT";
    public static final String MERCHANT_ID_DT = "011000000869650";
    public static final String TERMINAL_ID_DT = "R0000030";

    public static final String SERVICE_TYPE_HCC = "HCC";
    public static final String MERCHANT_ID_HCC = " 011000000869650";
    public static final String TERMINAL_ID_HCC = "R0000031";

    public static final String ACCESS_CODE = "8f28bec73d13e7c16ddf79924d6c5c09";
    public static final String FUNCTION_CREATE_TRANSACTION = "MSB_ONUS_CREATE_TRANSACTION";
    public static final String FUNCTION_VERIFY_TRANSACTION = "MSB_ONUS_VERIFY_TRANSACTION";
    public static final String FUNCTION_GET_TRANSACTION = "MSB_ONUS_GET_TRANSACTION";
    public static final String FUNCTION_NOTI_TRANSACTION_STATUS = "MSB_ONUS_NOTI_TRANSACTION_STATUS";

    static final ImmutableMap<Integer, String> ERROR_MESSAGE_EN = ImmutableMap.<Integer, String>builder()
            .put(0, "Success")
            .put(1, "Data input is not in format")
            .put(2, "Ip is denied")
            .put(3, "Bank code is not exit")
            .put(4, "Mobile is invalid")
            .put(5, "Post data fail")
            .put(6, "Pay date is not in format")
            .put(7, "Merchant is not exit")
            .put(8, "Merchant is not response")
            .put(9, "Service code is invalid")
            .put(10, "Service code is in active")
            .put(11, "Merchant is not active")
            .put(12, "Checksum failed")
            .put(18, "Invalid response code")
            .put(19, "Process error")
            .put(20, "Not found")
            .put(21, "Null transId")
            .put(22, "Null mId")
            .put(23, "Null amount")
            .put(24, "Null bill number")
            .put(25, "Null secure hash")
            .put(26, "Transaction not found or transaction expired")
            .put(27, "Null currency")
            .put(28, "Null tId")
            .put(40, "Ecom transaction is not paid successful")
            .put(41, "Ecom refund transaction create failed from processor")
            .put(99, "Internal errors")
            .put(400, "Type is mandatory")
            .put(500, "Internal errors")
            .build();

    public static String getErrorMessage(int errorCode) {
        if (ERROR_MESSAGE_EN.containsKey(errorCode)) {
            return ERROR_MESSAGE_EN.get(errorCode);
        } else {
            return errorCode + " Lỗi không xác định";
        }
    }
}
