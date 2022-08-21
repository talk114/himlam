package gateway.core.channel.bidv.dto;


import com.google.common.collect.ImmutableMap;

public class BIDVConstants {

    public static final String ERROR_CODE_SUCCESS = "000";
    public static final String PROVIDER_ID = "019";

    static final ImmutableMap<String, String> CHANNEL_MAP = ImmutableMap.<String, String>builder()
            .put("mobile", "211701").put("web", "211601").put("sms", "211901").build();

    static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
            .put("000", "Giao dịch thành công")
            .put("001", "Hóa đơn đã được thanh toán trước đó")
            .put("002", "Hóa đơn đã hết hạn thanh toán")
            .put("003", "Không tìm thấy thông tin khách hàng")
            .put("004", "Quá thời gian chờ xác thực OTP")
            .put("005", "Sai mã xác thực OTP")
            .put("006", "Dịch vụ không tồn tại")
            .put("007", "Sai chữ ký")
            .put("008", "Từ chối truy cập")
            .put("009", "Chưa định nghĩa client")
            .put("010", "Sai User và Password đăng nhập")

            .put("011", "Trùng dữ liệu hoặc trùng trans_id")
            .put("012", "Dịch vụ chưa đăng ký")
            .put("013", "Thông tin khách hàng đã tồn tại")
            .put("014", "Tài khoản không được phép thanh toán")
            .put("015", "NCC không thực hiện đảo giao dịch")
            .put("016", "NCC thực hiện đảo giao dịch bị lỗi")
            .put("017", "Tên đăng nhập BIDV Online (CIF) không tồn tại")
            .put("018", "Không tìm thấy thông tin giao dịch")
            .put("019", "Nhà cung cấp không đúng")
            .put("020", "Dịch vụ không đúng")

            .put("025", "Khách hàng không có hóa đơn")
            .put("026", "Số tiền thanh toán khác số tiền nợ")
            .put("027", "Số tiền giao dịch vượt quá hạn mức tối đa")
            .put("028", "Tổng số tiền giao dịch vượt quá hạn mức tối đa trong ngày")
            .put("029", "Tổng số lần giao dịch vượt quá hạn mức tối đa trong ngày")
            .put("030", "Mã CN bên nhà cung cấp chưa khai báo")
            .put("031", "Thông tin thẻ ghi nợ không tồn tại")
            .put("032", "Thẻ ghi nợ không hoạt động")
            .put("033", "Số tài khoản không tồn tại")
            .put("034", "TK KH bị tạm giữ")
            .put("035", "Tài khoản không đủ số dư để thanh toán")
            .put("036", "Tài khoản không hoạt động")
            .put("037", "Trạng thái tài khoản BIDV Online không hợp lệ")
            .put("038", "Số tiền còn lại trong tài khoản không đủ số dư tối thiểu")
            .put("039", "Tài khoản KH tại NCC đang bị khóa")

            .put("040", "Tên khách hàng không đúng")
            .put("041", "Khách hàng chưa đăng ký dịch vụ BIDV Online (gói tài chính) tại BIDV")
            .put("042", "Khách hàng chưa đăng ký dịch vụ tại NCC")
            .put("043", "Dữ liệu NULL")
            .put("044", "Số tiền không hợp lệ")
            .put("045", "Số tiền âm")
            .put("046", "Thẻ ghi nợ không hoạt động")
            .put("047", "Thẻ ghi nợ không hoạt động")
            .put("048", "Thẻ ghi nợ không hoạt động")
            .put("049", "Số tiền giao dịch nhỏ hơn hạn mức tối thiểu")

            .put("050", "Giao dịch đang được xử lý")
            .put("051", "Invalid IssueDate")
            .put("052", "Cần nhập OTP cho giao dịch vượt hạn mức/ngày")
            .put("055", "Mật khẩu không đúng")
            .put("056", "Không gạch nợ được hóa đơn")
            .put("057", "User BIDV online phi tài chính")
            .put("060", "Gọi service lấy OTP bị lỗi hoặc khách hàng bị khóa OTP tạm thời do nhập sai 5 lần liên tiếp")
            .put("061", "Gọi service xác thực OTP bị lỗi")
            .put("062", "Không vấn tin được số dư tài khoản")
            .put("063", "Giao dịch không thực hiện được. Vui lòng thực hiện lại sau")
            .put("064", "Giao dịch timeout hạch toán, chờ xử lý")
            .put("065", "Quý khách chưa đăng ký số điện thoại nhận OTP để thanh toán giao dịch trực tuyến")
            .put("066", "Tài khoản không hợp lệ")

