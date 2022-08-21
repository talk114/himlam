package gateway.core.channel.migs.service.schedule;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.*;
import gateway.core.channel.migs.config.MIGSConfig;
import gateway.core.util.EmailUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.nganluong.naba.entities.Payment;
import vn.nganluong.naba.service.PaymentService;

import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sonln
 */
@Service("MigsScheduleService")
public class MigsScheduleService {
    private static final String PREDIX = "DOI SOAT MIGS : ";
    private static String fileDownloadName = null;
    @Autowired
    private PaymentService paymentService;
    private static final Logger logger = LogManager.getLogger(MigsScheduleService.class);

    //TODO: 12:10 hang ngay thuc hien doi soat
    @Scheduled(cron = "0 10 14 * * MON-FRI")
    private void doiSoat() {
        String log = PREDIX;
        Calendar cal = Calendar.getInstance();
        final String year = String.valueOf(cal.get(Calendar.YEAR));
        final String month = (cal.get(Calendar.MONTH)) + 1 < 10 ? ("0" + (cal.get(Calendar.MONTH) + 1)) : ("" + (cal.get(Calendar.MONTH) + 1));
        final String day = (cal.get(Calendar.DAY_OF_MONTH)) < 10 ? ("0" + (cal.get(Calendar.DAY_OF_MONTH))) : "" + (cal.get(Calendar.DAY_OF_MONTH));
        final String yyyyMMdd = year + month + day;
        String fileDownloadPath = MIGSConfig.LOCAL_PATH + File.separator;

        try {
            log += String.format("NGAY %s THANG %s NAM %s", day, month, year);
            //TODO:tai file excel tren sftp
            log += "---> TAI FILE VP BANK ";
            downloadSFTP(yyyyMMdd, fileDownloadPath);
            log += "---> TAI XONG FILE VB BANK";

            //TODO:doc file lay du lieu doi soat
            log += "---> LAY GIAO DICH THANH CONG VB BANK ";
            fileDownloadPath = MIGSConfig.LOCAL_PATH + File.separator + fileDownloadName;
            Set<String> daysInFile = new LinkedHashSet();
            List<Payment_Rec> listPaymentSuccessInVPBank = readFileDownloaded(fileDownloadPath, daysInFile);
            log += "---> GIAO DICH THANH CONG VB BANK : " + listPaymentSuccessInVPBank;

            //TODO:lay du lieu giao dich tren gw
            log += "---> LAY GIAO DICH THANH CONG GW";
            List<Payment_Rec> listPaymentSuccessInGW = getDataPaymentGW(daysInFile);
            log += "---> GIAO DICH THANH CONG GW : " + listPaymentSuccessInGW;

            //TODO:loc giao dich sai lech
            log += "---> TAO FILE SAI LECH";
            String fileNameDownload = fileDownloadName.split("\\.")[0] + "_SAILECH.xlsx";
            String fileReconciliationPath = MIGSConfig.LOCAL_PATH + File.separator + fileNameDownload;
            filter(listPaymentSuccessInVPBank, listPaymentSuccessInGW, fileReconciliationPath);
            log += "---> TAO FILE SAI LECH THANH CONG : " + fileDownloadPath;

            //TODO:tai file len cho VPB
            log += "---> TAI FILE LEN CHO VP";
            File fileEncodeUpload = new File(fileDownloadPath);
            uploadSFTP(fileEncodeUpload, MIGSConfig.SFTPWORKINGDIR_UPLOAD + File.separator + fileNameDownload);
            log += "---> THANH CONG";

            //TODO:gui file doi soat cho van hanh: goc va sai lech
            log += "---> SEND MAIL TO AM";
            String[] sendFileList = {fileDownloadPath,fileReconciliationPath};
            sendMailToAM(sendFileList, day, month, year);
            log += "---> SEND MAIL TO AM SUCCES";
            fileDownloadName = null;
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
            log += "---> FAIL : " + e.getMessage();
            sendMailToAM(null, day, month, year);
            fileDownloadName = null;
        }
        logger.info(log);
    }

