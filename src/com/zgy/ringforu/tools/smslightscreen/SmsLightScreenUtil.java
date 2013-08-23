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
	 * �Ƿ���������Ļ
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
	 * �յ�����ʱ���Ƿ������Ļ
	 */
	public static void checkSmsLightScreenOn(Context context) {
		if (isSmsLightScreenOn()) {
			try {
				// ������Ļ��������;add by zhuanggy
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
	 * ����
	 * 
	 * @param open
	 * @return trueΪ�ѿ�����falseΪ�ѹر�
	 */
	public static void ctrlSmsLightScreenSwitch(boolean open) {
		MainConfig.getInstance().setSmsLightScreenOnOff(open);
	}
}
