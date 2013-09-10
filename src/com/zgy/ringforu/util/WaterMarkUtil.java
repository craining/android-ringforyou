package com.zgy.ringforu.util;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.service.WaterMarkService;

/**
 * 水印相关
 * 
 * @author ZGY
 * 
 */
public class WaterMarkUtil {

	public static final File FILE_WATERMARK_IMG = new File("/data/data/com.zgy.ringforu/files/watermark.jpg");
	public static final String FILE_WATERMARK_IMG_TEMP_CUT = MainUtil.FILE_IN_SDCARD + "cut";
	public static final String FILE_WATERMARK_IMG_TEMP_SRC = MainUtil.FILE_IN_SDCARD + "src";

	private static final String SERVICE_NAME_WATERMARK = "com.zgy.ringforu.service.WaterMarkService";

	private static final String TAG = "WaterMarkUtil";

	public static final int WATER_MARK_ALPHA_DEF = 50;

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
		if (FILE_WATERMARK_IMG.exists() && MainConfig.getInstance().isWaterMarkOn()) {
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
		MainConfig mainConfig = MainConfig.getInstance();
		if (on) {
			mainConfig.setWaterMarkOnOff(true);

		} else {
			mainConfig.setWaterMarkOnOff(false);
		}
	}

	/**
	 * check
	 * 
	 * @param context
	 */
	public static void checkState(Context context) {
		if (isWaterMarkSeted()) {
			WaterMarkService.show = true;
			ctrlWaterMarkBackService(context, true);
			NotificationUtil.showHideWaterMarkNotify(true, context);
		} else {
			ctrlWaterMarkBackService(context, false);
			NotificationUtil.showHideWaterMarkNotify(false, context);
		}
	}

	/**
	 * 后台监听服务开关
	 */
	public static void ctrlWaterMarkBackService(Context context, boolean open) {
		if (open) {
//			if (!MainUtil.isServiceStarted(context, SERVICE_NAME_WATERMARK)) {
				Intent i = new Intent(context, WaterMarkService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(i);

//				LogRingForu.v(TAG, "service is not running, need to start service!");
//			} else {

//				LogRingForu.v(TAG, "service is running, no need to start service!");
//			}
		} else {
			if (MainUtil.isServiceStarted(context, SERVICE_NAME_WATERMARK)) {
				Intent i = new Intent(context, WaterMarkService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.stopService(i);

				LogRingForu.v(TAG, "service is running, need to stop service!");
			} else {

				LogRingForu.v(TAG, "service is not running, no need to stop service!");
			}
		}

	}

}
