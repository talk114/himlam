package gateway.core.channel.bidv.bidv_transfer_247.dto.report;

import gateway.core.channel.anbinhbank.report.BaseReport;
import gateway.core.channel.bidv.bidv_transfer_247.object247.TransInfoDS;
import gateway.core.util.EmailUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FormReportBIDV {

    public static String BIDV_MAIL_TO_KT = "";
    public static String BIDV_MAIL_CC_KT = "";
    static String subjectMail = "";
    static String contentMail = "Danh sách các giao dịch thanh toán Ngân lượng - BIDV gửi trong file đính kèm. <br/>";

    static SimpleDateFormat dfArg = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat dfFileName = new SimpleDateFormat("yyyyMMdd");

    static String timeReport = "";
    static String timeReport2 = "";

    public static void main(String[] args) {
        List<TransInfoDS> listData = new ArrayList<>();
        TransInfoDS infoDS = new TransInfoDS("test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test","test",
                "test","test","test");
//        listData.add(infoDS);
        sendEmailBIDV(listData, 1);
    }

    public static void sendEmailBIDV(List<TransInfoDS> listData, int typeFile) {
        StringBuffer dataReport = new StringBuffer();
        dataReport.append("--------------STATUS LIST TRANSACTION--------------------\n");
        try {
            Calendar calender = Calendar.getInstance();
            calender.setTime(new Date());
            calender.set(Calendar.DAY_OF_MONTH, calender.get(Calendar.DAY_OF_MONTH));
            timeReport = dfArg.format(calender.getTime());
            timeReport2 = dfFileName.format(calender.getTime());
            EmailUtil.loadConfigEmail();

            //set email-to, email-cc
            Properties prop = new Properties();

            String fileConfig = "configEmail.properties";
            File jarFile = new File(
                    EmailUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            String inputFilePath = jarFile.getParent() + File.separator + fileConfig;

            File config = new File(inputFilePath);
            if (config.exists()) {
                FileInputStream inStream = new FileInputStream(new File(inputFilePath));
                prop.load(inStream);
            } else {
                InputStream inStream = BaseReport.class.getClassLoader().getResourceAsStream(fileConfig);
                prop.load(inStream);
            }
            BIDV_MAIL_TO_KT = prop.getProperty("BIDV_MAIL_TO_KT");
            BIDV_MAIL_CC_KT = prop.getProperty("BIDV_MAIL_CC_KT");

            // set subject email
            subjectMail = typeFile == 1 ? "Fileout BIDV ngày " : "Fileout-Refund BIDV ngày ";

            exportExcelFile(listData);
        } catch (URISyntaxException | IOException | NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            System.out.println("Load config Email fail");
        }
    }

    private static void exportExcelFile(List<TransInfoDS> listData) throws NoSuchAlgorithmException {
        String sheetName = "Doi_Soat_BIDV";// name of sheet

        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet(sheetName);
        mergeCell(sheet, "Giao dịch thành công BIDV", 0, true);

        // Column Report
        int idxRowCol = 1;
        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        XSSFRow row1 = sheet.createRow(idxRowCol);
        row1.createCell(0).setCellValue("STT");
        row1.createCell(1).setCellValue("Mã kênh giao dịch");
        row1.createCell(2).setCellValue("Mã dịch vụ/Mã xử lý");
        row1.createCell(3).setCellValue("Mã dịch vụ chi tiết");
        row1.createCell(4).setCellValue("Mã hóa đơn");
        row1.createCell(5).setCellValue("Mã khách hàng tại đối tác");
        row1.createCell(6).setCellValue("Số tiền giao dịch");
        row1.createCell(7).setCellValue("Mã tiền tệ giao dịch");
        row1.createCell(8).setCellValue("Số Trace hệ thống");
        row1.createCell(9).setCellValue("Giờ khởi tạo giao dịch ");
        row1.createCell(10).setCellValue("Ngày khởi tạo giao dịch");
        row1.createCell(11).setCellValue("Giờ thanh toán giao dịch");
        row1.createCell(12).setCellValue("Ngày thanh toán giao dịch");
        row1.createCell(13).setCellValue("Tài khoản ghi nợ");
        row1.createCell(14).setCellValue("Tài khoản ghi có");
        row1.createCell(15).setCellValue("Số thẻ");
        row1.createCell(16).setCellValue("Mã số thiết bị chấp nhận thẻ");
        row1.createCell(17).setCellValue("Kết quả đối soát");
        row1.createCell(18).setCellValue("Yêu cầu của Đối tác");
        row1.createCell(19).setCellValue("Loại giao dịch \n(TTHD/Rút ví điện tử/Nạp ví điện tử)");
        row1.createCell(20).setCellValue("Thông tin bổ sung");
        row1.createCell(21).setCellValue("ID giao dịch do hệ thống BIDV sinh");
        row1.getCell(19).setCellStyle(style);

        // Create Content Report
        int idxRowData = 1;

        if (listData.size() > 0) {
            idxRowData++;
            XSSFRow rowData;
            int stt = 1;
            for (TransInfoDS record : listData) {
                rowData = sheet.createRow(idxRowData);
                rowData.createCell(0).setCellValue(stt);
                rowData.createCell(1).setCellValue(record.getChanelId());
                rowData.createCell(2).setCellValue(record.getServiceId());
                rowData.createCell(3).setCellValue(record.getServiceProfile());
                rowData.createCell(4).setCellValue(record.getOrderCode());
                rowData.createCell(5).setCellValue(record.getCustomerId());
                rowData.createCell(6).setCellValue(record.getAmount());
                rowData.createCell(7).setCellValue(record.getCurrencyCode());
                rowData.createCell(8).setCellValue(record.getTraceNumber());
                rowData.createCell(9).setCellValue(convertFormatTime("HHmmss", record.getCreateTime()));
                rowData.createCell(10).setCellValue(convertFormatDate("yyyyMMdd", record.getCreateDate()));
                rowData.createCell(11).setCellValue(convertFormatTime("HHmmss", record.getPaymentTime()));
                rowData.createCell(12).setCellValue(convertFormatDate("yyyyMMdd", record.getPaymentDate()));
                rowData.createCell(13).setCellValue(record.getAccountOut());
                rowData.createCell(14).setCellValue(record.getAccountIn());
                rowData.createCell(15).setCellValue(record.getCardNumber());
                rowData.createCell(16).setCellValue(record.getDeviceCodeAcceptsCard());
                rowData.createCell(17).setCellValue(record.getResultsDS());
                rowData.createCell(18).setCellValue(record.getRequestDS());
                rowData.createCell(19).setCellValue(record.getTypeTran().equals("01") ? "Giao dịch thanh toán" : record.getTypeTran().equals("02") ? "Giao dịch nạp tiền ví điện tử" : "Giao dịch rút tiền ví điện tử");
                rowData.createCell(20).setCellValue(record.getMoreInfo());
                rowData.createCell(21).setCellValue(record.getTranIdBIDV());

                idxRowData++;
                stt++;
            }
        }

        for (int i = 0; i < 22; i++) {
            sheet.autoSizeColumn(i);
        }

        // write file
//        String PATH_FOLDER_FILE_DS = "D:/data/";//local
        String PATH_FOLDER_FILE_DS = "/data/doisoat/bidv/uat/";//uat
        File dir = new File(PATH_FOLDER_FILE_DS);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = timeReport2 + "_TRANS_BIDV_NGL.xlsx";
        String partFile = PATH_FOLDER_FILE_DS + File.separator + fileName;

        File file = null;
        FileOutputStream fileOut = null;
        try {
            file = new File(partFile);
            file.createNewFile();
            fileOut = new FileOutputStream(file);
            wb.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception e) {
            System.err.println("Create Report Fail: \n" + e.getMessage());
            System.exit(1);
        }

        EmailUtil.sendMail(partFile, subjectMail + timeReport, contentMail, BIDV_MAIL_TO_KT, BIDV_MAIL_CC_KT);

    }

    protected static void mergeCell(XSSFSheet sheet, String cellValue, int idxRow, boolean bold) {
        int idxCol = idxRow + 1;
        String[] cellStrings = ("A" + idxCol + ":V" + idxCol).split(":");
        CellReference start = new CellReference(cellStrings[0]);
        CellReference end = new CellReference(cellStrings[1]);

        CellRangeAddress address = new CellRangeAddress(start.getRow(), end.getRow(), start.getCol(), end.getCol());
        sheet.addMergedRegion(address);

        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();

        XSSFFont font = sheet.getWorkbook().createFont();
        font.setBold(bold);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        XSSFRow row0 = sheet.createRow(idxRow);
        row0.createCell(0).setCellValue(cellValue);

        row0.getCell(0).setCellStyle(style);
    }

    private static String convertFormatDate(String partern, String dateString) {
        if (partern.equals("yyyyMMdd")) {
            String year = dateString.substring(0, 4);
            String moth = dateString.substring(4, 6);
            String day = dateString.substring(6);
            return day + "/" + moth + "/" + year;
        }
        return dateString;
    }

    private static String convertFormatTime (String partern, String timeString) {
        if (partern.equals("HHmmss")) {
            String hour = timeString.substring(0, 2);
            String minute = timeString.substring(2, 4);
            String seconds = timeString.substring(4);
            return hour + ":" + minute + ":" + seconds;
        }
        return timeString;
    }

}
