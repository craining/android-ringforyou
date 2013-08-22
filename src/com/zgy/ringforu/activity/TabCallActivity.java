package com.zgy.ringforu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
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

import com.zgy.ringforu.R;
import com.zgy.ringforu.R.id;
import com.zgy.ringforu.R.layout;
import com.zgy.ringforu.R.string;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class TabCallActivity extends Activity implements OnClickListener {

	private static final String TAG = "TabCallActivity";

	private Button btnAddFromContacts;
	private Button btnAddByInput;
	private String allNumbersSelected;// ������ѡ����
	private String allNameSelected;// ������ѡ����
	private LinearLayout layoutShowNull;
	// private RelativeLayout layoutMain;
	private TextView textShowdelTip;
	private ListView listMain;
	private SimpleAdapter listItemAdapter;

	private Button btnClsList;
	private ImageView imgHelp;
	private ImageView imgSet;

	ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();

	private Vibrator vb = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab_activity_call);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		// layoutMain = (RelativeLayout)
		// findViewById(R.id.layout_tab_call_main);
		layoutShowNull = (LinearLayout) findViewById(R.id.layout_call_null);
		textShowdelTip = (TextView) findViewById(R.id.text_call_top_deletetip);

		btnAddFromContacts = (Button) findViewById(R.id.btn_call_addfrom);
		btnAddByInput = (Button) findViewById(R.id.btn_call_addby);
		btnClsList = (Button) findViewById(R.id.btn_call_clslist);
		listMain = (ListView) findViewById(R.id.list_call);
		imgHelp = (ImageView) findViewById(R.id.img_call_help);
		imgSet = (ImageView) findViewById(R.id.img_call_set);

		btnAddFromContacts.setOnClickListener(TabCallActivity.this);
		btnAddByInput.setOnClickListener(TabCallActivity.this);
		btnClsList.setOnClickListener(TabCallActivity.this);
		imgHelp.setOnClickListener(TabCallActivity.this);
		imgSet.setOnClickListener(TabCallActivity.this);

		listMain.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Log.v(TAG, "delete: " + position);
				if (PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
				listItem.remove(position);
				listItemAdapter.notifyDataSetChanged();
				refreshViews();
				saveLastAll();
				MyToast.makeText(TabCallActivity.this, R.string.del_success, Toast.LENGTH_SHORT, false).show();
				return false;
			}

		});
	}

	private void saveLastAll() {
		for (HashMap<String, String> a : listItem) {
			MainUtil.insert(a.get("name"), a.get("number"), TabCallActivity.this, 1);
		}
	}

	private void initListView() {
		listItem = new ArrayList<HashMap<String, String>>();
		MainConfig mainConfig = MainConfig.getInstance();
		allNumbersSelected = mainConfig.getInterceptCallNumbers();
		allNameSelected = mainConfig.getInterceptCallNames();
		if (allNumbersSelected != null && allNameSelected != null) {
			String[] allNums = allNumbersSelected.split(":::");
			String[] allNames = allNameSelected.split(":::");
			for (int i = 0; i < allNums.length; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("name", allNames[i]);
				map.put("number", allNums[i]);
				listItem.add(map);
			}
		}
		listItemAdapter = new SimpleAdapter(TabCallActivity.this, listItem, R.layout.listrow, new String[] { "name", "number" }, new int[] { R.id.name, R.id.number });
		listMain.setAdapter(listItemAdapter);
		refreshViews();
	}

	private void refreshViews() {
		if (listItem != null && listItem.size() > 0) {
			// btnExport.setEnabled(true);
			listMain.setVisibility(View.VISIBLE);
			textShowdelTip.setVisibility(View.VISIBLE);
			layoutShowNull.setVisibility(View.GONE);
			// layoutMain.setBackgroundResource(R.drawable.bg_light);
			btnClsList.setVisibility(View.VISIBLE);
		} else {
			// btnExport.setEnabled(false);
			listMain.setVisibility(View.GONE);
			textShowdelTip.setVisibility(View.GONE);
			layoutShowNull.setVisibility(View.VISIBLE);
			// layoutMain.setBackgroundColor(getResources().getColor(R.color.light_blue_bg));
			btnClsList.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Log.e(TAG, "onResume");
		initListView();
		super.onResume();
	}

	@Override
	public void onClick(View v) {

		if (PhoneUtil.bIsVerbOn) {
			vb.vibrate(new long[] { 0, 20 }, -1);
		}

		switch (v.getId()) {
		case R.id.btn_call_addfrom:
			Intent i = new Intent(TabCallActivity.this, AddByContactsActivity.class);
			i.putExtra("tag", 1);
			startActivityForResult(i, 1);
			break;
		case R.id.btn_call_addby:
			Intent i2 = new Intent(TabCallActivity.this, AddByInputActivity.class);
			i2.putExtra("tag", 1);
			startActivityForResult(i2, 1);
			break;

		case R.id.btn_call_clslist:
			// ��ն�������
			MyDialog.Builder builder = new MyDialog.Builder(TabCallActivity.this);
			builder.setTitle(R.string.str_tip).setMessage(R.string.sure_delete_call).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
					listItem = new ArrayList<HashMap<String, String>>();
					listItemAdapter.notifyDataSetChanged();
					refreshViews();
					saveLastAll();
					MyToast.makeText(TabCallActivity.this, R.string.clear_success, Toast.LENGTH_SHORT, false).show();
				}
			}).setNegativeButton(R.string.str_cancel, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
					if (PhoneUtil.bIsVerbOn) {
						vb.vibrate(new long[] { 0, 20 }, -1);
					}
				}
			}).create().show();

			break;
		case R.id.img_call_help:
			Intent i3 = new Intent(TabCallActivity.this, AboutActivity.class);
			startActivity(i3);
			break;
		case R.id.img_call_set:
			Intent i4 = new Intent(TabCallActivity.this, SetActivity.class);
			i4.putExtra("tag", 1);
			startActivity(i4);
			break;
		default:
			break;
		}

	}
}