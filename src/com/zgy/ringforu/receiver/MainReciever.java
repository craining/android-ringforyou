package com.zgy.ringforu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.util.MainUtil;

public class MainReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RingForU.DEBUG)
			Log.e("MainReciever", "recive action = " + intent.getAction());
		MainUtil.checkAllService(context);
	}

}
