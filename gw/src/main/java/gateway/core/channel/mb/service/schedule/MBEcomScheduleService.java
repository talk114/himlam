package gateway.core.channel.mb.service.schedule;

import gateway.core.channel.PaymentGate;
import gateway.core.channel.mb.dto.MBConst;
import gateway.core.channel.mb.dto.MBEcomDayReconciliateReq;
import gateway.core.util.PGUtil;
import gateway.core.util.SFTPUploader;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.dto.PaymentConst;
import vn.nganluong.naba.dto.ReconciliationDayDataCompareDTO;
import vn.nganluong.naba.entities.PgFunction;
import vn.nganluong.naba.entities.PgSftp;
import vn.nganluong.naba.repository.PaymentRepository;
import vn.nganluong.naba.repository.PgFunctionRepository;
import vn.nganluong.naba.repository.PgSftpRepository;
import vn.nganluong.naba.service.CommonLogService;
import vn.nganluong.naba.service.ExcelService;
import vn.nganluong.naba.service.FileService;
import vn.nganluong.naba.service.MailService;
import vn.nganluong.naba.service.SFTPService;
import vn.nganluong.naba.utils.HashUtils;
import vn.nganluong.naba.utils.MyDateUtil;

/**
 *
 * @author taind
 */
@Service
@Component("MBEcomScheduleService")
public class MBEcomScheduleService extends PaymentGate {

    private static final Logger logger = LogManager.getLogger(MBEcomScheduleService.class);
    @Autowired
    private PgFunctionRepository pgFunctionRepository;
    @Autowired
    private PgSftpRepository pgSftpRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private ExcelService excelService;
    @Autowired
    private SFTPService sFTPService;
    @Autowired
    private CommonLogService commonLogService;
    @Autowired
    private MailService mailService;

