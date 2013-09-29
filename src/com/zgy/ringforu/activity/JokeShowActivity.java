package com.zgy.ringforu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.NotificationUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.PushMessageUtils;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.util.ViewUtil;
import com.zgy.ringforu.view.FCMenu;
import com.zgy.ringforu.view.FCMenu.MenuItemOnClickListener;
import com.zgy.ringforu.view.FCMenuItem;
import com.zgy.ringforu.view.MyToast;

/**
 * 笑话展示
 * 
 * @Description:
 * @author: zhuanggy
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2013-9-24
 */

public class JokeShowActivity extends BaseGestureActivity implements OnClickListener {

	private Button btnBack;
	private Button btnShare;
	private TextView textInfo;
	private TextView textTitle;
	private ImageView imgTag;

	private FCMenu mTopMenu;
	private OnTopMenuItemClickedListener mTopMenuListener;
	private static final int ID_MENU_MSG = 1;

	private String mTitle;
	private String mContent;
	private String mTag;

	private static final String TAG_HAND = "hand";
	private static final String TAG_PANDA = "panda";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.joke_show_info);

		RingForUActivityManager.push(this);

		Bundle b = getIntent().getExtras();
		if (b == null) {
			RingForUActivityManager.pop(this);
			MyToast.makeText(this, R.string.joke_get_failed, Toast.LENGTH_SHORT, true).show();
			return;
		}

		mTitle = b.getString(NotificationUtil.INTENT_ACTION_KEY_JOKE_TITLE);
		mContent = b.getString(NotificationUtil.INTENT_ACTION_KEY_JOKE_CONTENT);
		mTag = b.getString(NotificationUtil.INTENT_ACTION_KEY_JOKE_TAG);

		btnBack = (Button) findViewById(R.id.btn_joke_return);
		btnShare = (Button) findViewById(R.id.btn_joke_share);
		textInfo = (TextView) findViewById(R.id.text_joke_info);
		textTitle = (TextView) findViewById(R.id.text_joke_title);
		imgTag = (ImageView) findViewById(R.id.img_joke_show);

		if (!StringUtil.isNull(mTitle)) {
			textTitle.setText(mTitle);
		}

		if (StringUtil.isNull(mContent)) {
			RingForUActivityManager.pop(this);
			MyToast.makeText(this, R.string.joke_get_failed, Toast.LENGTH_SHORT, true).show();
			return;
		}
		textInfo.setText(mContent.replaceAll(PushMessageUtils.MESSAGE_TAG_BREAKLINE, "\r\n"));

		int srcId = getImageSrcIdByTag(mTag);
		if (srcId != -1) {
			imgTag.setImageResource(srcId);
		}

		initTopMenu();

		btnBack.setOnClickListener(this);
		btnShare.setOnClickListener(this);

	}

	private int getImageSrcIdByTag(String tag) {

		if (!StringUtil.isNull(tag)) {
			if (tag.equals(TAG_HAND)) {
				return R.drawable.ic_joke_hand;
			} else if (tag.equals(TAG_PANDA)) {
				return R.drawable.ic_joke_panda;
			}
		}

		return -1;
	}

	private void initTopMenu() {
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		int[] widthHeight = new int[2];
		widthHeight[0] = dMetrics.widthPixels / 4;
		// widthHeight[1] = dMetrics.heightPixels / 2;
		widthHeight[1] = ViewGroup.LayoutParams.FILL_PARENT;

		mTopMenu = new FCMenu(JokeShowActivity.this, widthHeight, findViewById(R.id.view_joke_anchor));
		mTopMenuListener = new OnTopMenuItemClickedListener();
		mTopMenu.setMenuItemOnclickListener(mTopMenuListener);

		List<FCMenuItem> items = new ArrayList<FCMenuItem>();
		items.add(new FCMenuItem(ID_MENU_MSG, -1, R.string.joke_share_msg));
		mTopMenu.setDatas(items, -1, false);
	}

	private class OnTopMenuItemClickedListener implements MenuItemOnClickListener {

		@Override
		public void onItemClicked(FCMenuItem item) {
			PhoneUtil.doVibraterNormal(JokeShowActivity.super.mVb);
			switch (item.getOpID()) {
			case ID_MENU_MSG:
				// 短信分享

				StringBuilder sb = new StringBuilder();

				if (!StringUtil.isNull(mTitle)) {
					sb.append("【").append(mTitle).append("】");
				}

				sb.append(mContent);
				Intent it = new Intent(Intent.ACTION_VIEW);
				it.putExtra("sms_body", sb.toString());
				it.setType("vnd.android-dir/mms-sms");
				startActivity(it);

				break;

			default:
				break;
			}

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(super.mVb);
		switch (v.getId()) {
		case R.id.btn_joke_return:
			RingForUActivityManager.pop(JokeShowActivity.this);
			break;
		case R.id.btn_joke_share:
			mTopMenu.showMenu(-1);
			break;
		default:
			break;
		}

	}

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
		ViewUtil.onButtonPressedBack(btnBack);
		PhoneUtil.doVibraterNormal(super.mVb);
		if (mTopMenu.isShowing()) {
			mTopMenu.closeMenu();
		}
		RingForUActivityManager.pop(JokeShowActivity.this);
	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
		if (!mTopMenu.isShowing()) {
			ViewUtil.onButtonPressedBlue(btnShare);
			PhoneUtil.doVibraterNormal(super.mVb);
			mTopMenu.showMenu(-1);
		}
	}

}
