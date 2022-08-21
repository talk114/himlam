package vn.nganluong.naba.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.dao.PaymentDao;
import vn.nganluong.naba.dto.PaymentDto;
import vn.nganluong.naba.dto.PaymentSearchDto;
import vn.nganluong.naba.utils.PaginationResult;

@Repository
public class PaymentDaoImpl implements PaymentDao {

	private static final String MYSQL_FORMAT_DATE= "%Y-%m-%d %H:%i:%s";
	
	@Autowired
	private EntityManager entityManager;

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<PaymentDto> findListPaymentDto(String accountNo) {
		Session session = getSession();
		List<PaymentDto> paymentList = new ArrayList<PaymentDto>();
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT c.code AS channelCode, c.name AS channelName, CONCAT(COALESCE(p.account_no,''), COALESCE(p.card_no, '')) AS accountNo, FORMAT(p.amount, 0) AS amount, p.channel_transaction_status AS channelTransactionStatus, p.merchant_transaction_status AS merchantTransactionStatus, p.pg_transaction_status AS pgTransactionStatus, p.description AS description, DATE_FORMAT(p.time_created, '%Y-%m-%d %H:%i:%s') AS timeCreatedFormat, DATE_FORMAT(p.time_updated, '%Y-%m-%d %H:%i:%s') AS timeUpdatedFormat, p.time_created AS timeCreated, p.time_updated AS timeUpdated FROM payment p INNER JOIN `channel` c ON c.id = p.channel_id WHERE 1 = 1");

		if (StringUtils.isNotBlank(accountNo)) {
			sqlQuery.append(" AND p.account_no = :accountNo");
		}

		sqlQuery.append(" ORDER BY p.time_created DESC");

		Query<PaymentDto> query = session.createNativeQuery(sqlQuery.toString());
		query.unwrap(org.hibernate.query.NativeQuery.class);
		query.setResultTransformer(Transformers.aliasToBean(PaymentDto.class));

		if (StringUtils.isNotBlank(accountNo)) {
			query.setParameter("accountNo", accountNo);
		}

//		query.addScalar("sort", IntegerType.INSTANCE);
//		query.addScalar("question", StringType.INSTANCE);
//		query.addScalar("answer", StringType.INSTANCE);

		int page = 1;
		int maxResult = 1;
		int maxNavigationResult = 2;

		PaginationResult<PaymentDto> result = new PaginationResult<PaymentDto>(query, page, maxResult,
				maxNavigationResult);

		// Result:
		List<PaymentDto> payments = result.getList();
		int totalPages = result.getTotalPages();
		int totalRecords = result.getTotalRecords();

		// 1 2 3 4 5 ... 11 12 13
		List<Integer> navPages = result.getNavigationPages();

		// paymentList = query.getResultList();
		return paymentList;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public PaginationResult<PaymentDto> listPaymentHistory(PaymentSearchDto paymentSearchDto) {
		
		Session session = getSession();
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT p.id, c.code AS channelCode, c.name AS channelName, CONCAT(COALESCE(p.account_no,''), COALESCE(p.card_no, '')) AS accountNo, FORMAT(p.amount, 0) AS amount, p.channel_transaction_status AS channelTransactionStatus, p.merchant_transaction_status AS merchantTransactionStatus,p.merchant_name AS merchantName ,p.pg_transaction_status AS pgTransactionStatus, p.description AS description, DATE_FORMAT(p.time_created, '%Y-%m-%d %H:%i:%s') AS timeCreatedFormat, DATE_FORMAT(p.time_updated, '%Y-%m-%d %H:%i:%s') AS timeUpdatedFormat, p.time_created AS timeCreated, p.time_updated AS timeUpdated, p.channel_transaction_id as channelTransactionId , p.merchant_transaction_id as merchantTransactionId, p.revert_status AS revertStatus FROM payment p INNER JOIN `channel` c ON c.id = p.channel_id WHERE 1 = 1");

		if (StringUtils.isNotBlank(paymentSearchDto.getAccountNo())) {
			sqlQuery.append(" AND p.account_no = :accountNo");
		}
		if (StringUtils.isNotBlank(paymentSearchDto.getChannelCode())) {
			sqlQuery.append(" AND c.id = :channelId");
		}
		
		if (StringUtils.isNotBlank(paymentSearchDto.getClientTransactionId())) {
			sqlQuery.append(" AND p.merchant_transaction_id = :clientTransactionId");
		}
		
		if (StringUtils.isNotBlank(paymentSearchDto.getFromDate())) {
			sqlQuery.append(" AND p.time_created >=  STR_TO_DATE(:fromDate, '"
					+ MYSQL_FORMAT_DATE
					+ "')");
		}
		
		if (StringUtils.isNotBlank(paymentSearchDto.getToDate())) {
			sqlQuery.append(" AND p.time_created <=  STR_TO_DATE(:toDate, '"
					+ MYSQL_FORMAT_DATE
					+ "')");
		}

		sqlQuery.append(" ORDER BY p.time_created DESC");

		Query<PaymentDto> query = session.createNativeQuery(sqlQuery.toString());
		query.unwrap(org.hibernate.query.NativeQuery.class);
		query.setResultTransformer(Transformers.aliasToBean(PaymentDto.class));

		if (StringUtils.isNotBlank(paymentSearchDto.getAccountNo())) {
			query.setParameter("accountNo", paymentSearchDto.getAccountNo());
		}
		
		if (StringUtils.isNotBlank(paymentSearchDto.getChannelCode())) {
			query.setParameter("channelId", paymentSearchDto.getChannelCode());
		}
		if (StringUtils.isNotBlank(paymentSearchDto.getClientTransactionId())) {
			query.setParameter("clientTransactionId", paymentSearchDto.getClientTransactionId());
		}
		if (StringUtils.isNotBlank(paymentSearchDto.getFromDate())) {
			query.setParameter("fromDate", paymentSearchDto.getFromDate());
		}
		
		if (StringUtils.isNotBlank(paymentSearchDto.getToDate())) {
			query.setParameter("toDate", paymentSearchDto.getToDate());
		}
		if (StringUtils.isNotBlank(paymentSearchDto.getMerchantName())) {
			query.setParameter("merchantName", paymentSearchDto.getMerchantName());
		}

		
		// " AND l.created_at <=  STR_TO_DATE(:endDate, '%d/%m/%Y %H:%i:%s')"
		
		// " AND l.created_at >=  STR_TO_DATE(:startDate, '%d/%m/%Y %H:%i:%s')"
		
//		query.addScalar("sort", IntegerType.INSTANCE);
//		query.addScalar("question", StringType.INSTANCE);
//		query.addScalar("answer", StringType.INSTANCE);

		Integer page = paymentSearchDto.getPageOfList();
		if (page == null) {
			page = 1;
		}
		int maxResult = 10;
		int maxNavigationResult = 6;

		PaginationResult<PaymentDto> result = new PaginationResult<PaymentDto>(query, page, maxResult,
				maxNavigationResult);

		/*
		// Result:
		List<PaymentDto> payments = result.getList();
		int totalPages = result.getTotalPages();
		int totalRecords = result.getTotalRecords();

		// 1 2 3 4 5 ... 11 12 13
		List<Integer> navPages = result.getNavigationPages();
		*/
		return result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public PaymentDto findDetailPaymentById(Integer id) {
		
		Session session = getSession();
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT p.id, c.code AS channelCode, c.name AS channelName, CONCAT(COALESCE(p.account_no,''), COALESCE(p.card_no, '')) AS accountNo, FORMAT(p.amount, 0) AS amount, p.channel_transaction_status AS channelTransactionStatus, p.merchant_transaction_status AS merchantTransactionStatus, p.pg_transaction_status AS pgTransactionStatus, p.description AS description, DATE_FORMAT(p.time_created, '%Y-%m-%d %H:%i:%s') AS timeCreatedFormat, DATE_FORMAT(p.time_updated, '%Y-%m-%d %H:%i:%s') AS timeUpdatedFormat, p.channel_transaction_id as channelTransactionId , p.merchant_transaction_id as merchantTransactionId, p.raw_request as rawRequest, p.raw_response as rawResponse, p.revert_status AS revertStatus FROM payment p INNER JOIN `channel` c ON c.id = p.channel_id WHERE p.id = :id");

		Query<PaymentDto> query = session.createNativeQuery(sqlQuery.toString());
		query.unwrap(org.hibernate.query.NativeQuery.class);
		query.setResultTransformer(Transformers.aliasToBean(PaymentDto.class));

		query.setParameter("id", id);
		return query.getSingleResult();
	}


}