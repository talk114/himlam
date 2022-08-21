package vn.nganluong.naba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.nganluong.naba.channel.vib.dto.ReconciliationMonthDataCompareDTO;
import vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO;
import vn.nganluong.naba.entities.Payment;

import java.util.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT u FROM Payment u WHERE u.channelId = :channelId and u.channelTransactionId = :channelTransactionId and u.merchantTransactionId = :merchantTransactionId")
    Payment findByChannelIdAndTransactionId(@Param("channelId") Integer channelId,
                                            @Param("channelTransactionId") String channelTransactionId,
                                            @Param("merchantTransactionId") String merchantTransactionId);

    @Query("SELECT u FROM Payment u WHERE u.merchantTransactionId = :merchantTransactionId")
    Payment findByMerchantTransactionId(@Param("merchantTransactionId") String merchantTransactionId);

    @Query("SELECT u FROM Payment u WHERE u.channelTransactionId = :channelTransactionId")
    Payment findByChannelTransactionId(@Param("channelTransactionId") String channelTransactionId);

    /**
     * Phục vụ cho đối soát: 1. VIB IBFT
     *
     * @param channelId
     * @param paymentType
     * @param channelTransactionStatus
     * @param merchantTransactionStatus
     * @param startDate
     * @param endDate
     * @return
     */
    @Query("SELECT new vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO(u.accountNo, u.amount, u.description, u.merchantTransactionId, u.timeCreated, u.sourceAccount, u.channelTransactionType) FROM Payment u WHERE u.channelId = :channelId AND u.paymentType = :paymentType AND u.channelTransactionStatus = :channelTransactionStatus AND u.merchantTransactionStatus = :merchantTransactionStatus AND (u.timeCreated BETWEEN :startDate AND :endDate)")
    List<ReconciliationDayDataCompareDTO> findByChannelAndDateAndPaymentType(@Param("channelId") Integer channelId,
                                                                             @Param("paymentType") int paymentType,
                                                                             @Param("channelTransactionStatus") Integer channelTransactionStatus,
                                                                             @Param("merchantTransactionStatus") Integer merchantTransactionStatus,
                                                                             @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * Phục vụ cho đối soát: 1. VIB VA
     *
     * @param channelId
     * @param paymentType
     * @param channelTransactionStatus
     * @param merchantTransactionStatus
     * @param startDate
     * @param endDate
     * @return
     */
    @Query("SELECT new vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO(u.accountNo, u.amount, u.description, u.merchantTransactionId, u.timeCreated, u.sourceAccount, u.channelTransactionType, u.virtualAccountNo) FROM Payment u WHERE u.channelId = :channelId AND u.paymentType = :paymentType AND u.channelTransactionStatus = :channelTransactionStatus AND u.merchantTransactionStatus = :merchantTransactionStatus AND (u.timeCreated BETWEEN :startDate AND :endDate)")
    List<ReconciliationDayDataCompareDTO> findByChannelAndDateAndPaymentTypeVA(@Param("channelId") Integer channelId,
                                                                               @Param("paymentType") int paymentType,
                                                                               @Param("channelTransactionStatus") Integer channelTransactionStatus,
                                                                               @Param("merchantTransactionStatus") Integer merchantTransactionStatus,
                                                                               @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * Phục vụ cho đối soát: MB Ecom
     *
     * @param channelId
     * @param paymentType
     * @param channelTransactionStatus
     * @param merchantTransactionStatus
     * @param startDate
     * @param endDate
     * @return
     */
    @Query("SELECT new vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO(u.accountNo, u.amount, u.description, u.merchantTransactionId, u.timeCreated, u.sourceAccount, u.channelTransactionType, u.channelTransactionId, u.channelTransactionSeq, u.cardType) FROM Payment u WHERE u.channelId = :channelId AND" +
            " u.paymentType = :paymentType AND u.channelTransactionStatus = :channelTransactionStatus AND u.merchantTransactionStatus = :merchantTransactionStatus AND (u.timeCreated BETWEEN :startDate AND :endDate)")
    List<ReconciliationDayDataCompareDTO> findByChannelAndDateAndPaymentTypeReturnWithSeq(
            @Param("channelId") Integer channelId,
            @Param("paymentType") int paymentType,
            @Param("channelTransactionStatus") Integer channelTransactionStatus,
            @Param("merchantTransactionStatus") Integer merchantTransactionStatus,
            @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * Tổng hợp đối soát: Total amount & total transaction
     *
     * @param merchantTransactionId
     * @return
     */
    @Query("SELECT new vn.nganluong.naba.channel.vib.dto.ReconciliationMonthDataCompareDTO(u.sourceAccount, u.channelTransactionType, COUNT(u), SUM(u.amount)) FROM Payment u WHERE u.channelId = :channelId AND u.paymentType = :paymentType AND u.channelTransactionStatus = :channelTransactionStatus AND u.merchantTransactionStatus = :merchantTransactionStatus AND (u.timeCreated BETWEEN :startDate AND :endDate) AND u.channelTransactionType IS NOT NULL GROUP BY u.channelTransactionType, u.sourceAccount")
    List<ReconciliationMonthDataCompareDTO> getSummaryPaymentByChannelAndDateAndPaymentType(@Param("channelId") Integer channelId,
                                                                                            @Param("paymentType") int paymentType,
                                                                                            @Param("channelTransactionStatus") Integer channelTransactionStatus,
                                                                                            @Param("merchantTransactionStatus") Integer merchantTransactionStatus,
                                                                                            @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    /**
     * Query all Payment in yesterday
     *
     * @param channelId
     * @return
     */
    @Query(value = "SELECT * FROM payment  WHERE channel_id = :channelId AND DATE_FORMAT(time_created,'%d/%m/%Y') = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 DAY),'%d/%m/%Y')", nativeQuery = true)
    List<Payment> getAllPaymentsYesterday(@Param("channelId") Integer channelId);


    /**
     * Query all Payment succeeded in yesterday
     *
     * @param channelId
     * @return
     */
    @Query(value = "SELECT * FROM pg2.payment WHERE channel_id = :channelId and (channel_transaction_status = 1 and pg_transaction_status = 1 ) and DATE_FORMAT(time_created,'%d/%m/%Y') = DATE_FORMAT(DATE_ADD(NOW(), INTERVAL -1 DAY),'%d/%m/%Y')", nativeQuery = true)
    List<Payment> getAllPaymentsSucceededYesterday(@Param("channelId") Integer channelId);

    @Query(value = "SELECT * FROM pg2.payment WHERE channel_id = 10 and virtual_account_no is not null and description='N' and DATE_FORMAT(time_created,'%d/%m/%Y') = DATE_FORMAT(NOW(),'%d/%m/%Y')", nativeQuery = true)
    List<Payment> getAllTokenizationNotSave();

    @Query(value = "SELECT * FROM pg2.payment WHERE channel_id = :channelId and (channel_transaction_status = 1 and pg_transaction_status = 1 ) and DATE_FORMAT(time_created,'%d/%m/%Y') = :dateCreate", nativeQuery = true)
    List<Payment> getAllPaymentByCreateDate(@Param("channelId") Integer channelId, @Param("dateCreate") String dateCreate);

    @Query(value = "SELECT * FROM payment WHERE channel_id = :channelId and (channel_transaction_status = 1 and pg_transaction_status = 1 ) " +
            "and time_created >= :timeCreateFro and time_created <= :timeCreateTo and if(:noTransRevert = true,revert_status is null,true)", nativeQuery = true)
    List<Payment> getAllPaymentSuccessBetweenCreateTime(@Param("channelId") Integer channelId,
                                                        @Param("timeCreateFro") String timeCreateFro,
                                                        @Param("timeCreateTo") String timeCreateTo,
                                                        @Param("noTransRevert") boolean noTransRevert);

}
