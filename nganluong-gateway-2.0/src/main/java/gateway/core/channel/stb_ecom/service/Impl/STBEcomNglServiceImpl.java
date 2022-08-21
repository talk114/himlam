package gateway.core.channel.stb_ecom.service.Impl;

import gateway.core.channel.stb_ecom.STBEcom;
import gateway.core.channel.stb_ecom.service.STBEcomNglService;
import gateway.core.dto.PGResponse;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.PaymentAccount;

@Service
public class STBEcomNglServiceImpl extends STBEcom implements STBEcomNglService {
    /**
     * Giao dịch request OTP
     */
    @Override
    public PGResponse RequestOTP(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.RequestOTP(paymentAccount,inputStr);
    }

    /**
     * Giao dịch request OTP ACC
     */
    @Override
    public PGResponse RequestOTPAcc(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.RequestOTPAcc(paymentAccount,inputStr);
    }

    /**
     * Giao dịch nạp tiền vào Ví với thẻ liên kết
     */
    @Override
    public PGResponse TopUpByCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.TopUpByCard(paymentAccount,inputStr);
    }

    /**
     * Giao dịch nạp tiền vào ví với tài khoản đã liên kết
     */
    @Override
    public PGResponse TopUpByAccount(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.TopUpByAccount(paymentAccount,inputStr);
    }

    /**
     * Giao dịch thanh toán mua hàng bằng thẻ đã liên kết
     */
    @Override
    public PGResponse PurchaseByCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.PurchaseByCard(paymentAccount,inputStr);
    }

    /**
     * Giao dịch thanh toán mua hàng bằng tài khoản đã liên kết
     */
    @Override
    public PGResponse PurchaseByAccount(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.PurchaseByAccount(paymentAccount,inputStr);
    }

    /**
     * Step 1 Giao dịch rút tiền từ ví về thẻ/tài khoản liên kết
     */
    @Override
    public PGResponse SubscriptionInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.SubscriptionInquiry(paymentAccount,inputStr);
    }

    /**
     * Step 2 Giao dịch rút tiền từ ví về thẻ/tài khoản liên kết
     */
    @Override
    public PGResponse CashOutSubscription(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.CashOutSubscription(paymentAccount,inputStr);
    }

    /**
     * Step 1 Giao dịch thực hiện chuyển tiền từ Ví đếm thẻ sacombank
     */
    @Override
    public PGResponse CardInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.CardInquiry(paymentAccount,inputStr);
    }

    /**
     * Step 2 Giao dịch thực hiện chuyển tiền từ Ví đếm thẻ sacombank
     */
    @Override
    public PGResponse FundTransferToSTBCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.FundTransferToSTBCard(paymentAccount,inputStr);
    }

    /**
     * Step 1 Giao dịch thực hiện chuyển tiền từ ví đến tài khoản sacombank
     */
    @Override
    public PGResponse AccountInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.AccountInquiry(paymentAccount,inputStr);
    }

    /**
     * Step 2 Giao dịch thực hiện chuyển tiền từ ví đến tài khoản sacombank
     */
    @Override
    public PGResponse FundTransferToSTBAccount(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.FundTransferToSTBAccount(paymentAccount,inputStr);
    }

    /**
     * Giao dịch hủy liên kết thẻ/tài khoản
     */
    @Override
    public PGResponse CancelSubscription(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.CancelSubscription(paymentAccount,inputStr);
    }

    /**
     * Step 1 Giao dịch chuyển tiền đến thẻ nội địa Napas
     */
    @Override
    public PGResponse DomesticInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.DomesticInquiry(paymentAccount,inputStr);
    }

    /**
     * Step 2 Giao dịch chuyển tiền đến thẻ nội địa Napas
     */
    @Override
    public PGResponse DomesticFundTransfer(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.DomesticFundTransfer(paymentAccount,inputStr);
    }

    /**
     * Step 1 Giao dịch chuyển tiền đến taì khoản napas
     */
    @Override
    public PGResponse DomesticAccountInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.DomesticAccountInquiry(paymentAccount,inputStr);
    }

    /**
     * Step 2 Giao dịch chuyển tiền đến taì khoản napas
     */
    @Override
    public PGResponse DomesticAccountFundTransfer(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.DomesticAccountFundTransfer(paymentAccount,inputStr);
    }

    /**
     * Step 1 Giao dịch chuyển tiền đến thẻ Visa/Master
     */
    @Override
    public PGResponse VisaInquiry(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.VisaInquiry(paymentAccount,inputStr);
    }

    /**
     * Step 2 Giao dịch chuyển tiền đến thẻ Visa/Master
     */
    @Override
    public PGResponse VisaTransfer(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.VisaTransfer(paymentAccount,inputStr);
    }


    /**
     * Liên kết thẻ
     */
    @Override
    public PGResponse LinkCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.LinkCard(paymentAccount, inputStr);
    }

    /**
     * <b>Notify thanh toán checkout</b>
     *
     */
    @Override
    public String NotifySTB_ECOM( String inputStr) throws Exception {
        return super.NotifySTB_ECOM(inputStr);
    }


    /**
     * <b>TransactionQuery</b>
     *
     */
    @Override
    public PGResponse TransactionQuery(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.TransactionQuery(paymentAccount, inputStr);
    }

    /**
     * <b>Step 1 Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
     */
    @Override
    public PGResponse Payment(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.Payment(paymentAccount, inputStr);
    }

    /**
     * <b>Step 2 Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
     */
    public PGResponse VerifyOtpPayment(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.VerifyOtpPayment(paymentAccount, inputStr);
    }

    /**
     * <b>Đảo giao dịch Rút tiền/Thanh toán bằng thẻ STB nội địa</b>
     */
    public PGResponse ReversalPayment(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.ReversalPayment(paymentAccount, inputStr);
    }

    /**
     * <b>Tra cứu thông tin thẻ nội địa, napas trước khi thực hiển Rút Ví ->
     * Thẻ</b>
     */
    @Override
    public PGResponse CheckCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.CheckCard(paymentAccount, inputStr);
    }


    /**
     * <b>Chuyển tiền vào thẻ STB nội địa (ko phải thẻ liên kết)</b>
     */
    @Override
    public PGResponse TransferToCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.TransferToCard(paymentAccount, inputStr);
    }

    /**
     * <b>Đảo giao dịch Chuyển tiền vào thẻ STB nội địa (ko phải thẻ liên
     * kết)</b>
     *
     */
    public PGResponse ReversalTransferToCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.ReversalTransferToCard(paymentAccount, inputStr);
    }

    /**
     * <b>Tra cứu thông tin thẻ iBFT</b>
     */
    public PGResponse CheckCardIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.CheckCardIBFT(paymentAccount, inputStr);
    }

    /**
     * <b>Chuyển tiền tới thẻ IBFT</b>
     */
    public PGResponse TransferToCardIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.TransferToCardIBFT(paymentAccount, inputStr);
    }

    /**
     * <b>Tra cứu thẻ Visa Master</b>
     *
     */
    @Override
    public PGResponse CheckVisaMasterCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.CheckVisaMasterCard(paymentAccount, inputStr);
    }

    /**
     * <b>Chuyển tiền vào thẻ Visa Master</b>
     *
     */
    @Override
    public PGResponse TransferToVisaMasterCard(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.TransferToVisaMasterCard(paymentAccount, inputStr);
    }

    /**
     * <b>Tra cứu TKNH Sacombank</b>
     *
     */
    public PGResponse CheckBankAccSTB(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.CheckBankAccSTB(paymentAccount, inputStr);
    }

    /**
     * <b>Chuyển tiền vào TKNH Sacombank</b>
     *
     */
    public PGResponse TransferToBankAccSTB(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.TransferToBankAccSTB(paymentAccount, inputStr);
    }

    /**
     * <b>Tra cứu TKNH IBFT</b>
     *
     */
    public PGResponse CheckBankAccIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.CheckBankAccIBFT(paymentAccount, inputStr);
    }

    /**
     * <b>Chuyển tiền vào TKNH IBFT</b>
     *
     */
    public PGResponse TransferToBankAccIBFT(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.TransferToBankAccIBFT(paymentAccount, inputStr);
    }

    /**
     * <b>Tra cứu trạng thái giao dịch</b>
     *
     * @throws Exception
     */
    @Override
    public PGResponse Query(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.Query(paymentAccount, inputStr);
    }

    /**
     * <b>Tra cứu số dư tk NCC</b>
     *
     * @throws Exception
     */
    @Override
    public PGResponse QueryBalance(PaymentAccount paymentAccount, String inputStr) throws Exception {
        return super.QueryBalance(paymentAccount, inputStr);
    }
}
