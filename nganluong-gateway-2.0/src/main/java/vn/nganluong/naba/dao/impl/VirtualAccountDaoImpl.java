package vn.nganluong.naba.dao.impl;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.dao.VirtualAccountDao;
import vn.nganluong.naba.dto.VirtualAccountDto;
import vn.nganluong.naba.entities.VirtualAccount;

@Repository
public class VirtualAccountDaoImpl implements VirtualAccountDao {

	@Autowired
	private EntityManager entityManager;

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public boolean createVirtualAccount(VirtualAccountDto virtualAccountDto) {

		entityManager.createNativeQuery(
				"INSERT INTO `virtual_account` (`channel_id`, `virtual_account_no`, `virtual_account_name`, `merchant_code`, `phone_number`) VALUES (?, ?, ?, ?, ?)")
				.setParameter(1, virtualAccountDto.getChannelId())
				.setParameter(2, virtualAccountDto.getVirtualAccountNo())
				.setParameter(3, virtualAccountDto.getVirtualAccountName())
				.setParameter(4, virtualAccountDto.getMerchantCode())
				.setParameter(5, virtualAccountDto.getPhoneNumber()).executeUpdate();
		
//		Session session = getSession();
//		Query query = session.createNativeQuery("INSERT INTO `virtual_account` (`channel_id`, `virtual_account_no`, `virtual_account_name`, `merchant_code`, `phone_number`) VALUES (?, ?, ?, ?, ?)");
//		query.setParameter(1, virtualAccountDto.getChannelId())
//		.setParameter(2, virtualAccountDto.getVirtualAccountNo())
//		.setParameter(3, virtualAccountDto.getVirtualAccountName())
//		.setParameter(4, virtualAccountDto.getMerchantCode())
//		.setParameter(5, virtualAccountDto.getPhoneNumber()).executeUpdate();
		return true;
	}

	@Override
	public VirtualAccount findVirtualAccount(String virtualAccountNo) {
		Session session = getSession();
		String hql = "FROM VirtualAccount C WHERE C.virtualAccountNo = :virtualAccountNo and C.status =1";
		Query<VirtualAccount> query = session.createQuery(hql, VirtualAccount.class).setParameter("virtualAccountNo",
				virtualAccountNo);
		return query.uniqueResult();
	}

}