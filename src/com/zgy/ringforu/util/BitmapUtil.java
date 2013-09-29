package com.zgy.ringforu.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.bean.BitMapInfo;

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
	public static BitMapInfo readBitmapAutoSize(String filePath, int outWidth, int outHeight) {
		FileInputStream fs = null;
		BufferedInputStream bs = null;
		try {
			fs = new FileInputStream(filePath);
			bs = new BufferedInputStream(fs);
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

			// return BitmapFactory.decodeStream(bs, null, options);
			return getNewBitmap(BitmapFactory.decodeStream(bs, null, opt), outWidth, outHeight);
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
	private static BitMapInfo getNewBitmap(Bitmap bitmap, int width, int height) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// 若读取图片的宽度或高度小于ImageView的宽度或高度，则对图片进行放大
		// if (w < width || h < height) {
		Matrix matrix = new Matrix();
		float wScale = (float) width / w;
		float hScale = (float) height / h;
		matrix.postScale(wScale, hScale); // 长和宽放大缩小的比例

		// bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
		// bitmap.getHeight(), matrix,
		// false);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		// }
		LogRingForu.e("", "w = " + w + "  h=" + h + " (float) width / w" + (float) width / w + "   (float) height / h=" + (float) height / h);
		LogRingForu.e("", "bitmap.w=" + bitmap.getWidth() + " h=" + bitmap.getHeight());

		// // GoOutDebug.e(TAG, "w = " + output.getWidth() + "   h = " +
		// output.getHeight());
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

		BitMapInfo info = new BitMapInfo();
		info.setBitMap(bitmap);

		LogRingForu.e("", "wScale=" + wScale + "  hScale=" + hScale);

		// DecimalFormatfnum = new DecimalFormat("##0.00");
		// String dd=fnum.format(scale);

		BigDecimal ws = new BigDecimal(wScale);
		BigDecimal hs = new BigDecimal(hScale);
		
		if (ws.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue() - hs.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue() == 0) {
			// 长宽缩放比例变了，则图片比例变了
			info.setScalechanged(false);
		} else {
			info.setScalechanged(true);
		}

		return info;
	}
}
