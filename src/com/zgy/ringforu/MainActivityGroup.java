package com.zgy.ringforu;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zgy.ringforu.tools.disablegprs.DisableGprsService;
import com.zgy.ringforu.tools.disablegprs.DisableGprsUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.ReceiveEmailUtil;

public class MainActivityGroup extends ActivityGroup {

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

	// private String parameter = Constant.BUTTON_HOME;// 初始化加载

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_group);

		MainUtil.mainInitData(MainActivityGroup.this);// 初始化数据

		initMainView();
		// 主界面开始接收参数
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			flag = bundle.getInt("flag");
		}
		// 默认显示
		showView(flag);
		tabImportant.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 0;
				showView(flag);
			}
		});
		tabCall.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 1;
				showView(flag);
			}
		});
		tabSms.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 2;
				showView(flag);
			}
		});
		tabMore.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = 3;
				showView(flag);
			}
		});

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// ReceiveEmailUtil.getAllEmails();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		//
		// }
		// }).start();

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
		case MainUtil.TYPE_IMPORTANT:
			showImportant();
			break;
		case MainUtil.TYPE_CALL:
			showCall();
			break;
		case MainUtil.TYPE_SMS:
			showSms();
			break;
		case MainUtil.TYPE_MORE:
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
}
