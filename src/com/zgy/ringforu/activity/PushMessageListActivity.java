package com.zgy.ringforu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.adapter.PushMessageListAdapter;
import com.zgy.ringforu.bean.PushMessage;
import com.zgy.ringforu.interfaces.PushMessageCallBack;
import com.zgy.ringforu.logic.PushMessageController;
import com.zgy.ringforu.util.NotificationUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.ViewUtil;
import com.zgy.ringforu.view.MyToast;

public class PushMessageListActivity extends BaseGestureActivity implements OnClickListener {

	private static final String TAG = "DirectoryPicker";

	private ListView mListView;
	private Button mBtnBack;
	private Button mBtnDelete;
	private Button mBtnCancelSelect;
	private ImageView imgLoading;
	private AnimationDrawable animationDrawable;

	private List<PushMessage> mPushmessageList = new ArrayList<PushMessage>();

	private PushMessageController mController;

	private static final int LIMIT = 10;

	public static final String INTENT_INSERT_MESSAGE = "com.zgy.ringforu.PUSH_MESSAGE_INSERT";
	public static final String INTENT_INSERT_MESSAGE_EXTRA = "notificationId";

	private BroadcastReceiver mReciever;
	private ActivityManager mActivityManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_push_messagelist);

		RingForUActivityManager.push(PushMessageListActivity.this);
		initViews();

		mController = PushMessageController.getInstence();

		mController.getPushMessageList(0, LIMIT, mCallBack);

		IntentFilter counterActionFilter = new IntentFilter(INTENT_INSERT_MESSAGE);
		mReciever = new MyBroadcastReceiver();
		registerReceiver(mReciever, counterActionFilter);
		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		// 清除所有消息通知
		NotificationUtil.hideAllPushMessageNotifications(RingForU.getInstance());
	}

	@Override
	public void finalize() {
		mPushmessageList.clear();
		mPushmessageList = null;
	}

	private void initViews() {
		mListView = (ListView) findViewById(R.id.list_push_messagelist);
		mBtnDelete = (Button) findViewById(R.id.btn_push_messagelist_delete);
		mBtnBack = (Button) findViewById(R.id.btn_push_messagelist_back);
		mBtnCancelSelect = (Button) findViewById(R.id.btn_push_messagelist_cancel_select);
		imgLoading = (ImageView) findViewById(R.id.img_push_messagelist_animation);
		animationDrawable = (AnimationDrawable) imgLoading.getDrawable();
		animationDrawable.start();

		mBtnDelete.setOnClickListener(this);
		mBtnBack.setOnClickListener(this);
		mBtnCancelSelect.setOnClickListener(this);
		mBtnDelete.setVisibility(View.GONE);
		mBtnCancelSelect.setVisibility(View.GONE);
	}

	private void refreshListView() {
		PushMessageListAdapter a = (PushMessageListAdapter) mListView.getAdapter();
		if (a != null) {
			a.notifyDataSetChanged();
			refreshButtonViews();
		}

	}

	private void refreshButtonViews() {
		boolean hasSelected = false;
		for (PushMessage msg : mPushmessageList) {
			if (msg.isSelected()) {
				hasSelected = true;
				break;
			}
		}
		if (hasSelected) {
			mBtnDelete.setVisibility(View.VISIBLE);
			mBtnBack.setVisibility(View.GONE);
			mBtnCancelSelect.setVisibility(View.VISIBLE);
		} else {
			mBtnDelete.setVisibility(View.GONE);
			mBtnBack.setVisibility(View.VISIBLE);
			mBtnCancelSelect.setVisibility(View.GONE);
		}
	}

	private void cancelSelected() {
		for (PushMessage msg : mPushmessageList) {
			msg.setSelected(false);
		}
		refreshListView();
	}

	private void onDeleteSelected() {
		List<Integer> messages = new ArrayList<Integer>();
		for (PushMessage msg : mPushmessageList) {
			if (msg.isSelected()) {
				messages.add(msg.getId());
			}
		}

		int[] ids = new int[messages.size()];
		for (int i = 0; i < messages.size(); i++) {
			ids[i] = messages.get(i);
		}

		mController.deletePushMessages(ids, mCallBack);
	}

	@Override
	protected void onResume() {
		refreshListView();
		super.onResume();
	}

	private OnItemClickListener mLsnListItemClick = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
			PhoneUtil.doVibraterNormal(PushMessageListActivity.super.mVb);
			LogRingForu.e(TAG, "onItemClick id=" + pos);
			PushMessage msg = mPushmessageList.get(pos);
			msg.setReadStatue(PushMessage.READ);
			mController.setPushMessageReadStatue(msg.getReceiveTime(), PushMessage.READ, mCallBack);

			Intent i = new Intent(PushMessageListActivity.this, PushMessageShowActivity.class);
			i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_TITLE, msg.getTitle());
			i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_CONTENT, msg.getContent());
			i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_TAG, msg.getTag());
			i.putExtra(NotificationUtil.INTENT_ACTION_KEY_PUSH_MSG_RECIEVE_TIME, msg.getReceiveTime());
			startActivity(i);
		}
	};

	private OnItemLongClickListener mLsnListItemLongClick = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

			// 长按，选中或不选中
			PhoneUtil.doVibraterNormal(PushMessageListActivity.super.mVb);
			LogRingForu.e(TAG, "onItemLongClick id=" + position);
			PushMessage msg = mPushmessageList.get(position);
			msg.setSelected(!msg.isSelected());
			refreshListView();

			return true;
		}

	};

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(PushMessageListActivity.super.mVb);

		switch (v.getId()) {
		case R.id.btn_push_messagelist_back:
			RingForUActivityManager.pop(PushMessageListActivity.this);
			break;
		case R.id.btn_push_messagelist_delete:
			onDeleteSelected();
			break;

		case R.id.btn_push_messagelist_cancel_select:
			cancelSelected();
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mBtnCancelSelect.getVisibility() == View.VISIBLE) {
				cancelSelected();
			} else {
				RingForUActivityManager.pop(PushMessageListActivity.this);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private PushMessageCallBack mCallBack = new PushMessageCallBack() {

		@Override
		public void getPushMessageListFinished(boolean result, final List<PushMessage> pushMessages) {

			super.getPushMessageListFinished(result, pushMessages);

			if (result) {
				mPushmessageList = pushMessages;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						animationDrawable.stop();
						imgLoading.setVisibility(View.GONE);

						if (pushMessages == null || pushMessages.size() <= 0) {
							MyToast.makeText(RingForU.getInstance(), R.string.push_message_null, Toast.LENGTH_LONG, true).show();
						} else {
							mListView.setAdapter(new PushMessageListAdapter(PushMessageListActivity.this, mPushmessageList));
							mListView.setOnItemClickListener(mLsnListItemClick);
							mListView.setOnItemLongClickListener(mLsnListItemLongClick);
							refreshButtonViews();
						}

					}
				});
			}
		}

		@Override
		public void deletePushMessagesFinished(final int[] ids, final boolean result) {
			super.deletePushMessagesFinished(ids, result);

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (result) {
						MyToast.makeText(PushMessageListActivity.this, R.string.delete_success, Toast.LENGTH_SHORT, false).show();
						mController.getPushMessageList(0, mPushmessageList.size() - ids.length, mCallBack);
					} else {
						MyToast.makeText(PushMessageListActivity.this, R.string.delete_failed, Toast.LENGTH_SHORT, false).show();
						refreshListView();
					}
				}
			});

		}

	};

	private class MyBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(INTENT_INSERT_MESSAGE)) {
				mController.getPushMessageList(0, mPushmessageList.size() + 1, mCallBack);
				MyToast.makeText(context, R.string.push_message_receive_one, Toast.LENGTH_LONG, false).show();

				// 若在当前activity正在显示，则清除通知
				if (mActivityManager != null) {
					RunningTaskInfo info = mActivityManager.getRunningTasks(1).get(0);
					// 类名
					String className = info.topActivity.getClassName(); //
					// 完整类名
					String packageName = info.topActivity.getPackageName(); // 包名

					if (packageName.equals(MainCanstants.PACKAGE_NAME) && className.equals(MainCanstants.ACTIVITY_NAME_PUSHMESSAGE_LIST)) {
						NotificationUtil.showHidePushMessageNotify(false, context, null, intent.getIntExtra(INTENT_INSERT_MESSAGE_EXTRA, -1));
					}
				}

			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReciever);
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
	}

}