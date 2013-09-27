package com.zgy.ringforu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.adapter.PickerAdapter;
import com.zgy.ringforu.bean.AppInfo;
import com.zgy.ringforu.util.PhoneUtil;

/**
 * @Description:提供类似另存为对话框的功能
 * @author:
 * @see:
 * @since:
 * @copyright © 35.com
 * @Date:2012-11-2
 */
public class PickerApplicationActivity extends BaseGestureActivity implements OnClickListener {

	private static final String TAG = "DirectoryPicker";

	private ListView mListView;
	private Button mBtnOk;
	private Button mBtnCancel;

	private ArrayList<AppInfo> mAppsInfoList = new ArrayList<AppInfo>();

	// animation sets
	AnimationSet mCascadeAnimSet = new AnimationSet(true);
	AnimationSet mReverseOrderAnimSet = new AnimationSet(true);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pick_application);
		initViews();
		createAnimations();
		updateList();

	}

	@Override
	public void finalize() {
		mAppsInfoList.clear();
		mAppsInfoList = null;

		mCascadeAnimSet = null;
		mReverseOrderAnimSet = null;
	}

	private void initViews() {
		mListView = (ListView) findViewById(R.id.list_pick_app);
		mBtnOk = (Button) findViewById(R.id.btn_pick_app_set);
		mBtnCancel = (Button) findViewById(R.id.btn_pick_app_exit);

		mBtnOk.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);

		mListView.setAdapter(new PickerAdapter(this, mAppsInfoList));
		mListView.setOnItemClickListener(mLsnListItemClick);
	}

	private void updateList() {
		queryAppInfo();

		PickerAdapter a = (PickerAdapter) mListView.getAdapter();
		if (a != null) {
			a.notifyDataSetChanged();
			a = null;
		}
		addAnimations();
	}

	public void queryAppInfo() {
		PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// 通过查询，获得所有ResolveInfo对象.
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
		// 调用系统排序 ， 根据name排序
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
		if (mAppsInfoList != null) {
			mAppsInfoList.clear();
			String selectedApps = RingForU.getInstance().getPackageNameHideWaterMark();
			for (ResolveInfo reInfo : resolveInfos) {
				String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
				String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
				String appLabel = (String) reInfo.loadLabel(pm); // 获得应用程序的Label
				Drawable icon = reInfo.loadIcon(pm); // 获得应用程序图标
				// 为应用程序的启动Activity 准备Intent
				Intent launchIntent = new Intent();
				launchIntent.setComponent(new ComponentName(pkgName, activityName));
				// 创建一个AppInfo对象，并赋值
				AppInfo appInfo = new AppInfo();
				appInfo.setName(appLabel);
				appInfo.setPackageName(pkgName);
				appInfo.setAppIcon(icon);
				// 判断是否已选
				if (selectedApps.contains(pkgName)) {
					appInfo.setSelected(true);
				} else {
					appInfo.setSelected(false);
				}

				mAppsInfoList.add(appInfo); // 添加至列表中
			}
		}
	}

	private void createAnimations() {
		createCascadeAnimation();
		createReverseOrderAnimation();
	}

	private void createCascadeAnimation() {
		try {
			Animation animation = new AlphaAnimation(0.0f, 1.0f);
			animation.setDuration(100);
			mCascadeAnimSet.addAnimation(animation);

			Animation translate = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			translate.setDuration(150);
			mCascadeAnimSet.addAnimation(translate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void createReverseOrderAnimation() {
		try {
			Animation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			animation.setDuration(150);
			mReverseOrderAnimSet.addAnimation(animation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addAnimations() {
		int id = getRandomNum() % 2;
		switch (id) {
		case 0:
			addCascadeAnimation();
			break;
		case 1:
			addReverseOrderAnimation();
			break;
		}
	}

	private int getRandomNum() {
		Random r = new Random();
		return Math.abs(r.nextInt());
	}

	private void addCascadeAnimation() {
		try {
			LayoutAnimationController controller = mListView.getLayoutAnimation();
			if (controller == null) {
				controller = new LayoutAnimationController(mCascadeAnimSet, 0.5f);
			} else {
				controller.setAnimation(mCascadeAnimSet);
				controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
			}
			mListView.setLayoutAnimation(controller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addReverseOrderAnimation() {
		try {
			LayoutAnimationController controller = mListView.getLayoutAnimation();
			if (controller == null) {
				controller = new LayoutAnimationController(mReverseOrderAnimSet, 0.3f);
			} else {
				controller.setAnimation(mReverseOrderAnimSet);
				controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
			}
			mListView.setLayoutAnimation(controller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private OnItemClickListener mLsnListItemClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
			// TODO
		}
	};

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(PickerApplicationActivity.super.mVb);

		switch (v.getId()) {
		case R.id.btn_pick_app_exit:
			// TODO

			break;
		case R.id.btn_pick_app_set:
			// TODO
			break;

		default:
			break;
		}

	}

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
	}

}