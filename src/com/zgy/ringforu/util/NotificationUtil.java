package com.zgy.ringforu.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zgy.ringforu.R;
import com.zgy.ringforu.tools.busymode.BusyModeActivity;
import com.zgy.ringforu.tools.busymode.BusyModeUtil;
import com.zgy.ringforu.tools.disablegprs.DisableGprsActivity;
import com.zgy.ringforu.tools.watermark.WaterMarkActivity;

public class NotificationUtil {

	private static final int NOTIFICATION_ID_DISABLEGPRS_ON = 1;
	private static final int PENDINGINTENT_ID_DISABLEGPRS = 1;

	private static final int NOTIFICATION_ID_BUSYMODE_ON = 2;// ״̬��֪ͨ��id
	private static final int PENDINGINTENT_ID_BUSYMODE = 2;

	private static int NOTIFICATION_ID_BUSYMODE_REFUSED = 3;// ״̬��֪ͨ��id

	/**
	 * intent action
	 */
	public static final String BUSYMODE_ACTION_CALL = "com.zgy.ringforu.ACTION_NOTIFICATION_CALL";// ֪ͨ������绰
	public static final String BUSYMODE_ACTION_CLEAR = "com.zgy.ringforu.ACTION_NOTIFICATION_CLEAR";// ֪ͨ�����
	public static final String INTENT_ACTION_KEY_CALL = "notification_refused_number";
	public static final String INTENT_ACTION_KEY_CLEAR = "notification_refused_clear";

	private static final int PENDINGINTENT_ID_WATERMARK_ON = 0;
	private static final int NOTIFICATION_ID_WATERMARK_ON = 0;

	/**
	 * ��ʾgprs�Ѿ����õ�֪ͨ
	 * 
	 * @param con
	 */
	public static void showDisableGprsNotify(boolean show, Context context) {

		if (show) {
			// ��ʾ
			// ����һ��NotificationManager������
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_notification_disablegprs_on, context.getString(R.string.disable_gprs_on_tip), System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_NO_CLEAR;
			CharSequence contentText = context.getString(R.string.disable_gprs_notify_msg); // ��ûظ��Ķ�������
			CharSequence contentTitle = context.getString(R.string.disable_gprs_on_tip);// ���ݶ������ݻ�ñ���

			Intent notificationIntent = new Intent(context, DisableGprsActivity.class);
			// �����֪ͨ��Ҫ��ת��Activity
			PendingIntent contentItent = PendingIntent.getActivity(context, PENDINGINTENT_ID_DISABLEGPRS, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			// PendingIntent contentItent = PendingIntent.getBroadcast(context, requestCode, intent, flags)
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// ��Notification���ݸ�NotificationManager
			notificationManager.notify(NOTIFICATION_ID_DISABLEGPRS_ON, notification);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		} else {
			// ������ɾ��֮ǰ���Ƕ����֪ͨ
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_DISABLEGPRS_ON);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		}
	}

