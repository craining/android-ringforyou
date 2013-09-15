package com.zgy.ringforu;

import com.zgy.ringforu.activity.MainActivityGroup;
import com.zgy.ringforu.util.MainUtil;

import android.app.Application;
import android.view.WindowManager;

public class RingForU extends Application {

	// public static boolean DEBUG = true;

	public boolean bIsVerbOn = true;
	public boolean bIsGestureOn = true;
	public int selsectedTabId = 0;

	private static RingForU application;

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

}
