package com.zgy.ringforu;

public class MainCanstants {

	public static final int TYPE_IMPORTANT = 0;
	public static final int TYPE_INTECEPT_CALL = 1;
	public static final int TYPE_INTECEPT_SMS = 2;
	public static final int TYPE_MORE = 3;

	public static final int DLG_BTN_ALPHA = 100;// �Ի���ť��͸����
	// �����񶯷������
	public static final int VIBRATE_STREGTH_NORMAL = 40;// ��ǿ��
	public static final long[] VIBRATE_STREGTH_ERROR = new long[] { 0, 20, 100, 20 };// ��ǿ��
	public static boolean bIsVerbOn = true;
	public static boolean bIsGestureOn = true;
	// �������10��
	public static final int MAX_NUMS = 10;

	public static final String FEEDBACK_EMAIL_TO = "craining@163.com";
	public static final String FEEDBACK_TITLE = "RingForYou����";
	public static final String FEEDBACK_VERSION = "\r\n-------------------\r\nAPP VERSION: v2.0";
	public static final String FEEDBACK_NO_EMAIL_LABEL = "\r\n\r\nNo Email";
	public static final String FEEDBACK_EMAIL_LABEL = "\r\r\n\r\nEmail:  ";

	public static final int[] INT_ONFLING_LEN = { 100, 60 };
}
