package com.zgy.ringforu.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.bean.PushMessage;

/**
 * 通知栏util
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-9-6
 */
public class NotificationUtil {

	// 状态栏通知的id
	private static final int NOTIFICATION_ID_WATERMARK_ON = 0;
	private static final int NOTIFICATION_ID_DISABLEGPRS_ON = 1;
	private static final int NOTIFICATION_ID_BUSYMODE_ON = 2;
	private static final int NOTIFICATION_ID_SMSLIGHT_ON = 3;
	private static final int NOTIFICATION_ID_SIGNALRECONNECT_ON = 5;
	private static final int NOTIFICATION_ID_NEW_VERSION = 6;
	private static int NOTIFICATION_ID_BUSYMODE_REFUSED = 20;
	private static int NOTIFICATION_ID_PUSH_MSG = 500;

	/**
	 * intent action
	 */
	public static final String ACTION_REFUSED_CALL = "com.zgy.ringforu.ACTION_NOTIFICATION_CALL";// 通知栏拨打电话
	public static final String ACTION_BUSYMODE = "com.zgy.ringforu.ACTION_NOTIFICATION_BUSYMODE";
	public static final String ACTION_WATERMARK = "com.zgy.ringforu.ACTION_NOTIFICATION_WATERMARK";
	public static final String ACTION_DISABLEGPRS = "com.zgy.ringforu.ACTION_NOTIFICATION_DISABLEGPRS";
	public static final String ACTION_SMSLIGHT = "com.zgy.ringforu.ACTION_NOTIFICATION_SMSLIGHT";
	public static final String ACTION_SIGNALRECONNECT = "com.zgy.ringforu.ACTION_NOTIFICATION_SIGNALRECONNECT";
	public static final String BUSYMODE_ACTION_CLEAR = "com.zgy.ringforu.ACTION_NOTIFICATION_CLEAR";// 通知栏清除
	public static final String ACTION_NEW_VERSION = "com.zgy.ringforu.ACTION_NOTIFICATION_NEW_VERSION";
	public static final String ACTION_PUSH_MSG = "com.zgy.ringforu.ACTION_NOTIFICATION_PUSH_MSG";

	public static final String INTENT_ACTION_KEY_CALL = "notification_refused_number";
	public static final String INTENT_ACTION_KEY_DOWNLOAD_URL = "notification_downloadurl";
	public static final String INTENT_ACTION_KEY_CLEAR = "notification_refused_clear";
	public static final String INTENT_ACTION_KEY_PUSH_MSG_TITLE = "notification_msg_title";
	public static final String INTENT_ACTION_KEY_PUSH_MSG_CONTENT = "notification_msg_content";
	public static final String INTENT_ACTION_KEY_PUSH_MSG_TAG = "notification_msg_tag";
	public static final String INTENT_ACTION_KEY_PUSH_MSG_RECIEVE_TIME = "notification_msg_receive_time";
	public static final String INTENT_ACTION_KEY_PUSH_MSG_ID = "notification_msg_receive_id";

	/**
	 * 显示gprs已经禁用的通知
	 * 
	 * @param con
	 */
	public static void showHideDisableGprsNotify(boolean show, Context context) {

		if (show) {
			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_notification_disablegprs_on, context.getString(R.string.disable_gprs_on_tip), System.currentTimeMillis());
			notification.icon = R.drawable.ic_notification_disablegprs_on;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_NO_CLEAR;
			CharSequence contentText = context.getString(R.string.disable_gprs_notify_msg); // 获得回复的短信内容
			CharSequence contentTitle = context.getString(R.string.disable_gprs_on_tip);// 根据短信内容获得标题

			Intent notificationIntent = new Intent(ACTION_DISABLEGPRS);
			PendingIntent contentItent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_DISABLEGPRS_ON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(NOTIFICATION_ID_DISABLEGPRS_ON, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		} else {
			// 启动后删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_DISABLEGPRS_ON);// 注意ID号，不能与此程序中的其他通知栏图标相同
		}
	}

