package com.zgy.ringforu.util;

import android.content.Context;
import android.content.res.Resources;

import com.zgy.ringforu.R;


public class FontChangeUtil {

	private String simplified_cn;
	private String tw_cn;
	private String huoxing_cn;

	public FontChangeUtil(Context context) {
		Resources res = context.getResources();
		simplified_cn = res.getString(R.string.chinese_sim);
		tw_cn = res.getString(R.string.chinese_tw);
		huoxing_cn = res.getString(R.string.chinese_huoxing);
	}

	/**
	 * 转为简体
	 * 
	 * @param one
	 * @return
	 */
	public String changeToSim(String one) {
		String getString = "";
		int select = -1;
		for (int i = 0; i < one.length(); i++) {
			for (int m = 0; m < simplified_cn.length(); m++) {
				if ((one.charAt(i) == tw_cn.charAt(m)) || (one.charAt(i) == huoxing_cn.charAt(m))) {
					select = m;
				}
			}
			if (select >= 0) {
				getString = getString + simplified_cn.charAt(select);
				select = -1;
			} else {
				getString = getString + one.charAt(i);
			}
		}

		return getString;
	}

	/**
	 * 转为繁体
	 * 
	 * @param two
	 * @return
	 */
	public String changeToTw(String two) {
		String getString = "";
		int select = -1;
		for (int i = 0; i < two.length(); i++) {
			for (int m = 0; m < tw_cn.length(); m++) {
				if ((two.charAt(i) == simplified_cn.charAt(m)) || (two.charAt(i) == huoxing_cn.charAt(m))) {
					select = m;
				}
			}
			if (select >= 0) {
				getString = getString + tw_cn.charAt(select);
				select = -1;
			} else {
				getString = getString + two.charAt(i);
			}
		}

		return getString;

	}

	/**
	 * 转为火星文
	 * 
	 * @param three
	 * @return
	 */
	public String changeToHuoxig(String three) {
		String getString = "";
		int select = -1;

		for (int i = 0; i < three.length(); i++) {
			for (int m = 0; m < huoxing_cn.length(); m++) {
				if ((three.charAt(i) == tw_cn.charAt(m)) || (three.charAt(i) == simplified_cn.charAt(m))) {
					select = m;
				}
			}
			if (select >= 0) {
				getString = getString + huoxing_cn.charAt(select);
				select = -1;
			} else {
				getString = getString + three.charAt(i);
			}
		}
		return getString;
	}
}
