package vn.nganluong.naba.dao;

import vn.nganluong.naba.dto.ScheduleDto;
import vn.nganluong.naba.dto.ScheduleSearchDto;
import vn.nganluong.naba.utils.PaginationResult;

public interface ScheduleDao {
	
	public PaginationResult<ScheduleDto> listSchedule(ScheduleSearchDto scheduleSearchDto);
	
	public ScheduleDto findDetailScheduleById(Integer id);

}