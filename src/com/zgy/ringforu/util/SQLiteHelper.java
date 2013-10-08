package com.zgy.ringforu.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

/**
 * 鏁版嵁搴撴搷浣渦til
 * 
 * @Description:
 * @author:huangyx2
 * @see:
 * @since:
 * @copyright 漏 35.com
 * @Date:2013-7-24
 */
public abstract class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 鎻掑叆鏁版嵁锛堟浛鎹㈡垨蹇界暐锛?
	 * 
	 * @Description:
	 * @param tableName
	 * @param values
	 * @param conflictFlag
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	private synchronized long insertWithConflict(String tableName, ContentValues values, int conflictFlag) {
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		try {
			db.beginTransaction();
			result = db.insertWithOnConflict(tableName, null, values, conflictFlag);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}

	/**
	 * 鎵ц鎻掑叆鎿嶄綔, 濡傛灉宸茬粡瀛樺湪鐩稿叧璁板綍(鎻掑叆鏃跺彂鐢熺害鏉熷啿绐?, 鍒欒繘琛屾暣琛屾洿鏂?鏇挎崲)
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	public long insertOrReplace(String tableName, ContentValues values) {
		return insertWithConflict(tableName, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

	/**
	 * 娣诲姞
	 * 
	 * @Description:
	 * @param tableName
	 * @param values
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-8-7
	 */
	public synchronized long insert(String tableName, ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		long result = 0;
		try {
			db.beginTransaction();
			result = db.insert(tableName, null, values);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}

	/**
	 * 鎵ц鎻掑叆鎿嶄綔, 濡傛灉鎻掑叆杩囩▼涓彂鐢熶簡鏁版嵁搴撶害鏉熷啿绐? 鍒欎笉鍋氫换浣曚簨鎯?
	 * 
	 * @param tableName
	 * @param values
	 * @return
	 */
	public long insertOrIgnore(String tableName, ContentValues values) {
		return insertWithConflict(tableName, values, SQLiteDatabase.CONFLICT_IGNORE);
	}

	/**
	 * 鏇存柊, 鎹曡幏寮傚父骞跺叧闂暟鎹簱
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 */
	public synchronized int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		int result = 0;
		try {
			db.beginTransaction();
			result = db.update(table, values, whereClause, whereArgs);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}

	/**
	 * 閫氳繃琛屽彿鍒犻櫎鏁版嵁
	 * 
	 * @Description:
	 * @param tableName
	 * @param rowid
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public synchronized int deleteByRowID(String tableName, long rowid) {
		SQLiteDatabase db = getWritableDatabase();
		int result = 0;
		try {
			db.beginTransaction();
			result = db.delete(tableName, "_ROWID_=?", new String[] { String.valueOf(rowid) });
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}

	/**
	 * 鎵ц鍒犻櫎鎿嶄綔
	 * 
	 * @param tableName
	 * @param where
	 * @param args
	 * @return
	 */
	public synchronized int delete(String tableName, String where, String[] args) {
		SQLiteDatabase db = getWritableDatabase();
		int result = 0;
		try {
			db.beginTransaction();
			result = db.delete(tableName, where, args);
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
		return result;
	}

	/**
	 * 缁熻璁板綍鏁?
	 * 
	 * @Description:
	 * @param table
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public long count(String table) {
		SQLiteDatabase db = getReadableDatabase();
		try {
			return DatabaseUtils.queryNumEntries(db, table);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return 0;
	}

	/**
	 * 缁熻璁板綍鏁?
	 * 
	 * @Description:
	 * @param table
	 * @param where
	 * @param whereArgs
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public long count(String table, String where, String[] whereArgs) {
		SQLiteDatabase db = getWritableDatabase();
		try {
			String s = (!TextUtils.isEmpty(where)) ? " where " + where : "";
			return DatabaseUtils.longForQuery(db, "select count(*) from " + table + s, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return 0;
	}

	/**
	 * 鏌ヨ
	 * 
	 * @Description:
	 * @param table
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public Cursor query(String table) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, null, null, null, null, null, null);
	}

	/**
	 * 鏌ヨ鎸囧畾鍒?
	 * 
	 * @Description:
	 * @param table
	 * @param columns
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public Cursor query(String table, String[] columns) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, null, null, null, null, null);
	}

	/**
	 * 鏉′欢鏌ヨ鎸囧畾鍒?
	 * 
	 * @Description:
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, null);
	}

	/**
	 * 鏉′欢鏌ヨ鎸囧畾鍒楀苟鎺掑簭
	 * 
	 * @Description:
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, orderBy);
	}

	/**
	 * 鏉′欢鏌ヨ鎸囧畾鍒楃殑鍑犳潯鏁版嵁骞舵帓搴?
	 * 
	 * @Description:
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @param limit
	 * @return
	 * @see:
	 * @since:
	 * @author: huangyx2
	 * @date:2013-7-24
	 */
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy, String limit) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, orderBy, limit);
	}

}