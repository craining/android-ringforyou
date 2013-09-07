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
import com.zgy.ringforu.service.SignalReconnectService;
import com.zgy.ringforu.util.DisableGprsUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.SignalReconnectUtil;
import com.zgy.ringforu.util.SmsLightScreenUtil;
import com.zgy.ringforu.view.MyToast;

public class SignalReconnectActivity extends Activity implements OnClickListener {

	private Button btnCtrl;
	private TextView textShowContent;
	private TextView textTitle;
	private Button btnBack;

	private Vibrator vb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tools_dlg_ctrl);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		DisableGprsUtil.checkState(SignalReconnectActivity.this);

		btnCtrl = (Button) findViewById(R.id.btn_ctrl);
		btnBack = (Button) findViewById(R.id.btn_back);
		textShowContent = (TextView) findViewById(R.id.text_showcontent);
		textTitle = (TextView) findViewById(R.id.text_showstate);

		btnCtrl.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);
		btnBack.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);
		textShowContent.setText(R.string.signalreconnect_msg);

		if (SignalReconnectUtil.isSignalReconnectOn()) {
			textTitle.setText(R.string.title_state_on);
			btnCtrl.setText(R.string.btn_close_state);
		} else {
			textTitle.setText(R.string.title_state_off);
			btnCtrl.setText(R.string.btn_open_state);
		}

		btnCtrl.setOnClickListener(this);
		btnBack.setOnClickListener(this);

	}

	@Override
	protected void onPause() {
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
			if(SignalReconnectUtil.isSignalReconnectOn()) {
				SignalReconnectUtil.ctrlSignalReconnect(SignalReconnectActivity.this, false);
				MyToast.makeText(SignalReconnectActivity.this, R.string.toast_close_ok, Toast.LENGTH_LONG, false).show();
				  
			} else {
				SignalReconnectUtil.ctrlSignalReconnect(SignalReconnectActivity.this, true);
				MyToast.makeText(SignalReconnectActivity.this, R.string.toast_open_ok, Toast.LENGTH_LONG, false).show();
				
			}
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
