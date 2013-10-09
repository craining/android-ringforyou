package com.zgy.ringforu.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public abstract class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

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

	public long insertOrReplace(String tableName, ContentValues values) {
		return insertWithConflict(tableName, values, SQLiteDatabase.CONFLICT_REPLACE);
	}

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

	public long insertOrIgnore(String tableName, ContentValues values) {
		return insertWithConflict(tableName, values, SQLiteDatabase.CONFLICT_IGNORE);
	}

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

	public Cursor query(String table) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, null, null, null, null, null, null);
	}

	public Cursor query(String table, String[] columns) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, null, null, null, null, null);
	}

	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, null);
	}

	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, orderBy);
	}

	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String orderBy, String limit) {
		SQLiteDatabase db = getReadableDatabase();
		return db.query(table, columns, selection, selectionArgs, null, null, orderBy, limit);
	}

}