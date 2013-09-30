package com.zgy.ringforu.receiver;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushConstants;
import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.bean.PushMessage;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.interfaces.PushMessageCallBack;
import com.zgy.ringforu.logic.PushMessageController;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.NotificationUtil;
import com.zgy.ringforu.util.PushMessageUtils;
import com.zgy.ringforu.util.TimeUtil;

public class PushMessageReceiver extends BroadcastReceiver {

	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();

	/**
	 * @param context
	 *            Context
	 * @param intent
	 *            ���յ�intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {

		LogRingForu.d(TAG, ">>> Receive intent: \r\n" + intent);

		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			// ��ȡ��Ϣ����
			String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);

			// ��Ϣ���û��Զ������ݶ�ȡ��ʽ
			LogRingForu.i(TAG, "onMessage: " + message);

			// �û��ڴ��Զ��崦����Ϣ,���´���Ϊdemo����չʾ��
			if (message.contains(PushMessageUtils.MESSAGE_CONTENT_NEW_VERSION)) {
				// �汾������ʾ
				try {
					String extras = intent.getStringExtra(PushConstants.EXTRA_EXTRA);
					// �Զ������ݵ�json��
					LogRingForu.d(TAG, "EXTRA_EXTRA = " + extras);
					JSONObject json = new JSONObject(extras);
					int newVersionCode = Integer.parseInt(json.getString(PushMessageUtils.MESSAGE_TAG_VERSION_CODE));
					LogRingForu.v(TAG, "newVersionCode = " + newVersionCode + "   now version=" + MainUtil.getAppVersionCode(context));
					// if (newVersionCode > MainUtil.getAppVersionCode(context)) {
					MainConfig.getInstance().setPushNewVersionCode(newVersionCode);
					MainConfig.getInstance().setPushNewVersionDownloadUrl(json.getString(PushMessageUtils.MESSAGE_TAG_DOWNLOAD_URL));
					MainConfig.getInstance().setPushNewVersionInfo(json.getString(PushMessageUtils.MESSAGE_TAG_VERSION_INFO));
					MainUtil.checkNewVersion(context);
					// }
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (message.contains(PushMessageUtils.MESSAGE_CONTENT_PUSH_MSG)) {

				try {
					String extras = intent.getStringExtra(PushConstants.EXTRA_EXTRA);
					// �Զ������ݵ�json��
					LogRingForu.d(TAG, "EXTRA_EXTRA = " + extras);
					JSONObject json = new JSONObject(extras);

					PushMessage msg = new PushMessage();
					msg.setTitle(json.getString(PushMessageUtils.MESSAGE_TAG_TITLE));
					msg.setContent(json.getString(PushMessageUtils.MESSAGE_TAG_CONTENT));
					msg.setTag(json.getString(PushMessageUtils.MESSAGE_TAG_TAG));
					msg.setReceiveTime(TimeUtil.getCurrentTimeMillis());
					
					PushMessageController controller = PushMessageController.getInstence();
					controller.addCallBack(new MyPushMessageCallBack(context));
					controller.insertPushMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			// ����󶨵ȷ����ķ�������
			// PushManager.startWork()�ķ���ֵͨ��PushConstants.METHOD_BIND�õ�

			// ��ȡ����
			final String method = intent.getStringExtra(PushConstants.EXTRA_METHOD);
			// �������ش����롣���󶨷��ش��󣨷�0������Ӧ�ý���������������Ϣ��
			// ��ʧ�ܵ�ԭ���ж��֣�������ԭ�򣬻�access token���ڡ�
			// �벻Ҫ�ڳ���ʱ���м򵥵�startWork���ã����п��ܵ�����ѭ����
			// ����ͨ���������Դ���������������ʱ�����µ����������
			int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE, PushConstants.ERROR_SUCCESS);
			String content = "";
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				// ��������
				content = new String(intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			}

			// �û��ڴ��Զ��崦����Ϣ,���´���Ϊdemo����չʾ��
			LogRingForu.d(TAG, "onMessage: method : " + method);
			LogRingForu.d(TAG, "onMessage: result : " + errorCode);
			LogRingForu.d(TAG, "onMessage: content : " + content);
			// Toast.makeText(context, "method : " + method + "\n result: " + errorCode + "\n content = " +
			// content, Toast.LENGTH_SHORT).show();

		}
	}

	private class MyPushMessageCallBack extends PushMessageCallBack {

		private Context contex;

		// private PushMessage message;
		//
		//
		public MyPushMessageCallBack(Context context ) {
			this.contex = context;
			// this.message = message;
		}

		@Override
		public void insertPushMessageFinished(boolean result, PushMessage message) {
			super.insertPushMessageFinished(result, message);

			// ��Ϣ��ʾ
			if (MainConfig.getInstance().isPushMsgOn()) {
				NotificationUtil.showHidePushMessageNotify(true, contex, message);
			}
		}
	}

}
