package com.zgy.ringforu.util;

public class StringUtil {

	/**
	 * ��ȡ�ַ����е�����
	 * 
	 * @param str
	 * @return
	 */
	public static String getNumbersFromString(String str) {
		String str1 = str.trim();
		String str2 = "";
		if (str1 != null && !"".equals(str1)) {
			for (int i = 0; i < str.length(); i++) {
				if (str1.charAt(i) >= 48 && str1.charAt(i) <= 57) {
					str2 += str1.charAt(i);
				}
			}
		}
		return str2;
	}

	/**
	 * �绰�����г�ȥ������ַ�
	 * @Description:
	 * @param number
	 * @return
	 */
	public static String getRidofSpeciall(String number) {
		String result = number;
		if (result != null) {
			result = result.replaceAll("-", "");
			result = result.replaceAll(" ", "");
			result = result.replaceAll("\\+86", "");
			if (result.startsWith("12520")) {
				// ���Ƿ��Ŷ���
				result = result.replace("12520", "");
			}
		}

		return result;
	}
}
