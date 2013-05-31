package com.zgy.ringforu.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.R;

public class MyToast extends Toast {

	public MyToast(Context context) {
		super(context);
	}

	public static Toast makeText(Context context, CharSequence text, int duration, boolean error) {
		Toast result = new Toast(context);

		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.toast, null);

		LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_toast);
		TextView tv = (TextView) view.findViewById(R.id.text);
		tv.setText(text);
		// 如果是错误提示，则为黄色背景，否则为绿色背景
		if (error) {
			layout.setBackgroundColor(context.getResources().getColor(R.color.toast_red));
		} else {
			layout.setBackgroundColor(context.getResources().getColor(R.color.toast_blue));
		}

		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		result.setView(view);
		result.setDuration(duration);

		return result;
	}

	public static Toast makeText(Context context, int resId, int duration, boolean error) {
		Toast result = new Toast(context);

		LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflate.inflate(R.layout.toast, null);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.layout_toast);
		TextView tv = (TextView) view.findViewById(R.id.text);
		tv.setText(context.getResources().getText(resId));
		// 如果是错误提示，则为黄色背景，否则为绿色背景
		if (error) {
			layout.setBackgroundColor(context.getResources().getColor(R.color.toast_red));
		} else {
			layout.setBackgroundColor(context.getResources().getColor(R.color.toast_blue));
		}
		view.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		result.setView(view);
		result.setDuration(duration);

		return result;
	}
}
