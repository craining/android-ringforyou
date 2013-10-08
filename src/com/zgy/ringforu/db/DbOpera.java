package com.zgy.ringforu.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.bean.PushMessage;
import com.zgy.ringforu.db.Columns.Tb_PushMessage;

public class DbOpera extends DbHelper {

	private DbOpera() {
		super();
	}

	private static String mSyn = "";

	private static DbOpera instances;

	public static DbOpera getInstance() {

		synchronized (mSyn) {
			if (instances == null) {
				instances = new DbOpera();
			}
			return instances;
		}
	}

	/**
	 * 插入一条消息
	 * 
	 * @Description:
	 * @param msg
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void insertPushMessage(PushMessage msg) {
		ContentValues value = new ContentValues();
		value.put(Columns.Tb_PushMessage.TITLE, msg.getTitle());
		value.put(Columns.Tb_PushMessage.CONTENT, msg.getContent());
		value.put(Columns.Tb_PushMessage.TAG, msg.getTag());
		value.put(Columns.Tb_PushMessage.RECEIVE_TIME, msg.getReceiveTime());
		value.put(Columns.Tb_PushMessage.READ_STATUE, msg.getReadStatue());
		value.put(Columns.Tb_PushMessage.SHARED_TIMES, msg.getSharedTimes());

		insertOrIgnore(Tb_PushMessage.TB_NAME, value);
	}

	/**
	 * 查询消息总数
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public int getPushMessageTotalCount() {
		int count = 0;
		try {
			SQLiteDatabase db = getWritableDatabase();
			// TODO 目前只有处理箱子， 不管收藏夹
			// 总数
			StringBuffer sql = new StringBuffer();
			sql.append("select count(").append(Columns.Tb_PushMessage.ID).append(")");
			sql.append(" from ").append(Columns.Tb_PushMessage.TB_NAME);
			// sql.append(" where ").append(Columns.Tb_PushMessage.ID).append("=?");
			count = (int) DatabaseUtils.longForQuery(db, sql.toString(), null);
			sql = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	/**
	 * 读取消息列表
	 * 
	 * @Description:
	 * @param limit
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public List<PushMessage> getPushMessageList(String limit) {
		Cursor cursor = null;
		List<PushMessage> pushMessages = new ArrayList<PushMessage>();
		PushMessage msg = null;
		try {
			// 查询文件夹
			cursor = query(Columns.Tb_PushMessage.TB_NAME, null, null, null, Columns.Tb_PushMessage.RECEIVE_TIME + " desc", limit);
			while (cursor.moveToNext()) {
				msg = new PushMessage();
				msg.setId(cursor.getInt(cursor.getColumnIndex(Columns.Tb_PushMessage.ID)));
				msg.setTitle(cursor.getString(cursor.getColumnIndex(Columns.Tb_PushMessage.TITLE)));
				msg.setContent(cursor.getString(cursor.getColumnIndex(Columns.Tb_PushMessage.CONTENT)));
				msg.setTag(cursor.getString(cursor.getColumnIndex(Columns.Tb_PushMessage.TAG)));
				msg.setReceiveTime(cursor.getLong(cursor.getColumnIndex(Columns.Tb_PushMessage.RECEIVE_TIME)));
				msg.setReadStatue(cursor.getInt(cursor.getColumnIndex(Columns.Tb_PushMessage.READ_STATUE)));
				msg.setSharedTimes(cursor.getInt(cursor.getColumnIndex(Columns.Tb_PushMessage.SHARED_TIMES)));
				pushMessages.add(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return pushMessages;
	}

	/**
	 * 更改一则消息的已读未读状态
	 * 
	 * @Description:
	 * @param id
	 * @param flag
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void setPushMessageReadFalg(long receiveTime, int flag) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(Columns.Tb_PushMessage.TB_NAME).append(" set ").append(Columns.Tb_PushMessage.READ_STATUE).append("=? where ").append(Columns.Tb_PushMessage.RECEIVE_TIME).append("=?");
		getWritableDatabase().execSQL(sql.toString(), new Object[] { flag, receiveTime });
	}

	/**
	 * 分享次数加1
	 * 
	 * @Description:
	 * @param id
	 * @param flag
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-9-30
	 */
	public void addPushMessageSharedTime(long receiveTime) {

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE ").append(Columns.Tb_PushMessage.TB_NAME).append(" set ").append(Columns.Tb_PushMessage.SHARED_TIMES).append("CONVERT (CHAR,(CONVERT(").append(Columns.Tb_PushMessage.SHARED_TIMES).append(",INT)+1)) where ").append(Columns.Tb_PushMessage.RECEIVE_TIME).append("=?");
		LogRingForu.e("", "setPushMessageSharedTime sql=" + sql.toString());
		getWritableDatabase().execSQL(sql.toString(), new Object[] { receiveTime });
	}

	/**
	 * 删除信息
	 * 
	 * @Description:
	 * @param uids
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-10-8
	 */
	public boolean deletePushMessage(int[] ids) {
		SQLiteDatabase db = getWritableDatabase();
		db.beginTransaction();
		try {
			for (int id : ids) {
				db.delete(Tb_PushMessage.TB_NAME, Tb_PushMessage.ID + "=?", new String[] { id + "" });
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.endTransaction();
		}
		return true;
	}
}
