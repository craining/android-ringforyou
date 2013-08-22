package com.zgy.ringforu.db;

import android.content.ContentValues;

import com.zgy.ringforu.db.Columns.Tb_Info;

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

	public void insertInfoName(String name) {
		ContentValues value = new ContentValues();
		value.put(Columns.Tb_Info.NAME, name);
		insertOrIgnore(Tb_Info.TB_NAME, value);
	}

}
