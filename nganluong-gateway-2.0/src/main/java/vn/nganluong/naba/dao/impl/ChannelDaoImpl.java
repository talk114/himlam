package vn.nganluong.naba.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import vn.nganluong.naba.dao.ChannelDao;
import vn.nganluong.naba.entities.Channel;

@Repository
public class ChannelDaoImpl implements ChannelDao{

	@Autowired
    private EntityManager entityManager;
	
	private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
	
	@Transactional(propagation = Propagation.MANDATORY)
	@Override
	public Channel findByName(String s) {
		Session session = getSession();
		List<Channel> bankList = new ArrayList<Channel>();
		try {
			String sql = "select * from channel";
			Query query = session.createSQLQuery(sql).addEntity(Channel.class);
			List<Object> rows = new ArrayList<Object>();
			rows = query.list();
			for(Object r: rows) {
				bankList.add((Channel)r);
			}
		} catch (HibernateException ex) {
			ex.printStackTrace();
		}
		if (bankList.size() > 0) {
			for (int i = 0; i < bankList.size(); i++) {
				if(bankList.get(i).getName().equals(s)){
					return bankList.get(i);
				}
			}
		}
//		session.close();
		return null;
	}

	@Override
	public Channel findByCode(String code) {
		Session session = getSession();
		String hql = "FROM Channel C WHERE C.code = :code and C.status =1";
		Query<Channel> query = session.createQuery(hql, Channel.class).setParameter("code", code).setCacheable(true);
		return query.uniqueResult();
	}
}