package gateway.core.channel.bidv_ecom.dto;

import com.google.common.collect.ImmutableMap;

/**
 * @author sonln@nganluong.vn
 */
public class BidvEcomConstants {
    public static final String CHANNEL_CODE = "BIDV_ECOM";
    public static final String SERVICE = "ECOM";
    //KEY_ENCRYPT sandbox
//    public static final String KEY_ENCRYPT = "bidv-nganluong-abc123";
    //KEY_ENCRYPT live
    public static final String KEY_ENCRYPT = "bidv-nganluong-6688";
    public static final String MERCHANT_ID = "019001";
    public static final String MERCHANT_NAME = "NganLuong";
    public static final String SERVICE_ID = "019001";
    public static final String CURR = "VND";

    public static final String FNC_INIT_TRANS = "INIT_TRANS";
    public static final String FNC_VERIFY = "VERIFY";
    public static final String FNC_INQUIRY = "INQUIRY";
    public static final String FNC_BIDV_NOTIFY = "NOTIFY";

//    public static final String NL_URL_REDIRECT = "https://sandbox.nganluong.vn:8088/nl35/bidvnt_return.php";
//    public static final String NL_URL_REDIRECT = "https://uat.nganluong.vn/bidvecom_return.php";
    public static final String NL_URL_REDIRECT = "https://www.nganluong.vn/bidvecom_return.php";

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

    public static String getErrorMessage(String errorCode) {
        return ERROR_MESSAGE.getOrDefault(errorCode, "Lỗi không xác định");
    }

}
