package com.zgy.ringforu.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zgy.ringforu.R;

public class FCMenu extends FCPopupWindow implements OnItemClickListener {

	private Context mContext;
	private MenuItemOnClickListener callback;
	private ViewGroup container;

	private BaseAdapter mAdapter;
	private ListView mListView;
	private TextView mTextTitle;

	private int[] mWidthHeight;
	private View mViewAnchor;

	public FCMenu(Context context, int[] widthHeight, View anchor) {
		super(context);
		this.mContext = context;
		this.mWidthHeight = widthHeight;
		this.mViewAnchor = anchor;
		init();
	}

	public FCMenu(Context context, MenuItemOnClickListener callback, int[] widthHeight, View anchor) {
		super(context);
		this.mContext = context;
		this.callback = callback;
		this.mWidthHeight = widthHeight;
		this.mViewAnchor = anchor;
		init();
	}

	public void setMenuItemOnclickListener(MenuItemOnClickListener callback) {
		this.callback = callback;
	}

	private void init() {

		LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
		this.container = (ViewGroup) inflater.inflate(R.layout.top_menu_layout, null);

		this.mListView = (ListView) this.container.findViewById(R.id.list_top_menu);
		this.mTextTitle = (TextView) this.container.findViewById(R.id.text_top_menu_title);

		this.container.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (FCMenu.this.isShowing()) {
					FCMenu.this.close();
				}
			}
		});
		this.container.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_MENU) {
					if (FCMenu.this.isShowing()) {
						FCMenu.this.close();
						return false;
					}
					return true;
				}
				return true;
			}
		});
	}

	/**
	 * ÏÔÊ¾Êý¾Ý
	 * 
	 * @Description:
	 * @param columnNum
	 * @param items
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-10
	 */
	public void setDatas(List<FCMenuItem> items, int titleSrcId) {
		mTextTitle.setText(titleSrcId);
		mAdapter = new MenuAdapter(items);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	public void showMenu() {
		// this.showAtBottom(container);

		// this.showAsDropDown(container, anchor, ViewGroup.LayoutParams.WRAP_CONTENT,
		// ViewGroup.LayoutParams.WRAP_CONTENT);

		if (mWidthHeight == null) {
			this.showAsDropDown(container, mViewAnchor, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		} else {
			this.showAsDropDown(container, mViewAnchor, mWidthHeight[0], mWidthHeight[1]);
		}
	}

	public void closeMenu() {
		this.close();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		closeMenu();
		this.callback.onItemClicked((FCMenuItem) parent.getAdapter().getItem(position));
	}

	public class MenuHolder {

		ImageView iconView;
		TextView textView;
	}

	private class MenuAdapter extends BaseAdapter {

		List<FCMenuItem> mItems = new ArrayList<FCMenuItem>();

		public MenuAdapter(List<FCMenuItem> items) {
			this.mItems = items;
		}

		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public Object getItem(int position) {
			return mItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return mItems.get(position).getOpID();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuHolder holder = null;
			if (holder == null || convertView.getTag() == null) {
				holder = new MenuHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.top_menu_listrow, null);
				holder.iconView = (ImageView) convertView.findViewById(R.id.img_top_menu_icon);
				holder.textView = (TextView) convertView.findViewById(R.id.text_top_menu_item);
				convertView.setTag(holder);
			}
			FCMenuItem item = (FCMenuItem) getItem(position);
			if (item.getIconRes() != -1) {
				holder.iconView.setImageResource(item.getIconRes());
			} else {
				holder.iconView.setVisibility(View.GONE);
			}
			if (item.getTextRes() != -1) {
				holder.textView.setText(item.getTextRes());
			} else {
				holder.textView.setVisibility(View.GONE);
			}

			return convertView;
		}
	}

	public interface MenuItemOnClickListener {

		public void onItemClicked(FCMenuItem item);
	}
}
