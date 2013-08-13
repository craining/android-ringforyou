package com.zgy.ringforu;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
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
import com.zgy.ringforu.tools.watermark.WaterMarkUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.view.MyToast;

public class RToolsActivity extends Activity implements OnClickListener {

	private static final String TAG = "RToolsActivity";
	private Button btnBack;
	private RelativeLayout layoutWatermark;
	private RelativeLayout layoutBusyMode;
	private ImageView imgWatermarkSwitch;
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

	public static final String ACTION_WATERMARK_ON = "com.zgy.ringforu.ACTION_WATERMARK_ON";
	private ToolsReceiver mReceiver;

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
		layoutBusyMode = (RelativeLayout) findViewById(R.id.layout_tool_busymode);
		imgWatermarkSwitch = (ImageView) findViewById(R.id.image_watermark_switch);
		// layoutDisableGprs = (RelativeLayout) findViewById(R.id.layout_tool_disablegprs);
		// layoutSmsLightScreen = (RelativeLayout) findViewById(R.id.layout_tool_smslightscreen);
		imgSmsLightScreenSwitch = (ImageView) findViewById(R.id.image_smslightscreen_switch);
		imgDisableGprsSwitch = (ImageView) findViewById(R.id.image_disablegprs_switch);
		imgSignalReconnectSwitch = (ImageView) findViewById(R.id.image_signalreconnect_switch);
		imgBusyModeSwitch = (ImageView) findViewById(R.id.image_busymode_switch);
		textBusyModeTitle = (TextView) findViewById(R.id.text_tools_busymode_title);

		btnBack.setOnClickListener(this);
		// layoutDisableGprs.setOnClickListener(this);
		imgWatermarkSwitch.setOnClickListener(this);
		layoutWatermark.setOnClickListener(this);
		layoutBusyMode.setOnClickListener(this);
		imgSmsLightScreenSwitch.setOnClickListener(this);
		imgDisableGprsSwitch.setOnClickListener(this);
		imgSignalReconnectSwitch.setOnClickListener(this);
		imgBusyModeSwitch.setOnClickListener(this);

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

		if (PhoneUtil.bIsVerbOn) {
			vb.vibrate(new long[] { 0, 20 }, -1);
		}

		switch (v.getId()) {
		case R.id.btn_tools_return:
			finish();
			break;
		case R.id.layout_tool_watermark:
			startActivity(new Intent(RToolsActivity.this, WaterMarkActivity.class));
			break;
		case R.id.image_watermark_switch:
			if (WaterMarkUtil.isWaterMarkShowing(RToolsActivity.this)) {
				// 关闭水印
				WaterMarkUtil.setSwitchOnOff(false);
				WaterMarkUtil.checkWaterMarkState(RToolsActivity.this);
				refreshSwitch();
			} else {
				// 开启
				WaterMarkUtil.setSwitchOnOff(true);
				if (WaterMarkUtil.isWaterMarkSeted()) {
					WaterMarkUtil.checkWaterMarkState(RToolsActivity.this);
					refreshSwitch();
				} else {
					startActivity(new Intent(RToolsActivity.this, WaterMarkActivity.class));
				}
			}
			break;
		case R.id.image_disablegprs_switch:
			startActivity(new Intent(RToolsActivity.this, DisableGprsActivity.class));
			break;
		case R.id.image_smslightscreen_switch:
			// 短信点亮屏幕操作
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
			// 开关无信号重连操作
			if (SignalReconnectUtil.isSignalReconnectOn()) {
				SignalReconnectUtil.ctrlSignalReconnect(RToolsActivity.this, false);
			} else {
				SignalReconnectUtil.ctrlSignalReconnect(RToolsActivity.this, true);
				MyToast.makeText(RToolsActivity.this, R.string.signalreconnect_tip, MyToast.LENGTH_LONG, false).show();
			}
			refreshSwitch();
			break;
		case R.id.image_busymode_switch:
			// 开关忙碌模式
			if (BusyModeUtil.isBusyModeOn()) {
				BusyModeUtil.setBusyModeOn(RToolsActivity.this, false, null, false);
			} else {
				String content = BusyModeUtil.getBusyModeMsgContent(RToolsActivity.this);
				if (TextUtils.isEmpty(content)) {
					BusyModeUtil.setBusyModeOn(RToolsActivity.this, true, null, false);
				} else {
					BusyModeUtil.setBusyModeOn(RToolsActivity.this, true, content, true);
				}
				// Intent i = new Intent(RToolsActivity.this, BusyModeActivity.class);
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
	 * 刷新显示是否开关
	 */
	private void refreshSwitch() {

		// 水印开关
		if (WaterMarkUtil.isWaterMarkShowing(RToolsActivity.this)) {
			imgWatermarkSwitch.setImageResource(R.drawable.ic_on);
		} else {
			imgWatermarkSwitch.setImageResource(R.drawable.ic_off);
		}
		// 如果点亮屏幕为开，则设置图标显示为开
		if (SmsLightScreenUtil.isSmsLightScreenOn()) {
			imgSmsLightScreenSwitch.setImageResource(R.drawable.ic_on);
		} else {
			imgSmsLightScreenSwitch.setImageResource(R.drawable.ic_off);
		}
		// 如果点亮屏幕为开，则设置图标显示为开
		if (DisableGprsUtil.isDisableGprsOn()) {
			imgDisableGprsSwitch.setImageResource(R.drawable.ic_on);
		} else {
			imgDisableGprsSwitch.setImageResource(R.drawable.ic_off);
		}
		// 如果无信号后自动重新获取，则设置图标显示为开
		if (SignalReconnectUtil.isSignalReconnectOn()) {
			imgSignalReconnectSwitch.setImageResource(R.drawable.ic_on);
		} else {
			imgSignalReconnectSwitch.setImageResource(R.drawable.ic_off);
		}

		String title = BusyModeUtil.getMessageTitleFromContent(RToolsActivity.this);
		if (StringUtil.isNull(title)) {
			textBusyModeTitle.setText(mStrBusyModeTitle);
		} else {
			textBusyModeTitle.setText(mStrBusyModeTitle + " - " + title);
		}

		// 忙碌模式是否开启
		if (BusyModeUtil.isBusyModeOn()) {
			imgBusyModeSwitch.setImageResource(R.drawable.ic_on);
		} else {
			imgBusyModeSwitch.setImageResource(R.drawable.ic_off);
		}
		BusyModeUtil.checkBusyModeState(RToolsActivity.this);
	}

	private class ToolsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(ACTION_WATERMARK_ON)) {
				// 水印后台开启
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
