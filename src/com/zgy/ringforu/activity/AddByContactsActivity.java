package com.zgy.ringforu.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Service;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.bean.ContactInfo;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.view.MyToast;
import com.zgy.ringforu.view.TextEditor;

public class AddByContactsActivity extends Activity implements OnClickListener {

	private static final String TAG = "AddByContactsActivity";
	private AsyncQueryHandler asyncQuery;
	private RelativeLayout layoutTitle;
	private ListView listviewContacts;
	private ArrayList<ContactInfo> listContacts = new ArrayList<ContactInfo>();
	// private Set<ContactInfo> listContacts = new HashSet<ContactInfo>();
	private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	private SimpleAdapter listItemAdapter;
	private TextView textShowLeft;
	private TextView textShowNull;
	// private TextView textShowTip;
	private TextEditor editSearch;
	private ImageView imgClearEdit;
	private ImageView imgLoading;
	private LinearLayout layoutTip;
	private Button btnBack;

	private Vibrator vb = null;
	private Handler myHandler;

	private static final int MSG_REFRESH_LISTVEW = 0x188;

	private AnimationDrawable animationDrawable;

	// private int mMarchCount = 0;

	private int tag = 0;// 默认为重要联系人的添加 1:屏蔽电话 2:屏蔽短信

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contacts);

		Bundle b = getIntent().getExtras();
		if (b != null) {
			tag = b.getInt("tag");
		}

		asyncQuery = new MyAsyncQueryHandler(getContentResolver());
		myHandler = new MyHandler();

		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		layoutTitle = (RelativeLayout) findViewById(R.id.layout_add_contact_title);
		editSearch = (TextEditor) findViewById(R.id.edit_addcontact_search);
		imgClearEdit = (ImageView) findViewById(R.id.img_addcontacts_clear);
		listviewContacts = (ListView) findViewById(R.id.list_contacts);
		textShowLeft = (TextView) findViewById(R.id.text_add_contact);
		textShowNull = (TextView) findViewById(R.id.text_addcontacts_null);
		layoutTip = (LinearLayout) findViewById(R.id.layout_add_contact_tip);
		// textShowTip = (TextView) findViewById(R.id.text_add_contact_tip);
		btnBack = (Button) findViewById(R.id.btn_add_contact_return);
		imgLoading = (ImageView) findViewById(R.id.img_add_contact_animation);
		// 播放动画
		imgLoading.setImageResource(R.anim.loading);
		animationDrawable = (AnimationDrawable) imgLoading.getDrawable();
		imgLoading.post(new Runnable() {

			@Override
			public void run() {
				animationDrawable.start();
			}
		});

		btnBack.setOnClickListener(this);
		imgClearEdit.setOnClickListener(this);
		layoutTitle.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				PhoneUtil.hideKeyboard(AddByContactsActivity.this, editSearch);
				return false;
			}
		});

		listviewContacts.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

				int result = MainUtil.insert(listItem.get(position).get("name"), listItem.get(position).get("number"), AddByContactsActivity.this, tag);
				switch (result) {
				case 1:
					listItem.remove(position);
					listItemAdapter.notifyDataSetChanged();
					MyToast.makeText(AddByContactsActivity.this, R.string.add_success, Toast.LENGTH_SHORT, false).show();
					PhoneUtil.doVibraterNormal(vb);
					refreshViews();
					break;
				case -1:
					MyToast.makeText(AddByContactsActivity.this, R.string.add_cannot_more, Toast.LENGTH_SHORT, true).show();
					PhoneUtil.doVibraterNormal(vb);
					break;
				case 0:
					MyToast.makeText(AddByContactsActivity.this, R.string.add_fail, Toast.LENGTH_SHORT, true).show();
					PhoneUtil.doVibraterNormal(vb);
					break;
				default:
					break;
				}
				return false;
			}

		});

		editSearch.setOnTextChangeListener(new TextEditor.OnTextChangeListener() {

			public void onTextChanged(View v, String s) {
				if (s != null) {
					if (s.length() > 0) {
						imgClearEdit.setVisibility(View.VISIBLE);
					} else {
						imgClearEdit.setVisibility(View.GONE);
					}
					final String content = s.trim();
					if (editSearch.isModified()) { // filter the results
						updateList(content);
						editSearch.setModify(false);
					}
				} else {
					imgClearEdit.setVisibility(View.GONE);
				}
			}
		});
	}

	// private void loadDataInThread() {
	//
	// new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	//
	// try {
	// listContacts = ContactsUtil.getContactList(AddByContactsActivity.this);
	// } catch (Exception e) {
	// e.printStackTrace();
	// } finally {
	// myHandler.sendEmptyMessage(MSG_REFRESH_LISTVEW);
	// }
	// }
	//
	// }).start();
	//
	// }

	private class MyAsyncQueryHandler extends AsyncQueryHandler {

		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			ContactInfo info;
			boolean exist = false;
			listContacts = new ArrayList<ContactInfo>();
			cursor.moveToFirst();
			while (cursor.moveToNext()) {

				// 滤重，排除相同的号码的联系人，
				// 不用set原因是,需要保持原有顺序
				String num = StringUtil.getRidofSpeciall(cursor.getString(2));
				exist = false;
				for (ContactInfo inf : listContacts) {
					if (inf.num.equals(num)) {
						exist = true;
					}
				}
				if (!exist) {
					info = new ContactInfo();
					info.name = cursor.getString(1);
					info.num = StringUtil.getRidofSpeciall(cursor.getString(2));
					info.storeKey = cursor.getString(3);

					if (RingForU.DEBUG)
						LogRingForu.v(TAG, "info.name=" + info.name + "   changed=" + StringUtil.getRidofSpecialOfFileName(info.name));

					listContacts.add(info);
				}

			}
			freshListView();
		}

	}

	/**
	 * 刷新列表
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-12-4
	 */
	private void freshListView() {

		MainConfig mainConfig = MainConfig.getInstance();

		String getAllNumsImportant = mainConfig.getImporantNumbers();
		String getAllNumsCall = mainConfig.getInterceptCallNumbers();
		String getAllNumsSms = mainConfig.getInterceptSmsNumbers();

		switch (tag) {
		case MainCanstants.TYPE_IMPORTANT:
			// 重要联系人添加，不能包含屏蔽短信或电话的人
			listItem = new ArrayList<HashMap<String, String>>();
			if (listContacts != null && listContacts.size() > 0) {
				for (ContactInfo info : listContacts) {
					HashMap<String, String> map = new HashMap<String, String>();
					if (info.match && (getAllNumsImportant == null || !getAllNumsImportant.contains(info.num)) && (getAllNumsCall == null || !getAllNumsCall.contains(info.num)) && (getAllNumsSms == null || !getAllNumsSms.contains(info.num))) {
						map.put("name", info.name);
						map.put("number", info.num);
						listItem.add(map);
					}
				}
			}

			break;
		case MainCanstants.TYPE_INTECEPT_CALL:
			// 添加屏蔽电话的人，不能包含重要联系人
			listItem = new ArrayList<HashMap<String, String>>();
			if (listContacts != null && listContacts.size() > 0) {
				for (ContactInfo info : listContacts) {
					HashMap<String, String> map = new HashMap<String, String>();
					if (info.match && (getAllNumsCall == null || !getAllNumsCall.contains(info.num)) && (getAllNumsImportant == null || !getAllNumsImportant.contains(info.num))) {
						map.put("name", info.name);
						map.put("number", info.num);
						listItem.add(map);
					}
				}
			}

			break;
		case MainCanstants.TYPE_INTECEPT_SMS:
			// 添加屏蔽短信，不能包含重要联系人
			listItem = new ArrayList<HashMap<String, String>>();
			if (listContacts != null && listContacts.size() > 0) {
				for (ContactInfo info : listContacts) {
					HashMap<String, String> map = new HashMap<String, String>();
					if (info.match && (getAllNumsSms == null || !getAllNumsSms.contains(info.num)) && (getAllNumsImportant == null || !getAllNumsImportant.contains(info.num))) {
						map.put("name", info.name);
						map.put("number", info.num);
						listItem.add(map);
					}
				}

			}

			break;

		default:
			break;
		}
		if (RingForU.DEBUG)
			LogRingForu.v(TAG, "listItem " + listItem.size());
		listItemAdapter = new SimpleAdapter(AddByContactsActivity.this, listItem, R.layout.listrow, new String[] { "name", "number" }, new int[] { R.id.name, R.id.number });
		listviewContacts.setAdapter(listItemAdapter);
		refreshViews();
	}

	/**
	 * 刷新View
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2012-11-28
	 */
	private void refreshViews() {
		animationDrawable.stop();
		imgLoading.setVisibility(View.GONE);
		if (listItem != null && listItem.size() > 0) {
			listviewContacts.setVisibility(View.VISIBLE);
			textShowNull.setVisibility(View.GONE);
			layoutTip.setVisibility(View.VISIBLE);
		} else {
			listviewContacts.setVisibility(View.GONE);
			textShowNull.setVisibility(View.VISIBLE);
			layoutTip.setVisibility(View.GONE);
		}
		int left = MainUtil.getLeft(AddByContactsActivity.this, tag);
		textShowLeft.setText(getString(R.string.left_top) + left + getString(R.string.left_bottom));
		if (left <= 0) {
			MyToast.makeText(AddByContactsActivity.this, R.string.add_cannot_more, Toast.LENGTH_SHORT, true).show();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (vb != null) {
			vb.cancel();
			vb = null;
		}
	}

	@Override
	public void onClick(View v) {

		PhoneUtil.doVibraterNormal(vb);
		switch (v.getId()) {
		case R.id.btn_add_contact_return:
			finish();
			break;
		case R.id.img_addcontacts_clear:
			editSearch.setText("");
			break;
		default:
			break;
		}

	}

	@Override
	protected void onResume() {

		Uri uri = Uri.parse("content://com.android.contacts/data/phones");
		String[] projection = { "_id", "display_name", "data1", "sort_key" };
		asyncQuery.startQuery(0, null, uri, projection, null, null, "sort_key COLLATE LOCALIZED asc");// 按拼音排序查询

		super.onResume();
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case MSG_REFRESH_LISTVEW:
				if (RingForU.DEBUG)
					LogRingForu.v(TAG, "on fresh ");
				// imgLoading.setVisibility(View.GONE);
				freshListView();
				break;

			default:
				break;
			}
		}

	}

	/**
	 * 刷新list，搜索刷新
	 * 
	 * @param constraint
	 */
	private void updateList(String constraint) {
		filterData(constraint);
		freshListView();
	}

	// FIXME: 有些丑陋，但这里需要效率
	private void filterData(String constraint) {
		// mMarchCount = 0;
		if (constraint == null || constraint.trim().length() < 1) {
			for (ContactInfo info : listContacts) {
				info.match = true;
			}
			// mMarchCount = listContacts.size();
		} else {
			constraint = constraint.trim().toLowerCase();
			for (ContactInfo info : listContacts) {
				if (matchIgnorCase(info.name, constraint) || matchIgnorCase(info.num, constraint) || matchStartViewKeyStore(info.storeKey, constraint)) {
					info.match = true;
					// mMarchCount++;
				} else {
					info.match = false;
				}
			}
		}
	}

	private static boolean matchIgnorCase(String src, String lowerCase) {
		if (lowerCase == null)
			return true;
		if (src == null)
			return false;
		if (src.toLowerCase().indexOf(lowerCase) != -1)
			return true;
		return false;
	}

	private static boolean matchStartViewKeyStore(String src, String lowerCase) {
		if (lowerCase == null)
			return true;
		if (src == null)
			return false;
		if (src.toLowerCase().startsWith(lowerCase)) {
			return true;
		}
		return false;
	}
}
