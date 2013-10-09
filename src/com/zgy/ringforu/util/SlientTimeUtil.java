package com.zgy.ringforu.util;

import android.content.Context;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.config.MainConfig;

public class SlientTimeUtil {

	private static final String TAG = "SlientTimeUtil";

	/**
	 * ���Ӱ���ʱ��
	 * 
	 * @Description:
	 * @param con
	 * @param per
	 *            return -1:����2:�ɹ�
	 * @return -1���ظ��� 0���������� 1����ͻ�� 2���ɹ�; 3:����
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-4
	 */
	public static int insertSlientP(Context con, String per) {
		int result = 2;// Ĭ�ϲ�����
		String preTimePer = MainConfig.getInstance().getSlientTime();
		if (!StringUtil.isNull(preTimePer)) {
			if (preTimePer.contains(per)) {
				return -1;// �ظ�
			} else {
				String[] pretimes = preTimePer.split("-");
				String[] newtimes = per.split("-");

				if (pretimes[0].equals(newtimes[0])) {
					if (TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[1])) {
						// 01:00-05:00
						// 01:00-04:00
						// ������
						return 0;
					} else {
						// 01:00-05:00
						// 01:00-06:00
						// ����
						result = 3;
					}

				} else if (pretimes[1].equals(newtimes[1])) {
					if (TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[0])) {
						// 03:00-04:00
						// 01:00-04:00
						// ������
						return 0;
					} else {
						// 01:00-05:00
						// 01:00-06:00
						// ����
						result = 3;
					}
				} else if (TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[0]) && TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[1])) {
					return 0;// ������
				} else if (TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[0]) || TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[1])) {
					return 1;// ��ͻ
				} else {
					if (TimeUtil.isTestTimeInFreeTime(newtimes[0], newtimes[1], pretimes[0]) && TimeUtil.isTestTimeInFreeTime(newtimes[0], newtimes[1], pretimes[1])) {
						result = 3;// ����
					}
				}
			}
		}

		if (result == 3) {

			LogRingForu.v(TAG, "contains the pre periord");
			MainConfig.getInstance().setSlientTime(per);
		}
		if (result == 2) {
			MainConfig.getInstance().setSlientTime(StringUtil.isNull(preTimePer) ? per : preTimePer + MainCanstants.SPLIT_TAG + per);
		}
		return result;
	}

	/**
	 * �ж��Ƿ���Ч
	 * 
	 * @return
	 */
	public static boolean isEffective(Context con) {
		boolean result = true;

		String strSlientP = MainConfig.getInstance().getSlientTime();

		if (!StringUtil.isNull(strSlientP)) {
			if (strSlientP.contains(MainCanstants.SPLIT_TAG)) {
				String[] a = strSlientP.split(MainCanstants.SPLIT_TAG);
				for (String a_item : a) {
					if (a_item != null && a_item.contains("-")) {
						String[] times = a_item.split("-");
						if (TimeUtil.isCurrentTimeInFreeTime(times[0], times[1])) {
							return false;
						}
					}
				}
			}
		}
		return result;
	}
}
