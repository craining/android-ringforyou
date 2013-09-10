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

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.BusyModeUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.view.MyToast;
import com.zgy.ringforu.view.WordsFlowView;

public class ToolsBusyModeActivity extends BaseGestureActivity implements OnClickListener {

	private static final String TAG = "BusyModeActivity";

	private Button btnBack;
	private Button btnOk;
	private Button btnClose;
	private EditText editMsgContent;
	private WordsFlowView wordsFlow;

	public static String[] busyModesTitle;
	public static String[] busyModeInfo;

	private boolean boolFromNotification = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.busymode);
		
		RingForUActivityManager.push(this);
		
		btnBack = (Button) findViewById(R.id.btn_busymodeset_back);
		btnClose = (Button) findViewById(R.id.btn_busymode_title_close);
		btnOk = (Button) findViewById(R.id.btn_busymodeset_ok);
		editMsgContent = (EditText) findViewById(R.id.edit_busymode_msgcontent);
		wordsFlow = (WordsFlowView) findViewById(R.id.busymode_flow);

		// 显示“关闭”和“开启”
		// // 否则显示“返回”和“开启”
		// Bundle b = getIntent().getExtras();
		// if (b != null && b.containsKey("fromnotifybar") && b.getBoolean("fromnotifybar")) {
		// // 是从通知栏跳转过来的
		// boolFromNotification = true;
		//
		// }

		if (BusyModeUtil.isBusyModeOn()) {
			btnClose.setVisibility(View.VISIBLE);
		} else {
			btnClose.setVisibility(View.GONE);
		}

		wordsFlow.setDuration(500l);
		wordsFlow.setOnItemClickListener(this);
		btnBack.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		btnClose.setOnClickListener(this);

		busyModesTitle = getResources().getStringArray(R.array.busymodes_title);
		busyModeInfo = getResources().getStringArray(R.array.busymodes_info);

		// 添加
		// feedKeywordsFlow(wordsFlow, busyModesTitle);
		wordsFlow.feedKeywordsAll(busyModesTitle);
		// if (RingForU.DEBUG)
		// LogRingForu.e(TAG, "test test point 1");
		setSelectedMsgContent(BusyModeUtil.getBusyModeMsgContent(ToolsBusyModeActivity.this), true);
	}

	// /**
	// * 设置要显示的词语
	// *
	// * @param keywordsFlow
	// * @param arr
	// */
	// private static void feedKeywordsFlow(WordsFlowView keywordsFlow, String[] arr) {
	//
	// for (String words : arr) {
	// keywordsFlow.feedKeyword(words);
	// }
	// // Random random = new Random();
	// // for (int i = 0; i < WordsFlowView.MAX; i++) {
	// // int ran = random.nextInt(arr.length);
	// // String tmp = arr[ran];
	// // keywordsFlow.feedKeyword(tmp);
	// // }
	// }
	
	private void setOk() {
		// 设置自动回复的开关，以及短信回复的文字
		if (TextUtils.isEmpty(editMsgContent.getText())) {
			BusyModeUtil.setBusyModeOnOff(ToolsBusyModeActivity.this, true, null);
		} else {
			BusyModeUtil.setBusyModeOnOff(ToolsBusyModeActivity.this, true, editMsgContent.getText().toString());
		}

		MyToast.makeText(ToolsBusyModeActivity.this, R.string.busymode_tip_open_toast, MyToast.LENGTH_LONG, false).show();
		RingForUActivityManager.pop(this);
	}

	@Override
	public void onClick(View v) {

		PhoneUtil.doVibraterNormal(ToolsBusyModeActivity.super.mVb);
		if (v instanceof Button) {
			switch (v.getId()) {
			case R.id.btn_busymodeset_back:
				RingForUActivityManager.pop(this);
				break;
			case R.id.btn_busymode_title_close:
				BusyModeUtil.setBusyModeOnOff(ToolsBusyModeActivity.this, false, null);
				MyToast.makeText(ToolsBusyModeActivity.this, R.string.busymode_tip_close_toast, MyToast.LENGTH_LONG, false).show();
				RingForUActivityManager.pop(this);
				break;

			case R.id.btn_busymodeset_ok:
				setOk();
				break;

			default:
				break;
			}
		} else if (v instanceof TextView) {
			// 词语被点击
			setSelectedMsgTitle(((TextView) v).getText().toString(), false);
		}

	}

	/**
	 * 根据被点击的标题显示出短信内容
	 * 
	 * @param title
	 */
	private void setSelectedMsgTitle(String title, boolean anim) {

		editMsgContent.setText(BusyModeUtil.getMessageContentFromTitle(busyModesTitle, busyModeInfo, title));

		if (anim) {
			wordsFlow.go2Show(WordsFlowView.ANIMATION_IN);
		}
	}

	/**
	 * 显示出短信内容
	 * 
	 * @param title
	 */
	private void setSelectedMsgContent(String content, boolean anim) {
		editMsgContent.setText(content);
		if (anim) {
			wordsFlow.go2Show(WordsFlowView.ANIMATION_IN);
		}
	}

	@Override
	protected void onDestroy() {// 设置状态栏图标
		BusyModeUtil.checkState(ToolsBusyModeActivity.this);
		super.onDestroy();
	}

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
		PhoneUtil.doVibraterNormal(super.mVb);
		RingForUActivityManager.pop(this);
	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
		PhoneUtil.doVibraterNormal(super.mVb);
		setOk();
	}
	
	

}
