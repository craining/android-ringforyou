package com.zgy.ringforu.util;

import java.util.Enumeration;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class SendEmailUtil extends Authenticator {

	public static final String host = "smtp.163.com";
	// public static final String host = "smtp.gmail.com";

	private final String userName = "little__gg@163.com";
	private final String password = "6803895199";

	private Session session;

	private void initialize() {
		Properties props = new Properties();

		// //Gmail, ��δʵ��
		// props.setProperty("mail.smtp.host", "smtp.gmail.com");
		// props.setProperty("mail.smtp.socketFactory.class",
		// "javax.net.ssl.SSLSocketFactory");
		// props.setProperty("mail.smtp.socketFactory.fallback", "false");
		// props.setProperty("mail.smtp.port", "465");
		// props.setProperty("mail.smtp.socketFactory.port", "465");
		// props.put("mail.smtp.auth", "true");
		// 163
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "25");
		session = Session.getDefaultInstance(props, this);

	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

	/**
	 * ����Email
	 * 
	 * @param subject
	 *            ����
	 * @param body
	 *            ����
	 * @param sender
	 *            ������
	 * @param recipients
	 *            ������
	 * @throws MessagingException
	 * @throws AddressException
	 * */
	public synchronized void sendMail(String subject, String body, String recipients) throws AddressException, MessagingException {

		initialize(); // ��ʼ��

		MimeMessage message = new MimeMessage(session);
		DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));

		/*
		 * ����MIME��Ϣ
		 */
		message.setSender(new InternetAddress(userName));
		message.setSubject(subject);
		message.setDataHandler(handler);
		
		
		if (recipients.contains(",")) {
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
		} else {
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
		}

		Transport.send(message); // ����
          
	}
}
