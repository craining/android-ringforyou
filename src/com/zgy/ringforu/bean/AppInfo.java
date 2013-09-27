package com.zgy.ringforu.bean;

import android.graphics.drawable.Drawable;


public class AppInfo {
	private Drawable appIcon;
	private String name;
	private boolean selected;
	private String packageName;
	
	
	public String getPackageName() {
		return packageName;
	}

	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getAppIcon() {
		return appIcon;
	}
	
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean getSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
