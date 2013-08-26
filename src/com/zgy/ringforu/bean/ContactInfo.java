package com.zgy.ringforu.bean;

import android.util.Log;

import com.zgy.ringforu.RingForU;

public class ContactInfo {

	public boolean match = true;
	public String name;
	public String num;
	public String storeKey;// ∆¥“Ù

	public String getStoreKey() {
		return storeKey;
	}

	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}

	public boolean getMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@Override
	public int hashCode() {
		if (RingForU.DEBUG)
			Log.e("", "hashCode");
		return this.name.hashCode() + this.num.hashCode() + this.storeKey.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (RingForU.DEBUG)
			Log.e("", "equals");
		if (this == obj) {
			return true;
		}
		if (obj != null) {
			if (obj instanceof ContactInfo) {
				if (((ContactInfo) obj).name.equals(this.name) && ((ContactInfo) obj).num.equals(this.num) && ((ContactInfo) obj).storeKey.equals(this.storeKey)) {
					return true;
				}
			}
		}
		return false;
	}

}
