package com.zgy.ringforu.util;

import android.content.Context;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.MainConfig;

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
				if (RingForU.DEBUG)
					LogRingForu.e(TAG, "acquire wakeLock");
				new Handler().postDelayed(new Runnable() {

					public void run() {
						wakeLock.release();
						if (RingForU.DEBUG)
							LogRingForu.e(TAG, "release wakeLock");
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
	public static void ctrlSmsLightScreenSwitch(boolean open, Context context) {
		MainConfig.getInstance().setSmsLightScreenOnOff(open);
		checkState(context);
	}
	
	public static void checkState(Context context) {
		NotificationUtil.showHideSmsLightNotify(isSmsLightScreenOn(), context);
	}
}
