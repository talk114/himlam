package gateway.core.channel.stb_ecom.service;


import gateway.core.channel.stb_ecom.STBEcom;
import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.PaymentAccount;

public interface STBEcomNglService {
	/**
	 * Giao dịch request OTP
	 */
	PGResponse RequestOTP(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * Giao dịch request OTP ACC
	 */
	PGResponse RequestOTPAcc(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * Giao dịch nạp tiền vào Ví với thẻ liên kết
	 */
	PGResponse TopUpByCard(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Giao dịch nạp tiền vào ví với tài khoản đã liên kết
	 */
	PGResponse TopUpByAccount(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Giao dịch thanh toán mua hàng bằng thẻ đã liên kết
	 */
	PGResponse PurchaseByCard(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Giao dịch thanh toán mua hàng bằng tài khoản đã liên kết
	 */
	PGResponse PurchaseByAccount(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 1 Giao dịch rút tiền từ ví về thẻ/tài khoản liên kết
	 */
	PGResponse SubscriptionInquiry(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 2 Giao dịch rút tiền từ ví về thẻ/tài khoản liên kết
	 */
	PGResponse CashOutSubscription(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 1 Giao dịch thực hiện chuyển tiền từ Ví đếm thẻ sacombank
	 */
	PGResponse CardInquiry(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 2 Giao dịch thực hiện chuyển tiền từ Ví đếm thẻ sacombank
	 */
	PGResponse  FundTransferToSTBCard(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 1 Giao dịch thực hiện chuyển tiền từ ví đến tài khoản sacombank
	 */
	PGResponse AccountInquiry(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 2 Giao dịch thực hiện chuyển tiền từ ví đến tài khoản sacombank
	 */
	PGResponse FundTransferToSTBAccount(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Giao dịch hủy liên kết thẻ/tài khoản
	 */
	PGResponse CancelSubscription(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 1 Giao dịch chuyển tiền đến thẻ nội địa Napas
	 */
	PGResponse DomesticInquiry(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 2 Giao dịch chuyển tiền đến thẻ nội địa Napas
	 */
	PGResponse DomesticFundTransfer(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 1 Giao dịch chuyển tiền đến taì khoản napas
	 */
	PGResponse DomesticAccountInquiry(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 2 Giao dịch chuyển tiền đến taì khoản napas
	 */
	PGResponse DomesticAccountFundTransfer(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 1 Giao dịch chuyển tiền đến thẻ Visa/Master
	 */
	PGResponse VisaInquiry(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * Step 2 Giao dịch chuyển tiền đến thẻ Visa/Master
	 */
	PGResponse VisaTransfer(PaymentAccount paymentAccount,String inputStr) throws Exception;






	/**
	 * Liên kết thẻ
	 */
	PGResponse LinkCard(PaymentAccount paymentAccount,String inputStr) throws Exception;

	/**
	 * <b>Notify thanh toán checkout</b>
	 *
	 */
	String NotifySTB_ECOM(String inputStr) throws Exception;


	/**
	 * <b>TransactionQuery</b>
	 *
	 */
	PGResponse TransactionQuery(PaymentAccount paymentAccount,String inputStr) throws Exception;



















	/**
	 * <b>Step 1 Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
	 */
	PGResponse Payment(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Step 2 Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
	 */
	PGResponse VerifyOtpPayment(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Đảo giao dịch Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
	 */
	PGResponse ReversalPayment(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Tra cứu thông tin thẻ nội địa, napas trước khi thực hiển Rút Ví ->
	 * Thẻ</b>
	 */
	PGResponse CheckCard(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Chuyển tiền vào thẻ STB nội địa (ko phải thẻ liên kết)</b>
	 */
	PGResponse TransferToCard(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Đảo giao dịch Chuyển tiền vào thẻ STB nội địa (ko phải thẻ liên
	 * kết)</b>
	 * 
	 */
	PGResponse ReversalTransferToCard(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Tra cứu thông tin thẻ iBFT</b>
	 */
	PGResponse CheckCardIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Chuyển tiền tới thẻ IBFT</b>
	 */
	PGResponse TransferToCardIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Tra cứu thẻ Visa Master</b>
	 * 
	 */
	PGResponse CheckVisaMasterCard(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Chuyển tiền vào thẻ Visa Master</b>
	 *
	 */
	PGResponse TransferToVisaMasterCard(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Tra cứu TKNH Sacombank</b>
	 * 
	 */
	PGResponse CheckBankAccSTB(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Chuyển tiền vào TKNH Sacombank</b>
	 * 
	 */
	PGResponse TransferToBankAccSTB(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Tra cứu TKNH IBFT</b>
	 * 
	 */
	PGResponse CheckBankAccIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Chuyển tiền vào TKNH IBFT</b>
	 * 
	 */
	PGResponse TransferToBankAccIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Tra cứu trạng thái giao dịch</b>
	 * 
	 * @throws Exception
	 */
	PGResponse Query(PaymentAccount paymentAccount, String inputStr) throws Exception;

	/**
	 * <b>Tra cứu số dư tk NCC</b>
	 * 
	 * @throws Exception
	 */
	PGResponse QueryBalance(PaymentAccount paymentAccount, String inputStr) throws Exception;

}
