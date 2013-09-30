package com.zgy.ringforu.interfaces;

import java.util.ArrayList;

import com.zgy.ringforu.bean.PushMessage;

public class PushMessageCallBack {

	public void insertPushMessageFinished(boolean result, PushMessage message) {

	}

	public void getPushMessageTotalCountFinished(boolean result, int count) {

	}

	public void getPushMessageListFinished(boolean result, ArrayList<PushMessage> pushMessages) {

	}

	public void setPushMessageReadStatueFinished(boolean result) {

	}

	public void addPushMessageSharedTimesFinished(boolean result) {

	}
}
