package vn.nganluong.naba.dao;

import java.util.List;

public interface CacheDao {
	public void clearAllCache();
	public List<String> clearCacheByClassEntityName(String... tablesName);
	public List<?> listEntiesName();
}