package com.zgy.ringforu.config;

public class ConfigCanstants {

	public static final int STYLE_INTERCEPT_CALL_NO_ANSWER = 1;// 直接挂断
	public static final int STYLE_INTERCEPT_CALL_NULL = 2;// 空号
	public static final int STYLE_INTERCEPT_CALL_SHUTDOWN = 3;// 关机
	public static final int STYLE_INTERCEPT_CALL_RECEIVE_SHUTDOWN = 4;// 接听挂断

	public static final int STYLE_INTERCEPT_SMS_SLIENT = 1;// 静音接收
	public static final int STYLE_INTERCEPT_SMS_DISRECEIVE = 2;// 不接收

	public static final String STYLE_INTERCEPT_CALL = "STYLE_INTERCEPT_CALL";
	public static final String STYLE_INTERCEPT_SMS = "STYLE_INTERCEPT_SMS";
	public static final String SWITCH_BUSYMODE = "SWITCH_BUSYMODE";// TRUE为on，false为off
	public static final String BUSYMODE_REPLY = "BUSYMODE_REPLY";// 不为空，则短信回复其内容，为空，不回复
	public static final String SWITCH_DISABLE_GPRS = "SWITCH_DISABLE_GPRS";// TRUE为on
	public static final String SWITCH_SIGNAL_RECONNECT = "SWITCH_SIGNAL_RECONNECT";// TRUE为on
	public static final String SWITCH_SMSLIGHTSCREEN = "SWITCH_SMSLIGHTSCREEN_SWITCH";// TRUE为on
	public static final String SWITCH_SCREEN_WATERMARK = "SWITCH_SCREEN_WATERMARK";// TRUE为on
	public static final String SCREEN_WATERMARK_ALPHA = "SCREEN_WATERMARK_ALPHA";// 透明度
	public static final String SWITCH_OPERA_VIRBRATE = "SWITCH_OPERA_VIRBRATE";
	
	public static final String SLIENT_TIME = "SLIENT_TIME";// 安静时段
	// TODO
	public static final String INTERCEPT_CALL_NUMBER = "INTERCEPT_CALL_NUMBER";
	public static final String INTERCEPT_SMS_NUMBER = "INTERCEPT_SMS_NUMBER";
	public static final String IMPORTANT_CALL_NUMBER = "IMPORTANT_CALL_NUMBER";

	public static final String INTERCEPT_CALL_NAME = "INTERCEPT_CALL_NAME";
	public static final String INTERCEPT_SMS_NAME = "INTERCEPT_SMS_NAME";
	public static final String IMPORTANT_CALL_NAME = "IMPORTANT_CALL_NAME";
}
