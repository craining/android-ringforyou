package com.zgy.ringforu.activity;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.view.MyToast;

public class ReceiveNumbersFromOthersActivity extends Activity implements OnClickListener {

	private TextView textShowContent;
	private Button btnSms;
	private Button btnCall;
	private Button btnImportant;
	private Vibrator vb = null;
	private String getNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		String action = intent.getAction();

		if (Intent.ACTION_SEND.equals(action)) {
			if (intent.hasExtra(Intent.EXTRA_TEXT)) {
				CharSequence text = getIntent().getCharSequenceExtra(Intent.EXTRA_TEXT);
				if (text != null) {
					getNumber = StringUtil.getNumbersFromString(text.toString());
					if (getNumber == null || getNumber.length() <= 0) {
						MyToast.makeText(ReceiveNumbersFromOthersActivity.this, R.string.receive_numbers_null, Toast.LENGTH_LONG, true).show();
						finish();
					}
				} else {
					MyToast.makeText(ReceiveNumbersFromOthersActivity.this, R.string.receive_numbers_null, Toast.LENGTH_LONG, true).show();
					finish();
				}
			} else {
				MyToast.makeText(ReceiveNumbersFromOthersActivity.this, R.string.receive_numbers_null, Toast.LENGTH_LONG, true).show();
				finish();
			}

		}
		
		MainUtil.mainInitData(RingForU.getInstance());
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.receivenumbers);

		btnSms = (Button) findViewById(R.id.btn_receivedata_sms);
		btnCall = (Button) findViewById(R.id.btn_receivedata_call);
		textShowContent = (TextView) findViewById(R.id.text_receivedata_content);
		btnImportant = (Button) findViewById(R.id.btn_receivedata_important);

		btnSms.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);
		btnCall.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);
		btnImportant.getBackground().setAlpha(MainCanstants.DLG_BTN_ALPHA);

		String content = getString(R.string.receivedata_content1) + getNumber;
		textShowContent.setText(content);
		btnSms.setOnClickListener(this);
		btnCall.setOnClickListener(this);
		btnImportant.setOnClickListener(this);

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

	}

	@Override
	public void onClick(View v) {

		PhoneUtil.doVibraterNormal(vb);

		switch (v.getId()) {
		case R.id.btn_receivedata_sms:
			doAddSms();
			break;

		case R.id.btn_receivedata_call:
			doAddCall();
			break;

		case R.id.btn_receivedata_important:
			doAddImportant();
			break;

		default:
			break;
		}

	}

	/**
	 * 添加拦截电话
	 */
	private void doAddCall() {
		Intent i = new Intent();
		i.setClass(ReceiveNumbersFromOthersActivity.this, AddByInputActivity.class);
		i.putExtra("tag", 1);
		i.putExtra("number", getNumber);
		startActivity(i);
		finish();
	}

	/**
	 * 添加拦截短信
	 */
	private void doAddSms() {
		Intent i = new Intent();
		i.setClass(ReceiveNumbersFromOthersActivity.this, AddByInputActivity.class);
		i.putExtra("tag", 2);
		i.putExtra("number", getNumber);
		startActivity(i);
		finish();
	}

	/**
	 * 添加重要联系人
	 */
	private void doAddImportant() {
		Intent i = new Intent();
		i.setClass(ReceiveNumbersFromOthersActivity.this, AddByInputActivity.class);
		i.putExtra("tag", 0);
		i.putExtra("number", getNumber);
		startActivity(i);
		finish();
	}
}
