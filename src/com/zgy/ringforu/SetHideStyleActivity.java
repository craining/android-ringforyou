package com.zgy.ringforu;

import java.io.File;

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

import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;

public class SetHideStyleActivity extends Activity implements OnClickListener {

	private int tag = 1;// 1为通话的拦截方式； 2为短信的拦截方式

	private RelativeLayout layoutStyle1;
	private RelativeLayout layoutStyle2;

	private ImageView imgStyleSelected1;
	private ImageView imgStyleSelected2;

	private Button btnBack;

	private Vibrator vb = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			tag = b.getInt("style");
		}

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		switch (tag) {
		case MainUtil.TYPE_CALL:
			setContentView(R.layout.set_hidestyle_call);
			layoutStyle1 = (RelativeLayout) findViewById(R.id.layout_sethidestyle_call1);
			layoutStyle2 = (RelativeLayout) findViewById(R.id.layout_sethidestyle_call2);
			imgStyleSelected1 = (ImageView) findViewById(R.id.img_sethidestyle_call1);
			imgStyleSelected2 = (ImageView) findViewById(R.id.img_sethidestyle_call2);
			btnBack = (Button) findViewById(R.id.btn_sethidestyle_return_call);
			if ((new File(MainUtil.FILE_PATH_CALL_HIDE_TAG)).exists()) {
				imgStyleSelected1.setVisibility(View.GONE);
				imgStyleSelected2.setVisibility(View.VISIBLE);
			} else {
				imgStyleSelected1.setVisibility(View.VISIBLE);
				imgStyleSelected2.setVisibility(View.GONE);
			}
			break;
		case MainUtil.TYPE_SMS:
			setContentView(R.layout.set_hidestyle_sms);
			layoutStyle1 = (RelativeLayout) findViewById(R.id.layout_sethidestyle_sms1);
			layoutStyle2 = (RelativeLayout) findViewById(R.id.layout_sethidestyle_sms2);
			imgStyleSelected1 = (ImageView) findViewById(R.id.img_sethidestyle_sms1);
			imgStyleSelected2 = (ImageView) findViewById(R.id.img_sethidestyle_sms2);
			btnBack = (Button) findViewById(R.id.btn_sethidestyle_return_sms);
			if ((new File(MainUtil.FILE_PATH_SMS_HIDE_TAG)).exists()) {
				imgStyleSelected1.setVisibility(View.GONE);
				imgStyleSelected2.setVisibility(View.VISIBLE);
			} else {
				imgStyleSelected1.setVisibility(View.VISIBLE);
				imgStyleSelected2.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}

		layoutStyle1.setOnClickListener(this);
		layoutStyle2.setOnClickListener(this);
		imgStyleSelected1.setOnClickListener(this);
		imgStyleSelected2.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (PhoneUtil.bIsVerbOn) {
			vb.vibrate(new long[] { 0, 20 }, -1);
		}
		if (tag == 1) {
			switch (v.getId()) {
			case R.id.layout_sethidestyle_call1:
				if ((new File(MainUtil.FILE_PATH_CALL_HIDE_TAG)).exists()) {
					(new File(MainUtil.FILE_PATH_CALL_HIDE_TAG)).delete();
				}
				imgStyleSelected1.setVisibility(View.VISIBLE);
				imgStyleSelected2.setVisibility(View.GONE);
				break;
			case R.id.layout_sethidestyle_call2:
				if (!(new File(MainUtil.FILE_PATH_CALL_HIDE_TAG)).exists()) {
					(new File(MainUtil.FILE_PATH_CALL_HIDE_TAG)).mkdirs();
				}
				imgStyleSelected1.setVisibility(View.GONE);
				imgStyleSelected2.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_sethidestyle_return_call:
				finish();
				break;
			default:
				break;
			}
		} else {
			switch (v.getId()) {
			case R.id.layout_sethidestyle_sms1:
				if ((new File(MainUtil.FILE_PATH_SMS_HIDE_TAG)).exists()) {
					(new File(MainUtil.FILE_PATH_SMS_HIDE_TAG)).delete();
				}
				imgStyleSelected1.setVisibility(View.VISIBLE);
				imgStyleSelected2.setVisibility(View.GONE);
				break;
			case R.id.layout_sethidestyle_sms2:
				if (!(new File(MainUtil.FILE_PATH_SMS_HIDE_TAG)).exists()) {
					(new File(MainUtil.FILE_PATH_SMS_HIDE_TAG)).mkdirs();
				}
				imgStyleSelected1.setVisibility(View.GONE);
				imgStyleSelected2.setVisibility(View.VISIBLE);
				break;
			case R.id.btn_sethidestyle_return_sms:
				finish();
				break;
			default:
				break;
			}
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
