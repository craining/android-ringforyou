package com.zgy.ringforu.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.interfaces.OnGestureChangedListener;
import com.zgy.ringforu.util.AddContactUtil;
import com.zgy.ringforu.util.ImportExportUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.util.ViewUtil;
import com.zgy.ringforu.view.FCMenu;
import com.zgy.ringforu.view.FCMenu.MenuItemOnClickListener;
import com.zgy.ringforu.view.FCMenuItem;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class TabImportantActivity extends Activity implements OnClickListener {

	private static final String TAG = "TabImportantActivity";

	private String allNumbersSelected;// 所有已选号码
	private String allNameSelected;// 所有已选名字
	private LinearLayout layoutShowNull;
	// private RelativeLayout layoutMain;
	private TextView textShowdelTip;
	private ListView listMain;
	private SimpleAdapter listItemAdapter;

	private Button btnSet, btnExit;

	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

	private FCMenu mTopMenu;
	private OnTopMenuItemClickedListener mTopMenuListener;
	private static final int ID_MENU_ADD_CONTACTS = 1;
	private static final int ID_MENU_ADD_INPUT = 2;
	private static final int ID_MENU_IMPORT = 3;
	private static final int ID_MENU_EXPORT = 4;
	private static final int ID_MENU_CLEAR = 5;
	private static final int ID_MENU_MORE = 6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_activity_important);

		// RingForUActivityManager.push(this);

		// layoutMain = (RelativeLayout) findViewById(R.id.layout_tab_important_main);
		layoutShowNull = (LinearLayout) findViewById(R.id.layout_important_null);
		textShowdelTip = (TextView) findViewById(R.id.text_important_top_deletetip);

		listMain = (ListView) findViewById(R.id.list_important);
		btnSet = (Button) findViewById(R.id.btn_important_set);
		btnExit = (Button) findViewById(R.id.btn_important_exit);

		initTopMenu();

		btnSet.setOnClickListener(this);
		btnExit.setOnClickListener(this);

		listMain.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				LogRingForu.v(TAG, "delete: " + position);
				PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
				listItem.remove(position);
				listItemAdapter.notifyDataSetChanged();
				refreshViews();
				saveLastAll();
				MyToast.makeText(TabImportantActivity.this, R.string.del_success, Toast.LENGTH_SHORT, false).show();
				return false;
			}

		});
	}

	private void saveLastAll() {
		MainConfig.getInstance().setImportantNumbers("");
		MainConfig.getInstance().setImportantNames("");
		AddContactUtil.insert("", "", TabImportantActivity.this, MainCanstants.TYPE_IMPORTANT);
		for (HashMap<String, String> a : listItem) {
			AddContactUtil.insert(a.get("name"), a.get("number"), TabImportantActivity.this, MainCanstants.TYPE_IMPORTANT);
		}
	}

	private void initListView() {
		listItem = new ArrayList<HashMap<String, String>>();
		MainConfig mainConfig = MainConfig.getInstance();
		allNumbersSelected = mainConfig.getImporantNumbers();
		allNameSelected = mainConfig.getImporantNames();
		// if(RingForU.DEBUG)
		// LogRingForu.e(TAG, "names=" + allNameSelected);
		// LogRingForu.e(TAG, "nums=" + allNumbersSelected);
		if (!StringUtil.isNull(allNumbersSelected) && !StringUtil.isNull(allNameSelected)) {
			String[] allNums = allNumbersSelected.split(":::");
			String[] allNames = allNameSelected.split(":::");
			for (int i = 0; i < allNums.length; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", allNames[i]);
				map.put("number", allNums[i]);
				listItem.add(map);
			}
		}
		listItemAdapter = new SimpleAdapter(TabImportantActivity.this, listItem, R.layout.listrow, new String[] { "name", "number" }, new int[] { R.id.name, R.id.number });
		listMain.setAdapter(listItemAdapter);
		refreshViews();
	}

	private void refreshViews() {
		if (listItem != null && listItem.size() > 0) {
			listMain.setVisibility(View.VISIBLE);
			textShowdelTip.setVisibility(View.VISIBLE);
			layoutShowNull.setVisibility(View.GONE);
			// layoutMain.setBackgroundResource(R.drawable.bg_light);
			// btnClsList.setVisibility(View.VISIBLE);
		} else {
			listMain.setVisibility(View.GONE);
			textShowdelTip.setVisibility(View.GONE);
			layoutShowNull.setVisibility(View.VISIBLE);
			// layoutMain.setBackgroundColor(getResources().getColor(R.color.light_blue_bg));
			// btnClsList.setVisibility(View.GONE);
		}
	}

	private void initTopMenu() {
		DisplayMetrics dMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
		int[] widthHeight = new int[2];
		widthHeight[0] = dMetrics.widthPixels / 2;
		// widthHeight[1] = dMetrics.heightPixels / 2;
		widthHeight[1] = ViewGroup.LayoutParams.FILL_PARENT;

		mTopMenu = new FCMenu(TabImportantActivity.this, widthHeight, findViewById(R.id.view_important_anchor));
		mTopMenuListener = new OnTopMenuItemClickedListener();
		mTopMenu.setMenuItemOnclickListener(mTopMenuListener);

		List<FCMenuItem> items = new ArrayList<FCMenuItem>();
		items.add(new FCMenuItem(ID_MENU_ADD_CONTACTS, -1, R.string.add_fromcontacts));
		items.add(new FCMenuItem(ID_MENU_ADD_INPUT, -1, R.string.add_byhand));
		items.add(new FCMenuItem(ID_MENU_EXPORT, -1, R.string.export_data));
		items.add(new FCMenuItem(ID_MENU_IMPORT, -1, R.string.import_data));
		items.add(new FCMenuItem(ID_MENU_CLEAR, -1, R.string.clear_all));
		items.add(new FCMenuItem(ID_MENU_MORE, -1, R.string.set_str));
		mTopMenu.setDatas(items, R.string.menu_title_important);

	}

	private class OnTopMenuItemClickedListener implements MenuItemOnClickListener {

		@Override
		public void onItemClicked(FCMenuItem item) {
			PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
			switch (item.getOpID()) {
			case ID_MENU_ADD_CONTACTS:
				Intent i = new Intent(TabImportantActivity.this, AddByContactsActivity.class);
				i.putExtra("tag", MainCanstants.TYPE_IMPORTANT);
				startActivity(i);
				break;
			case ID_MENU_ADD_INPUT:
				Intent i2 = new Intent(TabImportantActivity.this, AddByInputActivity.class);
				i2.putExtra("tag", MainCanstants.TYPE_IMPORTANT);
				startActivity(i2);
				break;
			case ID_MENU_IMPORT:
				ImportExportUtil.importData(TabImportantActivity.this, MainCanstants.TYPE_IMPORTANT);
				break;
			case ID_MENU_EXPORT:
				ImportExportUtil.exportData(TabImportantActivity.this, MainCanstants.TYPE_IMPORTANT);
				break;
			case ID_MENU_CLEAR:
				if (listItem != null && listItem.size() > 0) {
					showClearDlg();
				} else {
					MyToast.makeText(TabImportantActivity.this, R.string.clear_null, Toast.LENGTH_LONG, true).show();
				}
				break;
			case ID_MENU_MORE:
				Intent i4 = new Intent(TabImportantActivity.this, SetActivity.class);
				i4.putExtra("tag", MainCanstants.TYPE_IMPORTANT);
				startActivity(i4);
				break;
			default:
				break;
			}

		}

	}

	private void showClearDlg() {
		// 清空重要联系人
		MyDialog.Builder builder = new MyDialog.Builder(TabImportantActivity.this);
		builder.setTitle(R.string.str_tip).setMessage(R.string.sure_delete).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
				listItem = new ArrayList<HashMap<String, String>>();
				listItemAdapter.notifyDataSetChanged();
				refreshViews();
				saveLastAll();
				MyToast.makeText(TabImportantActivity.this, R.string.clear_success, Toast.LENGTH_SHORT, false).show();
			}
		}).setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
			}
		}).create().show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		LogRingForu.e(TAG, "onResume");
		((MainActivityGroup) getParent()).setOnGestureChangedListener(mGuesterListener);
		initListView();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);

		switch (v.getId()) {
		case R.id.btn_important_set:

			if (mTopMenu == null) {
				initTopMenu();
			}
			if (mTopMenu.isShowing()) {
				mTopMenu.closeMenu();
			} else {
				mTopMenu.showMenu();
			}

			break;
		case R.id.btn_important_exit:
			finish();
			break;
		default:
			break;
		}

	}

	/**
	 * 手势监听，从ActivityGroup传递过来的
	 */
	private OnGestureChangedListener mGuesterListener = new OnGestureChangedListener() {

		@Override
		public void onSlideToRight() {
			ViewUtil.onButtonPressedBlue(btnExit);
			PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
			if (mTopMenu.isShowing()) {
				mTopMenu.closeMenu();
			}
			finish();
		}

		@Override
		public void onSlideToLeft() {
			if (!mTopMenu.isShowing()) {
				ViewUtil.onButtonPressedBlue(btnSet);
				PhoneUtil.doVibraterNormal(((MainActivityGroup) getParent()).mVb);
				mTopMenu.showMenu();
			}
		}
	};

}
