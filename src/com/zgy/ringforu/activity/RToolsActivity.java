package com.zgy.ringforu.activity;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.BusyModeUtil;
import com.zgy.ringforu.util.DisableGprsUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.SignalReconnectUtil;
import com.zgy.ringforu.util.SmsLightScreenUtil;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.util.WaterMarkUtil;
import com.zgy.ringforu.view.MyToast;

public class RToolsActivity extends Activity implements OnClickListener {

	private static final String TAG = "RToolsActivity";
	private Button btnBack;
	private RelativeLayout layoutWatermark, layoutBusyMode, layoutSmsLightScreen, layoutDisableGprs, layoutSignalReconnect;
	private CheckBox checkWatermarkSwitch, checkSmsLightScreenSwitch, checkDisableGprsSwitch, checkSignalReconnectSwitch, checkBusyModeSwitch;
	private TextView textBusyModeTitle;
	private TextView textBusyModeInfo;

	private String mStrBusyModeTitle = "";

	private Vibrator vb = null;

	public static final String ACTION_WATERMARK_ON = "com.zgy.ringforu.ACTION_WATERMARK_ON";
	private ToolsReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		RingForUActivityManager.push(this);

		setContentView(R.layout.toolsview);

		mStrBusyModeTitle = getString(R.string.busymode_title);

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		btnBack = (Button) findViewById(R.id.btn_tools_return);
		layoutWatermark = (RelativeLayout) findViewById(R.id.layout_tool_watermark);
		layoutBusyMode = (RelativeLayout) findViewById(R.id.layout_tool_busymode);
		checkWatermarkSwitch = (CheckBox) findViewById(R.id.check_watermark_switch);
		checkSmsLightScreenSwitch = (CheckBox) findViewById(R.id.check_tool_smslightscreen);
		checkDisableGprsSwitch = (CheckBox) findViewById(R.id.check_tool_disablegprs);
		checkSignalReconnectSwitch = (CheckBox) findViewById(R.id.check_tool_signalreconnect);
		layoutSmsLightScreen = (RelativeLayout) findViewById(R.id.layout_tool_smslightscreen);
		layoutDisableGprs = (RelativeLayout) findViewById(R.id.layout_tool_disablegprs);
		layoutSignalReconnect = (RelativeLayout) findViewById(R.id.layout_tool_signalreconnect);

		checkBusyModeSwitch = (CheckBox) findViewById(R.id.check_busymode_switch);
		textBusyModeTitle = (TextView) findViewById(R.id.text_tools_busymode_title);
		textBusyModeInfo = (TextView) findViewById(R.id.text_tools_busymode_info);

		btnBack.setOnClickListener(this);
		checkWatermarkSwitch.setOnClickListener(this);
		layoutWatermark.setOnClickListener(this);
		layoutBusyMode.setOnClickListener(this);
		checkBusyModeSwitch.setOnClickListener(this);
		layoutSmsLightScreen.setOnClickListener(this);
		layoutDisableGprs.setOnClickListener(this);
		layoutSignalReconnect.setOnClickListener(this);

