package com.zgy.ringforu.tools.smslightscreen;

import java.io.File;

import com.zgy.ringforu.config.MainConfig;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class SmsLightScreenUtil {


	private static final String TAG = "SmsLightScreenUtil";

	/**
	 * 是否开启点亮屏幕
	 * 
	 * @return
	 */
	public static boolean isSmsLightScreenOn() {
		if (MainConfig.getInstance().isSmsLightScreenOn()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 收到短信时，是否点亮屏幕
	 */
	public static void checkSmsLightScreenOn(Context context) {
		if (isSmsLightScreenOn()) {
			try {
				// 点亮屏幕，不解锁;add by zhuanggy
				PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
				final WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
				wakeLock.acquire();
				Log.e(TAG, "acquire wakeLock");
				new Handler().postDelayed(new Runnable() {

					public void run() {
						wakeLock.release();
						Log.e(TAG, "release wakeLock");
					}
				}, 5000);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 开关
	 * 
	 * @param open
	 * @return true为已开启，false为已关闭
	 */
	public static void ctrlSmsLightScreenSwitch(boolean open) {
		MainConfig.getInstance().setSmsLightScreenOnOff(open);
	}
}
