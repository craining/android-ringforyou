package com.zgy.ringforu.tools.busymode;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.zgy.ringforu.MainActivityGroup;
import com.zgy.ringforu.R;
import com.zgy.ringforu.util.ContactsUtil;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.MainUtil;

public class BusyModeUtil {

	public static final File FILE_BUSYMODE_SWITCH = new File("/data/data/com.zgy.ringforu/busymodeswitch");// ����
	public static final File FILE_BUSYMODE_MESSAGE = new File("/data/data/com.zgy.ringforu/files/busymodemsg.cfg");// �Զ��ظ��Ķ���
	public static final String FILENAME_BUSYMODE_MESSAGE = "busymodemsg.cfg";

	private static final int NOTIFICATION_ID_BUSYMODE_ON = 0;// ״̬��֪ͨ��id
	private static final int PENDINGINTENT_ID_BUSYMODE = 0;

	private static int NOTIFICATION_ID_BUSYMODE_REFUSED = 1;// ״̬��֪ͨ��id

	/**
	 * intent action
	 */
	public static final String BUSYMODE_ACTION_CALL = "com.zgy.ringforu.ACTION_NOTIFICATION_CALL";// ֪ͨ������绰
	public static final String BUSYMODE_ACTION_CLEAR = "com.zgy.ringforu.ACTION_NOTIFICATION_CLEAR";// ֪ͨ�����

	public static final String INTENT_ACTION_KEY_CALL = "notification_refused_number";
	public static final String INTENT_ACTION_KEY_CLEAR = "notification_refused_clear";

