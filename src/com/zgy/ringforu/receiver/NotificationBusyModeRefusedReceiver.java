package com.zgy.ringforu.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.util.NotificationUtil;

/**
 * 暂时没用，没用自定义通知栏
 * @Description:
 * @author: zhuanggy
 * @see:   
 * @since:      
 * @copyright © 35.com
 * @Date:2013-9-6
 */
public class NotificationBusyModeRefusedReceiver extends BroadcastReceiver {

	private static final String TAG = "NotificationBusyModeRefusedReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RingForU.DEBUG)
			LogRingForu.e(TAG, "onReceive");
		if (intent.getAction().equals(NotificationUtil.BUSYMODE_ACTION_CALL)) {
			String number = intent.getExtras().getString(NotificationUtil.INTENT_ACTION_KEY_CALL);
			Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(NotificationUtil.BUSYMODE_ACTION_CLEAR)) {
			int id = intent.getExtras().getInt(NotificationUtil.INTENT_ACTION_KEY_CLEAR);
			if (RingForU.DEBUG)
				LogRingForu.v(TAG, "cancel id = " + id);
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(id);
		}
	}

}
