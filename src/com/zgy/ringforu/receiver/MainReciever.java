package com.zgy.ringforu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zgy.ringforu.tools.disablegprs.DisableGprsUtil;
import com.zgy.ringforu.tools.signalreconnect.SignalReconnectUtil;
import com.zgy.ringforu.tools.watermark.WaterMarkUtil;
import com.zgy.ringforu.util.MainUtil;

public class MainReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("MainReciever", "recive action = " + intent.getAction());
		MainUtil.checkAllService(context);
	}

}
