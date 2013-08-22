package com.zgy.ringforu.activity;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.R;
import com.zgy.ringforu.R.id;
import com.zgy.ringforu.R.layout;
import com.zgy.ringforu.R.string;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.view.MyToast;

public class AddByInputActivity extends Activity implements OnClickListener {

	private static final String TAG = "AddByInputActivity";
	private EditText editName;
	private EditText editNumber;
	private Button btnOk;
	private Button btnBack;
	private TextView textShowLeft;
	private Vibrator vb = null;
	private int tag = 0;// 默认为重要联系人的添加 1:屏蔽电话 2:屏蔽短信
	
	private boolean addOne = false;//只添加一个，添加完成就退出

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.add_input);

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		editName = (EditText) findViewById(R.id.edit_name);
		editNumber = (EditText) findViewById(R.id.edit_num);
		btnOk = (Button) findViewById(R.id.btn_ok);
		textShowLeft = (TextView) findViewById(R.id.text_add_input);
		btnBack = (Button) findViewById(R.id.btn_add_input_return);
		btnBack.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		
		Bundle b = getIntent().getExtras();
		if (b != null) {
			tag = b.getInt("tag");
			if (b.containsKey("number")) {
				String number = b.getString("number");
				if (number != null) {
					editNumber.setText(number);
					addOne = true;
				}
			}
		}
		
		refresh();

	}

	private void refresh() {
		int left = MainUtil.getLeft(AddByInputActivity.this, tag);
		textShowLeft.setText(getString(R.string.left_top) + left + getString(R.string.left_bottom));
		if (left <= 0) {
			MyToast.makeText(AddByInputActivity.this, R.string.add_cannot_more, Toast.LENGTH_SHORT, true).show();
			editName.setEnabled(false);
			editNumber.setEnabled(false);
			btnOk.setEnabled(false);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_ok:

			String name = "";
			String number = "";
			if (TextUtils.isEmpty(editNumber.getText())) {
				MyToast.makeText(AddByInputActivity.this, R.string.add_null, Toast.LENGTH_SHORT, true).show();
			} else if (editNumber.getText().length() < 7) {
				MyToast.makeText(AddByInputActivity.this, R.string.add_error, Toast.LENGTH_SHORT, true).show();
			} else {

				if (TextUtils.isEmpty(editName.getText())) {
					name = getString(R.string.name_null);
				} else {
					name = editName.getText().toString();
				}
				number = editNumber.getText().toString();

				int result = MainUtil.insert(name, StringUtil.getRidofSpeciall(number), AddByInputActivity.this, tag);

				switch (result) {
				case -1:
					MyToast.makeText(AddByInputActivity.this, R.string.add_cannot_more, Toast.LENGTH_SHORT, true).show();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20, 100, 20 }, -1);
					}
					break;
				case 0:
					MyToast.makeText(AddByInputActivity.this, R.string.add_repeat, Toast.LENGTH_SHORT, true).show();
					editNumber.setText("");
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20, 100, 20 }, -1);
					}
					break;
				case 1:
					MyToast.makeText(AddByInputActivity.this, R.string.add_success, Toast.LENGTH_SHORT, false).show();
					editName.setText("");
					editNumber.setText("");
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
					refresh();
					break;

				default:
					break;
				}
				
				if(addOne) {
					finish();
				}
			}

			break;
		case R.id.btn_add_input_return:
			finish();
			break;
		default:
			break;
		}

	}
}
