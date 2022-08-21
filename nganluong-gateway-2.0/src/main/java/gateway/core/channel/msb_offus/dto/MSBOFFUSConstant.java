package gateway.core.channel.msb_offus.dto;

import com.google.common.collect.ImmutableMap;

public class MSBOFFUSConstant {

    public static final String CHANNEL_CODE = "MSB_ECOM_OFFUS";
    public static final String SERVICE_NAME = "MSB ECOM";
    public static final String METHOD = "POST";

    public static final String TRANSACTION_TYPE = "ECOM";

    public static final String MERCHANT_ID = "011000000869650";
    public static final String TERMINAL_ID = "R0000003";
    public static final String ACCESS_CODE = "8f28bec73d13e7c16ddf79924d6c5c09";

    // Access token
    public static final String ACCESS_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJ1c2VyX25hbWUiOiJuZ2FubHVvbmdfbWVyY2hhbnQiLCJpYXQiOjE2MTY5ODg4NzYsImF1dGhvcml0aWVzIjpbIkNSRUFURV9SRVZFUlNBTF9SRVFVRVNUIiwiRVhQT1JUX1JFVkVSU0FMX1JFUVVFU1QiLCJFWFBPUlRfVFJBTlNBQ1RJT04iLCJHRVRfVElEX01JRF9PRl9VU0VSIiwiU0VBUkNIX1JFVkVSU0FMX1JFUVVFU1QiLCJTRUFSQ0hfVFJBTlNBQ1RJT04iLCJWSUVXX1JFVkVSU0FMX1JFUVVFU1RfREVUQUlMIiwiVklFV19UUkFOU0FDVElPTl9ERVRBSUwiXX0.Ljm2dtr17LOW_Gsqh4PmJT6Zf8px3Q73dyu13AGwqpzoa5rK6I19CQjq5xCkxDgMA8DXQg4uSIGXSQ1oSL35vA";

    public static final String FUNCTION_CREATE_ORDER = "MSB_OFFUS_CREATE_ORDER";
    public static final String FUNCTION_CREATE_ORDER_ECOM = "MSB_OFFUS_CREATE_ORDER_ECOM";
    public static final String FUNCTION_GET_TRANSACTION = "MSB_OFFUS_GET_TRANSACTION";
    public static final String FUNCTION_GET_TRANSACTION_NL = "MSB_OFFUS_GET_TRANSACTION_NL";
    public static final String FUNCTION_CREATE_REFUND_TRANSACTION = "MSB_OFFUS_CREATE_REFUND_TRANSACTION";
    public static final String FUNCTION_GET_TRANSACTION_REFUND = "MSB_OFFUS_GET_TRANSACTION_REFUND";
    public static final String FUNCTION_NOTIFICATION_ORDER = "MSB_OFFUS_NOTIFICATION";
    public static final String FUNCTION_UPDATE_TRANSACTION = "MSB_OFFUS_UPDATE_TRANSACTION";

    public static final String MSB_OFFUS_CREATE_ORDER_URL = "https://apigateway.msb.com.vn/msbpay_create_bill/1.0.0";
    public static final String MSB_OFFUS_CREATE_ORDER_ECOM_URL = "https://apigateway.msb.com.vn/msbpay_create_ecom/1.0.0";
    public static final String MSB_OFFUS_GET_TRANSACTION_URL = "https://apigateway.msb.com.vn/msbpay_inquiry_bill/1.0.0";
    public static final String NL_NOTIFICATION_MSB_OFFUS = "https://www.nganluong.vn/api/msbEcomOffusNotify/index";

    public static final String USER_PASS_NGAN_LUONG = "adminNL:admin@NL2019";
    
    public static final String MSB_OFFUS_CREAT_REFFUND = "https://apigateway.msb.com.vn/mms_ecom_create_refund/1.0.0";
    public static final String MSB_OFFUS_GET_TRANSACTION_REFFUND = "https://apigateway.msb.com.vn/mms_inquiry_refund/1.0.0";

    static final ImmutableMap<Integer, String> ERROR_MESSAGE_VI = ImmutableMap.<Integer, String>builder()
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
            .put(400, "Lỗi không xác định")
            .put(500, "Internal errors")
            .build();
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
        if (ERROR_MESSAGE_VI.containsKey(errorCode)) {
            return ERROR_MESSAGE_VI.get(errorCode);
        } else {
            return errorCode + " Lỗi không xác định";
        }
    }
}
