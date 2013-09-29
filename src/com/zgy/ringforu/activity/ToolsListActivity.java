package com.zgy.ringforu.activity;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.BusyModeUtil;
import com.zgy.ringforu.util.DisableGprsUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.SignalReconnectUtil;
import com.zgy.ringforu.util.SmsLightScreenUtil;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.util.ViewUtil;
import com.zgy.ringforu.util.WaterMarkUtil;
import com.zgy.ringforu.view.MyToast;

public class ToolsListActivity extends BaseGestureActivity implements OnClickListener {

	private static final String TAG = "RToolsActivity";
	private Button btnBack;
	private RelativeLayout layoutWatermark, layoutBusyMode, layoutSmsLightScreen, layoutDisableGprs,
			layoutSignalReconnect, layoutJoke;
	private CheckBox checkWatermarkSwitch, checkSmsLightScreenSwitch, checkDisableGprsSwitch,
			checkSignalReconnectSwitch, checkBusyModeSwitch, checkJokeSwitch;
	private TextView textBusyModeTitle;
	private TextView textBusyModeInfo;

	private String mStrBusyModeTitle = "";

	public static final String ACTION_WATERMARK_ON = "com.zgy.ringforu.ACTION_WATERMARK_ON";
	private ToolsReceiver mReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		RingForUActivityManager.push(this);

		setContentView(R.layout.toolsview);

		mStrBusyModeTitle = getString(R.string.busymode_title);

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
		layoutJoke = (RelativeLayout) findViewById(R.id.layout_tool_joke);

		checkJokeSwitch = (CheckBox) findViewById(R.id.check_tool_joke);
		checkBusyModeSwitch = (CheckBox) findViewById(R.id.check_busymode_switch);
		textBusyModeTitle = (TextView) findViewById(R.id.text_tools_busymode_title);
		textBusyModeInfo = (TextView) findViewById(R.id.text_tools_busymode_info);

		btnBack.setOnClickListener(this);
		layoutJoke.setOnClickListener(this);
		checkWatermarkSwitch.setOnClickListener(this);
		layoutWatermark.setOnClickListener(this);
		layoutBusyMode.setOnClickListener(this);
		checkBusyModeSwitch.setOnClickListener(this);
		layoutSmsLightScreen.setOnClickListener(this);
		layoutDisableGprs.setOnClickListener(this);
		layoutSignalReconnect.setOnClickListener(this);

		MainConfig.getInstance().setRedToolsShown(true);

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

		PhoneUtil.doVibraterNormal(ToolsListActivity.super.mVb);

