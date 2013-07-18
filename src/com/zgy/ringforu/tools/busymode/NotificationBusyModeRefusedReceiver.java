package com.zgy.ringforu.tools.busymode;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class NotificationBusyModeRefusedReceiver extends BroadcastReceiver {

	private static final String TAG = "NotificationBusyModeRefusedReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(BusyModeUtil.BUSYMODE_ACTION_CALL)) {
			String number = intent.getExtras().getString(BusyModeUtil.INTENT_ACTION_KEY_CALL);
			Log.e(TAG, "BUSYMODE_ACTION_CALL");
			Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		} else if (intent.getAction().equals(BusyModeUtil.BUSYMODE_ACTION_CLEAR)) {
			int id = intent.getExtras().getInt(BusyModeUtil.INTENT_ACTION_KEY_CLEAR);
			NotificationManager notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(id);
		}
	}

}
