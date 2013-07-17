package com.zgy.ringforu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zgy.ringforu.R;
import com.zgy.ringforu.tools.busymode.BusyModeUtil;
import com.zgy.ringforu.tools.disablegprs.DisableGprsUtil;
import com.zgy.ringforu.tools.signalreconnect.SignalReconnectUtil;
import com.zgy.ringforu.tools.watermark.WaterMarkUtil;
import com.zgy.ringforu.view.MyDialog;

public class MainUtil {

	private static final String TAG = "MainUtil";

	public static final int DLG_BTN_ALPHA = 100;// 对话框按钮的透明度

	public static final int TYPE_IMPORTANT = 0;// 重要联系人的操作
	public static final int TYPE_CALL = 1;// 屏蔽电话的操作
	public static final int TYPE_SMS = 2;// 屏蔽短信的操作
	public static final int TYPE_MORE = 3;// 更多

	public static final String FILE_INNER = "/data/data/com.zgy.ringforu/files/";
	public static final String FILE_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.ringforu/";

	public static final String FILE_IMPORTANT_NUM_LOG = "importantnumbers.cfg";
	public static final String FILE_IMPORTANT_NAME_LOG = "importantnames.cfg";
	public static final File FILE_IMPORTANT_PATH_NUM = new File("/data/data/com.zgy.ringforu/files/importantnumbers.cfg");
	public static final File FILE_IMPORTANT_PATH_NAME = new File("/data/data/com.zgy.ringforu/files/importantnames.cfg");
	public static final File FILE_SDCARD_IMPORTANT_NUM = new File(FILE_SDCARD + "importantnumbers.cfg");
	public static final File FILE_SDCARD_IMPORTANT_NAME = new File(FILE_SDCARD + "importantnames.cfg");

	public static final String FILE_CALL_NUM_LOG = "callnumbers.cfg";
	public static final String FILE_CALL_NAME_LOG = "callnames.cfg";
	public static final File FILE_CALL_PATH_NUM = new File("/data/data/com.zgy.ringforu/files/callnumbers.cfg");
	public static final File FILE_CALL_PATH_NAME = new File("/data/data/com.zgy.ringforu/files/callnames.cfg");
	public static final File FILE_SDCARD_CALL_NUM = new File(FILE_SDCARD + "callnumbers.cfg");
	public static final File FILE_SDCARD_CALL_NAME = new File(FILE_SDCARD + "callnames.cfg");

	public static final String FILE_SMS_NUM_LOG = "smsnumbers.cfg";
	public static final String FILE_SMS_NAME_LOG = "smsnames.cfg";
	public static final File FILE_SMS_PATH_NUM = new File("/data/data/com.zgy.ringforu/files/smsnumbers.cfg");
	public static final File FILE_SMS_PATH_NAME = new File("/data/data/com.zgy.ringforu/files/smsnames.cfg");
	public static final File FILE_SDCARD_SMS_NUM = new File(FILE_SDCARD + "smsnumbers.cfg");
	public static final File FILE_SDCARD_SMS_NAME = new File(FILE_SDCARD + "smsnames.cfg");

	// 最多可添加10个
	public static final int MAX_NUMS = 10;

	// 安静时段配置文件
	public static final String FILE_SLIENT_PER = "slient.cfg";
	public static final String FILE_PATH_SLIENT_PER = "/data/data/com.zgy.ringforu/files/slient.cfg";

	public static final String FEEDBACK_EMAIL_TO = "craining@163.com";
	public static final String FEEDBACK_TITLE = "RingForYou反馈";
	public static final String FEEDBACK_VERSION = "\r\n-------------------\r\nAPP VERSION: v2.0";

	public static final String FILE_PATH_CALL_HIDE_TAG = "/data/data/com.zgy.ringforu/files/callhide2";// 此目录存在则通话拦截方式为2
	public static final String FILE_PATH_SMS_HIDE_TAG = "/data/data/com.zgy.ringforu/files/smshide2";// 此目录存在则短信拦截方式为2

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
	// Log.v(TAG, "hour" + hour);
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
	// Log.v(TAG, file.toString() + "wrote in:" + str);
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

