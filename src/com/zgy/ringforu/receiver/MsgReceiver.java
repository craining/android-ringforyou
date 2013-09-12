package com.zgy.ringforu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.ConfigCanstants;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.SlientTimeUtil;
import com.zgy.ringforu.util.SmsLightScreenUtil;
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

			LogRingForu.v(TAG, getFromNum);
			getFromNum = StringUtil.getRidofSpeciall(getFromNum);
			if (strAllNumsImportant != null && strAllNumsImportant.contains(getFromNum)) {

				LogRingForu.e(TAG, "check screen light");
				SmsLightScreenUtil.checkSmsLightScreenOn(context);// 点亮屏幕与否
				if (SlientTimeUtil.isEffective(context)) {
					PhoneUtil.turnUpMost(context);
				} else {

					LogRingForu.v(TAG, "not effective!");
				}
			} else if (strAllNumsSms != null && strAllNumsSms.contains(getFromNum)) {
				// 屏蔽短信，
				switch (MainConfig.getInstance().getInterceptSmsStyle()) {
				case ConfigCanstants.STYLE_INTERCEPT_SMS_SLIENT:

					LogRingForu.e(TAG, "hide sms slient!");
					// 保留短信
					PhoneUtil.turnDownThenUp(context);
					break;
				case ConfigCanstants.STYLE_INTERCEPT_SMS_DISRECEIVE:

					LogRingForu.e(TAG, "hide sms abort broadcast!");
					// 不存储短信
					abortBroadcast();
					setResultData(null);
					break;
				default:
					break;
				}
			} else {

				LogRingForu.e(TAG, "check screen light");
				SmsLightScreenUtil.checkSmsLightScreenOn(context);// 点亮屏幕与否
			}
		}
	}

}
