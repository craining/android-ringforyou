package com.zgy.ringforu.tools.watermark;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.view.MyToast;

public class WaterMarkService extends Service {

	private static final String TAG = "WaterMarkService";

	private WindowManager wm = null;
	private WindowManager.LayoutParams wmParams = null;
	private View view;

	public static boolean show = true;

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);

		Log.e(TAG, "WaterMarkService is start!");
		if (show) {

			if (WaterMarkUtil.isWaterMarkOn()) {
				if (wm == null && view == null) {
					try {
						createView();
					} catch (Exception e) {
						MyToast.makeText(WaterMarkService.this, R.string.watermark_show_fail, Toast.LENGTH_SHORT, true).show();
					}
				}

			} else {
				stopSelf();
			}

		} else {
			stopSelf();
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	private void createView() {
		// Log.v(TAG, "set View ");

		// ����LayoutParams(ȫ�ֱ�������ز���
		view = LayoutInflater.from(this).inflate(R.layout.watermark_floating, null);
		wm = (WindowManager) getApplicationContext().getSystemService("window");
		wmParams = ((MainApplication) getApplication()).getMywmParams();
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;// �������ṩ���û���������������Ӧ�ó����Ϸ���������״̬������
		wmParams.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;// �������κΰ����¼�
		// wmParams.gravity = Gravity.LEFT | Gravity.TOP; //
		// �����������������Ͻ�WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES |
		// WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
		wmParams.gravity = Gravity.FILL;

		// Rect frame = new Rect();
		// Window w = (Window) getApplicationContext().getSystemService("window");
		// w.getDecorView().getWindowVisibleDisplayFrame(frame);
		// Log.v(TAG, "getWindowVisibleDisplayFrame " + frame.top);
		// ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
		// wmParams.x = 0;
		// wmParams.y = 0;
		// �����������ڳ�������
		// wmParams.width = WindowManager.LayoutParams.FILL_PARENT;
		// wmParams.height = WindowManager.LayoutParams.FILL_PARENT;
		wmParams.format = PixelFormat.RGBA_8888;// ͸��
		ImageView imgShow = (ImageView) view.findViewById(R.id.watermark_image);

		int alpha = 0;
		alpha = Integer.parseInt(FileUtil.load(WaterMarkUtil.FILE_WATERMARK_ALPHA, WaterMarkService.this, false));
		// Log.v(TAG, "set alpha " + alpha);
		imgShow.setAlpha(alpha);

		// if (new File(WaterMarkUtil.FILEPATH_WATERMARK).exists()) {
		// imgShow.setImageDrawable(new
		// BitmapDrawable(Globle.FILEPATH_WATERMARK));
		Bitmap bitmap = BitmapFactory.decodeFile(WaterMarkUtil.FILE_WATERMARK_IMG.getAbsolutePath());
		imgShow.setImageBitmap(bitmap);
		// } else {
		// imgShow.setImageDrawable(null);
		// stopSelf();
		// }
		wm.addView(view, wmParams);
	}

	// private void updateViewPosition() {
	// // ���¸�������λ�ò���
	// wmParams.x = (int) (x - mTouchStartX);
	// wmParams.y = (int) (y - mTouchStartY);
	// wm.updateViewLayout(view, wmParams);
	// }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "WaterMarkService is destroyed!");
		
		if (wm != null && view != null) {
			if (view.isShown())
				wm.removeView(view);
			wm = null;
			view = null;
		}
	}
}