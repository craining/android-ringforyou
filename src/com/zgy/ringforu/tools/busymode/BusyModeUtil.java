package com.zgy.ringforu.tools.busymode;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.zgy.ringforu.R;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.ContactsUtil;
import com.zgy.ringforu.util.NotificationUtil;
import com.zgy.ringforu.util.StringUtil;

public class BusyModeUtil {

	/**
	 * 是否开启了忙碌模式
	 * 
	 * @return
	 */
	public static boolean isBusyModeOn() {
		if (MainConfig.getInstance().isBusyModeOn()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获得要自动回复的短信内容
	 * 
	 * @param con
	 * @return
	 */
	public static String getBusyModeMsgContent(Context con) {
		return MainConfig.getInstance().getBusyModeReplyStr();
	}

	/**
	 * 设置忙碌模式的开关
	 * 
	 * @param on
	 * @param msg
	 */
	public static void setBusyModeOnOff(Context con, boolean on, String msg) {

		MainConfig mainConfig = MainConfig.getInstance();
		if (on) {
			mainConfig.setBusyModeOnOff(true);

			// 若回复短信，则存储，否则不存储
			msg = StringUtil.isNull(msg) ? "" : msg;
			mainConfig.setBusyModeReplyStr(msg);

		} else {
			mainConfig.setBusyModeOnOff(false);
		}
	}

	/**
	 * 根据忙碌模式的title获得要回复短信的内容
	 * 
	 * @param titles
	 * @param contents
	 * @param title
	 * @return
	 */
	public static String getMessageContentFromTitle(String[] titles, String[] contents, String title) {

		int count = titles.length;
		for (int a = 0; a < count; a++) {
			if (titles[a].equals(title)) {
				return contents[a];
			}
		}

		return "";
	}

	/**
	 * 根据回复短信的内容获得忙碌模式
	 * 
	 * @param con
	 * @param titles
	 * @param contents
	 * @param content
	 * @return
	 */

	public static String getMessageTitleFromContent(Context con, String[] titles, String[] contents, String content) {

		String result = con.getString(R.string.busymode_title_0);// 若不存在既定忙碌模式中，则为自定义模式

		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	/**
	 * 根据回复短信的内容获得忙碌模式
	 * 
	 * @param con
	 * @return
	 */
	public static String getMessageTitleFromContent(Context con) {

		String[] contents = con.getResources().getStringArray(R.array.busymodes_info);
		String[] titles = con.getResources().getStringArray(R.array.busymodes_title);

		String content = getBusyModeMsgContent(con);

		String result = titles[9];
		if (!StringUtil.isNull(content)) {
			result = con.getString(R.string.busymode_title_0);// 若不存在既定忙碌模式中，则为自定义模式;
		}
		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	/**
	 * 根据回复短信的内容获得忙碌模式
	 * 
	 * @param con
	 * @param content
	 * @return
	 */
	public static String getMessageTitleFromContent(Context con, String content) {

		String[] contents = con.getResources().getStringArray(R.array.busymodes_info);
		String[] titles = con.getResources().getStringArray(R.array.busymodes_title);

		String result = con.getString(R.string.busymode_title_0);// 若不存在既定忙碌模式中，则为自定义模式

		int count = contents.length;
		for (int a = 0; a < count; a++) {
			if (contents[a].equals(content)) {
				result = titles[a];
			}
		}

		return result;
	}

	
	public static void checkBusyModeState(Context con) {
		NotificationUtil.checkBusyModeState(con, isBusyModeOn());
	}
	
}
