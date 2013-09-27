package com.zgy.ringforu.util;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.activity.MainActivityGroup;
import com.zgy.ringforu.config.MainConfig;

public class MainUtil {

	private static final String TAG = "MainUtil";

	// /**
	// * ��12:00--13:59
	// *
	// * �Լ�19:00֮��
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
	 * ͨ��Service���������ж��Ƿ�����ĳ������
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
	 * �жϸ��ֹ����Ƿ�����
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
	 * ÿ�δ�Ӧ��ʱ���ã���ʼ������
	 * 
	 * @param context
	 */
	public static void mainInitData(Context context) {

		MainConfig config = MainConfig.getInstance();

		// �õ��񶯵Ŀ���״̬
		RingForU.getInstance().setbIsVerbOn(config.isVibrateOn());
		RingForU.getInstance().setbIsGestureOn(config.isGestureOn());

		refreshNumbersImportant();
		refreshNumbersCall();
		refreshNumbersSms();
		refreshWaterMarkHideApps();

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

		String version = context.getString(R.string.version_name);
		if (StringUtil.isNull(config.getVersionName()) || !config.getVersionName().equals(version)) {
			config.setVersionName(version);
			config.setUserGuideShown(false);
			config.setRedToolsShown(false);
		}
	}

	
	public static void refreshWaterMarkHideApps() {
		RingForU.getInstance().setPackageNameHideWaterMark(MainConfig.getInstance().getWaterMarkHideApps());
	}
	
	public static void refreshWaterMarkHideApps(String apps) {
		RingForU.getInstance().setPackageNameHideWaterMark(apps);
		MainConfig.getInstance().setWaterMarkHideApps(apps);
	}
	
	public static void refreshNumbersImportant() {
		RingForU.getInstance().setNumbersImportant(MainConfig.getInstance().getNumbersImporant());
	}

	public static void refreshNumbersCall() {
		RingForU.getInstance().setNumbersCall(MainConfig.getInstance().getNumbersCall());
	}

	public static void refreshNumbersSms() {
		RingForU.getInstance().setNumbersSms(MainConfig.getInstance().getNumbersSms());
	}

	public static void refreshImportant(String numbers, String names) {
		MainConfig.getInstance().setNumbersImportant(numbers);
		MainConfig.getInstance().setImportantNames(names);
		RingForU.getInstance().setNumbersImportant(numbers);
	}

	public static void refreshCall(String numbers, String names) {
		MainConfig.getInstance().setNumbersCall(numbers);
		MainConfig.getInstance().setInterceptCallNames(names);
		RingForU.getInstance().setNumbersCall(numbers);
	}

	public static void refreshSms(String numbers, String names) {
		MainConfig.getInstance().setNumbersSms(numbers);
		MainConfig.getInstance().setInterceptSmsNames(names);
		RingForU.getInstance().setNumbersSms(numbers);
	}

	/**
	 * ��ó���汾��
	 * 
	 * @Description:
	 * @param context
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-23
	 */
	public static int getAppVersionCode(Context context) {
		try {
			PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * ����°汾
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-24
	 */
	public static void checkNewVersion(Context context) {
		if (MainConfig.getInstance().getPushNewVersionCode() > getAppVersionCode(context)) {
			NotificationUtil.showHideNewVersionNotify(true, context, MainConfig.getInstance().getPushNewVersionDownloadUrl());
		}
	}

}
