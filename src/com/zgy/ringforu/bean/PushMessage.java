package com.zgy.ringforu.bean;

public class PushMessage {

	
	
	public static final int READ = 1;
	public static final int UNREAD = 0;

	private int id;
	private String title;
	private String content;
	private String tag;
	private long receiveTime;
	private int readStatue;
	private int sharedTimes = -1;
	
	
	private boolean selected;

	
	public boolean isSelected() {
		return selected;
	}

	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public int getReadStatue() {
		return readStatue;
	}

	public void setReadStatue(int readStatue) {
		this.readStatue = readStatue;
	}

	public int getSharedTimes() {
		return sharedTimes;
	}

	public void setSharedTimes(int sharedTimes) {
		this.sharedTimes = sharedTimes;
	}

}
