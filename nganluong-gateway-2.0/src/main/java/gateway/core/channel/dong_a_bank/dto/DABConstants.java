package gateway.core.channel.dong_a_bank.dto;

import java.text.SimpleDateFormat;

public class DABConstants {

    public static final String CHANNEL_CODE = "DONG_A_BANK";
    public static final String FUNCTION_CODE_CREATE_ORDER = "CreateOrder";
    public static final String FUNCTION_CODE_UPDATE_ORDER = "UpdateOrderStatus";

    public static final String FUNCTION_CODE_CHECK_ORDER = "CheckOderStatus";
    public static final String FUNCTION_CODE_GET_BILL_INFO = "GetBillInfo";
    public static final String FUNCTION_CODE_GET_BILL_NL_INFO = "GetBillInfoNL";
    public static final String FUNCTION_CODE_GET_LIST_BILLING_SIZE = "GetListBillingSize";


    public static final SimpleDateFormat dfDateOrder = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat dfTimestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static final String ACCESS_KEY_HEADER = "accesskey";
    public static final String TIME_STAMP_HEADER = "timestamp";
    public static final String SIGNATURE_HEADER = "signature";

    public static final String CREATE_ORDER_ACTION = "sendOrderInformNewProcess";
    public static final String UPDATE_ORDER_ACTION = "updateNewStatus";
    public static final String CHECK_ORDER_ACTION = "checkStatusOrder";
    public static final String GET_BILL_INFO_ACTION = "getBillingInfo";
    public static final String GET_LIST_BILLING_SIZE_ACTION = "getListBillingSize";




    public static final String GET_BILL_INFO_NL_ACTION = "getBillingInfoByOFI";

    public static final int SUCCESS = 0;
    // Gọi phương thức, chứng thực, tham số hợp lệ thành công.

    public static final int CERTIFICATE_INVALIDE = -1;
    // Chứng thực không hợp lệ.

    public static final int HEADER_INVALIDE = -4;
    // Tham số truyền từ SOAP Header không hợp lệ.

    public static final int TIME_EXPIRED = -5;
    // Quá hạn thời gian 5’ gọi hàm API.

    public static final int TAG_HEADER_INVALIDE1 = -6;
    // Không tồn tại hoặc không hợp lệ thẻ tag SOAPHeader “securityHeader”

    public static final int TAG_HEADER_INVALIDE2 = -7;
    // Không tồn tại hoặc không hợp lệ thẻ các tag trong SOAPHeader
    // “securityHeader”.

    public static final int PARTNER_CODE_INVALIDE = -8;
    // Không tồn tại hoặc không hợp lệ PartnerCode với serviceType.

    public static final int SIGNATURE_INVALIDE = -9;
    // Không hợp lệ chứng thực chữ ký điện tử.

    public static final int METHOD_INVALIDE = -10;
    // Không tồn tại hoặc không hợp lệ phương thức cần gọi thực thi.

    public static final int SYSTEM_ERROR = -99;
    // Xảy ra lỗi hệ thống


    public static final String CREATE_ORDER_RESPONSE_CODE_SUCCESS = "0";
}
