package com.zgy.ringforu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.interfaces.OnGestureChangedListener;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.NotificationUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.PushMessageUtils;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.StringUtil;

public class MainActivityGroup extends BaseGestureActivityGroup implements OnClickListener {

	private LinearLayout bodyView;
	private LinearLayout tabImportant;
	private LinearLayout tabCall;
	private LinearLayout tabSms;
	private LinearLayout tabMore;
	private ImageView imgImportant;
	private ImageView imgCall;
	private ImageView imgSms;
	private ImageView imgMore;
	private int flag = 0; // 通过标记跳转不同的页面，显示不同的菜单项
	public int[] mWidthHeightTopMenu;

	private OnGestureChangedListener mListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_group);

		RingForUActivityManager.push(this);
		
		MainUtil.checkNewVersion(MainActivityGroup.this);

		PushMessageUtils.startPushWork(MainActivityGroup.this);
		
		if (MainConfig.getInstance().getPushNewVersionCode() == MainUtil.getAppVersionCode(MainActivityGroup.this)) {
			// 当前版本为新版本，若更新说明不为空，则提示更新说明，待显示后，清空更新说明
			if (!StringUtil.isNull(MainConfig.getInstance().getPushNewVersionInfo())) {
				startActivity(new Intent(MainActivityGroup.this, NewVersionInfoActivity.class));
			}
		}
		initMainView();
		showView(RingForU.getInstance().getSelsectedTabId());

		tabImportant.setOnClickListener(this);
		tabCall.setOnClickListener(this);
		tabSms.setOnClickListener(this);
		tabMore.setOnClickListener(this);

		// test
		// Intent i = new Intent(MainActivityGroup.this,
		// JokeShowActivity.class);
		// i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_TITLE, "hah");
		// i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_CONTENT,
		// "哈哈");
		// i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_TAG, "hand");
		// startActivity(i);

	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 如果要统计Push引起的用户使用应用情况，请实现本方法，且加上这一个语句
		setIntent(intent);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	/*
	 * 初始化主界面底部的功能菜单
	 */
	public void initMainView() {
		bodyView = (LinearLayout) findViewById(R.id.tab_layout_topbody);
		tabImportant = (LinearLayout) findViewById(R.id.tab_important);
		tabCall = (LinearLayout) findViewById(R.id.tab_call);
		tabSms = (LinearLayout) findViewById(R.id.tab_sms);
		tabMore = (LinearLayout) findViewById(R.id.tab_more);
		imgImportant = (ImageView) findViewById(R.id.img_tab_important);
		imgCall = (ImageView) findViewById(R.id.img_tab_call);
		imgSms = (ImageView) findViewById(R.id.img_tab_sms);
		imgMore = (ImageView) findViewById(R.id.img_tab_more);
	}

	// 在主界面中显示其他界面
	public void showView(int flag) {
		switch (flag) {
		case MainCanstants.TYPE_IMPORTANT:
			showImportant();
			break;
		case MainCanstants.TYPE_INTECEPT_CALL:
			showCall();
			break;
		case MainCanstants.TYPE_INTECEPT_SMS:
			showSms();
			break;
		case MainCanstants.TYPE_MORE:
			showMore();
			break;
		default:
			break;
		}
	}

	public void showImportant() {
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("important", new Intent(MainActivityGroup.this, TabImportantActivity.class)).getDecorView());
		tabImportant.setBackgroundResource(R.drawable.tab_selected);
		// tabImportant.getBackground().setAlpha(255);
		tabCall.setBackgroundResource(R.drawable.bg_translation);
		tabSms.setBackgroundResource(R.drawable.bg_translation);
		tabMore.setBackgroundResource(R.drawable.bg_translation);
		imgImportant.setImageResource(R.drawable.ic_tab_important_pressed);
		imgCall.setImageResource(R.drawable.ic_tab_call_normal);
		imgSms.setImageResource(R.drawable.ic_tab_sms_normal);
		imgMore.setImageResource(R.drawable.ic_tab_more_normal);
	}

	public void showCall() {
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("call", new Intent(MainActivityGroup.this, TabCallActivity.class)).getDecorView());
		tabImportant.setBackgroundResource(R.drawable.bg_translation);
		tabCall.setBackgroundResource(R.drawable.tab_selected);
		tabSms.setBackgroundResource(R.drawable.bg_translation);
		tabMore.setBackgroundResource(R.drawable.bg_translation);
		imgImportant.setImageResource(R.drawable.ic_tab_important_normal);
		imgCall.setImageResource(R.drawable.ic_tab_call_pressed);
		imgSms.setImageResource(R.drawable.ic_tab_sms_normal);
		imgMore.setImageResource(R.drawable.ic_tab_more_normal);
	}

	public void showSms() {
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("sms", new Intent(MainActivityGroup.this, TabSmsActivity.class)).getDecorView());
		tabImportant.setBackgroundResource(R.drawable.bg_translation);
		tabMore.setBackgroundResource(R.drawable.bg_translation);
		tabCall.setBackgroundResource(R.drawable.bg_translation);
		tabSms.setBackgroundResource(R.drawable.tab_selected);
		imgImportant.setImageResource(R.drawable.ic_tab_important_normal);
		imgCall.setImageResource(R.drawable.ic_tab_call_normal);
		imgMore.setImageResource(R.drawable.ic_tab_more_normal);
		imgSms.setImageResource(R.drawable.ic_tab_sms_pressed);
	}

	public void showMore() {
		bodyView.removeAllViews();
		bodyView.addView(getLocalActivityManager().startActivity("more", new Intent(MainActivityGroup.this, TabMoreActivity.class)).getDecorView());
		tabImportant.setBackgroundResource(R.drawable.bg_translation);
		tabCall.setBackgroundResource(R.drawable.bg_translation);
		tabSms.setBackgroundResource(R.drawable.bg_translation);
		tabMore.setBackgroundResource(R.drawable.tab_selected);
		imgImportant.setImageResource(R.drawable.ic_tab_important_normal);
		imgCall.setImageResource(R.drawable.ic_tab_call_normal);
		imgSms.setImageResource(R.drawable.ic_tab_sms_normal);
		imgMore.setImageResource(R.drawable.ic_tab_more_pressed);
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(MainActivityGroup.super.mVb);
		switch (v.getId()) {
		case R.id.tab_important:
			flag = 0;
			showView(flag);
			break;
		case R.id.tab_call:
			flag = 1;
			showView(flag);
			break;
		case R.id.tab_sms:
			flag = 2;
			showView(flag);
			break;
		case R.id.tab_more:
			flag = 3;
			showView(flag);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	public void setOnGestureChangedListener(OnGestureChangedListener listener) {
		this.mListener = listener;
	}

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
		if (mListener != null) {
			mListener.onSlideToRight();
		}

	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
		if (mListener != null) {
			mListener.onSlideToLeft();
		}
	}

}
