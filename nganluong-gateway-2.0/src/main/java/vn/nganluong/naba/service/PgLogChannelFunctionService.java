package vn.nganluong.naba.service;

public interface PgLogChannelFunctionService {

	void writeLogChannelFunction(String channelCode, String channelFunctionCode, boolean isSuccess);

}