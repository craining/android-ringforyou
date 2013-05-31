package com.zgy.ringforu.tools.smslightscreen;

import java.io.File;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;

public class SmsLightScreenUtil {

	public static final File FILE_SMSLIGHTSCREEN_SWITCH = new File("/data/data/com.zgy.ringforu/smslightscreen");// �رտ������

	private static final String TAG = "SmsLightScreenUtil";

	/**
	 * �Ƿ���������Ļ
	 * 
	 * @return
	 */
	public static boolean isSmsLightScreenOn() {
		if (FILE_SMSLIGHTSCREEN_SWITCH.exists()) {
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
	public static boolean ctrlSmsLightScreenSwitch(boolean open) {
		if (open) {
			if (!isSmsLightScreenOn()) {
				FILE_SMSLIGHTSCREEN_SWITCH.mkdir();
			}
			return true;
		} else {
			if (isSmsLightScreenOn()) {
				FILE_SMSLIGHTSCREEN_SWITCH.delete();
			}
			return false;
		}
	}
}
