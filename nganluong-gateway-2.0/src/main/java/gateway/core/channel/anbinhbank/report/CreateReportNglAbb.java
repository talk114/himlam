package gateway.core.channel.anbinhbank.report;

import gateway.core.channel.anbinhbank.report.obj.NglReportRes;
import gateway.core.channel.anbinhbank.report.obj.TrxNglAbbInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;

public class CreateReportNglAbb extends BaseReport {
    private static final Logger logger = LogManager.getLogger(CreateReportNglAbb.class);
    static String subjectMail = "[Ngân lượng - ABB] Thông báo đối soát giao dịch ngày ";
    static String contentMail = "Danh sách các giao dịch thanh toán Ngân lượng - ABB gửi trong file đính kèm. <br/>";

    static String subjectMail2 = "[Ngân lượng - ABB] File LOG";
    static String contentMail2 = "File LOG. <br/>";

    static String timeReport = "";
    static String timeReport2 = "";

    public static void main(String args[]) {
        doiSoat();
    }

    //Test Doi Soat
    public static void doiSoat() {
        StringBuffer dataReport = new StringBuffer();
        dataReport.append("--------------STATUS LIST TRANSACTION--------------------\n");
        try {
            loadConfig();
        } catch (URISyntaxException | IOException e2) {
//            e2.printStackTrace();
            logger.error("Load config ABB fail");
        }

        Calendar calender = Calendar.getInstance();
        try {
            calender.setTime(new Date());
            calender.set(Calendar.DAY_OF_MONTH, calender.get(Calendar.DAY_OF_MONTH) - 1);
            timeReport = dfArg.format(calender.getTime());
            timeReport2 = dfFileName.format(calender.getTime());

            getReport(dataReport, timeReport, timeReport2);

        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
    }

    public static void doiSoatGW(String timeStr) {
        StringBuffer dataReport = new StringBuffer();
        dataReport.append("--------------STATUS LIST TRANSACTION--------------------\n");
        try {
            loadConfig();
        } catch (URISyntaxException | IOException e2) {
//            e2.printStackTrace();
            logger.error("Load config ABB fail");
        }

        try {
            timeReport = timeStr;
            String[] times = timeStr.split("-");
            timeReport2 = times[2] + times[1] + times[0];

            getReport(dataReport, timeReport, timeReport2);

        } catch (IOException | KeyManagementException | NoSuchAlgorithmException e) {
            logger.error(e.getMessage());
        }
    }

    //get RePort NgL and ABB
    private static void getReport(StringBuffer dataReport, String time1, String time2) throws NoSuchAlgorithmException, IOException, KeyManagementException {
        // TODO: Get Report Nganluong
        String nglRes = HttpUtil.sendGet(URL_NGL + time1, "DSABB*" + time2);

        NglReportRes trxVimoRes = mapper.readValue(nglRes, NglReportRes.class);
        List<TrxNglAbbInfo> nglTrxList = trxVimoRes.getReportLst();
        logger.info("NL: " + mapper.writeValueAsString(nglTrxList));
        dataReport.append("Status List trans of NL: " + nglRes + "\n");
        dataReport.append("NL: " + mapper.writeValueAsString(nglTrxList) + "\n");

        // TODO: Get report ABB
        List<TrxNglAbbInfo> abbList = parseFileAbb(
                PATH_IN + File.separatorChar + time2 + FILE_IN_NGL, dataReport);
        logger.info("ABB: " + mapper.writeValueAsString(abbList));
        dataReport.append("ABB: " + mapper.writeValueAsString(abbList) + "\n");

        List<TrxNglAbbInfo> abbSuccessLst = new ArrayList<>(abbList);
        List<TrxNglAbbInfo> nglSuccessLst = new ArrayList<>(nglTrxList);

        List<TrxNglAbbInfo> abbNglSuccessLst = new ArrayList<>(nglTrxList);

        abbSuccessLst.removeAll(nglTrxList);
        nglSuccessLst.removeAll(abbList);
        abbNglSuccessLst.retainAll(abbList);

        Map<String, List<TrxNglAbbInfo>> mapByReconcile = new HashMap<>();
        if (abbNglSuccessLst.size() > 0) {
            mapByReconcile.put(NGL_ABB_SUCCESS, abbNglSuccessLst);
        }
        if (abbSuccessLst.size() > 0) {
            mapByReconcile.put(ABB_SUCCESS, abbSuccessLst);
        }
        if (nglSuccessLst.size() > 0) {
            mapByReconcile.put(NGL_SUCCESS, nglSuccessLst);
        }

		createReportAndSendMail(mapByReconcile, dataReport);
		createReportToAbb(mapByReconcile);
    }

    private static List<TrxNglAbbInfo> parseFileAbb(String filePath, StringBuffer dataRep) throws IOException, NoSuchAlgorithmException {

        Path path = Paths.get(filePath);
        List<TrxNglAbbInfo> lst = new ArrayList<>();
        dataRep.append("----------Status List trans of ABB: --------------" + "\n");

        if (Files.exists(path)) {
            List<String> contentFile = new ArrayList<>();
            logger.info("Read File Ngl ABB: " + filePath);
            dataRep.append("ABB da day file doi soat " + "\n");
            dataRep.append("Read File Ngl ABB: " + filePath + "\n");
            contentFile = Files.readAllLines(path);

            if (!contentFile.isEmpty() && contentFile.size() > 2) {
                contentFile.remove(0);
                contentFile.remove(contentFile.size() - 1);
            }

            if (!contentFile.isEmpty()) {
                for (String s : contentFile) {
                    TrxNglAbbInfo data = parseLineReport(s);
                    lst.add(data);
                }
            }
        } else {
            dataRep.append("ABB chua day file doi soat" + "\n");
            logger.info("File of ABB: " + lst);
        }
        return lst;
    }

    private static TrxNglAbbInfo parseLineReport(String record) throws NoSuchAlgorithmException {
        String recordArr[] = record.split("\\|");

        // [01, 01, 0365750826, VND, 20000, 000001999639, 20190711160607,
        // ABB19080266800618127, , 1cfb001e6b925e9786940a98a1bd64e0]
        TrxNglAbbInfo nabTxn = new TrxNglAbbInfo();
        nabTxn.setRecordType(recordArr[0]);
        nabTxn.setTransType(recordArr[1]);
        nabTxn.setCurrencyCode(recordArr[2]);
        nabTxn.setAmount(recordArr[3]);
        nabTxn.setNlTransId(recordArr[4]);
        nabTxn.setDateTime(recordArr[5]);
        nabTxn.setBankTransId(recordArr[6]);
        nabTxn.setMerchantId(recordArr[7]);
        nabTxn.setBankResponseCode(recordArr[8]);
        nabTxn.setChecksum(recordArr[9]);

        String checksum = md5(nabTxn.rawData() + MD5_NGL);
        // if (!checksum.equals(nabTxn.getChecksum())) {
        // nabTxn.setDescription("Checksum invalid");
        // }
        return nabTxn;
    }

    private static void createReportAndSendMail(Map<String, List<TrxNglAbbInfo>> mapByReconcile, StringBuffer dataReport) {
        String sheetName = "Doi_Soat_ABB";// name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);

        // Header
        int idxRowHeader = 0;
        mergeCell(sheet, "Giao dịch NH ABB ngày " + timeReport, idxRowHeader, true);

        // Column Report
        int idxRowCol = 1;
        XSSFRow row1 = sheet.createRow(idxRowCol);
        row1.createCell(0).setCellValue("STT");
        row1.createCell(1).setCellValue("Mã GD Ngân lượng");
        row1.createCell(2).setCellValue("Mã GD Anbinhbank");
        row1.createCell(3).setCellValue("Loại GD");
        row1.createCell(4).setCellValue("Số tiền");
        row1.createCell(5).setCellValue("Ngày giờ gd");

        // Create Content Report
        int idxRowData = 2;

        if (!mapByReconcile.isEmpty()) {
            for (Map.Entry<String, List<TrxNglAbbInfo>> entry : mapByReconcile.entrySet()) {
                mergeCell(sheet, TRANS_STATUS_NGL.get(entry.getKey()), idxRowData, false);
                idxRowData++;
                List<TrxNglAbbInfo> listData = entry.getValue();
                int stt = 1;
                XSSFRow rowData;

                Integer totalAmount = 0;
                for (TrxNglAbbInfo record : listData) {
                    rowData = sheet.createRow(idxRowData);
                    rowData.createCell(0).setCellValue(stt);
                    rowData.createCell(1).setCellValue(record.getNlTransId());
                    rowData.createCell(2).setCellValue(record.getBankTransId());
                    rowData.createCell(3).setCellValue(record.getTransType());
                    rowData.createCell(4).setCellValue(record.getAmount());

                    CellStyle cellStyle = rowData.getSheet().getWorkbook().createCellStyle();
                    cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                    rowData.getCell(4).setCellStyle(cellStyle);

                    if (StringUtils.isNotBlank(record.getDateTime())) {
                        try {
                            rowData.createCell(5).setCellValue(dfReport.format(dfRecord.parse(record.getDateTime())));
                        } catch (ParseException e) {
                            logger.error(e);
                        }
                    }

                    totalAmount += Integer.valueOf(record.getAmount());
                    idxRowData++;
                    stt++;
                }
                rowData = sheet.createRow(idxRowData);
                rowData.createCell(4).setCellValue(totalAmount.toString());
                idxRowData++;

                CellStyle cellStyle = rowData.getSheet().getWorkbook().createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                rowData.getCell(4).setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        String AttachFileName = "Doi_soat_Ngl_ABB_" + timeReport + ".xlsx";
        String StatusReport = "Report_Doi_Soat_" + timeReport + ".txt";

        File file = null;
        FileOutputStream fileOut = null;
        try {
            file = new File(AttachFileName);
            file.createNewFile();
            fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (IOException e) {
            logger.error(e);
        }

        File file2 = null;
        FileOutputStream fileOut2 = null;
        try {
            file2 = new File(StatusReport);
            file2.createNewFile();
            fileOut2 = new FileOutputStream(file2);
            DataOutputStream dos = new DataOutputStream(fileOut2);
            dos.writeBytes(String.valueOf(dataReport));
//			wb.write(fileOut);
            dos.flush();
            dos.close();
        } catch (IOException e) {
            logger.error("Create Fail: \n" + e.getMessage());
//            System.exit(1);
        }

        sendMail(AttachFileName, subjectMail + timeReport, contentMail, MAIL_TO_NGL, MAIL_CC_NGL);
        sendMail(StatusReport, subjectMail + timeReport, contentMail, MAIL_CC_NGL, MAIL_CC_NGL);
    }

    private static void createReportToAbb(Map<String, List<TrxNglAbbInfo>> mapByReconcile)
            throws NoSuchAlgorithmException {
        String sheetName = "Doi_Soat_ABB";// name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);

        // Column Report
        int idxRowCol = 0;
        XSSFRow row1 = sheet.createRow(idxRowCol);
        row1.createCell(0).setCellValue("RecordType");
        row1.createCell(1).setCellValue("MsgType");
        row1.createCell(2).setCellValue("CurCode");
        row1.createCell(3).setCellValue("Amount");
        row1.createCell(4).setCellValue("TranId");
        row1.createCell(5).setCellValue("TranTime");
        row1.createCell(6).setCellValue("BankTranId");
        row1.createCell(7).setCellValue("BankResponseCode");
        row1.createCell(8).setCellValue("CheckSum");

        // Create Content Report
        int idxRowData = 1;

        if (!mapByReconcile.isEmpty()) {
            for (Map.Entry<String, List<TrxNglAbbInfo>> entry : mapByReconcile.entrySet()) {
                if (entry.getKey().equals(NGL_ABB_SUCCESS)) {
                    continue;
                }

                mergeCell(sheet, TRANS_STATUS_NGL.get(entry.getKey()), idxRowData, false);
                idxRowData++;
                List<TrxNglAbbInfo> listData = entry.getValue();
                XSSFRow rowData;
                Integer totalAmount = 0;
                for (TrxNglAbbInfo record : listData) {
                    rowData = sheet.createRow(idxRowData);
                    rowData.createCell(0).setCellValue("01");
                    rowData.createCell(1).setCellValue("01");
                    rowData.createCell(2).setCellValue("VND");
                    rowData.createCell(3).setCellValue(record.getAmount());
                    rowData.createCell(4).setCellValue(record.getNlTransId());
                    rowData.createCell(5).setCellValue(record.getDateTime());
                    rowData.createCell(6).setCellValue(record.getBankTransId());
                    rowData.createCell(7).setCellValue(record.getBankResponseCode());
                    String rawData = "01" + "01" + "VND" + record.getAmount() + record.getNlTransId()
                            + record.getDateTime() + record.getBankTransId() + record.getBankResponseCode();
                    rowData.createCell(8).setCellValue(md5(rawData + MD5_NGL));

                    CellStyle cellStyle = rowData.getSheet().getWorkbook().createCellStyle();
                    cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                    rowData.getCell(3).setCellStyle(cellStyle);

                    totalAmount += Integer.valueOf(record.getAmount());
                    idxRowData++;
                }
                rowData = sheet.createRow(idxRowData);
                rowData.createCell(3).setCellValue(totalAmount.toString());

                CellStyle cellStyle = rowData.getSheet().getWorkbook().createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.RIGHT);
                rowData.getCell(3).setCellStyle(cellStyle);
                idxRowData++;
            }
        }

        for (int i = 0; i < 9; i++) {
            sheet.autoSizeColumn(i);
        }

        String AttachFileName = PATH_OUT + File.separatorChar + timeReport2 + "_TRANS_DISPUTE_NGL.xlsx";
        File file = null;
        FileOutputStream fileOut = null;
        try {
            file = new File(AttachFileName);
            file.createNewFile();
            fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            logger.error("Create Report Fail: \n" + e.getMessage());
//            System.exit(1);
        }
    }
}
