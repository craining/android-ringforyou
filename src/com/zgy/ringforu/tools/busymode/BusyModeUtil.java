package com.zgy.ringforu.tools.busymode;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zgy.ringforu.R;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.ContactsUtil;
import com.zgy.ringforu.util.StringUtil;

public class BusyModeUtil {

	private static final int NOTIFICATION_ID_BUSYMODE_ON = 0;// 状态栏通知的id
	private static final int PENDINGINTENT_ID_BUSYMODE = 0;

	private static int NOTIFICATION_ID_BUSYMODE_REFUSED = 1;// 状态栏通知的id

	/**
	 * intent action
	 */
	public static final String BUSYMODE_ACTION_CALL = "com.zgy.ringforu.ACTION_NOTIFICATION_CALL";// 通知栏拨打电话
	public static final String BUSYMODE_ACTION_CLEAR = "com.zgy.ringforu.ACTION_NOTIFICATION_CLEAR";// 通知栏清除

	public static final String INTENT_ACTION_KEY_CALL = "notification_refused_number";
	public static final String INTENT_ACTION_KEY_CLEAR = "notification_refused_clear";

	/**
	 * 是否开启了忙碌模式
	 * 
	 * @return
	 */
	public static boolean isBusyModeOn() {
		if (MainConfig.getInstance().isBusyModeOn()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得要自动回复的短信内容
	 * 
	 * @param con
	 * @return
	 */
	public static String getBusyModeMsgContent(Context con) {
		return MainConfig.getInstance().getBusyModeReplyStr();
	}

	// /**
	// * 判断是否自动回复短信，
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
	 * 设置忙碌模式的开关
	 * 
	 * @param on
	 * @param msg
	 */
	public static void setBusyModeOnOff(Context con, boolean on, String msg) {

		MainConfig mainConfig = MainConfig.getInstance();
		if (on) {
			mainConfig.setBusyModeOnOff(true);

			// 若回复短信，则存储，否则不存储
			msg = StringUtil.isNull(msg) ? "" : msg;
			mainConfig.setBusyModeReplyStr(msg);

		} else {
			mainConfig.setBusyModeOnOff(false);
		}
	}

	/**
	 * 根据忙碌模式的title获得要回复短信的内容
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
	 * 根据回复短信的内容获得忙碌模式
	 * 
	 * @param con
	 * @param titles
	 * @param contents
	 * @param content
	 * @return
	 */

	public static String getMessageTitleFromContent(Context con, String[] titles, String[] contents, String content) {

		String result = con.getString(R.string.busymode_title_0);// 若不存在既定忙碌模式中，则为自定义模式

		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	/**
	 * 根据回复短信的内容获得忙碌模式
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

		String result = con.getString(R.string.busymode_title_0);// 若不存在既定忙碌模式中，则为自定义模式

		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	/**
	 * 根据回复短信的内容获得忙碌模式
	 * 
	 * @param con
	 * @param content
	 * @return
	 */
	public static String getMessageTitleFromContent(Context con, String content) {

		String[] contents = con.getResources().getStringArray(R.array.busymodes_info);
		String[] titles = con.getResources().getStringArray(R.array.busymodes_title);

		String result = con.getString(R.string.busymode_title_0);// 若不存在既定忙碌模式中，则为自定义模式

		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	/**
	 * 若已经开启，则显示状态栏图标
	 * 
	 * @param con
	 */
	public static void checkBusyModeState(Context context) {

		if (isBusyModeOn()) {
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(R.drawable.ic_notification_busymode_on, context.getString(R.string.busymode_tip_open_toast), System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
			// notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // 设置通知的事件消息

			CharSequence contentText = getBusyModeMsgContent(context);// 获得回复的短信内容
			CharSequence contentTitle = context.getString(R.string.busymode_title) + " - " + getMessageTitleFromContent(context, contentText.toString());// 根据短信内容获得标题

			if (contentText != null && contentText.length() > 0) {
				contentText = context.getString(R.string.str_busymode_notification_msg) + contentText; // 通知栏内容
			} else {
				contentText = context.getString(R.string.str_busymode_notification_nomsg);
			}

			Intent notificationIntent = new Intent(context, BusyModeActivity.class);
			notificationIntent.putExtra("fromnotifybar", true);
			// 点击该通知后要跳转的Activity
			PendingIntent contentItent = PendingIntent.getActivity(context, PENDINGINTENT_ID_BUSYMODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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

		NOTIFICATION_ID_BUSYMODE_REFUSED = NOTIFICATION_ID_BUSYMODE_REFUSED + 1;// 动态创建id
		// 3.0以下不支持通知栏里的按钮响应
		// if (PhoneUtil.isUpAPI10(context)) {
		// Log.e("", "  3.0 以上");
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
		// Log.v("", " create notification id = " +
		// NOTIFICATION_ID_BUSYMODE_REFUSED);
		// } else {
		// Log.e("", "  3.0 以下");
		// // 定义Notification的各种属性
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		// 定义Notification的各种属性
		Notification notification = new Notification(R.drawable.ic_notification_busymode_refused, context.getString(R.string.str_busymode_notification_refused_notify), System.currentTimeMillis());
		// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
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

		Intent i = new Intent(BUSYMODE_ACTION_CALL);
		i.putExtra(INTENT_ACTION_KEY_CALL, number);

		// 点击该通知后要跳转的Activity
		PendingIntent contentItent = PendingIntent.getBroadcast(context, PENDINGINTENT_ID_BUSYMODE, i, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
		// 把Notification传递给NotificationManager
		notificationManager.notify(NOTIFICATION_ID_BUSYMODE_REFUSED, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		// }

	}
}
