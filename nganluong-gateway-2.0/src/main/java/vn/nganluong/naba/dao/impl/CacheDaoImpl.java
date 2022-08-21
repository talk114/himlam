package vn.nganluong.naba.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Cache;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vn.nganluong.naba.dao.CacheDao;

@Repository
public class CacheDaoImpl implements CacheDao {

	private static final Logger logger = LogManager.getLogger(CacheDaoImpl.class);
	@Autowired
	private EntityManager entityManager;

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	public void clearAllCache() {
		entityManager.getEntityManagerFactory().getCache().evictAll();
		Session session = getSession();

		if (session != null) {
			session.clear(); // internal cache clear
		}

		SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);

		Cache cache = sessionFactory.getCache();

		if (cache != null) {
			cache.evictAllRegions(); // Evict data from all query regions.
//	        cache.evictCollectionRegions();
//	        cache.evictDefaultQueryRegion();
//	        cache.evictEntityRegions();
//	        cache.evictQueryRegions();
//	        cache.evictNaturalIdRegions();

		}
	}

	@Override
	public List<String> clearCacheByClassEntityName(String... tablesName) {

		List<String> successList = new ArrayList<String>();
		if (tablesName != null && tablesName.length > 0) {

			for (int i = 0; i < tablesName.length; i++) {
				try {
					entityManager.getEntityManagerFactory().getCache()
							.evict(Class.forName("vn.nganluong.naba.entities." + tablesName[i]));
					
					successList.add(" " + tablesName[i]);
				} catch (ClassNotFoundException e) {
					logger.info("Cache - Clear table error: " + e.getMessage());
				}
			}

			Session session = getSession();

			if (session != null) {
				session.clear(); // internal cache clear
			}

			SessionFactory sessionFactory = entityManager.getEntityManagerFactory().unwrap(SessionFactory.class);

			Cache cache = sessionFactory.getCache();

			if (cache != null) {
				for (int i = 0; i < tablesName.length; i++) {
					try {
						cache.evict(Class.forName("vn.nganluong.naba.entities." + tablesName[i]));
					} catch (ClassNotFoundException e) {
						logger.info("Cache - Clear table error: " + e.getMessage());
					}
				}

			}
		}
		
		return successList;
	}

	@Override
	public List<?> listEntiesName() {
		Session session = getSession();
		Set<EntityType<?>> entities = session.getMetamodel().getEntities();
		return entities.stream().map(EntityType::getJavaType).filter(Objects::nonNull).collect(Collectors.toList());

	}

}