    @Scheduled(cron = "0 0 6 * * ?")
    public void doDayReconciliationMBEcomStep1() {
        System.out.println("run cron MB START!!! - " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()));
        try {
            PgFunction pgFunction = pgFunctionRepository.findFirstByCodeAndStatus(MBConst.FUNCTION_CODE_RECONCILIATION_DAY_STEP_1, true);
            PgSftp pgSftp = pgSftpRepository.findTopByChannelIdAndPgFunctionId(pgFunction.getChannelId(), pgFunction.getId());

            String startDay = PGUtil.formatDateTime("yyyyMMdd", System.currentTimeMillis() - 86400000L);
            String endDay = PGUtil.formatDateTime("yyyyMMdd", System.currentTimeMillis() - 86400000L);

            MBEcomDayReconciliateReq reconciliationReq = new MBEcomDayReconciliateReq(startDay, endDay);
            Date fromDate = null;
            Date toDate = null;
            String dateInFileName = null;
            if (StringUtils.isAnyEmpty(reconciliationReq.getFrom_date(), reconciliationReq.getTo_date())) {
                fromDate = MyDateUtil.getYesterday();
                toDate = MyDateUtil.getYesterdayLastest();
                dateInFileName = MyDateUtil.toDateString(MBConst.RECONCILIATION_FILE_NAME_FORMAT_DATE, new Date());
            } else {
                fromDate = DateUtils.parseDate(reconciliationReq.getFrom_date(), MBConst.RECONCILIATION_PARAMS_FORMAT_DATE);
                toDate = DateUtils.addDays(DateUtils.parseDate(reconciliationReq.getTo_date(), MBConst.RECONCILIATION_PARAMS_FORMAT_DATE), 1);
                toDate = DateUtils.addMilliseconds(toDate, -1);
                dateInFileName = MyDateUtil.toDateString(MBConst.RECONCILIATION_FILE_NAME_FORMAT_DATE, DateUtils.addDays(toDate, 1));
            }
            // filename doi soat MB
            dateInFileName = MyDateUtil.formatDateToString(MBConst.RECONCILIATION_FILE_NAME_FORMAT_DATE);

            List<ReconciliationDayDataCompareDTO> paymentsPg = paymentRepository.findByChannelAndDateAndPaymentTypeReturnWithSeq(
                    pgFunction.getChannelId(), PaymentConst.EnumPaymentType.ACCOUNT_NO.code(),
                    PaymentConst.EnumBankStatus.SUCCEEDED.code(), PaymentConst.EnumBankStatus.SUCCEEDED.code(),
                    fromDate, toDate);

            for (ReconciliationDayDataCompareDTO payment : paymentsPg) {
                payment.setTranTimeString(MyDateUtil.toDateString(MBConst.RECONCILIATION_FILE_STRUCT_FORMAT_DATE_TIME, payment.getTranTime()));
                payment.setTranAmtString(StringUtils.removeEnd(payment.getTranAmt().toPlainString(), ".00"));
            }
            String fileNameReconResultOutput = pgSftp.getLocalDirOut() + StringUtils.split(pgSftp.getLocalSyntaxFileNameOut(), '|')[0].replace("{0}", dateInFileName);
            if (createReconDetailOutputFile(fileNameReconResultOutput, paymentsPg)) {
                File fileDS = new File(fileNameReconResultOutput);
                String fileNameDelete = fileNameReconResultOutput;
                ClassLoader classLoader = MBEcomScheduleService.class.getClassLoader();
                String partPrivateKey = Objects.requireNonNull(classLoader.getResource("MB/id_rsa")).getPath();
                System.out.println("PATH: " + partPrivateKey);
                SFTPUploader.updateSFTP(fileDS, partPrivateKey, "/mbin", fileNameDelete);
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        System.out.println("run cron MB SUCCESS!!! - " + PGUtil.formatDateTime("yyyy-MM-dd'T'HH:mm:ss", System.currentTimeMillis()));
    }

//    @Scheduled(cron = "0 0 10 * * ?")
//    public void doDayReconciliationMBEcomStep2() {
//        try {
//            PgFunction pgFunction = pgFunctionRepository.findFirstByCodeAndStatus(MBConst.FUNCTION_CODE_RECONCILIATION_DAY_STEP_2, true);
//            PgSftp pgSftp = pgSftpRepository.findTopByChannelIdAndPgFunctionId(pgFunction.getChannelId(), pgFunction.getId());
//
//            String startDay = PGUtil.formatDateTime("yyyyMMdd", System.currentTimeMillis() - 86400000L);
//            String endDay = PGUtil.formatDateTime("yyyyMMdd", System.currentTimeMillis() - 86400000L);
//
//            MBEcomDayReconciliateReq reconciliationReq = new MBEcomDayReconciliateReq(startDay, endDay);
//
//            Date fromDate = null;
//            Date toDate = null;
//            String dateInFileName = null;
//            if (StringUtils.isAnyEmpty(reconciliationReq.getFrom_date(), reconciliationReq.getTo_date())) {
//                fromDate = MyDateUtil.getYesterday();
//                toDate = MyDateUtil.getYesterdayLastest();
//                dateInFileName = MyDateUtil.toDateString(MBConst.RECONCILIATION_FILE_NAME_FORMAT_DATE, new Date());
//            } else {
//                fromDate = DateUtils.parseDate(reconciliationReq.getFrom_date(), MBConst.RECONCILIATION_PARAMS_FORMAT_DATE);
//                toDate = DateUtils.addDays(DateUtils.parseDate(reconciliationReq.getTo_date(), MBConst.RECONCILIATION_PARAMS_FORMAT_DATE), 1);
//                toDate = DateUtils.addMilliseconds(toDate, -1);
//                dateInFileName = MyDateUtil.toDateString(MBConst.RECONCILIATION_FILE_NAME_FORMAT_DATE, DateUtils.addDays(toDate, 1));
//            }
//            List<String> filesDownloaded = sFTPService.DownloadFileTSubtract1(pgSftp, dateInFileName);
//            if (filesDownloaded.size() > 0) {
//                // Thực hiện đối soát giao dịch ngày
//                String timeReport = MyDateUtil.toDateString(MBConst.RECONCILIATION_FORMAT_DATE_REPORT, toDate);
//                doiSoatChenhLechGiaoDich(pgFunction.getChannelId(), pgSftp, dateInFileName, fromDate, toDate, timeReport);
//            } else {
//                String[] paramsLog = new String[]{"Cannot download file or file does not exist."};
//                logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, MBConst.SERVICE_NAME, MBConst.FUNCTION_CODE_RECONCILIATION_DAY_STEP_2, false, false, true, paramsLog));
//            }
//        } catch (Exception e) {
//            logger.info(ExceptionUtils.getStackTrace(e));
//        }
//    }

//    @Scheduled(cron = "0 30 5 1 * ? ")
//    public void doMonthReconciliationMBEcom() {
//        try {
//            PgFunction pgFunction = pgFunctionRepository.findFirstByCodeAndStatus(MBConst.FUNCTION_CODE_RECONCILIATION_MONTH, true);
//            PgSftp pgSftp = pgSftpRepository.findTopByChannelIdAndPgFunctionId(pgFunction.getChannelId(), pgFunction.getId());
//            
//            String month = PGUtil.formatDateTime("yyyyMM", System.currentTimeMillis() - 86400000L);
//            
//            Date startMonth = null;
//            Date endMonth = null;
//            String monthInFileName = month;
//            if (StringUtils.isBlank(month)) {
//                Date currentMonth = DateUtils.truncate(new Date(), Calendar.MONTH);
//                startMonth = DateUtils.addMonths(currentMonth, -1);
//                endMonth = DateUtils.addMilliseconds(currentMonth, -1);
//                monthInFileName = MyDateUtil.toDateString(MBConst.RECONCILIATION_FILE_NAME_FORMAT_MONTH, startMonth);
//            } else {
//                startMonth = DateUtils.truncate(DateUtils.parseDate(month + "01", MBConst.RECONCILIATION_PARAMS_FORMAT_DATE), Calendar.MONTH);
//                endMonth = DateUtils.addMilliseconds(DateUtils.addMonths(startMonth, 1), -1);
//            }
//            String monthInFileNameDownload = monthInFileName + MBConst.RECONCILIATION_FILE_NAME_CHARACTER_AFTER_DATE;
//            List<String> filesDownloaded = sFTPService.DownloadFileTSubtract1(pgSftp, monthInFileNameDownload);
//            if (filesDownloaded.size() > 0) {
//                // Thực hiện đối soát giao dịch tháng
//                String timeReport = MyDateUtil.toDateString(MBConst.RECONCILIATION_FORMAT_MONTH_REPORT, endMonth);
//                doiSoatChenhLechGiaoDich(pgFunction.getChannelId(), pgSftp, monthInFileName, startMonth, endMonth, timeReport);
//            } else {
//                String[] paramsLog = new String[]{"Cannot download file or file does not exist."};
//                logger.info(commonLogService.createContentLog(MBConst.CHANNEL_CODE, MBConst.SERVICE_NAME, MBConst.FUNCTION_CODE_RECONCILIATION_MONTH, false, false, true, paramsLog));
//            }
//        } catch (Exception e) {
//            logger.info(ExceptionUtils.getStackTrace(e));
//        }
//    }

//    private void doiSoatChenhLechGiaoDich(Integer channelId, PgSftp pgSftp, String dateInFileName, Date fromDate, Date toDate, String timeReport) {
//        String fileName = pgSftp.getLocalDirIn() + StringUtils.split(pgSftp.getLocalSyntaxFileNameIn(), '|')[0].replace("{0}", dateInFileName);
//        List<ReconciliationDayDataCompareDTO> reconciliationResultList = createReconciliationResultList(channelId, fromDate, toDate, fileName);
//        String fileNameReconResultOutput = pgSftp.getLocalDirOut() + StringUtils.split(pgSftp.getLocalSyntaxFileNameOut(), '|')[0].replace("{0}", dateInFileName);
//
//        String fileNameExcelRecon = StringUtils.EMPTY;
//        if (CollectionUtils.isNotEmpty(reconciliationResultList) && createReconDetailOutputFile(
//                fileNameReconResultOutput, reconciliationResultList)) {
//
//            File fileDS = new File(fileNameReconResultOutput);
//            String fileNameDelete = fileNameReconResultOutput;
//            ClassLoader classLoader = MBEcomScheduleService.class.getClassLoader();
//            String partPrivateKey = Objects.requireNonNull(classLoader.getResource("MB/id_rsa")).getPath();
//            SFTPUploader.updateSFTP(fileDS, partPrivateKey, "/mbout", fileNameDelete);
//        }
//
//        if (StringUtils.isNotEmpty(fileNameExcelRecon)) {
//            mailService.sendMail(MessageFormat.format(pgSftp.getMailSubject(), timeReport), pgSftp.getMailContent(), fileNameExcelRecon);
//        }
//    }
//
//    private List<ReconciliationDayDataCompareDTO> createReconciliationResultList(Integer channelId, Date fromDate, Date toDate, String fileName) {
//        List<ReconciliationDayDataCompareDTO> paymentsMBEcom = new ArrayList<ReconciliationDayDataCompareDTO>();
//
//        @SuppressWarnings("unchecked")
//        List<List<String>> listData = (List<List<String>>) fileService.readFileTextToList(fileName, MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR_IN_FILE);
//
//        if (CollectionUtils.size(listData) < 1) {
//            return paymentsMBEcom;
//        }
//        // Lấy dữ liệu trong database trước khi duyệt List từ file.
//        // Do file Bank trả về không có mã UNC, cần lấy ra trước để duyệt mã UNC bước sau.
//        List<ReconciliationDayDataCompareDTO> paymentsPg = paymentRepository
//                .findByChannelAndDateAndPaymentTypeReturnWithSeq(
//                        channelId, PaymentConst.EnumPaymentType.ACCOUNT_NO.code(),
//                        PaymentConst.EnumBankStatus.SUCCEEDED.code(), PaymentConst.EnumBankStatus.SUCCEEDED.code(),
//                        fromDate, toDate);
//
//        for (ReconciliationDayDataCompareDTO payment : paymentsPg) {
//            payment.setTranTimeString(MyDateUtil.toDateString(MBConst.RECONCILIATION_FILE_STRUCT_FORMAT_DATE_TIME, payment.getTranTime()));
//            payment.setTranAmtString(StringUtils.removeEnd(payment.getTranAmt().toPlainString(), ".00"));
//            payment.setCcy(MBConst.CCY);
//            payment.setReconCode(MBConst.RECONCILIATION_FILE_STRUCT_RECON_CODE_SUCCESS); // BankResponseCode 00 Giao dich thanh cong
//        }
//        // DateUtils.addHours(date, amount)
//        for (List<String> row : listData) {
//            if (!StringUtils.equals(row.get(0), MBConst.RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY)) {
//                continue;
//            }
//            // Checksum:
//            String[] fieldsToHas = new String[]{row.get(0), row.get(1), row.get(2), row.get(3), row.get(4), row.get(5),
//                row.get(6), row.get(7), row.get(8), row.get(9), row.get(10), row.get(11), row.get(12), row.get(13)};
//            String dataToHas = StringUtils.join(fieldsToHas);
//
//            if (!StringUtils.equals(HashUtils.hashingMD5(dataToHas), row.get(14))) {
//                System.out.println("Fail checksum");
//                // continue;
//            }
//            // Check time
//            Date tranTime = MyDateUtil.parseDateFormat(MBConst.RECONCILIATION_FILE_STRUCT_FORMAT_DATE_TIME, row.get(6));
//            if (!(tranTime.compareTo(fromDate) >= 0 && tranTime.compareTo(toDate) <= 0)) {
//                System.out.println("Out of date");
//                continue;
//            }
//            ReconciliationDayDataCompareDTO dataRecord = new ReconciliationDayDataCompareDTO();
//
//            dataRecord.setRecordType(row.get(0)); // 1. Loại bản ghi
//            dataRecord.setReconResult(row.get(1)); // 2. Kết quả đối soát
//            dataRecord.setCcy(MBConst.CCY); // 3. CCY
//            // 4. MsgType 1004
//            // 5. Amount
//            dataRecord.setTranAmtString(row.get(4));
//            if (NumberUtils.isParsable(row.get(4))) {
//                dataRecord.setTranAmt(NumberUtils.createBigDecimal(row.get(4)));
//            }
//            dataRecord.setChannelTranId(row.get(5)); // 6. RequestId
//            dataRecord.setTranTime(tranTime);
//            dataRecord.setTranTimeString(row.get(6)); // 7. TranDate
//            // 8. ChannelId P3
//            dataRecord.setChannelTranSeq(row.get(8)); // 9. BankTrxSeq
//            dataRecord.setRetRefNumber(row.get(9)); // 10.RetRefNumber
//            dataRecord.setReconCode(row.get(10)); // 11. BankResponseCode
//            dataRecord.setCardId(row.get(11)); // 12. CardID
//            dataRecord.setFromAcct(row.get(12)); // 13. Account
//            // Set mã UNC (Do file bank trả về không có mã UNC):
//            if (paymentsPg.size() > 0) {
//                ReconciliationDayDataCompareDTO paymentSearch = paymentsPg.stream().
//                        filter(p -> p.getChannelTranId().equals(dataRecord.getChannelTranId())
//                        && p.getChannelTranSeq().equals(dataRecord.getChannelTranSeq())).
//                        findFirst().orElse(null);
//                if (paymentSearch != null) {
//                    dataRecord.setClientTranId(paymentSearch.getClientTranId());
//                } else {
//                    // dataRecord.setClientTranId(dataRecord.getChannelTranSeq());
//                    dataRecord.setClientTranId(StringUtils.EMPTY);
//                }
//            } else {
//                // dataRecord.setClientTranId(dataRecord.getChannelTranSeq());
//                dataRecord.setClientTranId(StringUtils.EMPTY);
//            }
//            paymentsMBEcom.add(dataRecord);
//        }
//        // Compare two list
//        // 01 – Có Ngân Lượng, không có tại VIB
//        List<ReconciliationDayDataCompareDTO> pgResidual = paymentsPg.stream()
//                .filter(o1 -> paymentsMBEcom.stream()
//                .noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())))
//                .collect(Collectors.toList());
//        // Cập nhật trạng thái 01
//        if (CollectionUtils.isNotEmpty(pgResidual)) {
//            for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : pgResidual) {
//                // set format tiền lấy từ DB, loại bỏ ".00" ở sau
//                reconciliationDayDataCompareDTO.setTranAmtString(
//                        reconciliationDayDataCompareDTO.getTranAmt().toPlainString().replace(".00", ""));
//                reconciliationDayDataCompareDTO.setReconResult(MBConst.RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_NL);
//                reconciliationDayDataCompareDTO.setRecordType(MBConst.RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY);
//                // reconciliationDayDataCompareDTO.setReconCode(MBConst.RECONCILIATION_FILE_STRUCT_RECON_CODE_SUCCESS);
//            }
//        }
//
//        // 02 – Có VIB, không có Ngân Lượng
//        List<ReconciliationDayDataCompareDTO> channelResidual = paymentsMBEcom.stream()
//                .filter(o1 -> paymentsPg.stream()
//                .noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())))
//                .collect(Collectors.toList());
//        // Cập nhật trạng thái 02
//        if (CollectionUtils.isNotEmpty(channelResidual)) {
//            for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : channelResidual) {
//                reconciliationDayDataCompareDTO.setReconResult(MBConst.RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_MB);
//            }
//        }
//
//        // 03 – Sai lệch số liệu
//        // Sai lệch từ PG hoặc Channel
//        List<ReconciliationDayDataCompareDTO> differenceValue = paymentsMBEcom.stream()
//                .filter(o1 -> paymentsPg.stream()
//                .noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())
//                && o2.getTranAmt().compareTo(o1.getTranAmt()) == 0
//                && StringUtils.equals(o2.getReconCode(), o1.getReconCode())
//                && StringUtils.equals(o2.getChannelTranSeq(), o1.getChannelTranSeq())))
//                .filter(o1 -> pgResidual.stream().noneMatch(
//                o3 -> o3.getClientTranId().equals(o1.getClientTranId()))) // Loại bỏ bản ghi trùng từ (01)
//                .filter(o1 -> channelResidual.stream().noneMatch(
//                o4 -> o4.getClientTranId().equals(o1.getClientTranId()))) // Loại bỏ bản ghi trùng từ (02)
//                .collect(Collectors.toList());
//
//        // Cập nhật trạng thái 03
//        if (CollectionUtils.isNotEmpty(differenceValue)) {
//            for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : differenceValue) {
//                reconciliationDayDataCompareDTO
//                        .setReconResult(MBConst.RECONCILIATION_FILE_STRUCT_RECON_RESULT_DIFF_VALUE);
//            }
//        }
//        // Sai lệch từ Channel
//        List<ReconciliationDayDataCompareDTO> differencePayment = new ArrayList<ReconciliationDayDataCompareDTO>();
//
//        if (CollectionUtils.isNotEmpty(pgResidual)) {
//            differencePayment.addAll(pgResidual);
//        }
//        if (CollectionUtils.isNotEmpty(channelResidual)) {
//            differencePayment.addAll(channelResidual);
//        }
//        if (CollectionUtils.isNotEmpty(differenceValue)) {
//            differencePayment.addAll(differenceValue);
//        }
//        // Danh sách tổng hợp
//        // (00) Tổng hợp lại các bản ghi trùng khớp đối soát (Kết quả đối soát: 00)
//        List<ReconciliationDayDataCompareDTO> reconResultList = paymentsMBEcom.stream()
//                .filter(o1 -> differencePayment.stream()
//                .noneMatch(o2 -> o2.getClientTranId().equals(o1.getClientTranId())))
//                .collect(Collectors.toList());
//        // Cập nhật trạng thái 00
//        for (ReconciliationDayDataCompareDTO reconciliationDayDataCompareDTO : reconResultList) {
//            reconciliationDayDataCompareDTO.setReconResult(MBConst.RECONCILIATION_FILE_STRUCT_RECON_RESULT_EQUAL);
//        }
//        // Thêm bản ghi sai lệch vào danh sách tổng hợp (01,02,03)
//        reconResultList.addAll(differencePayment);
//        return reconResultList;
//    }

    private boolean createReconDetailOutputFile(String fileName, List<ReconciliationDayDataCompareDTO> reconResultList) {
        List<String> stringList = new ArrayList<String>();
        List<String> stringListForNL = new ArrayList<String>();
        // add tiêu đề
        String[] header = new String[]{"RecordType", "RcReconcile", "CurCode", "MsgType", "Amount", "Fee", "RequestId", "TranDate", "ChanelId", "BankTrxSeq", "RetRefNumber", "BankResponseCode", "CardID", "Account"};
        String lineJoinHeader = StringUtils.join(header, MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR) + MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR + "Cardnumber" + MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR + "TransType" + MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR + "Checksum" + "\r\n";
        stringList.add(lineJoinHeader);
        for (ReconciliationDayDataCompareDTO row : reconResultList) {
            String cardType = row.getCardType() == 2 ? "1003" : MBConst.RECONCILIATION_FILE_STRUCT_RECON_MSG_TYPE_PAYG_CARD;

            String[] fields = new String[]{MBConst.RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY,
                StringUtils.trimToEmpty(row.getReconResult()), //StringUtils.EMPTY,
                MBConst.CCY,
                cardType,
                StringUtils.trimToEmpty(row.getTranAmtString()),
                "",
                StringUtils.trimToEmpty(row.getChannelTranId()), // RequestId
                StringUtils.trimToEmpty(row.getTranTimeString()),
                MBConst.RECONCILIATION_FILE_STRUCT_RECON_CHANNEL_ID_MB,
                StringUtils.trimToEmpty(row.getChannelTranSeq()), // BankTrxSeq
                cardType.equals("1003") ? "" : StringUtils.trimToEmpty(row.getChannelTranId()), // RetRefNumber
                //                StringUtils.trimToEmpty(row.getRetRefNumber()).equals("") ? "null" : StringUtils.trimToEmpty(row.getRetRefNumber()), // StringUtils.EMPTY, // RetRefNumber
                StringUtils.trimToEmpty(row.getReconCode()).equals("") ? "00" : StringUtils.trimToEmpty(row.getReconCode()), // BankResponseCode
                "",
                StringUtils.trimToEmpty(row.getFromAcct()), //StringUtils.EMPTY, // Account
                StringUtils.trimToEmpty(formatCard(row.getToAcct())), //StringUtils.EMPTY, // CardID
                MBConst.RECONCILIATION_FILE_STRUCT_RECON_TRANS_TYPE_MB};
            String dataToHas = StringUtils.join(fields);
            String checkSum = HashUtils.hashingMD5(dataToHas);

            String lineJoin = StringUtils.join(fields, MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR) + MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR + checkSum + "\r\n";
            stringList.add(lineJoin);

            String[] fieldsForNL = new String[]{MBConst.RECONCILIATION_FILE_STRUCT_RECORD_TYPE_BODY,
                StringUtils.trimToEmpty(row.getReconResult()), //StringUtils.EMPTY,
                MBConst.CCY,
                cardType,
                StringUtils.trimToEmpty(row.getTranAmtString()),
                "",
                StringUtils.trimToEmpty(row.getChannelTranId()), // RequestId
                StringUtils.trimToEmpty(row.getTranTimeString()),
                MBConst.RECONCILIATION_FILE_STRUCT_RECON_CHANNEL_ID_MB,
                StringUtils.trimToEmpty(row.getChannelTranSeq()), // BankTrxSeq
                cardType.equals("1003") ? "" : StringUtils.trimToEmpty(row.getChannelTranId()), // RetRefNumber
                //                StringUtils.trimToEmpty(row.getRetRefNumber()).equals("") ? "null" : StringUtils.trimToEmpty(row.getRetRefNumber()), // StringUtils.EMPTY, // RetRefNumber
                StringUtils.trimToEmpty(row.getReconCode()).equals("") ? "00" : StringUtils.trimToEmpty(row.getReconCode()), // BankResponseCode
                "",
                StringUtils.trimToEmpty(row.getFromAcct()), //StringUtils.EMPTY, // Account
                StringUtils.trimToEmpty(formatCard(row.getToAcct())), //StringUtils.EMPTY, // CardID
                MBConst.RECONCILIATION_FILE_STRUCT_RECON_TRANS_TYPE_MB};
            String lineJoinForNL = StringUtils.join(fieldsForNL, MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR);
            stringListForNL.add(lineJoinForNL);
        }
        String footerContent = createFooter(reconResultList.size());
        stringList.add(footerContent);
        stringListForNL.add(footerContent);

        boolean result = fileService.writeListStringToFile(fileName, stringList);
        String[] columns = {"RecordType", "RcReconcile", "CurCode", "MsgType", "Amount", "Fee", "RequestId", "TranDate", "ChanelId", "BankTrxSeq",
            "RetRefNumber", "BankResponseCode", "CardID", "AccountCardnumber", "TransType", "Checksum"};
        excelService.writeFileExcelFromList(columns, stringListForNL, StringUtils.replace(fileName, ".txt", ".xlsx"));
        return result;
    }

    private String createFooter(int sizeList) {
        String timeCreate = MyDateUtil.toDateString(MBConst.RECONCILIATION_FILE_STRUCT_FORMAT_DATE_TIME, new Date());
        return MBConst.RECONCILIATION_FILE_STRUCT_RECORD_TYPE_FOOTER + MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR
                + sizeList + MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR
                + MBConst.RECONCILIATION_FILE_STRUCT_RECON_CREATOR + MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR
                + timeCreate + MBConst.RECONCILIATION_FILE_STRUCT_SEPARATOR_CHAR
                + HashUtils.hashingMD5(timeCreate + sizeList) + "\r\n";
    }

    private String formatCard(String cardNumber) {
        String result = "";
        if (cardNumber != null && !cardNumber.equals("")) {
            String first = cardNumber.substring(0, 6);
            String last = cardNumber.substring(cardNumber.length() - 4, cardNumber.length());
            result = first + "xxxxxx" + last;
        }
        return result;
    }
}
