//package gateway.schedule;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import gateway.core.channel.mb.dto.MBEcomDayReconciliateReq;
//import gateway.core.dto.PGRequest;
//import gateway.reconciliation.service.MBReconciliationService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import vn.nganluong.naba.channel.vib.service.ReconciliationService;
//
//@Component
//public class MBScheduleService {
//
//	@Autowired
//	private MBReconciliationService mbReconciliationService;
//
//	@Scheduled(cron = "#{@getCronValueOfMBEcomDayReconciliation}")
//	public void scheduleDayReconciliation() {
//		try {
//
//			MBEcomDayReconciliateReq req = new MBEcomDayReconciliateReq();
//			ObjectMapper mapper = new ObjectMapper();
//			PGRequest pGRequest  = new PGRequest();
//			pGRequest.setData(mapper.writeValueAsString(req));
//			mbReconciliationService.doDayReconciliationMBEcomStep1(pGRequest);
//			System.out.println(">>>>>>>>> MB Schedule Day RECONCILIATION");
//		} catch (Exception e) {
//			logger.info(ExceptionUtils.getStackTrace(e));
//		}
//	}
//
//	// getCronValueOfMBEcomDayReconciliation
//	
//
//	@ExceptionHandler
//	public void handle(Exception e) {
//		logger.info(ExceptionUtils.getStackTrace(e));
//	}
//}
