package com.zgy.ringforu.util;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.activity.MainActivityGroup;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class PushMessageUtils {

	public static final String TAG = "PushMessageUtils";
	public static final String RESPONSE_METHOD = "method";
	public static final String RESPONSE_CONTENT = "content";
	public static final String RESPONSE_ERRCODE = "errcode";
	protected static final String ACTION_LOGIN = "com.baidu.pushdemo.action.LOGIN";
	public static final String ACTION_RESPONSE = "bccsclient.action.RESPONSE";
	public static final String ACTION_SHOW_MESSAGE = "bccsclient.action.SHOW_MESSAGE";
	protected static final String EXTRA_ACCESS_TOKEN = "access_token";

	// AppKey
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}
	
	
	
	/*****扩展*******/
	public static final String MESSAGE_TAG_BREAKLINE = ":::";//换行符
	
	public static final String MESSAGE_CONTENT_NEW_VERSION = "new_version";//消息内容
	//消息字段名
	public static final String MESSAGE_TAG_VERSION_CODE = "version_code";
	public static final String MESSAGE_TAG_DOWNLOAD_URL = "download_url";
	public static final String MESSAGE_TAG_VERSION_INFO = "version_info";
	
	public static final String MESSAGE_CONTENT_JOKE = "joke";
	public static final String MESSAGE_TAG_TITLE = "title";
	public static final String MESSAGE_TAG_CONTENT = "content";
	public static final String MESSAGE_TAG_TAG = "tag";
	
	
	/**
	 * 检测push状态
	 * @Description:
	 * @param context
	 * @see: 
	 * @since: 
	 * @author: zhuanggy
	 * @date:2013-9-24
	 */
	public static void startPushWork(Context context) {
//		if(!PushManager.isPushEnabled(context)){
		
			PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, PushMessageUtils.getMetaValue(context, "api_key"));
//			LogRingForu.v(TAG, "start push work");
//		} else {
//			LogRingForu.v(TAG, "no need start push work");
//		}
	}
	
}
