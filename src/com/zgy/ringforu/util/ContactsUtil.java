package com.zgy.ringforu.util;

import java.util.ArrayList;

import com.zgy.ringforu.R;
import com.zgy.ringforu.bean.ContactInfo;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Contacts.People;
import android.util.Log;

public class ContactsUtil {

//	/**
//	 * 获得联系人姓名地电话
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
//					//过滤掉重复的电话号码， 后期优化 , 不过速度还是挺快的TODO
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

//	
//	/**
//	 * 根据号码获得联系人姓名
//	 * 
//	 * @Description:
//	 * @param con
//	 * @param number
//	 * @return
//	 * @see:
//	 * @since:
//	 * @author: zgy
//	 * @date:2012-8-29
//	 */
//	//TODO 添加从sim卡里读取
//	public static String getNameFromPhone(Context con, String number) {
//		String name = number;
//		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };
//		Cursor cursor = con.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
//		if (cursor != null && cursor.getCount() > 0) {
//			cursor.moveToFirst();
//			String newNumber = "";
//			do {
//				newNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//				if (newNumber.contains(number) || number.contains(newNumber)) {
//					name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)) + ":" + newNumber;
//					break;
//				}
//			} while (cursor.moveToNext());
//			cursor.close();
//		}
//
//		return name;
//	}
	
	
	
	
	
	
	/**
	 * 根据号码获得联系人姓名
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
	public static String getNameFromContactsByNumber(Context con, String number) {
		String name = null;

		// 从手机通讯录查找
		String[] projection = { ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER };
		Cursor cursor = null;
		try {
			cursor = con.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				String newNumber = "";
				do {
					newNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					newNumber = StringUtil.getRidofSpeciall(newNumber);
//					Log.v("", " newNumber =" + newNumber);
					if (newNumber.contains(number) || number.contains(newNumber)) {
						name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
						break;
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}

		}

		// 未获取到姓名，尝试从sim卡里获取
		if (StringUtil.isNull(name)) {
			Cursor cur = null;
			try {
				cur = con.getContentResolver().query(Uri.parse("content://icc/adn"), null, null, null, null);
				if (cur != null && cur.getCount() > 0) {
					cur.moveToFirst();
					String num = "";
					do {
						num = cur.getString(cur.getColumnIndex(People.NUMBER));
						num = StringUtil.getRidofSpeciall(num);
						if (num.contains(number) || number.contains(num)) {
							name = cur.getString(cur.getColumnIndex(People.NAME)) + ":" + num;
							break;
						}

					} while (cur.moveToNext());
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cur != null) {
					cur.close();
				}
			}
		}

		return name;
	}
}
