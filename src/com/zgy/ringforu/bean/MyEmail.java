package com.zgy.ringforu.bean;

public class MyEmail {

	private static final String isMessage = "pushpushMessage";// 弹消息
	private static final String isUpdate = "pushpushUpdate";// 更新app

	private String title = "";
	private String content = "";
	private int attachmentCount = 0;
	private long sendTime;
	private boolean isMessageToPush = false;
	private boolean isUpdateApp = false;

	private String from = "";
	private boolean isRead = false;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title.contains(isMessage)) {
			isMessageToPush = true;
		} else {
			isMessageToPush = false;
		}
		if (title.contains(isUpdate)) {
			isUpdateApp = true;
		} else {
			isUpdateApp = false;
		}
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(int attachmentCount) {
		this.attachmentCount = attachmentCount;
	}

	public long getSendTime() {
		return sendTime;
	}

	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}

	public boolean isMessageToPush() {
		return isMessageToPush;
	}

	public void setMessageToPush(boolean isMessageToPush) {
		this.isMessageToPush = isMessageToPush;
	}

	public boolean isUpdateApp() {
		return isUpdateApp;
	}

	public void setUpdateApp(boolean isUpdateApp) {
		this.isUpdateApp = isUpdateApp;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
}
