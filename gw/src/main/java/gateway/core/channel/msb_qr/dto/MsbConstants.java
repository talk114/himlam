package gateway.core.channel.msb_qr.dto;

public class MsbConstants {

    public static final String SERVICE_NAME_COMPLETE_CALLBACK = "MSB QRCODE";

    public static final String QR_TYPE_ORDER_ONLINE = "01";
    public static final String QR_TYPE_PRODUCT = "02";
    public static final String QR_TYPE_ORDER_OFFLINE = "03";
    public static final String QR_TYPE_BILLING = "04";

    public static final String CHANNEL_CODE = "MSB_QRCODE";
    public static final String FUNCTION_CODE_QR_CODE_PAYMENT = "QrCodePayment";
    public static final String FUNCTION_CODE_CHECK_QR_ORDER = "CheckQrOrder";
    public static final String FUNCTION_CODE_COMPLETE_QR_PAYMENT = "CompleteNLQrPayment";

    public static final String CREATE_QRCODE_URL_SUFFIX = "/createQrcode";//http://103.89.122.10:8080/v1/api/msb/acq-hub/qr-process/createQrcode
    public static final String CHECK_PAYMENT_URL_SUFFIX = "/inquiryQrCode";//http://103.89.122.10:8080/v1/api/msb/acq-hub/qr-process/inquiryQrCode

    public static final String API_RESPONSE_STATUS_CODE_SUCCESS = "00";
}
