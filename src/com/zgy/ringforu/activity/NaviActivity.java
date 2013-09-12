package com.zgy.ringforu.activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.zgy.ringforu.R;


public class NaviActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// 此页较为简单，没用xml布局

		LinearLayout layouMain = new LinearLayout(NaviActivity.this);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		layouMain.setLayoutParams(lp);

		ViewPager viewPager = new ViewPager(NaviActivity.this);
		List<View> mListViews = new ArrayList<View>();
		List<Drawable> backGrounds = getBackGroundDrawables();

		for (int i = 0; i < backGrounds.size(); i++) {
			LinearLayout layout = new LinearLayout(NaviActivity.this);
			layout.setLayoutParams(lp);
			layout.setBackgroundDrawable(backGrounds.get(i));

			// 点击最后页，退出
			if (i == (backGrounds.size() - 1)) {
				layout.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
			}

			mListViews.add(layout);
		}

		viewPager.setAdapter(new MyViewPagerAdapter(mListViews));
		layouMain.addView(viewPager);
		this.setContentView(layouMain);
	}

	/**
	 * 读取背景资源
	 * 
	 * @Description:
	 * @param position
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-5
	 */
	private List<Drawable> getBackGroundDrawables() {
		List<Drawable> drawables = new ArrayList<Drawable>();

		drawables.add(new BitmapDrawable(readBitMap(R.drawable.bg_light)));
		drawables.add(new BitmapDrawable(readBitMap(R.drawable.bg_light)));
		drawables.add(new BitmapDrawable(readBitMap(R.drawable.bg_light)));
		drawables.add(new BitmapDrawable(readBitMap(R.drawable.bg_light)));

		return drawables;

	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @Description:
	 * @param resId
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-6
	 */
	public Bitmap readBitMap(int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 适配
	 * 
	 * @Description:
	 * @author: zhuanggy
	 * @see:
	 * @since:
	 * @copyright © 35.com
	 * @Date:2013-9-5
	 */
	public class MyViewPagerAdapter extends PagerAdapter {

		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;// 构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));// 删除页卡
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) { // 这个方法用来实例化页卡
			container.addView(mListViews.get(position), 0);// 添加页卡
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();// 返回页卡的数量
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
}
