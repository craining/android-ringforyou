package com.zgy.ringforu.tools.disablegprs;

import java.io.File;
import java.lang.reflect.Method;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.MainUtil;

public class DisableGprsUtil {

	public static final File FILE_DISABLESCREEN_SWITCH = new File("/data/data/com.zgy.ringforu/disablegprs");// �رտ������
	private static final String SERVICE_NAME_DISABLESCREEN = "com.zgy.ringforu.tools.disablegprs.DisableGprsService";

	private static final String TAG = "DisableGprsUtil";

	private static final int NOTIFICATION_ID_DISABLEGPRS_ON = 100;
	private static final int PENDINGINTENT_ID_DISABLEGPRS = 100;

	/**
	 * �����������
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
			DisableGprsUtil.ctrlDisableGprsBackService(context, true);
			showNotify(true, context);
		} else {
			DisableGprsUtil.ctrlDisableGprsBackService(context, false);
			showNotify(false, context);
		}
	}

	/**
	 * �жϿ����Ƿ���
	 */
	public static boolean isDisableGprsOn() {
		if (FILE_DISABLESCREEN_SWITCH.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ����
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

	/**
	 * ��̨�������񿪹�
	 */
	public static void ctrlDisableGprsBackService(Context context, boolean open) {
		if (open) {
			// if (!MainUtil.isServiceStarted(context, SERVICE_NAME_DISABLESCREEN)) {
			Intent i = new Intent(context, DisableGprsService.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(i);
			Log.v(TAG, "service is not running, need to start service!");
			// } else {
			// Log.v(TAG, "service is running, no need to start service!");
			// }
		} else {
			if (MainUtil.isServiceStarted(context, SERVICE_NAME_DISABLESCREEN)) {
				Intent i = new Intent(context, DisableGprsService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.stopService(i);
				Log.v(TAG, "service is running, need to stop service!");
			} else {
				Log.v(TAG, "service is not running, no need to stop service!");
			}
		}

	}

	/**
	 * �жϴ򿪻�ر� GPRS�Ƿ�����
	 */
	public static boolean isMobileNetworkEnabled(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// // State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		// // if (wifi != State.CONNECTED) {
		// State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		// if (mobile == State.CONNECTED) {
		// return true;
		// }
		// // }

		return gprsIsOpenMethod("getMobileDataEnabled", conMan);
	}

	/**
	 * �򿪻�ر� GPRS
	 */
	public static boolean gprsEnabled(boolean bEnable, Context context) {

		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		Object[] argObjects = null;

		boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled", conMan);
		if (isOpen == !bEnable) {
			setGprsEnabled("setMobileDataEnabled", conMan, bEnable);
		}

		return isOpen;
	}

	// ���GPRS�Ƿ��
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

	// ����/�ر�GPRS
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
	 * ��ʾgprs�Ѿ����õ�֪ͨ
	 * 
	 * @param con
	 */
	private static void showNotify(boolean show, Context context) {

		if (show) {
			// ��ʾ
			// ����һ��NotificationManager������
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// ����Notification�ĸ�������
			Notification notification = new Notification(R.drawable.ic_notification_disablegprs_on, "Gprs���������ѽ��ã�", System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ�� notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // ����֪ͨ���¼���Ϣ

			CharSequence contentText = "�����������";// ��ûظ��Ķ�������
			CharSequence contentTitle = "Gprs���������ѽ���";// ���ݶ������ݻ�ñ���

			Intent notificationIntent = new Intent(context, DisableGprsActivity.class);
			// �����֪ͨ��Ҫ��ת��Activity
			PendingIntent contentItent = PendingIntent.getActivity(context, PENDINGINTENT_ID_DISABLEGPRS, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentItent);
			// ��Notification���ݸ�NotificationManager
			notificationManager.notify(NOTIFICATION_ID_DISABLEGPRS_ON, notification);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		} else {
			// ������ɾ��֮ǰ���Ƕ����֪ͨ
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(NOTIFICATION_ID_DISABLEGPRS_ON);// ע��ID�ţ�������˳����е�����֪ͨ��ͼ����ͬ
		}

	}
}
