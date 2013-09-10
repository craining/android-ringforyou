package com.zgy.ringforu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 同一页面多个textView同时跑马�?
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-8-28
 */
public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}

	// Example:
	// <com.zgy.util.view.views.MarqueeTextView
	// android:layout_width="match_parent"
	// android:layout_height="wrap_content"
	// android:ellipsize="marquee"
	// android:marqueeRepeatLimit="marquee_forever"
	// android:singleLine="true"/>

}
