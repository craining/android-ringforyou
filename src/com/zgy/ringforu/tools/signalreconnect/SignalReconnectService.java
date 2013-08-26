package com.zgy.ringforu.tools.signalreconnect;

import com.zgy.ringforu.RingForU;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class SignalReconnectService extends Service {

	private static final String TAG = "SignalReconnectService";

	private TelephonyManager mTelephonyMgr;
	private SignalStrengthListener myListenter;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (RingForU.DEBUG)
			Log.e(TAG, "SignalReconnectService is start!");
		if (SignalReconnectUtil.isSignalReconnectOn()) {
			mTelephonyMgr = (TelephonyManager) getSystemService(SignalReconnectService.TELEPHONY_SERVICE);
			myListenter = new SignalStrengthListener();
			mTelephonyMgr.listen(myListenter, PhoneStateListener.LISTEN_SIGNAL_STRENGTH);
		} else {
			stopSelf();
		}
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		if (RingForU.DEBUG)
			Log.e(TAG, "SignalReconnectService is destroy!");
		if (mTelephonyMgr != null && myListenter != null) {
			mTelephonyMgr.listen(myListenter, PhoneStateListener.LISTEN_NONE);
			mTelephonyMgr = null;
			myListenter = null;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class SignalStrengthListener extends PhoneStateListener {

		@Override
		public void onSignalStrengthChanged(int asu) {
			// TODO Auto-generated method stub
			super.onSignalStrengthChanged(asu);
			if (asu < 10) {
				SignalReconnectUtil.doSignalReconnect(SignalReconnectService.this);
			}
			if (RingForU.DEBUG)
				Log.e(TAG, "asu = " + asu);
		}

	}

}
