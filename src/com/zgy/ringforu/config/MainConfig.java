package com.zgy.ringforu.config;

import android.content.Context;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.util.PreferenceUtil;

public class MainConfig extends PreferenceUtil {

	private static final String PREFERENCE_NAME = "com.zgy.ringforu.config";

	private static MainConfig config;

	private MainConfig() {
		super(PREFERENCE_NAME);
	}

	public static MainConfig getInstance() {
		if (config == null) {
			config = new MainConfig();
		}
		return config;
	}

	@Override
	protected Context getContext() {
		return RingForU.getInstance();
	}

	public synchronized String getSlientTime() {
		return getString(ConfigCanstants.SLIENT_TIME, null);
	}

	public synchronized void setSlientTime(String time) {
		putString(ConfigCanstants.SLIENT_TIME, time);
	}

	public synchronized boolean isWaterMarkOn() {
		return getBool(ConfigCanstants.SWITCH_SCREEN_WATERMARK, false);
	}

	public synchronized void setWaterMarkOnOff(boolean on) {
		putBool(ConfigCanstants.SWITCH_SCREEN_WATERMARK, on);
	}

	public synchronized void setWaterMarkAlpha(int alpha) {
		putInt(ConfigCanstants.SCREEN_WATERMARK_ALPHA, alpha);
	}

	public synchronized int getWaterMarkAlpha() {
		return getInt(ConfigCanstants.SCREEN_WATERMARK_ALPHA, MainCanstants.WATER_MARK_ALPHA_DEF);
	}

	public synchronized String getBusyModeReplyStr() {
		return getString(ConfigCanstants.BUSYMODE_REPLY, null);
	}

	public synchronized void setBusyModeReplyStr(String str) {
		putString(ConfigCanstants.BUSYMODE_REPLY, str);
	}

	public synchronized boolean isBusyModeOn() {
		return getBool(ConfigCanstants.SWITCH_BUSYMODE, false);
	}

	public synchronized void setBusyModeOnOff(boolean on) {
		putBool(ConfigCanstants.SWITCH_BUSYMODE, on);
	}

	public synchronized String getImporantNumbers() {
		return getString(ConfigCanstants.IMPORTANT_CALL_NUMBER, null);
	}

	public synchronized void setImportantNumbers(String numbers) {
		putString(ConfigCanstants.IMPORTANT_CALL_NUMBER, numbers);
	}

	public synchronized String getInterceptCallNumbers() {
		return getString(ConfigCanstants.INTERCEPT_CALL_NUMBER, null);
	}

	public synchronized void setInterceptCallNumbers(String numbers) {
		putString(ConfigCanstants.INTERCEPT_CALL_NUMBER, numbers);
	}

	public synchronized String getInterceptSmsNumbers() {
		return getString(ConfigCanstants.INTERCEPT_SMS_NUMBER, null);
	}

	public synchronized void setInterceptSmsNumbers(String numbers) {
		putString(ConfigCanstants.INTERCEPT_SMS_NUMBER, numbers);
	}

	public synchronized String getImporantNames() {
		return getString(ConfigCanstants.IMPORTANT_CALL_NAME, null);
	}

	public synchronized void setImportantNames(String names) {
		putString(ConfigCanstants.IMPORTANT_CALL_NAME, names);
	}

	public synchronized String getInterceptCallNames() {
		return getString(ConfigCanstants.INTERCEPT_CALL_NAME, null);
	}

	public synchronized void setInterceptCallNames(String names) {
		putString(ConfigCanstants.INTERCEPT_CALL_NAME, names);
	}

	public synchronized String getInterceptSmsNames() {
		return getString(ConfigCanstants.INTERCEPT_SMS_NAME, null);
	}

	public synchronized void setInterceptSmsNames(String names) {
		putString(ConfigCanstants.INTERCEPT_SMS_NAME, names);
	}

	public synchronized int getInterceptCallStyle() {
		return getInt(ConfigCanstants.STYLE_INTERCEPT_CALL, ConfigCanstants.STYLE_INTERCEPT_CALL_NO_ANSWER);
	}

