package com.zgy.ringforu.interfaces;

import java.util.List;

import com.zgy.ringforu.bean.PushMessage;

public class PushMessageCallBack {

	public void insertPushMessageFinished(boolean result, PushMessage message) {

	}

	public void getPushMessageTotalCountFinished(boolean result, int count) {

	}

	public void getPushMessageListFinished(boolean result, List<PushMessage> pushMessages) {

	}

	public void setPushMessageReadStatueFinished(boolean result) {

	}

	public void addPushMessageSharedTimesFinished(boolean result) {

	}

	public void deletePushMessagesFinished(int[] ids, boolean result) {

	}
}
