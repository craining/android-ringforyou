package com.zgy.ringforu.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;

public class PopActivityUtil {

	/**
	 * �жϵ�ǰӦ���Ƿ���
	 * 
	 * @param context
	 * @return
	 */
	public static void popAllActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		RunningTaskInfo info = manager.getRunningTasks(1).get(0);
		// String shortClassName = info.topActivity.getShortClassName(); // ����
		// String className = info.topActivity.getClassName(); // ��������
		String packageName = info.topActivity.getPackageName(); // ����

		if (!packageName.equals(context.getPackageName())) {
			RingForUActivityManager.popAll();
		}
	}
}
