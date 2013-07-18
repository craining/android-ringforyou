package com.zgy.ringforu.util;

import java.lang.reflect.Method;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zgy.ringforu.R;
import com.zgy.ringforu.view.MyDialog;

public class PhoneUtil {

	private static final String TAG = "PhoneUtil";

	// 操作震动反馈相关
	public static boolean bIsVerbOn = true;
	public static final String FILE_PATH_VERB_TAG = "/data/data/com.zgy.ringforu/files/notverb";// 此目录存在判断是否震动

	public static final int ANSWER_RINGING_CALL = 10;
	public static final int SILENCE_RINGING_CALL = 11;
	public static final int HANG_UP_CALL = 12;

	/**
	 * 接听电话、拒接电话、或静音
	 * 
	 * @param context
	 * @param what
	 */
	public static void doActionAboutCall(Context context, int what) {
		TelephonyManager phoneManager = (TelephonyManager) context.getSystemService("phone");
		Class phoneManagerClass = phoneManager.getClass();
		try {
			Method getITelephony = phoneManagerClass.getDeclaredMethod("getITelephony", new Class[] {});
			getITelephony.setAccessible(true);
			// ITelephony
			Object insITelephony = getITelephony.invoke(phoneManager, new Object[] {});
			Class ITelephony = insITelephony.getClass();
			switch (what) {
			case ANSWER_RINGING_CALL:
				Method answerRingingCall = ITelephony.getMethod("answerRingingCall", new Class[] {});
				answerRingingCall.invoke(insITelephony, new Object[] {});
				break;

			case SILENCE_RINGING_CALL:
				Method silenceRinger = ITelephony.getMethod("silenceRinger", new Class[] {});
				silenceRinger.invoke(insITelephony, new Object[] {});

				break;

			case HANG_UP_CALL:
				Method endCall = ITelephony.getMethod("endCall", new Class[] {});
				endCall.invoke(insITelephony, new Object[] {});

				break;
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 调最大音量
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-11-22
	 */
	public static void turnUpMost(Context context) {
		Log.v(TAG, "turnUpMost");
		AudioManager audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioMgr.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
		audioMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		audioMgr.setStreamVolume(AudioManager.STREAM_RING, audioMgr.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
		audioMgr.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audioMgr.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
	}

	/**
	 * 静音模式
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-11-22
	 */
	public static void turnDown(Context context) {
		Log.v(TAG, "turnDown");
		AudioManager audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioMgr.setRingerMode(AudioManager.RINGER_MODE_SILENT);// 静音模式、不震动
		// audioMgr.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
	}

	/**
	 * 屏蔽短信
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-11-22
	 */
	public static void hideMsg(final Context context) {
		// 先静音、在清除通知栏、再还原声音
		turnDown(context);
		// NotificationManager nm = (NotificationManager)
		// context.getSystemService(Context.NOTIFICATION_SERVICE);
		// nm.cancelAll();
		// SystemClock.sleep(1000);

		new Handler().postDelayed(new Runnable() {

			public void run() {
				turnPre(context);
			}
		}, 5000);

	}

	/**
	 * 还原之前音量模式
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-11-22
	 */
	public static void turnPre(Context context) {
		Log.v(TAG, "turnPre");
		AudioManager audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		audioMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
	}

	/**
	 * 获得手机信息
	 * 
	 * @Description:
	 * @param con
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-3
	 */
	public static String getHandsetInfo(Context con) {

		TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb = new StringBuilder();

		int version = -1;
		String manufacturer = "null";
		String model = "null";
		String device = "null";

		try {
			Class<android.os.Build.VERSION> build_version_class = android.os.Build.VERSION.class;
			// 取得 android 版本
			java.lang.reflect.Field field;
			field = build_version_class.getField("SDK_INT");
			version = (Integer) field.get(new android.os.Build.VERSION());
			sb.append("\r\nSDK_INT = " + version);

			Class<android.os.Build> build_class = android.os.Build.class;
			// 取得牌子
			java.lang.reflect.Field manu_field = build_class.getField("MANUFACTURER");
			manufacturer = (String) manu_field.get(new android.os.Build());
			sb.append("\r\nManufacturer = " + manufacturer);
			// 取得型號
			java.lang.reflect.Field field2 = build_class.getField("MODEL");
			model = (String) field2.get(new android.os.Build());
			sb.append("\r\nMODEL = " + model);
			// 模組號碼
			java.lang.reflect.Field device_field = build_class.getField("DEVICE");
			device = (String) device_field.get(new android.os.Build());
			sb.append("\r\nDEVICE = " + device);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		sb.append("\r\nDeviceId(IMEI) = " + tm.getDeviceId());
		// sb.append("\r\nDeviceSoftwareVersion = " +
		// tm.getDeviceSoftwareVersion());
		// sb.append("\r\nLine1Number = " + tm.getLine1Number());
		// sb.append("\r\nNetworkCountryIso = " + tm.getNetworkCountryIso());
		// sb.append("\r\nNetworkOperator = " + tm.getNetworkOperator());
		// sb.append("\r\nNetworkOperatorName = " +
		// tm.getNetworkOperatorName());
		// sb.append("\r\nNetworkType = " + tm.getNetworkType());
		// sb.append("\r\nPhoneType = " + tm.getPhoneType());
		sb.append("\r\nSimCountryIso = " + tm.getSimCountryIso());
		// sb.append("\r\nSimOperator = " + tm.getSimOperator());
		sb.append("\r\nSimOperatorName = " + tm.getSimOperatorName());
		// sb.append("\r\nSimSerialNumber = " + tm.getSimSerialNumber());
		// sb.append("\r\nSimState = " + tm.getSimState());
		sb.append("\r\nSubscriberId(IMSI) = " + tm.getSubscriberId());
		// sb.append("\r\nVoiceMailNumber = " + tm.getVoiceMailNumber());

		Log.i("info", sb.toString());
		return sb.toString();
	}

	/**
	 * 
	 * 判断网络状态是否可用
	 * 
	 * @return true:网络可用; false:网络不可用
	 */

	public static boolean isConnectInternet(Context con) {
		ConnectivityManager conManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出错

			return networkInfo.isAvailable();
		}

		return false;
	}

	/**
	 * 跳转到设置网络页面
	 * 
	 * @Description:
	 * @param con
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-3
	 */
	public static void setNetConnection(final Context con, final Vibrator vb) {
		// 设置网络
		MyDialog.Builder builder = new MyDialog.Builder(con);
		builder.setTitle(R.string.str_tip).setMessage(R.string.nonet_tip).setPositiveButton(R.string.str_set, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				if (bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
				dialog.dismiss();
				con.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
			}
		}).setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				if (bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
			}
		}).create().show();
	}

	public static void hideKeyboard(Context con, EditText edit) {
		// 隐藏软件盘
		InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	/**
	 * 判断存储卡是否存在
	 * 
	 * @return
	 */
	public static boolean existSDcard() {
		if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
			return true;
		} else
			return false;
	}

	/**
	 * 控制飞行模式开关
	 * 
	 * @param con
	 * @param on
	 */
	public static void setAirplaneModeOff(Context con, boolean off) {
		// Change the system setting
		Settings.System.putInt(con.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, off ? 1 : 0);
		// Post the intent
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", off);
		con.sendBroadcast(intent);
	}

	/**
	 * 发送短信
	 * 
	 * @param con
	 * @param number
	 * @param content
	 */
	public static void sendMessage(Context con, String tonumber, String content) {
		// 直接调用短信接口发短信
		SmsManager smsManager = SmsManager.getDefault();
		List<String> divideContents = smsManager.divideMessage(content);
		for (String text : divideContents) {
			smsManager.sendTextMessage(tonumber, null, text, null, null);
			Log.e(TAG, "send msg, to:" + tonumber + "  content: " + content);
		}
	}

	/**
	 * 是否是3.0以上版本
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-7-18
	 */
	public static boolean isUpAPI10(Context con) {
		int version = -1;
		try {
			Class<android.os.Build.VERSION> build_version_class = android.os.Build.VERSION.class;
			// 取得 android 版本
			java.lang.reflect.Field field;
			field = build_version_class.getField("SDK_INT");
			version = (Integer) field.get(new android.os.Build.VERSION());
			Log.v(TAG, "isUpAPI10 ? api = " + version);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (version > 10) {
			return true;
		} else {
			return false;
		}
	}

}
