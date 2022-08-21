package vn.nganluong.naba.service;

import vn.nganluong.naba.entities.PgErrorDefinition;

public interface PgErrorDefinitionService {

	
	public PgErrorDefinition findByCode(String code);

}