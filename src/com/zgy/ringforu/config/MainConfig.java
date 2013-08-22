package com.zgy.ringforu.config;

import android.content.Context;

import com.zgy.ringforu.MainApplication;
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

	public String getSlientTime() {
		return getString(ConfigCanstants.SLIENT_TIME, null);
	}

	public void setSlientTime(String time) {
		putString(ConfigCanstants.SLIENT_TIME, time);
	}
}
