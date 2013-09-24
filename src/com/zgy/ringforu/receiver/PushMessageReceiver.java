package com.zgy.ringforu.receiver;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.android.pushservice.PushConstants;
import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PushMessageUtils;

public class PushMessageReceiver extends BroadcastReceiver {

	/** TAG to Log */
	public static final String TAG = PushMessageReceiver.class.getSimpleName();

	/**
	 * @param context
	 *            Context
	 * @param intent
	 *            接收的intent
	 */
	@Override
	public void onReceive(final Context context, Intent intent) {

		LogRingForu.d(TAG, ">>> Receive intent: \r\n" + intent);

		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			// 获取消息内容
			String message = intent.getExtras().getString(PushConstants.EXTRA_PUSH_MESSAGE_STRING);

			// 消息的用户自定义内容读取方式
			LogRingForu.i(TAG, "onMessage: " + message);

			// 用户在此自定义处理消息,以下代码为demo界面展示用
			if (message.equals(PushMessageUtils.MESSAGE_CONTENT_NEW_VERSION)) {
				try {
					String extras = intent.getStringExtra(PushConstants.EXTRA_EXTRA);
					// 自定义内容的json串
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
			}

		}

		else if (intent.getAction().equals(PushConstants.ACTION_RECEIVE)) {
			// 处理绑定等方法的返回数据
			// PushManager.startWork()的返回值通过PushConstants.METHOD_BIND得到

			// 获取方法
			final String method = intent.getStringExtra(PushConstants.EXTRA_METHOD);
			// 方法返回错误码。若绑定返回错误（非0），则应用将不能正常接收消息。
			// 绑定失败的原因有多种，如网络原因，或access token过期。
			// 请不要在出错时进行简单的startWork调用，这有可能导致死循环。
			// 可以通过限制重试次数，或者在其他时机重新调用来解决。
			int errorCode = intent.getIntExtra(PushConstants.EXTRA_ERROR_CODE, PushConstants.ERROR_SUCCESS);
			String content = "";
			if (intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT) != null) {
				// 返回内容
				content = new String(intent.getByteArrayExtra(PushConstants.EXTRA_CONTENT));
			}

			// 用户在此自定义处理消息,以下代码为demo界面展示用
			LogRingForu.d(TAG, "onMessage: method : " + method);
			LogRingForu.d(TAG, "onMessage: result : " + errorCode);
			LogRingForu.d(TAG, "onMessage: content : " + content);
			// Toast.makeText(context, "method : " + method + "\n result: " + errorCode + "\n content = " +
			// content, Toast.LENGTH_SHORT).show();

			// 可选。通知用户点击事件处理
		} else if (intent.getAction().equals(PushConstants.ACTION_RECEIVER_NOTIFICATION_CLICK)) {
			LogRingForu.d(TAG, "intent=" + intent.toUri(0));

			// 自定义内容的json串
			LogRingForu.d(TAG, "EXTRA_EXTRA = " + intent.getStringExtra(PushConstants.EXTRA_EXTRA));
			String title = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_TITLE);
			String content = intent.getStringExtra(PushConstants.EXTRA_NOTIFICATION_CONTENT);

		}
	}

}
