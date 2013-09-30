package com.zgy.ringforu.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.zgy.ringforu.bean.PushMessage;
import com.zgy.ringforu.db.DbOpera;
import com.zgy.ringforu.interfaces.PushMessageCallBack;

public class PushMessageController {

	private static DbOpera mDbOpera;
	private static PushMessageController mController;

	// private Set<PushMessageCallBack> mCallbacks = new
	// CopyOnWriteArraySet<PushMessageCallBack>();

	// public void addCallBack(PushMessageCallBack callback) {
	// mCallbacks.add(callback);
	// }
	//
	// public void remoeCallBack(PushMessageCallBack callback) {
	// mCallbacks.remove(callback);
	// }
	//
	// private Set<PushMessageCallBack> getCallBacks() {
	// return mCallbacks;
	// }

	private PushMessageController() {

	}

	public static PushMessageController getInstence() {
		if (mDbOpera == null) {
			mDbOpera = DbOpera.getInstance();
		}
		if (mController == null) {
			mController = new PushMessageController();
		}
		return mController;
	}

	/**
	 * 保存一则信息
	 * 
	 * @Description:
	 * @param msg
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void insertPushMessage(final PushMessage msg, final PushMessageCallBack callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mDbOpera.insertPushMessage(msg);

					// for (PushMessageCallBack callback : getCallBacks()) {
					callback.insertPushMessageFinished(true, msg);
					// }
				} catch (Exception e) {
					e.printStackTrace();
					// for (PushMessageCallBack callback : getCallBacks()) {
					callback.insertPushMessageFinished(false, msg);
					// }
				}

			}
		}).start();
	}

	/**
	 * 更新已读未读状态
	 * 
	 * @Description:
	 * @param receiveTime
	 * @param statue
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void setPushMessageReadStatue(final long receiveTime, final int statue, final PushMessageCallBack callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mDbOpera.setPushMessageReadFalg(receiveTime, statue);

					// for (PushMessageCallBack callback : getCallBacks()) {
					callback.setPushMessageReadStatueFinished(true);
					// }
				} catch (Exception e) {
					e.printStackTrace();
					// for (PushMessageCallBack callback : getCallBacks()) {
					callback.setPushMessageReadStatueFinished(false);
					// }
				}

			}
		}).start();
	}

	/**
	 * 增加分享次数
	 * 
	 * @Description:
	 * @param receiveTime
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void addShareTimesLog(final long receiveTime, final PushMessageCallBack callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mDbOpera.addPushMessageSharedTime(receiveTime);

					// for (PushMessageCallBack callback : getCallBacks()) {
					callback.addPushMessageSharedTimesFinished(true);
					// }
				} catch (Exception e) {
					e.printStackTrace();
					// for (PushMessageCallBack callback : getCallBacks()) {
					callback.addPushMessageSharedTimesFinished(false);
					// }
				}

			}
		}).start();
	}

	/**
	 * 读取信息列表
	 * 
	 * @param start
	 * @param end
	 */
	public void getPushMessageList(final int start, final int end, final PushMessageCallBack callback) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					List<PushMessage> messages = new ArrayList<PushMessage>();
					messages = mDbOpera.getPushMessageList(String.valueOf(start + "," + end));

					// for (PushMessageCallBack callback : getCallBacks()) {
					callback.getPushMessageListFinished(true, messages);
					// }
				} catch (Exception e) {
					e.printStackTrace();
					// for (PushMessageCallBack callback : getCallBacks()) {
					callback.getPushMessageListFinished(false, null);
					// }
				}

			}
		}).start();
	}
}
