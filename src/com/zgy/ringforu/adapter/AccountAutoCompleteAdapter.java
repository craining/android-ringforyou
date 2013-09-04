package com.zgy.ringforu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.zgy.ringforu.R;

public class AccountAutoCompleteAdapter extends BaseAdapter implements Filterable {

	private Context context;
	private ArrayFilter mFilter;
	private ArrayList<String> mOriginalValues;// 所有的Item
	private List<String> mObjects;// 过滤后的item
	private final Object mLock = new Object();
	// 注意是有序的
	private static final String[] normalDomains = { "126.com", "139.com", "163.com", "188.com", "189.com", "21cn.com", "263.net", "35.cn", "eyou.com", "foxmail.com", "gamail.com", "googlemail.com", "hotmail.com", "live.cn", "msn.com", "qq.com", "sina.com", "sohu.com", "tom.com", "vip.163.com", "vip.188.com", "vip.qq.com", "wo.com.cn", "yahoo.cn", "yahoo.com", "yahoo.com.cn", "yeah.net" };

	private int lineTag = -1;// 用来标记以前登陆过的账户和通用邮箱地址之间的横线显示位置

	private static final String TAG = "AutoCompleteAdapter";

	public AccountAutoCompleteAdapter(Context context, ArrayList<String> mOriginalValues) {
		this.context = context;
		// Arrays.sort(normalDomains, String.CASE_INSENSITIVE_ORDER);
		this.mOriginalValues = mOriginalValues;
		if (mOriginalValues == null) {
			mOriginalValues = new ArrayList<String>();
		}
	}

	@Override
	public Filter getFilter() {
		// if(RingForU.DEBUG)
		// LogRingForu.e(TAG, "getFilter");
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			// if(RingForU.DEBUG)
			// LogRingForu.e(TAG, "FilterResults");

			FilterResults results = new FilterResults();

			// if (mOriginalValues == null) {
			// synchronized (mLock) {
			// mOriginalValues = new ArrayList<String>(mObjects);//
			// }
			// }

			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					// ArrayList<String> list = new ArrayList<String>(mOriginalValues);
					ArrayList<String> list = new ArrayList<String>();
					results.values = list;
					results.count = list.size();
					return results;
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();
				ArrayList<String> newValues = new ArrayList<String>();

				if (prefixString.contains("@")) {
					// 输入@后的匹配内容
					if (occurTimes(prefixString, "@") == 1) {
						// 仅有一个@
						if (mOriginalValues != null && mOriginalValues.size() > 0) {
							final int count = mOriginalValues.size();
							for (int i = 0; i < count; i++) {
								final String value = mOriginalValues.get(i);
								final String valueText = value.toLowerCase();
								if (valueText.startsWith(prefixString)) {
									newValues.add(value);
								}
							}
						}
						lineTag = newValues.size();
						String prefixString1 = prefixString.substring(0, prefixString.indexOf("@"));// 鎴彇@涔嬪墠鐨勫瓧绗�
						// Debug.v(TAG, "prefixString1=" + prefixString1);
						String prefixString2 = prefixString.substring(prefixString.indexOf("@") + 1, prefixString.length());// 鎴彇@涔嬪悗鐨勫瓧绗�
						if (prefixString2 != null && prefixString2.length() > 0) {
							for (String a : normalDomains) {
								if (a.startsWith(prefixString2)) {
									newValues.add(prefixString1 + "@" + a);
								}
							}
							// Debug.v(TAG, "prefixString2=" + prefixString2);
						} else {

							for (String a : normalDomains) {
								newValues.add(prefixString1 + "@" + a);
							}
						}

					} else {
						// 多个@符，无匹配
						synchronized (mLock) {
							ArrayList<String> list = new ArrayList<String>();
							results.values = list;
							results.count = list.size();
							return results;
						}
					}

				} else {
					// 尚未输入@时的匹配内容
					if (mOriginalValues != null && mOriginalValues.size() > 0) {
						final int count = mOriginalValues.size();
						for (int i = 0; i < count; i++) {
							final String value = mOriginalValues.get(i);
							final String valueText = value.toLowerCase();

							if (valueText.startsWith(prefixString)) {
								newValues.add(value);
							}
						}
					}
					lineTag = newValues.size();
					for (String a : normalDomains) {
						newValues.add(prefixString + "@" + a);
					}
				}

				// 滤重，原因是过滤掉 XXX@35.cn重复的可能情况
				for (int m = 0; m < lineTag; m++) {
					// 注意此处的“a = lineTag”，需要确保newValues的size大于lineTag
					for (int a = lineTag; a < newValues.size(); a++) {
						if (newValues.get(a).equals(newValues.get(m))) {
							newValues.remove(a);
							// Debug.v(TAG, "remove id=" + a);
						}
					}
				}
				// if (newValues != null) {
				// HashSet set = new HashSet(newValues);
				// newValues = new ArrayList<String>(set);
				// }

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			// if(RingForU.DEBUG)
			// LogRingForu.e(TAG, "publishResults");
			mObjects = (List<String>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}

	@Override
	public int getCount() {
		// Debug.v(TAG, "getCount");
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return mObjects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// if(RingForU.DEBUG)
		// LogRingForu.e(TAG, "getView");
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.simple_list_item_for_account_autocomplete, null);
			holder.tv = (TextView) convertView.findViewById(R.id.autocomplete_text);
			holder.viewDive = (View) convertView.findViewById(R.id.autocomplete_diver);
			convertView.setTag(holder);
			convertView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
			// if (position == 0) {
			// convertView.setBackgroundResource(R.drawable.bg_account_autocomplete_listitem_topcorner);
			// } else if (position == (mObjects.size() - 1)) {
			// convertView.setBackgroundResource(R.drawable.bg_account_autocomplete_listitem_bottomcorner);
			// } else {
			// convertView.setBackgroundResource(R.drawable.bg_account_autocomplete_listitem_nocorner);
			// }

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv.setText(mObjects.get(position));

		if (position == (lineTag - 1)) {
			holder.viewDive.setVisibility(View.VISIBLE);
		} else {
			holder.viewDive.setVisibility(View.GONE);
		}

		return convertView;
	}

	class ViewHolder {

		TextView tv;
		View viewDive;
	}

	/**
	 * 判断某个字符个数
	 * 
	 * @param string
	 * @param a
	 * @return
	 */
	public static int occurTimes(String string, String a) {
		int pos = -2;
		int n = 0;

		while (pos != -1) {
			if (pos == -2) {
				pos = -1;
			}
			pos = string.indexOf(a, pos + 1);
			if (pos != -1) {
				n++;
			}
		}
		return n;
	}
}
