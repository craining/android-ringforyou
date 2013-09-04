package com.zgy.ringforu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.util.MainUtil;

public class MainReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (RingForU.DEBUG)
			LogRingForu.e("MainReciever", "recive action = " + intent.getAction());
		MainUtil.checkAllService(context);
	}

}
