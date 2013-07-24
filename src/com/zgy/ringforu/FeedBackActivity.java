package com.zgy.ringforu;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import android.app.Activity;
import android.app.Service;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zgy.ringforu.adapter.AccountAutoCompleteAdapter;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.NetWorkUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.SendEmailUtil;
import com.zgy.ringforu.view.MyToast;

public class FeedBackActivity extends Activity implements OnClickListener {

	private static final String TAG = "FeedBackActivity";
	private Button btnBack;
	private Button btnSubmit;
	private EditText editContent;
	private AutoCompleteTextView editEmail;
	private Vibrator vb = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.feedback);

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		btnBack = (Button) findViewById(R.id.btn_feedback_return);
		btnSubmit = (Button) findViewById(R.id.btn_feedback_ok);
		editContent = (EditText) findViewById(R.id.edit_feedback_content);
		editEmail = (AutoCompleteTextView) findViewById(R.id.edit_feedback_email);
		btnSubmit.setEnabled(false);
		btnBack.setOnClickListener(this);
		btnSubmit.setOnClickListener(this);

		
		AccountAutoCompleteAdapter adapter = new AccountAutoCompleteAdapter(FeedBackActivity.this, null);
		editEmail.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
		editEmail.setAdapter(adapter);
		editEmail.setThreshold(0);
		
		editEmail.setDropDownBackgroundResource(R.drawable.edit_bg_normal_nopadding);
		
		editContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(editContent.getText())) {
					btnSubmit.setEnabled(true);
				} else {
					btnSubmit.setEnabled(false);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (PhoneUtil.bIsVerbOn) {
			vb.vibrate(new long[] { 0, 20 }, -1);
		}
		switch (v.getId()) {
		case R.id.btn_feedback_return:
			finish();
			break;

		case R.id.btn_feedback_ok:

			if (NetWorkUtil.isConnectInternet(FeedBackActivity.this)) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						SendEmailUtil send = new SendEmailUtil();
						try {
							StringBuilder sb = new StringBuilder();
							sb.append(editContent.getText().toString()).append(getAddr()).append(MainUtil.FEEDBACK_VERSION).append(PhoneUtil.getHandsetInfo(FeedBackActivity.this));
							send.sendMail(MainUtil.FEEDBACK_TITLE, sb.toString(), MainUtil.FEEDBACK_EMAIL_TO);
						} catch (AddressException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							MyToast.makeText(FeedBackActivity.this, R.string.feedback_contentfail, Toast.LENGTH_SHORT, true).show();
						} catch (MessagingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							MyToast.makeText(FeedBackActivity.this, R.string.feedback_contentfail, Toast.LENGTH_SHORT, true).show();
						}
					}
				}).start();
				MyToast.makeText(FeedBackActivity.this, R.string.feedback_subminting, Toast.LENGTH_SHORT, false).show();
				finish();
			} else {
				NetWorkUtil.setNetConnection(FeedBackActivity.this, vb);
			}

			break;

		default:
			break;
		}

	}

	/**
	 * 获得联系方式
	 * 
	 * @return
	 */
	private String getAddr() {
		if (TextUtils.isEmpty(editEmail.getText())) {
			return "\r\nno addr";
		} else {
			return "\r\naddr: " + editEmail.getText().toString();
		}
	}
}
