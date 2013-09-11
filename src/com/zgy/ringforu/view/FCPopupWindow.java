package com.zgy.ringforu.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.zgy.ringforu.R;

public class FCPopupWindow {

	private Context mContext;
	private PopupWindow mPopupWindow;

	public FCPopupWindow(Context context) {
		this.mContext = context;

	}

	public void showAsDropDown(View contentView, View anchor, int xoff, int yoff, int width, int height) {
		
		if (isShowing()) {
			mPopupWindow.dismiss();
		}
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(contentView, width, height, true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setAnimationStyle(R.style.top_menu);
			mPopupWindow.update();
		}

		mPopupWindow.showAsDropDown(anchor, xoff, yoff);
	}

	public void showAsDropDown(View contentView, View anchor, int width, int height) {
		showAsDropDown(contentView, anchor, 0, 0, width, height);
	}

	public void showAtBottom(int widthLayoutParams, int heightLayoutParams, View contentView, View parentView, boolean isFocusable, BitmapDrawable bitmapDrawable) {
		if (isShowing()) {
			mPopupWindow.dismiss();
		}
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(contentView, widthLayoutParams, heightLayoutParams, isFocusable);
			mPopupWindow.setBackgroundDrawable(bitmapDrawable);
			mPopupWindow.setAnimationStyle(R.style.top_menu);
			mPopupWindow.update();

		}

		mPopupWindow.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

	}

	public void showAtBottom(View contentView, View parent, int y) {
		if (isShowing()) {
			mPopupWindow.dismiss();
		}
		if (mPopupWindow == null) {
			mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setAnimationStyle(R.style.top_menu);
			mPopupWindow.update();

		}

		mPopupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, y);

	}

	public void showAtBottom(View contentView) {
		showAtBottom(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, contentView, contentView.getRootView(), true, new BitmapDrawable());
	}

	public void setOnDismissListener(OnDismissListener onDismissListener) {
		mPopupWindow.setOnDismissListener(onDismissListener);
	}

	public boolean isShowing() {
		return mPopupWindow != null && mPopupWindow.isShowing();
	}

	public void close() {
		if (isShowing()) {
			this.mPopupWindow.dismiss();
		}
	}

	public Context getContext() {
		return mContext;
	}

}
