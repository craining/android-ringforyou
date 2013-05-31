package com.zgy.ringforu.util;

import java.util.ArrayList;

import com.zgy.ringforu.R;
import com.zgy.ringforu.bean.ContactInfo;


import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

public class ContactsUtil {

//	/**
//	 * �����ϵ�������ص绰
//	 * 
//	 */
//	public static ArrayList<ContactInfo> getContactList(Context con) {
//
//		ArrayList<ContactInfo> list = new ArrayList<ContactInfo>();
//
//		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };
//		Cursor cursor = con.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
//		if (cursor != null && cursor.getCount() > 0) {
//			cursor.moveToFirst();
//			String newNumber = "";
//			do {
//				newNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//				if (newNumber != null) {
//					newNumber = MainUtil.getRidofSpeciall(newNumber);
//					//���˵��ظ��ĵ绰���룬 �����Ż� , �����ٶȻ���ͦ���TODO
//					boolean exist = false;
//					for (ContactInfo a : list) {
//						if(a.getNum().equals(newNumber)) {
//							exist = true;
//						}
//					}
//					if(!exist) {
//						ContactInfo info = new ContactInfo();
//						String name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
//						if (name == null) {
//							name = con.getString(R.string.name_null);
//						}
//						info.name = name;
//						info.num = newNumber;
//						list.add(info);
//					}
//				}
//
//			} while (cursor.moveToNext());
//			cursor.close();
//		}
//
//		return list;
//	}

	
	/**
	 * ���ݺ�������ϵ������
	 * 
	 * @Description:
	 * @param con
	 * @param number
	 * @return
	 * @see:
	 * @since:
	 * @author: zgy
	 * @date:2012-8-29
	 */
	public static String getNameFromPhone(Context con, String number) {
		String name = number;
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };
		Cursor cursor = con.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			String newNumber = "";
			do {
				newNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				if (newNumber.contains(number) || number.contains(newNumber)) {
					name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)) + ":" + newNumber;
					break;
				}
			} while (cursor.moveToNext());
			cursor.close();
		}

		return name;
	}
}
