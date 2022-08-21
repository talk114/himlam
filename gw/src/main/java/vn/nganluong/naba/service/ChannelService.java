package vn.nganluong.naba.service;

import java.util.List;
import java.util.Map;

import vn.nganluong.naba.entities.Channel;

public interface ChannelService {

	Channel findById(Integer id);

	public Channel findByName(String name);
	
	public Channel findByCode(String code);
	
	public List<Channel> findAllByStatusOrderByIdAsc();

	List<Channel> findAllByStatusOrderByCodeAsc();

	Map<String, String> getMapChannelList(ChannelService channelService);

}