	/**
	 * 若已经开启，则显示状态栏图标
	 * 
	 * @param con
	 */
	public static void showHideBusyModeNotify(Context context, boolean on) {

		if (on) {
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(R.drawable.ic_notification_busymode_on, context.getString(R.string.busymode_tip_open_toast), System.currentTimeMillis());

			// notification.internalApp = 1; //加上这句就ok了. 只适用于魅族的sdk
			notification.icon = R.drawable.ic_notification_busymode_on;

			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			// notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // 设置通知的事件消息

			CharSequence contentText = BusyModeUtil.getBusyModeMsgContent(context);// 获得回复的短信内容
			CharSequence contentTitle = context.getString(R.string.busymode_title) + " - " + BusyModeUtil.getMessageTitleFromContent(context, contentText.toString());// 根据短信内容获得标题

			if (contentText != null && contentText.length() > 0) {
				contentText = context.getString(R.string.str_busymode_notification_msg) + contentText; // 通知栏内容
			} else {
				contentText = context.getString(R.string.str_busymode_notification_nomsg);
			}

			Intent notificationIntent = new Intent(ACTION_BUSYMODE);
			PendingIntent contentItent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_BUSYMODE_ON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(NOTIFICATION_ID_BUSYMODE_ON, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		} else {
			// 启动后删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_BUSYMODE_ON);// 注意ID号，不能与此程序中的其他通知栏图标相同
		}

	}

	/**
	 * 显示拦截电话的通知
	 * 
	 * @param context
	 */
	public static void showRefusedNumberNotification(Context context, String number) {

		NOTIFICATION_ID_BUSYMODE_REFUSED += 1;// 动态创建id
		// 3.0以下不支持通知栏里的按钮响应
		// if (PhoneUtil.isUpAPI10(context)) {
		// if (RingForU.DEBUG)
		// LogRingForu.e("", "  3.0 以上");
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
		// // // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
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
		// LogRingForu.e("", "  3.0 以下");
		// // 定义Notification的各种属性
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		// 定义Notification的各种属性
		Notification notification = new Notification(R.drawable.ic_notification_busymode_refused, context.getString(R.string.str_busymode_notification_refused_notify), System.currentTimeMillis());
		// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
		notification.icon = R.drawable.ic_notification_busymode_refused;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
		// notification.flags |=
		// Notification.FLAG_SHOW_LIGHTS;

		CharSequence contentTitle = context.getString(R.string.str_busymode_notification_refused_title);// 通知栏标题

		String name = ContactsUtil.getNameFromContactsByNumber(context, number);

		if (StringUtil.isNull(name)) {
			name = context.getString(R.string.busymode_refused_unknown);
		}

		CharSequence contentText = name + context.getString(R.string.busymode_refused_tel) + number;

		Intent i = new Intent(ACTION_REFUSED_CALL);
		i.putExtra(INTENT_ACTION_KEY_CALL, number);

		// 点击该通知后要跳转的Activity
		PendingIntent contentItent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_BUSYMODE_REFUSED, i, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
		// 把Notification传递给NotificationManager
		notificationManager.notify(NOTIFICATION_ID_BUSYMODE_REFUSED, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		// }

	}

	/**
	 * 显示gprs已经禁用的通知
	 * 
	 * @param con
	 */
	public static void showHideWaterMarkNotify(boolean show, Context context) {

		if (show) {
			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(R.drawable.ic_notification_watermark_on, context.getString(R.string.watermark_on), System.currentTimeMillis());
			notification.icon = R.drawable.ic_notification_watermark_on;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			// notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // 设置通知的事件消息

			CharSequence contentText = context.getString(R.string.watermark_set_tip);
			CharSequence contentTitle = context.getString(R.string.watermark_on);

			Intent notificationIntent = new Intent(ACTION_WATERMARK);
			PendingIntent contentItent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_WATERMARK_ON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(NOTIFICATION_ID_WATERMARK_ON, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		} else {
			// 启动后删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_WATERMARK_ON);// 注意ID号，不能与此程序中的其他通知栏图标相同
		}
	}

	/**
	 * 显示gprs已经禁用的通知
	 * 
	 * @param con
	 */
	public static void showHideSmsLightNotify(boolean show, Context context) {

		if (show) {
			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(R.drawable.ic_notification_smslight_on, context.getString(R.string.smslight_on), System.currentTimeMillis());
			notification.icon = R.drawable.ic_notification_smslight_on;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			// notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // 设置通知的事件消息

			CharSequence contentText = context.getString(R.string.smslight_set_tip);
			CharSequence contentTitle = context.getString(R.string.smslight_on);

			Intent notificationIntent = new Intent(ACTION_SMSLIGHT);
			PendingIntent contentItent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_SMSLIGHT_ON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(NOTIFICATION_ID_SMSLIGHT_ON, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		} else {
			// 启动后删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_SMSLIGHT_ON);// 注意ID号，不能与此程序中的其他通知栏图标相同
		}
	}

	/**
	 * 显示gprs已经禁用的通知
	 * 
	 * @param con
	 */
	public static void showHideSignalReconnectNotify(boolean show, Context context) {

		if (show) {
			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(R.drawable.ic_notification_signalreconnect_on, context.getString(R.string.signalreconnect_on), System.currentTimeMillis());
			notification.icon = R.drawable.ic_notification_signalreconnect_on;
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			// notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // 设置通知的事件消息

			CharSequence contentText = context.getString(R.string.signalreconnect_set_tip);
			CharSequence contentTitle = context.getString(R.string.signalreconnect_on);

			Intent notificationIntent = new Intent(ACTION_SIGNALRECONNECT);
			PendingIntent contentItent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_SIGNALRECONNECT_ON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(NOTIFICATION_ID_SIGNALRECONNECT_ON, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		} else {
			// 启动后删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_SIGNALRECONNECT_ON);// 注意ID号，不能与此程序中的其他通知栏图标相同
		}
	}

	/**
	 * 显示新版本的通知
	 * 
	 * @param con
	 */
	public static void showHideNewVersionNotify(boolean show, Context context, String downloadUrl) {

		if (show) {
			LogRingForu.e("", "显示通知");
			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(R.drawable.ic_launcher, context.getString(R.string.notify_new_version), System.currentTimeMillis());
			notification.icon = R.drawable.ic_launcher;
			// notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			CharSequence contentText = context.getString(R.string.notify_new_version_tip);
			CharSequence contentTitle = context.getString(R.string.notify_new_version);

			Intent notificationIntent = new Intent(ACTION_NEW_VERSION);
			notificationIntent.putExtra(INTENT_ACTION_KEY_DOWNLOAD_URL, downloadUrl);
			PendingIntent contentItent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_NEW_VERSION, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(NOTIFICATION_ID_NEW_VERSION, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		} else {
			// 启动后删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_NEW_VERSION);// 注意ID号，不能与此程序中的其他通知栏图标相同
		}
	}

	/**
	 * 显示Push消息的通知
	 * 
	 * @param con
	 */
	public static int showHidePushMessageNotify(boolean show, Context context, PushMessage message, int notifyId) {

		if (show) {
			NOTIFICATION_ID_PUSH_MSG += 1;
			LogRingForu.e("", "显示通知");
			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(R.drawable.ic_notification, context.getString(R.string.msg_title), System.currentTimeMillis());
			notification.icon = R.drawable.ic_notification;
			// notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			CharSequence contentText = message.getTitle();
			CharSequence contentTitle = context.getString(R.string.msg_title);

			Intent notificationIntent = new Intent(ACTION_PUSH_MSG);
			notificationIntent.putExtra(INTENT_ACTION_KEY_PUSH_MSG_TITLE, message.getTitle());
			notificationIntent.putExtra(INTENT_ACTION_KEY_PUSH_MSG_CONTENT, message.getContent());
			notificationIntent.putExtra(INTENT_ACTION_KEY_PUSH_MSG_TAG, message.getTag());
			notificationIntent.putExtra(INTENT_ACTION_KEY_PUSH_MSG_RECIEVE_TIME, message.getReceiveTime());
			notificationIntent.putExtra(INTENT_ACTION_KEY_PUSH_MSG_ID, NOTIFICATION_ID_PUSH_MSG);
			PendingIntent contentItent = PendingIntent.getBroadcast(context, NOTIFICATION_ID_PUSH_MSG, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(NOTIFICATION_ID_PUSH_MSG, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同

			RingForU.getInstance().putOneInmAllPushMessageNotificationIds(NOTIFICATION_ID_PUSH_MSG);
		} else {
			// 启动后删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(notifyId);// 注意ID号，不能与此程序中的其他通知栏图标相同
		}

		return NOTIFICATION_ID_PUSH_MSG;
	}

	/**
	 * 清除所有push消息通知
	 * @param context
	 */
	public static void hideAllPushMessageNotifications(Context context) {

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);

		for (int i : RingForU.getInstance().getmAllPushMessageNotificationIds()) {
			if (i >= 500) {
				notificationManager.cancel(i);
			}
		}
	}
}
