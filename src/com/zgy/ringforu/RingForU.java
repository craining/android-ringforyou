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
	 * 创建全局变量 全局变量一般都比较倾向于创建一个单独的数据类文件，并使用static静态变量
	 * 这里使用了在Application中添加数据的方法实现全局变量
	 * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
	 */
	private static WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public static WindowManager.LayoutParams getMywmParams() {
		return wmParams;
	}

	@Override
	public void onCreate() {

		application = this;

		MainUtil.mainInitData(application);// 初始化数据

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
