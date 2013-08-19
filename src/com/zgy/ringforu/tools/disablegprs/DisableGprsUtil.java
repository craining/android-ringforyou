package com.zgy.ringforu.tools.disablegprs;

import java.io.File;
import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.MainUtil;

public class DisableGprsUtil {

	public static final File FILE_DISABLESCREEN_SWITCH = new File("/data/data/com.zgy.ringforu/disablegprs");// 关闭控制与否
	private static final String SERVICE_NAME_DISABLESCREEN = "com.zgy.ringforu.tools.disablegprs.DisableGprsService";

	private static final String TAG = "DisableGprsUtil";

	private static final int NOTIFICATION_ID_DISABLEGPRS_ON = 100;
	private static final int PENDINGINTENT_ID_DISABLEGPRS = 100;

	/**
	 * 开启服务与否
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-1-31
	 */
	public static void checkDisableGprsState(Context context) {
		if (DisableGprsUtil.isDisableGprsOn()) {
//			DisableGprsUtil.ctrlDisableGprsBackService(context, true);
			showNotify(true, context);
		} else {
//			DisableGprsUtil.ctrlDisableGprsBackService(context, false);
			showNotify(false, context);
		}
	}

	/**
	 * 判断开关是否开启
	 */
	public static boolean isDisableGprsOn() {
		if (FILE_DISABLESCREEN_SWITCH.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 开关
	 */
	public static void ctrlDisableGprsSwitch(Context context, boolean open) {
		if (open) {
			if (!FILE_DISABLESCREEN_SWITCH.exists()) {
				FILE_DISABLESCREEN_SWITCH.mkdir();
			}
		} else {
			if (FILE_DISABLESCREEN_SWITCH.exists()) {
				FILE_DISABLESCREEN_SWITCH.delete();
			}
		}
		checkDisableGprsState(context);
	}

//	/**
//	 * 后台监听服务开关
//	 */
//	public static void ctrlDisableGprsBackService(Context context, boolean open) {
//		if (open) {
//			// if (!MainUtil.isServiceStarted(context, SERVICE_NAME_DISABLESCREEN)) {
//			Intent i = new Intent(context, DisableGprsService.class);
//			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startService(i);
//			Log.v(TAG, "service is not running, need to start service!");
//			// } else {
//			// Log.v(TAG, "service is running, no need to start service!");
//			// }
//		} else {
//			if (MainUtil.isServiceStarted(context, SERVICE_NAME_DISABLESCREEN)) {
//				Intent i = new Intent(context, DisableGprsService.class);
//				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				context.stopService(i);
//				Log.v(TAG, "service is running, need to stop service!");
//			} else {
//				Log.v(TAG, "service is not running, no need to stop service!");
//			}
//		}
//
//	}

	/**
	 * 判断打开或关闭 GPRS是否连接
	 */
	// public static boolean isMobileNetworkEnabled(Context context) {
	// ConnectivityManager conMan = (ConnectivityManager)
	// context.getSystemService(Context.CONNECTIVITY_SERVICE);
	// // State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
	// // if (wifi != State.CONNECTED) {
	// State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
	// if (mobile == State.CONNECTED) {
	// return true;
	// }
	// // }
	//
	// return gprsIsOpenMethod("getMobileDataEnabled", conMan);
	// }
	public static boolean isMobileNetworkEnabled(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (wifi != State.CONNECTED) {
			State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			if (mobile == State.CONNECTED) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 打开或关闭 GPRS
	 */
	public static void gprsEnabled(boolean bEnable, Context context) {

		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Object[] argObjects = null;
		// boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled", conMan);
		// if (isOpen == !bEnable) {
		setGprsEnabled("setMobileDataEnabled", conMan, bEnable);
		// }

	}

	// 检测GPRS是否打开
	private static boolean gprsIsOpenMethod(String methodName, ConnectivityManager conMan) {

		Class cmClass = conMan.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			isOpen = (Boolean) method.invoke(conMan, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpen;
	}

	// 开启/关闭GPRS
	private static void setGprsEnabled(String methodName, ConnectivityManager conMan, boolean isEnable) {
		Class cmClass = conMan.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			method.invoke(conMan, isEnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示gprs已经禁用的通知
	 * 
	 * @param con
	 */
	private static void showNotify(boolean show, Context context) {

		if (show) {
			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			Notification notification = new Notification(R.drawable.ic_notification_disablegprs_on, context.getString(R.string.disable_gprs_on_tip), System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			notification.flags |= Notification.FLAG_NO_CLEAR;
			CharSequence contentText = context.getString(R.string.disable_gprs_notify_msg); // 获得回复的短信内容
			CharSequence contentTitle = context.getString(R.string.disable_gprs_on_tip);// 根据短信内容获得标题

			Intent notificationIntent = new Intent(context, DisableGprsActivity.class);
			// 点击该通知后要跳转的Activity
			PendingIntent contentItent = PendingIntent.getActivity(context, PENDINGINTENT_ID_DISABLEGPRS, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// 把Notification传递给NotificationManager
			notificationManager.notify(NOTIFICATION_ID_DISABLEGPRS_ON, notification);// 注意ID号，不能与此程序中的其他通知栏图标相同
		} else {
			// 启动后删除之前我们定义的通知
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_DISABLEGPRS_ON);// 注意ID号，不能与此程序中的其他通知栏图标相同
		}

	}
}
