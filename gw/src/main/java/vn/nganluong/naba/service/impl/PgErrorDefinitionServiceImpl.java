package vn.nganluong.naba.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.nganluong.naba.entities.PgErrorDefinition;
import vn.nganluong.naba.repository.PgErrorDefinitionRepository;
import vn.nganluong.naba.service.PgErrorDefinitionService;

@Service
@Transactional
public class PgErrorDefinitionServiceImpl implements PgErrorDefinitionService {

	@Autowired
	private PgErrorDefinitionRepository pgErrorDefinitionRepository;
	
	@Override
	public PgErrorDefinition findByCode(String code) {
		return pgErrorDefinitionRepository.findByCodeAndStatus(code);
	}

	

}