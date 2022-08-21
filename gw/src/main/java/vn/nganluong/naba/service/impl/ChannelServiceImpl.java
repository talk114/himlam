package vn.nganluong.naba.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import vn.nganluong.naba.dao.ChannelDao;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.service.ChannelService;

@Service
@Transactional
public class ChannelServiceImpl implements ChannelService {
	@Autowired
	private ChannelDao channelDao;
	
	@Autowired
	private ChannelRepository channelRepository;

	@Override
	public Channel findById(Integer id) {
		return channelRepository.findById(id).get();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public Channel findByName(String name) {
		return channelDao.findByName(name);
	}

	@Override
	public Channel findByCode(String code) {
		
		return channelDao.findByCode(code);
	}

	@Override
	public List<Channel> findAllByStatusOrderByIdAsc() {
		return channelRepository.findAllByStatusOrderByIdAsc(true);
	}

	@Override
	public List<Channel> findAllByStatusOrderByCodeAsc() {
		return channelRepository.findAllByStatusOrderByCodeAsc(true);
	}

	@Override
	public Map<String, String> getMapChannelList(ChannelService channelService) {

		List<Channel> channelList = findAllByStatusOrderByCodeAsc();
		Map<String, String> entitiesListName = new HashMap<String, String>();
		for (Channel entity : channelList) {
			entitiesListName.put(entity.getId().toString(), entity.getName());
		}

		return entitiesListName;
	}

}