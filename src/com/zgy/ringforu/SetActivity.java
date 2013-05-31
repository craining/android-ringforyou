package com.zgy.ringforu;

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

import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class SetActivity extends Activity implements OnClickListener {

	private static final String TAG = "SetActivity";

	private Button btnBack;
	private ImageView imgV;
	private RelativeLayout layoutFeedback;
	private RelativeLayout layoutHelp;
	private RelativeLayout layoutTools;
	private RelativeLayout layout_set_clear;

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
	private int tag = 0;// Ĭ��Ϊ��Ҫ��ϵ�˵���� 1:���ε绰 2:���ζ���

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

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		btnBack = (Button) findViewById(R.id.btn_set_return);
		layoutFeedback = (RelativeLayout) findViewById(R.id.layout_set_feedback);
		layoutHelp = (RelativeLayout) findViewById(R.id.layout_set_help);
		layoutTools = (RelativeLayout) findViewById(R.id.layout_set_tools);
		layout_set_clear = (RelativeLayout) findViewById(R.id.layout_set_clear);
		imgV = (ImageView) findViewById(R.id.img_set_checkv);

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
		layoutFeedback.setOnClickListener(this);
		layoutHelp.setOnClickListener(this);
		layoutTools.setOnClickListener(this);
		layout_set_clear.setOnClickListener(this);
		imgImportantAddClam.setOnClickListener(this);
		imgImportantDeleteClam1.setOnClickListener(this);
		imgIMportantDeleteClam2.setOnClickListener(this);

		layoutImportantBackup.setOnClickListener(this);
		layoutCallBackup.setOnClickListener(this);
		layoutSmsBackup.setOnClickListener(this);

		layoutCallHideStyle.setOnClickListener(this);
		layoutSmsHideStyle.setOnClickListener(this);

		// �𶯵Ŀ����赥������
		imgV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// �𶯵Ŀ����ر�
				File tag = new File(PhoneUtil.FILE_PATH_VERB_TAG);
				if (!PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
					if (tag.exists()) {
						tag.delete();
					}
				} else {
					if (!tag.exists()) {
						tag.mkdir();
					}
				}

				PhoneUtil.bIsVerbOn = !PhoneUtil.bIsVerbOn;
				refreshView();

			}
		});

	}

	/**
	 * ˢ����ʾ
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-4
	 */
	private void refreshView() {
		Log.v(TAG, "refresh set views");
		// ˢ���𶯿��ص���ʾ
		if (PhoneUtil.bIsVerbOn) {
			imgV.setImageResource(R.drawable.ic_on);
		} else {
			imgV.setImageResource(R.drawable.ic_off);
		}

		// ˢ�°���ʱ�ε���ʾ
		if (!(new File(MainUtil.FILE_PATH_SLIENT_PER)).exists()) {
			// δ��Ӱ���ʱ��
			layoutImportantClam1.setVisibility(View.GONE);
			layoutImportantClam2.setVisibility(View.GONE);
			imgImportantAddClam.setVisibility(View.VISIBLE);
		} else {
			String strSlientP = FileUtil.load(MainUtil.FILE_SLIENT_PER, SetActivity.this, true);
			Log.v(TAG, strSlientP + "<-- strSlientP");
			String[] ps = strSlientP.split(":::");
			if (ps.length == 1) {
				// �����һ������ʱ��
				imgImportantAddClam.setVisibility(View.VISIBLE);
				layoutImportantClam1.setVisibility(View.VISIBLE);
				layoutImportantClam2.setVisibility(View.GONE);
				textImportantClam1.setText(ps[0]);
			} else if (ps.length == 2) {
				// �������������ʱ��
				imgImportantAddClam.setVisibility(View.GONE);
				layoutImportantClam1.setVisibility(View.VISIBLE);
				layoutImportantClam2.setVisibility(View.VISIBLE);
				textImportantClam1.setText(ps[0]);
				textImportantClam2.setText(ps[1]);
			}
		}
		// ���ݲ�ͬ������
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
			// textCallHideStyleTitle.setText((new File(Globle.FILE_PATH_CALL_HIDE_TAG)).exists() ?
			// R.string.set_hidestyle_call2 : R.string.set_hidestyle_call1);
			// textCallHideStyleInfo.setText((new File(Globle.FILE_PATH_CALL_HIDE_TAG)).exists() ?
			// R.string.set_hidestyle_call2_info : R.string.set_hidestyle_call1_info);
			textCallHideStyleInfo.setText((new File(MainUtil.FILE_PATH_CALL_HIDE_TAG)).exists() ? R.string.set_hidestyle_call2 : R.string.set_hidestyle_call1);
			break;
		case MainUtil.TYPE_SMS:
			layoutImportant.setVisibility(View.GONE);
			layoutCall.setVisibility(View.GONE);
			layoutSms.setVisibility(View.VISIBLE);
			// textSmsHideStyleTitle.setText((new File(Globle.FILE_PATH_SMS_HIDE_TAG)).exists() ?
			// R.string.set_hidestyle_sms2 : R.string.set_hidestyle_sms1);
			// textSmsHideStyleInfo.setText((new File(Globle.FILE_PATH_SMS_HIDE_TAG)).exists() ?
			// R.string.set_hidestyle_sms2_info : R.string.set_hidestyle_sms1_info);
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
		case R.id.layout_set_feedback:
			if (PhoneUtil.isConnectInternet(SetActivity.this)) {
				startActivity(new Intent(SetActivity.this, FeedBackActivity.class));
			} else {
				PhoneUtil.setNetConnection(SetActivity.this, vb);
			}
			break;
		case R.id.layout_set_help:
			startActivity(new Intent(SetActivity.this, HelpActivity.class));
			break;
		case R.id.img_del_calm_1_important:
			// ɾ����һ������ʱ��
			String strSlientP = FileUtil.load(MainUtil.FILE_SLIENT_PER, SetActivity.this, true);
			String[] ps = strSlientP.split(":::");
			if (ps.length == 1) {
				(new File(MainUtil.FILE_PATH_SLIENT_PER)).delete();
			} else if (ps.length == 2) {
				strSlientP = ps[1] + ":::";
				FileUtil.save(MainUtil.FILE_SLIENT_PER, strSlientP, SetActivity.this);
			}

			refreshView();
			break;
		case R.id.img_del_calm_2_important:
			// ɾ���ڶ�������ʱ��
			String strSlientP2 = FileUtil.load(MainUtil.FILE_SLIENT_PER, SetActivity.this, true);
			String[] ps2 = strSlientP2.split(":::");
			strSlientP2 = ps2[0] + ":::";
			FileUtil.save(MainUtil.FILE_SLIENT_PER, strSlientP2, SetActivity.this);
			refreshView();
			break;
		case R.id.img_add_calm_important:
			startActivity(new Intent(SetActivity.this, AddSlientPerActivity.class));
			break;
		case R.id.layout_set_tools:
			startActivity(new Intent(SetActivity.this, RToolsActivity.class));
			break;

		case R.id.layout_set_clear:
			// ��ջ���
			clearData();
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
	// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (requestCode == 1) {
	// // ����Ӿ���ʱ�λ�����
	// refreshView();
	// }
	// }

	private void doBackup() {

		if (PhoneUtil.existSDcard()) {
			// �洢���Ѿ�����
			MyDialog.Builder builder = new MyDialog.Builder(SetActivity.this);
			builder.setTitle(R.string.set_backup_select).setMessage(R.string.set_backup_select_text).setPositiveButton(R.string.export_items, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
					// ��������
					exportData();
				}
			}).setNegativeButton(R.string.import_items, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
					// ���뱸��
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
			// ������Ҫ��ϵ��
			if (!(MainUtil.FILE_IMPORTANT_PATH_NAME.exists())) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_noimportant, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.copyFileTo(MainUtil.FILE_IMPORTANT_PATH_NAME, MainUtil.FILE_SDCARD_IMPORTANT_NAME) && FileUtil.copyFileTo(MainUtil.FILE_IMPORTANT_PATH_NUM, MainUtil.FILE_SDCARD_IMPORTANT_NUM)) {
					MyToast.makeText(SetActivity.this, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (IOException e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
			}

			break;
		case MainUtil.TYPE_CALL:
			if (!(MainUtil.FILE_CALL_PATH_NAME.exists())) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nocall, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// ����ͨ����������
			try {
				if (FileUtil.copyFileTo(MainUtil.FILE_CALL_PATH_NAME, MainUtil.FILE_SDCARD_CALL_NAME) && FileUtil.copyFileTo(MainUtil.FILE_CALL_PATH_NUM, MainUtil.FILE_SDCARD_CALL_NUM)) {
					MyToast.makeText(SetActivity.this, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (IOException e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
			}

			break;
		case MainUtil.TYPE_SMS:
			// �������ض���
			if (!(MainUtil.FILE_SMS_PATH_NAME.exists())) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nosms, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.copyFileTo(MainUtil.FILE_SMS_PATH_NAME, MainUtil.FILE_SDCARD_SMS_NAME) && FileUtil.copyFileTo(MainUtil.FILE_SMS_PATH_NUM, MainUtil.FILE_SDCARD_SMS_NUM)) {
					MyToast.makeText(SetActivity.this, R.string.export_success, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.export_fail, Toast.LENGTH_SHORT, true).show();
				}
			} catch (IOException e) {
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
			// ���뱸��
			if (!(MainUtil.FILE_SDCARD_IMPORTANT_NAME.exists())) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			try {
				if (FileUtil.copyFileTo(MainUtil.FILE_SDCARD_IMPORTANT_NAME, MainUtil.FILE_IMPORTANT_PATH_NAME) && FileUtil.copyFileTo(MainUtil.FILE_SDCARD_IMPORTANT_NUM, MainUtil.FILE_IMPORTANT_PATH_NUM)) {
					MyToast.makeText(SetActivity.this, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
					// if (new File(Globle.FILE_IMPORTANT_NUM_LOG).exists()) {
					// new File(Globle.FILE_IMPORTANT_NUM_LOG).delete();
					// }
					// if (new File(Globle.FILE_IMPORTANT_NAME_LOG).exists()) {
					// new File(Globle.FILE_IMPORTANT_NAME_LOG).delete();
					// }
				}
			} catch (IOException e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
				// if (new File(Globle.FILE_IMPORTANT_NUM_LOG).exists()) {
				// new File(Globle.FILE_IMPORTANT_NUM_LOG).delete();
				// }
				// if (new File(Globle.FILE_IMPORTANT_NAME_LOG).exists()) {
				// new File(Globle.FILE_IMPORTANT_NAME_LOG).delete();
				// }
			}
			break;
		case MainUtil.TYPE_CALL:
			if (!(MainUtil.FILE_SDCARD_CALL_NAME.exists())) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// ���뱸��
			try {
				if (FileUtil.copyFileTo(MainUtil.FILE_SDCARD_CALL_NAME, MainUtil.FILE_CALL_PATH_NAME) && FileUtil.copyFileTo(MainUtil.FILE_SDCARD_CALL_NUM, MainUtil.FILE_CALL_PATH_NUM)) {
					MyToast.makeText(SetActivity.this, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
					// if (new File(Globle.FILE_CALL_NUM_LOG).exists()) {
					// new File(Globle.FILE_CALL_NUM_LOG).delete();
					// }
					// if (new File(Globle.FILE_CALL_NAME_LOG).exists()) {
					// new File(Globle.FILE_CALL_NAME_LOG).delete();
					// }
				}
			} catch (IOException e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
				// if (new File(Globle.FILE_IMPORTANT_NUM_LOG).exists()) {
				// new File(Globle.FILE_IMPORTANT_NUM_LOG).delete();
				// }
				// if (new File(Globle.FILE_IMPORTANT_NAME_LOG).exists()) {
				// new File(Globle.FILE_IMPORTANT_NAME_LOG).delete();
				// }
			}
			break;
		case MainUtil.TYPE_SMS:
			if (!MainUtil.FILE_SDCARD_SMS_NAME.exists()) {
				MyToast.makeText(SetActivity.this, R.string.set_backup_nobackups, Toast.LENGTH_SHORT, true).show();
				return;
			}
			// ���뱸��
			try {
				if (FileUtil.copyFileTo(MainUtil.FILE_SDCARD_SMS_NAME, MainUtil.FILE_SMS_PATH_NAME) && FileUtil.copyFileTo(MainUtil.FILE_SDCARD_SMS_NUM, MainUtil.FILE_SMS_PATH_NUM)) {
					MyToast.makeText(SetActivity.this, R.string.import_sueccess, Toast.LENGTH_SHORT, false).show();
				} else {
					MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
					// if (new File(Globle.FILE_SMS_NUM_LOG).exists()) {
					// new File(Globle.FILE_SMS_NUM_LOG).delete();
					// }
					// if (new File(Globle.FILE_SMS_NAME_LOG).exists()) {
					// new File(Globle.FILE_SMS_NAME_LOG).delete();
					// }
				}
			} catch (IOException e) {
				e.printStackTrace();
				MyToast.makeText(SetActivity.this, R.string.import_fail, Toast.LENGTH_SHORT, true).show();
				// if (new File(Globle.FILE_SMS_NUM_LOG).exists()) {
				// new File(Globle.FILE_SMS_NUM_LOG).delete();
				// }
				// if (new File(Globle.FILE_SMS_NAME_LOG).exists()) {
				// new File(Globle.FILE_SMS_NAME_LOG).delete();
				// }
			}
			break;

		default:
			break;
		}
	}

	/**
	 * ��ջ���
	 */
	private void clearData() {
		if (PhoneUtil.existSDcard()) {
			// �����Ҫ��ϵ��
			MyDialog.Builder builder = new MyDialog.Builder(SetActivity.this);
			builder.setTitle(R.string.str_tip).setMessage(R.string.clear_alert).setPositiveButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
				}
			}).setNegativeButton(R.string.str_ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
					// ɾ���ļ�
					MainUtil.clearData();
					MyToast.makeText(SetActivity.this, R.string.clear_data_over, Toast.LENGTH_LONG, false).show();
				}
			}).create().show();
		} else {
			MyToast.makeText(SetActivity.this, R.string.set_backup_nosdcard, Toast.LENGTH_SHORT, true).show();
		}
	}
	/***************************
	 * if (new File(Globle.FILE_SDCARD_NAME).exists() && new File(Globle.FILE_SDCARD_NUM).exists()) {
	 * btnImport.setEnabled(true); } else { btnImport.setEnabled(false); }
	 * 
	 * private BroadcastReceiver broadcastRec;
	 * 
	 * private void listenSdState() { IntentFilter intentFilter = new
	 * IntentFilter(Intent.ACTION_MEDIA_MOUNTED); intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
	 * intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE); intentFilter.addDataScheme("file");
	 * 
	 * broadcastRec = new BroadcastReceiver() {
	 * 
	 * @Override public void onReceive(Context context, Intent intent) { if
	 *           (intent.getAction().equals(Intent.ACTION_MEDIA_MOUNTED)) { Log.v(TAG, "SD card mounted!!!");
	 *           refreshViews(); } else if (intent.getAction().equals(Intent.ACTION_MEDIA_BAD_REMOVAL) ||
	 *           intent.getAction().equals(Intent.ACTION_MEDIA_REMOVED) ||
	 *           intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTED) ||
	 *           intent.getAction().equals(Intent.ACTION_MEDIA_UNMOUNTABLE)) { Log.v(TAG,
	 *           "SD card UNmounted!!!"); btnImport.setEnabled(false); btnExport.setEnabled(false); } } };
	 *           registerReceiver(broadcastRec, intentFilter);// ע��������� }
	 * 
	 *           //onDestroy if (broadcastRec != null) { unregisterReceiver(broadcastRec); broadcastRec =
	 *           null; }
	 * 
	 ****/
}
