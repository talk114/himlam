package gateway.core.channel.stb_ecom.dto;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * @author vinhnt
 *
 */

public class STBConstants {
	public static final String AccessKey = "orbjrmcrvfflpzkimvftxkzg";
	public static final String ProfileID = "NGANLUONGbzywi";
	public static final String ShareKey = "qswclfgzjaxjqhdgrgholroe";

	public static String Url_Checkout = "https://checkout.sacombank.com.vn/checkout/stbCheckout";

	public static String Url_Query = "https://checkout.sacombank.com.vn/checkout/stbCheckout/pay";

	public static String Url_location = "https://checkout.sacombank.com.vn/checkout/";


    public static final String FUNC_BALANCE_INQUIRY = "EMerchantBalanceInquiry";
	public static final String FUNC_REQUEST_OTP = "ERequestOTP";
	public static final String FUNC_REQUEST_OTP_ACC ="ERequestOTPAcc";
	public static final String FUNC_TOP_UP_BY_CARD = "ETopupByCard";
	public static final String FUNC_TOP_UP_BY_ACCOUNT = "ETopupByAccount";
	public static final String FUNC_PURCHASE_BY_CARD = "EPurchaseByCard";
	public static final String FUNC_PURCHASE_BY_ACCOUNT = "EPurchaseByAccount";
	public static final String FUNC_SUBSCRIPTION_INQUIRY = "ESubscriptionInquiry";
	public static final String FUNC_CASH_OUT_SUBSCRIPTION = "ECashoutSubscription";
	public static final String FUNC_CARD_INQUIRY = "ESTBCardInquiry";
	public static final String FUNC_TRANSFER_TO_STB_CARD = "EFundTransferToSTBCard";
	public static final String FUNC_ACCOUNT_INQUIRY = "ESTBAccountInquiry";
	public static final String FUNC_TRANSFER_TO_STB_ACCOUNT= "EFundTransferToSTBAccount";
	public static final String FUNC_CANCEL_SUBSCRIPTION = "ECancelSubscription";
	public static final String FUNC_DOMESTIC_INQUIRY = "EDomesticInquiry";
	public static final String FUNC_DOMESTIC_FUND_TRANSFER = "EDomesticFundTransfer";
	public static final String FUNC_DOMESTIC_ACCOUNT_INQUIRY = "EDomesticAccountInquiry";
	public static final String FUNC_DOMESTIC_ACCOUNT_FUND_TRANSFER = "EDomesticAccountFundTransfer";
	public static final String FUNC_VISA_INQUIRY = "EACNL";
	public static final String FUNC_VISA_TRANSFER = "EOCT";





	public static final String FUNC_LINK_CARD = "EcomSubscriptionCreation";
	public static final String FUNC_UNLINK_CARD = "EcomSubscriptionDelete";

	public static final String FUNC_PAY_WITH_TOKEN = "EcomSubscriptionAuthorization";
	public static final String FUNC_REVERSAL_PAY_WITH_TOKEN = "EcomSubscriptionAuthorizationReversal";

	public static final String FUNC_PAY_WITH_CARD = "EcomAuthorization";
	public static final String FUNC_REVERSAL_PAY_WITH_CARD = "EcomAuthorizationReversal";

	public static final String FUNC_CHECK_CARD = "SacombankNameInquiry";
	public static final String FUNC_TRANSFER_TO_CARD = "SacombankFundTransfer";
	public static final String FUNC_REVERSAL_TRANSFER_TO_CARD = "SacombankFundTransferReversal";

	public static final String FUNC_CHECK_CARD_NAPAS = "DomesticNameInquiry";
	public static final String FUNC_TRANSFER_TO_CARD_NAPAS = "DomesticFundTransfer";

	public static final String FUNC_CHECK_CARD_VISA = "EcomACNL";
	public static final String FUNC_TRANSFER_TO_CARD_VISA = "EcomOCT";

	public static final String FUNC_CHECK_BANK_ACC_STB = "SacombankAccountNameInquiry";
	public static final String FUNC_TRANSFER_BANK_ACC_STB = "SacombankAccountFundTransfer";

	public static final String FUNC_CHECK_BANK_ACC_IBFT = "DomesticAccountNameInquiry";
	public static final String FUNC_TRANSFER_BANK_ACC_IBFT = "DomesticAccountFundTransfer";

