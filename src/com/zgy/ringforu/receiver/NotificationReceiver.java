package com.zgy.ringforu.receiver;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.activity.BusyModeActivity;
import com.zgy.ringforu.activity.DisableGprsActivity;
import com.zgy.ringforu.activity.SignalReconnectActivity;
import com.zgy.ringforu.activity.SmsLightActivity;
import com.zgy.ringforu.activity.WaterMarkActivity;
import com.zgy.ringforu.util.NotificationUtil;
import com.zgy.ringforu.util.PopActivityUtil;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * 点击通知栏的receiver
 * @Description:
 * @author: zhuanggy
 * @see:   
 * @since:      
 * @copyright © 35.com
 * @Date:2013-9-6
 */
public class NotificationReceiver extends BroadcastReceiver{
	private static final String TAG = "NotificationBusyModeRefusedReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i;
		
		PopActivityUtil.popAllActivity(context);
		
		if (RingForU.DEBUG)
			LogRingForu.e(TAG, "onReceive");
		if (intent.getAction().equals(NotificationUtil.ACTION_REFUSED_CALL)) {
			String number = intent.getExtras().getString(NotificationUtil.INTENT_ACTION_KEY_CALL);
			i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(NotificationUtil.BUSYMODE_ACTION_CLEAR)) {
			int id = intent.getExtras().getInt(NotificationUtil.INTENT_ACTION_KEY_CLEAR);
			if (RingForU.DEBUG)
				LogRingForu.v(TAG, "cancel id = " + id);
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(id);
		} else if(intent.getAction().equals(NotificationUtil.ACTION_WATERMARK)) {
			i = new Intent(context, WaterMarkActivity.class);
			i.putExtra("fromnotifybar", true);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if(intent.getAction().equals(NotificationUtil.ACTION_BUSYMODE)) {
			i = new Intent(context, BusyModeActivity.class);
			i.putExtra("fromnotifybar", true);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if(intent.getAction().equals(NotificationUtil.ACTION_DISABLEGPRS)) {
			i = new Intent(context, DisableGprsActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else  if(intent.getAction().equals(NotificationUtil.ACTION_SMSLIGHT)) {
			i = new Intent(context, SmsLightActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else  if(intent.getAction().equals(NotificationUtil.ACTION_SIGNALRECONNECT)) {
			i = new Intent(context, SignalReconnectActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} 
		
	}

}
