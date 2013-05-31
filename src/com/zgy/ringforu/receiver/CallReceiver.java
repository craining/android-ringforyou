package com.zgy.ringforu.receiver;

import java.io.File;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.zgy.ringforu.tools.busymode.BusyModeUtil;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.StringUtil;

public class CallReceiver extends BroadcastReceiver {

	private static final String TAG = "CallReceiver";

	private TelephonyManager tm;
	private MyListenner ml;

	@Override
	public void onReceive(Context context, Intent intent) {
		String strAllNumsImportant = FileUtil.load(MainUtil.FILE_IMPORTANT_NUM_LOG, context, true);
		String strAllNumsCall = FileUtil.load(MainUtil.FILE_CALL_NUM_LOG, context, true);
		// if (tm == null) {
		tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		// }
		// if (ml == null) {
		ml = new MyListenner(context, strAllNumsImportant, strAllNumsCall);
		// }
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
		} else {
			tm.listen(ml, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	private class MyListenner extends PhoneStateListener {

		private Context con;
		private String strAllNumsImportant;
		private String strAllNumsCall;

		public MyListenner(Context c, String numsImportant, String numsCall) {
			this.con = c;
			this.strAllNumsImportant = numsImportant;
			this.strAllNumsCall = numsCall;
		}

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {

			case TelephonyManager.CALL_STATE_IDLE:
				Log.v(TAG, "idle");
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:
				Log.v(TAG, "offhook");
				break;
			case TelephonyManager.CALL_STATE_RINGING:
				Log.v(TAG, "ring num: " + incomingNumber);
				if (strAllNumsImportant != null && strAllNumsImportant.contains(StringUtil.getRidofSpeciall(incomingNumber))) {
					if (MainUtil.isEffective(con)) {
						PhoneUtil.turnUpMost(con);
					} else {
						Log.v(TAG, "not effective!");
					}

				} else if (strAllNumsCall != null && strAllNumsCall.contains(StringUtil.getRidofSpeciall(incomingNumber))) {

					// ���ε绰��
					if ((new File(MainUtil.FILE_PATH_CALL_HIDE_TAG)).exists()) {

						// ���������̹Ҷ�
						PhoneUtil.doActionAboutCall(con, PhoneUtil.ANSWER_RINGING_CALL);
						new Handler().postDelayed(new Runnable() {

							public void run() {
								PhoneUtil.doActionAboutCall(con, PhoneUtil.HANG_UP_CALL);
							}
						}, 1000);

						// Log.e(TAG, "change phone airplane state to offhook incoming call! -- off");
						// // ����ģʽ
						// PhoneUtil.setAirplaneModeOff(con, true);
						// // SystemClock.sleep(5000);
						// // Log.e(TAG, "change phone airplane state to offhook incoming call! -- on");
						// // PhoneUtil.setAirplaneModeOff(con, false);
						//
						// new Handler().postDelayed(new Runnable() {
						//
						// public void run() {
						// Log.e(TAG, "change phone airplane state to offhook incoming call! -- on");
						// PhoneUtil.setAirplaneModeOff(con, false);
						// }
						// }, 5000);

					} else {
						PhoneUtil.doActionAboutCall(con, PhoneUtil.HANG_UP_CALL);
						// �Զ��Ҷ�
						// try {
						// // Field field =
						// // this.getClass().getSuperclass().getDeclaredField("mAbortBroadcast");
						// // Field field =
						// //
						// CallReceiver.this.getClass().getSuperclass().getDeclaredField("mAbortBroadcast");
						// Field field =
						// CallReceiver.this.getClass().getSuperclass().getDeclaredField("mPendingResult");
						// Object obj = field.get(CallReceiver.this);
						// field = obj.getClass().getDeclaredField("mAbortBroadcast");
						//
						// // Field field =
						// // CallReceiver.this.getClass().getSuperclass().getDeclaredField("mFinished");
						// field.setAccessible(true);
						// field.set(obj, true);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }

						// �޷�abrot����Ϊ������㲥
						// abortBroadcast();
						// setResultData(null);

						// mAbortBroadcast

						Log.e(TAG, "offhook incoming call!");
						// try {
						// Method getITelephonyMethod =
						// TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
						// getITelephonyMethod.setAccessible(true);
						// TelephonyManager mTelephonyManager = (TelephonyManager)
						// con.getSystemService(Context.TELEPHONY_SERVICE);
						// Object mITelephony = getITelephonyMethod.invoke(mTelephonyManager, (Object[])
						// null);
						// mITelephony.getClass().getMethod("endCall", new Class[] {}).invoke(mITelephony, new
						// Object[] {});
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
					}
				} else {
					// �������ж��Ƿ�Ϊæµģʽ
					if (BusyModeUtil.isBusyModeOn()) {
						PhoneUtil.doActionAboutCall(con, PhoneUtil.HANG_UP_CALL);
						BusyModeUtil.showRefusedNumberNotification(con, incomingNumber);
						Log.e(TAG, "BUSY MODE, offhook incoming call!");
						String msg = BusyModeUtil.getBusyModeMsgContent(con);
						if (msg != null && msg.length() > 0) {
							PhoneUtil.sendMessage(con, incomingNumber, msg);
							Log.e(TAG, "BUSY MODE, offhook incoming call then send msg!");
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