            .put("092", "Routing Error")
            .put("096", "Giao dịch không thực hiện được. Vui lòng thực hiện lại sau")
            .put("097", "Giao dịch không thực hiện được. Vui lòng thực hiện lại sau")
            .put("098", "Loại thẻ thanh toán không hợp lệ")
            .put("099", "Thông tin thanh toán bị thay đổi")
            .put("100", "Giao dịch timeout gạch nợ/timeout khi thông báo kết quả hạch toán cho NCC, chờ xử lý")
            .put("101", "Sai định dạng Message/Không định nghĩa Message")
            .put("102", "Không hỗ trợ loại tiền tệ này")
            .put("103", "Giao dịch không thực hiện được. Vui lòng thực hiện lại sau")
            .put("104", "Giao dịch không thực hiện được. Vui lòng thực hiện lại sau")
            .put("105", "Giao dịch bị hủy")
            .put("108", "Số CIF đã sử dụng đăng ký BUNO hoặc đang xử lý tại quầy")
            .put("109", "Mã ví đang được xử lý tại quầy BIDV")
            .put("110", "Mã ví đã được liên kết")
            .put("111", "Mã ví chưa liên kết")
            .put("112", "Cần nhập OTP cho giao dịch vượt hạn mức/lần")
            .put("113", "Số tài khoản đã được liên kết")
            .put("114", "Giao dịch không thực hiện được. Vui lòng thực hiện lại sau")
            .build();

    static final ImmutableMap<String, String> ERROR_MESSAGE_DS = ImmutableMap.<String, String>builder()
            .put("000", "Thành công")
            .put("001", "Không tìm thấy file theo request date")
            .put("002", "Sai định dạng message")
            .put("003", "Sai tên file")
            .put("004", "Sai chữ ký")
            .put("005", "Lỗi không xác định")
            .put("006", "File name đã tồn tại")
            .build();

    public static String getErrorMessage(String errorCode){
        if(ERROR_MESSAGE.containsKey(errorCode))
            return ERROR_MESSAGE.get(errorCode);
        else
            return "";
    }

    public static String getErrorMessageDS(String errorCode) {
        return ERROR_MESSAGE_DS.getOrDefault(errorCode, "Lỗi không xác định");
    }

    public static final ImmutableMap<String, String> VIMO_BIDV_GET_INFO = ImmutableMap.<String, String>builder()
            .put("00", "000")
            .put("01", "017")
            .put("02", "018")
            .build();

    public static final ImmutableMap<String, String> VIMO_BIDV_TOPUP = ImmutableMap.<String, String>builder()
            .put("00", "000")
            .put("01", "096")
            .build();

    public static final String CUSTOMER_ID = "f2";
    public static final String SERVICE_CODE = "f3";
    public static final String AMOUNT = "f4";
    public static final String REQUEST_TIME = "f7";
    public static final String AUDIT_NUMBER = "f11";			// Mã trace, unique trong ngày có thể dùng trong đối soát
    public static final String PAY_TIME = "f12";
    public static final String CHANNEL_CODE = "f22";			// Mã phân loại kênh giao dịch
    public static final String FUNCTION_CODE = "f24";			// Mã yêu cầu
    public static final String SENDER_CODE = "f32";				// Mã đơn vị gửi request
    public static final String AREA_CODE = "f34";			// Mã dịch vụ/Mã khu vực của NCC DV (Vimo, NL)
    public static final String RESPONSE_CODE = "f39";				// Mã trả lời. Do các bên thống nhất với nhau
    public static final String ACCEPTOR_TERMINAL_ID = "f41";	// Mã đầu cuối tại điểm giao dịch
    public static final String MORE_RESPONSE_DATA = "f44";		// Thông tin trả lời bổ sung
    public static final String CUSTOMER_INFO = "f48";			// Thông tin KH
    public static final String CURRENCY_CODE = "f49";			// Mã tiền tệ
    public static final String SECURITY_CODE = "f53";			// Thông tin phiên làm việc
    public static final String TRANSACTION_OLD_INFO = "f56";	// Thông tin giao dịch cũ (sd trong trường hợp hủy bỏ thanh toán)
    public static final String RESERVED_FOR_PRIVATE_USER = "f61";	// Trường dự phòng, sử dụng nếu phát sinh nghiệp vụ mới. Các bên thống nhất
    public static final String DESCRIPTION_ORDER = "f62";			// Thông tin chi tiết hóa đơn
    public static final String SECURE_CODE = "f63";					// Nội dung chữ ký số

    // Costant TOPUP
    public static final String GET_INFO_REQ_HEADER = "1100";
    public static final String GET_INFO_RES_HEADER = "1110";
    public static final String TOPUP_REQ_HEADER = "1200";
    public static final String TOPUP_RES_HEADER = "1210";

    public static final String CURRENCY_VND = "VND";
    public static final String CURRENCY_USD = "USD";

    public static final String CONFIRM_LINK_CARD = "ConfirmLinkCard";
    public static final String GET_INFO = "GetInfo";
    public static final String TOPUP = "Topup";
}
