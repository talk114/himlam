package gateway.core.channel.ocb.dto;

public class OCBConstants {
    public static final String CHANNEL_CODE = "OCB";
    public static final String SERVICE_NAME = "PAYMENT";
    public static final Integer OCB_CHANNEL_ID = 17;
    public static final String PARTNER_CODE = "NGANLUONGGATE";

    /* SANDBOX */
//    public static final String GET_TOKEN_URL = "https://sandbox.api.ocb.com.vn/corporates/developer/ocb-oauth-provider/oauth2/token";
//    public static final String CLIENT_ID = "0f25a0aaa64d08bc297d567491d0f362";
//    public static final String CLIENT_SECRET = "9cf76182ced6852248c4ef428eb98740";
//    public static final String GRANT_TYPE = "password";
//    public static final String SCOPE = "OCB";
//    public static final String USERNAME = "jka3375";
//    public static final String PASSWORD = "hpp4483";

    /* LIVE */
    public static final String GET_TOKEN_URL = "https://api.ocb.com.vn/corporates/partner/ocb-oauth-provider/oauth2/token";
    public static final String CLIENT_ID = "3b35f789b851445147a48646023757bc";
    public static final String CLIENT_SECRET = "945b817f0916131c932907f663957a81";
    public static final String GRANT_TYPE = "password";
    public static final String SCOPE = "OCB";
    public static final String USERNAME = "ahv0467";
    public static final String PASSWORD = "yc5MBV.r";



    public static final String CONTENT_TYPE = "application/json";
    public static final String ACCEPT = "application/json";

    public static final String GET_JWT = "GET_JWT";
    /**
     * All OCB function
     */
    public static final String FUNCTION_PAYMENT_STEP_1 = "OCB_PAYMENT_STEP_1";
    public static final String FUNCTION_PAYMENT_STEP_2 = "OCB_PAYMENT_STEP_2" ;
    public static final String FUNCTION_PAYMENT_STATUS = "OCB_PAYMENT_STATUS";

    public static final String FUNCTION_RESEND_OTP = "OCB_RESEND_OTP";
    public static final String FUNCTION_ATM_LOCATION = "OCB_INQUIRY_ATM_LOCATION";
    public static final String FUNCTION_BANK_LOCATION = "OCB_INQUIRY_BRANCH_LOCATION";

    public static final String FUNCTION_TRANSACTION_HISTORY = "OCB_TRANSACTION_HISTORY";

}
