package com.zgy.ringforu.util;

import java.io.File;

import android.content.Context;
import android.widget.Toast;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.activity.SetActivity;
import com.zgy.ringforu.activity.TabCallActivity;
import com.zgy.ringforu.activity.TabImportantActivity;
import com.zgy.ringforu.activity.TabSmsActivity;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.view.MyToast;

public class ImportExportUtil {

	/**
	 * 导出备份
	 * 
	 * @Description:
	 * @param context
	 * @param mainConfig
	 * @param tag
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-10
	 */
	public static void exportData(Context context, int tag) {

		MainConfig mainConfig = MainConfig.getInstance();

		switch (tag) {
		case MainCanstants.TYPE_IMPORTANT:
			// 备份重要联系人
			String importantNums = RingForU.getInstance().getNumbersImportant();
			if (StringUtil.isNull(importantNums)) {
				MyToast.makeText(context, R.string.set_backup_noimportant, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.write(mainConfig.getImporantNames(), MainCanstants.getSdFileImportantName(), false) && FileUtil.write(RingForU.getInstance().getNumbersImportant(), MainCanstants.getSdFileImportantNum(), false)) {
					MyToast.makeText(context, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(context, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(context, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
			}

			break;
		case MainCanstants.TYPE_INTECEPT_CALL:
			String callNums = RingForU.getInstance().getNumbersCall();
			if (StringUtil.isNull(callNums)) {
				MyToast.makeText(context, R.string.set_backup_nocall, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 备份通话拦截数据
			try {
				if (FileUtil.write(mainConfig.getInterceptCallNames(), MainCanstants.getSdFileCallName(), false) && FileUtil.write(RingForU.getInstance().getNumbersCall(), MainCanstants.getSdFileCallNum(), false)) {
					MyToast.makeText(context, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(context, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(context, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
			}

			break;
		case MainCanstants.TYPE_INTECEPT_SMS:
			// 备份拦截短信
			String smsNumbers = RingForU.getInstance().getNumbersSms();
			if (StringUtil.isNull(smsNumbers)) {
				MyToast.makeText(context, R.string.set_backup_nosms, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.write(mainConfig.getInterceptSmsNames(), MainCanstants.getSdFileSmsName(), false) && FileUtil.write(RingForU.getInstance().getNumbersSms(), MainCanstants.getSdFileSmsNum(), false)) {
					MyToast.makeText(context, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(context, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(context, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
			}

			break;

		default:
			break;
		}
	}

	/**
	 * 导入备份
	 * 
	 * @Description:
	 * @param context
	 * @param mainConfig
	 * @param tag
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-10
	 */
	public static void importData(Context context, int tag) {
		MainConfig mainConfig = MainConfig.getInstance();
		switch (tag) {
		case MainCanstants.TYPE_IMPORTANT:
			// 导入备份
			if (!MainCanstants.getSdFileImportantName().exists()) {
				MyToast.makeText(context, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				MainUtil.refreshImportant(FileUtil.read(MainCanstants.getSdFileImportantNum()), FileUtil.read(MainCanstants.getSdFileImportantName()));
				MyToast.makeText(context, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				((TabImportantActivity) context).initListView();
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(context, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
			}
			break;
		case MainCanstants.TYPE_INTECEPT_CALL:
			if (!MainCanstants.getSdFileCallName().exists()) {
				MyToast.makeText(context, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 导入备份
			try {
				MainUtil.refreshCall(FileUtil.read(MainCanstants.getSdFileCallNum()), FileUtil.read(MainCanstants.getSdFileCallName()));
				MyToast.makeText(context, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				((TabCallActivity) context).initListView();
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(context, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
			}
			break;
		case MainCanstants.TYPE_INTECEPT_SMS:
			if (!MainCanstants.getSdFileSmsName().exists()) {
				MyToast.makeText(context, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 导入备份
			try {
				MainUtil.refreshSms(FileUtil.read(MainCanstants.getSdFileSmsNum()), FileUtil.read(MainCanstants.getSdFileSmsName()));
				MyToast.makeText(context, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				((TabSmsActivity) context).initListView();
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(context, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
			}
			break;

		default:
			break;
		}
	}
}