    private static void downloadSFTP(String yyyyMMdd, String fileDownloadPath) throws JSchException, IOException, SftpException {
        final String sftpWorkingDir = MIGSConfig.SFTPWORKINGDIR_DOWNLOAD;
        final String SFTP_HOST = MIGSConfig.SFTP_HOST;
        final int SFTP_PORT = MIGSConfig.SFTP_PORT;
        final String SFTP_USER = MIGSConfig.SFTP_USERNAME;

        Session session;
        Channel channel;
        ChannelSftp channelSftp;

        JSch jsch = new JSch();
        session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        session.setConfig("PreferredAuthentications", "publickey,gssapi-with-mic,keyboard-interactive,password");
        session.setConfig("StrictHostKeyChecking", "no");

        ClassLoader classLoader = MigsScheduleService.class.getClassLoader();
        String fileAuth = Objects.requireNonNull(classLoader.getResource("vpb_migs/id_rsa")).getFile();
        jsch.addIdentity(fileAuth);
        session.connect();
        System.out.println("connected server......");
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
        channelSftp.cd(sftpWorkingDir);

        System.out.printf(PREDIX + "Finding File Rec...");
        Vector<String> vec = channelSftp.ls(channelSftp.pwd());
        Iterator i = vec.iterator();
        String fileName = null;
        while (i.hasNext()) {
            String out = i.next().toString();
            if (out.contains(yyyyMMdd)) {
                String list[] = out.split(" ");
                for (int j = 0; j < list.length; j++) {
                    if (list[j].contains(yyyyMMdd)) {
                        fileName = "MPGS NGAN " + list[j];
                    }
                }
            }
        }
        if (fileName == null) {
            System.out.println("File do not exist!!");
            channelSftp.disconnect();
            session.disconnect();
            return;
        }

        System.out.println(PREDIX + "file name VPBANK" + fileName);
        fileDownloadName = fileName;
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = new BufferedInputStream(channelSftp.get(fileName));
        File newFile = new File(fileDownloadPath+fileName);
        OutputStream os = new FileOutputStream(newFile);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        int readCount;
        while ((readCount = bis.read(buffer)) > 0) {
            System.out.println("Writing: ");
            bos.write(buffer, 0, readCount);
        }
        bis.close();
        bos.close();
        channelSftp.disconnect();
        session.disconnect();

    }

