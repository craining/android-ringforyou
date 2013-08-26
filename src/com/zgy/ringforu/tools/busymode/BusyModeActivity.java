package com.zgy.ringforu.tools.busymode;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.view.MyToast;
import com.zgy.ringforu.view.WordsFlowView;

public class BusyModeActivity extends Activity implements OnClickListener {

	private static final String TAG = "BusyModeActivity";

	private Button btnBack;
	private Button btnOk;
	private Button btnClose;
	private EditText editMsgContent;
	private WordsFlowView wordsFlow;

	private Vibrator vb = null;

	public static String[] busyModesTitle;
	public static String[] busyModeInfo;

	private boolean boolFromNotification = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.busymode);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);

		btnBack = (Button) findViewById(R.id.btn_busymodeset_back);
		btnClose = (Button) findViewById(R.id.btn_busymode_title_close);
		btnOk = (Button) findViewById(R.id.btn_busymodeset_ok);
		editMsgContent = (EditText) findViewById(R.id.edit_busymode_msgcontent);
		wordsFlow = (WordsFlowView) findViewById(R.id.busymode_flow);

		// ��ʾ���رա��͡�������
		// // ������ʾ�����ء��͡�������
		// Bundle b = getIntent().getExtras();
		// if (b != null && b.containsKey("fromnotifybar") && b.getBoolean("fromnotifybar")) {
		// // �Ǵ�֪ͨ����ת������
		// boolFromNotification = true;
		//
		// }

		if (BusyModeUtil.isBusyModeOn()) {
			btnClose.setVisibility(View.VISIBLE);
		} else {
			btnClose.setVisibility(View.GONE);
		}

		wordsFlow.setDuration(500l);
		wordsFlow.setOnItemClickListener(this);
		btnBack.setOnClickListener(this);
		btnOk.setOnClickListener(this);
		btnClose.setOnClickListener(this);

		busyModesTitle = getResources().getStringArray(R.array.busymodes_title);
		busyModeInfo = getResources().getStringArray(R.array.busymodes_info);

		// ���
		// feedKeywordsFlow(wordsFlow, busyModesTitle);
		wordsFlow.feedKeywordsAll(busyModesTitle);
		// if (RingForU.DEBUG)
		// Log.e(TAG, "test test point 1");
		setSelectedMsgContent(BusyModeUtil.getBusyModeMsgContent(BusyModeActivity.this), true);
	}

	// /**
	// * ����Ҫ��ʾ�Ĵ���
	// *
	// * @param keywordsFlow
	// * @param arr
	// */
	// private static void feedKeywordsFlow(WordsFlowView keywordsFlow, String[] arr) {
	//
	// for (String words : arr) {
	// keywordsFlow.feedKeyword(words);
	// }
	// // Random random = new Random();
	// // for (int i = 0; i < WordsFlowView.MAX; i++) {
	// // int ran = random.nextInt(arr.length);
	// // String tmp = arr[ran];
	// // keywordsFlow.feedKeyword(tmp);
	// // }
	// }

	@Override
	public void onClick(View v) {

		PhoneUtil.doVibraterNormal(vb);
		if (v instanceof Button) {
			switch (v.getId()) {
			case R.id.btn_busymodeset_back:
				finish();
				break;
			case R.id.btn_busymode_title_close:
				BusyModeUtil.setBusyModeOnOff(BusyModeActivity.this, false, null);
				MyToast.makeText(BusyModeActivity.this, R.string.busymode_tip_close_toast, MyToast.LENGTH_LONG, false).show();
				finish();
				break;

			case R.id.btn_busymodeset_ok:
				// �����Զ��ظ��Ŀ��أ��Լ����Żظ�������
				if (TextUtils.isEmpty(editMsgContent.getText())) {
					BusyModeUtil.setBusyModeOnOff(BusyModeActivity.this, true, null);
				} else {
					BusyModeUtil.setBusyModeOnOff(BusyModeActivity.this, true, editMsgContent.getText().toString());
				}

				MyToast.makeText(BusyModeActivity.this, R.string.busymode_tip_open_toast, MyToast.LENGTH_LONG, false).show();
				finish();
				break;

			default:
				break;
			}
		} else if (v instanceof TextView) {
			// ���ﱻ���
			setSelectedMsgTitle(((TextView) v).getText().toString(), false);
		}

	}

	/**
	 * ���ݱ�����ı�����ʾ����������
	 * 
	 * @param title
	 */
	private void setSelectedMsgTitle(String title, boolean anim) {

		editMsgContent.setText(BusyModeUtil.getMessageContentFromTitle(busyModesTitle, busyModeInfo, title));

		if (anim) {
			wordsFlow.go2Show(WordsFlowView.ANIMATION_IN);
		}
	}

	/**
	 * ��ʾ����������
	 * 
	 * @param title
	 */
	private void setSelectedMsgContent(String content, boolean anim) {
		editMsgContent.setText(content);
		if (anim) {
			wordsFlow.go2Show(WordsFlowView.ANIMATION_IN);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub// ����״̬��ͼ��
		BusyModeUtil.checkBusyModeState(BusyModeActivity.this);
		super.onDestroy();
	}

}
