package gateway.schedule.service;

import vn.nganluong.naba.dto.ScheduleDto;
import vn.nganluong.naba.dto.ScheduleSearchDto;
import vn.nganluong.naba.entities.PgSchedule;
import vn.nganluong.naba.utils.PaginationResult;

public interface ScheduleService {

	PgSchedule findByChannelIdAndPgFunctionId(int channelId, Integer pgFunctionId);

	String findCronByChannelIdAndPgFunctionId(int channelId, Integer pgFunctionId);

	String findCronByChannelCodeAndPgFunctionCode(String channelCode, String pgFunctionCode);

	public PaginationResult<ScheduleDto> listSchedule(ScheduleSearchDto scheduleSearchDto);

	public ScheduleDto findDetailScheduleById(Integer id);
}
