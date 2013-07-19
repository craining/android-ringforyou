package com.zgy.ringforu;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class TabMoreActivity extends Activity implements OnClickListener {

	private static final String TAG = "TabCallActivity";

	private ImageView imgV;
	private RelativeLayout layoutFeedback;
	private RelativeLayout layoutHelp;
	private RelativeLayout layoutTools;
	private RelativeLayout layoutClear;

	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

	private Vibrator vb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_activity_more);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		layoutFeedback = (RelativeLayout) findViewById(R.id.layout_more_feedback);
		layoutHelp = (RelativeLayout) findViewById(R.id.layout_more_help);
		layoutTools = (RelativeLayout) findViewById(R.id.layout_more_tools);
		layoutClear = (RelativeLayout) findViewById(R.id.layout_more_clear);
		imgV = (ImageView) findViewById(R.id.img_more_checkv);

		layoutFeedback.setOnClickListener(this);
		layoutHelp.setOnClickListener(this);
		layoutTools.setOnClickListener(this);
		layoutClear.setOnClickListener(this);

		// 震动的开关需单独处理
		imgV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 震动的开启关闭
				File tag = new File(PhoneUtil.FILE_PATH_VERB_TAG);
				if (!PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
					if (tag.exists()) {
						tag.delete();
					}
				} else {
					if (!tag.exists()) {
						tag.mkdir();
					}
				}

				PhoneUtil.bIsVerbOn = !PhoneUtil.bIsVerbOn;
				refreshViews();

			}
		});
	}

	private void refreshViews() {
		// 刷新震动开关的显示
		if (PhoneUtil.bIsVerbOn) {
			imgV.setImageResource(R.drawable.ic_on);
		} else {
			imgV.setImageResource(R.drawable.ic_off);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Log.e(TAG, "onResume");
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		if (PhoneUtil.bIsVerbOn) {
			vb.vibrate(new long[] { 0, 20 }, -1);
		}

		switch (v.getId()) {
		case R.id.btn_set_return:
			finish();
			break;
		case R.id.layout_more_feedback:
			if (PhoneUtil.isConnectInternet(TabMoreActivity.this)) {
				startActivity(new Intent(TabMoreActivity.this, FeedBackActivity.class));
			} else {
				PhoneUtil.setNetConnection(TabMoreActivity.this, vb);
			}
			break;
		case R.id.layout_more_help:
			startActivity(new Intent(TabMoreActivity.this, AboutActivity.class));
			break;
		case R.id.img_add_calm_important:
			startActivity(new Intent(TabMoreActivity.this, AddSlientPerActivity.class));
			break;
		case R.id.layout_more_tools:
			startActivity(new Intent(TabMoreActivity.this, RToolsActivity.class));
			break;

		case R.id.layout_more_clear:
			// 清空缓存
			clearData();
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
			// 清空重要联系人
			MyDialog.Builder builder = new MyDialog.Builder(TabMoreActivity.this);
			builder.setTitle(R.string.str_tip).setMessage(R.string.clear_alert).setPositiveButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
				}
			}).setNegativeButton(R.string.str_ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
					// 删除文件
					MainUtil.clearData();
					MyToast.makeText(TabMoreActivity.this, R.string.clear_data_over, Toast.LENGTH_LONG, false).show();
				}
			}).create().show();
		} else {
			MyToast.makeText(TabMoreActivity.this, R.string.set_backup_nosdcard, Toast.LENGTH_SHORT, true).show();
		}
	}
}
