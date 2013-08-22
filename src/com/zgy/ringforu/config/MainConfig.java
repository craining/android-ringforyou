package com.zgy.ringforu.config;

import android.content.Context;

import com.zgy.ringforu.MainApplication;
import com.zgy.ringforu.tools.watermark.WaterMarkUtil;
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
		return MainApplication.getInstance();
	}

	public synchronized String getSlientTime() {
		return getString(ConfigCanstants.SLIENT_TIME, null);
	}

	public synchronized void setSlientTime(String time) {
		putString(ConfigCanstants.SLIENT_TIME, time);
	}

	public synchronized boolean isWaterMarkOn() {
		return getBool(ConfigCanstants.SCREEN_WATERMARK, false);
	}

	public synchronized void setWaterMarkOnOff(boolean on) {
		putBool(ConfigCanstants.SCREEN_WATERMARK, on);
	}

	public synchronized void setWaterMarkAlpha(int alpha) {
		putInt(ConfigCanstants.SCREEN_WATERMARK_ALPHA, alpha);
	}

	public synchronized int getWaterMarkAlpha() {
		return getInt(ConfigCanstants.SCREEN_WATERMARK_ALPHA, WaterMarkUtil.WATER_MARK_ALPHA_DEF);
	}

	public synchronized String getBusyModeReplyStr() {
		return getString(ConfigCanstants.BUSYMODE_REPLY, null);
	}

	public synchronized void setBusyModeReplyStr(String str) {
		putString(ConfigCanstants.BUSYMODE_REPLY, str);
	}

	public synchronized boolean isBusyModeOn() {
		return getBool(ConfigCanstants.BUSYMODE, false);
	}

	public synchronized void setBusyModeOnOff(boolean on) {
		putBool(ConfigCanstants.BUSYMODE, on);
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
}
