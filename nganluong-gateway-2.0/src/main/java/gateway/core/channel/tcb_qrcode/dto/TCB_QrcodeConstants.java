package gateway.core.channel.tcb_qrcode.dto;

public class TCB_QrcodeConstants {

    public static final String CHANNEL_CODE = "TCB_QRCODE";


    public static final String FUNCTION_CODE_GET_QR_BANK_CODE = "GetQrBankCode";
    public static final String FUNCTION_CODE_CREATE_QR_CODE = "CreateQrCode";
    public static final String FUNCTION_CODE_CHECK_PAYMENT = "CheckPayment";


    public static final String GET_QR_BANK_CODE_ACTION = "getQrBankCode";
    public static final String CREATE_QR_CODE_ACTION = "createQRCode";
    public static final String CHECK_PAYMENT_ACTION = "checkPayment";


    public static final long   MerchantID = 1627713L;
    public static final String AppID = "1627713FB3Ju5nb";
    public static final String Username = "PayOnSDK";
    public static final String Password = "1393748dcdfa24c734b5b404131f5c65";
    public static final String Secret_key = "9mfEsVyFXwRX2s22D3LEfB";
//    public static final String Secret_key = "9d5b2dfde02abb9a49fc2bba7c26862ac44314f732ac97532f5055d877c49d68";

    public static final String Url_CallWeb = "https://www.nganluong.vn/api/PayonTcbQrcode/index";

    public static final String Service_type_code = "PAYNOW";
    public static final String Service_code = "PAYNOW_QRLOCALBANK_DYNAMIC";
    public static final String Method_code = "LOCALBANK";



    public static final String Url_GetQrBankCode = "https://sdk.payon.vn/v1/merchant/getQrBankCode";
    public static final String Url_CreateQrCode = "https://sdk.payon.vn/v1/merchant/createQRCode";
    public static final String Url_CheckPayment = "https://sdk.payon.vn/v1/merchant/checkPayment";

    public static final String Url_Notify = "https://gateway05.nganluong.vn/gateway/restful/api/partner/tcb_qrcode/notify";







}
