package com.zgy.ringforu;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.view.WindowManager;

import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.StringUtil;

public class RingForU extends Application {

	public static boolean DEBUG = false;
	public static boolean DB_SAVE_SDCARD = false;

	private boolean bIsVerbOn = true;
	private boolean bIsGestureOn = true;
	private int selsectedTabId = 0;

	private static RingForU application;

	private String numbersImportant;
	private String numbersCall;
	private String numbersSms;

	private String packageNameHideWaterMark;

	private List<Integer> mAllPushMessageNotificationIds = new ArrayList<Integer>();

	/**
	 * ����ȫ�ֱ��� ȫ�ֱ���һ�㶼�Ƚ������ڴ���һ���������������ļ�����ʹ��static��̬����
	 * ����ʹ������Application��������ݵķ���ʵ��ȫ�ֱ���
	 * ע����AndroidManifest.xml�е�Application�ڵ����android:name=".MyApplication"����
	 */
	private static WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public static WindowManager.LayoutParams getMywmParams() {
		return wmParams;
	}

	@Override
	public void onCreate() {

		application = this;

		MainUtil.mainInitData(application);// ��ʼ������

		super.onCreate();
	}

	public static RingForU getInstance() {
		return application;
	}

	public boolean isbIsVerbOn() {
		return bIsVerbOn;
	}

	public void setbIsVerbOn(boolean bIsVerbOn) {
		this.bIsVerbOn = bIsVerbOn;
	}

	public boolean isbIsGestureOn() {
		return bIsGestureOn;
	}

	public void setbIsGestureOn(boolean bIsGestureOn) {
		this.bIsGestureOn = bIsGestureOn;
	}

	public int getSelsectedTabId() {
		return selsectedTabId;
	}

	public void setSelsectedTabId(int selsectedTabId) {
		this.selsectedTabId = selsectedTabId;
	}

	public String getNumbersImportant() {
		if (StringUtil.isNull(numbersImportant)) {
			MainUtil.refreshNumbersImportant();
		}
		if (numbersImportant == null) {
			numbersImportant = "";
		}
		return numbersImportant;
	}

	public void setNumbersImportant(String numbersImportant) {
		this.numbersImportant = numbersImportant;
	}

	public String getNumbersCall() {
		if (StringUtil.isNull(numbersCall)) {
			MainUtil.refreshNumbersCall();
		}
		if (numbersCall == null) {
			numbersCall = "";
		}
		return numbersCall;
	}

	public void setNumbersCall(String numbersCall) {
		this.numbersCall = numbersCall;
	}

	public String getNumbersSms() {
		if (StringUtil.isNull(numbersSms)) {
			MainUtil.refreshNumbersSms();
		}
		if (numbersSms == null) {
			numbersSms = "";
		}
		return numbersSms;
	}

	public void setNumbersSms(String numbersSms) {
		this.numbersSms = numbersSms;
	}

	public String getPackageNameHideWaterMark() {
		if (packageNameHideWaterMark == null) {
			MainUtil.refreshWaterMarkHideApps();
		}
		return packageNameHideWaterMark;
	}

	public void setPackageNameHideWaterMark(String packageNameHideWaterMark) {
		this.packageNameHideWaterMark = packageNameHideWaterMark;
	}

	public List<Integer> getmAllPushMessageNotificationIds() {
		return mAllPushMessageNotificationIds;
	}

	public void putOneInmAllPushMessageNotificationIds(Integer i) {
		mAllPushMessageNotificationIds.add(i);
	}

	public void removeOneInmAllPushMessageNotificationIds(Integer i) {
		mAllPushMessageNotificationIds.remove(i);
	}
}
