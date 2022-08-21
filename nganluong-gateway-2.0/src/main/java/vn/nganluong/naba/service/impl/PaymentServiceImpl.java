package vn.nganluong.naba.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.nganluong.naba.channel.vib.dto.PaymentDTO;
import vn.nganluong.naba.dao.PaymentDao;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.dto.PaymentConst.EnumPaymentType;
import vn.nganluong.naba.dto.PaymentDto;
import vn.nganluong.naba.dto.PaymentSearchDto;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.repository.PaymentRepository;
import vn.nganluong.naba.service.PaymentService;
import vn.nganluong.naba.utils.PaginationResult;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    public void createPayment(PaymentDTO paymentDTO) {

        Payment payment = new Payment();
        payment.setChannelId(paymentDTO.getChannelId());
        payment.setPgFunctionId(paymentDTO.getPgFunctionId());
        payment.setMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        payment.setChannelTransactionId(paymentDTO.getChannelTransactionId());
        payment.setMerchantCode(paymentDTO.getMerchantCode());
        payment.setMerchantName(paymentDTO.getMerchantName());
        payment.setChannelTransactionType(paymentDTO.getChannelTransactionType());
        payment.setSourceAccount(paymentDTO.getSourceAccount());

        if (NumberUtils.isParsable(paymentDTO.getAmount())) {
            if (paymentDTO.getAmount().length() <= 12) {
                payment.setAmount(NumberUtils.createBigDecimal(paymentDTO.getAmount()));
            }

        }

        payment.setPgTransactionStatus(paymentDTO.getPgTransactionStatus());
        payment.setAccountNo(paymentDTO.getAccountNo());
        payment.setCardNo(paymentDTO.getCardNo());
        payment.setDescription(paymentDTO.getDescription());
        payment.setRawRequest(paymentDTO.getRawRequest());
        payment.setRawResponse(paymentDTO.getRawResponse());

        payment.setVirtualAccountNo(paymentDTO.getVirtualAccountNo());
        if (paymentDTO.getChannelTransactionStatus() == null) {
            payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        } else {
            payment.setChannelTransactionStatus(paymentDTO.getChannelTransactionStatus());
        }
        if (paymentDTO.getMerchantTransactionStatus() == null) {
            payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        } else {
            payment.setMerchantTransactionStatus(paymentDTO.getMerchantTransactionStatus());
        }
        if (paymentDTO.getPgTransactionStatus() == null) {
            payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        } else {
            payment.setPgTransactionStatus(paymentDTO.getPgTransactionStatus());
        }
        payment.setTimeCreated(paymentDTO.getTimeCreated());
        if (paymentDTO.getPaymentType() == null) {
            payment.setPaymentType(EnumPaymentType.ACCOUNT_NO.code());
        } else {
            payment.setPaymentType(paymentDTO.getPaymentType());
        }

        if (paymentDTO.getCardType() != null) {
            payment.setPaymentType(paymentDTO.getCardType());
        }

        paymentRepository.save(payment);

    }

    @Override
    public Payment createPaymentDto(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setChannelId(paymentDTO.getChannelId());
        payment.setPgFunctionId(paymentDTO.getPgFunctionId());
        payment.setMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        payment.setMerchantCode(paymentDTO.getMerchantCode());
        payment.setChannelTransactionType(paymentDTO.getChannelTransactionType());
        payment.setSourceAccount(paymentDTO.getSourceAccount());
        if (NumberUtils.isParsable(paymentDTO.getAmount())) {
            if (paymentDTO.getAmount().length() <= 12) {
                payment.setAmount(NumberUtils.createBigDecimal(paymentDTO.getAmount()));
            }
        }
        payment.setPgTransactionStatus(paymentDTO.getPgTransactionStatus());
        payment.setAccountNo(paymentDTO.getAccountNo());
        payment.setCardNo(paymentDTO.getCardNo());
        payment.setDescription(paymentDTO.getDescription());
        payment.setRawRequest(paymentDTO.getRawRequest());
        payment.setChannelTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        payment.setPgTransactionStatus(PaymentConst.EnumBankStatus.PENDING.code());
        if (paymentDTO.getPaymentType() == null) {
            payment.setPaymentType(EnumPaymentType.ACCOUNT_NO.code());
        } else {
            payment.setPaymentType(paymentDTO.getPaymentType());
        }
        return paymentRepository.save(payment);
    }

    @Override
    public void updateTransactionStatusAfterCreatedPayment(PaymentDTO paymentDTO) {

        Payment payment = paymentRepository.findByMerchantTransactionId(paymentDTO.getMerchantTransactionId());

        if (payment != null) {
            payment.setPgTransactionStatus(paymentDTO.getPgTransactionStatus());
            payment.setChannelTransactionId(paymentDTO.getChannelTransactionId());
            if (paymentDTO.getChannelTransactionStatus() != null) {
                payment.setChannelTransactionStatus(paymentDTO.getChannelTransactionStatus());
            }

            if (paymentDTO.getMerchantTransactionStatus() != null) {
                // TODO
                payment.setMerchantTransactionStatus(paymentDTO.getMerchantTransactionStatus());
            }
            payment.setChannelMessage(paymentDTO.getChannelMessage());
            payment.setChannelStatus(paymentDTO.getChannelStatus());

            payment.setClientRequestId(paymentDTO.getClientRequestId());
            payment.setRawResponse(paymentDTO.getRawResponse());
            payment.setTimeUpdated(null);

            paymentRepository.save(payment);
        }

    }

    @Override
    public Payment updatePayment(PaymentDTO paymentDTO) {

        Payment payment = paymentRepository.findByMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        payment.setVirtualAccountNo(paymentDTO.getVirtualAccountNo());
        payment.setDescription(paymentDTO.getDescription());
        payment.setPgFunctionId(paymentDTO.getPgFunctionId());
        payment.setRawRequest(payment.getRawRequest() + "***2***" + paymentDTO.getRawRequest());
        payment.setRawResponse(payment.getRawResponse() + "***2***" + paymentDTO.getRawResponse());

        if (payment.getPgTransactionStatus() != paymentDTO.getPgTransactionStatus()) {
            payment.setPgTransactionStatus(paymentDTO.getPgTransactionStatus());
        }

        return paymentRepository.save(payment);
    }

    @Override
    public void updateChannelTransactionStatusPayment(PaymentDTO paymentDTO) {

        System.out.println("UPDATE TRANSACTION STATUS REQUEST: " + paymentDTO.getRawRequest() + "RESPONSE: " + paymentDTO.getRawResponse());

        Payment payment = paymentRepository.findByMerchantTransactionId(paymentDTO.getMerchantTransactionId());

        if (payment != null) {

            if (paymentDTO.getChannelTransactionStatus() != null
                    && NumberUtils.compare(payment.getChannelTransactionStatus().intValue(),
                    PaymentConst.EnumBankStatus.PENDING.code()) == 0) {

                payment.setChannelTransactionStatus(paymentDTO.getChannelTransactionStatus());
                payment.setMerchantTransactionStatus(paymentDTO.getChannelTransactionStatus()); // TODO

                // Update sequence no (FT)
                if (StringUtils.isNotBlank(paymentDTO.getChannelTransactionSeq())) {
                    payment.setChannelTransactionSeq(paymentDTO.getChannelTransactionSeq());
                }

            }

            if (paymentDTO.getChannelRevertStatus() != null) {
                payment.setRevertStatus(paymentDTO.getChannelRevertStatus());
            }

            if (StringUtils.isNotBlank(paymentDTO.getRawRequest())) {
                payment.setRawRequest(payment.getRawRequest() + paymentDTO.getRawRequest());
            }

            if (StringUtils.isNotBlank(paymentDTO.getRawResponse())) {
                payment.setRawResponse(payment.getRawResponse() + paymentDTO.getRawResponse());
            }
            if (StringUtils.isNotBlank(paymentDTO.getChannelTransactionId())) {
                payment.setChannelTransactionId(payment.getChannelTransactionId() + paymentDTO.getChannelTransactionId());
            }
            if (paymentDTO.getCardType() != null) {
                payment.setCardType(paymentDTO.getCardType());
            }
            if (paymentDTO.getPgTransactionStatus() != null) {
                payment.setPgTransactionStatus(paymentDTO.getPgTransactionStatus());
            }
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            payment.setTimeUpdated(cal.getTime());
            paymentRepository.save(payment);
        }

    }

    @Override
    public Payment findByMerchantTransactionId(String merchantTransactionId) {
        return paymentRepository.findByMerchantTransactionId(merchantTransactionId);
    }

    @Override
    public Payment findByChannelTransactionId(String channelTransactionId) {
        return paymentRepository.findByChannelTransactionId(channelTransactionId);
    }

    @Override
    public List<PaymentDto> getListPaymentHistory() {
        // TODO
        List<PaymentDto> resultList = paymentDao.findListPaymentDto("001704060026697");
        return resultList;
    }

    @Override
    public PaginationResult<PaymentDto> listPaymentHistory(PaymentSearchDto paymentSearchDto) {
        return paymentDao.listPaymentHistory(paymentSearchDto);
    }

    @Override
    public PaymentDto findDetailPaymentById(Integer id) {
        return paymentDao.findDetailPaymentById(id);
    }

    @Override
    public void createPaymentCallbackSuccess(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setChannelId(paymentDTO.getChannelId());
        payment.setPgFunctionId(paymentDTO.getPgFunctionId());
        payment.setMerchantTransactionId(paymentDTO.getMerchantTransactionId());
        payment.setChannelTransactionId(paymentDTO.getChannelTransactionId());

        if (NumberUtils.isParsable(paymentDTO.getAmount())) {
            if (paymentDTO.getAmount().length() <= 12) {
                payment.setAmount(NumberUtils.createBigDecimal(paymentDTO.getAmount()));
            }

        }

        payment.setAccountNo(paymentDTO.getAccountNo());
        payment.setDescription(paymentDTO.getDescription());
        payment.setRawRequest(paymentDTO.getRawRequest());

        payment.setChannelTransactionStatus(paymentDTO.getChannelTransactionStatus());
        payment.setMerchantTransactionStatus(PaymentConst.EnumBankStatus.SUCCEEDED.code()); // TODO
        payment.setPgTransactionStatus(paymentDTO.getPgTransactionStatus());

        if (paymentDTO.getPaymentType() == null) {
            payment.setPaymentType(EnumPaymentType.ACCOUNT_NO.code());
        } else {
            payment.setPaymentType(paymentDTO.getPaymentType());
        }

        payment.setTimeCreated(paymentDTO.getTimeCreated());
        payment.setVirtualAccountNo(paymentDTO.getVirtualAccountNo());
        paymentRepository.save(payment);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Payment updatePayment(Payment payment) {
        if (payment != null) {
            System.out.println("Update payment: " + payment);
            paymentRepository.saveAndFlush(payment);
        }
        return payment;
    }

    @Override
    public List<Payment> getAllPaymentsYesterday(Integer channelId) {
        return paymentRepository.getAllPaymentsYesterday(channelId);
    }

    @Override
    public List<Payment> getAllPaymentsSucceededYesterday(Integer channelId) {
        return paymentRepository.getAllPaymentsSucceededYesterday(channelId);
    }

    @Override
    public List<Payment> getAllTokenizationNotSave() {
        return paymentRepository.getAllTokenizationNotSave();
    }


    @Override
    public List<Payment> getAllPaymentByCreateDate(Integer channelId, String dateCreate) {
        return paymentRepository.getAllPaymentByCreateDate(channelId, dateCreate);
    }

    @Override
    public List<Payment> getAllPaymentSuccessBetweenCreateTime(Integer channelId, String timeCreateFro, String timeCreateTo, boolean noTransRevert) {
        return paymentRepository.getAllPaymentSuccessBetweenCreateTime(channelId, timeCreateFro, timeCreateTo, noTransRevert);
    }
}
