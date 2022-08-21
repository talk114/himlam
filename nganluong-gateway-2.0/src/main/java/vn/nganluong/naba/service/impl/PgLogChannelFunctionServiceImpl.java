package vn.nganluong.naba.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.nganluong.naba.dao.ChannelDao;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.ChannelFunction;
import vn.nganluong.naba.entities.PgLogChannelFunction;
import vn.nganluong.naba.repository.ChannelFunctionRepository;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.repository.PgLogChannelFunctionRepository;
import vn.nganluong.naba.service.PgLogChannelFunctionService;

@Service
@Transactional
public class PgLogChannelFunctionServiceImpl implements PgLogChannelFunctionService {

	@Autowired
	private ChannelRepository channelRepository;

//	@Autowired
//	private ChannelDao channelDao;

	@Autowired
	private ChannelFunctionRepository channelFunctionRepository;

	@Autowired
	private PgLogChannelFunctionRepository pgLogChannelFunctionRepository;

	@Override
	public void writeLogChannelFunction(String channelCode, String channelFunctionCode, boolean isSuccess) {

		Channel channel = channelRepository.findByCode(channelCode);
		if (channel != null) {
			ChannelFunction channelFunction = channelFunctionRepository.findByCodeAndChannelId(channelFunctionCode,
					channel.getId());

			if (channelFunction != null) {
				PgLogChannelFunction pgLogChannelFunction = pgLogChannelFunctionRepository
						.findByChannelFunctionId(channelFunction.getId());

				if (pgLogChannelFunction != null) {

					if (isSuccess) {
						pgLogChannelFunction.setRequestSuccess(pgLogChannelFunction.getRequestSuccess() + 1);
					} else {
						pgLogChannelFunction.setRequestFail(pgLogChannelFunction.getRequestFail() + 1);
					}
					pgLogChannelFunction.setRequestTotal(pgLogChannelFunction.getRequestTotal() + 1);

					pgLogChannelFunctionRepository.save(pgLogChannelFunction);
				} else {
					PgLogChannelFunction newPgLogChannelFunction = new PgLogChannelFunction();
					newPgLogChannelFunction.setChannelFunction(channelFunction);
					newPgLogChannelFunction.setRequestTotal(1);
					if (isSuccess) {
						newPgLogChannelFunction.setRequestSuccess(1);
					} else {
						newPgLogChannelFunction.setRequestFail(1);
					}
					pgLogChannelFunctionRepository.save(newPgLogChannelFunction);
				}

			}
		}

	}

}