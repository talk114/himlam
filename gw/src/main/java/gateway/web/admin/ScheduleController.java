package gateway.web.admin;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.nganluong.naba.dto.ScheduleDto;
import vn.nganluong.naba.dto.ScheduleSearchDto;
import vn.nganluong.naba.service.ChannelService;
import vn.nganluong.naba.service.CommonLogService;
import gateway.schedule.service.ScheduleService;
import vn.nganluong.naba.utils.PaginationResult;

@Controller
@RequestMapping(value = { "admin/schedule" })
public class ScheduleController {

	private static final Logger logger = LogManager.getLogger(ScheduleController.class);

	private static final String SERVICE_NAME = "ADMIN_COMMON";

	private static final String FUNCTION_CODE_SCHEDULE_SEARCH = "ADMIN_SCHEDULE";

	@Autowired
	private ScheduleService scheduleService;

	@Autowired
	private CommonLogService commonLogService;

	@Autowired
	private ChannelService channelService;

	@RequestMapping(value = { "index", "search", "" }, method = RequestMethod.GET)
	public String dashboard(Model model, @ModelAttribute ScheduleSearchDto scheduleSearchDto, Integer page) {
		logger.info("Start admin schedule");

		PaginationResult<ScheduleDto> pageData = scheduleService.listSchedule(scheduleSearchDto);

		List<ScheduleDto> schedules = pageData.getList();

		model.addAttribute("scheduleSearchDto", scheduleSearchDto);
		model.addAttribute("schedules", schedules);
		model.addAttribute("paginationProducts", pageData);

		return "admin.schedule.index";
	}

	@ModelAttribute("channelList")
	public Map<String, String> getEntitiesList() {
		return channelService.getMapChannelList(channelService);
	}

	@RequestMapping(value = "search", method = RequestMethod.POST)
	public String searchScheduleHistory(Model model, @ModelAttribute ScheduleSearchDto scheduleSearchDto) {

		logger.info("Start admin schedule");

		PaginationResult<ScheduleDto> pageData = scheduleService.listSchedule(scheduleSearchDto);

		List<ScheduleDto> schedule = pageData.getList();

		model.addAttribute("scheduleSearchDto", scheduleSearchDto);
		model.addAttribute("schedules", schedule);
		model.addAttribute("paginationProducts", pageData);

		String[] paramsLog = new String[] { scheduleSearchDto.getChannelId(), scheduleSearchDto.getCron_expression() };
		logger.info(commonLogService.createContentLog(null, SERVICE_NAME, FUNCTION_CODE_SCHEDULE_SEARCH, true, true,
				false, paramsLog));

		return "admin.schedule.index";
	}
//
//	@RequestMapping(value = "detail/{id}", method = RequestMethod.GET, produces = "application/json")
//	@ResponseBody
//	public ScheduleDto getDetailSchedule(@PathVariable("id") String id) {
//
//		return scheduleService.findDetailScheduleById(NumberUtils.createInteger(id));
//	}
}
