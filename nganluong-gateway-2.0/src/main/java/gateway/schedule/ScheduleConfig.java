package gateway.schedule;

import gateway.schedule.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import gateway.core.channel.mb.dto.MBConst;
import vn.nganluong.naba.channel.vib.dto.VIBConst;

@Configuration
@EnableScheduling
public class ScheduleConfig {

	@Autowired
	private ScheduleService scheduleService;

	@Bean
	public String getCronValueOfVIBIBFTReconciliation() {

		return scheduleService.findCronByChannelCodeAndPgFunctionCode(VIBConst.CHANNEL_CODE,
				VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_DAY);
	}

	@Bean
	public String getCronValueOfMBEcomDayReconciliation() {

		return scheduleService.findCronByChannelCodeAndPgFunctionCode(MBConst.CHANNEL_CODE,
				MBConst.FUNCTION_CODE_RECONCILIATION_DAY_STEP_1);
	}
//		@Bean
//	    public ScheduleService schedule() {
//	        return new ScheduleService();
//	    }
	
	// https://stackoverflow.com/a/45659602
	
	/*
	@EnableScheduling
	@Configuration
	class SchedulingConfiguration implements SchedulingConfigurer {
	    private final Logger logger = LogManager.getLogger(getClass());
	    private final ThreadPoolTaskScheduler taskScheduler;
	
	    SchedulingConfiguration() {
	        taskScheduler = new ThreadPoolTaskScheduler();
	        taskScheduler.setErrorHandler(t -> logger.error("Exception in @Scheduled task. ", t));
	        taskScheduler.setThreadNamePrefix("@scheduled-");
	
	        taskScheduler.initialize();
	    }
	
	    @Override
	    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
	        taskRegistrar.setScheduler(taskScheduler);
	    }
	}
	 */
}
