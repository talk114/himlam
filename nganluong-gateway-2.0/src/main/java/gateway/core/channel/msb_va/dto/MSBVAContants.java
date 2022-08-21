package gateway.core.channel.msb_va.dto;

import com.google.common.collect.ImmutableMap;

/**
 * @author sonln
 */
public class MSBVAContants {
    public static final String CHANNEL_CODE = "MSB_VA";
    public static final String SERVICE = "MSB_VA";
    public static final String MID = "011000000869650";
    public static final String TID = "R0000003";
    public static final String SERVICE_CODE = "968668666";
    public static final String VA_CODE = "968668666";
    public static final String ACCESS_CODE = "NGANLUONG@2021";


    public static final String FNC_LOGIN = "LOGIN";
    public static final String FNC_TRANSACTION_HISTORY = "TRANSACTION_HISTORY";
    public static final String FNC_CREATE_VA = "CREATE_VA";
    public static final String FNC_UPDATE_VA = "UPDATE_VA";
    public static final String FNC_MSB_VA_NOTIFY = "MSB_VA_NOTIFY";

    public static String getErrorMessage(String errCode) {
        return ERROR_MESSAGE.getOrDefault(errCode, "Lỗi không xác định");
    }

    static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
            .put("0", "Giao dịch thành công")
            .put("1", "Request body không đúng định dạng")
            .put("7", "Thông tin merchant không chính xác(tid, mid)")
            .put("12", "Mã secureHash gửi lên không đúng")
            .put("18", "Processor trả về không đúng định dạng")
            .put("22", "Gửi thiếu thông tin mid")
            .put("42", "Số tài khoản gắn với merchant không chính xác")
            .put("43", "Merchant chưa được cấu hình dịch vụ VA")
            .put("44", "Merchant chưa được cấu hình dịch vụ VA")
            .put("46", "Merchant không gửi thông tin khởi tạo VA")
            .put("48", "Thiếu thông tin về hạn mức khi khởi tạo VA")
            .put("49", "Số tiền ở hạn mức không phải số nguyên dương(")
            .put("50", "Số tiền min > số tiền max")
            .put("51", "VA code gửi lên không chính xác")
            .put("178", "Trùng với số Seq giao dịch đã tồn tại trong CORE MSB")
            .put("2000", "Thông tin đăng ký tài khoản không hợp lệ")
            .put("201", "Số VA null hoặc không đúng độ dài quy định")
            .put("202", "Số tài khoản không hợp lệ độ dài")
            .put("203", "Số tài khoản không hợp lệ độ dài đăng ký (19)")
            .put("204", "Mã đầu số của tài khoản không hợp lệ đầu số đăng ký")
            .put("205", "Số tài khoản không hợp lệ là số và chữ")
            .put("206", "Số VA bị trùng")
            .put("207", "Không tồn tại tài khoản VA")
            .put("208", "Reference number null hoặc không đúng độ dài quy định")
            .put("209", "Số tham chiếu tài khoản không hợp lệ độ dài")
            .put("210", "Tên VA null hoặc không đúng độ dài quy định")
            .put("211", "Thông tin Email chủ tài khoản không hợp lệ độ dài")
            .put("212", "Thông tin số Phone chủ tài khoản không hợp lệ độ dài")
            .put("213", "Thông tin Detail chủ tài khoản không hợp lệ độ dài")
            .put("214", "Tên thụ hưởng không hợp lệ độ dài")
            .put("215", "Thông tin gợi ý không hợp lệ độ dài")
            .put("216", "Định dạng date time không hợp lệ(Bắt buộc dd/MM/yyyy HH:mm:ss)")
            .put("217", "Pay Type tài khoản không hợp lệ")
            .put("218", "Số tiền không hợp lệ (số nguyên)")
            .put("219", "Số tiền không hợp lệ (<0)")
            .put("220", "Không đăng ký giới hạn giao dịch (min,max) với equal amount")
            .put("221", "Không hợp lệ giới hạn số tiền giao dịch")
            .put("222", "Tham số VaNumber không hợp lệ PathVariable")
            .put("223", "Status tài khoản không hợp lệ (not in rank)")
            .put("224", "Tham số không hợp lệ (null)")
            .put("225", "Tài khoản không hoạt động")
            .put("226", "Tài khoản hết hạn sử dụng")
            .put("227", "Thông tin MaxAmount không hợp lệ độ dài")
            .put("228", "Thông tin MinAmount không hợp lệ độ dài")
            .put("229", "Thông tin EqualAmount không hợp lệ độ dài")
            .put("230", "Thông tin gợi ý không hợp lệ ký tự")
            .put("231", "Thông tin ExpiryDateTime phải còn hạn dùng (Thời hạn trong tương lai)")
            .put("232", "Thông tin tên NSDDV không hợp lệ ký tự")
            .put("233", "Số tài khoản không hợp lệ")
            .put("400", "Thông tin log Va Transaction null")
            .put("401", "Token hết hạn")
            .put("403", "Token hết hạn")
            .put("504", "File vượt quá số lượng item!.")
            .put("236", "Sai ngày giao dịch trong CORE MSB")
            .put("999", "Time out")
            .put("E99", "Time out")
            .put("E01", "Trong bản tin có kí tự đặc biệt không parse được")
            .put("99", "Có lỗi phát sinh khi xử lý giao dịch")
            .build();

}
