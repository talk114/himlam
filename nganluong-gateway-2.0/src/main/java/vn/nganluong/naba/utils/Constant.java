package vn.nganluong.naba.utils;

import gateway.core.channel.cybersouce.enu.CardType;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author TaiND
 */
public class Constant {

    public static final Map<String, String> CARD_LABEL_CODE_MAP;
    public static final Map<String, String> ERROR_CODE_DESC_MAP;

    static {
        ERROR_CODE_DESC_MAP = new HashMap<String, String>() {
            {
                put("100", "Giao dịch thành công");
                put("101", "Thông tin giao dịch bị thiếu một hoặc nhiều trường dữ liệu bắt buộc");
                put("102", "Một hoặc nhiều trường thông tin trong giao dịch chứa dữ liệu không hợp lệ");
                put("110", "Một phần tiền trong số tiền thanh toán đã được xử lý thành công");
                put("150", "Lỗi hệ thống thanh toán, giao dịch chưa được xử lý");
                put("151", "Thông tin giao dịch đã được gửi tới Cổng thanh toán quốc tế, tuy nhiên giao dịch bị trễ do đường truyền");
                put("152", "Thông tin giao dịch đã được gửi tới Cổng thanh toán quốc tế, tuy nhiên giao dịch bị trễ do đường truyền và đang được xử lý");
                put("200", "Giao dịch bị từ chối do địa chỉ nhận hàng không khớp với địa chỉ chủ thẻ đã khai báo");
                put("201", "Giao dịch chờ xử lý do ngân hàng phát hành thẻ yêu cầu bạn phải trả lời một số câu hỏi");
                put("202", "Thẻ đã hết hạn sử dụng, vui lòng liên hệ ngân hàng phát hành thẻ để biết thêm chi tiết");
                put("203", "Giao dịch bị từ chối bởi ngân hàng phát hành thẻ");
                put("204", "Số dư tài khoản thẻ không đủ hoặc thẻ đã hết hạn mức thanh toán");
                put("205", "Thẻ bị từ chối giao dịch do chủ thẻ thông báo với ngân hàng phát hành là thẻ đã bị mất hoặc bị đánh cắp");
                put("207", "Hệ thống ngân hàng phát hành thẻ đang bị lỗi, không thể thực hiện được giao dịch");
                put("208", "Không kiểm tra được thẻ, có thể bạn chưa đăng ký chức năng giao dịch qua Internet, vui lòng liên hệ ngân hàng phát hành thẻ để trợ giúp");
                put("209", "Giao dịch bị từ chối thực hiện do Mã xác thực thẻ American Express (CID) không chính xác");
                put("210", "Thẻ hết hạn mức thanh toán");
                put("211", "Thông tin thẻ không chính xác");
                put("220", "Bộ vi xử lý từ chối yêu cầu dựa trên một vấn đề chung với tài khoản của khách hàng");
                put("221", "The customer matched an entry on the processor negative file");
                put("222", "Tài khoản thẻ đang bị đóng băng bởi ngân hàng phát hành");
                put("230", "Thông tin thẻ không chính xác");
                put("231", "Số thẻ không hợp lệ");
                put("232", "Loại thẻ không được chấp nhận bởi hệ thống thanh toán");
                put("233", "Hệ thống thanh toán thẻ quốc tế không chấp nhận xử lý giao dịch");
                put("234", "Có lỗi giữa hệ thống alepay.vn với hệ thống thanh toán thẻ quốc tế");
                put("235", "Yêu cầu xử lý giao dịch với số tiền lớn hơn số tiền khi kiểm tra thông tin thẻ");
                put("236", "Hệ thống xử lý thẻ quốc tế đang bị lỗi, không thể thực hiện được giao dịch");
                put("237", "Giao dịch đã được trả lại");
                put("238", "Tài khoản thẻ của khách hàng đã bị trừ tiền");
                put("239", "Số tiền trong yêu cầu xử lý sai khác với thông tin trong giao dịch trước đó");
                put("240", "Bạn chọn sai loại thẻ");
                put("241", "Request ID không chính xác");
                put("242", "Yêu cầu thanh toán đã được gửi nhưng không thể trừ được tiền");
                put("243", "Yêu cầu thanh toán đã được gửi thực hiện hoặc bị chuyển trả ở lần trước đó");
                put("247", "Yêu cầu thanh toán đã bị hủy");
                put("250", "Yêu cầu thanh toán bị trễ do đường truyền");
                put("475", "Chủ thẻ được ghi danh vào Xác thực mật khẩu thanh toán (3D-Secure). Vui lòng xác thực chủ thẻ trước khi tiếp tục giao dịch");
                put("476", "Xác thực mật khẩu thanh toán (3D-Secure) không thành công");
                put("480", "Thẻ đã bị trừ tiền, tuy nhiên do một số cảnh báo từ cổng quốc tế, Alepay sẽ kiểm tra thông tin trước khi hoàn tất giao dịch của bạn");
                put("481", "Thẻ đã bị trừ tiền, tuy nhiên do một số cảnh báo từ cổng quốc tế, Alepay sẽ kiểm tra thông tin trước khi hoàn tất giao dịch của bạn");

            }
        };

        CARD_LABEL_CODE_MAP = new HashMap<String, String>() {
            {
                put("VISA", "001");
                put("MASTERCARD", "002");
                put("AMERICAN EXPRESS", "003");
                put("Discover", "004");
                put("Diners Club", "005");
                put("Carte Blanche", "006");
                put("JCB", "007");
                put("EnRoute", "014");
                put("JAL", "021");
                put("Maestro (UK Domestic), Solo", "024");
                put("Delta", "031");
                put("Visa Electron", "033");
                put("Dankort", "034");
                put("Laser", "035");
                put("Carte Bleue", "036");
                put("Carta Si", "037");
                put("UATP", "040");
            }
        };

    }

    public static String getCardType(CardType cardType) {
        switch (cardType) {
            case VISA:
                return "001";
            case MASTERCARD:
                return "002";
            case AMERICAN_EXPRESS:
                return "003";
            case JCB:
                return "007";
            default:
                return "";
        }
    }

    public static final String PAYMENT_STATUS_FAILED = "Failed";
    public static final String PAYMENT_STATUS_PROCESSING = "Processing";
    public static final String PAYMENT_STATUS_OK = "Ok";
    public static final String PAYMENT_STATUS_MANUAL_PROCESSING_REQUIRED = "Manual processing required";
    public static final String AUTH = "AUTH";
    public static final String CAPTURE = "CAPTURE";
    public static final String REFUND = "REFUND";
    public static final String REVERSE_AUTH = "REVERSE_AUTH";
    public static final String AUTHORIZE_CARD = "AUTHORIZE_CARD";
    public static final String AUTHORIZE_SUBCRIPTION = "AUTHORIZE_SUBCRIPTION";
    public static final String VOID_AUTHORIZE_CARD = "VOID_AUTHORIZE_CARD";
    public static final String UPDATE_CARD_NUMBER = "UPDATE_CARD_NUMBER";
    public static final String CHECK_ENROLLMENT = "CHECK_ENROLLMENT";
    public static final String CONVERT_TRANSACTION = "CONVERT_TRANSACTION";
    public static final String TOKENIZATION_PAYMENT = "TOKENIZATION_PAYMENT";
    public static final String DELETE_TOKENIZATION = "DELETE_TOKENIZATION";
}
