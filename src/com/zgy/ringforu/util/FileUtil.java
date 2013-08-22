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
	 * 向 file 中写数据
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
	 * 读取 file 中的数据
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
		Log.v(TAG, "读取内容" + result);
		return result;
	}

	/**
	 * 拷贝目录下的所有文件到指定目录
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
			return false;// 判断是否是目录
		}
		File[] srcFiles = srcDir.listFiles();
		for (int i = 0; i < srcFiles.length; i++) {
			if (srcFiles[i].isFile()) {
				// 获得目标文件
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
	 * 拷贝一个文件,srcFile源文件，destFile目标文件
	 * 
	 * @param srcFile
	 * @param destFile
	 * @return
	 * @throws IOException
	 */
	public static boolean copyFileTo(File srcFile, File destFile) throws IOException {
		if (srcFile.isDirectory() || destFile.isDirectory()) {
			return false;// 判断是否是文件
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
