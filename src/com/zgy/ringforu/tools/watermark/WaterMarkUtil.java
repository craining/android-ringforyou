package com.zgy.ringforu.tools.watermark;

import java.io.File;

import android.R.bool;
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
 * 水印相关
 * 
 * @author ZGY
 * 
 */
public class WaterMarkUtil {

	public static final File FILE_WATERMARK_IMG = new File("/data/data/com.zgy.ringforu/files/watermark.jpg");
	public static final String FILE_WATERMARK_IMG_TEMP_CUT = MainUtil.FILE_SDCARD +  "cut";
	public static final String FILE_WATERMARK_IMG_TEMP_SRC = MainUtil.FILE_SDCARD +  "src";
	public static final String FILE_WATERMARK_ALPHA = "alpah.cfg";
	public static final File FILEPATH_WATERMARK_ALPHA = new File("/data/data/com.zgy.ringforu/files/alpah.cfg");
	public static final File FILE_WATERMARK_SWITCH = new File("/data/data/com.zgy.ringforu/watermark");

	private static final String SERVICE_NAME_WATERMARK = "com.zgy.ringforu.tools.watermark.WaterMarkService";

	private static final String TAG = "WaterMarkUtil";

	private static final int PENDINGINTENT_ID_WATERMARK_ON = 102;
	private static final int NOTIFICATION_ID_DISABLEGPRS_ON = 102;

	// TODO getstate , check ,

	public static boolean isWaterMarkShowing(Context context) {
		if (isWaterMarkSeted() && MainUtil.isServiceStarted(context, SERVICE_NAME_WATERMARK)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * check state
	 * 
	 * @return
	 */
	public static boolean isWaterMarkSeted() {
		if (FILE_WATERMARK_IMG.exists() && FILEPATH_WATERMARK_ALPHA.exists() && FILE_WATERMARK_SWITCH.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 水印开关
	 * 
	 * @Description:
	 * @param on
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-7-18
	 */
	public static void setSwitchOnOff(boolean on) {
		if (on) {
			if (!FILE_WATERMARK_SWITCH.exists()) {
				FILE_WATERMARK_SWITCH.mkdir();
			}
		} else {
			if (FILE_WATERMARK_SWITCH.exists()) {
				FILE_WATERMARK_SWITCH.delete();
			}
		}
	}

	/**
	 * check
	 * 
	 * @param context
	 */
	public static void checkWaterMarkState(Context context) {
		if (isWaterMarkSeted()) {
			WaterMarkService.show = true;
			ctrlWaterMarkBackService(context, true);
			showNotify(true, context);
		} else {
			ctrlWaterMarkBackService(context, false);
			showNotify(false, context);
		}
	}

	/**
	 * 后台监听服务开关
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
	 * 显示gprs已经禁用的通知
	 * 
	 * @param con
	 */
	private static void showNotify(boolean show, Context context) {

		if (show) {
			// 显示
			// 创建一个NotificationManager的引用
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			// 定义Notification的各种属性
			Notification notification = new Notification(R.drawable.ic_notification_watermark_on, "屏幕水印已开启！", System.currentTimeMillis());
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			// 将此通知放到通知栏的"Ongoing"即"正在运行"组中
			notification.flags |= Notification.FLAG_NO_CLEAR;
			// 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用 notification.flags |=
			// Notification.FLAG_SHOW_LIGHTS;
			// notification.defaults = Notification.DEFAULT_LIGHTS;
			// notification.ledARGB = Color.YELLOW;
			// notification.ledOnMS = 5000; // 设置通知的事件消息

			CharSequence contentText = "点击进入水印设置页";// 获得回复的短信内容
			CharSequence contentTitle = "屏幕水印已开启";// 根据短信内容获得标题

			Intent notificationIntent = new Intent(context, WaterMarkActivity.class);
			notificationIntent.putExtra("fromnotifybar", true);
			// 点击该通知后要跳转的Activity
			PendingIntent contentItent = PendingIntent.getActivity(context, PENDINGINTENT_ID_WATERMARK_ON, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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
