package com.zgy.ringforu.tools.watermark;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class WaterMarkActivity extends Activity implements OnSeekBarChangeListener, OnClickListener {

	private static final String TAG = "WaterMarkActivity";

	// private Uri picUri;
	private String picPath;
	// private int picAlpha;
	private ImageView imgShow;
	private Button btnOk;
	private Button btnCancel;
	private Button btnCut;
	private Button btnDel;
	private Button btnChange;
	private Button btnOrientation;
	private TextView textSeekbar;
	private LinearLayout layoutOperas;
	private LinearLayout layoutChangeBg;
	private RelativeLayout layoutMain;
	private TextView textChangeTip;

	private SeekBar seekbarAlpha;

	private static final int REQUEST_CUTPIC = 101;
	private static final int REQUEST_PICKPIC = 102;

	// private File FILE_MARK = WaterMarkUtil.FILEPATH_WATERMARK;
	// private File FILE_MARK_TEMP = new File(WaterMarkUtil.FILEPATH_WATERMARK_TEMP);
	// private File FILE_ALPHA = new File(WaterMarkUtil.FILEPATH_WATERMARK_ALPHA);

	private Vibrator vb = null;
	private String[] arrayBbColors;
	private String[] arrayTextColors;
	private int currentBgColor = 0;

	private Uri tempPickPicUri = null;

	// private float screenWidth;
	// private float screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tools_watermark);

		// DisplayMetrics metric = getResources().getDisplayMetrics();
		// screenWidth = metric.widthPixels;
		// screenHeight = metric.heightPixels;
		// Log.v(TAG, "get screen " + screenWidth + "x" + screenHeight);
		vb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		arrayBbColors = getResources().getStringArray(R.array.bg_markwater);
		arrayTextColors = getResources().getStringArray(R.array.color_markwater_text);
		imgShow = (ImageView) findViewById(R.id.img_watermark);
		seekbarAlpha = (SeekBar) findViewById(R.id.seekbar_watermark);
		seekbarAlpha.setMax(200);
		btnOk = (Button) findViewById(R.id.btn_watermark_ok);
		btnChange = (Button) findViewById(R.id.btn_watermark_change);
		btnDel = (Button) findViewById(R.id.btn_watermark_delete);
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
		// seekbarAlpha.setBackgroundResource(R.drawable.seekbar_line_back);
		// seekbarAlpha.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_line_top));
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnCut.setOnClickListener(this);
		btnChange.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnOrientation.setOnClickListener(this);
		layoutChangeBg.setOnClickListener(this);

		Intent intent = getIntent();
		String action = intent.getAction();
		// 共享获得的图片
		if (Intent.ACTION_SEND.equals(action) && intent.hasExtra(Intent.EXTRA_STREAM)) {
			String type = intent.getType();
			Uri stream = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
			if (stream != null && type != null) {
				startPhotoZoom(stream);
				return;
			}
		}
		refreshViews();
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

		if (PhoneUtil.bIsVerbOn) {
			vb.vibrate(new long[] { 0, 20 }, -1);
		}

		switch (v.getId()) {
		case R.id.btn_watermark_ok:
			if (WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.exists()) {
				WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.delete();
			}
			if (FileUtil.save(WaterMarkUtil.FILE_WATERMARK_ALPHA, seekbarAlpha.getProgress() + "", WaterMarkActivity.this)) {
				MyToast.makeText(WaterMarkActivity.this, R.string.watermark_set_success, Toast.LENGTH_SHORT, false).show();
			}

			finish();
			break;

		case R.id.btn_watermark_delete:

			if (WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.exists()) {
				WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.delete();
			}

			if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
				WaterMarkUtil.FILE_WATERMARK_IMG.delete();
			}

			if (WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE.exists()) {
				WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE.delete();
			}

			MyToast.makeText(WaterMarkActivity.this, R.string.watermark_del_success, Toast.LENGTH_SHORT, false).show();
			finish();
			break;

		case R.id.btn_watermark_change:
			pickPic();
			break;
		case R.id.btn_watermark_cancel:
			finish();
			break;
		case R.id.btn_watermark_cut:
			Uri uriMark = Uri.fromFile(WaterMarkUtil.FILE_WATERMARK_IMG);
			startPhotoZoom(uriMark);
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
				MyToast.makeText(WaterMarkActivity.this, R.string.watermark_ori_tip, Toast.LENGTH_LONG, false).show();
			}
			break;

		case R.id.layout_watermark_changebg:
			setBgAndTextColor();
			break;
		default:
			break;
		}

	}

	private void setBgAndTextColor() {
		if (currentBgColor == arrayBbColors.length) {
			currentBgColor = 0;
		}
		// layoutMain.setAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.alpha_in_long));
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
				if (PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
				clearWaterMark();
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, REQUEST_PICKPIC);
			}
		}).setNegativeButton(R.string.watermark_camera, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				if (PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
				clearWaterMark();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				tempPickPicUri = getOutputImageFileUri();
				intent.putExtra(MediaStore.EXTRA_OUTPUT, tempPickPicUri);

				startActivityForResult(intent, REQUEST_PICKPIC);
			}
		}).create().show();
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri cutFileUri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html 直接在里面Ctrl+F搜：CROP
		 * ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++ 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */

		Uri uriTemp = Uri.fromFile(WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE);
		if (WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE.exists()) {
			WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE.delete();
		}

		Intent intent = new Intent("com.android.camera.action.CROP");

		// Bitmap bitmap = intt.getParcelableExtra("data");
		// if(bitmap != null){
		// intent.setType("image/*");
		// intent.putExtra("data", bitmap);
		// }else{
		// intent.setDataAndType(intt.getData(), "image/*");
		// }

		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(cutFileUri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		DisplayMetrics metric = getResources().getDisplayMetrics();
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", metric.widthPixels);
		intent.putExtra("aspectY", metric.heightPixels);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", metric.widthPixels);
		intent.putExtra("outputY", metric.heightPixels);
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTemp);
		startActivityForResult(intent, REQUEST_CUTPIC);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CUTPIC:
			if (data != null) {
				try {
					if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
						WaterMarkUtil.FILE_WATERMARK_IMG.delete();
					}
					FileUtil.copyFileTo(WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE, WaterMarkUtil.FILE_WATERMARK_IMG);
					// if (FileUtil.copyFileTo(WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE,
					// WaterMarkUtil.FILE_WATERMARK_IMG)) {
					// imgShow.setImageDrawable(new BitmapDrawable(Globle.FILEPATH_WATERMARK));
					// refreshViews();
					// }
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			refreshViews();
			break;
		case REQUEST_PICKPIC:
			if (data != null) {
				Uri picPath = data.getData();
				Log.e(TAG, "PIC uri=" + picPath);

				if (picPath != null) {
					startPhotoZoom(picPath);
				} else {

					Log.e(TAG, "ERROR!!!" + "   null ");

					// Bundle extras = data.getExtras();
					// if (extras != null) {
					// // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取 Bitmap图片
					// Bitmap image = extras.getParcelable("data");
					// if (image != null) {
					// }
					// }
				}

			} else {
				if (!WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
					pickPic();
				}
			}

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

	// 如何将一个路径转换为一个uri
	private Uri getOutputImageFileUri() {
		Uri uri = Uri.fromFile(getOutputImageFile());
		// 留意一下这个文件路径是按照怎样的规则转换为一个uri的
		Log.v(TAG, "根据路径转换的uri为：" + uri.toString());
		return uri;
	}

	// 创建文件路径
	private File getOutputImageFile() {
		File mediaFile = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File mediaDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "myCamara");
			Log.v(TAG, "存储路径目录：" + mediaDir.getAbsolutePath());
			if (!mediaDir.exists()) {
				if (!mediaDir.mkdirs())
					Log.v(TAG, "存储路径目录创建失败");
				return null;
			}
			// 利用时间戳作为文件名
			String timeStamp = (new SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
			mediaFile = new File(mediaDir.getAbsoluteFile() + File.separator + "IMG_" + timeStamp + ".jpg");
			Log.v(TAG, "文件存储路径为：" + mediaFile.getAbsolutePath());
		}

		return mediaFile;
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

		if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
			WaterMarkUtil.ctrlWaterMarkBackService(WaterMarkActivity.this, false);
			imgShow.setImageDrawable(new BitmapDrawable(WaterMarkUtil.FILE_WATERMARK_IMG.getAbsolutePath()));
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
		if (WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.exists()) {
			int alpahSaved = Integer.parseInt(FileUtil.load(WaterMarkUtil.FILE_WATERMARK_ALPHA, WaterMarkActivity.this, false));
			seekbarAlpha.setProgress(alpahSaved);
			imgShow.setAlpha(alpahSaved);
		} else {
			imgShow.setAlpha(50);
			seekbarAlpha.setProgress(50);
		}

	}

	@Override
	protected void onDestroy() {
		// WaterMarkService.show = true;
		// WaterMarkUtil.ctrlWaterMarkBackService(WaterMarkActivity.this, true);
		WaterMarkUtil.checkWaterMarkState(WaterMarkActivity.this);
		if (WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE.exists()) {
			WaterMarkUtil.FILE_WATERMARK_TEMP_IMAGE.delete();
		}
		super.onDestroy();
	}

}