		switch (v.getId()) {
		case R.id.btn_tools_return:
			RingForUActivityManager.pop(this);
			overridePendingTransition(R.anim.alpha_in, R.anim.push_right_out);
			break;
		case R.id.layout_tool_watermark:
			startActivity(new Intent(ToolsListActivity.this, ToolsWaterMarkActivity.class));
			break;
		case R.id.check_watermark_switch:
			if (WaterMarkUtil.isWaterMarkShowing(ToolsListActivity.this)) {
				// 关闭水印
				WaterMarkUtil.setSwitchOnOff(false);
				WaterMarkUtil.checkState(ToolsListActivity.this);
				refreshSwitch();
			} else {
				// 开启
				WaterMarkUtil.setSwitchOnOff(true);
				if (WaterMarkUtil.isWaterMarkSeted()) {
					WaterMarkUtil.checkState(ToolsListActivity.this);
					refreshSwitch();
				} else {
					startActivity(new Intent(ToolsListActivity.this, ToolsWaterMarkActivity.class));
				}
			}
			break;
		case R.id.layout_tool_disablegprs:
			// 禁用移动网络
			// startActivity(new Intent(RToolsActivity.this,
			// DisableGprsActivity.class));
			if (DisableGprsUtil.isDisableGprsOn()) {
				DisableGprsUtil.ctrlDisableGprsSwitch(ToolsListActivity.this, false);
			} else {
				DisableGprsUtil.ctrlDisableGprsSwitch(ToolsListActivity.this, true);
			}
			refreshSwitch();
			break;
		case R.id.layout_tool_smslightscreen:
			// 短信点亮屏幕操作
			if (SmsLightScreenUtil.isSmsLightScreenOn()) {
				SmsLightScreenUtil.ctrlSmsLightScreenSwitch(false, ToolsListActivity.this);
			} else {
				SmsLightScreenUtil.ctrlSmsLightScreenSwitch(true, ToolsListActivity.this);
				// MyToast.makeText(RToolsActivity.this,
				// R.string.sms_lightscreen_toast_on, Toast.LENGTH_LONG,
				// false).show();
			}
			refreshSwitch();
			break;
		case R.id.layout_tool_signalreconnect:
			// 开关无信号重连操作
			if (SignalReconnectUtil.isSignalReconnectOn()) {
				SignalReconnectUtil.ctrlSignalReconnect(ToolsListActivity.this, false);
			} else {
				if (Build.VERSION.SDK_INT >= 17) {
					MyToast.makeText(ToolsListActivity.this, R.string.signalreconnect_not_support, MyToast.LENGTH_LONG, true).show();
				} else {
					SignalReconnectUtil.ctrlSignalReconnect(ToolsListActivity.this, true);
					MyToast.makeText(ToolsListActivity.this, R.string.signalreconnect_success, MyToast.LENGTH_LONG, false).show();
				}
			}
			refreshSwitch();
			break;
		case R.id.check_busymode_switch:
			// 开关忙碌模式
			if (BusyModeUtil.isBusyModeOn()) {
				BusyModeUtil.setBusyModeOnOff(ToolsListActivity.this, false, null);
			} else {
				String content = BusyModeUtil.getBusyModeMsgContent(ToolsListActivity.this);
				if (TextUtils.isEmpty(content)) {
					BusyModeUtil.setBusyModeOnOff(ToolsListActivity.this, true, null);
				} else {
					BusyModeUtil.setBusyModeOnOff(ToolsListActivity.this, true, content);
				}
				// Intent i = new Intent(RToolsActivity.this,
				// BusyModeActivity.class);
				// startActivity(i);
			}
			refreshSwitch();
			break;
		case R.id.layout_tool_busymode:
			Intent i = new Intent(ToolsListActivity.this, ToolsBusyModeActivity.class);
			startActivity(i);
			break;
		case R.id.layout_tool_joke:
			MainConfig mc = MainConfig.getInstance();
			mc.setPushJokeOn(!mc.isPushMsgOn());
			refreshSwitch();
			break;
		default:
			break;
		}

	}

	/**
	 * 刷新显示是否开关
	 */
	private void refreshSwitch() {

		// 水印开关
		checkWatermarkSwitch.setChecked(WaterMarkUtil.isWaterMarkShowing(ToolsListActivity.this));
		// 如果点亮屏幕为开，则设置图标显示为开
		checkSmsLightScreenSwitch.setChecked(SmsLightScreenUtil.isSmsLightScreenOn());
		// 如果点亮屏幕为开，则设置图标显示为开
		checkDisableGprsSwitch.setChecked(DisableGprsUtil.isDisableGprsOn());
		// 如果无信号后自动重新获取，则设置图标显示为开
		checkSignalReconnectSwitch.setChecked(SignalReconnectUtil.isSignalReconnectOn());

		String title = BusyModeUtil.getMessageTitleFromContent(ToolsListActivity.this);
		if (StringUtil.isNull(title)) {
			textBusyModeTitle.setText(mStrBusyModeTitle);
		} else {
			textBusyModeTitle.setText(mStrBusyModeTitle + " - " + title);
			if (getResources().getStringArray(R.array.busymodes_title)[9].equals(title)) {
				// 不回复短信
				textBusyModeInfo.setText(R.string.busymode_info_notreply);
			} else {
				textBusyModeInfo.setText(R.string.busymode_info);
			}
		}

		// 忙碌模式是否开启
		checkBusyModeSwitch.setChecked(BusyModeUtil.isBusyModeOn());
		BusyModeUtil.checkState(ToolsListActivity.this);

		checkJokeSwitch.setChecked(MainConfig.getInstance().isPushMsgOn());
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

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
		ViewUtil.onButtonPressedBack(btnBack);
		PhoneUtil.doVibraterNormal(super.mVb);
		RingForUActivityManager.pop(this);
		overridePendingTransition(R.anim.alpha_in, R.anim.push_right_out);
	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
	}

}
