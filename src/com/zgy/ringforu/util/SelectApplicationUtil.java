package com.zgy.ringforu.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SelectApplicationUtil {

	/**
	 * 
	 * @Description:���������html�ļ�
	 * @param context
	 * @param file
	 * @see:
	 * @since:
	 * @author: ���
	 * @date:2013-8-9
	 */
	public static void viewHtml(Context context, String url) {
		try {// Ĭ��ϵͳ�����
			Uri u = Uri.parse(url);
			Intent it = new Intent();
			it.setAction(Intent.ACTION_VIEW);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.setDataAndType(u, "text/html");
			it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			context.startActivity(it);
		} catch (Exception e) {
			try {// ѡ���б��
				Uri u = Uri.parse(url);
				Intent it = new Intent();
				it.setAction(Intent.ACTION_VIEW);
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				it.setDataAndType(u, "text/html");
				context.startActivity(it);
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
	}
}
