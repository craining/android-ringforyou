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
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.view.MyToast;

public class ToolsDisableGprsActivity extends BaseGestureActivity implements OnClickListener {

	private Button btnCtrl;
	private TextView textShowState, textShowContent;
	private Button btnBack;

	private Vibrator vb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_dlg_ctrl);
		
		RingForUActivityManager.push(this);
		
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		DisableGprsUtil.checkState(ToolsDisableGprsActivity.this);

		btnCtrl = (Button) findViewById(R.id.btn_ctrl);
		btnBack = (Button) findViewById(R.id.btn_back);
		textShowState = (TextView) findViewById(R.id.text_showstate);
		textShowContent = (TextView) findViewById(R.id.text_showcontent);

		btnCtrl.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);
		btnBack.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);

		textShowContent.setText(R.string.gprs_msg);

		if (DisableGprsUtil.isDisableGprsOn()) {
			textShowState.setText(R.string.disable_gprs_on);
			btnCtrl.setText(R.string.gprs_undisable);
		} else {
			textShowState.setText(R.string.disable_gprs_off);
			btnCtrl.setText(R.string.gprs_disable);
		}

		btnCtrl.setOnClickListener(this);
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
		case R.id.btn_ctrl:
			if (DisableGprsUtil.isDisableGprsOn()) {
				DisableGprsUtil.ctrlDisableGprsSwitch(ToolsDisableGprsActivity.this, false);
				MyToast.makeText(ToolsDisableGprsActivity.this, R.string.disable_gprs_off_tip, Toast.LENGTH_LONG, false).show();
			} else {
				DisableGprsUtil.ctrlDisableGprsSwitch(ToolsDisableGprsActivity.this, true);
				MyToast.makeText(ToolsDisableGprsActivity.this, R.string.disable_gprs_on_tip, Toast.LENGTH_LONG, false).show();
			}
			RingForUActivityManager.pop(ToolsDisableGprsActivity.this);
			break;
		case R.id.btn_back:
			RingForUActivityManager.pop(ToolsDisableGprsActivity.this);
			break;

		default:
			break;
		}

	}

}
