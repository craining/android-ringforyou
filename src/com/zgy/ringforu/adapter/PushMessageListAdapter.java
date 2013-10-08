package com.zgy.ringforu.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
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

		PushMessage msg = mMessages.get(position);

		holder.textTitle.setText(msg.getTitle());
		holder.textContent.setText(msg.getContent().replaceAll(PushMessageUtils.MESSAGE_TAG_BREAKLINE, "\r\n"));
		holder.textReceiveTime.setText(TimeUtil.longToDateTimeString(msg.getReceiveTime()));

		if (msg.getReadStatue() == PushMessage.READ) {
			holder.textReceiveTime.setTypeface(Typeface.DEFAULT);
			holder.textReceiveTime.getPaint().setFakeBoldText(false);
			holder.textTitle.setTypeface(Typeface.DEFAULT);
			holder.textTitle.getPaint().setFakeBoldText(false);
			// holder.textTitle.setTextColor(getResources().getColor(R.color.black));
		} else {
			holder.textReceiveTime.setTypeface(Typeface.DEFAULT_BOLD);
			holder.textReceiveTime.getPaint().setFakeBoldText(true);
			holder.textTitle.setTypeface(Typeface.DEFAULT_BOLD);
			holder.textTitle.getPaint().setFakeBoldText(true);
		}

		// ±³¾°
		if (msg.isSelected()) {
			((RelativeLayout) convertView.findViewById(R.id.layout_push_message_item_middle)).setBackgroundResource(R.drawable.shape_rounded_layout_p);
		} else {
			if (msg.getReadStatue() == PushMessage.READ) {
				((RelativeLayout) convertView.findViewById(R.id.layout_push_message_item_middle)).setBackgroundResource(R.drawable.selector_rounded_layout_read);
			} else {
				((RelativeLayout) convertView.findViewById(R.id.layout_push_message_item_middle)).setBackgroundResource(R.drawable.selector_rounded_layout_unread);
			}
		}

		return convertView;
	}

	private class ViewHolder {

		TextView textTitle;
		TextView textContent;
		TextView textReceiveTime;
	}
}
