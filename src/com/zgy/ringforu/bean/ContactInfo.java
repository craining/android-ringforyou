package com.zgy.ringforu.bean;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.RingForU;

public class ContactInfo {

	public boolean match = true;
	public String name;
	public String num;
	public String sortKey;// ∆¥“Ù

	public String getStoreKey() {
		return sortKey;
	}

	public void setStoreKey(String storeKey) {
		this.sortKey = storeKey;
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
		// LogRingForu.e("", "hashCode");
		return this.name.hashCode() + this.num.hashCode() + this.sortKey.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// LogRingForu.e("", "equals");
		if (this == obj) {
			return true;
		}
		if (obj != null) {
			if (obj instanceof ContactInfo) {
				if (((ContactInfo) obj).name.equals(this.name) && ((ContactInfo) obj).num.equals(this.num) && ((ContactInfo) obj).sortKey.equals(this.sortKey)) {
					return true;
				}
			}
		}
		return false;
	}

}
