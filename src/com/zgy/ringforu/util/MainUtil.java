package com.zgy.ringforu.util;

import java.io.File;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.MainConfig;

public class MainUtil {

	private static final String TAG = "MainUtil";

	public static final String FILE_INNER = "/data/data/com.zgy.ringforu/files/";
	public static final String FILE_IN_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ringforu/";

	public static final File FILE_SDCARD_IMPORTANT_NUM = new File(FILE_IN_SDCARD + "importantnumbers.cfg");
	public static final File FILE_SDCARD_IMPORTANT_NAME = new File(FILE_IN_SDCARD + "importantnames.cfg");

	public static final File FILE_SDCARD_CALL_NUM = new File(FILE_IN_SDCARD + "callnumbers.cfg");
	public static final File FILE_SDCARD_CALL_NAME = new File(FILE_IN_SDCARD + "callnames.cfg");

	public static final File FILE_SDCARD_SMS_NUM = new File(FILE_IN_SDCARD + "smsnumbers.cfg");
	public static final File FILE_SDCARD_SMS_NAME = new File(FILE_IN_SDCARD + "smsnames.cfg");

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

	//
	// public static boolean writeFile(String str, File file, boolean add) {
	// LogRingForu.v(TAG, file.toString() + "wrote in:" + str);
	//
	// FileOutputStream out;
	// try {
	// if (!file.getParentFile().exists()) {
	// file.getParentFile().mkdirs();
	// }
	// if (!file.exists()) {
	// file.createNewFile();
	// }
	//
	// out = new FileOutputStream(file, add);
	// String infoToWrite = str;
	// out.write(infoToWrite.getBytes());
	// out.close();
	//
	// } catch (IOException e) {
	// return false;
	// }
	//
	// return true;
	// }
	//
	// public static String getinfo(File file) {
	// String str = "";
	// if (file.exists()) {
	// FileInputStream in;
	// try {
	// in = new FileInputStream(file);
	// int length = (int) file.length();
	// byte[] temp = new byte[length];
	// in.read(temp, 0, length);
	// str = EncodingUtils.getString(temp, "utf-8");
	// in.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return str;
	// }

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

	/**
	 * 增加安静时段
	 * 
	 * @Description:
	 * @param con
	 * @param per
	 *            return -1:错误；2:成功
	 * @return -1：重复； 0：被包含； 1：冲突； 2：成功; 3:包含
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-4
	 */
	public static int insertSlientP(Context con, String per) {
		int result = 2;// 默认不包含
		String preTimePer = MainConfig.getInstance().getSlientTime();
		if (!StringUtil.isNull(preTimePer)) {
			if (preTimePer.contains(per)) {
				return -1;// 重复
			} else {
//				String[] a = preTimePer.split(":::");
				String[] pretimes = preTimePer.split("-");
				String[] newtimes = per.split("-");

				if (pretimes[0].equals(newtimes[0])) {
					if (TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[1])) {
						// 01:00-05:00
						// 01:00-04:00
						// 被包含
						return 0;
					} else {
						// 01:00-05:00
						// 01:00-06:00
						// 包含
						result = 3;
					}

				} else if (pretimes[1].equals(newtimes[1])) {
					if (TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[0])) {
						// 03:00-04:00
						// 01:00-04:00
						// 被包含
						return 0;
					} else {
						// 01:00-05:00
						// 01:00-06:00
						// 包含
						result = 3;
					}
				} else if (TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[0]) && TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[1])) {
					return 0;// 被包含
				} else if (TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[0]) || TimeUtil.isTestTimeInFreeTime(pretimes[0], pretimes[1], newtimes[1])) {
					return 1;// 冲突
				} else {
					if (TimeUtil.isTestTimeInFreeTime(newtimes[0], newtimes[1], pretimes[0]) && TimeUtil.isTestTimeInFreeTime(newtimes[0], newtimes[1], pretimes[1])) {
						result = 3;// 包含
					}
				}
			}
		}

		if (result == 3) {

			LogRingForu.v(TAG, "contains the pre periord");
			MainConfig.getInstance().setSlientTime(per);
		}
		if (result == 2) {
			MainConfig.getInstance().setSlientTime(StringUtil.isNull(preTimePer) ? per : preTimePer + ":::" + per);
		}
		return result;
	}

	/**
	 * 判断是否生效
	 * 
	 * @return
	 */
	public static boolean isEffective(Context con) {
		boolean result = true;

		String strSlientP = MainConfig.getInstance().getSlientTime();

		if (!StringUtil.isNull(strSlientP)) {
			if (strSlientP.contains(":::")) {
				String[] a = strSlientP.split(":::");
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

	/**
	 * 清空缓存（备份文件）
	 */
	public static void clearData() {

		if (new File(FILE_IN_SDCARD).exists()) {
			FileUtil.delFileDir(new File(FILE_IN_SDCARD));
		}

		// if (FILE_SDCARD_IMPORTANT_NUM.exists()) {
		// FILE_SDCARD_IMPORTANT_NUM.delete();
		// }
		// if (FILE_SDCARD_IMPORTANT_NAME.exists()) {
		// FILE_SDCARD_IMPORTANT_NAME.delete();
		// }
		// if (FILE_SDCARD_CALL_NUM.exists()) {
		// FILE_SDCARD_CALL_NUM.delete();
		// }
		// if (FILE_SDCARD_CALL_NAME.exists()) {
		// FILE_SDCARD_CALL_NAME.delete();
		// }
		// if (FILE_SDCARD_SMS_NUM.exists()) {
		// FILE_SDCARD_SMS_NUM.delete();
		// }
		// if (FILE_SDCARD_SMS_NAME.exists()) {
		// FILE_SDCARD_SMS_NAME.delete();
		// }
	}

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
		// 得到振动的开关状态
			MainCanstants.bIsVerbOn = MainConfig.getInstance().isVibrateOn();
			MainCanstants.bIsGestureOn = MainConfig.getInstance().isGestureOn();
		if (!(new File(MainUtil.FILE_INNER).exists())) {
			new File(MainUtil.FILE_INNER).mkdirs();
		}

		if (PhoneUtil.existSDcard()) {
			File f = new File(MainUtil.FILE_IN_SDCARD);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		checkAllState(context);
	}

}
