package vn.nganluong.naba.service;

import java.util.List;

public interface CacheService {

	public void clearCache();

	public List<String> clearCacheByClassEntityName(String tablesNames);
	
	public List<String> listEntiesName();

}