		int result = -1;
		switch (tag) {
		case TYPE_IMPORTANT:
			String numbersImportant = FileUtil.load(FILE_IMPORTANT_NUM_LOG, con, false);
			if (numbersImportant == null) {
				FileUtil.save(FILE_IMPORTANT_NUM_LOG, strNum + ":::", con);
				FileUtil.save(FILE_IMPORTANT_NAME_LOG, strName + ":::", con);
				result = 1;
			} else {

				if (getLeft(con, 0) <= 0) {
					result = -1;
				} else {
					if (!numbersImportant.contains(strNum + ":::")) {
						String namesImportant = FileUtil.load(FILE_IMPORTANT_NAME_LOG, con, false);
						FileUtil.save(FILE_IMPORTANT_NUM_LOG, numbersImportant + strNum + ":::", con);
						FileUtil.save(FILE_IMPORTANT_NAME_LOG, namesImportant + strName + ":::", con);
						result = 1;
					} else {
						result = 0;
					}
				}
			}
			break;
		case TYPE_CALL:
			String numbersCall = FileUtil.load(FILE_CALL_NUM_LOG, con, false);
			if (numbersCall == null) {
				FileUtil.save(FILE_CALL_NUM_LOG, strNum + ":::", con);
				FileUtil.save(FILE_CALL_NAME_LOG, strName + ":::", con);
				result = 1;
			} else {

				if (getLeft(con, 1) <= 0) {
					result = -1;
				} else {
					if (!numbersCall.contains(strNum + ":::")) {
						String namesCall = FileUtil.load(FILE_CALL_NAME_LOG, con, false);
						FileUtil.save(FILE_CALL_NUM_LOG, numbersCall + strNum + ":::", con);
						FileUtil.save(FILE_CALL_NAME_LOG, namesCall + strName + ":::", con);
						result = 1;
					} else {
						result = 0;
					}
				}
			}
			break;
		case TYPE_SMS:
			String numbersSms = FileUtil.load(FILE_SMS_NUM_LOG, con, false);
			if (numbersSms == null) {
				FileUtil.save(FILE_SMS_NUM_LOG, strNum + ":::", con);
				FileUtil.save(FILE_SMS_NAME_LOG, strName + ":::", con);
				result = 1;
			} else {

				if (getLeft(con, 2) <= 0) {
					result = -1;
				} else {
					if (!numbersSms.contains(strNum + ":::")) {
						String namesSms = FileUtil.load(FILE_SMS_NAME_LOG, con, false);
						FileUtil.save(FILE_SMS_NUM_LOG, numbersSms + strNum + ":::", con);
						FileUtil.save(FILE_SMS_NAME_LOG, namesSms + strName + ":::", con);
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
		int result = MAX_NUMS;

		switch (tag) {
		case TYPE_IMPORTANT:
			String numbersImportant = FileUtil.load(FILE_IMPORTANT_NUM_LOG, con, false);
			if (numbersImportant != null) {
				String[] arry = numbersImportant.split(":::");
				Log.v(TAG, " arry.length" + arry.length);
				result = MAX_NUMS - arry.length;
			}
			break;
		case TYPE_CALL:
			String numbersCall = FileUtil.load(FILE_CALL_NUM_LOG, con, false);
			if (numbersCall != null) {
				String[] arry = numbersCall.split(":::");
				Log.v(TAG, " arry.length" + arry.length);
				result = MAX_NUMS - arry.length;
			}
			break;
		case TYPE_SMS:
			String numbersSms = FileUtil.load(FILE_SMS_NUM_LOG, con, false);
			if (numbersSms != null) {
				String[] arry = numbersSms.split(":::");
				Log.v(TAG, " arry.length" + arry.length);
				result = MAX_NUMS - arry.length;
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
		int result = 2;
		String preTimePer = "";
		if ((new File(FILE_PATH_SLIENT_PER).exists())) {
			preTimePer = FileUtil.load(FILE_SLIENT_PER, con, false);
			if (preTimePer != null) {
				if (preTimePer.contains(per)) {
					return -1;// 重复
				} else {
					String[] a = preTimePer.split(":::");
					String[] pretimes = a[0].split("-");
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
		}

		if (result == 3) {
			(new File(FILE_PATH_SLIENT_PER)).delete();
			Log.v(TAG, "delete the other periord");
			FileUtil.save(MainUtil.FILE_SLIENT_PER, per + ":::", con);
		}
		if (result == 2) {
			FileUtil.save(MainUtil.FILE_SLIENT_PER, preTimePer + per + ":::", con);
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

		if ((new File(FILE_PATH_SLIENT_PER).exists())) {
			String strSlientP = FileUtil.load(FILE_SLIENT_PER, con, false);
			if (strSlientP != null && strSlientP.contains(":::")) {
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
		if (FILE_SDCARD_IMPORTANT_NUM.exists()) {
			FILE_SDCARD_IMPORTANT_NUM.delete();
		}
		if (FILE_SDCARD_IMPORTANT_NAME.exists()) {
			FILE_SDCARD_IMPORTANT_NAME.delete();
		}
		if (FILE_SDCARD_CALL_NUM.exists()) {
			FILE_SDCARD_CALL_NUM.delete();
		}
		if (FILE_SDCARD_CALL_NAME.exists()) {
			FILE_SDCARD_CALL_NAME.delete();
		}
		if (FILE_SDCARD_SMS_NUM.exists()) {
			FILE_SDCARD_SMS_NUM.delete();
		}
		if (FILE_SDCARD_SMS_NAME.exists()) {
			FILE_SDCARD_SMS_NAME.delete();
		}
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
	public static void checkAllService(Context context) {
		DisableGprsUtil.checkDisableGprsState(context);
		SignalReconnectUtil.checkSignalReconnectState(context);
		WaterMarkUtil.checkWaterMarkState(context);
		BusyModeUtil.checkBusyModeState(context);
	}

	/**
	 * 每次打开应用时调用，初始化数据
	 * 
	 * @param context
	 */
	public static void mainInitData(Context context) {
		// 得到振动的开关状态
		if (new File(PhoneUtil.FILE_PATH_VERB_TAG).exists()) {
			PhoneUtil.bIsVerbOn = false;
		} else {
			PhoneUtil.bIsVerbOn = true;
		}

		if (!(new File(MainUtil.FILE_SDCARD).exists())) {
			new File(MainUtil.FILE_SDCARD).mkdirs();
		}

		if (!(new File(MainUtil.FILE_INNER).exists())) {
			new File(MainUtil.FILE_INNER).mkdirs();
		}
		checkAllService(context);
	}
}
