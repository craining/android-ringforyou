package com.zgy.ringforu.util;

import android.content.Context;
import android.widget.Toast;

import com.zgy.ringforu.R;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.view.MyToast;

public class DisableGprsUtil {

	private static final String TAG = "DisableGprsUtil";

	/**
	 * ����Gprs
	 * 
	 * @param context
	 */
	public static void ctrlNetWorkConnection(Context context) {
		if (NetWorkUtil.isMobileNetworkEnabled(context) && DisableGprsUtil.isDisableGprsOn()) {
			NetWorkUtil.setGprsEnabled(false, context);
			MyToast.makeText(context, R.string.disable_gprs_on_tip_toast, Toast.LENGTH_LONG, true).show();
		}
	}

	/**
	 * �������
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-1-31
	 */
	public static void checkDisableGprsState(Context context) {
		NotificationUtil.showHideDisableGprsNotify(DisableGprsUtil.isDisableGprsOn(), context);
		if (DisableGprsUtil.isDisableGprsOn()) {
			DisableGprsUtil.ctrlNetWorkConnection(context);
		}
	}

	/**
	 * �жϿ����Ƿ���
	 */
	public static boolean isDisableGprsOn() {
		if (MainConfig.getInstance().isDisableGprsOn()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ����
	 */
	public static void ctrlDisableGprsSwitch(Context context, boolean open) {
		MainConfig.getInstance().setDisableGprsOnOff(open);
		checkDisableGprsState(context);
	}

}
