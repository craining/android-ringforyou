package com.zgy.ringforu.tools.signalreconnect;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;

public class SignalReconnectUtil {

	// public static final File FILE_SIGNAL_RECONNECT_SWITCH = new
	// File("/data/data/com.zgy.ringforu/signalreconnect");// 是否掉线重连的开关
	private static final String SERVICE_NAME_SIGNAL_RECONNECT = "com.zgy.ringforu.tools.signalreconnect.SignalReconnectService";

	private static final String TAG = "SignalReconnectUtil";

	public static boolean doReconnect = true;

	/**
	 * 是否开启了自从重连
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
	 * 开或关
	 * 
	 * @param open
	 */
	public static void ctrlSignalReconnect(Context context, boolean open) {
		MainConfig.getInstance().setSignalReconnectOnOff(open);
		checkSignalReconnectState(context);
	}

	/**
	 * 执行重启飞行模式操作。重新连接移动网络
	 * 
	 * @param context
	 */
	public static void doSignalReconnect(final Context context) {

		// 此处策略为：
		// 通过开关一次飞行模式，实现重新连接sim卡网络功能
		// 在信号（asu）低于10时，视为信号过低，

		if (isSignalReconnectOn() && doReconnect) {
			doReconnect = false;
			Log.e(TAG, "change phone airplane state to reconnect START");

			new Handler().postDelayed(new Runnable() {

				public void run() {
					PhoneUtil.setAirplaneModeOff(context, false);// 信号过低后，60秒后开启飞行模式
					Log.e(TAG, "on");
					new Handler().postDelayed(new Runnable() {

						public void run() {
							Log.e(TAG, "off");
							PhoneUtil.setAirplaneModeOff(context, true);// 延迟8秒后关闭飞行模式
							new Handler().postDelayed(new Runnable() {

								public void run() {
									doReconnect = true;// 延迟60秒后,设置可以再次重新开关飞行模式
									Log.e(TAG, "change phone airplane state to reconnect STOP");
								}
							}, 60000);

						}
					}, 8000);
				}
			}, 60000);

		} else {
			Log.e(TAG, "change phone airplane state to reconnect NO NO NO");
		}
	}

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
	public static void checkSignalReconnectState(Context context) {
		if (isSignalReconnectOn()) {
			ctrlSignalReconnectBackService(context, true);
		} else {
			ctrlSignalReconnectBackService(context, false);
		}
	}

	/**
	 * 后台监听服务开关
	 */
	public static void ctrlSignalReconnectBackService(Context context, boolean open) {
		if (open) {
			// 此处需用在未开启服务的情况下开启，防止重复注册监听器
			if (!MainUtil.isServiceStarted(context, SERVICE_NAME_SIGNAL_RECONNECT)) {
				Intent i = new Intent(context, SignalReconnectService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startService(i);
				Log.v(TAG, "service is not running, need to start service!");
			} else {
				Log.v(TAG, "service is running, no need to start service!");
			}
		} else {
			if (MainUtil.isServiceStarted(context, SERVICE_NAME_SIGNAL_RECONNECT)) {
				Intent i = new Intent(context, SignalReconnectService.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.stopService(i);
				Log.v(TAG, "service is running, need to stop service!");
			} else {
				Log.v(TAG, "service is not running, no need to stop service!");
			}
		}

	}

}
