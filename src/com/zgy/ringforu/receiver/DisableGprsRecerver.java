package com.zgy.ringforu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.util.DisableGprsUtil;
import com.zgy.ringforu.view.MyToast;

public class DisableGprsRecerver extends BroadcastReceiver {

	private static final String TAG = "DisableGprsRecerver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RingForU.DEBUG)
			LogRingForu.v(TAG, "changed");
		DisableGprsUtil.ctrlNetWorkConnection(context);

	}

}
