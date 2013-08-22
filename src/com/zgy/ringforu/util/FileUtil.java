package com.zgy.ringforu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

public class FileUtil {

	private static final String TAG = "FileUtil";

	/**
	 * �� file ��д����
	 * 
	 * @param fileName
	 * @param toSave
	 * @return
	 * @throws Exception
	 */
	public static boolean save(String fileName, String toSave, Context con) throws Exception {
		Properties properties = new Properties();

		properties.put("string", toSave);
		// try {
		FileOutputStream stream = con.openFileOutput(fileName, Context.MODE_WORLD_WRITEABLE);
		properties.store(stream, "string");
		// } catch (FileNotFoundException e) {
		// throw e;
		// } catch (IOException e) {
		// throw e;
		// }

		return true;
	}

	/**
	 * ��ȡ file �е�����
	 * 
	 * @param fileName
	 * @return
	 */
	public static String load(String fileName, Context con) throws Exception {
		Properties properties = new Properties();
		// try {
		FileInputStream stream = con.openFileInput(fileName);
		properties.load(stream);
		// } catch (FileNotFoundException e) {
		// return null;
		// } catch (IOException e) {
		// return null;
		// }

		String result = null;
		result = properties.get("string").toString();
		Log.v(TAG, "��ȡ����" + result);
		return result;
	}

	/**
	 * ����Ŀ¼�µ������ļ���ָ��Ŀ¼
	 * 
	 * @param srcDir
	 * @param destDir
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFilesTo(File srcDir, File destDir) throws IOException {
		if (!destDir.exists())
			destDir.mkdirs();
		if (!srcDir.isDirectory() || !destDir.isDirectory()) {
			return false;// �ж��Ƿ���Ŀ¼
		}
		File[] srcFiles = srcDir.listFiles();
		for (int i = 0; i < srcFiles.length; i++) {
			if (srcFiles[i].isFile()) {
				// ���Ŀ���ļ�
				File destFile = new File(destDir.getPath() + "/" + srcFiles[i].getName());
				copyFileTo(srcFiles[i], destFile);
			} else if (srcFiles[i].isDirectory()) {
				File theDestDir = new File(destDir.getPath() + "/" + srcFiles[i].getName());
				copyFilesTo(srcFiles[i], theDestDir);
			}
		}

		return true;
	}

	/**
	 * ����һ���ļ�,srcFileԴ�ļ���destFileĿ���ļ�
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFileTo(File srcFile, File destFile) throws IOException {
		if (srcFile.isDirectory() || destFile.isDirectory()) {
			return false;// �ж��Ƿ����ļ�
		}
		if (destFile.exists()) {
			destFile.delete();
		}
		FileInputStream fis = new FileInputStream(srcFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		int readLen = 0;
		byte[] buf = new byte[1024];
		while ((readLen = fis.read(buf)) != -1) {
			fos.write(buf, 0, readLen);
		}
		fos.flush();
		fos.close();
		fis.close();

		return true;
	}
}
