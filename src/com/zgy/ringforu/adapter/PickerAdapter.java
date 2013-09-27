package com.zgy.ringforu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zgy.ringforu.R;
import com.zgy.ringforu.bean.AppInfo;

public class PickerAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private ArrayList<AppInfo> allApps;

	public PickerAdapter(Context context, ArrayList<AppInfo> apps) {
		this.allApps = apps;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return allApps.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.listrow_app, null);

			holder = new ViewHolder();
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_app_icon);
			holder.imgChecked = (ImageView) convertView.findViewById(R.id.img_app_selected);
			holder.textName = (TextView) convertView.findViewById(R.id.text_app_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imgIcon.setImageDrawable(allApps.get(position).getAppIcon());
		holder.textName.setText(allApps.get(position).getName());
		holder.imgChecked.setVisibility(allApps.get(position).getSelected() ? View.VISIBLE : View.GONE);

		return convertView;
	}

	private class ViewHolder {

		ImageView imgIcon;
		TextView textName;
		ImageView imgChecked;
	}
}
