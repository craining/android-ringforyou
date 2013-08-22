package com.zgy.ringforu.activity;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.R;
import com.zgy.ringforu.config.ConfigCanstants;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class SetActivity extends Activity implements OnClickListener {

	private static final String TAG = "SetActivity";

	private Button btnBack;

	// Important
	private LinearLayout layoutImportant;
	private RelativeLayout layoutImportantClam1;
	private RelativeLayout layoutImportantClam2;
	private ImageView imgImportantAddClam;
	private TextView textImportantClam1;
	private TextView textImportantClam2;
	private ImageView imgImportantDeleteClam1;
	private ImageView imgIMportantDeleteClam2;
	private RelativeLayout layoutImportantBackup;

	// call
	private LinearLayout layoutCall;
	private RelativeLayout layoutCallBackup;
	private RelativeLayout layoutCallHideStyle;
	private TextView textCallHideStyleTitle;
	private TextView textCallHideStyleInfo;

	// sms
	private LinearLayout layoutSms;
	private RelativeLayout layoutSmsBackup;
	private RelativeLayout layoutSmsHideStyle;
	private TextView textSmsHideStyleTitle;
	private TextView textSmsHideStyleInfo;

	private Vibrator vb = null;
	private int tag = 0;// 默认为重要联系人的添加 1:屏蔽电话 2:屏蔽短信

	private MainConfig mMainConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setview);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			tag = b.getInt("tag");
		}

		mMainConfig = MainConfig.getInstance();

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		btnBack = (Button) findViewById(R.id.btn_set_return);

		// Important
		layoutImportant = (LinearLayout) findViewById(R.id.layout_set_important);
		imgImportantAddClam = (ImageView) findViewById(R.id.img_add_calm_important);
		textImportantClam1 = (TextView) findViewById(R.id.text_calmper_1);
		textImportantClam2 = (TextView) findViewById(R.id.text_calmper_2);
		imgImportantDeleteClam1 = (ImageView) findViewById(R.id.img_del_calm_1_important);
		imgIMportantDeleteClam2 = (ImageView) findViewById(R.id.img_del_calm_2_important);
		layoutImportantClam1 = (RelativeLayout) findViewById(R.id.layout_clam_1_important);
		layoutImportantClam2 = (RelativeLayout) findViewById(R.id.layout_clam_2_important);
		layoutImportantBackup = (RelativeLayout) findViewById(R.id.layout_set_backup_important);

		// call
		layoutCall = (LinearLayout) findViewById(R.id.layout_set_call);
		layoutCallBackup = (RelativeLayout) findViewById(R.id.layout_set_backup_call);
		layoutCallHideStyle = (RelativeLayout) findViewById(R.id.layout_set_hidestyle_call);
		textCallHideStyleTitle = (TextView) findViewById(R.id.text_set_hidestyle_calltitle);
		textCallHideStyleInfo = (TextView) findViewById(R.id.text_set_hidestyle_callinfo);

		// sms
		layoutSms = (LinearLayout) findViewById(R.id.layout_set_sms);
		layoutSmsBackup = (RelativeLayout) findViewById(R.id.layout_set_backup_sms);
		layoutSmsHideStyle = (RelativeLayout) findViewById(R.id.layout_set_hidestyle_sms);
		textSmsHideStyleTitle = (TextView) findViewById(R.id.text_set_hidestyle_smstitle);
		textSmsHideStyleInfo = (TextView) findViewById(R.id.text_set_hidestyle_smsinfo);

		btnBack.setOnClickListener(this);

		imgImportantAddClam.setOnClickListener(this);
		imgImportantDeleteClam1.setOnClickListener(this);
		imgIMportantDeleteClam2.setOnClickListener(this);

		layoutImportantBackup.setOnClickListener(this);
		layoutCallBackup.setOnClickListener(this);
		layoutSmsBackup.setOnClickListener(this);

		layoutCallHideStyle.setOnClickListener(this);
		layoutSmsHideStyle.setOnClickListener(this);
	}

	/**
	 * 刷新显示
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-4
	 */
	private void refreshView() {
		Log.v(TAG, "refresh set views");

		String strSlient = mMainConfig.getSlientTime();

		// 刷新安静时段的显示
		if (StringUtil.isNull(strSlient)) {
			// 未添加安静时段
			layoutImportantClam1.setVisibility(View.GONE);
			layoutImportantClam2.setVisibility(View.GONE);
			imgImportantAddClam.setVisibility(View.VISIBLE);
		} else {
			Log.v(TAG, strSlient + "<-- strSlientP");
			String[] ps = strSlient.split(":::");
			if (ps.length == 1) {
				// 添加了一个安静时段
				imgImportantAddClam.setVisibility(View.VISIBLE);
				layoutImportantClam1.setVisibility(View.VISIBLE);
				layoutImportantClam2.setVisibility(View.GONE);
				textImportantClam1.setText(ps[0]);
			} else if (ps.length == 2) {
				// 添加了两个安静时段
				imgImportantAddClam.setVisibility(View.GONE);
				layoutImportantClam1.setVisibility(View.VISIBLE);
				layoutImportantClam2.setVisibility(View.VISIBLE);
				textImportantClam1.setText(ps[0]);
				textImportantClam2.setText(ps[1]);
			}
		}
		// 根据不同的设置
		switch (tag) {
		case MainUtil.TYPE_IMPORTANT:
			layoutImportant.setVisibility(View.VISIBLE);
			layoutCall.setVisibility(View.GONE);
			layoutSms.setVisibility(View.GONE);
			break;
		case MainUtil.TYPE_CALL:
			layoutImportant.setVisibility(View.GONE);
			layoutCall.setVisibility(View.VISIBLE);
			layoutSms.setVisibility(View.GONE);
			// textCallHideStyleTitle.setText((new
			// File(Globle.FILE_PATH_CALL_HIDE_TAG)).exists() ?
			// R.string.set_hidestyle_call2 : R.string.set_hidestyle_call1);
			// textCallHideStyleInfo.setText((new
			// File(Globle.FILE_PATH_CALL_HIDE_TAG)).exists() ?
			// R.string.set_hidestyle_call2_info :
			// R.string.set_hidestyle_call1_info);
			textCallHideStyleInfo.setText((new File(MainUtil.FILE_PATH_CALL_HIDE_TAG)).exists() ? R.string.set_hidestyle_call2 : R.string.set_hidestyle_call1);
			break;
		case MainUtil.TYPE_SMS:
			layoutImportant.setVisibility(View.GONE);
			layoutCall.setVisibility(View.GONE);
			layoutSms.setVisibility(View.VISIBLE);
			// textSmsHideStyleTitle.setText((new
			// File(Globle.FILE_PATH_SMS_HIDE_TAG)).exists() ?
			// R.string.set_hidestyle_sms2 : R.string.set_hidestyle_sms1);
			// textSmsHideStyleInfo.setText((new
			// File(Globle.FILE_PATH_SMS_HIDE_TAG)).exists() ?
			// R.string.set_hidestyle_sms2_info :
			// R.string.set_hidestyle_sms1_info);
			textSmsHideStyleInfo.setText((new File(MainUtil.FILE_PATH_SMS_HIDE_TAG)).exists() ? R.string.set_hidestyle_sms2 : R.string.set_hidestyle_sms1);
			break;

		default:
			break;
		}

	}

	@Override
	public void onClick(View v) {

		if (PhoneUtil.bIsVerbOn) {
			vb.vibrate(new long[] { 0, 20 }, -1);
		}

		switch (v.getId()) {
		case R.id.btn_set_return:
			finish();
			break;
		case R.id.img_del_calm_1_important:
			// 删除第一个安静时段
			String strSlientP = mMainConfig.getSlientTime();
			String[] ps = strSlientP.split(":::");
			if (ps.length == 1) {
				mMainConfig.setSlientTime("");// TODO delete key?
			} else if (ps.length == 2) {
				mMainConfig.setSlientTime(ps[1]);
			}

			refreshView();
			break;
		case R.id.img_del_calm_2_important:
			// 删除第二个安静时段
			String[] ps2 = mMainConfig.getSlientTime().split(":::");
			mMainConfig.setSlientTime(ps2[0]);
			refreshView();
			break;
		case R.id.img_add_calm_important:
			startActivity(new Intent(SetActivity.this, AddSlientPerActivity.class));
			break;
		case R.id.layout_set_backup_important:
			doBackup();
			break;

		case R.id.layout_set_backup_call:
			doBackup();
			break;

		case R.id.layout_set_backup_sms:
			doBackup();
			break;

		case R.id.layout_set_hidestyle_sms:
			Intent iSms = new Intent(SetActivity.this, SetHideStyleActivity.class);
			iSms.putExtra("style", 2);
			startActivity(iSms);
			break;
		case R.id.layout_set_hidestyle_call:
			Intent iCall = new Intent(SetActivity.this, SetHideStyleActivity.class);
			iCall.putExtra("style", 1);
			startActivity(iCall);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshView();
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (requestCode == 1) {
	// // 从添加静音时段回来的
	// refreshView();
	// }
	// }

	private void doBackup() {

		if (PhoneUtil.existSDcard()) {
			// 存储卡已经挂载
			MyDialog.Builder builder = new MyDialog.Builder(SetActivity.this);
			builder.setTitle(R.string.set_backup_select).setMessage(R.string.set_backup_select_text).setPositiveButton(R.string.export_items, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
					// 导出备份
					exportData();
				}
			}).setNegativeButton(R.string.import_items, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
					// 导入备份
					importData();
				}
			}).create().show();
		} else {
			MyToast.makeText(SetActivity.this, R.string.set_backup_nosdcard, Toast.LENGTH_SHORT, true).show();
		}

	}

	private void exportData() {

		switch (tag) {
		case MainUtil.TYPE_IMPORTANT:
			// 备份重要联系人
			String importantNums = mMainConfig.getImporantNumbers();
			if (StringUtil.isNull(importantNums)) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_noimportant, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.save(MainUtil.FILE_SDCARD_IMPORTANT_NAME.getAbsolutePath(), mMainConfig.getImporantNames(), SetActivity.this)
						&& FileUtil.save(MainUtil.FILE_SDCARD_IMPORTANT_NUM.getAbsolutePath(), mMainConfig.getImporantNumbers(), SetActivity.this)) {
					MyToast.makeText(SetActivity.this, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
			}

			break;
		case MainUtil.TYPE_CALL:
			String callNums = mMainConfig.getInterceptCallNumbers();
			if (StringUtil.isNull(callNums)) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nocall, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 备份通话拦截数据
			try {
				if (FileUtil.save(MainUtil.FILE_SDCARD_CALL_NAME.getAbsolutePath(), mMainConfig.getInterceptCallNames(), SetActivity.this)
						&& FileUtil.save(MainUtil.FILE_SDCARD_CALL_NUM.getAbsolutePath(), mMainConfig.getInterceptCallNumbers(), SetActivity.this)) {
					MyToast.makeText(SetActivity.this, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
			}

			break;
		case MainUtil.TYPE_SMS:
			// 备份拦截短信
			String smsNumbers = mMainConfig.getInterceptSmsNumbers();
			if (StringUtil.isNull(smsNumbers)) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nosms, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.save(MainUtil.FILE_SDCARD_SMS_NAME.getAbsolutePath(), mMainConfig.getInterceptSmsNames(), SetActivity.this)
						&& FileUtil.save(MainUtil.FILE_SDCARD_SMS_NUM.getAbsolutePath(), mMainConfig.getInterceptSmsNumbers(), SetActivity.this)) {
					MyToast.makeText(SetActivity.this, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
			}

			break;

		default:
			break;
		}
	}

	private void importData() {

		switch (tag) {
		case MainUtil.TYPE_IMPORTANT:
			// 导入备份
			if (!(MainUtil.FILE_SDCARD_IMPORTANT_NAME.exists())) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				mMainConfig.setImportantNames(FileUtil.load(MainUtil.FILE_SDCARD_IMPORTANT_NAME.getAbsolutePath(), SetActivity.this));
				mMainConfig.setImportantNumbers(FileUtil.load(MainUtil.FILE_SDCARD_IMPORTANT_NUM.getAbsolutePath(), SetActivity.this));
				MyToast.makeText(SetActivity.this, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				// MyToast.makeText(SetActivity.this, R.string.import_fail,
				// Toast.LENGTH_SHORT, true).show();
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
			}
			break;
		case MainUtil.TYPE_CALL:
			if (!(MainUtil.FILE_SDCARD_CALL_NAME.exists())) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 导入备份
			try {
				mMainConfig.setInterceptCallNames(FileUtil.load(MainUtil.FILE_SDCARD_CALL_NAME.getAbsolutePath(), SetActivity.this));
				mMainConfig.setInterceptCallNumbers(FileUtil.load(MainUtil.FILE_SDCARD_CALL_NUM.getAbsolutePath(), SetActivity.this));
				MyToast.makeText(SetActivity.this, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				// MyToast.makeText(SetActivity.this, R.string.import_fail,
				// Toast.LENGTH_SHORT, true).show();
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
			}
			break;
		case MainUtil.TYPE_SMS:
			if (!MainUtil.FILE_SDCARD_SMS_NAME.exists()) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// 导入备份
			try {

				mMainConfig.setInterceptSmsNames(FileUtil.load(MainUtil.FILE_SDCARD_SMS_NAME.getAbsolutePath(), SetActivity.this));
				mMainConfig.setInterceptSmsNumbers(FileUtil.load(MainUtil.FILE_SDCARD_SMS_NUM.getAbsolutePath(), SetActivity.this));
				MyToast.makeText(SetActivity.this, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				// MyToast.makeText(SetActivity.this, R.string.import_fail,
				// Toast.LENGTH_SHORT, true).show();
			} catch (Exception e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
			}
			break;

		default:
			break;
		}
	}

	/***************************
	 * if (new File(Globle.FILE_SDCARD_NAME).exists() && new
	 * File(Globle.FILE_SDCARD_NUM).exists()) { btnImport.setEnabled(true); }
	 * else { btnImport.setEnabled(false); }
	 * 
	 * private BroadcastReceiver broadcastRec;
	 * 
	 * private void listenSdState() { IntentFilter intentFilter = new
	 * IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
	 * intentFilter.addDataScheme("file");
	 * 
	 * broadcastRec = new BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { if
	 *           (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) {
	 *           Log.v(TAG, "SD card mounted!!!"); refreshViews(); } else if
	 *           (intent.getAction().equals(Intent.ACTION_MEDIA_BAD_REMOVAL) ||
	 *           intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED) ||
	 *           intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED) ||
	 *           intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTABLE)) {
	 *           Log.v(TAG, "SD card UNmounted!!!");
	 *           btnImport.setEnabled(false); btnExport.setEnabled(false); } }
	 *           }; registerReceiver(broadcastRec, intentFilter);// 注册监听函数 }
	 * 
	 *           //onDestroy if (broadcastRec != null) {
	 *           unregisterReceiver(broadcastRec); broadcastRec = null; }
	 * 
	 ****/
}