	public synchronized void setInterceptCallStyle(int style) {
		putInt(ConfigCanstants.STYLE_INTERCEPT_CALL, style);
	}

	public synchronized int getInterceptSmsStyle() {
		return getInt(ConfigCanstants.STYLE_INTERCEPT_SMS, ConfigCanstants.STYLE_INTERCEPT_SMS_SLIENT);
	}

	public synchronized void setInterceptSmsStyle(int style) {
		putInt(ConfigCanstants.STYLE_INTERCEPT_SMS, style);
	}

	public synchronized boolean isDisableGprsOn() {
		return getBool(ConfigCanstants.SWITCH_DISABLE_GPRS, false);
	}

	public synchronized void setDisableGprsOnOff(boolean on) {
		putBool(ConfigCanstants.SWITCH_DISABLE_GPRS, on);
	}

	public synchronized boolean isSignalReconnectOn() {
		return getBool(ConfigCanstants.SWITCH_SIGNAL_RECONNECT, false);
	}

	public synchronized void setSignalReconnectOnOff(boolean on) {
		putBool(ConfigCanstants.SWITCH_SIGNAL_RECONNECT, on);
	}

	public synchronized boolean isSmsLightScreenOn() {
		return getBool(ConfigCanstants.SWITCH_SMSLIGHTSCREEN, false);
	}

	public synchronized void setSmsLightScreenOnOff(boolean on) {
		putBool(ConfigCanstants.SWITCH_SMSLIGHTSCREEN, on);
	}

	public synchronized boolean isVibrateOn() {
		return getBool(ConfigCanstants.SWITCH_OPERA_VIRBRATE, true);
	}

	public synchronized void setVibrateOnOff(boolean on) {
		putBool(ConfigCanstants.SWITCH_OPERA_VIRBRATE, on);
	}

	public synchronized boolean isGestureOn() {
		return getBool(ConfigCanstants.SWITCH_OPERA_GESTURE, true);
	}

	public synchronized void setGestureOnOff(boolean on) {
		putBool(ConfigCanstants.SWITCH_OPERA_GESTURE, on);
	}

	public synchronized boolean isNotificationOn() {
		return getBool(ConfigCanstants.NOTIFICATION_SWITCH, true);
	}

	public synchronized void setNotificationOnOff(boolean on) {
		putBool(ConfigCanstants.NOTIFICATION_SWITCH, on);
	}

	public synchronized String getVersionCode() {
		return getString(ConfigCanstants.VERSION_CODE, "");
	}

	public synchronized void setVersionCode(String code) {
		putString(ConfigCanstants.VERSION_CODE, code);
	}

	public synchronized boolean isUserGuideShown() {
		return getBool(ConfigCanstants.USER_GUIDE_SHOWD, false);
	}

	public synchronized void setUserGuideShown(boolean shown) {
		putBool(ConfigCanstants.USER_GUIDE_SHOWD, shown);
	}

	public synchronized boolean isRedToolsShown() {
		return getBool(ConfigCanstants.RED_TOOLS, false);
	}

	public synchronized void setRedToolsShown(boolean shown) {
		putBool(ConfigCanstants.RED_TOOLS, shown);
	}

	public synchronized int getPushNewVersionCode() {
		return getInt(ConfigCanstants.PUSH_NEW_VERSION_CODE, 0);
	}

	public synchronized void setPushNewVersionCode(int versionCode) {
		putInt(ConfigCanstants.PUSH_NEW_VERSION_CODE, versionCode);
	}

	public synchronized String getPushNewVersionDownloadUrl() {
		return getString(ConfigCanstants.PUSH_NEW_VERSION_DOWNLOAD_URL, null);
	}

	public synchronized void setPushNewVersionDownloadUrl(String url) {
		putString(ConfigCanstants.PUSH_NEW_VERSION_DOWNLOAD_URL, url);
	}
}
