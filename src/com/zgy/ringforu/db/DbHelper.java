package com.zgy.ringforu.db;

import android.database.sqlite.SQLiteDatabase;

import com.zgy.ringforu.MainApplication;
import com.zgy.ringforu.util.SQLiteHelper;

public class DbHelper extends SQLiteHelper {

	private static final String DB_NAME = "dbname";
	private static final int DB_VERSION = 1;

	private static String CREATE_TB_INFO;
	private static String DROP_TB_INFO = "DROP TABLE IF EXISTS " + Columns.Tb_Info.TB_NAME;;

	public DbHelper() {
		super(MainApplication.getInstance(), DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		initCreateSql();
		db.execSQL(CREATE_TB_INFO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(DROP_TB_INFO);
	}

	private void initCreateSql() {
		StringBuffer sb = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
		sb.append(Columns.Tb_Info.TB_NAME).append(" (").append(Columns.Tb_Info.ID).append(" INTEGER PRIMARY KEY, ").append(Columns.Tb_Info.NAME).append(" TEXT not null);");
		CREATE_TB_INFO = sb.toString();
	}

}
