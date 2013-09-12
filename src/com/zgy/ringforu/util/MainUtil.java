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
