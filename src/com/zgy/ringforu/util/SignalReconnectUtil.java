package com.zgy.ringforu.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.service.SignalReconnectService;

public class SignalReconnectUtil {

	// public static final File FILE_SIGNAL_RECONNECT_SWITCH = new
	// File("/data/data/com.zgy.ringforu/signalreconnect");// �Ƿ���������Ŀ���
	private static final String SERVICE_NAME_SIGNAL_RECONNECT = "com.zgy.ringforu.service.SignalReconnectService";

	private static final String TAG = "SignalReconnectUtil";

	public static boolean doReconnect = true;

	/**
	 * �Ƿ������Դ�����
	 * 
	 * @return
	 */
	public static boolean isSignalReconnectOn() {
		if (MainConfig.getInstance().isSignalReconnectOn()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * �����
	 * 
	 * @param open
	 */
	public static void ctrlSignalReconnect(Context context, boolean open) {
		MainConfig.getInstance().setSignalReconnectOnOff(open);
		checkState(context);
	}

	/**
	 * ִ����������ģʽ���������������ƶ�����
	 * 
	 * @param context
	 */
	public static void doSignalReconnect(final Context context) {

		// �˴�����Ϊ��
		// ͨ������һ�η���ģʽ��ʵ����������sim�����繦��
		// ���źţ�asu������10ʱ����Ϊ�źŹ��ͣ�

		if (isSignalReconnectOn() && doReconnect) {
			doReconnect = false;

			LogRingForu.e(TAG, "change phone airplane state to reconnect START");

			new Handler().postDelayed(new Runnable() {

				public void run() {
					PhoneUtil.setAirplaneModeOff(context, false);// �źŹ��ͺ�60���������ģʽ

					LogRingForu.e(TAG, "on");
					new Handler().postDelayed(new Runnable() {

						public void run() {

							LogRingForu.e(TAG, "off");
							PhoneUtil.setAirplaneModeOff(context, true);// �ӳ�8���رշ���ģʽ
							new Handler().postDelayed(new Runnable() {

								public void run() {
									doReconnect = true;// �ӳ�60���,���ÿ����ٴ����¿��ط���ģʽ

									LogRingForu.e(TAG, "change phone airplane state to reconnect STOP");
								}
							}, 60000);

						}
					}, 8000);
				}
			}, 60000);

		} else {

			LogRingForu.e(TAG, "change phone airplane state to reconnect NO NO NO");
		}
	}

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
	public static void checkState(Context context) {
		if (isSignalReconnectOn()) {
			ctrlSignalReconnectBackService(context, true);
			NotificationUtil.showHideSignalReconnectNotify(true, context);
		} else {
			ctrlSignalReconnectBackService(context, false);
			NotificationUtil.showHideSignalReconnectNotify(false, context);
		}
	}

	/**
	 * ��̨�������񿪹�
	 */
	public static void ctrlSignalReconnectBackService(Context context, boolean open) {
		if (open) {
			// �˴�������δ�������������¿�������ֹ�ظ�ע�������
			if (!MainUtil.isServiceStarted(context, SERVICE_NAME_SIGNAL_RECONNECT)) {
				Intent i = new Intent(context, SignalReconnectService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(i);

				LogRingForu.v(TAG, "service is not running, need to start service!");
			} else {

				LogRingForu.v(TAG, "service is running, no need to start service!");
			}
		} else {
			if (MainUtil.isServiceStarted(context, SERVICE_NAME_SIGNAL_RECONNECT)) {
				Intent i = new Intent(context, SignalReconnectService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.stopService(i);

				LogRingForu.v(TAG, "service is running, need to stop service!");
			} else {

				LogRingForu.v(TAG, "service is not running, no need to stop service!");
			}
		}

	}

}