	/**
	 * ���Ѿ�����������ʾ״̬��ͼ��
	 * 
	 * @param con
	 */
	public static void checkBusyModeState(Context context, boolean on) {

		if (on) {
			// ����һ��NotificationManager������
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// ����Notification�ĸ�������
			Notification notification = new Notification(R.drawable.ic_notification_busymode_on, context.getString(R.string.busymode_tip_open_toast), System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��
			// notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // ����֪ͨ���¼���Ϣ

			CharSequence contentText = BusyModeUtil.getBusyModeMsgContent(context);// ��ûظ��Ķ�������
			CharSequence contentTitle = context.getString(R.string.busymode_title) + " - " + BusyModeUtil.getMessageTitleFromContent(context, contentText.toString());// ���ݶ������ݻ�ñ���

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
		// 3.0���²�֧��֪ͨ����İ�ť��Ӧ
		// if (PhoneUtil.isUpAPI10(context)) {
		// if (RingForU.DEBUG)
		// LogRingForu.e("", "  3.0 ����");
		// Notification notification = new
		// Notification(R.drawable.ic_notification_busymode_refused,
		// context.getString(R.string.str_busymode_notification_refused_notify),
		// System.currentTimeMillis());
		// RemoteViews contentView = new RemoteViews(context.getPackageName(),
		// R.layout.notification_busymode_refused);
		// contentView.setImageViewResource(R.id.image_notification_busymode_refused_ic,
		// R.drawable.ic_notification_busymode_refused);
		// contentView.setTextViewText(R.id.text_notification_busymode_refused_title,
		// context.getString(R.string.busymode_refused_title));
		// contentView.setTextViewText(R.id.text_notification_busymode_refused_content,
		// ContactsUtil.getNameFromPhone(context, number));
		//
		// Intent i = new Intent(BUSYMODE_ACTION_CALL);
		// i.putExtra(INTENT_ACTION_KEY_CALL, number);
		// PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
		// i,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		// contentView.setOnClickPendingIntent(R.id.btn_notification_busymode_refused_call,
		// pendingIntent);
		//
		// Intent i2 = new Intent(BUSYMODE_ACTION_CLEAR);
		// i2.putExtra(INTENT_ACTION_KEY_CLEAR,
		// NOTIFICATION_ID_BUSYMODE_REFUSED);
		// PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 2,
		// i2,
		// PendingIntent.FLAG_UPDATE_CURRENT);
		// contentView.setOnClickPendingIntent(R.id.btn_notification_busymode_refused_clear,
		// pendingIntent2);
		//
		// notification.contentView = contentView;
		//
		// // // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
		// // notification.flags |= Notification.FLAG_NO_CLEAR;
		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//
		// Intent notificationIntent = new Intent(context,
		// MainActivityGroup.class);
		// PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
		// notificationIntent, 0);
		// notification.contentIntent = contentIntent;
		//
		// NotificationManager mNotificationManager = (NotificationManager)
		// context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mNotificationManager.notify(NOTIFICATION_ID_BUSYMODE_REFUSED,
		// notification);
		//
		// LogRingForu.v("", " create notification id = " +
		// NOTIFICATION_ID_BUSYMODE_REFUSED);
		// } else {
		// LogRingForu.e("", "  3.0 ����");
		// // ����Notification�ĸ�������
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		// ����Notification�ĸ�������
		Notification notification = new Notification(R.drawable.ic_notification_busymode_refused, context.getString(R.string.str_busymode_notification_refused_notify), System.currentTimeMillis());
		// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��
		// notification.flags |=
		// Notification.FLAG_SHOW_LIGHTS;

		CharSequence contentTitle = context.getString(R.string.str_busymode_notification_refused_title);// ֪ͨ������

		String name = ContactsUtil.getNameFromContactsByNumber(context, number);

		if (StringUtil.isNull(name)) {
			name = context.getString(R.string.busymode_refused_unknown);
		}

		CharSequence contentText = name + context.getString(R.string.busymode_refused_tel) + number;

		Intent i = new Intent(BUSYMODE_ACTION_CALL);
		i.putExtra(INTENT_ACTION_KEY_CALL, number);

		// �����֪ͨ��Ҫ��ת��Activity
		PendingIntent contentItent = PendingIntent.getBroadcast(context, PENDINGINTENT_ID_BUSYMODE, i, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
		// ��Notification���ݸ�NotificationManager
		notificationManager.notify(NOTIFICATION_ID_BUSYMODE_REFUSED, notification);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		// }

	}

	/**
	 * ��ʾgprs�Ѿ����õ�֪ͨ
	 * 
	 * @param con
	 */
	public static void showHideWaterMarkNotify(boolean show, Context context) {

		if (show) {
			// ��ʾ
			// ����һ��NotificationManager������
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// ����Notification�ĸ�������
			Notification notification = new Notification(R.drawable.ic_notification_watermark_on, context.getString(R.string.watermark_on), System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��
			// notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // ����֪ͨ���¼���Ϣ

			CharSequence contentText = context.getString(R.string.watermark_set_tip);
			CharSequence contentTitle = context.getString(R.string.watermark_on);

			Intent notificationIntent = new Intent(context, WaterMarkActivity.class);
			notificationIntent.putExtra("fromnotifybar", true);
			// �����֪ͨ��Ҫ��ת��Activity
			PendingIntent contentItent = PendingIntent.getActivity(context, PENDINGINTENT_ID_WATERMARK_ON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// ��Notification���ݸ�NotificationManager
			notificationManager.notify(NOTIFICATION_ID_WATERMARK_ON, notification);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		} else {
			// ������ɾ��֮ǰ���Ƕ����֪ͨ
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_WATERMARK_ON);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		}

	}
}
