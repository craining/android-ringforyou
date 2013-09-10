package com.zgy.ringforu.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.RingForUActivityManager;

public class AboutActivity extends BaseGestureActivity implements OnClickListener {


	private Button btnBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);

		RingForUActivityManager.push(this);

		btnBack = (Button) findViewById(R.id.btn_about_return);

		btnBack.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(super.mVb);
		switch (v.getId()) {
		case R.id.btn_about_return:

			RingForUActivityManager.pop(AboutActivity.this);
			break;

		default:
			break;
		}

	}

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
		PhoneUtil.doVibraterNormal(super.mVb);
		RingForUActivityManager.pop(AboutActivity.this);
	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
	}

 
	
	

}
