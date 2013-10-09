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
	// 最多可添加10个
	public static final int MAX_NUMS = 10;

	public static final String FEEDBACK_EMAIL_TO = "craining@163.com";
	public static final String FEEDBACK_TITLE = "RingForYou反馈";
	public static final String FEEDBACK_VERSION = "\r\n-------------------\r\nAPP VERSION: v2.0";
	public static final String FEEDBACK_NO_EMAIL_LABEL = "\r\n\r\nNo Email";
	public static final String FEEDBACK_EMAIL_LABEL = "\r\r\n\r\nEmail:  ";

	public static final int[] INT_ONFLING_LEN = { 100, 70 };

	public static final int BUTTON_PRESSED_STATUES_SHOW_TIME = 500;// 按钮按下状态显示时间，手势触发时的ui显示

	public static final String FILE_INNER = "/data/data/com.zgy.ringforu/files/";
	private static final String FILE_PATH_IN_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ringforu/";
	private static final File FILE_IN_SDCARD = new File(FILE_PATH_IN_SDCARD);

	private static final File FILE_SDCARD_IMPORTANT_NUM = new File(FILE_PATH_IN_SDCARD + "importantnumbers.cfg");
	private static final File FILE_SDCARD_IMPORTANT_NAME = new File(FILE_PATH_IN_SDCARD + "importantnames.cfg");

	private static final File FILE_SDCARD_CALL_NUM = new File(FILE_PATH_IN_SDCARD + "callnumbers.cfg");
	private static final File FILE_SDCARD_CALL_NAME = new File(FILE_PATH_IN_SDCARD + "callnames.cfg");

	private static final File FILE_SDCARD_SMS_NUM = new File(FILE_PATH_IN_SDCARD + "smsnumbers.cfg");
	private static final File FILE_SDCARD_SMS_NAME = new File(FILE_PATH_IN_SDCARD + "smsnames.cfg");

	public static final File FILE_WATERMARK_IMG = new File("/data/data/com.zgy.ringforu/files/watermark.jpg");
	private static final String FILE_WATERMARK_IMG_TEMP_CUT_DES = MainCanstants.FILE_PATH_IN_SDCARD + "cut";
	private static final String FILE_WATERMARK_IMG_TEMP_CUT_SRC = MainCanstants.FILE_PATH_IN_SDCARD + "src";
	public static final String SERVICE_NAME_WATERMARK = "com.zgy.ringforu.service.WaterMarkService";
	
	public static final String ACTIVITY_NAME_PUSHMESSAGE_LIST = "com.zgy.ringforu.activity.PushMessageListActivity";
	public static final String PACKAGE_NAME = "com.zgy.ringforu";
	
	public static final int WATER_MARK_ALPHA_DEF = 50;

	
	
	public static final String SPLIT_TAG = ":::";//分隔符
	
	
	
	
	
	
	
	
	
	////////////////////////////////////////////
	public static File getSdFile() {
		checkSdFile();
		return FILE_IN_SDCARD;
	}

	public static void checkSdFile() {
		if (!FILE_IN_SDCARD.exists()) {
			FILE_IN_SDCARD.mkdirs();
		}
	}

	public static File getSdFileImportantNum() {
		checkSdFile();
		return FILE_SDCARD_IMPORTANT_NUM;
	}

	public static File getSdFileImportantName() {
		checkSdFile();
		return FILE_SDCARD_IMPORTANT_NAME;
	}

	public static File getSdFileSmsNum() {
		checkSdFile();
		return FILE_SDCARD_SMS_NUM;
	}

	public static File getSdFileSmsName() {
		checkSdFile();
		return FILE_SDCARD_SMS_NAME;
	}

	public static File getSdFileCallNum() {
		checkSdFile();
		return FILE_SDCARD_CALL_NUM;
	}

	public static File getSdFileCallName() {
		checkSdFile();
		return FILE_SDCARD_CALL_NAME;
	}

	public static String getsdFileWaterMarkCutSrcTemp() {
		checkSdFile();
		return FILE_WATERMARK_IMG_TEMP_CUT_SRC;
	}
	
	public static String getsdFileWaterMarkCutDesTemp() {
		checkSdFile();
		return FILE_WATERMARK_IMG_TEMP_CUT_DES;
	}
}
