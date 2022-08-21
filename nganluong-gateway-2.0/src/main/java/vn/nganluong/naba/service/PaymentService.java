package vn.nganluong.naba.service;

import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dto.PaymentDto;
import vn.nganluong.naba.dto.PaymentSearchDto;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.utils.PaginationResult;

import java.util.List;

public interface PaymentService {

    /**
     * Khởi tạo payment, chưa bao gồm trạng thái trả về của channel hoặc trạng
     * thái kết quả khởi tạo giao dịch từ gateway.
     *
     * @param paymentDTO
     */
    public void createPayment(PaymentDTO paymentDTO);

    public Payment createPaymentDto(PaymentDTO paymentDTO);

    /**
     * Cập nhật trạng thái giao dịch: Khởi tạo tại gateway, kết quả khởi tạo
     * giao dịch tại channel sau khi gọi API khởi tạo giao dịch tới channel Bao
     * gồm cập nhật cả raw response từ API tạo giao dịch mà channel trả về.
     *
     * @param paymentDTO
     */
    public void updateTransactionStatusAfterCreatedPayment(PaymentDTO paymentDTO);

    /**
     * Cập nhật trạng thái giao dịch channel sau khi truy vấn API trạng thái
     * giao dịch tới channel
     *
     * @param paymentDTO
     */
    public void updateChannelTransactionStatusPayment(PaymentDTO paymentDTO);

    public Payment findByMerchantTransactionId(String merchantTransactionId);

    public Payment findByChannelTransactionId(String channelTransactionId);

    public List<PaymentDto> getListPaymentHistory();

    public PaginationResult<PaymentDto> listPaymentHistory(PaymentSearchDto paymentSearchDto);

    public PaymentDto findDetailPaymentById(Integer id);

    /**
     * Tạo giao dịch thành công khi nhận callback từ phía bank, channel
     *
     * @param paymentDTO
     */
    public void createPaymentCallbackSuccess(PaymentDTO paymentDTO);

    public Payment updatePayment(Payment payment);

    List<Payment> getAllPaymentsYesterday(Integer channelId);

    List<Payment> getAllPaymentsSucceededYesterday(Integer channelId);

    List<Payment> getAllTokenizationNotSave();

    List<Payment> getAllPaymentByCreateDate(Integer channelId, String dateCreate);

    Payment updatePayment(PaymentDTO paymentDTO);

    List<Payment> getAllPaymentSuccessBetweenCreateTime(Integer channelId, String timeCreateFro, String timeCreateTo, boolean noTransRevert);
}
