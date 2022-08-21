package vn.nganluong.naba.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.nganluong.naba.dao.CacheDao;
import vn.nganluong.naba.service.CacheService;

@Service
public class CacheServiceImpl implements CacheService {
	@Autowired
	private CacheDao cacheDao;

	@Override
	public void clearCache() {
		cacheDao.clearAllCache();
	}

	@Override
	public List<String> clearCacheByClassEntityName(String tablesNames) {

		if (StringUtils.isNotEmpty(tablesNames)) {
			return cacheDao.clearCacheByClassEntityName(StringUtils.split(tablesNames, ","));
		}
		return null;
	}

	@Override
	public List<String> listEntiesName() {
		List<String> entitiesName = new ArrayList<String>();
		List<?> listName = cacheDao.listEntiesName();
		for (Object object : listName) {
			String[] classDetail = StringUtils.split(object.toString(), '.');
			String className = classDetail[classDetail.length - 1];
			
			entitiesName.add(className);
		}
		return entitiesName;
	}

}