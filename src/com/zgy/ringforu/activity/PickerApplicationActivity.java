package com.zgy.ringforu.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.adapter.PickerAdapter;
import com.zgy.ringforu.bean.AppInfo;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.ViewUtil;

public class PickerApplicationActivity extends BaseGestureActivity implements OnClickListener {

	private static final String TAG = "DirectoryPicker";

	private ListView mListView;
	private Button mBtnOk;
	private Button mBtnBack;
	private ImageView imgLoading;
	private AnimationDrawable animationDrawable;

	private ArrayList<AppInfo> mAppsInfoList = new ArrayList<AppInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pick_application);

		RingForUActivityManager.push(PickerApplicationActivity.this);
		initViews();
		queryFilterAppInfo();

	}

	@Override
	public void finalize() {
		mAppsInfoList.clear();
		mAppsInfoList = null;
	}

	private void initViews() {
		mListView = (ListView) findViewById(R.id.list_pick_app);
		mBtnOk = (Button) findViewById(R.id.btn_pick_app_set);
		mBtnBack = (Button) findViewById(R.id.btn_pick_back);
		imgLoading = (ImageView) findViewById(R.id.img_pick_app_animation);
		animationDrawable = (AnimationDrawable) imgLoading.getDrawable();
		animationDrawable.start();

		mBtnOk.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
	}

	private void refreshListView() {
		PickerAdapter a = (PickerAdapter) mListView.getAdapter();
		if (a != null) {
			a.notifyDataSetChanged();
		}

	}

	// 全部程序包
	private void queryFilterAppInfo() {

		new Thread(new Runnable() {

			@Override
			public void run() {
				mAppsInfoList = new ArrayList<AppInfo>();
				HashSet<AppInfo> apps = new HashSet<AppInfo>();
				String selectedApps = RingForU.getInstance().getPackageNameHideWaterMark();

				PackageManager pm = PickerApplicationActivity.this.getPackageManager();
				// 查询所有已经安装的应用程序
				List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的

				for (ApplicationInfo app : listAppcations) {
					AppInfo appInfo = new AppInfo();
					appInfo.setName((String) app.loadLabel(pm));
					appInfo.setAppIcon(app.loadIcon(pm));
					appInfo.setPackageName(app.packageName);
					// 判断是否已选
					if (selectedApps.contains(appInfo.getPackageName())) {
						appInfo.setSelected(true);
					} else {
						appInfo.setSelected(false);
					}
					apps.add(appInfo);
				}
				for (AppInfo app : apps) {
					mAppsInfoList.add(app);
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						animationDrawable.stop();
						imgLoading.setVisibility(View.GONE);
						mListView.setAdapter(new PickerAdapter(PickerApplicationActivity.this, mAppsInfoList));
						mListView.setOnItemClickListener(mLsnListItemClick);
					}
				});
			}
		}).start();
	}

	public void queryAppInfo() {
		mAppsInfoList = new ArrayList<AppInfo>();
		HashSet<AppInfo> apps = new HashSet<AppInfo>();
		String selectedApps = RingForU.getInstance().getPackageNameHideWaterMark();

		PackageManager pm = this.getPackageManager(); // 获得PackageManager对象
		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		// mainIntent.addCategory(Intent.CATEGORY_DEFAULT);
		// 通过查询，获得所有ResolveInfo对象.
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent, PackageManager.MATCH_DEFAULT_ONLY);
		// 调用系统排序 ， 根据name排序
		// 该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
		for (ResolveInfo reInfo : resolveInfos) {
			// String activityName = reInfo.activityInfo.name; //
			// 获得该应用程序的启动Activity的name
			// // 为应用程序的启动Activity 准备Intent
			// Intent launchIntent = new Intent();
			// launchIntent.setComponent(new ComponentName(pkgName,
			// activityName));
			// 创建一个AppInfo对象，并赋值
			AppInfo appInfo = new AppInfo();
			appInfo.setName((String) reInfo.loadLabel(pm));
			appInfo.setPackageName(reInfo.activityInfo.packageName);
			appInfo.setAppIcon(reInfo.loadIcon(pm));
			// 判断是否已选
			if (selectedApps.contains(appInfo.getPackageName())) {
				appInfo.setSelected(true);
			} else {
				appInfo.setSelected(false);
			}
			apps.add(appInfo); // 添加至列表中
		}

		for (AppInfo app : apps) {
			mAppsInfoList.add(app);
		}
	}

	private OnItemClickListener mLsnListItemClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
			//
			LogRingForu.e(TAG, "onItemClick id=" + pos);
			if (mAppsInfoList.get(pos).getSelected()) {
				mAppsInfoList.get(pos).setSelected(false);
			} else {
				mAppsInfoList.get(pos).setSelected(true);
			}

			refreshListView();
		}
	};

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(PickerApplicationActivity.super.mVb);

		switch (v.getId()) {
		case R.id.btn_pick_back:
			RingForUActivityManager.pop(PickerApplicationActivity.this);
			break;
		case R.id.btn_pick_app_set:
			onSet();
			break;

		default:
			break;
		}

	}

	private void onSet() {
		StringBuilder sb = new StringBuilder();
		for (AppInfo app : mAppsInfoList) {
			if (app.getSelected()) {
				sb.append(app.getPackageName() + ":::");
			}
		}
		String apps = sb.toString();
		MainUtil.refreshWaterMarkHideApps(apps.substring(0, apps.length() - 3));

		LogRingForu.e(TAG, "apps=" + apps.substring(0, apps.length() - 3));
		RingForUActivityManager.pop(PickerApplicationActivity.this);
	}

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
		ViewUtil.onButtonPressedBack(mBtnBack);
		PhoneUtil.doVibraterNormal(super.mVb);
		RingForUActivityManager.pop(this);

	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
		ViewUtil.onButtonPressedBlue(mBtnOk);
		PhoneUtil.doVibraterNormal(super.mVb);
		onSet();
		RingForUActivityManager.pop(this);
	}

}