	public static final String FUNC_CARDLESS = "EcomCardless";
	public static final String FUNC_REVERSAL_CARDLESS = "EcomCardlessReversal";
	public static final String FUNC_CANCEL_CARDLESS = "EcomCardlessCancel";

	public static final String FUNC_QUERY_TRANSACTION = "EcomStatusInquiry";
	public static final String FUNC_QUERY_BALANCE = "EcomMerchantBalanceInquiry";

	public static final String FUNC_PREPAID_CREATE = "EcomPrepaidIssuing";
	public static final String FUNC_PREPAID_TOPUP = "EcomPrepaidTopup";
	public static final String FUNC_PREPAID_BALANCE = "EcomPrepaidBalanceInquiry";

	public static final String FUNC_QR_REGIS = "EcomWalletRegistration";
	public static final String FUNC_QR_PAYMENT = "EcomWalletQRPayment";

	public static final String FUNC_AUTH_SERVICE = "EcomAuthService";

	public static final String FUNC_GEN_QRCODE = "PartnerEMVQRGeneration";
	public static final String FUNC_QUERY_QRCODE_TRANS = "PartnerQRQueryTransaction";

	public static final String FUNCTION_CODE_REQUEST_OTP = "RequestOTP";
	public static final String FUNCTION_CODE_REQUEST_OTP_ACC = "Request0TPAcc";
	public static final String FUNCTION_CODE_TOP_UP_BY_CARD = "TopUpByCard";
	public static final String FUNCTION_CODE_TOP_UP_BY_ACCOUNT = "TopUpByAccount";
	public static final String FUNCTION_CODE_PURCHASE_BY_CARD = "PurchaseByCard";
	public static final String FUNCTION_CODE_PURCHASE_BY_ACCOUNT = "PurchaseByAccount";
	public static final String FUNCTION_CODE_SUBSCRIPTION_INQUIRY = "SubscriptionInquiry";
	public static final String FUNCTION_CODE_CASH_OUT_SUBSCRIPTION = "CashOutSubscription";
	public static final String FUNCTION_CODE_CARD_INQUIRY ="CardInquiry";
	public static final String FUNCTION_CODE_TRANSFER_TO_STB_CARD = "FundTransferToSTBCard";
	public static final String FUNCTION_CODE_ACCOUNT_INQUIRY = "AccountInquiry";
	public static final String FUNCTION_CODE_TRANSFER_TO_STB_ACCOUNT = "FundTransferToSTBAccount";
	public static final String FUNCTION_CODE_CANCEL_SUBSCRIPTION = "CancelSubscription";
	public static final String FUNCTION_CODE_DOMESTIC_INQUIRY = "DomesticInquiry";
	public static final String FUNCTION_CODE_DOMESTIC_FUND_TRANSFER = "DomesticFundTransfer";
	public static final String FUNCTION_CODE_DOMESTIC_ACCOUNT_INQUIRY = "DomesticAccountInquiry";
	public static final String FUNCTION_CODE_DOMESTIC_ACCOUNT_FUND_TRANSFER = "DomesticAccountFundTransfer";
	public static final String FUNCTION_CODE_VISA_INQUIRY = "ACNL";
	public static final String FUNCTION_CODE_VISA_TRANSFER = "OCT";
	public static final String FUNCTION_CODE_LINK_CARD = "SendOrder";
	public static final String FUNCTION_CODE_TRANSACTION_QUERY = "TransactionQuery";
	public static final String FUNCTION_CODE_BALANCE_INQUIRY = "MerchantBalanceInquiry";


