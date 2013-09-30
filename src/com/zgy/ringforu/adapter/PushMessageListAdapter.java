package com.zgy.ringforu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zgy.ringforu.R;
import com.zgy.ringforu.bean.PushMessage;
import com.zgy.ringforu.util.PushMessageUtils;
import com.zgy.ringforu.util.TimeUtil;

public class PushMessageListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;

	private List<PushMessage> mMessages;

	public PushMessageListAdapter(Context context, List<PushMessage> messages) {
		this.mMessages = messages;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mMessages.size();
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
			convertView = mInflater.inflate(R.layout.listrow_push_msg, null);

			holder = new ViewHolder();
			holder.textTitle = (TextView) convertView.findViewById(R.id.text_push_message_title);
			holder.textContent = (TextView) convertView.findViewById(R.id.text_push_message_content);
			holder.textReceiveTime = (TextView) convertView.findViewById(R.id.text_push_message_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		convertView.findViewById(R.id.view_push_message_item_top).setVisibility(position == 0 ? View.VISIBLE : View.GONE);
		convertView.findViewById(R.id.view_push_message_item_bottom).setVisibility(position == mMessages.size() - 1 ? View.VISIBLE : View.GONE);

		holder.textTitle.setText(mMessages.get(position).getTitle());
		holder.textContent.setText(mMessages.get(position).getContent().replaceAll(PushMessageUtils.MESSAGE_TAG_BREAKLINE, "\r\n"));
		holder.textReceiveTime.setText(TimeUtil.longToDateTimeString(mMessages.get(position).getReceiveTime()));

		((RelativeLayout) convertView.findViewById(R.id.layout_push_message_item_middle))
				.setBackgroundResource(mMessages.get(position).getReadStatue() == PushMessage.READ ? R.drawable.selector_rounded_layout_read : R.drawable.selector_rounded_layout_unread);

		return convertView;
	}

	private class ViewHolder {

		TextView textTitle;
		TextView textContent;
		TextView textReceiveTime;
	}
}
