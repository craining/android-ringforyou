package com.zgy.ringforu.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

public class PopActivityUtil {

	/**
	 * 判断当前应用是否开启
	 * 
	 * @param context
	 * @return
	 */
	public static void popAllActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		// String shortClassName = info.topActivity.getShortClassName(); // 类名
		// String className = info.topActivity.getClassName(); // 完整类名
		String packageName = info.topActivity.getPackageName(); // 包名

		if (!packageName.equals(context.getPackageName())) {
			RingForUActivityManager.popAll();
		}
	}
}
