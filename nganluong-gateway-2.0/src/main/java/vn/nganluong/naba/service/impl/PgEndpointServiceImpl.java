package vn.nganluong.naba.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.nganluong.naba.entities.PgEndpoint;
import vn.nganluong.naba.repository.PgEndPointRepository;
import vn.nganluong.naba.service.PgEndpointService;

@Service
@Transactional
public class PgEndpointServiceImpl implements PgEndpointService {

	@Autowired
	PgEndPointRepository pgEndPointRepository;


	@Override
	public PgEndpoint findById(Integer id) {
		return  pgEndPointRepository.findById(id).get();
	}
}