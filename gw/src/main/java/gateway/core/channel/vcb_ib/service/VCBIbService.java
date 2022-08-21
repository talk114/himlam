package gateway.core.channel.vcb_ib.service;

import gateway.core.dto.PGResponse;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PaymentAccount;

import java.io.IOException;

public interface VCBIbService {

    /**
     * Hàm gọi tới VCB để xác minh trạng thái thanh toán của giao dịch
     * @param inputStr
     * @return
     * @throws Exception
     */
    PGResponse query(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    /**
     * Hàm VCB sẽ xác minh Partner và khởi tạo giao dịch
     * @param inputStr
     * @return
     * @throws Exception
     */
    PGResponse verifyPayment(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    /**
     * VCB sẽ xác minh giao dịch refund trên nguyên tắc đã tồn tại giao dịch đó.
     * Số tiền refund sẽ phụ thuộc vào Merchant
     * @param channelFunction
     * @param paymentAccount
     * @param inputStr
     * @return
     * @throws Exception
     */
    PGResponse refund(ChannelFunction channelFunction, PaymentAccount paymentAccount, String inputStr) throws Exception;

    /**
     * Khi khách hàng thanh toán thành công VCB callback
     * @param paymentAccount
     * @param request
     * @throws IOException
     */
    void vcbCallback(PaymentAccount paymentAccount, String request) throws IOException;
}