		mReceiver = new ToolsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_WATERMARK_ON);
		registerReceiver(mReceiver, filter);
	}

	@Override
	protected void onResume() {
		refreshSwitch();
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		PhoneUtil.doVibraterNormal(vb);

		switch (v.getId()) {
		case R.id.btn_tools_return:
			RingForUActivityManager.pop(this);
			break;
		case R.id.layout_tool_watermark:
			startActivity(new Intent(RToolsActivity.this, WaterMarkActivity.class));
			break;
		case R.id.check_watermark_switch:
			if (WaterMarkUtil.isWaterMarkShowing(RToolsActivity.this)) {
				// �ر�ˮӡ
				WaterMarkUtil.setSwitchOnOff(false);
				WaterMarkUtil.checkState(RToolsActivity.this);
				refreshSwitch();
			} else {
				// ����
				WaterMarkUtil.setSwitchOnOff(true);
				if (WaterMarkUtil.isWaterMarkSeted()) {
					WaterMarkUtil.checkState(RToolsActivity.this);
					refreshSwitch();
				} else {
					startActivity(new Intent(RToolsActivity.this, WaterMarkActivity.class));
				}
			}
			break;
		case R.id.layout_tool_disablegprs:
			// �����ƶ�����
			// startActivity(new Intent(RToolsActivity.this,
			// DisableGprsActivity.class));
			if (DisableGprsUtil.isDisableGprsOn()) {
				DisableGprsUtil.ctrlDisableGprsSwitch(RToolsActivity.this, false);
			} else {
				DisableGprsUtil.ctrlDisableGprsSwitch(RToolsActivity.this, true);
			}
			refreshSwitch();
			break;
		case R.id.layout_tool_smslightscreen:
			// ���ŵ�����Ļ����
			if (SmsLightScreenUtil.isSmsLightScreenOn()) {
				SmsLightScreenUtil.ctrlSmsLightScreenSwitch(false, RToolsActivity.this);
			} else {
				SmsLightScreenUtil.ctrlSmsLightScreenSwitch(true, RToolsActivity.this);
				// MyToast.makeText(RToolsActivity.this,
				// R.string.sms_lightscreen_toast_on, Toast.LENGTH_LONG,
				// false).show();
			}
			refreshSwitch();
			break;
		case R.id.layout_tool_signalreconnect:
			// �������ź���������
			if (SignalReconnectUtil.isSignalReconnectOn()) {
				SignalReconnectUtil.ctrlSignalReconnect(RToolsActivity.this, false);
			} else {
				if (Build.VERSION.SDK_INT >= 17) {
					MyToast.makeText(RToolsActivity.this, R.string.signalreconnect_not_support, MyToast.LENGTH_LONG, true).show();
				} else {
					SignalReconnectUtil.ctrlSignalReconnect(RToolsActivity.this, true);
					MyToast.makeText(RToolsActivity.this, R.string.signalreconnect_success, MyToast.LENGTH_LONG, false).show();
				}
			}
			refreshSwitch();
			break;
		case R.id.check_busymode_switch:
			// ����æµģʽ
			if (BusyModeUtil.isBusyModeOn()) {
				BusyModeUtil.setBusyModeOnOff(RToolsActivity.this, false, null);
			} else {
				String content = BusyModeUtil.getBusyModeMsgContent(RToolsActivity.this);
				if (TextUtils.isEmpty(content)) {
					BusyModeUtil.setBusyModeOnOff(RToolsActivity.this, true, null);
				} else {
					BusyModeUtil.setBusyModeOnOff(RToolsActivity.this, true, content);
				}
				// Intent i = new Intent(RToolsActivity.this,
				// BusyModeActivity.class);
				// startActivity(i);
			}
			refreshSwitch();
			break;
		case R.id.layout_tool_busymode:
			Intent i = new Intent(RToolsActivity.this, BusyModeActivity.class);
			startActivity(i);
			break;
		default:
			break;
		}

	}

	/**
	 * ˢ����ʾ�Ƿ񿪹�
	 */
	private void refreshSwitch() {

		// ˮӡ����
		checkWatermarkSwitch.setChecked(WaterMarkUtil.isWaterMarkShowing(RToolsActivity.this));
		// ���������ĻΪ����������ͼ����ʾΪ��
		checkSmsLightScreenSwitch.setChecked(SmsLightScreenUtil.isSmsLightScreenOn());
		// ���������ĻΪ����������ͼ����ʾΪ��
		checkDisableGprsSwitch.setChecked(DisableGprsUtil.isDisableGprsOn());
		// ������źź��Զ����»�ȡ��������ͼ����ʾΪ��
		checkSignalReconnectSwitch.setChecked(SignalReconnectUtil.isSignalReconnectOn());

		String title = BusyModeUtil.getMessageTitleFromContent(RToolsActivity.this);
		if (StringUtil.isNull(title)) {
			textBusyModeTitle.setText(mStrBusyModeTitle);
		} else {
			textBusyModeTitle.setText(mStrBusyModeTitle + " - " + title);
			if(getResources().getStringArray(R.array.busymodes_title)[9].equals(title)) {
				//���ظ�����
				textBusyModeInfo.setText(R.string.busymode_info_notreply);
			}else {
				textBusyModeInfo.setText(R.string.busymode_info);
			}
		}

		
		
		// æµģʽ�Ƿ���
		checkBusyModeSwitch.setChecked(BusyModeUtil.isBusyModeOn());
		BusyModeUtil.checkState(RToolsActivity.this);
	}

	private class ToolsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_WATERMARK_ON)) {
				// ˮӡ��̨����
				refreshSwitch();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mReceiver != null) {
			unregisterReceiver(mReceiver);
		}
	}

}
