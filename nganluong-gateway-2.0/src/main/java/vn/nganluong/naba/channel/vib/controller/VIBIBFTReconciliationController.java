package vn.nganluong.naba.channel.vib.controller;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.nganluong.naba.channel.vib.dto.VIBConst;
import vn.nganluong.naba.channel.vib.service.ReconciliationService;
import vn.nganluong.naba.dto.ResponseJson;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.CommonResponseService;

@RestController
@RequestMapping(value = "/vib/ibft")
public class VIBIBFTReconciliationController {

	private static final Logger logger = LogManager.getLogger(VIBIBFTReconciliationController.class);

	@Autowired
	private ReconciliationService reconciliationService;

	@Autowired
	private CommonResponseService commonResponseService;

	@Autowired
	private CommonLogService commonLogService;

	public VIBIBFTReconciliationController() {

	}

	@GetMapping(path = { "/reconciliation_day", "/reconciliation_day/{fromDate}/{toDate}" })
	ResponseEntity<?> reconciliationDay(@PathVariable(required = false) String fromDate,
			@PathVariable(required = false) String toDate) throws KeyManagementException, NoSuchAlgorithmException {

		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE,
				VIBIBFTController.SERVICE_NAME, VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_DAY, true));
		boolean resultRecon = false;
		try {
			if (StringUtils.isAnyEmpty(fromDate, toDate)) {
				resultRecon = reconciliationService.doDayReconciliationVIBIBFT();
			} else {
				String[] dates = new String[] { fromDate, toDate };
				resultRecon = reconciliationService.doDayReconciliationVIBIBFT(dates);
			}


			ResponseJson responseJson = null;
			if (resultRecon){
				responseJson = commonResponseService.returnGatewayRequestSuccessPrefix();
			}
			else {
				responseJson = commonResponseService.returnReconciliationGatewayFail();
			}

			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE,
					VIBIBFTController.SERVICE_NAME, VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_DAY, false));

			return new ResponseEntity<ResponseJson>(responseJson, HttpStatus.OK);
		} catch (Exception e) {

			String[] paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBIBFTController.SERVICE_NAME,
					VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_DAY, false, false, true, paramsLog));

			return commonResponseService.returnBadGateway();
		}

	}

	@GetMapping(path = { "/reconciliation_month", "/reconciliation_month/{month}" })
	ResponseEntity<?> reconciliationMonth(@PathVariable(required = false) String month)
			throws KeyManagementException, NoSuchAlgorithmException {

		logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE,
				VIBIBFTController.SERVICE_NAME, VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_MONTH, true));

		try {

			if (StringUtils.isNoneBlank(month)) {
				try {
					DateUtils.parseDate(month + "01", VIBConst.VIB_IBFT_PARAMS_FORMAT_DATE);
				} catch (ParseException pe) {
					String[] paramsLog = new String[] { "parameter `Month` invalid" };
					logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBIBFTController.SERVICE_NAME,
							VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_MONTH, false, false, true, paramsLog));
					return commonResponseService.returnBadGateway();
				}
			}

			reconciliationService.doMonthReconciliationVIBIBFT(month);

			logger.info(commonLogService.createContentLogStartEndFunction(VIBConst.CHANNEL_CODE,
					VIBIBFTController.SERVICE_NAME, VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_MONTH, false));
			ResponseJson responseJson = commonResponseService.returnGatewayRequestSuccessPrefix();
			return new ResponseEntity<ResponseJson>(responseJson, HttpStatus.OK);
		} catch (Exception e) {

			String[] paramsLog = new String[] { e.getMessage() };
			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBIBFTController.SERVICE_NAME,
					VIBConst.FUNCTION_CODE_IBFT_RECONCILIATION_MONTH, false, false, true, paramsLog));
			return commonResponseService.returnBadGateway();
		}

	}

}
