package com.zgy.ringforu.view;

 
public class FCMenuItem {

	private int opID;
	private int iconRes = -1;
	private int textRes = -1;

	public FCMenuItem() {
	}

	public FCMenuItem(int opID, int iconRes, int textRes) {
		this.opID = opID;
		this.iconRes = iconRes;
		this.textRes = textRes;
	}

	public int getOpID() {
		return opID;
	}

	public void setOpID(int opID) {
		this.opID = opID;
	}

	public int getIconRes() {
		return iconRes;
	}

	public void setIconRes(int iconRes) {
		this.iconRes = iconRes;
	}

	public int getTextRes() {
		return textRes;
	}

	public void setTextRes(int textRes) {
		this.textRes = textRes;
	}
}
