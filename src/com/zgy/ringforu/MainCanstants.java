package com.zgy.ringforu;

import java.io.File;

import android.os.Environment;

public class MainCanstants {

	public static final int TYPE_IMPORTANT = 0;
	public static final int TYPE_INTECEPT_CALL = 1;
	public static final int TYPE_INTECEPT_SMS = 2;
	public static final int TYPE_MORE = 3;

	public static final int DLG_BTN_ALPHA = 100;// 对话框按钮的透明度
	// 操作振动反馈相关
	public static final int VIBRATE_STREGTH_NORMAL = 40;// 振动强度
	public static final long[] VIBRATE_STREGTH_ERROR = new long[] { 0, 20, 100, 20 };// 振动强度
	public static boolean bIsVerbOn = true;
	public static boolean bIsGestureOn = true;
	// 最多可添加10个
	public static final int MAX_NUMS = 10;

	public static final String FEEDBACK_EMAIL_TO = "craining@163.com";
	public static final String FEEDBACK_TITLE = "RingForYou反馈";
	public static final String FEEDBACK_VERSION = "\r\n-------------------\r\nAPP VERSION: v2.0";
	public static final String FEEDBACK_NO_EMAIL_LABEL = "\r\n\r\nNo Email";
	public static final String FEEDBACK_EMAIL_LABEL = "\r\r\n\r\nEmail:  ";

	public static final int[] INT_ONFLING_LEN = { 100, 200 };
	
	public static final int BUTTON_PRESSED_STATUES_SHOW_TIME = 500;//按钮按下状态显示时间，手势触发时的ui显示
	
	public static final String FILE_INNER = "/data/data/com.zgy.ringforu/files/";
	public static final String FILE_IN_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ringforu/";

	public static final File FILE_SDCARD_IMPORTANT_NUM = new File(FILE_IN_SDCARD + "importantnumbers.cfg");
	public static final File FILE_SDCARD_IMPORTANT_NAME = new File(FILE_IN_SDCARD + "importantnames.cfg");

	public static final File FILE_SDCARD_CALL_NUM = new File(FILE_IN_SDCARD + "callnumbers.cfg");
	public static final File FILE_SDCARD_CALL_NAME = new File(FILE_IN_SDCARD + "callnames.cfg");

	public static final File FILE_SDCARD_SMS_NUM = new File(FILE_IN_SDCARD + "smsnumbers.cfg");
	public static final File FILE_SDCARD_SMS_NAME = new File(FILE_IN_SDCARD + "smsnames.cfg");
	
	public static final File FILE_WATERMARK_IMG = new File("/data/data/com.zgy.ringforu/files/watermark.jpg");
	public static final String FILE_WATERMARK_IMG_TEMP_CUT = MainCanstants.FILE_IN_SDCARD + "cut";
	public static final String FILE_WATERMARK_IMG_TEMP_SRC = MainCanstants.FILE_IN_SDCARD + "src";
	public static final String SERVICE_NAME_WATERMARK = "com.zgy.ringforu.service.WaterMarkService";
	public static final int WATER_MARK_ALPHA_DEF = 50;
}
