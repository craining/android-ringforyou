package com.zgy.ringforu.bean;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import com.zgy.ringforu.util.ReceiveEmailUtil;

import android.util.Log;

public class MyEmailFromServer {

	private static final String TAG = "MyEmailFromServer";
	private String dateformat = "yy-MM-dd��HH:mm";// Ĭ�ϵ���ǰ��ʾ��ʽ

	private MimeMessage mimeMessage;

	private static final String FILEPATH_UPDATE_FILE = "/mnt/sdcard/ringforu/";// �������������ĸ����ļ�

	public MyEmailFromServer(MimeMessage m) {
		this.mimeMessage = m;
	}

	public String getFromAddr() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String from = address[0].getAddress();
		if (from == null) {
			from = "";
		}
		return from;
	}

	public String getFromPerson() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String person = address[0].getPersonal();
		if (person == null) {
			person = "";
		}
		return person;
	}

	/**
	 * ����*������ʼ����� ����
	 */
	public String getSubject() throws Exception {
		String subject = "";
		subject = MimeUtility.decodeText(mimeMessage.getSubject());
		if (subject == null)
			subject = "";

		return subject;
	}

	/**
	 * ����*������ʼ�����ʱ�䡡
	 */
	public String getSendDateString() throws Exception {
		Date senddate = mimeMessage.getSentDate();
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return format.format(senddate);
	}

	/**
	 * ����*������ʼ�����ʱ�䡡��
	 */
	public Long getSendDateLong() throws Exception {
		Date senddate = mimeMessage.getSentDate();
		return senddate.getTime();
	}

	/**
	 * ����ô��ʼ���Message-ID ����
	 * 
	 * @throws MessagingException
	 */
	public String getMessageId() throws MessagingException {
		return mimeMessage.getMessageID();
	}

	/**
	 * ����*���жϴ��ʼ��Ƿ���Ҫ��ִ�������Ҫ��ִ����"true",���򷵻�"false" ����
	 * 
	 * @throws MessagingException
	 */
	public boolean getReplySign() throws MessagingException {
		boolean replysign = false;
		String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");
		if (needreply != null) {
			replysign = true;
		}
		return replysign;
	}

	/**
	 * ��*�����жϴ��ʼ��Ƿ��Ѷ������δ�����ط���false,��֮����true�� ����
	 * 
	 * @throws MessagingException
	 */
	public boolean isNew() throws MessagingException {
		boolean isnew = false;
		Flags flags = ((Message) mimeMessage).getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		for (int i = 0; i < flag.length; i++) {
			if (flag[i] == Flags.Flag.SEEN) {
				isnew = true;
				break;
			}
		}
		return isnew;
	}

	// ���ظ���
	public void downloadAttach(String mailID) throws Exception {
		saveAttachMent((Part) mimeMessage);
	}

	/**
	 * ��*�������渽���� ��
	 * 
	 * @throws Exception
	 * @throws IOException
	 * @throws MessagingException
	 * @throws Exception
	 */
	private void saveAttachMent(Part part) throws Exception {
		String fileName = "";
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
					fileName = mpart.getFileName();
					if (fileName.toLowerCase().indexOf("gbk") != -1) {
						fileName = MimeUtility.decodeText(fileName);
					}
					saveFile(fileName, mpart.getInputStream());
				} else if (mpart.isMimeType("multipart/*")) {
					saveAttachMent(mpart);
				} else {
					fileName = mpart.getFileName();
					if ((fileName != null) && (fileName.toLowerCase().indexOf("GB2312") != -1)) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(fileName, mpart.getInputStream());
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachMent((Part) part.getContent());
		}
	}

	/**
	 * ��*���������ı��渽����ָ��Ŀ¼� ��
	 */
	private void saveFile(String fileName, InputStream in) throws Exception {
		String osName = System.getProperty("os.name");
		Log.e(TAG, "osName = " + osName);
		if (osName == null)
			osName = "";

		if (!new File(FILEPATH_UPDATE_FILE).exists()) {
			new File(FILEPATH_UPDATE_FILE).mkdirs();
		}

		File storefile = new File(MimeUtility.decodeText(FILEPATH_UPDATE_FILE) + MimeUtility.decodeText(fileName));
		Log.e(TAG, "storefile's path: " + storefile.toString());
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(storefile));
			bis = new BufferedInputStream(in);
			int c;
			while ((c = bis.read()) != -1) {
				bos.write(c);
				bos.flush();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new Exception("�ļ�����ʧ��!");
		} finally {
			bos.close();
			bis.close();
		}
	}

	/**
	 * ����*�������ʼ����ѵõ����ʼ����ݱ��浽һ��StringBuffer�����У������ʼ� ����*����Ҫ�Ǹ���MimeType���͵Ĳ�ִͬ�в�ͬ�Ĳ�����һ��һ���Ľ��� ����
	 */
	public String getMailContent(Part part) throws Exception {
		StringBuffer bodytext = new StringBuffer();
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1)
			conname = true;
		if (part.isMimeType("text/plain") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("text/html") && !conname) {
			bodytext.append((String) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i));
			}
		} else if (part.isMimeType("message/rfc822")) {
			getMailContent((Part) part.getContent());
		} else {
		}

		return bodytext.toString();
	}

	/**
	 * ��*���жϴ��ʼ��Ƿ�������� ��
	 * 
	 * @throws MessagingException
	 */
	public boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
		if (part.isMimeType("multipart/*")) {
			Multipart mp = (Multipart) part.getContent();
			// ��ȡ�������ƿ��ܰ����������
			for (int j = 0; j < mp.getCount(); j++) {
				BodyPart mpart = mp.getBodyPart(j);
				String disposition = mpart.getDescription();
				if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE)))) {
					attachflag = true;
				} else if (mpart.isMimeType("multipart/*")) {
					attachflag = isContainAttach((Part) mpart);
				} else {
					String contype = mpart.getContentType();
					if (contype.toLowerCase().indexOf("application") != -1)
						attachflag = true;
					if (contype.toLowerCase().indexOf("name") != -1)
						attachflag = true;
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			attachflag = isContainAttach((Part) part.getContent());
		}
		return attachflag;
	}
}
