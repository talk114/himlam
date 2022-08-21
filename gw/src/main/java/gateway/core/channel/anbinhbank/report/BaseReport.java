package gateway.core.channel.anbinhbank.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseReport {

    private static final Logger logger = LogManager.getLogger(BaseReport.class);
    static ObjectMapper mapper = new ObjectMapper();
    static SimpleDateFormat dfArg = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat dfArg2 = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat dfFileName = new SimpleDateFormat("yyyyMMdd");
    static SimpleDateFormat dfRecord = new SimpleDateFormat("yyyyMMddHHmmss");
    static SimpleDateFormat dfReport = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static String PATH_IN = "/data/upload/live/doisoat/abb/abb_in";
    public static String PATH_OUT = "/data/upload/live/doisoat/abb/abb_out";

    public static String FILE_IN_NGL = "_TRANSGATE_ABBNGANLUONG.txt";

    public static String FILE_OUT_NGL = "_TRANSGATE_ABBVIMO";

    public static String MD5_NGL = "ABBVIMO2019ABBVIMO2019ABBVIMO019";

    //	public static String URL_NGL = "http://10.1.121.20:8940/payment/payment-report/nab";
    public static String URL_NGL = "https://www.nganluong.vn/api/abbCompare?time=";

    public static String MAIL_TO_NGL = "doisoat@nganluong.vn";
    public static String MAIL_CC_NGL = "haind@nganluong.vn";

    public static String MAIL_FROM = "no-reply@nganluong.vn";
    public static String USER_MAIL_AUTHEN = "nganluong.dev@peacesoft.net";
    public static String PASS_MAIL_AUTHEN = "4e632218-ca83-4a70-92f2-fda951dd511e";
    public static String HOST_MAIL = "smtp.elasticemail.com";
    public static String PORT_MAIL = "2525";

    public static String ABB_SUCCESS = "001";
    public static String NGL_ABB_SUCCESS = "000";
    public static String NGL_SUCCESS = "002";

    public static final Map<String, String> TRANS_STATUS_NGL = new HashMap<>();

    static {
        TRANS_STATUS_NGL.put(NGL_ABB_SUCCESS, "000 - GD thành công 2 bên, đối chiếu khớp đúng");
        TRANS_STATUS_NGL.put(ABB_SUCCESS, "001 - ABB thành công, Ngân lượng thất bại");
        TRANS_STATUS_NGL.put(NGL_SUCCESS, "002 - Ngân lượng thành công, ABB thất bại");
    }

    public static final Map<String, String> TRANS_TYPE = new HashMap<>();

    static {
        TRANS_TYPE.put("01", "01 - Nạp ví");
        TRANS_TYPE.put("02", "02 - Rút ví");
        TRANS_TYPE.put("03", "03 - Thanh toán online");
    }

    protected static void loadConfig() throws URISyntaxException, IOException {
        Properties prop = new Properties();

        String fileConfig = "ConfigABB.properties";
        File jarFile = new File(
                BaseReport.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        String inputFilePath = jarFile.getParent() + File.separator + fileConfig;

        File config = new File(inputFilePath);
        if (config.exists()) {
            FileInputStream inStream = new FileInputStream(new File(inputFilePath));
            prop.load(inStream);
        } else {
            InputStream inStream = BaseReport.class.getClassLoader().getResourceAsStream(fileConfig);
            prop.load(inStream);
        }
        PATH_IN = prop.getProperty("PATH_IN");
        PATH_OUT = prop.getProperty("PATH_OUT");

        FILE_IN_NGL = prop.getProperty("FILE_IN_NGL");

        FILE_OUT_NGL = prop.getProperty("FILE_OUT_NGL");

        MD5_NGL = prop.getProperty("MD5_NGL");

        URL_NGL = prop.getProperty("URL_NGL");

        MAIL_TO_NGL = prop.getProperty("MAIL_TO_NGL");
        MAIL_CC_NGL = prop.getProperty("MAIL_CC_NGL");

        MAIL_FROM = prop.getProperty("MAIL_FROM");
        USER_MAIL_AUTHEN = prop.getProperty("USER_MAIL_AUTHEN");
        PASS_MAIL_AUTHEN = prop.getProperty("PASS_MAIL_AUTHEN");
        HOST_MAIL = prop.getProperty("HOST_MAIL");
        PORT_MAIL = prop.getProperty("PORT_MAIL");
    }

    protected static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] byteData = null;
        try {
            byteData = instance.digest(input.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            logger.info(ExceptionUtils.getStackTrace(e));
        }
        String hexString = DatatypeConverter.printHexBinary(byteData);
        return hexString.toLowerCase();
    }

    protected static void sendMail(String fileName, String subject, String content, String mailTo, String mailCC) {

        final String username = USER_MAIL_AUTHEN;
        final String password = PASS_MAIL_AUTHEN;

        //Authenticator TLS
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST_MAIL);
        props.put("mail.smtp.port", PORT_MAIL);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_FROM, "GATEWAY"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailCC));
            message.setSubject(subject);

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Content Mail
            StringBuilder sb = new StringBuilder();
            sb.append("<b>Gửi bộ phận xử lý đối soát.</b> <br/>");
            sb.append(content);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(sb.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(messageBodyPart);

            // Attachment mail
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(source.getName());
            messageBodyPart.setDescription(source.getName());
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                logger.info(ExceptionUtils.getStackTrace(e));
            }
            File file = new File(fileName);

            if (file.delete()) {
                logger.info(file.getName() + " is deleted!");
            } else {
                logger.info("Delete operation is failed.");
            }
            logger.info("Send mail SUCCESS");

        } catch (Exception e) {
            logger.error("Send mail Fail \n" + e.getMessage());
            //System.exit(1);
        }
    }

    //send mail report
    protected static void sendMailReportError(String subject, String content, String mailTo) {

        final String username = USER_MAIL_AUTHEN;
        final String password = PASS_MAIL_AUTHEN;

        //Authenticator TLS
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST_MAIL);
        props.put("mail.smtp.port", PORT_MAIL);

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_FROM, "GATEWAY"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
//			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailCC));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                logger.info(ExceptionUtils.getStackTrace(e));
            }
            logger.info("Send mail SUCCESS");

        } catch (Exception e) {
            logger.error("Send mail Fail \n" + e.getMessage());

        }
    }

    protected static void mergeCell(XSSFSheet sheet, String cellValue, int idxRow, boolean bold) {
        int idxCol = idxRow + 1;
        String[] cellStrings = ("A" + idxCol + ":G" + idxCol).split(":");
        CellReference start = new CellReference(cellStrings[0]);
        CellReference end = new CellReference(cellStrings[1]);

        CellRangeAddress address = new CellRangeAddress(start.getRow(), end.getRow(), start.getCol(), end.getCol());
        sheet.addMergedRegion(address);

        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();

        XSSFFont font = sheet.getWorkbook().createFont();
        font.setBold(bold);
        style.setFont(font);

        XSSFRow row0 = sheet.createRow(idxRow);
        row0.createCell(0).setCellValue(cellValue);

        row0.getCell(0).setCellStyle(style);
    }
}
