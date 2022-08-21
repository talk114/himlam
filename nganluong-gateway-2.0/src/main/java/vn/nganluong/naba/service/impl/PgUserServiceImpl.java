package vn.nganluong.naba.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import vn.nganluong.naba.dao.ChannelDao;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.PgUser;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.repository.PgUserRepository;
import vn.nganluong.naba.service.ChannelService;
import vn.nganluong.naba.service.PgUserService;

import java.util.List;

@Service
@Transactional
public class PgUserServiceImpl implements PgUserService {

	@Autowired
	PgUserRepository pgUserRepository;

	@Override
	public PgUser findByCode(String code) {
		return pgUserRepository.findByCode(code);
	}
}