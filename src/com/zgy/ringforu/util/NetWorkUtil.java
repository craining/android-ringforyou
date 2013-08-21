package com.zgy.ringforu.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Vibrator;
import android.provider.Settings;

import com.zgy.ringforu.R;
import com.zgy.ringforu.view.MyDialog;


public class NetWorkUtil {

	/**
	 * 
	 * �ж�����״̬�Ƿ����
	 * 
	 * @return true:�������; false:���粻����
	 */

	public static boolean isConnectInternet(Context con) {
		ConnectivityManager conManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // ע�⣬����ж�һ��Ҫ��Ŷ��Ҫ��Ȼ�����

			return networkInfo.isAvailable();
		}

		return false;
	}

	/**
	 * ��ת����������ҳ��
	 * 
	 * @Description:
	 * @param con
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-3
	 */
	public static void setNetConnection(final Context con, final Vibrator vb) {
		// ��������
		MyDialog.Builder builder = new MyDialog.Builder(con);
		builder.setTitle(R.string.str_tip).setMessage(R.string.nonet_tip).setPositiveButton(R.string.str_set, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				if (PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
				dialog.dismiss();
				con.startActivity(new Intent(Settings.ACTION_SETTINGS));
			}
		}).setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				if (PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
			}
		}).create().show();
	}

}
