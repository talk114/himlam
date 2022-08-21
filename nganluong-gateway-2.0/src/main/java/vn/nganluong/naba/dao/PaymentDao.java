package vn.nganluong.naba.dao;

import java.util.List;

import vn.nganluong.naba.dto.PaymentDto;
import vn.nganluong.naba.dto.PaymentSearchDto;
import vn.nganluong.naba.utils.PaginationResult;

public interface PaymentDao {
	public List<PaymentDto> findListPaymentDto(String accountNo);
	
	public PaginationResult<PaymentDto> listPaymentHistory(PaymentSearchDto paymentSearchDto);
	
	public PaymentDto findDetailPaymentById(Integer id);

}