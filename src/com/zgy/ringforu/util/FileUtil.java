package com.zgy.ringforu.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.zgy.ringforu.MainCanstants;

public class FileUtil {

	private static final String TAG = "FileUtil";

	private static final String TEXT_ENCODING = "utf-8";

	// /**
	// * �� file ��д����
	// *
	// * @param fileName
	// * @param toSave
	// * @return
	// * @throws Exception
	// */
	// public static boolean save(String fileName, String toSave, Context con)
	// throws Exception {
	// Properties properties = new Properties();
	//
	// properties.put("string", toSave);
	// // try {
	// FileOutputStream stream = con.openFileOutput(fileName,
	// Context.MODE_WORLD_WRITEABLE);
	// properties.store(stream, "string");
	// // } catch (FileNotFoundException e) {
	// // throw e;
	// // } catch (IOException e) {
	// // throw e;
	// // }
	//
	// return true;
	// }
	//
	// /**
	// * ��ȡ file �е�����
	// *
	// * @param fileName
	// * @return
	// */
	// public static String load(String fileName, Context con) throws Exception
	// {
	// Properties properties = new Properties();
	// // try {
	// FileInputStream stream = con.openFileInput(fileName);
	// properties.load(stream);
	// // } catch (FileNotFoundException e) {
	// // return null;
	// // } catch (IOException e) {
	// // return null;
	// // }
	//
	// String result = null;
	// result = properties.get("string").toString();
	//
	// LogRingForu.v(TAG, "��ȡ����" + result);
	// return result;
	// }

	/**
	 * д���ļ�
	 * 
	 * @param str
	 *            д����ַ���
	 * @param file
	 *            �ļ�·��
	 * @param add
	 *            ׷�����
	 * @return
	 */
	public static boolean write(String str, File file, boolean add) {
		Log.e("writeFile", str);
		FileOutputStream out;
		try {
			if (!file.exists()) {
				// �����ļ�
				file.createNewFile();
			}

			// ���ļ�file��OutputStream
			out = new FileOutputStream(file, add);
			String infoToWrite = str;
			// ���ַ���ת����byte����д���ļ�
			out.write(infoToWrite.getBytes(TEXT_ENCODING));
			// �ر��ļ�file��OutputStream
			out.close();

		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * ��utf-8�����ȡ�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static String read(File file) {
		String str = "";
		FileInputStream in;
		try {
			// ���ļ�file��InputStream
			in = new FileInputStream(file);
			// ���ļ�����ȫ�����뵽byte����
			int length = (int) file.length();
			byte[] temp = new byte[length];
			in.read(temp, 0, length);
			// ��byte������UTF-8���벢����display�ַ�����
			str = EncodingUtils.getString(temp, TEXT_ENCODING);
			// �ر��ļ�file��InputStream

			in.close();
		} catch (IOException e) {
		}

		return str;
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

	/**
	 * �ݹ�ɾ��ĳĿ¼�����������ļ�����Ŀ¼
	 * 
	 * @Description:
	 * @param dir
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-7-24
	 */
	public static boolean delFileDir(File dir) {
		if (dir == null || !dir.exists()) {
			return false;
		}
		if (dir.isFile()) {
			dir.delete();
		} else {
			File[] listFiles = dir.listFiles();
			if (listFiles == null || listFiles.length == 0) {
				dir.delete();
			} else {
				for (File file : listFiles) {
					if (file.isFile()) {
						file.delete();
					} else if (file.isDirectory()) {
						delFileDir(file);
					}
				}
			}
		}

		return true;
	}

	/**
	 * ��ȡͼƬ�ľ���·��
	 * 
	 * @param uri
	 * @return
	 */
	public static String getImageAbsolutePath(Context context, Uri uri) {
		String result = uri.toString();
		try {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = ((Activity) context).managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ��ջ��棨�����ļ���
	 */
	public static boolean clearData() {
		FileUtil.delFileDir(MainCanstants.getSdFile());
		return true;
		// if (FILE_SDCARD_IMPORTANT_NUM.exists()) {
		// FILE_SDCARD_IMPORTANT_NUM.delete();
		// }
		// if (FILE_SDCARD_IMPORTANT_NAME.exists()) {
		// FILE_SDCARD_IMPORTANT_NAME.delete();
		// }
		// if (FILE_SDCARD_CALL_NUM.exists()) {
		// FILE_SDCARD_CALL_NUM.delete();
		// }
		// if (FILE_SDCARD_CALL_NAME.exists()) {
		// FILE_SDCARD_CALL_NAME.delete();
		// }
		// if (FILE_SDCARD_SMS_NUM.exists()) {
		// FILE_SDCARD_SMS_NUM.delete();
		// }
		// if (FILE_SDCARD_SMS_NAME.exists()) {
		// FILE_SDCARD_SMS_NAME.delete();
		// }
	}
}
