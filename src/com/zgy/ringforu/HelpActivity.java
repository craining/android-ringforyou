package com.zgy.ringforu;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.zgy.ringforu.util.PhoneUtil;

public class HelpActivity extends Activity {

	private LinearLayout main;
	private Vibrator vb = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		main = (LinearLayout) findViewById(R.id.layout_help);

		main.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
				finish();
				return false;
			}
		});
	}

}
