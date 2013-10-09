package com.zgy.ringforu.util;

import android.content.Context;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.MainConfig;

public class AddContactUtil {

	private static final String TAG = "AddContactUtil";

	/**
	 * 添加重要联系人、电话短信屏蔽号码
	 * 
	 * @Description:
	 * @param strName
	 * @param strNum
	 * @param con
	 * @param tag
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-4
	 */
	public static int insert(String strName, String strNum, Context con, int tag) {

		MainConfig mainConfig = MainConfig.getInstance();

		int result = -1;
		switch (tag) {
		case MainCanstants.TYPE_IMPORTANT:

			String numbersImportant = RingForU.getInstance().getNumbersImportant();
			if (StringUtil.isNull(numbersImportant)) {
				MainUtil.refreshImportant(strNum, strName);
				result = 1;
			} else {
				if (getLeft(con, 0) <= 0) {
					result = -1;
				} else {
					if (!numbersImportant.contains(strNum)) {
						String namesImportant = mainConfig.getImporantNames();
						MainUtil.refreshImportant(numbersImportant + MainCanstants.SPLIT_TAG + strNum, namesImportant + MainCanstants.SPLIT_TAG + strName);
						result = 1;
					} else {
						result = 0;
					}
				}
			}
			break;
		case MainCanstants.TYPE_INTECEPT_CALL:
			String numbersCall = RingForU.getInstance().getNumbersCall();
			if (StringUtil.isNull(numbersCall)) {
				MainUtil.refreshCall(strNum, strName);
				result = 1;
			} else {

				if (getLeft(con, 1) <= 0) {
					result = -1;
				} else {
					if (!numbersCall.contains(strNum)) {
						String namesCall = mainConfig.getInterceptCallNames();
						MainUtil.refreshCall(numbersCall + MainCanstants.SPLIT_TAG + strNum, namesCall + MainCanstants.SPLIT_TAG + strName);
						result = 1;
					} else {
						result = 0;
					}
				}
			}
			break;
		case MainCanstants.TYPE_INTECEPT_SMS:
			String numbersSms = RingForU.getInstance().getNumbersSms();
			if (StringUtil.isNull(numbersSms)) {
				MainUtil.refreshSms(strNum, strName);
				result = 1;
			} else {

				if (getLeft(con, 2) <= 0) {
					result = -1;
				} else {
					if (!numbersSms.contains(strNum)) {
						String namesSms = mainConfig.getInterceptSmsNames();
						MainUtil.refreshSms(numbersSms + MainCanstants.SPLIT_TAG + strNum, namesSms + MainCanstants.SPLIT_TAG + strName);
						result = 1;
					} else {
						result = 0;
					}
				}
			}
			break;

		default:
			break;
		}

		return result;

	}

	/**
	 * 获得剩余可添加数
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-11-23
	 */
	public static int getLeft(Context con, int tag) {
		int result = MainCanstants.MAX_NUMS;

		MainConfig mainConfig = MainConfig.getInstance();
		switch (tag) {
		case MainCanstants.TYPE_IMPORTANT:
			String numbersImportant = RingForU.getInstance().getNumbersImportant();
			if (!StringUtil.isNull(numbersImportant)) {
				String[] arry = numbersImportant.split(MainCanstants.SPLIT_TAG);

				LogRingForu.v(TAG, " arry.length" + arry.length);
				result = MainCanstants.MAX_NUMS - arry.length;
			}
			break;
		case MainCanstants.TYPE_INTECEPT_CALL:
			String numbersCall = RingForU.getInstance().getNumbersCall();
			if (!StringUtil.isNull(numbersCall)) {
				String[] arry = numbersCall.split(MainCanstants.SPLIT_TAG);

				LogRingForu.v(TAG, " arry.length" + arry.length);
				result = MainCanstants.MAX_NUMS - arry.length;
			}
			break;
		case MainCanstants.TYPE_INTECEPT_SMS:
			String numbersSms = RingForU.getInstance().getNumbersSms();
			if (!StringUtil.isNull(numbersSms)) {
				String[] arry = numbersSms.split(MainCanstants.SPLIT_TAG);

				LogRingForu.v(TAG, " arry.length" + arry.length);
				result = MainCanstants.MAX_NUMS - arry.length;
			}
			break;

		default:
			break;
		}

		return result;
	}
}
