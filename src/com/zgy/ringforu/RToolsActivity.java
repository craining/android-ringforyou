package com.zgy.ringforu;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgy.ringforu.tools.busymode.BusyModeActivity;
import com.zgy.ringforu.tools.busymode.BusyModeUtil;
import com.zgy.ringforu.tools.disablegprs.DisableGprsActivity;
import com.zgy.ringforu.tools.disablegprs.DisableGprsUtil;
import com.zgy.ringforu.tools.signalreconnect.SignalReconnectUtil;
import com.zgy.ringforu.tools.smslightscreen.SmsLightScreenUtil;
import com.zgy.ringforu.tools.watermark.WaterMarkActivity;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.view.MyToast;

public class RToolsActivity extends Activity implements OnClickListener {

	private static final String TAG = "RToolsActivity";
	private Button btnBack;
	private RelativeLayout layoutWatermark;
	// private RelativeLayout layoutDisableGprs;
	// private RelativeLayout layoutSmsLightScreen;

	private ImageView imgSmsLightScreenSwitch;
	private ImageView imgDisableGprsSwitch;
	private ImageView imgSignalReconnectSwitch;
	private ImageView imgBusyModeSwitch;
	private TextView textBusyModeTitle;

	private String mStrBusyModeTitle = "";

	private Vibrator vb = null;

