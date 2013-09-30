package com.zgy.ringforu.logic;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.zgy.ringforu.bean.PushMessage;
import com.zgy.ringforu.db.DbOpera;
import com.zgy.ringforu.interfaces.PushMessageCallBack;

public class PushMessageController {

	private static DbOpera mDbOpera;
	private static PushMessageController mController;

	private Set<PushMessageCallBack> mCallbacks = new CopyOnWriteArraySet<PushMessageCallBack>();

	public void addCallBack(PushMessageCallBack callback) {
		mCallbacks.add(callback);
	}

	public void remoeCallBack(PushMessageCallBack callback) {
		mCallbacks.remove(callback);
	}

	private Set<PushMessageCallBack> getCallBacks() {
		return mCallbacks;
	}

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
	 * ����һ����Ϣ
	 * 
	 * @Description:
	 * @param msg
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void insertPushMessage(final PushMessage msg) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mDbOpera.insertPushMessage(msg);

					for (PushMessageCallBack callback : getCallBacks()) {
						callback.insertPushMessageFinished(true, msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					for (PushMessageCallBack callback : getCallBacks()) {
						callback.insertPushMessageFinished(false, msg);
					}
				}

			}
		}).start();
	}

	/**
	 * �����Ѷ�δ��״̬
	 * @Description:
	 * @param receiveTime
	 * @param statue
	 * @see: 
	 * @since: 
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void setPushMessageReadStatue(final long receiveTime, final int statue) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mDbOpera.setPushMessageReadFalg(receiveTime, statue);

					for (PushMessageCallBack callback : getCallBacks()) {
						callback.setPushMessageReadStatueFinished(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					for (PushMessageCallBack callback : getCallBacks()) {
						callback.setPushMessageReadStatueFinished(false);
					}
				}

			}
		}).start();
	}
	/**
	 * ���ӷ������
	 * @Description:
	 * @param receiveTime
	 * @see: 
	 * @since: 
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void addShareTimesLog(final long receiveTime) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mDbOpera.addPushMessageSharedTime(receiveTime);

					for (PushMessageCallBack callback : getCallBacks()) {
						callback.addPushMessageSharedTimesFinished(true);
					}
				} catch (Exception e) {
					e.printStackTrace();
					for (PushMessageCallBack callback : getCallBacks()) {
						callback.addPushMessageSharedTimesFinished(false);
					}
				}

			}
		}).start();
	}
}
