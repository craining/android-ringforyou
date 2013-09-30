package com.zgy.ringforu.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	private static String TIME_DATE_TIME_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static String TIME_DATE_STRING_FORMAT = "yyyy-MM-dd";//

	public static long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

	/**
	 * �жϵ�ǰʱ���Ƿ�����ʱ��
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
	 * ĳʱ���Ƿ�����ʱ��
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
	 * ������ڻ�ʱ���ַ���
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

		if (dateOrTime == 0) { // ��������

			return date;
		} else if (dateOrTime == 1) {// ����ʱ��
			return time;
		} else {
			return date + "  " + time;
		}
	}

	/**
	 * ��ʱ��תΪ�ض��ĸ�ʽ ��: 1 תΪ 01
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

	public static long dateTimeStringToLong(String dateTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_DATE_TIME_STRING_FORMAT);
		Date dt2;
		try {
			dt2 = sdf.parse(dateTime);
			return dt2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String longToDateTimeString(long dateTimeMillis) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_DATE_TIME_STRING_FORMAT);
		Date dt = new Date(dateTimeMillis);
		return sdf.format(dt);
	}

	public static long dateStringToLong(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_DATE_STRING_FORMAT);
		Date dt2;
		try {
			dt2 = sdf.parse(date);
			return dt2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static String longToDateString(long dateMillis) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_DATE_STRING_FORMAT);
		Date dt = new Date(dateMillis);
		return sdf.format(dt);
	}

}
