package vn.nganluong.naba.service.impl;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.nganluong.naba.entities.PgMail;
import vn.nganluong.naba.repository.PgMailRepository;
import vn.nganluong.naba.service.MailService;

@Service
public class MailServiceImpl implements MailService {

	@Autowired
	PgMailRepository pgMailRepository;

	public void sendMail(String subject, String content, String... fileNames) {

		PgMail pgMail = pgMailRepository.findFirstByStatusOrderByIdAsc(true);

		final String username = pgMail.getUserName();
		final String password = pgMail.getPassword();

		// Authenticator TLS
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", pgMail.getHost());
		props.put("mail.smtp.port", pgMail.getPort());

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			String encodingOptions = "text/html; charset=UTF-8";
			MimeMessage message = new MimeMessage(session);
			message.setHeader("Content-Type", encodingOptions);
			message.setFrom(new InternetAddress(pgMail.getMailFromAddress(), pgMail.getMailFromName()));

			if (StringUtils.isNotBlank(pgMail.getMailTo())) {
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(pgMail.getMailTo()));
			}

			if (StringUtils.isNotBlank(pgMail.getMailCc())) {
				message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(pgMail.getMailCc()));
			}

			message.setSubject(subject, "UTF-8");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Content Mail
			StringBuilder sb = new StringBuilder();
			sb.append(content);

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(sb.toString(), "text/html; charset=utf-8");
			multipart.addBodyPart(messageBodyPart);

			// Attachment mail
			// https://stackoverflow.com/a/3177640
			if (fileNames != null && fileNames.length > 0) {
				for (int i = 0; i < fileNames.length; i++) {
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(fileNames[i]);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(source.getName());
					messageBodyPart.setDescription(source.getName());
					multipart.addBodyPart(messageBodyPart);
				}
			}

			// Send the complete message parts
			message.setContent(multipart);

			Transport.send(message);

//			try {
//				TimeUnit.SECONDS.sleep(5);
//			} catch (InterruptedException e) {
//				logger.info(ExceptionUtils.getStackTrace(e));
//			}
//			File file = new File(fileName);
//
//			if (file.delete()) {
//				System.out.println(file.getName() + " is deleted!");
//			} else {
//				System.out.println("Delete operation is failed.");
//			}
			System.out.println("Send mail SUCCESS");

		} catch (Exception e) {
			System.out.println("Send mail Fail \n" + e.getMessage());
		}
	}

}