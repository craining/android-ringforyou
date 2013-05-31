package com.zgy.ringforu.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class TimeUtil {


	/**
	 * 判断当前时间是否在闲时内
	 * 
	 * @Description:
	 * @param startTime
	 * @param endTime
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-5
	 */
	public static boolean isCurrentTimeInFreeTime(String startTime, String endTime) {
		boolean isIn = false;
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		String current = getTimeformatString(hour) + ":" + getTimeformatString(min);

		if (startTime.compareTo(endTime) > 0) {
			if (!(current.compareTo(endTime) > 0 && current.compareTo(startTime) < 0)) {
				isIn = true;
			}
		} else {
			if (current.compareTo(startTime) > 0 && current.compareTo(endTime) < 0) {
				isIn = true;
			}
		}
		return isIn;
	}

	/**
	 * 某时间是否在闲时内
	 * 
	 * @Description:
	 * @param startTime
	 * @param endTime
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-5
	 */
	public static boolean isTestTimeInFreeTime(String startTime, String endTime, String testTime) {
		boolean isIn = false;

		if (startTime.compareTo(endTime) > 0) {
			if (!(testTime.compareTo(endTime) > 0 && testTime.compareTo(startTime) < 0)) {
				isIn = true;
			}
		} else {
			if (testTime.compareTo(startTime) > 0 && testTime.compareTo(endTime) < 0) {
				isIn = true;
			}
		}
		return isIn;
	}

	
	/**
	 * 获得日期或时间字符串
	 * 
	 * @param dateOrTime
	 * @return
	 */
	public static String returnDateOrTime(int dateOrTime) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = sDateFormat.format(new java.util.Date());

		Calendar ca = Calendar.getInstance();
		int minute = ca.get(Calendar.MINUTE);
		int hour = ca.get(Calendar.HOUR_OF_DAY);
		int second = ca.get(Calendar.SECOND);

		String time = getTimeformatString(hour) + ":" + getTimeformatString(minute) + ":" + getTimeformatString(second);

		if (dateOrTime == 0) { // 返回日期

			return date;
		} else if (dateOrTime == 1) {// 返回时刻
			return time;
		} else {
			return date + "  " + time;
		}
	}

	/**
	 * 将时间转为特定的格式 如: 1 转为 01
	 * 
	 * @param mmm
	 * @return
	 */
	public static String getTimeformatString(int mmm) {
		if (mmm < 10) {
			if (mmm == 0) {
				return "00";
			} else {
				return "0" + mmm;
			}
		} else {
			return "" + mmm;
		}
	}
}
