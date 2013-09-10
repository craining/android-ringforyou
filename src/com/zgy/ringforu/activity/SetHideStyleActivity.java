package com.zgy.ringforu.activity;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.config.ConfigCanstants;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.RingForUActivityManager;

public class SetHideStyleActivity extends BaseGestureActivity implements OnClickListener {

	private int tag = 1;// 1为通话的拦截方式； 2为短信的拦截方式

	// SMS
	private RelativeLayout smsDisreceive;
	private RelativeLayout smsSlientReceive;

	private ImageView imgSmsDisreceive;
	private ImageView imgSmsSlientReceive;

	// CALL
	private RelativeLayout callNoAnswer;
	private RelativeLayout callNull;
	private RelativeLayout callShutDwon;
	private RelativeLayout callReceiveShutDown;

	private ImageView imgCallNoAnswer;
	private ImageView imgCallNull;
	private ImageView imgCallShutDwon;
	private ImageView imgCallReceiveShutDown;

	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		RingForUActivityManager.push(this);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			tag = b.getInt("style");
		}

		switch (tag) {
		case MainCanstants.TYPE_INTECEPT_CALL:
			setContentView(R.layout.set_hidestyle_call);
			callNoAnswer = (RelativeLayout) findViewById(R.id.layout_sethidestyle_call_no_answer);
			callNull = (RelativeLayout) findViewById(R.id.layout_sethidestyle_call_null);
			callShutDwon = (RelativeLayout) findViewById(R.id.layout_sethidestyle_call_shutdown);
			callReceiveShutDown = (RelativeLayout) findViewById(R.id.layout_sethidestyle_call_receive_shutdown);
			imgCallNoAnswer = (ImageView) findViewById(R.id.img_sethidestyle_call_no_answer);
			imgCallNull = (ImageView) findViewById(R.id.img_sethidestyle_call_null);
			imgCallShutDwon = (ImageView) findViewById(R.id.img_sethidestyle_call_shutdown);
			imgCallReceiveShutDown = (ImageView) findViewById(R.id.img_sethidestyle_call_receive_shutdown);
			btnBack = (Button) findViewById(R.id.btn_sethidestyle_return_call);
			callNoAnswer.setOnClickListener(this);
			callNull.setOnClickListener(this);
			callShutDwon.setOnClickListener(this);
			callReceiveShutDown.setOnClickListener(this);
			refreshCallSelected();
			break;
		case MainCanstants.TYPE_INTECEPT_SMS:
			setContentView(R.layout.set_hidestyle_sms);
			smsDisreceive = (RelativeLayout) findViewById(R.id.layout_sethidestyle_sms_disreceive);
			smsSlientReceive = (RelativeLayout) findViewById(R.id.layout_sethidestyle_sms_slient);
			imgSmsDisreceive = (ImageView) findViewById(R.id.img_sethidestyle_sms_disreceive);
			imgSmsSlientReceive = (ImageView) findViewById(R.id.img_sethidestyle_sms_slient);
			btnBack = (Button) findViewById(R.id.btn_sethidestyle_return_sms);

			smsDisreceive.setOnClickListener(this);
			smsSlientReceive.setOnClickListener(this);
			refreshSmsSelected();
			break;

		default:
			break;
		}

		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(SetHideStyleActivity.super.mVb);
		switch (v.getId()) {
		case R.id.layout_sethidestyle_call_null:
			MainConfig.getInstance().setInterceptCallStyle(ConfigCanstants.STYLE_INTERCEPT_CALL_NULL);
			refreshCallSelected();
			break;
		case R.id.layout_sethidestyle_call_shutdown:
			MainConfig.getInstance().setInterceptCallStyle(ConfigCanstants.STYLE_INTERCEPT_CALL_SHUTDOWN);
			refreshCallSelected();
			break;

		case R.id.layout_sethidestyle_call_no_answer:
			MainConfig.getInstance().setInterceptCallStyle(ConfigCanstants.STYLE_INTERCEPT_CALL_NO_ANSWER);
			refreshCallSelected();
			break;
		case R.id.layout_sethidestyle_call_receive_shutdown:
			MainConfig.getInstance().setInterceptCallStyle(ConfigCanstants.STYLE_INTERCEPT_CALL_RECEIVE_SHUTDOWN);
			refreshCallSelected();
			break;

		case R.id.btn_sethidestyle_return_call:
			RingForUActivityManager.pop(this);
			break;

		case R.id.layout_sethidestyle_sms_disreceive:
			MainConfig.getInstance().setInterceptSmsStyle(ConfigCanstants.STYLE_INTERCEPT_SMS_DISRECEIVE);
			refreshSmsSelected();
			break;
		case R.id.layout_sethidestyle_sms_slient:
			MainConfig.getInstance().setInterceptSmsStyle(ConfigCanstants.STYLE_INTERCEPT_SMS_SLIENT);
			refreshSmsSelected();
			break;
		case R.id.btn_sethidestyle_return_sms:
			RingForUActivityManager.pop(this);
			break;
		default:
			break;
		}
		// }

	}

	private void refreshCallSelected() {
		switch (MainConfig.getInstance().getInterceptCallStyle()) {
		case ConfigCanstants.STYLE_INTERCEPT_CALL_NULL:
			imgCallNull.setVisibility(View.VISIBLE);
			imgCallNoAnswer.setVisibility(View.GONE);
			imgCallShutDwon.setVisibility(View.GONE);
			imgCallReceiveShutDown.setVisibility(View.GONE);
			break;
		case ConfigCanstants.STYLE_INTERCEPT_CALL_SHUTDOWN:
			imgCallNull.setVisibility(View.GONE);
			imgCallNoAnswer.setVisibility(View.GONE);
			imgCallShutDwon.setVisibility(View.VISIBLE);
			imgCallReceiveShutDown.setVisibility(View.GONE);
			break;

		case ConfigCanstants.STYLE_INTERCEPT_CALL_NO_ANSWER:
			imgCallNull.setVisibility(View.GONE);
			imgCallNoAnswer.setVisibility(View.VISIBLE);
			imgCallShutDwon.setVisibility(View.GONE);
			imgCallReceiveShutDown.setVisibility(View.GONE);
			break;

		case ConfigCanstants.STYLE_INTERCEPT_CALL_RECEIVE_SHUTDOWN:
			imgCallNull.setVisibility(View.GONE);
			imgCallNoAnswer.setVisibility(View.GONE);
			imgCallShutDwon.setVisibility(View.GONE);
			imgCallReceiveShutDown.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	private void refreshSmsSelected() {
		switch (MainConfig.getInstance().getInterceptSmsStyle()) {
		case ConfigCanstants.STYLE_INTERCEPT_SMS_DISRECEIVE:
			imgSmsDisreceive.setVisibility(View.VISIBLE);
			imgSmsSlientReceive.setVisibility(View.GONE);
			break;
		case ConfigCanstants.STYLE_INTERCEPT_SMS_SLIENT:
			imgSmsDisreceive.setVisibility(View.GONE);
			imgSmsSlientReceive.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
		PhoneUtil.doVibraterNormal(super.mVb);
		RingForUActivityManager.pop(this);
	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
	}

}
