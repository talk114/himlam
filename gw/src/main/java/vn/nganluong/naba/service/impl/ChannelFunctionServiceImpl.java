package vn.nganluong.naba.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.repository.ChannelFunctionRepository;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.service.ChannelFunctionService;

@Service
@Transactional
public class ChannelFunctionServiceImpl implements ChannelFunctionService {

	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private ChannelFunctionRepository channelFunctionRepository;

	@Override
	public ChannelFunction findChannelFunctionByCodeAndChannelCode(String code, String channelCode) {
		
		Channel channel = channelRepository.findByCode(channelCode);
		if (channel != null) {
			return channelFunctionRepository.findByCodeAndChannelId(code, channel.getId());	
		}
		return null;
		
	}

	@Override
	public ChannelFunction findChannelFunctionByCodeAndChannelId(String code, Integer channelId) {
		return channelFunctionRepository.findByCodeAndChannelId(code, channelId);
	}

	@Override
	public ChannelFunction findChannelFunctionByName(String name) {
		return channelFunctionRepository.findTopByNameAndStatus(name, true);
	}

}