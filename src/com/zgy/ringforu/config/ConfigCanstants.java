package com.zgy.ringforu.config;

public class ConfigCanstants {

	public static final int INTERCEPT_CALL_STYLE_NULL = 1;// 空号
	public static final int INTERCEPT_CALL_STYLE_SHUTDOWN = 2;// 关机

	public static final int INTERCEPT_SMS_STYLE_SLIENT = 1;// 静音接收
	public static final int INTERCEPT_SMS_STYLE_DISRECEIVE = 2;// 不接收

	public static final String INTERCEPT_CALL_STYLE = "INTERCEPT_CALL_STYLE";
	public static final String INTERCEPT_SMS_STYLE = "INTERCEPT_SMS_STYLE";
	public static final String BUSYMODE = "BUSYMODE";// TRUE为on，false为off
	public static final String BUSYMODE_REPLY = "BUSYMODE_REPLY";// 不为空，则短信回复其内容，为空，不回复
	public static final String DISABLE_GPRS = "DISABLE_GPRS";// TRUE为on
	public static final String SIGNAL_RECONNECT = "SIGNAL_RECONNECT";// TRUE为on
	public static final String SCREEN_WATERMARK = "SCREEN_WATERMARK";// TRUE为on
	public static final String SCREEN_WATERMARK_ALPHA = "SCREEN_WATERMARK_ALPHA";// 透明度
	public static final String SLIENT_TIME = "SLIENT_TIME";// 安静时段

	// TODO
	public static final String INTERCEPT_CALL_NUMBER = "INTERCEPT_CALL_NUMBER";
	public static final String INTERCEPT_SMS_NUMBER = "INTERCEPT_SMS_NUMBER";
	public static final String IMPORTANT_CALL_NUMBER = "IMPORTANT_CALL_NUMBER";
	
	public static final String INTERCEPT_CALL_NAME = "INTERCEPT_CALL_NAME";
	public static final String INTERCEPT_SMS_NAME = "INTERCEPT_SMS_NAME";
	public static final String IMPORTANT_CALL_NAME = "IMPORTANT_CALL_NAME";
}
