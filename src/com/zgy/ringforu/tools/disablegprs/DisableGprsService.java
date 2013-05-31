package com.zgy.ringforu.tools.disablegprs;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.zgy.ringforu.R;
import com.zgy.ringforu.view.MyToast;

public class DisableGprsService extends Service {

	private Handler mainHandler = new mainHandlerClass();
	private static final int MSG_CHECK_NOW = 111;// 是否检测网络状态
	private static final int TIME_DELY = 10000;// 间隔10秒

	private static final String TAG = "DisableGprsService";

	// private TelephonyManager mTelephonyMgr;
	// private ConnectionStateListener myListenter;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.e(TAG, "service is start!");
		if (DisableGprsUtil.isDisableGprsOn()) {
			// mTelephonyMgr = (TelephonyManager) getSystemService(DisableGprsService.TELEPHONY_SERVICE);
			// myListenter = new ConnectionStateListener();
			// mTelephonyMgr.listen(myListenter, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
			mainHandler.removeMessages(MSG_CHECK_NOW);
			mainHandler.sendEmptyMessage(MSG_CHECK_NOW);

		} else {
			stopSelf();
		}

		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "service is destroyed!");
		// if (mTelephonyMgr != null && myListenter != null) {
		// mTelephonyMgr.listen(myListenter, PhoneStateListener.LISTEN_NONE);
		// }
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class mainHandlerClass extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_CHECK_NOW:
				Log.e(TAG, "mainHandlerClass check state");
				if (DisableGprsUtil.isDisableGprsOn()) {
					Log.e(TAG, "mainHandlerClass switch state is open");
					if (DisableGprsUtil.isMobileNetworkEnabled(DisableGprsService.this)) {
						Log.e(TAG, "mainHandlerClass mobile network state is open. and try to close!");
						DisableGprsUtil.gprsEnabled(false, DisableGprsService.this);
						MyToast.makeText(DisableGprsService.this, R.string.disable_gprs_on_tip, Toast.LENGTH_LONG, true).show();
					} else {
						Log.e(TAG, "mainHandlerClass mobile network state is closed!");
					}
					mainHandler.sendEmptyMessageDelayed(MSG_CHECK_NOW, TIME_DELY);
				} else {
					stopSelf();
				}

				break;

			default:
				break;
			}
		}
	}

	// private class ConnectionStateListener extends PhoneStateListener {
	//
	// // @Override
	// // public void onDataConnectionStateChanged(int state) {
	// // super.onDataConnectionStateChanged(state);
	// // switch (state) {
	// // case TelephonyManager.DATA_DISCONNECTED:
	// // Log.e(TAG, "DATA_DISCONNECTED");
	// // break;
	// // case TelephonyManager.DATA_CONNECTING:// 网络正在连接
	// // Log.e(TAG, "DATA_CONNECTING");
	// // break;
	// // case TelephonyManager.DATA_CONNECTED:// 网络连接上
	// // Log.e(TAG, "DATA_CONNECTED");
	// // break;
	// // default:
	// // break;
	// // }
	// //
	// //
	// // }
	//
	// @Override
	// public void onDataConnectionStateChanged(int state, int networkType) {
	// super.onDataConnectionStateChanged(state, networkType);
	// if (networkType == ConnectivityManager.TYPE_MOBILE) {
	// if (DisableGprsUtil.isDisableGprsOn()) {
	// Log.e(TAG, "PhoneStateListener switch state is open");
	// if (DisableGprsUtil.isMobileNetworkEnabled(DisableGprsService.this)) {
	// Log.e(TAG, "PhoneStateListener mobile network state is open. and try to close!");
	// DisableGprsUtil.gprsEnabled(false, DisableGprsService.this);
	// MyToast.makeText(DisableGprsService.this, R.string.disable_gprs_on_tip, Toast.LENGTH_LONG,
	// true).show();
	// } else {
	// Log.e(TAG, "PhoneStateListener mobile network state is closed!");
	// }
	// } else {
	// stopSelf();
	// }
	// }
	// }
	//
	// }

}
