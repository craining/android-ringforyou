package com.zgy.ringforu.util;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Vibrator;
import android.provider.Settings;

import com.zgy.ringforu.R;
import com.zgy.ringforu.view.MyDialog;

public class NetWorkUtil {

	/**
	 * 
	 * 判断网络状态是否可用
	 * 
	 * @return true:网络可用; false:网络不可用
	 */

	public static boolean isConnectInternet(Context con) {
		ConnectivityManager conManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出错

			return networkInfo.isAvailable();
		}

		return false;
	}

	/**
	 * 跳转到设置网络页面
	 * 
	 * @Description:
	 * @param con
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-3
	 */
	public static void setNetConnection(final Context con, final Vibrator vb) {
		// 设置网络
		MyDialog.Builder builder = new MyDialog.Builder(con);
		builder.setTitle(R.string.str_tip).setMessage(R.string.nonet_tip).setPositiveButton(R.string.str_set, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				PhoneUtil.doVibraterNormal(vb);
				dialog.dismiss();
				con.startActivity(new Intent(Settings.ACTION_SETTINGS));
			}
		}).setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				PhoneUtil.doVibraterNormal(vb);
			}
		}).create().show();
	}

	
	/**
	 * 判断打开或关闭 GPRS是否连接
	 */
	public static boolean isMobileNetworkEnabled(Context context) {
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (wifi != State.CONNECTED) {
			State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			if (mobile == State.CONNECTED) {
				return true;
			}
		}
		return false;

	}
	
	
	/**
	 * 打开或关闭 GPRS
	 */
	public static void setGprsEnabled(boolean bEnable, Context context) {

		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Object[] argObjects = null;
		// boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled", conMan);
		// if (isOpen == !bEnable) {
		setGprsEnabled("setMobileDataEnabled", conMan, bEnable);
		// }

	}

	// 检测GPRS是否打开
	private static boolean gprsIsOpenMethod(String methodName, ConnectivityManager conMan) {

		Class cmClass = conMan.getClass();
		Class[] argClasses = null;
		Object[] argObject = null;

		Boolean isOpen = false;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			isOpen = (Boolean) method.invoke(conMan, argObject);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isOpen;
	}

	// 开启/关闭GPRS
	private static void setGprsEnabled(String methodName, ConnectivityManager conMan, boolean isEnable) {
		Class cmClass = conMan.getClass();
		Class[] argClasses = new Class[1];
		argClasses[0] = boolean.class;
		try {
			Method method = cmClass.getMethod(methodName, argClasses);
			method.invoke(conMan, isEnable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
