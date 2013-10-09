package com.zgy.ringforu;

import android.util.Log;

public class LogRingForu {

	// 用来防止msg为空时的异常
	private static final String NULL_STR = "msg is null!";

	public static void d(String tag, String msg) {
		if (RingForU.DEBUG)
			Log.d(tag, msg != null ? msg : NULL_STR);
	}

	public static void d(String tag, String msg, Throwable tr) {
		if (RingForU.DEBUG)
			Log.d(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void e(String tag, String msg) {
		if (RingForU.DEBUG)
			Log.e(tag, msg != null ? msg : NULL_STR);
	}

	public static void e(String tag, String msg, Throwable tr) {
		if (RingForU.DEBUG)
			Log.e(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void v(String tag, String msg) {
		if (RingForU.DEBUG)
			Log.v(tag, msg != null ? msg : NULL_STR);

	}

	public static void v(String tag, String msg, Throwable tr) {
		if (RingForU.DEBUG)
			Log.v(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void i(String tag, String msg) {
		if (RingForU.DEBUG)
			Log.i(tag, msg != null ? msg : NULL_STR);
	}

	public static void i(String tag, String msg, Throwable tr) {
		if (RingForU.DEBUG)
			Log.i(tag, msg != null ? msg : NULL_STR, tr);
	}

	public static void w(String tag, String msg) {
		if (RingForU.DEBUG)
			Log.w(tag, msg != null ? msg : NULL_STR);
	}

	public static void w(String tag, String msg, Throwable tr) {
		if (RingForU.DEBUG)
			Log.w(tag, msg != null ? msg : NULL_STR, tr);
	}
}
