package com.zgy.ringforu.activity;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.TimeUtil;
import com.zgy.ringforu.view.MyToast;
import com.zgy.ringforu.view.NumericWheelAdapter;
import com.zgy.ringforu.view.OnWheelChangedListener;
import com.zgy.ringforu.view.OnWheelScrollListener;
import com.zgy.ringforu.view.WheelView;

public class AddSlientPerActivity extends Activity implements OnClickListener {

	private static final String TAG = "AddSlientPerActivity";
	private WheelView hoursPickStart;
	private WheelView hoursPickEnd;
	private WheelView minutesPickStart;
	private WheelView minutesPickEnd;

	private Button btnBack;
	private Button btnAdd;
	private Vibrator vb = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addslientper);

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		hoursPickStart = (WheelView) findViewById(R.id.wheel_alarmaddhour_start);
		hoursPickEnd = (WheelView) findViewById(R.id.wheel_alarmaddhour_end);
		minutesPickStart = (WheelView) findViewById(R.id.wheel_alarmaddmins_start);
		minutesPickEnd = (WheelView) findViewById(R.id.wheel_alarmaddmins_end);
		btnBack = (Button) findViewById(R.id.btn_add_slient_return);
		btnAdd = (Button) findViewById(R.id.btn_add_slient_add);
		btnAdd.setOnClickListener(this);
		btnBack.setOnClickListener(this);

		hoursPickStart.setAdapter(new NumericWheelAdapter(0, 23));
		hoursPickStart.setCyclic(false);
		hoursPickStart.setVisibleItems(5);

		hoursPickEnd.setAdapter(new NumericWheelAdapter(0, 23));
		hoursPickEnd.setCyclic(false);
		hoursPickEnd.setVisibleItems(5);

		minutesPickStart.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		minutesPickStart.setCyclic(true);
		minutesPickStart.setVisibleItems(5);

		minutesPickEnd.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		minutesPickEnd.setCyclic(true);
		minutesPickEnd.setVisibleItems(5);

		hoursPickStart.setCurrentItem(22);
		addChangingListener(hoursPickStart);
		addScrollingListener(hoursPickStart);

		minutesPickStart.setCurrentItem(00);
		addChangingListener(minutesPickStart);
		addScrollingListener(minutesPickStart);

		hoursPickEnd.setCurrentItem(7);
		addChangingListener(hoursPickEnd);
		addScrollingListener(hoursPickEnd);

		minutesPickEnd.setCurrentItem(00);
		addChangingListener(minutesPickEnd);
		addScrollingListener(minutesPickEnd);

		// minutesPick.getCurrentItem();

	}

	private void addChangingListener(final WheelView wheel) {
		wheel.addChangingListener(new OnWheelChangedListener() {

			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// wheel.setLabel(newValue != 1 ? label + "s" : label);
			}
		});
	}

	private void addScrollingListener(final WheelView wheel) {
		wheel.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(vb);
		switch (v.getId()) {
		case R.id.btn_add_slient_return:
			finish();
			break;
		case R.id.btn_add_slient_add:
			// 添加静音时段
			// -1：重复； 0：被包含； 1：冲突； 2：成功; 3:包含
			if ((hoursPickStart.getCurrentItem() == hoursPickEnd.getCurrentItem()) && (minutesPickStart.getCurrentItem() == minutesPickEnd.getCurrentItem())) {
				MyToast.makeText(AddSlientPerActivity.this, R.string.addslient_fail_same, Toast.LENGTH_SHORT, true).show();
			} else {
				switch (MainUtil.insertSlientP(AddSlientPerActivity.this, TimeUtil.getTimeformatString(hoursPickStart.getCurrentItem()) + ":" + TimeUtil.getTimeformatString(minutesPickStart.getCurrentItem()) + "-" + TimeUtil.getTimeformatString(hoursPickEnd.getCurrentItem()) + ":" + TimeUtil.getTimeformatString(minutesPickEnd.getCurrentItem()))) {
				case 2:
					// 成功
					MyToast.makeText(AddSlientPerActivity.this, R.string.add_success, Toast.LENGTH_SHORT, false).show();
					finish();
					break;
				case -1:
					// 重复
					MyToast.makeText(AddSlientPerActivity.this, R.string.addslient_fail_repeat, Toast.LENGTH_SHORT, true).show();
					break;
				case 0:
					// 被包含
					MyToast.makeText(AddSlientPerActivity.this, R.string.addslient_fail_contained, Toast.LENGTH_SHORT, true).show();
					break;
				case 1:
					// 冲突
					MyToast.makeText(AddSlientPerActivity.this, R.string.addslient_fail_conflict, Toast.LENGTH_SHORT, true).show();
					break;
				case 3:
					// 包含
					MyToast.makeText(AddSlientPerActivity.this, R.string.addslient_contain, Toast.LENGTH_SHORT, false).show();
					finish();
					break;

				default:
					break;
				}
			}

			break;
		default:
			break;
		}

	}
}
