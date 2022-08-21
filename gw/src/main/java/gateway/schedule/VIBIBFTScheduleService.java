//package gateway.schedule;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import vn.nganluong.naba.channel.vib.service.ReconciliationService;
//
//@Component
//public class VIBIBFTScheduleService {
//
//	@Autowired
//	ReconciliationService reconciliationService;
//
//	@Scheduled(cron = "#{@getCronValueOfVIBIBFTReconciliation}")
//	public void scheduleDayReconciliation() {
//		try {
//			System.out.println(">>>>>>>>> VIB Schedule RECONCILIATION");
////			reconciliationService.doReconciliationVIBIBFT();
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
