package com.zgy.ringforu;

import android.app.Application;
import android.view.WindowManager;

public class RingForU extends Application {

	
	public static boolean DEBUG = true;
	
	
	
	
	private static RingForU application;

	
	/**
	 * ����ȫ�ֱ��� ȫ�ֱ���һ�㶼�Ƚ������ڴ���һ���������������ļ�����ʹ��static��̬���� ����ʹ������Application���������ݵķ���ʵ��ȫ�ֱ���
	 * ע����AndroidManifest.xml�е�Application�ڵ�����android:name=".MyApplication"����
	 */
	private static WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

	public static WindowManager.LayoutParams getMywmParams() {
		return wmParams;
	}
	
	@Override
	public void onCreate() {

		application = this;

		super.onCreate();
	}

	public static RingForU getInstance() {
		return application;
	}

}