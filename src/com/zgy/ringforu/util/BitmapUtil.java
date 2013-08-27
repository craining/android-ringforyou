package com.zgy.ringforu.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import com.zgy.ringforu.RingForU;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

public class BitmapUtil {

	/**
	 * ��ʡ�ڴ�
	 * 
	 * @Description:
	 * @param filePath
	 * @param outWidth
	 * @param outHeight
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-12
	 */
	public static Bitmap readBitmapAutoSize(String filePath, int outWidth, int outHeight) {
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
			BitmapFactory.Options options = setBitmapOption(filePath);
			// return BitmapFactory.decodeStream(bs, null, options);
			return getNewBitmap(BitmapFactory.decodeStream(bs, null, options), outWidth, outHeight);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bs.close();
				fs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static BitmapFactory.Options setBitmapOption(String file) {
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;

		// opt.inJustDecodeBounds = true;
		// ����ֻ�ǽ���ͼƬ�ı߾࣬�˲���Ŀ���Ƕ���ͼƬ��ʵ�ʿ�Ⱥ͸߶�
		// BitmapFactory.decodeFile(file, opt);
		// opt.inDither = false;
		// opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// opt.inJustDecodeBounds = false;// ���ѱ�־��ԭ
		return opt;
	}

	/**
	 * ���Բ��ͼƬ
	 * 
	 * @Description:
	 * @param bitmap
	 * @param roundPx
	 * @return
	 * @see:
	 * @since:
	 * @author: zhuanggy
	 * @date:2013-3-14
	 */
	private static Bitmap getNewBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// ����ȡͼƬ�Ŀ�Ȼ�߶�С��ImageView�Ŀ�Ȼ�߶ȣ����ͼƬ���зŴ�
		if (w < width || h < height) {
			Matrix matrix = new Matrix();
			matrix.postScale((float) width / w, (float) height / h); // ���Ϳ�Ŵ���С�ı���
			// bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
			// false);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		}
		if (RingForU.DEBUG) {
			Log.e("", "w = " + w + "  h=" + h + " (float) width / w" + (float) width / w + "   (float) height / h=" + (float) height / h);
			Log.e("", "bitmap.w=" + bitmap.getWidth() + " h=" + bitmap.getHeight());
		}

		// // GoOutDebug.e(TAG, "w = " + output.getWidth() + "   h = " + output.getHeight());
		// // ����һ���µ�bitmap��Ȼ����bitmap�ﴴ��һ��Բ�ǻ�������֮ǰ��ͼƬ�������档
		// Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// Canvas canvas = new Canvas(output);
		// final int color = 0xff424242;
		// final Paint paint = new Paint();
		// final Rect rect = new Rect(0, 0, width, height);
		// final RectF rectF = new RectF(rect);
		// paint.setAntiAlias(true);
		// canvas.drawARGB(0, 0, 0, 0);
		// paint.setColor(color);
		// canvas.drawRoundRect(rectF, 10, 10, paint);// Բ��ƽ����
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas.drawBitmap(bitmap, rect, rect, paint);

		return bitmap;
	}

}
