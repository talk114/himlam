package gateway.core.channel.vccb_va.dto;

import java.io.File;

/**
 * @author dungla
 */
public final class VCCBVAConfig {

    //Find url in database
    public static final String VCCB_URL = "https://wso2uat.vietcapitalbank.com.vn:8243/va-services/1.0";
    public static final String PARTNER_CODE = "NGLG";
    public static final String TOKEN_BEAR = "Bear 55241b15-b6ef-388d-b3f8-c9d802e56111";
    public static final String PUBLIC_KEY_PATH = "vccb_key" + File.separator + "BVB_VIMO_SIT_2048_pub.pem";
    public static final String CHANNEL_CODE = "VCCB";
    public static final String SERVICE_NAME = "Virtual Account";

    //Channel Fuction
    public static final String FUNCTION_CODE_CALLBACK = "VCCB_VA_CALLBACK";
    public static final String FUNCTION_CODE_CREATE_VIRTUAL_ACCOUNT = "VCCB_VA_CREATE";
    public static final String FUNCTION_CODE_UPDATE_VIRTUAL_ACCOUNT = "VCCB_VA_UPDATE";
    public static final String FUNCTION_CODE_CLOSE_VIRTUAL_ACCOUNT = "VCCB_VA_CLOSE";
    public static final String FUNCTION_CODE_REOPEN_VIRTUAL_ACCOUNT = "VCCB_VA_REOPEN";
    public static final String FUNCTION_CODE_DETAIL_VIRTUAL_ACCOUNT = "VCCB_VA_DETAIL";
    public static final String FUNCTION_CODE_GET_LIST_VIRTUAL_ACCOUNT = "VCCB_VA_GET_LIST";
    public static final String FUNCTION_CODE_TRACE_VA_PAYMENT = "VCCB_VA_TRACE";
    private VCCBVAConfig(){}

//    Account
//    A001 Account not found Không tìm thấy tài khoản
//    A002 Account was closed Tài khoản đã đóng
//    A003 Account was frozen Tài khoản đang đóng băng
//    A004 Account was exist Tài khoản đã tồn tại
//    System
//    ST001 Status invalid Trạng thái không phù hợp
//    PR001 Invalid param request Lỗi dữ liệu đầu vào
//    SY001 System Internal Error Lỗi hệ thống
}