	private static final int REQUEST_WATERMARK = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.toolsview);

		mStrBusyModeTitle = getString(R.string.busymode_title);

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		btnBack = (Button) findViewById(R.id.btn_tools_return);
		layoutWatermark = (RelativeLayout) findViewById(R.id.layout_tool_watermark);
		// layoutDisableGprs = (RelativeLayout) findViewById(R.id.layout_tool_disablegprs);
		// layoutSmsLightScreen = (RelativeLayout) findViewById(R.id.layout_tool_smslightscreen);
		imgSmsLightScreenSwitch = (ImageView) findViewById(R.id.image_smslightscreen_switch);
		imgDisableGprsSwitch = (ImageView) findViewById(R.id.image_disablegprs_switch);
		imgSignalReconnectSwitch = (ImageView) findViewById(R.id.image_signalreconnect_switch);
		imgBusyModeSwitch = (ImageView) findViewById(R.id.image_busymode_switch);
		textBusyModeTitle = (TextView) findViewById(R.id.text_tools_busymode_title);

		btnBack.setOnClickListener(this);
		// layoutDisableGprs.setOnClickListener(this);
		layoutWatermark.setOnClickListener(this);
		imgSmsLightScreenSwitch.setOnClickListener(this);
		imgDisableGprsSwitch.setOnClickListener(this);
		imgSignalReconnectSwitch.setOnClickListener(this);
		imgBusyModeSwitch.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		refreshSwitch();
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		if (PhoneUtil.bIsVerbOn) {
			vb.vibrate(new long[] { 0, 20 }, -1);
		}

		switch (v.getId()) {
		case R.id.btn_tools_return:
			finish();
			break;
		case R.id.layout_tool_watermark:
			// if(!new File(Globle.FILEPATH_WATERMARK).exists()) {
			// // ѡ��ͼ��ͻ����
			// // �����Ҫ��ϵ��
			// MyDialog.Builder builder = new MyDialog.Builder(RToolsActivity.this);
			// builder.setTitle(R.string.watermark_select_tip).setMessage(R.string.watermark_select_str).setPositiveButton(R.string.watermark_album,
			// new DialogInterface.OnClickListener() {
			//
			// public void onClick(DialogInterface dialog, int whichButton) {
			// dialog.dismiss();
			// Intent intent = new Intent(Intent.ACTION_PICK, null);
			// intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			// startActivityForResult(intent, REQUEST_WATERMARK);
			// }
			// }).setNegativeButton(R.string.watermark_camera, new DialogInterface.OnClickListener() {
			//
			// public void onClick(DialogInterface dialog, int whichButton) {
			// dialog.dismiss();
			// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// startActivityForResult(intent, REQUEST_WATERMARK);
			// }
			// }).create().show();
			// } else {
			startActivity(new Intent(RToolsActivity.this, WaterMarkActivity.class));
			// }

			break;
		case R.id.image_disablegprs_switch:
			startActivity(new Intent(RToolsActivity.this, DisableGprsActivity.class));
			break;
		case R.id.image_smslightscreen_switch:
			// ���ŵ�����Ļ����
			if (SmsLightScreenUtil.isSmsLightScreenOn()) {
				SmsLightScreenUtil.ctrlSmsLightScreenSwitch(false);
			} else {
				SmsLightScreenUtil.ctrlSmsLightScreenSwitch(true);
				// MyToast.makeText(RToolsActivity.this, R.string.sms_lightscreen_toast_on, Toast.LENGTH_LONG,
				// false).show();
			}
			refreshSwitch();
			break;
		case R.id.image_signalreconnect_switch:
			// �������ź���������
			if (SignalReconnectUtil.isSignalReconnectOn()) {
				SignalReconnectUtil.ctrlSignalReconnect(RToolsActivity.this, false);
			} else {
				SignalReconnectUtil.ctrlSignalReconnect(RToolsActivity.this, true);
				MyToast.makeText(RToolsActivity.this, R.string.signalreconnect_tip, MyToast.LENGTH_LONG, false).show();
			}
			refreshSwitch();
			break;
		case R.id.image_busymode_switch:
			// ����æµģʽ
			if (BusyModeUtil.isBusyModeOn()) {
				BusyModeUtil.setBusyModeOn(RToolsActivity.this, false, null, false);
			} else {
				Intent i = new Intent(RToolsActivity.this, BusyModeActivity.class);
				i.putExtra("fromnotification", "false");
				startActivity(i);
			}
			refreshSwitch();
			break;
		default:
			break;
		}

	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// if (resultCode == RESULT_OK && requestCode == REQUEST_WATERMARK) {
	// Uri picPath = data.getData();
	// Intent intent = new Intent(RToolsActivity.this, RToolWaterMarkActivity.class);
	// intent.putExtra("picuri", picPath.toString());
	// startActivity(intent);
	// super.onActivityResult(requestCode, resultCode, data);
	// }
	// }

	/**
	 * ˢ����ʾ�Ƿ񿪹�
	 */
	private void refreshSwitch() {
		// ���������ĻΪ����������ͼ����ʾΪ��
		if (SmsLightScreenUtil.isSmsLightScreenOn()) {
			imgSmsLightScreenSwitch.setImageResource(R.drawable.ic_on);
		} else {
			imgSmsLightScreenSwitch.setImageResource(R.drawable.ic_off);
		}
		// ���������ĻΪ����������ͼ����ʾΪ��
		if (DisableGprsUtil.isDisableGprsOn()) {
			imgDisableGprsSwitch.setImageResource(R.drawable.ic_on);
		} else {
			imgDisableGprsSwitch.setImageResource(R.drawable.ic_off);
		}
		// ������źź��Զ����»�ȡ��������ͼ����ʾΪ��
		if (SignalReconnectUtil.isSignalReconnectOn()) {
			imgSignalReconnectSwitch.setImageResource(R.drawable.ic_on);
		} else {
			imgSignalReconnectSwitch.setImageResource(R.drawable.ic_off);
		}
		// æµģʽ�Ƿ���
		if (BusyModeUtil.isBusyModeOn()) {
			imgBusyModeSwitch.setImageResource(R.drawable.ic_on);
			textBusyModeTitle.setText(mStrBusyModeTitle + " - " + BusyModeUtil.getMessageTitleFromContent(RToolsActivity.this));
		} else {
			imgBusyModeSwitch.setImageResource(R.drawable.ic_off);
			textBusyModeTitle.setText(mStrBusyModeTitle);
		}
		BusyModeUtil.checkBusyModeState(RToolsActivity.this);
	}
}
