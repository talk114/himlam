package gateway.schedule.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import vn.nganluong.naba.dao.ScheduleDao;
import vn.nganluong.naba.dto.ScheduleDto;
import vn.nganluong.naba.dto.ScheduleSearchDto;
import vn.nganluong.naba.entities.Channel;
import vn.nganluong.naba.entities.PgFunction;
import vn.nganluong.naba.entities.PgSchedule;
import vn.nganluong.naba.repository.ChannelRepository;
import vn.nganluong.naba.repository.PgFunctionRepository;
import vn.nganluong.naba.repository.PgScheduleRepository;
import gateway.schedule.service.ScheduleService;
import vn.nganluong.naba.service.PgFunctionService;
import vn.nganluong.naba.utils.PaginationResult;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleDao scheduleDao;

	@Autowired
	private PgScheduleRepository pgScheduleRepository;

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private PgFunctionService pgFunctionService;

	@Override
	public PgSchedule findByChannelIdAndPgFunctionId(int channelId, Integer pgFunctionId) {
		return pgScheduleRepository.findFirstByChannelIdAndPgFunctionIdAndStatus(channelId, pgFunctionId, true);
	}

	@Override
	public String findCronByChannelIdAndPgFunctionId(int channelId, Integer pgFunctionId) {
		PgSchedule schedule = findByChannelIdAndPgFunctionId(channelId, pgFunctionId);
		if (schedule != null) {
			String cronExpression = schedule.getCronExpression();
			if (CronSequenceGenerator.isValidExpression(cronExpression)) {
				return cronExpression;
				// return "0 * 0 ? * * ";
				// return "0 0 0 25 12 ? ";
				// return "*/5 * * * * * ";
			}
		}
		// return "0 0 0 25 12 ?";
		return "0 0 0 25 12 ? ";
	}

	@Override
	public String findCronByChannelCodeAndPgFunctionCode(String channelCode, String pgFunctionCode) {

		Channel channel = channelRepository.findByCode(channelCode);
		PgFunction pgFunction = pgFunctionService.findByCodeAndChannelCode(pgFunctionCode, channelCode);
		if (channel != null && pgFunction != null){
			return findCronByChannelIdAndPgFunctionId(channel.getId(), pgFunction.getId());
		}
		return "0 0 0 25 12 ? ";
	}

	@Override
	public PaginationResult<ScheduleDto> listSchedule(ScheduleSearchDto scheduleSearchDto) {
		return scheduleDao.listSchedule(scheduleSearchDto);
	}

	@Override
	public ScheduleDto findDetailScheduleById(Integer id) {
		return scheduleDao.findDetailScheduleById(id);
	}

}
