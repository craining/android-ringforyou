package com.zgy.ringforu.tools.watermark;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zgy.ringforu.R;
import com.zgy.ringforu.tools.disablegprs.DisableGprsActivity;
import com.zgy.ringforu.tools.disablegprs.DisableGprsService;
import com.zgy.ringforu.util.MainUtil;

/**
 * ˮӡ���
 * 
 * @author ZGY
 * 
 */
public class WaterMarkUtil {

	public static final File FILE_WATERMARK_IMG = new File("/data/data/com.zgy.ringforu/files/watermark");
	public static final File FILE_WATERMARK_TEMP_IMAGE = new File("/mnt/sdcard/.ringforu/watermarktemp");
	public static final String FILE_WATERMARK_ALPHA = "alpah.cfg";
	public static final File FILEPATH_WATERMARK_ALPHA = new File("/data/data/com.zgy.ringforu/files/alpah.cfg");

	private static final String SERVICE_NAME_WATERMARK = "com.zgy.ringforu.tools.watermark.WaterMarkService";

	private static final String TAG = "WaterMarkUtil";

	private static final int PENDINGINTENT_ID_WATERMARK_ON = 102;
	private static final int NOTIFICATION_ID_DISABLEGPRS_ON = 102;

	// TODO getstate , check ,
	/**
	 * check state
	 * 
	 * @return
	 */
	public static boolean isWaterMarkOn() {
		if (FILE_WATERMARK_IMG.exists() && FILEPATH_WATERMARK_ALPHA.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * check
	 * 
	 * @param context
	 */
	public static void checkWaterMarkState(Context context) {
		if (isWaterMarkOn()) {
			WaterMarkService.show = true;
			ctrlWaterMarkBackService(context, true);
			showNotify(true, context);
		} else {
			ctrlWaterMarkBackService(context, false);
			showNotify(false, context);
		}
	}

	/**
	 * ��̨�������񿪹�
	 */
	public static void ctrlWaterMarkBackService(Context context, boolean open) {
		if (open) {
			if (!MainUtil.isServiceStarted(context, SERVICE_NAME_WATERMARK)) {
				Intent i = new Intent(context, WaterMarkService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(i);
				Log.v(TAG, "service is not running, need to start service!");
			} else {
				Log.v(TAG, "service is running, no need to start service!");
			}
		} else {
			if (MainUtil.isServiceStarted(context, SERVICE_NAME_WATERMARK)) {
				Intent i = new Intent(context, WaterMarkService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.stopService(i);
				Log.v(TAG, "service is running, need to stop service!");
			} else {
				Log.v(TAG, "service is not running, no need to stop service!");
			}
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
			Notification notification = new Notification(R.drawable.ic_delete_n, "��Ļˮӡ�ѿ�����", System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ�� notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // ����֪ͨ���¼���Ϣ

			CharSequence contentText = "�������ˮӡ����ҳ";// ��ûظ��Ķ�������
			CharSequence contentTitle = "��Ļˮӡ�ѿ���";// ���ݶ������ݻ�ñ���

			Intent notificationIntent = new Intent(context, WaterMarkActivity.class);
			// �����֪ͨ��Ҫ��ת��Activity
			PendingIntent contentItent = PendingIntent.getActivity(context, PENDINGINTENT_ID_WATERMARK_ON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
