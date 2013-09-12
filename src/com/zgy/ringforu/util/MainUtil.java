package com.zgy.ringforu.util;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.config.MainConfig;

public class MainUtil {

	private static final String TAG = "MainUtil";

	// /**
	// * 在12:00--13:59
	// *
	// * 以及19:00之后
	// *
	// * @Description:
	// * @return
	// * @see:
	// * @since:
	// * @author: zhuanggy
	// * @date:2012-11-22
	// */
	// public static int inTime() {
	// int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	// LogRingForu.v(TAG, "hour" + hour);
	// if (hour >= 18) {
	// return 1;
	// } else if (hour == 12 || hour == 13) {
	// return 2;
	// } else {
	// return -1;
	// }
	//
	// }

	/**
	 * 通过Service的类名来判断是否启动某个服务
	 */
	public static boolean isServiceStarted(Context context, String serviceName) {

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mServiceList = activityManager.getRunningServices(100);

		for (int i = 0; i < mServiceList.size(); i++) {
			if (serviceName.equals(mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	/********************************************************************************/

	/**
	 * 判断各种功能是否开启了
	 * 
	 * @param context
	 */
	public static void checkAllState(Context context) {
		DisableGprsUtil.checkState(context);
		SignalReconnectUtil.checkState(context);
		WaterMarkUtil.checkState(context);
		BusyModeUtil.checkState(context);
		SmsLightScreenUtil.checkState(context);
		SignalReconnectUtil.checkState(context);
	}

	/**
	 * 每次打开应用时调用，初始化数据
	 * 
	 * @param context
	 */
	public static void mainInitData(Context context) {

		MainConfig config = MainConfig.getInstance();

		// 得到振动的开关状态
		MainCanstants.bIsVerbOn = config.isVibrateOn();
		MainCanstants.bIsGestureOn = config.isGestureOn();
		if (!(new File(MainCanstants.FILE_INNER).exists())) {
			new File(MainCanstants.FILE_INNER).mkdirs();
		}

		if (PhoneUtil.existSDcard()) {
			File f = new File(MainCanstants.FILE_IN_SDCARD);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		checkAllState(context);

		String version = context.getString(R.string.version_code);
		if (StringUtil.isNull(config.getVersionCode()) || !config.getVersionCode().equals(version)) {
			config.setVersionCode(version);
			config.setUserGuideShown(false);
			config.setRedToolsShown(false);
		}
	}

}
