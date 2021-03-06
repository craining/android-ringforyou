package com.zgy.ringforu.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.activity.PushMessageShowActivity;
import com.zgy.ringforu.activity.ToolsBusyModeActivity;
import com.zgy.ringforu.activity.ToolsDisableGprsActivity;
import com.zgy.ringforu.activity.ToolsSignalReconnectActivity;
import com.zgy.ringforu.activity.ToolsSmsLightActivity;
import com.zgy.ringforu.activity.ToolsWaterMarkActivity;
import com.zgy.ringforu.util.NotificationUtil;
import com.zgy.ringforu.util.PopActivityUtil;
import com.zgy.ringforu.util.SelectApplicationUtil;

/**
 * 点击通知栏的receiver
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-9-6
 */
public class NotificationReceiver extends BroadcastReceiver {

	private static final String TAG = "NotificationBusyModeRefusedReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i;

		PopActivityUtil.popAllActivity(context);

		LogRingForu.e(TAG, "onReceive");
		if (intent.getAction().equals(NotificationUtil.ACTION_REFUSED_CALL)) {
			String number = intent.getExtras().getString(NotificationUtil.INTENT_ACTION_KEY_CALL);
			i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(NotificationUtil.BUSYMODE_ACTION_CLEAR)) {
			int id = intent.getExtras().getInt(NotificationUtil.INTENT_ACTION_KEY_CLEAR);

			LogRingForu.v(TAG, "cancel id = " + id);
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(id);
		} else if (intent.getAction().equals(NotificationUtil.ACTION_WATERMARK)) {
			i = new Intent(context, ToolsWaterMarkActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(NotificationUtil.ACTION_BUSYMODE)) {
			i = new Intent(context, ToolsBusyModeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(NotificationUtil.ACTION_DISABLEGPRS)) {
			i = new Intent(context, ToolsDisableGprsActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(NotificationUtil.ACTION_SMSLIGHT)) {
			i = new Intent(context, ToolsSmsLightActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(NotificationUtil.ACTION_SIGNALRECONNECT)) {
			i = new Intent(context, ToolsSignalReconnectActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(NotificationUtil.ACTION_NEW_VERSION)) {
			SelectApplicationUtil.viewHtml(context, intent.getExtras().getString(NotificationUtil.INTENT_ACTION_KEY_DOWNLOAD_URL));
		} else if (intent.getAction().equals(NotificationUtil.ACTION_PUSH_MSG)) {
			String title = intent.getStringExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_TITLE);
			String content = intent.getStringExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_CONTENT);
			String tag = intent.getStringExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_TAG);
			Integer id = intent.getIntExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_ID, 0);
			long receiveTime = intent.getLongExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_RECIEVE_TIME, -1);
			i = new Intent(context, PushMessageShowActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_TITLE, title);
			i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_CONTENT, content);
			i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_TAG, tag);
			i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_RECIEVE_TIME, receiveTime);
			RingForU.getInstance().removeOneInmAllPushMessageNotificationIds(id);
			context.startActivity(i);
		}

	}

}
