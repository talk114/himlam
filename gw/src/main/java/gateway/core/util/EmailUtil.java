package gateway.core.util;

import gateway.core.channel.anbinhbank.report.BaseReport;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class EmailUtil {
    private static final Logger logger = LogManager.getLogger(EmailUtil.class);

    public static String MAIL_FROM = "no-reply@nganluong.vn";
    public static String USER_MAIL_AUTHEN = "nganluong.dev@peacesoft.net";
    public static String PASS_MAIL_AUTHEN = "4e632218-ca83-4a70-92f2-fda951dd511e";
    public static String HOST_MAIL = "smtp.elasticemail.com";
    public static String PORT_MAIL = "2525";
    public static String WITHDRAW = "withdraw@nganluong.vn";
    public static final String RECONCILIATION = "doisoat@nganluong.vn";

    public static void loadConfigEmail() throws URISyntaxException, IOException {
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
        MAIL_FROM = prop.getProperty("MAIL_FROM");
        USER_MAIL_AUTHEN = prop.getProperty("USER_MAIL_AUTHEN");
        PASS_MAIL_AUTHEN = prop.getProperty("PASS_MAIL_AUTHEN");
        HOST_MAIL = prop.getProperty("HOST_MAIL");
        PORT_MAIL = prop.getProperty("PORT_MAIL");
    }

    public static void sendMail(String fileName, String subject, String content, String mailTo, String mailCC) {

        final String username = USER_MAIL_AUTHEN;
        final String password = PASS_MAIL_AUTHEN;

        //Authenticator TLS
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST_MAIL);
        props.put("mail.smtp.port", PORT_MAIL);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_FROM, "GATEWAY"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailCC));
            message.setSubject(subject, "utf-8");

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

//            if (file.delete()) {
//                System.out.println(file.getName() + " is deleted!");
//            } else {
//                System.out.println("Delete operation is failed.");
//            }
            System.out.println("Send mail SUCCESS");

        } catch (Exception e) {
            System.out.println("Send mail Fail \n" + e.getMessage());
        }
    }

    public static void sendMail(String subject, String content, String mailTo, String mailCC) {

        final String username = USER_MAIL_AUTHEN;
        final String password = PASS_MAIL_AUTHEN;

        //Authenticator TLS
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST_MAIL);
        props.put("mail.smtp.port", PORT_MAIL);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_FROM, "GATEWAY"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailCC));
            message.setSubject(subject, "utf-8");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Content Mail
            StringBuilder sb = new StringBuilder();
            sb.append("<b>Gửi bộ phận xử lý đối soát.</b> <br/>");
            sb.append(content);

            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(sb.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                logger.info(ExceptionUtils.getStackTrace(e));
            }
            System.out.println("Send mail SUCCESS");

        } catch (Exception e) {
            System.out.println("Send mail Fail \n" + e.getMessage());
        }
    }

    public static void sendMail(String[] fileNames, String subject, String content, String mailTo, String mailCC) {

        final String username = USER_MAIL_AUTHEN;
        final String password = PASS_MAIL_AUTHEN;

        //Authenticator TLS
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST_MAIL);
        props.put("mail.smtp.port", PORT_MAIL);

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(MAIL_FROM, "GATEWAY"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mailCC));
            message.setSubject(subject, "utf-8");

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
            for (String filename : fileNames) {
                addAttachment(multipart,filename);
            }

            // Send the complete message parts
            message.setContent(multipart);

            Transport.send(message);

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                logger.info(ExceptionUtils.getStackTrace(e));
            }
            System.out.println("Send mail SUCCESS");

        } catch (Exception e) {
            System.out.println("Send mail Fail \n" + e.getMessage());
        }
    }

    private static void addAttachment(Multipart multipart, String filename) throws MessagingException {
        DataSource source = new FileDataSource(filename);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(source.getName());
        messageBodyPart.setDescription(source.getName());
        multipart.addBodyPart(messageBodyPart);
    }
}
