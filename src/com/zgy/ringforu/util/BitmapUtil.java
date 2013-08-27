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
	 * 节省内存
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
		// 设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度
		// BitmapFactory.decodeFile(file, opt);
		// opt.inDither = false;
		// opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// opt.inJustDecodeBounds = false;// 最后把标志复原
		return opt;
	}

	/**
	 * 获得圆角图片
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

		// 若读取图片的宽度或高度小于ImageView的宽度或高度，则对图片进行放大
		if (w < width || h < height) {
			Matrix matrix = new Matrix();
			matrix.postScale((float) width / w, (float) height / h); // 长和宽放大缩小的比例
			// bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
			// false);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		}
		if (RingForU.DEBUG) {
			Log.e("", "w = " + w + "  h=" + h + " (float) width / w" + (float) width / w + "   (float) height / h=" + (float) height / h);
			Log.e("", "bitmap.w=" + bitmap.getWidth() + " h=" + bitmap.getHeight());
		}

		// // GoOutDebug.e(TAG, "w = " + output.getWidth() + "   h = " + output.getHeight());
		// // 创建一个新的bitmap，然后在bitmap里创建一个圆角画布，将之前的图片画在里面。
		// Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// Canvas canvas = new Canvas(output);
		// final int color = 0xff424242;
		// final Paint paint = new Paint();
		// final Rect rect = new Rect(0, 0, width, height);
		// final RectF rectF = new RectF(rect);
		// paint.setAntiAlias(true);
		// canvas.drawARGB(0, 0, 0, 0);
		// paint.setColor(color);
		// canvas.drawRoundRect(rectF, 10, 10, paint);// 圆角平滑度
		// paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// canvas.drawBitmap(bitmap, rect, rect, paint);

		return bitmap;
	}

}
