package com.zgy.ringforu.db;

import android.database.sqlite.SQLiteDatabase;

import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.util.SQLiteHelper;

public class DbHelper extends SQLiteHelper {

	private static final String DB_NAME = "dbname";
	private static final int DB_VERSION = 1;

	private static String CREATE_TB_INFO;
	private static String DROP_TB_INFO = "DROP TABLE IF EXISTS " + Columns.Tb_PushMessage.TB_NAME;;

	public DbHelper() {
		super(RingForU.getInstance(), getDbName(), null, DB_VERSION);
	}

	private static String getDbName() {
		if (RingForU.DB_SAVE_SDCARD) {
			return MainCanstants.getSdFile().getPath() + "/" + DB_NAME;
		} else {
			return DB_NAME;
		}
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
		sb.append(Columns.Tb_PushMessage.TB_NAME).append(" (")

		.append(Columns.Tb_PushMessage.ID).append(" INTEGER PRIMARY KEY, ")

		.append(Columns.Tb_PushMessage.TITLE).append(" TEXT, ")

		.append(Columns.Tb_PushMessage.CONTENT).append(" TEXT, ")

		.append(Columns.Tb_PushMessage.TAG).append(" TEXT, ")

		.append(Columns.Tb_PushMessage.RECEIVE_TIME).append(" LONG, ")

		.append(Columns.Tb_PushMessage.SHARED_TIMES).append(" INT DEFAULE -1, ")

		.append(Columns.Tb_PushMessage.READ_STATUE).append(" INT DEFAULE 0);");

		CREATE_TB_INFO = sb.toString();
	}

}
