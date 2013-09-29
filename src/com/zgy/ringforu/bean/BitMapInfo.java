package com.zgy.ringforu.bean;

import android.graphics.Bitmap;

public class BitMapInfo {
	private Bitmap bitMap;
	private boolean scalechanged;

	public Bitmap getBitMap() {
		return bitMap;
	}

	public void setBitMap(Bitmap bitMap) {
		this.bitMap = bitMap;
	}

	public boolean isScalechanged() {
		return scalechanged;
	}

	public void setScalechanged(boolean scalechanged) {
		this.scalechanged = scalechanged;
	}
}
