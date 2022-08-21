package vn.nganluong.naba.dao;

import vn.nganluong.naba.entities.Channel;

public interface ChannelDao {
	public Channel findByName(String s);
	
	public Channel findByCode(String code);
}