	public static final String CHANNEL_CODE = "STB_ECOM";
	public static final String FUNCTION_CODE_PAYMENT = "Payment";
	public static final String FUNCTION_CODE_VERIFY_OTP_PAYMENT = "VerifyOtpPayment";
	public static final String FUNCTION_CODE_REVERSAL_PAYMENT = "ReversalPayment";
	public static final String FUNCTION_CODE_CHECK_CARD = "CheckCard";
	public static final String FUNCTION_CODE_TRANSFER_TO_CARD = "TransferToCard";
	public static final String FUNCTION_CODE_REVERSAL_TRANSFER_TO_CARD = "ReversalTransferToCard";
	public static final String FUNCTION_CODE_CHECK_CARD_IBFT = "CheckCardIBFT";
	public static final String FUNCTION_CODE_TRANSFER_TO_CARD_IBFT = "TransferToCardIBFT";
	public static final String FUNCTION_CODE_CHECK_VISA_MASTERCARD = "CheckVisaMasterCard";
	public static final String FUNCTION_CODE_TRANSFER_TO_VISA_MASTERCARD = "TransferToVisaMasterCard";
	public static final String FUNCTION_CODE_CHECK_BANK_ACC_STB = "CheckBankAccSTB";
	public static final String FUNCTION_CODE_TRANSFER_TO_BANK_ACC_STB = "TransferToBankAccSTB";
	public static final String FUNCTION_CODE_CHECK_BANK_ACC_IBFT = "CheckBankAccIBFT";
	public static final String FUNCTION_CODE_TRANSFER_TO_BANK_ACC_IBFT = "TransferToBankAccIBFT";
	public static final String FUNCTION_CODE_QUERY = "Query";
	public static final String FUNCTION_CODE_QUERYBALANCE = "QueryBalance";

    
	static final ImmutableMap<String, String> ERROR_MESSAGE = ImmutableMap.<String, String>builder()
			.put("00", "Giao dịch thành công")
			.put("01", "Giao dịch cần được kiểm tra tại Sacombank")
			.put("03", "Sai thông tin input")
			.put("05", "Giao dịch bị lỗi không xác định. Liên hệ Sacombank hoặc chờ đối soát")
			.put("08", "Xác thực OTP không thành công, OTP đã hết hạn")
			.put("10", "Yêu cầu nhập OTP")
			.put("12", "Giao dịch không hợp lệ")
			.put("13", "Số tiền không hợp lệ")
			.put("14", "Số thẻ không tồn tại")
			.put("18", "Thẻ không hỗ trợ")
			.put("20", "Sai OTP")
			.put("25", "Không tìm thấy giao dịch gốc để revert")
			.put("26", "Thẻ liên kết đã tồn tại")
			.put("30", "Sai format")
			.put("31", "Thẻ không được phép")
			.put("36", "Thẻ bị khóa")
			.put("46", "Thẻ chưa đăng ký dịch vụ giao dịch trực tuyến")
			.put("51", "Không đủ số dư thực hiện giao dịch")
			.put("54", "Thẻ hết hạn")
			.put("55", "Sai OTP")
			.put("57", "Thẻ không được phép sử dụng cho loại hình giao dịch")
			.put("61", "Giao dịch vượt hạn mức cho phép")
			.put("68", "Giao dịch quá thời gian xử lý")
			.put("75", "Sai OTP 3 lần")
			.put("91", "Lỗi hệ thống")
			.put("94", "Giao dịch trùng mã giao dịch")
			.put("ZZ", "Giao dịch gốc thất bại")
			.build();
	
	public static String getErrorMessage(String errorCode){
		if(ERROR_MESSAGE.containsKey(errorCode))
			return ERROR_MESSAGE.get(errorCode);
		else
			return "Lỗi không xác định";
	}
	
	static final ImmutableMap<String, String> ERROR_QRCODE_MESSAGE = ImmutableMap.<String, String>builder()
			.put("00", "Giao dịch thành công")
			.put("01", "Sai dữ liệu")
			.put("02", "User không tồn tại")
			.put("03", "Cập nhật thông tin thất bại")
			.put("04", "Không gửi được email")
			.put("05", "Lỗi exception")
			.put("06", "Không có quyền")
			.put("07", "User đã bị khóa")
			.put("08", "Sai email")
			.put("09", "Sai password")
			.put("10", "Password sai format")
			.put("11", "Không có dữ liệu")
			.put("12", "Sai số tiền")
			.put("13", "Không hỗ trợ")
			.put("68", "Yêu cầu bị timeout")
			.put("91", "Hệ thống không hoạt động")
			.put("94", "Giao dịch bị trùng")
			.put("99", "Lỗi phía server")
			.build();
	
	public static String getErrorQrcodeMessage(String errorCode){
		if(ERROR_QRCODE_MESSAGE.containsKey(errorCode))
			return ERROR_QRCODE_MESSAGE.get(errorCode);
		else
			return "Lỗi không xác định";
	}
}
