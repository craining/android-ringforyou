package com.zgy.ringforu.tools.watermark;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.R;
import com.zgy.ringforu.RingForU;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.util.BitmapUtil;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.MainUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.TimeUtil;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class WaterMarkActivity extends Activity implements OnSeekBarChangeListener, OnClickListener {

	private static final String TAG = "WaterMarkActivity";

	// private Uri picUri;
	// private String picPath;
	// private int picAlpha;
	private ImageView imgShow;
	private Button btnOk;
	private Button btnCancel;
	private Button btnCut;
	private Button btnDel;
	private Button btnTitleClose;
	private Button btnChange;
	private Button btnOrientation;
	private TextView textSeekbar;
	private LinearLayout layoutOperas;
	private LinearLayout layoutChangeBg;
	private RelativeLayout layoutMain;
	private TextView textChangeTip;

	private SeekBar seekbarAlpha;

	private static final int REQUEST_CUTPIC = 101;
	private static final int REQUEST_PICKPIC_CAMERA = 102;
	private static final int REQUEST_PICKPIC_GALLERY = 103;

	private Vibrator vb = null;
	private String[] arrayBbColors;
	private String[] arrayTextColors;
	private int currentBgColor = 0;

	private File tempFileSrc;
	private File tempFileCutted;

	private MainConfig mMainConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tools_watermark);
		mMainConfig = MainConfig.getInstance();

		// DisplayMetrics metric = getResources().getDisplayMetrics();
		// screenWidth = metric.widthPixels;
		// screenHeight = metric.heightPixels;
		// LogRingForu.v(TAG, "get screen " + screenWidth + "x" + screenHeight);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		arrayBbColors = getResources().getStringArray(R.array.bg_markwater);
		arrayTextColors = getResources().getStringArray(R.array.color_markwater_text);
		imgShow = (ImageView) findViewById(R.id.img_watermark);
		seekbarAlpha = (SeekBar) findViewById(R.id.seekbar_watermark);
		seekbarAlpha.setMax(200);
		btnOk = (Button) findViewById(R.id.btn_watermark_ok);
		btnChange = (Button) findViewById(R.id.btn_watermark_change);
		btnDel = (Button) findViewById(R.id.btn_watermark_delete);
		btnTitleClose = (Button) findViewById(R.id.btn_watermark_title_close);
		btnCancel = (Button) findViewById(R.id.btn_watermark_cancel);
		btnCut = (Button) findViewById(R.id.btn_watermark_cut);
		btnOrientation = (Button) findViewById(R.id.btn_watermark_orientation);
		textSeekbar = (TextView) findViewById(R.id.text_watermark);
		layoutOperas = (LinearLayout) findViewById(R.id.layout_watermark_operas);
		layoutChangeBg = (LinearLayout) findViewById(R.id.layout_watermark_changebg);
		layoutMain = (RelativeLayout) findViewById(R.id.layout_watermark_main);
		textChangeTip = (TextView) findViewById(R.id.text_watermark_changetip);
		setBgAndTextColor();
		seekbarAlpha.setOnSeekBarChangeListener(this);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnCut.setOnClickListener(this);
		btnChange.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnTitleClose.setOnClickListener(this);
		btnOrientation.setOnClickListener(this);
		layoutChangeBg.setOnClickListener(this);

		refreshViews();

		Intent intent = getIntent();
		String action = intent.getAction();
		// 共享获得的图片
		if (Intent.ACTION_SEND.equals(action) && intent.hasExtra(Intent.EXTRA_STREAM)) {
			String type = intent.getType();
			Uri stream = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
			if (stream != null && type != null) {
				startPhotoZoom(stream);
			}
		}
		// else {
		// Bundle b = intent.getExtras();
		// if (b != null && b.containsKey("fromnotifybar") &&
		// b.getBoolean("fromnotifybar")) {
		// // 从通知栏跳转过来的
		// fromNotifBar = true;
		// btnTitleClose.setVisibility(View.VISIBLE);
		// btnOk.setVisibility(View.GONE);
		// }
		// }
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		imgShow.setAlpha(arg1);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(vb);
		switch (v.getId()) {
		case R.id.btn_watermark_ok:
			open();
			finish();
			break;

		case R.id.btn_watermark_delete:
			// 移除
			WaterMarkUtil.setSwitchOnOff(false);

			if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
				WaterMarkUtil.FILE_WATERMARK_IMG.delete();
			}
			WaterMarkUtil.checkWaterMarkState(WaterMarkActivity.this);
			MyToast.makeText(WaterMarkActivity.this, R.string.watermark_del_success, MyToast.LENGTH_SHORT, false).show();
			finish();
			break;
		case R.id.btn_watermark_title_close:
			// 关闭
			WaterMarkUtil.setSwitchOnOff(false);
			WaterMarkUtil.checkWaterMarkState(WaterMarkActivity.this);
			MyToast.makeText(WaterMarkActivity.this, R.string.watermark_close_finish, MyToast.LENGTH_SHORT, false).show();
			finish();
			break;
		case R.id.btn_watermark_change:
			pickPic();
			break;
		case R.id.btn_watermark_cancel:
			finish();
			break;
		case R.id.btn_watermark_cut:

			if (PhoneUtil.existSDcard()) {
				try {
					tempFileSrc = new File(WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_SRC + TimeUtil.getCurrentTimeMillis());
					FileUtil.copyFileTo(WaterMarkUtil.FILE_WATERMARK_IMG, tempFileSrc);
					Uri uriMark = Uri.fromFile(tempFileSrc);
					startPhotoZoom(uriMark);
				} catch (IOException e) {
					e.printStackTrace();
					MyToast.makeText(WaterMarkActivity.this, R.string.watermark_cannot_pick, MyToast.LENGTH_SHORT, true).show();
				}

			} else {
				MyToast.makeText(WaterMarkActivity.this, R.string.watermark_cannot_pick, MyToast.LENGTH_SHORT, true).show();
			}

			break;
		case R.id.btn_watermark_orientation:
			if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				btnOrientation.setText(R.string.watermark_orientation_portrait);
			} else {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				btnOrientation.setText(R.string.watermark_orientation_landscape);
			}

			if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
				MyToast.makeText(WaterMarkActivity.this, R.string.watermark_ori_tip, MyToast.LENGTH_LONG, false).show();
			}
			break;

		case R.id.layout_watermark_changebg:
			setBgAndTextColor();
			break;
		default:
			break;
		}

	}

	private void open() {
		mMainConfig.setWaterMarkAlpha(seekbarAlpha.getProgress());
		MyToast.makeText(WaterMarkActivity.this, R.string.watermark_set_success, MyToast.LENGTH_SHORT, false).show();
		WaterMarkUtil.setSwitchOnOff(true);
	}

	private void setBgAndTextColor() {
		if (currentBgColor == arrayBbColors.length) {
			currentBgColor = 0;
		}
		// layoutMain.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),
		// R.anim.alpha_in_long));
		layoutMain.setBackgroundColor(Color.parseColor(arrayBbColors[currentBgColor]));
		textChangeTip.setTextColor(Color.parseColor(arrayTextColors[currentBgColor]));
		textSeekbar.setTextColor(Color.parseColor(arrayTextColors[currentBgColor]));
		currentBgColor++;
	}

	/**
	 * 选取图片
	 */
	private void pickPic() {
		// 选择图库和或相机
		// 清空重要联系人
		MyDialog.Builder builder = new MyDialog.Builder(WaterMarkActivity.this);
		builder.setTitle(R.string.watermark_select_tip).setMessage(R.string.watermark_select_str).setPositiveButton(R.string.watermark_album, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				PhoneUtil.doVibraterNormal(vb);
				clearWaterMark();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, REQUEST_PICKPIC_GALLERY);
			}
		}).setNegativeButton(R.string.watermark_camera, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				PhoneUtil.doVibraterNormal(vb);
				if (PhoneUtil.existSDcard()) {
					dialog.dismiss();
					clearWaterMark();
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					tempFileSrc = new File(WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_SRC + TimeUtil.getCurrentTimeMillis());
					File mediaDir = tempFileSrc.getParentFile();
					if (!mediaDir.exists()) {
						mediaDir.mkdirs();
					}

					Uri tempTakePicUri = Uri.fromFile(tempFileSrc);
					// 留意一下这个文件路径是按照怎样的规则转换为一个uri的
					if (RingForU.DEBUG)
						LogRingForu.v(TAG, "根据路径转换的uri为：" + tempTakePicUri.toString());
					intent.putExtra(MediaStore.EXTRA_OUTPUT, tempTakePicUri);

					startActivityForResult(intent, REQUEST_PICKPIC_CAMERA);
				} else {
					MyToast.makeText(WaterMarkActivity.this, R.string.watermark_cannot_pick, MyToast.LENGTH_SHORT, true).show();
				}

			}
		}).create().show();
	}

	// private void cropImageUri(Uri uri, int outputX, int outputY, int
	// requestCode){
	// Intent intent = new Intent("com.android.camera.action.CROP");
	// intent.setDataAndType(uri, "image/*");
	// intent.putExtra("crop", "true");
	// intent.putExtra("aspectX", 2);
	// intent.putExtra("aspectY", 1);
	// intent.putExtra("outputX", outputX);
	// intent.putExtra("outputY", outputY);
	// intent.putExtra("scale", true);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	// intent.putExtra("return-data", false);
	// intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	// intent.putExtra("noFaceDetection", true); // no face detection
	// startActivityForResult(intent, requestCode);
	// }

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri cutFileUri) {

		tempFileCutted = new File(WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CUT + TimeUtil.getCurrentTimeMillis());

		Uri uriTemp = Uri.fromFile(tempFileCutted);
		if (RingForU.DEBUG)
			LogRingForu.e(TAG, "after cut uriTemp =" + uriTemp.toString() + "  src uri = " + cutFileUri.toString());

		//
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT, cutFileUri);
		// intent.setData(cutFileUri);
		// intent.setType("image/*");
		// intent.putExtra("crop", "true");
		// DisplayMetrics metric = getResources().getDisplayMetrics();
		// // aspectX aspectY 是宽高的比例
		// intent.putExtra("aspectX", metric.widthPixels);
		// intent.putExtra("aspectY", metric.heightPixels);
		// // outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", metric.widthPixels);
		// intent.putExtra("outputY", metric.heightPixels);
		// intent.putExtra("noFaceDetection", true);
		// intent.putExtra("scale", true);
		// intent.putExtra("return-data", false);
		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTemp);
		// intent.putExtra("outputFormat",
		// Bitmap.CompressFormat.JPEG.toString());

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(cutFileUri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		DisplayMetrics metric = getResources().getDisplayMetrics();
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", metric.widthPixels);
		intent.putExtra("aspectY", metric.heightPixels);
		// // outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", metric.widthPixels);
		// intent.putExtra("outputY", metric.heightPixels);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTemp);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);

		startActivityForResult(intent, REQUEST_CUTPIC);
	}

	// private void cropImageUri(Uri uri, int outputX, int outputY){
	// Intent intent = new Intent("com.android.camera.action.CROP");
	// intent.setDataAndType(uri, "image/*");
	// intent.putExtra("crop", "true");
	// intent.putExtra("aspectX", 2);
	// intent.putExtra("aspectY", 1);
	// intent.putExtra("outputX", outputX);
	// intent.putExtra("outputY", outputY);
	// intent.putExtra("scale", true);
	// // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
	// intent.putExtra(MediaStore.EXTRA_OUTPUT,
	// Uri.parse("file:///sdcard/result.jpg"));
	// intent.putExtra("return-data", false);
	// intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
	// intent.putExtra("noFaceDetection", true); // no face detection
	// startActivityForResult(intent, 0);
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CUTPIC:
			if (tempFileCutted.exists()) {

				try {
					if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
						WaterMarkUtil.FILE_WATERMARK_IMG.delete();
					}
					if (tempFileSrc != null && tempFileSrc.exists()) {
						tempFileSrc.delete();
					}
					FileUtil.copyFileTo(tempFileCutted, WaterMarkUtil.FILE_WATERMARK_IMG);
					// tempFileCutted.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				MyToast.makeText(WaterMarkActivity.this, "剪裁图片失败！", MyToast.LENGTH_SHORT, true).show();
			}
			refreshViews();
			break;
		case REQUEST_PICKPIC_CAMERA:

			if (tempFileSrc.exists()) {
				startPhotoZoom(Uri.fromFile(tempFileSrc));
			} else {
				MyToast.makeText(WaterMarkActivity.this, "获取图片失败！", MyToast.LENGTH_SHORT, true).show();
			}
			refreshViews();
			break;

		case REQUEST_PICKPIC_GALLERY:
			if (data != null) {
				Uri picPath = data.getData();
				if (RingForU.DEBUG)
					LogRingForu.e(TAG, "PIC uri=" + picPath);
				if (picPath != null) {
					File getFileSrc = new File(getImageAbsolutePath(picPath));

					try {
						tempFileSrc = new File(WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_SRC + TimeUtil.getCurrentTimeMillis());
						FileUtil.copyFileTo(getFileSrc, tempFileSrc);
						startPhotoZoom(Uri.fromFile(tempFileSrc));
						// cropImageUri(Uri.fromFile(tempFileSrc), 2000, 500);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
			MyToast.makeText(WaterMarkActivity.this, "获取图片失败！", MyToast.LENGTH_SHORT, true).show();
			refreshViews();

			break;
		default:
			break;
		}

	}

	/**
	 * 获取图片的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	private String getImageAbsolutePath(Uri uri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}

	/**
	 * 清除临时水印
	 */
	private void clearWaterMark() {
		WaterMarkService.show = false;
		WaterMarkUtil.ctrlWaterMarkBackService(WaterMarkActivity.this, true);
	}

	/**
	 * 刷新当前界面
	 */
	private void refreshViews() {
		// 显示水印到view
		if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
			WaterMarkUtil.ctrlWaterMarkBackService(WaterMarkActivity.this, false);
			DisplayMetrics metric = getResources().getDisplayMetrics();
			imgShow.setImageBitmap(BitmapUtil.readBitmapAutoSize(WaterMarkUtil.FILE_WATERMARK_IMG.getAbsolutePath(), metric.widthPixels, metric.heightPixels));
			imgShow.setScaleType(ScaleType.FIT_XY);
			btnOk.setVisibility(View.VISIBLE);
			btnCut.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.VISIBLE);
			// btnCancel.setVisibility(View.VISIBLE);
			seekbarAlpha.setVisibility(View.VISIBLE);
			textSeekbar.setVisibility(View.VISIBLE);
			btnChange.setText(R.string.watermark_change);
			// textChangeTip.setVisibility(View.VISIBLE);
			layoutChangeBg.setVisibility(View.VISIBLE);
		} else {
			btnOk.setVisibility(View.GONE);
			btnCut.setVisibility(View.GONE);
			btnDel.setVisibility(View.GONE);
			// btnCancel.setVisibility(View.GONE);
			seekbarAlpha.setVisibility(View.GONE);
			textSeekbar.setVisibility(View.GONE);
			btnChange.setText(R.string.watermark_select_tip);
			// textChangeTip.setVisibility(View.GONE);
			layoutChangeBg.setVisibility(View.GONE);
			pickPic();
		}
		// 控制透明度
		int alpha = mMainConfig.getWaterMarkAlpha();
		imgShow.setAlpha(alpha);
		seekbarAlpha.setProgress(alpha);
		if (WaterMarkUtil.isWaterMarkSeted()) {
			btnTitleClose.setVisibility(View.VISIBLE);
			// btnOk.setVisibility(View.GONE);
		} else {
			btnTitleClose.setVisibility(View.GONE);
			// btnOk.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		// WaterMarkService.show = true;
		// WaterMarkUtil.ctrlWaterMarkBackService(WaterMarkActivity.this, true);
		WaterMarkUtil.checkWaterMarkState(WaterMarkActivity.this);
		// if (WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE.exists()) {
		// WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE.delete();
		// }
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		if (PhoneUtil.existSDcard()) {
			File f = new File(MainUtil.FILE_IN_SDCARD);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		WaterMarkService.show = true;
		WaterMarkUtil.ctrlWaterMarkBackService(WaterMarkActivity.this, true);
		super.onPause();
	}

}
