package com.zgy.ringforu.tools.disablegprs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.view.MyToast;

public class DisableGprsActivity extends Activity {

	private Button btnOpen;
	private Button btnClose;
	private TextView textShowState;
	private Button btnBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_disablegprs);

		DisableGprsUtil.checkDisableGprsState(DisableGprsActivity.this);

		btnOpen = (Button) findViewById(R.id.btn_open);
		btnClose = (Button) findViewById(R.id.btn_close);
		btnBack = (Button) findViewById(R.id.btn_back);
		textShowState = (TextView) findViewById(R.id.text_showstate);

		btnOpen.getBackground().setAlpha(MainUtil.DLG_BTN_ALPHA);
		btnClose.getBackground().setAlpha(MainUtil.DLG_BTN_ALPHA);
		btnBack.getBackground().setAlpha(MainUtil.DLG_BTN_ALPHA);
		
		if (DisableGprsUtil.isDisableGprsOn()) {
			textShowState.setText(R.string.disable_gprs_on);
			btnOpen.setVisibility(View.GONE);
		} else {
			textShowState.setText(R.string.disable_gprs_off);
			btnClose.setVisibility(View.GONE);
		}

		btnOpen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DisableGprsUtil.ctrlDisableGprsSwitch(DisableGprsActivity.this, true);
				MyToast.makeText(DisableGprsActivity.this, R.string.disable_gprs_on_tip, Toast.LENGTH_LONG, false).show();
				finish();
			}
		});

		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DisableGprsUtil.ctrlDisableGprsSwitch(DisableGprsActivity.this, false);
				MyToast.makeText(DisableGprsActivity.this, R.string.disable_gprs_off_tip, Toast.LENGTH_LONG, false).show();
				finish();
			}
		});
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
