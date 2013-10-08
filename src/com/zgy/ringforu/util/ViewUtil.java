package com.zgy.ringforu.util;

import android.os.Handler;
import android.widget.Button;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;

public class ViewUtil {

	/**
	 * 手势触发蓝色按钮按下时的状态显示
	 * 
	 * @param btn
	 */
	public static void onButtonPressedBlue(final Button btn) {
		btn.setBackgroundResource(R.drawable.btn_blue_p);
		 
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				btn.setBackgroundResource(R.drawable.bg_btn_blue);
			}
		}, MainCanstants.BUTTON_PRESSED_STATUES_SHOW_TIME);
	}

	/**
	 * 手势触发蓝色按钮按下时的状态显示
	 * 
	 * @param btn
	 */
	public static void onButtonPressedBack(final Button btn) {
		btn.setBackgroundResource(R.drawable.btn_back_p);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				btn.setBackgroundResource(R.drawable.bg_btn_return);
			}
		}, MainCanstants.BUTTON_PRESSED_STATUES_SHOW_TIME);

	}

	/**
	 * 手势触发红色按钮按下时的状态显示
	 * 
	 * @param btn
	 */
	public static void onButtonPressedRed(final Button btn) {
		btn.setBackgroundResource(R.drawable.btn_red_p);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				btn.setBackgroundResource(R.drawable.bg_btn_red);
			}
		}, MainCanstants.BUTTON_PRESSED_STATUES_SHOW_TIME);

	}

}
