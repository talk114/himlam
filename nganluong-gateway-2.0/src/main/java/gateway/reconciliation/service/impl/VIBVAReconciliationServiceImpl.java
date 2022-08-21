//package gateway.reconciliation.service.impl;
//
//import gateway.core.channel.PaymentGate;
//import gateway.core.dto.PGRequest;
//import gateway.core.dto.PGResponse;
//import gateway.reconciliation.dto.DayReconciliationReq;
//import gateway.reconciliation.dto.MonthReconciliationReq;
//import gateway.reconciliation.service.VIBVAReconciliationService;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.apache.commons.lang3.time.DateUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import vn.nganluong.naba.channel.vib.dto.ReconciliationMonthDataCompareDTO;
//import vn.nganluong.naba.channel.vib.dto.VIBConst;
//import vn.nganluong.naba.channel.vib.dto.VIBERPReconciliationDayDataCompareDTO;
//import vn.nganluong.naba.dto.PaymentConst;
//import vn.nganluong.naba.dto.PaymentConst.EnumPaymentType;
//import vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO;
//import vn.nganluong.naba.entities.Channel;
//import vn.nganluong.naba.entities.ChannelFunction;
//import vn.nganluong.naba.entities.PgFunction;
//import vn.nganluong.naba.entities.PgSftp;
//import vn.nganluong.naba.repository.ChannelRepository;
//import vn.nganluong.naba.repository.PaymentRepository;
//import vn.nganluong.naba.repository.PgFunctionRepository;
//import vn.nganluong.naba.repository.PgSftpRepository;
//import vn.nganluong.naba.service.*;
//import vn.nganluong.naba.utils.HashUtils;
//import vn.nganluong.naba.utils.MyDateUtil;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.MessageFormat;
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class VIBVAReconciliationServiceImpl extends PaymentGate implements VIBVAReconciliationService {
//
//	private static final Logger logger = LogManager.getLogger(VIBVAReconciliationServiceImpl.class);
//	
//	// https://www.journaldev.com/17899/java-simpledateformat-java-date-format
//	@Autowired
//	private ChannelRepository channelRepository;
//
//	@Autowired
//	private PgFunctionRepository pgFunctionRepository;
//
//	@Autowired
//	private SFTPService sFTPService;
//
//	@Autowired
//	private ExcelService excelService;
//
//	@Autowired
//	private FileService fileService;
//	
//	@Autowired
//	private PgSftpRepository pgSftpRepository;
//
//	@Autowired
//	private PaymentRepository paymentRepository;
//
//	@Autowired
//	private MailService mailService;
//	
//	@Autowired
//	private CommonLogService commonLogService;
//
//	@Autowired
//	private CommonPGResponseService commonResponseService;
//
//	@Override
//	public ResponseEntity<PGResponse> doDayReconciliationVIBVA(PGRequest pgRequest) {
//
//		try {
//			DayReconciliationReq dayReconciliationReq = objectMapper
//					.readValue(pgRequest.getData(), DayReconciliationReq.class);
//
//			String[] dates = null;
//			if (!StringUtils.isAnyEmpty(dayReconciliationReq.getFrom_date(), dayReconciliationReq.getTo_date())) {
//				dates = new String[]{dayReconciliationReq.getFrom_date(), dayReconciliationReq.getTo_date()};
//			}
//
////			ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelCode(
////					VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION, VIBConst.CHANNEL_CODE);
//			Channel channel = channelRepository.findFirstByCodeAndStatus(pgRequest.getChannelName(), true);
//			PgFunction pgFunction = pgFunctionRepository
//					.findFirstByCodeAndStatus(VIBConst.FUNCTION_CODE_VA_RECONCILIATION_DAY, true);
//
//			PgSftp pgSftp = pgSftpRepository.findTopByChannelIdAndPgFunctionId(channel.getId(),
//					pgFunction.getId());
//
//			//Date fromDate = null;
//			Date toDate = null;
//			String dateInFileName = null;
//			if (dates != null && dates.length > 1) {
//				// fromDate = DateUtils.parseDate(dates[0], VIBConst.VIB_IBFT_PARAMS_FORMAT_DATE);
//				toDate = DateUtils.parseDate(dates[1], VIBConst.VIB_IBFT_PARAMS_FORMAT_DATE);
//				dateInFileName =  MyDateUtil.toDateString(VIBConst.VIB_IBFT_RECONCILIATION_FILE_NAME_FORMAT_DATE, DateUtils.addDays(toDate, 1));
//			}
//			else {
//				// Date dateYesterday = MyDateUtil.getYesterday();
//				dateInFileName = MyDateUtil.toDateString(VIBConst.VIB_IBFT_RECONCILIATION_FILE_NAME_FORMAT_DATE, new Date());
//			}
//			
//			List<String> filesDownloaded = sFTPService.DownloadFileTSubtract1(pgSftp, dateInFileName);
//			if (filesDownloaded.size() > 0) {
//
//				// Thực hiện đối soát giao dịch ngày
//				doiSoatERPGiaoDichNgay(channel.getId(), pgSftp, filesDownloaded, dateInFileName, dates);
//				return getPgResponseResponseReconSuccess();
//				// Thực hiện đối soát sổ phụ
//				// doiSoatSoPhu(channelFunction, pgSftp, toDate, dateInFileName);
//
//			}
//			else {
//				String[] paramsLog = new String[] { "Cannot download file or file does not exist." };
//				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
//						VIBConst.FUNCTION_CODE_VA_RECONCILIATION_DAY, false, false, true, paramsLog));
//				return commonResponseService.returnBadGatewayWithCause(StringUtils.join(paramsLog));
//			}
//
//		} catch (Exception e) {
//			String[] paramsLog = new String[] {ExceptionUtils.getStackTrace(e) };
//			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
//					VIBConst.FUNCTION_CODE_VA_RECONCILIATION_DAY, false, false, true, paramsLog));
//			return commonResponseService.returnBadGatewayWithCause(StringUtils.join(paramsLog));
//		}
//	}
//
//
//	/**
//	 *
//	 * @param channelId
//	 * @param pgSftp
//	 * @param filesDownloaded
//	 * @param dateInFileName
//	 * @param dates
//	 * @throws IOException
//	 * @throws ParseException
//	 */
//	private void doiSoatERPGiaoDichNgay(Integer channelId, PgSftp pgSftp, List<String> filesDownloaded,
//										String dateInFileName, String... dates)
//			throws IOException, ParseException {
//		
//		
//		Date fromDate = null;
//		// String fromDateString = null;
//		Date toDate = null;
//		// String toDateString = null;
//		
//		if (dates == null || dates.length < 1) {
//			fromDate = MyDateUtil.getYesterday();
//			toDate = MyDateUtil.getYesterdayLastest();
//		} else {
//			fromDate = DateUtils.parseDate(dates[0], VIBConst.VIB_IBFT_PARAMS_FORMAT_DATE);
//			
//			Date toDateTemp = DateUtils.addDays(DateUtils.parseDate(dates[1], VIBConst.VIB_IBFT_PARAMS_FORMAT_DATE), 1);
//			// Todate trừ đi 1ms để trở lại thời điểm cuối ngày trước đó
//			toDate = DateUtils.addMilliseconds(toDateTemp, -1);
//		}
//
////		String dateFileName = new SimpleDateFormat(VIBConst.VIB_IBFT_RECONCILIATION_FILE_NAME_FORMAT_DATE,
////				Locale.ENGLISH).format(toDate);
//
//		String fileName = pgSftp.getLocalDirIn() + StringUtils.split(pgSftp.getLocalSyntaxFileNameIn(), '|')[0].replace("{0}", dateInFileName);
//
//
//		List<ReconciliationDayDataCompareDTO> reconciliationResultList = createReconciliationResultList(
//				channelId, fromDate, toDate, fileName);
//
//		String fileNameReconResultOutput = pgSftp.getLocalDirOut()
//				+ StringUtils.split(pgSftp.getLocalSyntaxFileNameOut(), '|')[0].replace("{0}", dateInFileName);
//		
//		String timeReport = MyDateUtil.toDateString(VIBConst.VIB_IBFT_RECONCILIATION_DATE_FORMAT_DATE, toDate);
//		String fileNameExcelRecon = StringUtils.EMPTY;
//		if (CollectionUtils.isNotEmpty(reconciliationResultList) && createReconDetailOutputFile(fileNameReconResultOutput, reconciliationResultList)) {
//			String fileDst = pgSftp.getRemoteDirOut() + pgSftp.getRemoteSyntaxFileNameOut().replace("{0}", dateInFileName);
//			sFTPService.UploadFile(pgSftp, fileNameReconResultOutput, fileDst);
//			fileNameExcelRecon = StringUtils.replace(fileNameReconResultOutput, ".txt", ".xlsx");
//		}
//
//		String containFileName_SoPhu = StringUtils.split(pgSftp.getLocalSyntaxFileNameIn(), '|')[1];
//		List<String> filesNameConvertedExcel = convertFileSoPhuTxtToExcel(filesDownloaded, containFileName_SoPhu);
//
//		if (StringUtils.isNotEmpty(fileNameExcelRecon)) {
//			filesNameConvertedExcel.add(0, fileNameExcelRecon);
//		}
//
//		if (CollectionUtils.size(filesNameConvertedExcel) > 0){
//
//			String[] filesNameMultipart = (String[]) filesNameConvertedExcel.toArray(new String[0]);
//			mailService.sendMail(MessageFormat.format(pgSftp.getMailSubject(), timeReport), pgSftp.getMailContent(), filesNameMultipart);
//
//		}
//	}
//	
//	public List<String> convertFileSoPhuTxtToExcel(List<String> filesDownloaded, String containName) {
//
//		List<String> filesResult = new ArrayList<String>();
//		for (String fileName : filesDownloaded) {
//
//			if (StringUtils.contains(fileName, containName)) {
//				@SuppressWarnings("unchecked")
//				List<List<String>> listData = (List<List<String>>) fileService.readFileTextToList(fileName,
//						VIBConst.ERP_RECONCILIATION_SEPERATOR_CHAR_IN_FILE);
//
//				String[] columns = { "RECORD_TYPE", "BANKSRCACCTNO", "TRAN_DATE", "TRAN_TIME", "SEQ_NO", "TRAN_TYPE",
//						"TRAN_DESC", "NARRATIVE", "CLIENT_TRANID", "MAKER_ID", "DEBIT_AMOUNT", "CREDIT_AMOUNT",
//						"CLOSING_BAL", "CHECK_SUM" };
//				String fileNameExcel = StringUtils.replace(fileName, ".txt", ".xlsx");
//				if (excelService.writeFileExcelFromNestedList(columns, listData, fileNameExcel)) {
//					filesResult.add(fileNameExcel);
//				}
//			}
//			
//		}
//
//		return filesResult;
//	}
//
//	
//	@Override
//	public ResponseEntity<PGResponse> doMonthReconciliationVIBVA(PGRequest pgRequest) {
//		try {
//
//			MonthReconciliationReq reconciliationReq = objectMapper
//					.readValue(pgRequest.getData(), MonthReconciliationReq.class);
//
//			String month = reconciliationReq.getMonth();
////			ChannelFunction channelFunction = channelFunctionService.findChannelFunctionByCodeAndChannelCode(
////					VIBConst.CHANNEL_FUNCTION_STATUS_TRANSACTION, VIBConst.CHANNEL_CODE);
//			Channel channel = channelRepository.findFirstByCodeAndStatus(pgRequest.getChannelName(), true);
//			PgFunction pgFunction = pgFunctionRepository
//					.findFirstByCodeAndStatus(VIBConst.FUNCTION_CODE_VA_RECONCILIATION_MONTH, true);
//
//			PgSftp pgSftp = pgSftpRepository.findTopByChannelIdAndPgFunctionId(channel.getId(), pgFunction.getId());
//			
//			Date startMonth = null;
//			Date endMonth = null;
//			
//			String monthInFileName = month;
//			if (StringUtils.isBlank(month)) {
//				Date currentMonth = DateUtils.truncate(new Date(), Calendar.MONTH);
//				startMonth = DateUtils.addMonths(currentMonth, -1);
//				endMonth = DateUtils.addMilliseconds(currentMonth, -1);
//				
//				monthInFileName = MyDateUtil.toDateString(VIBConst.VIB_IBFT_PARAMS_YEAR_MONTH_FORMAT_DATE, startMonth);
//				
//			}
//			else {
//				startMonth = DateUtils.truncate(DateUtils.parseDate(month + "01", VIBConst.VIB_IBFT_PARAMS_FORMAT_DATE), Calendar.MONTH);
//				endMonth = DateUtils.addMilliseconds(DateUtils.addMonths(startMonth, 1), -1);
//			}
//			
//			String monthInFileNameDownload = monthInFileName + VIBConst.VIB_IBFT_RECONCILIATION_CHARACTER_AFTER_DATE_IN_FILE_NAME;
//			
//			List<String> filesDownloaded = sFTPService.DownloadFileTSubtract1(pgSftp, monthInFileNameDownload);
//			if (filesDownloaded.size() > 0) {
//
//				// Thực hiện đối soát chi tiết tháng.
//				String detailDiffFileName = doiSoatERPChiTietGiaoDichThang(channel.getId(), pgSftp, startMonth, endMonth, monthInFileName);
//				System.out.println(detailDiffFileName);
//				
//				// Thực hiện đối soát tổng hợp tháng.
////				String summaryDiffFileName = doiSoatERPTongHopGiaoDichThang(channel.getId(), pgSftp, startMonth, endMonth, monthInFileName);
////				System.out.println(summaryDiffFileName);
//
//				String containFileName_SoPhu = StringUtils.split(pgSftp.getLocalSyntaxFileNameIn(), '|')[2];
//				List<String> filesNameConvertedExcel = convertFileSoPhuTxtToExcel(filesDownloaded, containFileName_SoPhu);
//
//				if (StringUtils.isNotBlank(detailDiffFileName)){
//					filesNameConvertedExcel.add(0, StringUtils.replace(detailDiffFileName, ".txt", ".xlsx"));
//				}
//
////				if (StringUtils.isNotBlank(summaryDiffFileName)){
////					filesNameConvertedExcel.add(0, StringUtils.replace(summaryDiffFileName, ".txt", ".xlsx"));
////				}
//
//				String[] filesNameMultipart = (String[]) filesNameConvertedExcel.toArray(new String[0]);
//				
//				String timeReport = MyDateUtil.toDateString(PaymentConst.MONTH_FORMAT_DISPAY, endMonth);
//				mailService.sendMail(MessageFormat.format(pgSftp.getMailSubject(), timeReport), pgSftp.getMailContent() ,filesNameMultipart);
//				return getPgResponseResponseReconSuccess();
//			}
//			else{
//				String[] paramsLog = new String[]{"Cannot download file or file does not exist."};
//				logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
//						VIBConst.FUNCTION_CODE_VA_RECONCILIATION_MONTH, false, false, true, paramsLog));
//				return commonResponseService.returnBadGatewayWithCause(StringUtils.join(paramsLog));
//			}
//
//		} catch (Exception e) {
//			String[] paramsLog = new String[]{ExceptionUtils.getStackTrace(e)};
//			logger.info(commonLogService.createContentLog(VIBConst.CHANNEL_CODE, VIBConst.SERVICE_NAME_IBFT,
//					VIBConst.FUNCTION_CODE_VA_RECONCILIATION_MONTH, false, false, true, paramsLog));
//			return commonResponseService.returnBadGatewayWithCause(StringUtils.join(paramsLog));
//		}
//	}
//	
//	/**
//	 * Đối soát file giao dịch tháng
//	 * Trả về tên file csv đã tạo sau khi đối soát.
//	 * @param channelId
//	 * @param pgSftp
//	 * @param fromDate
//	 * @param toDate
//	 * @param dateFileName
//	 * @return File csv created
//	 * @throws IOException
//	 */
//	private String doiSoatERPChiTietGiaoDichThang(Integer channelId, PgSftp pgSftp, Date fromDate, Date toDate, String dateFileName)
//			throws IOException {
//		
//		
//		String fileName = pgSftp.getLocalDirIn() + StringUtils.split(pgSftp.getLocalSyntaxFileNameIn(), '|')[1].replace("{0}", dateFileName);
//
//		List<ReconciliationDayDataCompareDTO> differencePayment = createReconciliationResultList(
//				channelId, fromDate, toDate, fileName);
//
//		String fileNameReconResultOutput = pgSftp.getLocalDirOut()
//				+ StringUtils.split(pgSftp.getLocalSyntaxFileNameOut(), '|')[1].replace("{0}", dateFileName);
//		
//		if (CollectionUtils.isNotEmpty(differencePayment) && createReconDetailOutputFile(fileNameReconResultOutput, differencePayment)) {
//			String fileDst = pgSftp.getRemoteDirOut() + StringUtils.split(pgSftp.getRemoteSyntaxFileNameOut(), '|')[1].replace("{0}", dateFileName);
//			sFTPService.UploadFile(pgSftp, fileNameReconResultOutput, fileDst);
//
//			return fileNameReconResultOutput;
//		}
//		else {
//			return StringUtils.EMPTY;
//		}
//	}
//
//	/**
//	 *
//	 * Đối soát file giao dịch tháng
//	 * Trả về tên file csv đã tạo sau khi đối soát.
//	 * @param channelId
//	 * @param pgSftp
//	 * @param fromDate
//	 * @param toDate
//	 * @param dateFileName
//	 * @return
//	 * @throws IOException
//	 */
//	private String doiSoatERPTongHopGiaoDichThang(Integer channelId, PgSftp pgSftp, Date fromDate, Date toDate, String dateFileName)
//			throws IOException {
//		
//		
//		String fileName = pgSftp.getLocalDirIn() + StringUtils.split(pgSftp.getLocalSyntaxFileNameIn(), '|')[0].replace("{0}", dateFileName);
//
//		List<ReconciliationMonthDataCompareDTO> differencePayment = createReconciliationMonthResultList(
//				channelId, fromDate, toDate, fileName);
//
//		String fileNameReconMonthSummaryOutput = pgSftp.getLocalDirOut()
//				+ StringUtils.split(pgSftp.getLocalSyntaxFileNameOut(), '|')[0].replace("{0}", dateFileName);
//		
//		if (CollectionUtils.size(differencePayment) > 0 && createReconMonthOuputFile(fileNameReconMonthSummaryOutput, differencePayment)) {
//			String fileDst = pgSftp.getRemoteDirOut() + StringUtils.split(pgSftp.getRemoteSyntaxFileNameOut(), '|')[0].replace("{0}", dateFileName);
//			sFTPService.UploadFile(pgSftp, fileNameReconMonthSummaryOutput, fileDst);
//			return fileNameReconMonthSummaryOutput;
//		}
//		else {
//			return StringUtils.EMPTY;
//		}
//	}
//
//	/**
//	 *
//	 * @param channelId
//	 * @param fromDate
//	 * @param toDate
//	 * @param fileName
//	 * @return
//	 */
//	private List<ReconciliationDayDataCompareDTO> createReconciliationResultList(Integer channelId,
//			Date fromDate, Date toDate, String fileName) {
//		
//		List<ReconciliationDayDataCompareDTO> paymentsVIB = new ArrayList<ReconciliationDayDataCompareDTO>();
//		
//		@SuppressWarnings("unchecked")
//		List<List<String>> listData = (List<List<String>>) fileService
//				.readFileTextToList(fileName, VIBConst.ERP_RECONCILIATION_SEPERATOR_CHAR_IN_FILE);
//
//		if (CollectionUtils.size(listData) < 1){
//			return paymentsVIB;
//		}
//
//		// DateUtils.addHours(date, amount)
//		for (List<String> row : listData) {
//
//			if (!StringUtils.equals(row.get(0), VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY)
//					|| !StringUtils.equals(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_CODE_SUCCESS, row.get(1))) {
//				continue;
//			}
//			
//			// Checksum:
//			String[] fieldsToHas = new String[] {row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5),
//					row.get(6),row.get(7), row.get(8), row.get(9), row.get(10), row.get(11), row.get(12), row.get(13), row.get(14)};
//			String dataToHas = StringUtils.join(fieldsToHas);
//			
//
//			if (!StringUtils.equals(HashUtils.hashingMD5(dataToHas), row.get(15))) {
//				System.out.println("Fail checksum");
//				// TODO Uncomment if Live
////				continue;
//			}
//
//			// Check time
//			Date tranTime = MyDateUtil.parseDateFormat(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_FORMAT_TIME
//					+ VIBConst.ERP_RECONCILIATION_FILE_STRUCT_FORMAT_DATE, row.get(8) + row.get(9));
//			
//			if (!(tranTime.compareTo(fromDate) >= 0 && tranTime.compareTo(toDate) <= 0)) {
//				// || !StringUtils.equalsAny(StringUtils.trim(row.get(10)), PaymentConst.EnumBankStatus.SUCCEEDED.status(),
//				// PaymentConst.EnumBankStatus.COMPLETED.status())
//
//				System.out.println("Out of date");
//				continue;
//			}
//
//			ReconciliationDayDataCompareDTO dataRecord = new ReconciliationDayDataCompareDTO();
//
//			dataRecord.setRecordType(row.get(0)); // Loại bản ghi
//			dataRecord.setReconCode(row.get(1)); // Mã đối soát
//			dataRecord.setTranType(row.get(2)); // Loại GD
//			dataRecord.setFromAcct(row.get(3)); // TK chuyển
//			dataRecord.setVirtualAccountNo(row.get(4)); // TK nhận
//			dataRecord.setTranAmtString(row.get(5));
//			if (NumberUtils.isParsable(row.get(5))) {
//				dataRecord.setTranAmt(NumberUtils.createBigDecimal(row.get(5)));
//			}
//			dataRecord.setCcy(row.get(6));
//			dataRecord.setDescription(row.get(7));
//			dataRecord.setTranHourString(row.get(8));
//			dataRecord.setTranDateString(row.get(9));
//			dataRecord.setTranTime(tranTime);
//			
//			dataRecord.setTranSequence(row.get(10));
//			dataRecord.setClientTranId(row.get(11));
//			
//			dataRecord.setTranFeeString(row.get(12));
//			if (NumberUtils.isParsable(row.get(12))) {
//				dataRecord.setTranFee(NumberUtils.createBigDecimal(row.get(12)));
//			}
//			
//			dataRecord.setNapasResult(row.get(13));
//			dataRecord.setReconResult(row.get(14));
//			dataRecord.setChecksum(row.get(15));
//
//			paymentsVIB.add(dataRecord);
//		}
//
//		List<ReconciliationDayDataCompareDTO> paymentsPg = paymentRepository.findByChannelAndDateAndPaymentTypeVA(
//				channelId, EnumPaymentType.VIRTUAL_ACCOUNT_NO.code(),
//				PaymentConst.EnumBankStatus.SUCCEEDED.code(), PaymentConst.EnumBankStatus.SUCCEEDED.code(),
//				fromDate, toDate);
//		
//		for (ReconciliationDayDataCompareDTO payment : paymentsPg) {
//			payment.setTranTimeString(MyDateUtil.toDateString(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_FORMAT_TIME + " "
//					+ VIBConst.ERP_RECONCILIATION_FILE_STRUCT_FORMAT_DATE, payment.getTranTime()));
//			if (StringUtils.isNotBlank(payment.getTranTimeString())) {
//				String[] timePart = payment.getTranTimeString().split(" ");
//				payment.setTranHourString(timePart[0]);
//				payment.setTranDateString(timePart[1]);
//			}
//			payment.setCcy(VIBConst.CCY);
//
//		}
//		
//
//		// Compare two list
//		// 01 – Có Ngân Lượng, không có tại VIB
////		List<ReconciliationDayDataCompareDTO> pgDifference = paymentsPg.stream()
////				.filter(o1 -> paymentsVIB.stream()
////						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())
////								&& o2.getTranAmt().compareTo(o1.getTranAmt()) == 0))
////				.collect(Collectors.toList());
//		
//		List<ReconciliationDayDataCompareDTO> pgResidual = paymentsPg.stream()
//				.filter(o1 -> paymentsVIB.stream()
//						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())))
//				.collect(Collectors.toList());
//		// Cập nhật trạng thái 01
//		if (CollectionUtils.isNotEmpty(pgResidual)) {
//			for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : pgResidual) {
//
//				// set format tiền lấy từ DB, loại bỏ ".00" ở sau
//				reconciliationDayDataCompareDTO.setTranAmtString(reconciliationDayDataCompareDTO.getTranAmt().toPlainString().replace(".00", ""));
//
//				reconciliationDayDataCompareDTO.setReconResult(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_NL);
//				reconciliationDayDataCompareDTO.setRecordType(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY);
//				reconciliationDayDataCompareDTO.setReconCode(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_CODE_SUCCESS);
//			}
//		}
//		
//		
//		// 02 – Có VIB, không có Ngân Lượng
////		List<VIBERPReconciliationDayDataCompareDTO> channelDifference = paymentsVIB.stream()
////				.filter(o1 -> paymentsPg.stream()
////						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())
////								&& o2.getTranAmt().compareTo(o1.getTranAmt()) == 0))
////				.collect(Collectors.toList());
//		
//		List<ReconciliationDayDataCompareDTO> channelResidual = paymentsVIB.stream()
//				.filter(o1 -> paymentsPg.stream()
//						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())))
//				.collect(Collectors.toList());
//		// Cập nhật trạng thái 02
//		if (CollectionUtils.isNotEmpty(channelResidual)) {
//			for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : channelResidual) {
//				reconciliationDayDataCompareDTO.setReconResult(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_VIB);
//			}
//		}
//
//		// 03 – Sai lệch số liệu
//		// Sai lệch từ PG hoặc Channel
//		List<ReconciliationDayDataCompareDTO> differenceValue = paymentsVIB.stream()
//				.filter(o1 -> paymentsPg.stream()
//						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())
//								&& o2.getTranAmt().compareTo(o1.getTranAmt()) == 0
//								&& StringUtils.equals(o2.getVirtualAccountNo(), o1.getVirtualAccountNo())))
//				.filter(o1 -> pgResidual.stream().noneMatch(o3 -> o3.getClientTranId().equals(o1.getClientTranId()))) // Loại bỏ bản ghi trùng từ (01)
//				.filter(o1 -> channelResidual.stream().noneMatch(o4 -> o4.getClientTranId().equals(o1.getClientTranId()))) // Loại bỏ bản ghi trùng từ (02)
//				.collect(Collectors.toList());
//		
//		// Cập nhật trạng thái 03
//		if (CollectionUtils.isNotEmpty(differenceValue)) {
//			for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : differenceValue) {
//				reconciliationDayDataCompareDTO.setReconResult(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_VALUE);
//			}
//		}
//		
//		// Sai lệch từ Channel
////		List<VIBERPReconciliationDayDataCompareDTO> channelDifference = paymentsVIB.stream()
////				.filter(o1 -> paymentsPg.stream()
////						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())
////								&& o2.getTranAmt().compareTo(o1.getTranAmt()) == 0
////								&& StringUtils.equals(o2.getVirtualAccountNo(), o1.getVirtualAccountNo())))
////				.filter(o1 -> channelResidual.stream().noneMatch(o3 -> o3.getClientTranId().equals(o1.getClientTranId()))) // Loại bỏ bản ghi trùng
////				.collect(Collectors.toList());
//		
//
//		List<ReconciliationDayDataCompareDTO> differencePayment = new ArrayList<ReconciliationDayDataCompareDTO>();
//		
//		if (CollectionUtils.isNotEmpty(pgResidual)) {
//			differencePayment.addAll(pgResidual);
//		}
//		
//		if (CollectionUtils.isNotEmpty(channelResidual)) {
//			differencePayment.addAll(channelResidual);
//		}
//		
//		if (CollectionUtils.isNotEmpty(differenceValue)) {
//			 differencePayment.addAll(differenceValue);
//		}
//
//		// Danh sách tổng hợp
//		// (00) Tổng hợp lại các bản ghi trùng khớp đối soát (Kết quả đối soát: 00)	
//		
//		List<ReconciliationDayDataCompareDTO> reconResultList = paymentsVIB.stream()
//				.filter(o1 -> differencePayment.stream()
//						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())))
//				.collect(Collectors.toList());
//		
//		// Cập nhật trạng thái 00
//		for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : reconResultList) {
//			reconciliationDayDataCompareDTO.setReconResult(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_EQUAL);
//		}
//
//		// Thêm bản ghi sai lệch vào danh sách tổng hợp (01,02,03)   
//		reconResultList.addAll(differencePayment);
//		
//		// Thêm header, footer:
//		if (CollectionUtils.size(listData) > 1) {
//			int countTrans = CollectionUtils.size(reconResultList);
//			
//			List<String> header = listData.get(0);
//			ReconciliationDayDataCompareDTO headerRecon = new ReconciliationDayDataCompareDTO();
//			headerRecon.setRecordType(header.get(0));
//			headerRecon.setBinBank(header.get(1));
//			headerRecon.setTranDateString(header.get(2));
//			reconResultList.add(0, headerRecon);
//			
//			
//			List<String> footer = listData.get(listData.size() - 1);
//			ReconciliationDayDataCompareDTO footerRecon = new ReconciliationDayDataCompareDTO();
//			footerRecon.setRecordType(footer.get(0));
//			footerRecon.setTotalRecord(StringUtils.leftPad(countTrans + "", 9, '0'));
//			footerRecon.setCreator(StringUtils.leftPad(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_CREATOR, 20, ' '));
//			
//		
//			String[] timeReport = MyDateUtil.toDateString(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_FORMAT_DATE + " " + VIBConst.ERP_RECONCILIATION_FILE_STRUCT_FORMAT_TIME,
//					new Date()).split(" ");
//			
//			footerRecon.setTranHourString(timeReport[1]);
//			footerRecon.setTranDateString(timeReport[0]);
//			
//			footerRecon.setChecksum(HashUtils.hashingMD5(footerRecon.getRecordType() 
//					+ footerRecon.getTotalRecord() + footerRecon.getCreator() 
//					+ footerRecon.getTranHourString() + footerRecon.getTranDateString()));
//			
//			reconResultList.add(footerRecon);
//			
//		}
//		
//		return reconResultList;
//	}
//	
//	
//	private List<ReconciliationMonthDataCompareDTO> createReconciliationMonthResultList(Integer channelId, Date fromDate,
//			Date toDate, String fileName) {
//
//		List<ReconciliationMonthDataCompareDTO> paymentsVIB = new ArrayList<ReconciliationMonthDataCompareDTO>();
//
//		@SuppressWarnings("unchecked")
//		List<List<String>> listData = (List<List<String>>) fileService.readFileTextToList(fileName,
//				VIBConst.ERP_RECONCILIATION_SEPERATOR_CHAR_IN_FILE);
//
//		// DateUtils.addHours(date, amount)
//		for (List<String> row : listData) {
//
//			if (!StringUtils.equals(row.get(0), VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY)) {
//				continue;
//			}
//
//			// Checksum:
//			String[] fieldsToHas = new String[] { row.get(0), row.get(1), row.get(2), row.get(3), row.get(4),
//					row.get(5), row.get(6), row.get(7) };
//			String dataToHas = StringUtils.join(fieldsToHas);
//
//			if (!StringUtils.equals(HashUtils.hashingMD5(dataToHas), row.get(8))) {
//
//				System.out.println("Fail checksum in createReconciliationMonthResultList");
//				// TODO uncomment if live
////				continue;
//			}
//
//			Date fromTranTime = MyDateUtil.parseDateFormat(VIBConst.ERP_RECONCILIATION_MONTH_FILE_STRUCT_FORMAT_DATE,
//					row.get(3));
//			Date toTranTime = MyDateUtil.parseDateFormat(VIBConst.ERP_RECONCILIATION_MONTH_FILE_STRUCT_FORMAT_DATE,
//					row.get(4));
//
//			if (!(fromTranTime.compareTo(fromDate) >= 0 && fromTranTime.compareTo(toDate) <= 0)
//					|| !(toTranTime.compareTo(fromDate) >= 0 && toTranTime.compareTo(toDate) <= 0)) {
//				System.out.println("Out of date in createReconciliationMonthResultList");
//				continue;
//			}
//
//			ReconciliationMonthDataCompareDTO dataRecord = new ReconciliationMonthDataCompareDTO();
//
//			dataRecord.setRecordType(row.get(0));
//			dataRecord.setSourceAcct(row.get(1));
//			dataRecord.setTranType(row.get(2));
//			dataRecord.setFromDateString(row.get(3));
//			dataRecord.setToDateString(row.get(4));
//			dataRecord.setChannelTotalAmt(row.get(5));
//			dataRecord.setChannelTotalFee(row.get(6));
//			dataRecord.setChannelTotalItems(row.get(7));
//
//			paymentsVIB.add(dataRecord);
//		}
//
//		if (CollectionUtils.size(paymentsVIB) < 1){
//			return paymentsVIB;
//		}
//
//		List<ReconciliationMonthDataCompareDTO> paymentsPg = paymentRepository
//				.getSummaryPaymentByChannelAndDateAndPaymentType(channelId, EnumPaymentType.VIRTUAL_ACCOUNT_NO.code(),
//						PaymentConst.EnumBankStatus.SUCCEEDED.code(), PaymentConst.EnumBankStatus.SUCCEEDED.code(),
//						fromDate, toDate);
//
//		if (CollectionUtils.isNotEmpty(paymentsPg) && CollectionUtils.isNotEmpty(paymentsPg)) {
//			for (ReconciliationMonthDataCompareDTO record : paymentsVIB) {
//
//				ReconciliationMonthDataCompareDTO pgUpdate = paymentsPg.stream()
//						.filter(p -> p.getTranType().equals(record.getTranType())).findFirst().orElse(null);
//
//				if (pgUpdate != null) {
//					record.setPgTotalAmtString(StringUtils.removeEnd(pgUpdate.getPgTotalAmt().toString(), ".00") );
//					record.setPgTotalItemsString(pgUpdate.getPgTotalItems() + "");
//					
//					if (NumberUtils.createBigDecimal(record.getChannelTotalAmt()).compareTo(pgUpdate.getPgTotalAmt())  == 0 
//							&& NumberUtils.compare(NumberUtils.toLong(record.getChannelTotalItems()), pgUpdate.getPgTotalItems())==0) {
//						record.setReconResult(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_EQUAL);
//					}
//					else {
//						record.setReconResult(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_VALUE);
//					}
//					
//				}
//
//			}
//
//			// Thêm header, footer:
//			int countTrans = CollectionUtils.size(paymentsVIB);
//
//			List<String> header = listData.get(0);
//			ReconciliationMonthDataCompareDTO headerRecon = new ReconciliationMonthDataCompareDTO();
//			headerRecon.setRecordType(header.get(0));
//			headerRecon.setBinBank(header.get(1));
//			headerRecon.setTranDateString(header.get(2));
//			paymentsVIB.add(0, headerRecon);
//
//			List<String> footer = listData.get(listData.size() - 1);
//			ReconciliationMonthDataCompareDTO footerRecon = new ReconciliationMonthDataCompareDTO();
//			footerRecon.setRecordType(footer.get(0));
//			footerRecon.setTotalRecord(StringUtils.leftPad(countTrans + "", 9, '0'));
//			footerRecon.setCreator(StringUtils.leftPad(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_RECON_CREATOR, 20, ' '));
//
//			String[] timeReport = MyDateUtil.toDateString(VIBConst.ERP_RECONCILIATION_FILE_STRUCT_FORMAT_DATE + " "
//					+ VIBConst.ERP_RECONCILIATION_FILE_STRUCT_FORMAT_TIME, new Date()).split(" ");
//
//			footerRecon.setTranHourString(timeReport[1]);
//			footerRecon.setTranDateString(timeReport[0]);
//
//			footerRecon.setChecksum(HashUtils.hashingMD5(footerRecon.getRecordType() + footerRecon.getTotalRecord()
//					+ footerRecon.getCreator() + footerRecon.getTranHourString() + footerRecon.getTranDateString()));
//
//			paymentsVIB.add(footerRecon);
//
//		}
//
//		return paymentsVIB;
//
//	}
//
//
//	/**
//	 *
//	 * @param channelFunction
//	 * @param pgSftp
//	 * @param dateRc
//	 * @param dateInFileName
//	 * @throws IOException
//	 */
//	private void doiSoatSoPhu(ChannelFunction channelFunction, PgSftp pgSftp, Date dateRc, String dateInFileName)
//			throws IOException {
//		List<VIBERPReconciliationDayDataCompareDTO> paymentsVIB = new ArrayList<VIBERPReconciliationDayDataCompareDTO>();
//		List<VIBERPReconciliationDayDataCompareDTO> paymentsVIB2324hours = new ArrayList<VIBERPReconciliationDayDataCompareDTO>();
//		List<VIBERPReconciliationDayDataCompareDTO> paymentsVIBNonERP = new ArrayList<VIBERPReconciliationDayDataCompareDTO>();
//
//		String fileName = StringUtils.split(pgSftp.getLocalSyntaxFileNameIn(), '|')[1].replace("{0}", dateInFileName);
//
//		@SuppressWarnings("unchecked")
//		List<List<String>> listData = (List<List<String>>) excelService
//				.readFileExcelToList(pgSftp.getLocalDirIn() + fileName);
//
//		for (List<String> row : listData) {
//
//			Date tranTime = MyDateUtil.parseDateFormat(VIBConst.VIB_IBFT_RECONCILIATION_TRAN_TIME_FORMAT_DATE,
//					row.get(0));
//
//			if (!DateUtils.isSameDay(dateRc, tranTime)) {
//				continue;
//			}
//
//			VIBERPReconciliationDayDataCompareDTO dataRecord = new VIBERPReconciliationDayDataCompareDTO();
//
//			dataRecord.setTranTime(tranTime);
//			dataRecord.setTranTimeString(row.get(0));
//			dataRecord.setClientTranId(row.get(1));
//			dataRecord.setTranSequence(row.get(2));
//
//			if (NumberUtils.isParsable(row.get(3))) {
//				dataRecord.setTranAmt(NumberUtils.createBigDecimal(row.get(3)));
//			}
//			dataRecord.setDrAmtString(row.get(3));
//			dataRecord.setCrAmtString(row.get(4));
//			dataRecord.setDescription(row.get(6));
//			dataRecord.setBalance(row.get(7));
//
//			if (StringUtils.isBlank(dataRecord.getTranSequence())) {
//				paymentsVIBNonERP.add(dataRecord);
//
//			} else {
//				if (DateUtils.getFragmentInHours(tranTime, Calendar.DAY_OF_YEAR) >= 23) {
//					paymentsVIB2324hours.add(dataRecord);
//				} else {
//					paymentsVIB.add(dataRecord);
//				}
//			}
//		}
//
//		Date timeLastestOfDateRc = DateUtils.addMilliseconds(DateUtils.addDays(dateRc, 1), -1);
//		List<ReconciliationDayDataCompareDTO> paymentsPg = paymentRepository.findByChannelAndDateAndPaymentTypeVA(
//				channelFunction.getChannel().getId(), EnumPaymentType.VIRTUAL_ACCOUNT_NO.code(),
//				PaymentConst.EnumBankStatus.SUCCEEDED.code(), PaymentConst.EnumBankStatus.SUCCEEDED.code(),
//				dateRc, timeLastestOfDateRc);
//
//		// Compare two list
//		List<VIBERPReconciliationDayDataCompareDTO> channelDifference = paymentsVIB.stream()
//				.filter(o1 -> paymentsPg.stream()
//						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())
//								&& o2.getTranAmt().compareTo(o1.getTranAmt()) == 0))
//				.collect(Collectors.toList());
//
//		List<ReconciliationDayDataCompareDTO> pgDifference = paymentsPg.stream()
//				.filter(o1 -> paymentsVIB.stream()
//						.noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())
//								&& o2.getTranAmt().compareTo(o1.getTranAmt()) == 0))
//				.collect(Collectors.toList());
//
//		List<VIBERPReconciliationDayDataCompareDTO> differencePayment = new ArrayList<VIBERPReconciliationDayDataCompareDTO>();
//		
//		if (CollectionUtils.isNotEmpty(channelDifference)) {
//			differencePayment.addAll(channelDifference);
//		}
//		if (CollectionUtils.isNotEmpty(pgDifference)) {
//			// differencePayment.addAll(pgDifference);
//		}
//
//		String[] headerCsv = new String[] { "THOI_GIAN", "UY_NHIEM_CHI", "TRANS_SEQUENCE", "GHI_NO", "GHI_CO",
//				"LOAI_TIEN", "NOI_DUNG", "SO_DU" };
//
//		String fileNameCsv = pgSftp.getLocalDirOut()
//				+ StringUtils.split(pgSftp.getLocalSyntaxFileNameOut(), '|')[1].replace("{0}", dateInFileName);
//
//		String timeReport = MyDateUtil.toDateString(VIBConst.VIB_IBFT_RECONCILIATION_DATE_FORMAT_DATE,
//				new Date());
//		if (createCSVFile_SoPhu(fileNameCsv, headerCsv, paymentsVIBNonERP, paymentsVIB2324hours,
//				differencePayment)) {
//			//mailService.sendMail(fileNameCsv, subjectMail + timeReport, contentMail,
//			//		"codeleader_am@nganluong.vn", "ba_am@nganluong.vn");
//		}
//	}
//	
//	public boolean createReconDetailOutputFile(String fileName,
//											   List<ReconciliationDayDataCompareDTO> reconResultList) throws IOException {
//		
//		String seperatorChar = "|";
//		List<String> stringList = new ArrayList<String>();
//		
//		ReconciliationDayDataCompareDTO header = reconResultList.remove(0);
//		stringList.add(header.getRecordType() + seperatorChar + header.getBinBank() + seperatorChar + header.getTranDateString() + "\r\n");
//		
//		ReconciliationDayDataCompareDTO footerRecon = reconResultList.remove(reconResultList.size() - 1);
//		String footerContent = footerRecon.getRecordType() + seperatorChar + footerRecon.getTotalRecord()
//				+ seperatorChar + footerRecon.getCreator() + seperatorChar + footerRecon.getTranHourString()
//				+ seperatorChar + footerRecon.getTranDateString() + seperatorChar + footerRecon.getChecksum() + "\r\n";
//		
//		for (ReconciliationDayDataCompareDTO row : reconResultList) {
//			
//			String[] fieldsToHas = new String[] {row.getRecordType(), StringUtils.trimToEmpty(row.getReconCode()), StringUtils.trimToEmpty(row.getTranType()),
//					StringUtils.trimToEmpty(row.getFromAcct()), StringUtils.trimToEmpty(row.getVirtualAccountNo()), StringUtils.trimToEmpty(row.getTranAmtString()),
//					StringUtils.trimToEmpty(row.getCcy()), StringUtils.trimToEmpty(row.getDescription()) ,StringUtils.trimToEmpty(row.getTranHourString()),
//					StringUtils.trimToEmpty(row.getTranDateString()), StringUtils.trimToEmpty(row.getTranSequence()), StringUtils.trimToEmpty(row.getClientTranId()),
//					StringUtils.trimToEmpty(row.getTranFeeString()), StringUtils.trimToEmpty(row.getNapasResult()), StringUtils.trimToEmpty(row.getReconResult())};
//			String dataToHas = StringUtils.join(fieldsToHas);
//			String checkSum = HashUtils.hashingMD5(dataToHas);
//			
//			StringBuilder line = new StringBuilder(row.getRecordType());
//			line.append(seperatorChar).append(StringUtils.trimToEmpty(row.getReconCode()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getTranType()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getFromAcct()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getVirtualAccountNo()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getTranAmtString()))
//					.append(seperatorChar).append(StringUtils.trimToEmpty(row.getCcy()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getDescription()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getTranHourString()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getTranDateString()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getTranSequence()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getClientTranId()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getTranFeeString()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getNapasResult()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getReconResult()))
//					.append(seperatorChar)
//					.append(StringUtils.trimToEmpty(checkSum)).append("\r\n");
//			
//			stringList.add(line.toString());
//		}
//		stringList.add(footerContent);
//		boolean result = fileService.writeListStringToFile(fileName, stringList);
//
//		String[] columns = { "RECORD_TYPE", "RECON_CODE", "TRAN_TYPE", "SOURCE_ACCOUNT", "TO_ACCOUNT",
//				"TRAN_AMOUNT", "CCY", "TRAN_DESC", "TRAN_TIME", "TRAN_DATE", "SEQ_NO (VIB)", "Ma UNC",
//				"TRAN_FEE", "NAPAS_RESULT", "RECON_RESULT", "CHECK_SUM" };
//		stringList.remove(0);
//
//		excelService.writeFileExcelFromList(columns, stringList, StringUtils.replace(fileName, ".txt", ".xlsx"));
//
//		return result;
//
//	}
//	
//	public boolean createReconMonthOuputFile(String fileName,
//			List<ReconciliationMonthDataCompareDTO> reconResultList) throws IOException {
//		
//		String seperatorChar = "|";
//		List<String> stringList = new ArrayList<String>();
//		
//		ReconciliationMonthDataCompareDTO header = reconResultList.remove(0);
//		stringList.add(header.getRecordType() + seperatorChar + header.getBinBank() + seperatorChar + header.getTranDateString() + "\r\n");
//		
//		ReconciliationMonthDataCompareDTO footerRecon = reconResultList.remove(reconResultList.size() - 1);
//		String footerContent = footerRecon.getRecordType() + seperatorChar + footerRecon.getTotalRecord()
//				+ seperatorChar + footerRecon.getCreator() + seperatorChar + footerRecon.getTranHourString()
//				+ seperatorChar + footerRecon.getTranDateString() + seperatorChar + footerRecon.getChecksum();
//		
//		for (ReconciliationMonthDataCompareDTO row : reconResultList) {
//			
//			String[] fieldsToHas = new String[] { row.getRecordType(), StringUtils.trimToEmpty(row.getSourceAcct()),
//					StringUtils.trimToEmpty(row.getTranType()), StringUtils.trimToEmpty(row.getFromDateString()),
//					StringUtils.trimToEmpty(row.getToDateString()), StringUtils.trimToEmpty(row.getChannelTotalAmt()),
//					StringUtils.trimToEmpty(row.getChannelTotalFee()),
//					StringUtils.trimToEmpty(row.getChannelTotalItems()), StringUtils.trimToEmpty(row.getReconResult()),
//					StringUtils.trimToEmpty(row.getPgTotalAmtString()),
//					StringUtils.trimToEmpty(row.getPgTotalItemsString()) };
//			String dataToHas = StringUtils.join(fieldsToHas);
//			String checkSum = HashUtils.hashingMD5(dataToHas);
//			
//			StringBuilder line = new StringBuilder(row.getRecordType());
//			line.append(seperatorChar).append(StringUtils.trimToEmpty(row.getSourceAcct())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getTranType())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getFromDateString())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getToDateString())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getChannelTotalAmt())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getChannelTotalFee())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getChannelTotalItems())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getReconResult())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getPgTotalAmtString())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(row.getPgTotalItemsString())).append(seperatorChar)
//					.append(StringUtils.trimToEmpty(checkSum)).append("\r\n");
//			
//			stringList.add(line.toString());
//		}
//		stringList.add(footerContent);
//		boolean result = fileService.writeListStringToFile(fileName, stringList);
//		
//		String[] columns = { "RECORD_TYPE", "BANKSRCACCTNO", "TRAN_TYPE", "FROM_DATE", "TO_DATE",
//				"VIB_TOTAL_AMT", "VIB_TOTAL_FEE", "VIB_TOTAL_ITEMS", "RECON_RESULT", 
//				"GATEWAY_TOTAL_AMT", "GATEWAY_TOTAL_ITEMS", "CHECK_SUM" };
//		stringList.remove(0);
//
//		excelService.writeFileExcelFromList(columns, stringList, StringUtils.replace(fileName, ".txt", ".xlsx"));
//
//		return result; 
//
//	}
//
//	public boolean createCSVFile_SoPhu(String fileName, String[] header,
//			List<VIBERPReconciliationDayDataCompareDTO> nonERPPayment, List<VIBERPReconciliationDayDataCompareDTO> overTimePayment,
//			List<VIBERPReconciliationDayDataCompareDTO> differencePayment) throws IOException {
//		FileWriter out = new FileWriter(fileName);
//		try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(header))) {
//			printer.printRecord("Non ERP Payment:");
//			for (VIBERPReconciliationDayDataCompareDTO item : nonERPPayment) {
//				printer.printRecord(
//						MyDateUtil.toDateString(VIBConst.VIB_IBFT_RECONCILIATION_TRAN_TIME_CSV_FORMAT_DATE,
//								item.getTranTime()),
//						item.getClientTranId(), item.getTranSequence(), item.getDrAmtString(), item.getCrAmtString(),
//						VIBConst.CCY, item.getDescription(), item.getBalance());
//			}
//
//			printer.printRecord("Difference ERP Payment (00:00:00 ~ 22:59:59):");
//			for (VIBERPReconciliationDayDataCompareDTO item : differencePayment) {
//				printer.printRecord(
//						MyDateUtil.toDateString(VIBConst.VIB_IBFT_RECONCILIATION_TRAN_TIME_CSV_FORMAT_DATE,
//								item.getTranTime()),
//						item.getClientTranId(), item.getTranSequence(), item.getDrAmtString(), item.getCrAmtString(),
//						VIBConst.CCY, item.getDescription(), item.getBalance());
//			}
//
//			printer.printRecord("Over time ERP Payment (23:00:00 ~ 23:59:59):");
//			for (VIBERPReconciliationDayDataCompareDTO item : overTimePayment) {
//				printer.printRecord(
//						MyDateUtil.toDateString(VIBConst.VIB_IBFT_RECONCILIATION_TRAN_TIME_CSV_FORMAT_DATE,
//								item.getTranTime()),
//						item.getClientTranId(), item.getTranSequence(), item.getDrAmtString(), item.getCrAmtString(),
//						VIBConst.CCY, item.getDescription(), item.getBalance());
//			}
//			
//			printer.printRecord("Tong cong sai lech ERP: " + (differencePayment.size() + overTimePayment.size()) + " giao dich");
//			return true;
//		} catch (Exception e) {
//			return false;
//		}
//
//	}
//
//	private ResponseEntity<PGResponse> getPgResponseResponseReconSuccess() {
//		PGResponse prefixResult = commonResponseService.returnGatewayRequestSuccessPrefix();
//		PGResponse pgResponse = new PGResponse();
//		pgResponse.setStatus(true);
//
//		pgResponse.setErrorCode(prefixResult.getErrorCode());
//		pgResponse.setMessage(prefixResult.getMessage());
//
//		return new ResponseEntity<>(pgResponse, HttpStatus.OK);
//	}
//	
//
//}
