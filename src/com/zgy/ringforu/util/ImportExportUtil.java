package com.zgy.ringforu.util;

import java.io.File;

import android.content.Context;
import android.widget.Toast;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.activity.SetActivity;
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
			String importantNums = mainConfig.getImporantNumbers();
			if (StringUtil.isNull(importantNums)) {
				MyToast.makeText(context, R.string.set_backup_noimportant, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.write(mainConfig.getImporantNames(), new File(MainUtil.FILE_SDCARD_IMPORTANT_NAME.getAbsolutePath()), false) && FileUtil.write(mainConfig.getImporantNumbers(), new File(MainUtil.FILE_SDCARD_IMPORTANT_NUM.getAbsolutePath()), false)) {
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
			String callNums = mainConfig.getInterceptCallNumbers();
			if (StringUtil.isNull(callNums)) {
				MyToast.makeText(context, R.string.set_backup_nocall, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 备份通话拦截数据
			try {
				if (FileUtil.write(mainConfig.getInterceptCallNames(), new File(MainUtil.FILE_SDCARD_CALL_NAME.getAbsolutePath()), false) && FileUtil.write(mainConfig.getInterceptCallNumbers(), new File(MainUtil.FILE_SDCARD_CALL_NUM.getAbsolutePath()), false)) {
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
			String smsNumbers = mainConfig.getInterceptSmsNumbers();
			if (StringUtil.isNull(smsNumbers)) {
				MyToast.makeText(context, R.string.set_backup_nosms, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.write(mainConfig.getInterceptSmsNames(), new File(MainUtil.FILE_SDCARD_SMS_NAME.getAbsolutePath()), false) && FileUtil.write(mainConfig.getInterceptSmsNumbers(), new File(MainUtil.FILE_SDCARD_SMS_NUM.getAbsolutePath()), false)) {
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
			if (!(MainUtil.FILE_SDCARD_IMPORTANT_NAME.exists())) {
				MyToast.makeText(context, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				mainConfig.setImportantNames(FileUtil.read(new File(MainUtil.FILE_SDCARD_IMPORTANT_NAME.getAbsolutePath())));
				mainConfig.setImportantNumbers(FileUtil.read(new File(MainUtil.FILE_SDCARD_IMPORTANT_NUM.getAbsolutePath())));
				MyToast.makeText(context, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				// MyToast.makeText(conttext, R.string.import_fail,
				// Toast.LENGTH_SHORT, true).show();
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(context, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
			}
			break;
		case MainCanstants.TYPE_INTECEPT_CALL:
			if (!(MainUtil.FILE_SDCARD_CALL_NAME.exists())) {
				MyToast.makeText(context, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 导入备份
			try {
				mainConfig.setInterceptCallNames(FileUtil.read(new File(MainUtil.FILE_SDCARD_CALL_NAME.getAbsolutePath())));
				mainConfig.setInterceptCallNumbers(FileUtil.read(new File(MainUtil.FILE_SDCARD_CALL_NUM.getAbsolutePath())));
				MyToast.makeText(context, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				// MyToast.makeText(conttext, R.string.import_fail,
				// Toast.LENGTH_SHORT, true).show();
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(context, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
			}
			break;
		case MainCanstants.TYPE_INTECEPT_SMS:
			if (!MainUtil.FILE_SDCARD_SMS_NAME.exists()) {
				MyToast.makeText(context, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 导入备份
			try {

				mainConfig.setInterceptSmsNames(FileUtil.read(new File(MainUtil.FILE_SDCARD_SMS_NAME.getAbsolutePath())));
				mainConfig.setInterceptSmsNumbers(FileUtil.read(new File(MainUtil.FILE_SDCARD_SMS_NUM.getAbsolutePath())));
				MyToast.makeText(context, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				// MyToast.makeText(conttext, R.string.import_fail,
				// Toast.LENGTH_SHORT, true).show();
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
