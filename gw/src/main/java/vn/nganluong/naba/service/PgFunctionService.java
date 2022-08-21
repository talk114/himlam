package vn.nganluong.naba.service;

import vn.nganluong.naba.entities.PgFunction;

public interface PgFunctionService {

	
	PgFunction findByCodeAndChannelCode(String code, String channelCode);

	PgFunction findPgFunctionByCode(String pgFuntion);
}