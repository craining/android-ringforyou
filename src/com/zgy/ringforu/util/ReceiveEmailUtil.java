package com.zgy.ringforu.util;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import android.util.Log;

import com.zgy.ringforu.bean.MyEmailFromServer;

public class ReceiveEmailUtil {

	public static String serverhost = "pop.163.com";
	public static String serverPort = "110";
	public static String emailname = "craining@163.com";
	public static String emailpassword = "wszgy555,,!";

	private static final String TAG = "ReceiveEmailUtil";

	public Store store;

	public static void getAllEmails() throws Exception {
		Properties props = new Properties();

		props.put("mail.pop3.host", serverhost);
		props.put("mail.pop3.port", serverPort);
		props.put("mail.pop3.auth", "true");

		Session session = Session.getInstance(props, null);
		Store store = session.getStore("pop3");
		store.connect(serverhost, emailname, emailpassword);
		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);

		Message message[] = folder.getMessages();

		
		
//		for (int i = 0; i < message.length; i++) {
//			Log.i(TAG, message[i].toString());
//			// message[i].setFlag(Flags.Flag.SEEN, true);
//			MyEmailFromServer msg = new MyEmailFromServer(msg);
//		}
		folder.close(true);
		store.close();
	}

	// // 删除邮件
	// public static void deleteMail(String delID) throws Exception {
	// Properties props = new Properties();
	// Session session = Session.getDefaultInstance(props, null);
	// Store store = session.getStore("pop3");
	// store.connect(emailhost, emailname, emailpassword);
	//
	// Folder folder = store.getFolder("INBOX");
	// folder.open(Folder.READ_WRITE);// 注意这里与读取时的区别
	// Message message[] = folder.getMessages();
	//
	// ReceiveEmailUtil pmm = null;
	// for (int i = 0; i < message.length; i++) {
	// String getID = getMessageId((MimeMessage) message[i]);
	// if (getID != null && getID.equals(delID)) {
	// message[i].setFlag(Flags.Flag.DELETED, true);
	// }
	// }
	// folder.close(true);// 注意这里与读取的区别，这里是确认删除的操作....
	// store.close();
	//
	// }

}