    private List<Payment_Rec> readFileDownloaded(String fileDownloadPath, Set<String> daysInFile) throws IOException {
        List<Payment_Rec> listPaymentSuccessInVPBank = new ArrayList<>();
        FileInputStream fis = new FileInputStream(fileDownloadPath);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                if (row.getCell(4) != null) {
                    Payment_Rec payment_rec = new Payment_Rec.PaymentRecBuilder()
                            .merchantId(String.valueOf(row.getCell(0)))
                            .terminalId(String.valueOf(row.getCell(1)))
                            .transType(String.valueOf(row.getCell(2)))
                            .requestCategory(String.valueOf(row.getCell(3)))
                            .transDatetime(String.valueOf(row.getCell(4)))
                            .authCode(String.valueOf(row.getCell(5)))
                            .accountNumber(String.valueOf(row.getCell(6)).replace("-", "x"))
                            .cardType(String.valueOf(row.getCell(7)))
                            .currency(String.valueOf(row.getCell(8)))
                            .amount(new BigDecimal(String.valueOf(row.getCell(9))).toBigInteger().toString())
                            .fee(new BigDecimal(String.valueOf(row.getCell(10))).toBigInteger().toString())
                            .vat(new BigDecimal(String.valueOf(row.getCell(11))).toBigInteger().toString())
                            .retRefNumber(new BigDecimal(String.valueOf(row.getCell(12))).toBigInteger().toString())
                            .postingStatus(String.valueOf(row.getCell(13)))
                            .transCondition(String.valueOf(row.getCell(14)))
                            .contractName(String.valueOf(row.getCell(15)))
                            .orderId(String.valueOf(row.getCell(16)))
                            .build();
                    listPaymentSuccessInVPBank.add(payment_rec);

                    String[] rawDate = String.valueOf(row.getCell(4)).split(" ")[0].split("-");
                    String convertDate = rawDate[2] + "/" + rawDate[1] + "/" + rawDate[0];
                    daysInFile.add(convertDate);
                }
            }
        }
        return listPaymentSuccessInVPBank;
    }

    private List<Payment_Rec> getDataPaymentGW(Set<String> daysInFile) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Payment_Rec> listPaymentSuccessInGW = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d{1,2}[-|/]\\d{1,2}[-|/]\\d{4}");
        try {
            for (String theDay : daysInFile) {
                Matcher matcher = pattern.matcher(theDay);
                if (matcher.matches()) {
                    List<Payment> paymentList = paymentService.getAllPaymentByCreateDate(14, theDay);
                    for (Payment payment : paymentList) {
                        System.out.printf("PAYMENT GW" + objectMapper.writeValueAsString(payment));
                        if(payment.getDescription() != null) {
                            Payment_Rec payment_rec = objectMapper.readValue(payment.getDescription(), Payment_Rec.class);
                            String date = payment_rec.getTransDatetime();
                            payment_rec.setTransDatetime(formatDateTime(date));
                            if(payment_rec.getAuthCode().equals("AUTH_CODE")){
                                String amount = payment_rec.getTransAmount();
                                payment_rec.setTransAmount("-"+amount);
                            }
                            listPaymentSuccessInGW.add(payment_rec);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        return listPaymentSuccessInGW;
    }

    private void filter(List<Payment_Rec> lstSuccesInVPBank, List<Payment_Rec> lstSuccesInGW, String fileDownloadPath) throws IOException {
        //TODO: loc giao dich
        List<Payment_Rec> successVPBankNotSuccessGW = new ArrayList<>();
        List<Payment_Rec> successGWNotSuccessVPBank = new ArrayList<>();
        for (Payment_Rec successVPBank : lstSuccesInVPBank) {
            if (!lstSuccesInGW.contains(successVPBank)) {
                successVPBankNotSuccessGW.add(successVPBank);
            }
        }
        for (Payment_Rec successGW : lstSuccesInGW) {
            if (!lstSuccesInVPBank.contains(successGW)) {
                successGWNotSuccessVPBank.add(successGW);
            }
        }
//        successVPBankNotSuccessGW.addAll(successGWNotSuccessVPBank);

        Map<String, List<Payment_Rec>> dataFilter = new TreeMap<>();
        dataFilter.put("SAI LECH", successVPBankNotSuccessGW);
//        dataFilter.put("SUCCESS_NL_NOT_VPBANK", successGWNotSuccessVPBank);
        //TODO:tao file excel du lieu sai lech
        XSSFWorkbook workbook = new XSSFWorkbook();
        //TODO: Dữ liệu sẽ được ghi xuống file exel
        Map<String, Payment_Rec> dataWriteFile = new TreeMap<>();

        dataWriteFile.put("1", new Payment_Rec.PaymentRecBuilder()
                .merchantId("MERCHANT_ID")
                .terminalId("TERMINAL_ID")
                .transType("TRANS_TYPE")
                .requestCategory("REQUEST_CATEGORY")
                .transDatetime("TRANSACTION_DATETIME")
                .authCode("AUTH_CODE")
                .accountNumber("ACCOUNT_NUMBER")
                .cardType("CARD_TYPE")
                .currency("CURR")
                .amount("AMOUNT")
                .fee("FEE")
                .vat("VAT")
                .retRefNumber("RET_REF_NUMBER")
                .postingStatus("POSTING_STATUS")
                .transCondition("TRANS_CONDITION")
                .contractName("CONTRACT_NAME")
                .orderId("MPGSORDERID")
                .build());

        //TODO: Duyệt và thêm dữ liệu từng row
        Set<String> keySetDataFilter = dataFilter.keySet();
        for (String keyDataFilter : keySetDataFilter) {
            XSSFSheet sheet = workbook.createSheet(keyDataFilter);
            List<Payment_Rec> payment_recList = dataFilter.get(keyDataFilter);
            for (Payment_Rec payment_rec : payment_recList) {
                dataWriteFile.put(String.valueOf(payment_recList.indexOf(payment_rec) + 2), payment_rec);
            }
            int rownum = 0;
            Set<String> keySetDataWrite = dataWriteFile.keySet();
            for (String keyDataWrite : keySetDataWrite) {
                Row row = sheet.createRow(rownum++);
                sheet.setDefaultColumnWidth(40);
                Payment_Rec payment_rec = dataWriteFile.get(keyDataWrite);
                Field[] fields = payment_rec.getClass().getDeclaredFields();
                JSONObject jsObject = new JSONObject(payment_rec);
                int cellnum = 0;
                for (Field field : fields) {
                    Cell cell = row.createCell(cellnum++);
                    Object value = jsObject.toMap().get(field.getName());
                    if (value instanceof String)
                        cell.setCellValue((String) value);
                    else if (value instanceof Integer)
                        cell.setCellValue((Integer) value);
                }
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(fileDownloadPath);
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }

    }

    private void uploadSFTP(File file, String SFTPWORKINGDIR) throws Exception {
        final String SFTP_HOST = MIGSConfig.SFTP_HOST;
        final int SFTP_PORT = MIGSConfig.SFTP_PORT;
        final String SFTP_USER = MIGSConfig.SFTP_USERNAME;
        Session session;
        Channel channel;
        ChannelSftp channelSftp;
        JSch jsch = new JSch();
        session = jsch.getSession(SFTP_USER, SFTP_HOST, SFTP_PORT);
        session.setConfig("PreferredAuthentications", "publickey,gssapi-with-mic,keyboard-interactive,password");
        session.setConfig("StrictHostKeyChecking", "no");
        ClassLoader classLoader = MigsScheduleService.class.getClassLoader();
        String fileAuth = Objects.requireNonNull(classLoader.getResource("vpb_migs/id_rsa")).getFile();
        jsch.addIdentity(fileAuth);
        session.connect();
        channel = session.openChannel("sftp");
        channel.connect();
        channelSftp = (ChannelSftp) channel;
        logger.info("###### FILE PATH: " + file.getAbsolutePath() + "--" + SFTPWORKINGDIR);
        channelSftp.put(file.getAbsolutePath(), SFTPWORKINGDIR);
        channelSftp.exit();
        session.disconnect();
    }

    private void sendMailToAM(String[] fileDownloadPath, String day, String month, String year) {
        String subjectMail = String.format("Hệ thống gửi file đối soát MIGS ngày %s tháng %s năm %s", day, month, year);
        String contentMail = String.format("Hệ thống gửi file đối soát MIGS ngày %s tháng %s năm %s ", day, month, year);
        String mailTo = "doisoat@alepay.vn";
        String mailCC = "";
//        String mailCC = "luongdt@nganluong.vn,vantt2@peacesoft.net,dungla@nganluong.vn";
        if (fileDownloadPath != null) {
            EmailUtil.sendMail(fileDownloadPath, subjectMail, contentMail,mailTo , mailCC);
        } else {
            String contentMail2 = String.format("Không tìm thấy file đối soát ngày %s tháng %s năm %s trên sFTP, địa chỉ 10.0.14.10",day, month, year);
            EmailUtil.sendMail(subjectMail, contentMail2, mailTo , mailCC);
        }
    }
    private String formatDateTime(String stringDate) throws ParseException {

        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = utcFormat.parse(stringDate);

        DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        pstFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

        String datetime = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(date);

        return datetime;
    }
}

