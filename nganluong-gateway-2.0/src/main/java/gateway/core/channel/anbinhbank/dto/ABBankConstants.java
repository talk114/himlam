package gateway.core.channel.anbinhbank.dto;

import com.google.common.collect.ImmutableMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ABBankConstants {

	//config LOG
	private static final Logger logger = LogManager.getLogger(ABBankConstants.class);
	protected static final String COMMENT_LOG_BEGIN = "############################ ";
	protected static final String COMMENT_LOG_END = " ############################";
	protected static final String CHARACTER = "    |    ";

	public static final String HEADER_UUID = "x-abb-uuid";
	public static final String HEADER_SIGNATURE = "x-abb-signature";
	public static final String HEADER_TOKEN = "Authorization";

	public static int COUNT_INDEX = 1;
	public static String NGL_DOISOAT_URL = "";

	public static final String GET_TOKEN_URL_SUFFIX     = "/v1/confidential/oauth2/token";//both NL&Vimo
	
	public static final String LINK_ACC_URL_SUFFIX     = "/v1/wallets/link";
	public static final String UNLINK_ACC_URL_SUFFIX     = "/v1/wallets/unlink";
	public static final String DEPOSIT_URL_SUFFIX     = "/v1/wallets/bank-wallet";
	public static final String WITHDRAW_URL_SUFFIX     = "/v1/wallets/wallet-bank";
	public static final String VERIFY_OTP_URL_SUFFIX     = "/v1/verifications/sms-otp";//NL
	
	public static final String CHECK_TRANS_URL_SUFFIX     = "/v1/wallets/check-status";
	public static final String GET_BALANCE_URL_SUFFIX     = "/v1/wallets/balance";
	
	public static final String PAY_WITH_BANK_ACC     = "/v1/payments/account";//NL
	public static final String CHECK_TRANS_PAY_URL_SUFFIX     = "/v1/payments/check-status";//NL
	public static final String GET_BALANCE_PAY_URL_SUFFIX     = "/v1/payments/balance";//NL

	public static final String CHANNEL_CODE = "AN_BINH_BANK_NGL";
	public static final String FUNCTION_CODE_VERIFY_OTP = "VerifyOtp";
	public static final String FUNCTION_CODE_GET_BALANCE = "GetBalance";
	public static final String FUNCTION_CODE_CHECK_TRANS_STATUS = "CheckTransStatus";
	public static final String FUNCTION_CODE_DOI_SOAT = "DoiSoat";
	public static final String FUNCTION_CODE_DOI_SOAT_GW = "DoiSoatGW";
	public static final String FUNCTION_CODE_PAYMENT_WITH_BANK_ACC = "PaymentWithBankAcc";

	
	public static final ImmutableMap<Integer, String> MAP_HTTP_STT = ImmutableMap.<Integer, String>builder()
			.put(200, "00")
			.put(201, "10")
			.build();
	
	static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
			.put("00", "Thành công")
			.put("01", "Lỗi hệ thống")
			.put("04", "Tham số không hợp lệ.")
			.put("05", "Sai dữ liệu mã hóa")
			.put("10", "Giao dịch yêu cầu OTP để xác thực")
			.put("11", "Mã OTP không đúng. Quý khách vui lòng kiểm tra và thực hiện lại")
			.put("12", "OTP bị hết hạn. Quý khách vui lòng thực hiện lại giao dịch.")
			.put("13", "Lỗi khi gửi OTP cho Khách hàng")
			.put("14", "OTP sai quá 3 lần. Quý khách vui lòng thực hiện lại giao dịch.")
			.put("30", "Trùng mã giao dịch")
			.put("53", "Tài khoản không đủ số dư")
			.put("54", "Vượt hạn mức của Sim chủ")
			.put("70", "Tài khoản khách hàng bị tạm giữ/khóa")
			.put("71", "Tài khoản của quý khách đã không hoạt động trên 12 tháng. Quý khách vui lòng kiểm tra thông tin, kích hoạt tài khoản và thực hiện lại giao dịch")
			.put("72", "Tài khoản khách hàng không tồn tại")
			.put("73", "Thông tin số tài khoản không chính xác. Quý khách vui lòng kiểm tra và thực hiện lại.")
			.put("75", "Thất bại do Quý khách chưa đăng ký dịch vụ SMS Banking với Ngân hàng.")
			.put("76", "Số điện thoại ví Vimo phải trùng với số điện thoại đăng ký SMS Banking với ngân hàng.")
			.put("77", "Số cmnd không đúng của KH")
			.put("80", "Liên kết không tồn tại")
			.put("82", "Tài khoản đã được liên kết")
			.put("99", "Dữ liệu không hợp lệ hoặc lỗi hệ thống")
			.put("ZZ", "Không lấy được access token")
			.build();
	
	public static String getErrorMessage(String errorCode){
		if(ERROR_MESSAGE.containsKey(errorCode))
			return ERROR_MESSAGE.get(errorCode);
		else
			return errorCode + " Lỗi không xác định";
	}

	//Get LOG
	public static void WriteInfoLog(String title, String key, Object content) {
		logger.info(COMMENT_LOG_BEGIN + title + COMMENT_LOG_END + "\n" + key + CHARACTER + content + "\n");
	}
}
