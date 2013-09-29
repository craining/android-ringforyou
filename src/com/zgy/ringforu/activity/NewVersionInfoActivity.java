package com.zgy.ringforu.activity;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zgy.ringforu.R;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.PushMessageUtils;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.StringUtil;

/**
 * 更新说明
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-9-24
 */

public class NewVersionInfoActivity extends BaseGestureActivity implements OnClickListener {

	// private Button btnOk;
	private TextView textInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_version_info);

		RingForUActivityManager.push(this);

		// btnOk = (Button) findViewById(R.id.btn_new_version_return);
		textInfo = (TextView) findViewById(R.id.text_new_version_info);

		// textInfo.setText("MainConfig.getInstance().getPushNewVersionInfo().replaceAll(PushMessageUtils.MESSAGE_TAG_BREAKLINE,");

		textInfo.setText(MainConfig.getInstance().getPushNewVersionInfo().replaceAll(PushMessageUtils.MESSAGE_TAG_BREAKLINE, "\r\n"));
		MainConfig.getInstance().setPushNewVersionInfo("");

		// btnOk.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(super.mVb);
		// switch (v.getId()) {
		// case R.id.btn_new_version_return:
		//
		// RingForUActivityManager.pop(NewVersionInfoActivity.this);
		// break;

		// default:
		// break;
		// }

	}

}
