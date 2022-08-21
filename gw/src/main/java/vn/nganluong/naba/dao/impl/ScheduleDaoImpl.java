package vn.nganluong.naba.dao.impl;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.dao.ScheduleDao;
import vn.nganluong.naba.dto.ScheduleDto;
import vn.nganluong.naba.dto.ScheduleSearchDto;
import vn.nganluong.naba.utils.PaginationResult;

@Repository
public class ScheduleDaoImpl implements ScheduleDao {

	private static final String MYSQL_FORMAT_DATE= "%Y-%m-%d %H:%i:%s";
	
	@Autowired
	private EntityManager entityManager;

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public PaginationResult<ScheduleDto> listSchedule(ScheduleSearchDto scheduleSearchDto) {
		
		Session session = getSession();
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT s.id AS id, s.cron_expression AS cronExpression, s.description AS description, s.schedule_name AS scheduleName, DATE_FORMAT(s.time_created, '%Y-%m-%d %H:%i:%s') AS timeCreated, c.`name` AS channelName, f.`name` AS functionName FROM pg_schedule s INNER JOIN `channel` c ON c.id = s.channel_id INNER JOIN pg_function f ON f.id = s.pg_function_id WHERE 1 = 1");

		if (StringUtils.isNotBlank(scheduleSearchDto.getChannelId())) {
			sqlQuery.append(" AND c.id = :channelId");
		}
		if (StringUtils.isNotBlank(scheduleSearchDto.getCron_expression())) {
			sqlQuery.append(" AND s.cron_expression LIKE :cronEx");
		}

		sqlQuery.append(" ORDER BY s.time_created DESC");

		Query<ScheduleDto> query = session.createNativeQuery(sqlQuery.toString());
		query.unwrap(org.hibernate.query.NativeQuery.class);
		query.setResultTransformer(Transformers.aliasToBean(ScheduleDto.class));

		if (StringUtils.isNotBlank(scheduleSearchDto.getChannelId())) {
			query.setParameter("channelId", scheduleSearchDto.getChannelId());
		}
		
		if (StringUtils.isNotBlank(scheduleSearchDto.getCron_expression())) {
			query.setParameter("cronEx", "%" + scheduleSearchDto.getCron_expression() + "%");
		}

		Integer page = scheduleSearchDto.getPageOfList();
		if (page == null) {
			page = 1;
		}
		int maxResult = 10;
		int maxNavigationResult = 6;

		PaginationResult<ScheduleDto> result = new PaginationResult<ScheduleDto>(query, page, maxResult,
				maxNavigationResult);

		return result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	public ScheduleDto findDetailScheduleById(Integer id) {
		
		Session session = getSession();
		StringBuilder sqlQuery = new StringBuilder(
				"SELECT p.id, c.code AS channelCode, c.name AS channelName, p.account_no AS accountNo, FORMAT(p.amount, 0) AS amount, p.channel_transaction_status AS channelTransactionStatus, p.merchant_transaction_status AS merchantTransactionStatus, p.pg_transaction_status AS pgTransactionStatus, p.description AS description, DATE_FORMAT(p.time_created, '%Y-%m-%d %H:%i:%s') AS timeCreatedFormat, DATE_FORMAT(p.time_updated, '%Y-%m-%d %H:%i:%s') AS timeUpdatedFormat, p.channel_transaction_id as channelTransactionId , p.merchant_transaction_id as merchantTransactionId, p.raw_request as rawRequest, p.raw_response as rawResponse FROM payment p INNER JOIN `channel` c ON c.id = p.channel_id WHERE p.id = :id");

		Query<ScheduleDto> query = session.createNativeQuery(sqlQuery.toString());
		query.unwrap(org.hibernate.query.NativeQuery.class);
		query.setResultTransformer(Transformers.aliasToBean(ScheduleDto.class));

		query.setParameter("id", id);
		return query.getSingleResult();
	}


}