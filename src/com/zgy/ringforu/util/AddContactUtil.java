package com.zgy.ringforu.util;

import android.content.Context;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
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

				String numbersImportant = mainConfig.getImporantNumbers();
				if (StringUtil.isNull(numbersImportant)) {
					mainConfig.setImportantNumbers(strNum);
					mainConfig.setImportantNames(strName);
					result = 1;
				} else {
					if (getLeft(con, 0) <= 0) {
						result = -1;
					} else {
						if (!numbersImportant.contains(strNum)) {
							String namesImportant = mainConfig.getImporantNames();
							mainConfig.setImportantNumbers(numbersImportant + ":::" + strNum);
							mainConfig.setImportantNames(namesImportant + ":::" + strName);
							result = 1;
						} else {
							result = 0;
						}
					}
				}
				break;
			case MainCanstants.TYPE_INTECEPT_CALL:
				String numbersCall = mainConfig.getInterceptCallNumbers();
				if (StringUtil.isNull(numbersCall)) {
					mainConfig.setInterceptCallNumbers(strNum);
					mainConfig.setInterceptCallNames(strName);
					result = 1;
				} else {

					if (getLeft(con, 1) <= 0) {
						result = -1;
					} else {
						if (!numbersCall.contains(strNum)) {
							String namesCall = mainConfig.getInterceptCallNames();
							mainConfig.setInterceptCallNumbers(numbersCall + ":::" + strNum);
							mainConfig.setInterceptCallNames(namesCall + ":::" + strName);
							result = 1;
						} else {
							result = 0;
						}
					}
				}
				break;
			case MainCanstants.TYPE_INTECEPT_SMS:
				String numbersSms = mainConfig.getInterceptSmsNumbers();
				if (StringUtil.isNull(numbersSms)) {

					mainConfig.setInterceptSmsNumbers(strNum);
					mainConfig.setInterceptSmsNames(strName);

					result = 1;
				} else {

					if (getLeft(con, 2) <= 0) {
						result = -1;
					} else {
						if (!numbersSms.contains(strNum)) {
							String namesSms = mainConfig.getInterceptSmsNames();
							mainConfig.setInterceptSmsNumbers(numbersSms + ":::" + strNum);
							mainConfig.setInterceptSmsNames(namesSms + ":::" + strName);
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
				String numbersImportant = mainConfig.getImporantNumbers();
				if (!StringUtil.isNull(numbersImportant)) {
					String[] arry = numbersImportant.split(":::");

					LogRingForu.v(TAG, " arry.length" + arry.length);
					result = MainCanstants.MAX_NUMS - arry.length;
				}
				break;
			case MainCanstants.TYPE_INTECEPT_CALL:
				String numbersCall = mainConfig.getInterceptCallNumbers();
				if (!StringUtil.isNull(numbersCall)) {
					String[] arry = numbersCall.split(":::");

					LogRingForu.v(TAG, " arry.length" + arry.length);
					result = MainCanstants.MAX_NUMS - arry.length;
				}
				break;
			case MainCanstants.TYPE_INTECEPT_SMS:
				String numbersSms = mainConfig.getInterceptSmsNumbers();
				if (!StringUtil.isNull(numbersSms)) {
					String[] arry = numbersSms.split(":::");

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
