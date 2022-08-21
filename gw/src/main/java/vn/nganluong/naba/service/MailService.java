package vn.nganluong.naba.service;

public interface MailService {
	public void sendMail(String subject, String content, String... fileNames);
}