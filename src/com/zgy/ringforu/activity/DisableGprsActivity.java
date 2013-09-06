package com.zgy.ringforu.activity;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.util.DisableGprsUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.view.MyToast;

public class DisableGprsActivity extends Activity implements OnClickListener {

	private Button btnOpen;
	private Button btnClose;
	private TextView textShowState;
	private Button btnBack;

	private Vibrator vb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_disablegprs);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		DisableGprsUtil.checkDisableGprsState(DisableGprsActivity.this);

		btnOpen = (Button) findViewById(R.id.btn_open);
		btnClose = (Button) findViewById(R.id.btn_close);
		btnBack = (Button) findViewById(R.id.btn_back);
		textShowState = (TextView) findViewById(R.id.text_showstate);

		btnOpen.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);
		btnClose.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);
		btnBack.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);

		if (DisableGprsUtil.isDisableGprsOn()) {
			textShowState.setText(R.string.disable_gprs_on);
			btnOpen.setVisibility(View.GONE);
		} else {
			textShowState.setText(R.string.disable_gprs_off);
			btnClose.setVisibility(View.GONE);
		}

		btnOpen.setOnClickListener(this);
		btnClose.setOnClickListener(this);
		btnBack.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(vb);
		switch (v.getId()) {
		case R.id.btn_open:
			DisableGprsUtil.ctrlDisableGprsSwitch(DisableGprsActivity.this, true);
			MyToast.makeText(DisableGprsActivity.this, R.string.disable_gprs_on_tip, Toast.LENGTH_LONG, false).show();
			finish();
			break;
		case R.id.btn_close:
			DisableGprsUtil.ctrlDisableGprsSwitch(DisableGprsActivity.this, false);
			MyToast.makeText(DisableGprsActivity.this, R.string.disable_gprs_off_tip, Toast.LENGTH_LONG, false).show();
			finish();
			break;
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}

	}

}
