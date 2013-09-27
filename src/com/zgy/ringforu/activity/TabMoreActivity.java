package com.zgy.ringforu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.interfaces.OnGestureChangedListener;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.NetWorkUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.ViewUtil;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class TabMoreActivity extends Activity implements OnClickListener {

	private static final String TAG = "TabCallActivity";

	private CheckBox checkV, checkGesture;
	private RelativeLayout layoutFeedback;
	private RelativeLayout layoutHelp;
	private RelativeLayout layoutTools;
	private RelativeLayout layoutClear;
	private RelativeLayout layoutV;
	private RelativeLayout layoutGesture;
	private RelativeLayout layoutRedTools;
	private Button btnExit;

	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_activity_more);

		// RingForUActivityManager.push(this);

		layoutFeedback = (RelativeLayout) findViewById(R.id.layout_more_feedback);
		layoutHelp = (RelativeLayout) findViewById(R.id.layout_more_help);
		layoutTools = (RelativeLayout) findViewById(R.id.layout_more_tools);
		layoutGesture = (RelativeLayout) findViewById(R.id.layout_more_gesture);
		layoutClear = (RelativeLayout) findViewById(R.id.layout_more_clear);
		layoutV = (RelativeLayout) findViewById(R.id.layout_more_v);
		checkV = (CheckBox) findViewById(R.id.check_more_checkv);
		checkGesture = (CheckBox) findViewById(R.id.check_more_gesture);
		layoutRedTools = (RelativeLayout) findViewById(R.id.layout_more_tool_red);
		btnExit = (Button) findViewById(R.id.btn_more_exit);

		layoutFeedback.setOnClickListener(this);
		layoutHelp.setOnClickListener(this);
		layoutTools.setOnClickListener(this);
		layoutClear.setOnClickListener(this);
		layoutGesture.setOnClickListener(this);
		btnExit.setOnClickListener(this);

		// 震动的开关需单独处理
		layoutV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 震动的开启关闭
				if (!RingForU.getInstance().isbIsVerbOn()) {
					((MainActivityGroup) getParent()).mVb.vibrate(MainCanstants.VIBRATE_STREGTH_NORMAL);
					RingForU.getInstance().setbIsVerbOn(true);
					MainConfig.getInstance().setVibrateOnOff(true);
				} else {
					RingForU.getInstance().setbIsVerbOn(false);
					MainConfig.getInstance().setVibrateOnOff(false);
				}

				refreshViews();

			}
		});
	}

	private void refreshViews() {
		// 刷新震动开关的显示
		checkV.setChecked(RingForU.getInstance().isbIsVerbOn());
		checkGesture.setChecked(RingForU.getInstance().isbIsGestureOn());
		// 刷新小红点
		if (!MainConfig.getInstance().isRedToolsShown()) {
			layoutRedTools.setVisibility(View.VISIBLE);
		} else {
			layoutRedTools.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		RingForU.getInstance().setSelsectedTabId(MainCanstants.TYPE_MORE);
		((MainActivityGroup) getParent()).setOnGestureChangedListener(mGuesterListener);
		refreshViews();
		LogRingForu.e(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);

		switch (v.getId()) {
		case R.id.btn_set_return:
			RingForUActivityManager.pop(this);
			break;
		case R.id.layout_more_feedback:
			if (NetWorkUtil.isConnectInternet(TabMoreActivity.this)) {
				startActivity(new Intent(TabMoreActivity.this, FeedBackActivity.class));
			} else {
				NetWorkUtil.setNetConnection(TabMoreActivity.this, ((MainActivityGroup) getParent()).mVb);
			}
			break;
		case R.id.layout_more_help:
			startActivity(new Intent(TabMoreActivity.this, AboutActivity.class));
			break;
		case R.id.img_add_calm_important:
			startActivity(new Intent(TabMoreActivity.this, AddSlientPerActivity.class));
			break;
		case R.id.layout_more_tools:
			startActivity(new Intent(TabMoreActivity.this, ToolsListActivity.class));
			break;

		case R.id.layout_more_clear:
			// 清空缓存
			clearData();
			break;

		case R.id.layout_more_gesture:
			// 手势开关
			if (RingForU.getInstance().isbIsGestureOn()) {
				RingForU.getInstance().setbIsGestureOn(false);
				MainConfig.getInstance().setGestureOnOff(false);
			} else {
				RingForU.getInstance().setbIsGestureOn(true);
				MainConfig.getInstance().setGestureOnOff(true);
			}

			refreshViews();
			break;
		case R.id.btn_more_exit:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 清空缓存
	 */
	private void clearData() {

		if (PhoneUtil.existSDcard()) {

			File file = new File(MainCanstants.FILE_IN_SDCARD);

			String[] files = file.list();
			if (files == null || files.length <= 0) {
				MyToast.makeText(TabMoreActivity.this, R.string.clear_data_null, Toast.LENGTH_LONG, true).show();
				return;
			}

			// 清空重要联系人
			MyDialog.Builder builder = new MyDialog.Builder(TabMoreActivity.this);
			builder.setTitle(R.string.str_tip).setMessage(R.string.clear_alert).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
					// 删除文件
					if (PhoneUtil.existSDcard()) {
						if (FileUtil.clearData()) {
							MyToast.makeText(TabMoreActivity.this, R.string.clear_data_over, Toast.LENGTH_LONG, false).show();
						}
					} else {
						MyToast.makeText(TabMoreActivity.this, R.string.no_sdcard, Toast.LENGTH_LONG, true).show();
					}
				}
			}).setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
				}
			}).create().show();
		} else {
			MyToast.makeText(TabMoreActivity.this, R.string.set_backup_nosdcard, Toast.LENGTH_SHORT, true).show();
		}
	}

	/**
	 * 手势监听，从ActivityGroup传递过来的
	 */
	private OnGestureChangedListener mGuesterListener = new OnGestureChangedListener() {

		@Override
		public void onSlideToRight() {
			ViewUtil.onButtonPressedBlue(btnExit);
			PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
			finish();
		}

		@Override
		public void onSlideToLeft() {
		}
	};
}
