package com.zgy.ringforu.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.ConfigCanstants;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.BusyModeUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.NotificationUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.SlientTimeUtil;
import com.zgy.ringforu.util.StringUtil;

public class CallReceiver extends BroadcastReceiver {

	private static final String TAG = "CallReceiver";

	private TelephonyManager tm;
	private MyListenner ml;

	@Override
	public void onReceive(Context context, Intent intent) {
		MainConfig mainConfig = MainConfig.getInstance();
//		String strAllNumsImportant = mainConfig.getImporantNumbers();
//		String strAllNumsCall = mainConfig.getInterceptCallNumbers();
		// if (tm == null) {
		tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		// }
		// if (ml == null) {
		ml = new MyListenner(context);
		// }
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
		} else {
			tm.listen(ml, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	private class MyListenner extends PhoneStateListener {

		private Context con;

		public MyListenner(Context c) {
			this.con = c;
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {

			case TelephonyManager.CALL_STATE_IDLE:

				LogRingForu.v(TAG, "idle");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				LogRingForu.v(TAG, "offhook");
				break;
			case TelephonyManager.CALL_STATE_RINGING:

				LogRingForu.v(TAG, "ring num: " + incomingNumber);
				if (RingForU.getInstance().getNumbersImportant().contains(StringUtil.getRidofSpeciall(incomingNumber))) {
					if (SlientTimeUtil.isEffective(con)) {
						PhoneUtil.turnUpMost(con);
					} else {

						LogRingForu.v(TAG, "not effective!");
					}

				} else if (RingForU.getInstance().getNumbersCall().contains(StringUtil.getRidofSpeciall(incomingNumber))) {

					// 屏蔽电话，
					PhoneUtil.turnDownThenUp(con);
					switch (MainConfig.getInstance().getInterceptCallStyle()) {
					case ConfigCanstants.STYLE_INTERCEPT_CALL_NULL:

						LogRingForu.e(TAG, "INTERCEPT_CALL_STYLE_NULL");

						break;
					case ConfigCanstants.STYLE_INTERCEPT_CALL_SHUTDOWN:

						LogRingForu.e(TAG, "INTERCEPT_CALL_STYLE_SHUTDOWN");
						break;
					case ConfigCanstants.STYLE_INTERCEPT_CALL_RECEIVE_SHUTDOWN:
						// 接听后立刻挂断

						LogRingForu.e(TAG, "INTERCEPT_CALL_STYLE_RECEIVE_SHUTDOWN");
						PhoneUtil.doActionAboutCall(con, PhoneUtil.ANSWER_RINGING_CALL);
						new Handler().postDelayed(new Runnable() {

							public void run() {
								PhoneUtil.doActionAboutCall(con, PhoneUtil.HANG_UP_CALL);
							}
						}, 1000);
						break;
					case ConfigCanstants.STYLE_INTERCEPT_CALL_NO_ANSWER:

						LogRingForu.e(TAG, "STYLE_INTERCEPT_CALL_NO_ANSWER");
						PhoneUtil.doActionAboutCall(con, PhoneUtil.HANG_UP_CALL);
						break;
					default:
						break;
					}
				} else {
					// 正常，判断是否为忙碌模式
					if (BusyModeUtil.isBusyModeOn()) {
						PhoneUtil.doActionAboutCall(con, PhoneUtil.HANG_UP_CALL);
						NotificationUtil.showRefusedNumberNotification(con, incomingNumber);

						LogRingForu.e(TAG, "BUSY MODE, offhook incoming call!");
						String msg = BusyModeUtil.getBusyModeMsgContent(con);
						if (msg != null && msg.length() > 0) {
							PhoneUtil.sendMessage(con, incomingNumber, msg);

							LogRingForu.e(TAG, "BUSY MODE, offhook incoming call then send msg!");
						}
					}
				}
				break;
			}
			tm.listen(ml, PhoneStateListener.LISTEN_NONE);
			tm.getCallState();
		}

	}

}
