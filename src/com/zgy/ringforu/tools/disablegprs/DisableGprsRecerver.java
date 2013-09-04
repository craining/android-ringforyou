package com.zgy.ringforu.tools.disablegprs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.view.MyToast;

public class DisableGprsRecerver extends BroadcastReceiver {

	private static final String TAG = "DisableGprsRecerver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RingForU.DEBUG)
			LogRingForu.v(TAG, "changed");
		if (DisableGprsUtil.isMobileNetworkEnabled(context) && DisableGprsUtil.isDisableGprsOn()) {
			DisableGprsUtil.gprsEnabled(false, context);
			MyToast.makeText(context, R.string.disable_gprs_on_tip_toast, Toast.LENGTH_LONG, true).show();
		}

	}

}
