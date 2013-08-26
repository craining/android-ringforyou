package com.zgy.ringforu.activity;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;

public class AboutActivity extends Activity {

	private LinearLayout main;
	private Vibrator vb = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		main = (LinearLayout) findViewById(R.id.layout_about);

		main.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				PhoneUtil.doVibraterNormal(vb);
				finish();
				return false;
			}
		});
	}

}
