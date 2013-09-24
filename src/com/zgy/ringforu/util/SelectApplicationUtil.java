package com.zgy.ringforu.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SelectApplicationUtil {

	/**
	 * 
	 * @Description:用浏览器打开html文件
	 * @param context
	 * @param file
	 * @see:
	 * @since:
	 * @author: 温楠
	 * @date:2013-8-9
	 */
	public static void viewHtml(Context context, String url) {
		try {// 默认系统浏览器
			Uri u = Uri.parse(url);
			Intent it = new Intent();
			it.setAction(Intent.ACTION_VIEW);
			it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			it.setDataAndType(u, "text/html");
			it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			context.startActivity(it);
		} catch (Exception e) {
			try {// 选择列表打开
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
