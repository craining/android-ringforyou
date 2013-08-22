package com.zgy.ringforu.receiver;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.tools.smslightscreen.SmsLightScreenUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.StringUtil;

public class MsgReceiver extends BroadcastReceiver {

	private static final String TAG = "MsgReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		MainConfig mainConfig = MainConfig.getInstance();
		String strAllNumsImportant = mainConfig.getImporantNumbers();
		String strAllNumsSms = mainConfig.getInterceptSmsNumbers();

		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
			SmsMessage[] msg = null;

			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdusObj = (Object[]) bundle.get("pdus");
				msg = new SmsMessage[pdusObj.length];
				int mmm = pdusObj.length;
				for (int i = 0; i < mmm; i++)
					msg[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
			}

			// String msgTxt = "";
			// int msgLength = msg.length;
			// for (int i = 0; i < msgLength; i++) {
			// msgTxt += msg[i].getMessageBody();
			// }

			// 获得发信人号码
			String getFromNum = "";
			for (SmsMessage currMsg : msg) {
				getFromNum = currMsg.getDisplayOriginatingAddress();
			}
			// Log.v(TAG, getFromNum);
			getFromNum = StringUtil.getRidofSpeciall(getFromNum);
			if (strAllNumsImportant != null && strAllNumsImportant.contains(getFromNum)) {
				Log.e(TAG, "check screen light");
				SmsLightScreenUtil.checkSmsLightScreenOn(context);// 点亮屏幕与否
				if (MainUtil.isEffective(context)) {
					PhoneUtil.turnUpMost(context);
				} else {
					Log.v(TAG, "not effective!");
				}
			} else if (strAllNumsSms != null && strAllNumsSms.contains(getFromNum)) {
				// 屏蔽短信，
				if ((new File(MainUtil.FILE_PATH_SMS_HIDE_TAG)).exists()) {
					Log.e(TAG, "hide sms calm down!");
					// 保留短信
					PhoneUtil.hideMsg(context);
				} else {
					Log.e(TAG, "hide sms abort broadcast!");
					// 不存储短信
					abortBroadcast();
					setResultData(null);
				}
			} else {
				Log.e(TAG, "check screen light");
				SmsLightScreenUtil.checkSmsLightScreenOn(context);// 点亮屏幕与否
			}
		}
	}

}
