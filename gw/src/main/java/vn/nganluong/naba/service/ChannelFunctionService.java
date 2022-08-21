package vn.nganluong.naba.service;

import vn.nganluong.naba.entities.ChannelFunction;

public interface ChannelFunctionService {

	ChannelFunction findChannelFunctionByCodeAndChannelCode(String code, String channelCode);

	ChannelFunction findChannelFunctionByCodeAndChannelId(String code, Integer channelId);

	ChannelFunction findChannelFunctionByName(String name);

}