	/**
	 * �Ƿ�����æµģʽ
	 * 
	 * @return
	 */
	public static boolean isBusyModeOn() {
		if (FILE_BUSYMODE_SWITCH.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ���Ҫ�Զ��ظ��Ķ�������
	 * 
	 * @param con
	 * @return
	 */
	public static String getBusyModeMsgContent(Context con) {
		if (FILE_BUSYMODE_MESSAGE.exists()) {
			return FileUtil.load(FILENAME_BUSYMODE_MESSAGE, con, false);
		} else {
			// return (con.getResources().getStringArray(R.array.busymodes_info))[0];
			return "";
		}
	}

	// /**
	// * �ж��Ƿ��Զ��ظ����ţ�
	// *
	// * @return
	// */
	// public static boolean isAutoSendMsg() {
	// if (FILE_BUSYMODE_MESSAGE.exists()) {
	// return true;
	// } else {
	// return false;
	// }
	// }

	/**
	 * ����æµģʽ�Ŀ���
	 * 
	 * @param on
	 * @param msg
	 */
	public static void setBusyModeOn(Context con, boolean on, String msg, boolean autoSendMsg) {
		if (on) {
			if (!FILE_BUSYMODE_SWITCH.exists()) {
				FILE_BUSYMODE_SWITCH.mkdir();
			}

			if (FILE_BUSYMODE_MESSAGE.exists()) {
				FILE_BUSYMODE_MESSAGE.delete();
			}
			// ���ظ����ţ���洢�����򲻴洢
			if (autoSendMsg) {
				FileUtil.save(FILENAME_BUSYMODE_MESSAGE, msg, con);
			}

		} else {
			if (FILE_BUSYMODE_SWITCH.exists()) {
				FILE_BUSYMODE_SWITCH.delete();
			}
		}

	}

	/**
	 * ����æµģʽ��title���Ҫ�ظ����ŵ�����
	 * 
	 * @param titles
	 * @param contents
	 * @param title
	 * @return
	 */
	public static String getMessageContentFromTitle(String[] titles, String[] contents, String title) {

		int count = titles.length;
		for (int a = 0; a < count; a++) {
			if (titles[a].equals(title)) {
				return contents[a];
			}
		}

		return "";
	}

	/**
	 * ���ݻظ����ŵ����ݻ��æµģʽ
	 * 
	 * @param con
	 * @param titles
	 * @param contents
	 * @param content
	 * @return
	 */

	public static String getMessageTitleFromContent(Context con, String[] titles, String[] contents, String content) {

		String result = con.getString(R.string.busymode_title_0);// �������ڼȶ�æµģʽ�У���Ϊ�Զ���ģʽ

		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	/**
	 * ���ݻظ����ŵ����ݻ��æµģʽ
	 * 
	 * @param con
	 * @return
	 */
	public static String getMessageTitleFromContent(Context con) {

		String[] contents = con.getResources().getStringArray(R.array.busymodes_info);
		String[] titles = con.getResources().getStringArray(R.array.busymodes_title);

		String content = getBusyModeMsgContent(con);

		// if (isAutoSendMsg()) {
		// content = FileUtil.load(FILENAME_BUSYMODE_MESSAGE, con, false);
		// }

		String result = con.getString(R.string.busymode_title_0);// �������ڼȶ�æµģʽ�У���Ϊ�Զ���ģʽ

		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	/**
	 * ���ݻظ����ŵ����ݻ��æµģʽ
	 * 
	 * @param con
	 * @param content
	 * @return
	 */
	public static String getMessageTitleFromContent(Context con, String content) {

		String[] contents = con.getResources().getStringArray(R.array.busymodes_info);
		String[] titles = con.getResources().getStringArray(R.array.busymodes_title);

		String result = con.getString(R.string.busymode_title_0);// �������ڼȶ�æµģʽ�У���Ϊ�Զ���ģʽ

		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	/**
	 * ���Ѿ�����������ʾ״̬��ͼ��
	 * 
	 * @param con
	 */
	public static void checkBusyModeState(Context context) {

		if (isBusyModeOn()) {
			// ����һ��NotificationManager������
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// ����Notification�ĸ�������
			Notification notification = new Notification(R.drawable.ic_delete_n, context.getString(R.string.busymode_tip_open_toast), System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ�� notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // ����֪ͨ���¼���Ϣ

			CharSequence contentText = getBusyModeMsgContent(context);// ��ûظ��Ķ�������
			CharSequence contentTitle = context.getString(R.string.busymode_title) + " - " + getMessageTitleFromContent(context, contentText.toString());// ���ݶ������ݻ�ñ���

			if (contentText != null && contentText.length() > 0) {
				contentText = context.getString(R.string.str_busymode_notification_msg) + contentText; // ֪ͨ������
			} else {
				contentText = context.getString(R.string.str_busymode_notification_nomsg);
			}

			Intent notificationIntent = new Intent(context, BusyModeActivity.class);
			notificationIntent.putExtra("fromnotifybar", true);
			// �����֪ͨ��Ҫ��ת��Activity
			PendingIntent contentItent = PendingIntent.getActivity(context, PENDINGINTENT_ID_BUSYMODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// ��Notification���ݸ�NotificationManager
			notificationManager.notify(NOTIFICATION_ID_BUSYMODE_ON, notification);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		} else {
			// ������ɾ��֮ǰ���Ƕ����֪ͨ
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_BUSYMODE_ON);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		}

	}

	/**
	 * ��ʾ���ص绰��֪ͨ
	 * 
	 * @param context
	 */
	public static void showRefusedNumberNotification(Context context, String number) {

		NOTIFICATION_ID_BUSYMODE_REFUSED = NOTIFICATION_ID_BUSYMODE_REFUSED + 1;// ��̬����id
		// ���޷��Զ����֣���Ϊ3.0���²�֧��֪ͨ����İ�ť��Ӧ
		// Notification notification = new Notification(R.drawable.ic_launcher, "�ܽ�����",
		// System.currentTimeMillis());
		// RemoteViews contentView = new RemoteViews(context.getPackageName(),
		// R.layout.notification_busymode_refused);
		// contentView.setImageViewResource(R.id.image_notification_busymode_refused_ic,
		// R.drawable.ic_delete_n);
		// contentView.setTextViewText(R.id.text_notification_busymode_refused_title, "æµģʽ�¾ܾ�������");
		// contentView.setTextViewText(R.id.text_notification_busymode_refused_content,
		// ContactsUtil.getNameFromPhone(context, number));
		//
		// Intent i = new Intent(BUSYMODE_ACTION_CALL);
		// i.putExtra(INTENT_ACTION_KEY_CALL, number);
		// PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		// contentView.setOnClickPendingIntent(R.id.btn_notification_busymode_refused_call, pendingIntent);
		//
		// Intent i2 = new Intent(BUSYMODE_ACTION_CLEAR);
		// i2.putExtra(INTENT_ACTION_KEY_CLEAR, id);
		// PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, i2,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		// contentView.setOnClickPendingIntent(R.id.btn_notification_busymode_refused_clear, pendingIntent2);
		//
		// notification.contentView = contentView;
		//
		// notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// // // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
		// // notification.flags |= Notification.FLAG_NO_CLEAR;
		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//
		// Intent notificationIntent = new Intent(context, MainActivityGroup.class);
		// PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		// notification.contentIntent = contentIntent;
		//
		// NotificationManager mNotificationManager = (NotificationManager)
		// context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mNotificationManager.notify(id, notification);

		// // ����Notification�ĸ�������
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		// ����Notification�ĸ�������
		Notification notification = new Notification(R.drawable.ic_notification_busymode_refused, context.getString(R.string.str_busymode_notification_refused_notify), System.currentTimeMillis());
		// notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ�� notification.flags |=
		// Notification.FLAG_SHOW_LIGHTS;

		CharSequence contentTitle = context.getString(R.string.str_busymode_notification_refused_title);// ֪ͨ������
		CharSequence contentText = ContactsUtil.getNameFromPhone(context, number);

		// if (contentText != null && contentText.length() > 0) {
		// contentText = context.getString(R.string.str_busymode_notification_msg) +
		// getBusyModeMsgContent(context); // ֪ͨ������
		// } else {
		// contentText = context.getString(R.string.str_busymode_notification_nomsg);
		// }

		Intent i = new Intent(BUSYMODE_ACTION_CALL);
		i.putExtra(INTENT_ACTION_KEY_CALL, number);

		// Intent notificationIntent = new Intent(context, BusyModeActivity.class);
		// �����֪ͨ��Ҫ��ת��Activity
		PendingIntent contentItent = PendingIntent.getBroadcast(context, PENDINGINTENT_ID_BUSYMODE, i, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
		// ��Notification���ݸ�NotificationManager
		notificationManager.notify(NOTIFICATION_ID_BUSYMODE_REFUSED, notification);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ

	}
}
