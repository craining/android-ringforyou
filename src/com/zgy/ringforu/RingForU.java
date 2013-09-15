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

}
