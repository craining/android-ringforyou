package com.zgy.ringforu.activity;

import java.io.File;
import java.io.IOException;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.Uri;
import android.os.Bundle;
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
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.R;
import com.zgy.ringforu.bean.BitMapInfo;
import com.zgy.ringforu.config.MainConfig;
import com.zgy.ringforu.service.WaterMarkService;
import com.zgy.ringforu.util.BitmapUtil;
import com.zgy.ringforu.util.FileUtil;
import com.zgy.ringforu.util.PhoneUtil;
import com.zgy.ringforu.util.RingForUActivityManager;
import com.zgy.ringforu.util.StringUtil;
import com.zgy.ringforu.util.TimeUtil;
import com.zgy.ringforu.util.ViewUtil;
import com.zgy.ringforu.util.WaterMarkUtil;
import com.zgy.ringforu.view.MyDialog;
import com.zgy.ringforu.view.MyToast;

public class ToolsWaterMarkActivity extends BaseGestureActivity implements OnSeekBarChangeListener, OnClickListener {

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
	private Button btnHideApp;
	private TextView textSeekbar;
	private LinearLayout layoutOperas;
	private TextView mTextChangeBg;
	private TextView mTextCutTip;
	private RelativeLayout layoutMain;

	private SeekBar seekbarAlpha;

	private static final int REQUEST_CUTPIC = 0;
	private static final int REQUEST_PICKPIC_CAMERA = 1;
	private static final int REQUEST_PICKPIC_GALLERY = 2;

	private String[] arrayBbColors;
	private String[] arrayTextColors;
	private int currentBgColor = 0;

	private File tempFileSrc;
	private File tempFileCutted;

	private MainConfig mMainConfig;

	private boolean mSeekBarOnTouchMove;

	private boolean mJumpOutFromPick;// 若从选择图片或剪裁图片时跳转，则无需check service

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tools_watermark);

		LogRingForu.v(TAG, "onCreate");

		mMainConfig = MainConfig.getInstance();

		mSeekBarOnTouchMove = false;

		RingForUActivityManager.push(this);

		// DisplayMetrics metric = getResources().getDisplayMetrics();
		// screenWidth = metric.widthPixels;
		// screenHeight = metric.heightPixels;
		// LogRingForu.v(TAG, "get screen " + screenWidth + "x" + screenHeight);
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
		btnHideApp = (Button) findViewById(R.id.btn_watermark_hide_app);
		textSeekbar = (TextView) findViewById(R.id.text_watermark);
		layoutOperas = (LinearLayout) findViewById(R.id.layout_watermark_operas);
		layoutMain = (RelativeLayout) findViewById(R.id.layout_watermark_main);
		mTextChangeBg = (TextView) findViewById(R.id.text_watermark_changebg);
		mTextCutTip = (TextView) findViewById(R.id.text_watermark_recut);

		setBgAndTextColor();

		seekbarAlpha.setOnSeekBarChangeListener(this);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnCut.setOnClickListener(this);
		mTextChangeBg.setOnClickListener(this);
		btnChange.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnTitleClose.setOnClickListener(this);
		btnOrientation.setOnClickListener(this);
		btnHideApp.setOnClickListener(this);

		Intent intent = getIntent();
		String action = intent.getAction();
		// 共享获得的图片
		if (Intent.ACTION_SEND.equals(action) && intent.hasExtra(Intent.EXTRA_STREAM)) {
			String type = intent.getType();
			Uri stream = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

			LogRingForu.e(TAG, "get intent  type=" + type + "    stream =" + stream.toString());

			if (stream != null && type != null) {
				try {
					LogRingForu.e(TAG, "stream.getPath()=" + stream.getPath());
					FileUtil.copyFileTo(new File(FileUtil.getImageAbsolutePath(ToolsWaterMarkActivity.this, stream)), MainCanstants.FILE_WATERMARK_IMG);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (Intent.ACTION_VIEW.equals(action)) {
			String type = intent.getType();
			Uri stream = (Uri) intent.getData();

			LogRingForu.e(TAG, "get intent  type=" + type + "    stream =" + stream.toString());

			if (stream != null && type != null) {
				try {
					LogRingForu.e(TAG, "stream.getPath()=" + stream.getPath());
					FileUtil.copyFileTo(new File(stream.getPath()), MainCanstants.FILE_WATERMARK_IMG);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
		imgShow.setAlpha(arg1);
		// mSeekBarOnTouchMove = true;
		LogRingForu.e(TAG, "onProgressChanged");
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		mSeekBarOnTouchMove = true;
		LogRingForu.e(TAG, "onProgressChanged");
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		mSeekBarOnTouchMove = false;
		LogRingForu.e(TAG, "onStopTrackingTouch");
	}

	@Override
	public void onClick(View v) {
		PhoneUtil.doVibraterNormal(ToolsWaterMarkActivity.super.mVb);
		switch (v.getId()) {
		case R.id.btn_watermark_ok:
			open();
			RingForUActivityManager.pop(this);
			break;

		case R.id.btn_watermark_delete:
			// 移除
			WaterMarkUtil.setSwitchOnOff(false);

			if (MainCanstants.FILE_WATERMARK_IMG.exists()) {
				MainCanstants.FILE_WATERMARK_IMG.delete();
			}
			WaterMarkUtil.checkState(ToolsWaterMarkActivity.this);
			MyToast.makeText(ToolsWaterMarkActivity.this, R.string.watermark_del_success, MyToast.LENGTH_SHORT, false).show();
			RingForUActivityManager.pop(this);
			break;
		case R.id.btn_watermark_title_close:
			// 关闭
			WaterMarkUtil.setSwitchOnOff(false);
			WaterMarkUtil.checkState(ToolsWaterMarkActivity.this);
			MyToast.makeText(ToolsWaterMarkActivity.this, R.string.watermark_close_finish, MyToast.LENGTH_SHORT, false).show();
			RingForUActivityManager.pop(this);
			break;
		case R.id.btn_watermark_change:
			pickPic();
			break;
		case R.id.btn_watermark_cancel:
			RingForUActivityManager.pop(this);
			break;
		case R.id.btn_watermark_cut:

			if (PhoneUtil.existSDcard()) {
				try {
					tempFileSrc = new File(MainCanstants.getsdFileWaterMarkCutSrcTemp() + TimeUtil.getCurrentTimeMillis());
					FileUtil.copyFileTo(MainCanstants.FILE_WATERMARK_IMG, tempFileSrc);
					Uri uriMark = Uri.fromFile(tempFileSrc);
					startPhotoZoom(uriMark);
				} catch (IOException e) {
					e.printStackTrace();
					MyToast.makeText(ToolsWaterMarkActivity.this, R.string.no_sdcard, MyToast.LENGTH_SHORT, true).show();
				}

			} else {
				MyToast.makeText(ToolsWaterMarkActivity.this, R.string.no_sdcard, MyToast.LENGTH_SHORT, true).show();
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
			break;

		// case R.id.layout_watermark_changebg:
		// break;
		case R.id.text_watermark_changebg:
			setBgAndTextColor();
			break;

		case R.id.btn_watermark_hide_app:
			startActivity(new Intent(ToolsWaterMarkActivity.this, PickerApplicationActivity.class));
			break;
		default:
			break;
		}

	}

	private void open() {
		mMainConfig.setWaterMarkAlpha(seekbarAlpha.getProgress());
		MyToast.makeText(ToolsWaterMarkActivity.this, R.string.watermark_set_success, MyToast.LENGTH_SHORT, false).show();
		WaterMarkUtil.setSwitchOnOff(true);
	}

	private void setBgAndTextColor() {
		if (currentBgColor == arrayBbColors.length) {
			currentBgColor = 0;
		}
		// layoutMain.setAnimation(AnimationUtils.loadAnimation(getBaseContext(),
		// R.anim.alpha_in_long));
		layoutMain.setBackgroundColor(Color.parseColor(arrayBbColors[currentBgColor]));
		textSeekbar.setTextColor(Color.parseColor(arrayTextColors[currentBgColor]));
		mTextCutTip.setTextColor(Color.parseColor(arrayTextColors[currentBgColor]));
		currentBgColor++;
	}

	/**
	 * 选取图片
	 */
	private void pickPic() {
		// 选择图库和或相机
		// 清空重要联系人
		MyDialog.Builder builder = new MyDialog.Builder(ToolsWaterMarkActivity.this);
		builder.setTitle(R.string.watermark_select_tip).setMessage(R.string.watermark_select_str).setPositiveButton(R.string.watermark_album, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				PhoneUtil.doVibraterNormal(ToolsWaterMarkActivity.super.mVb);
				mJumpOutFromPick = true;
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, REQUEST_PICKPIC_GALLERY);
			}
		}).setNegativeButton(R.string.watermark_camera, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				PhoneUtil.doVibraterNormal(ToolsWaterMarkActivity.super.mVb);
				if (PhoneUtil.existSDcard()) {
					dialog.dismiss();
					mJumpOutFromPick = true;
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					tempFileSrc = new File(MainCanstants.getsdFileWaterMarkCutSrcTemp() + TimeUtil.getCurrentTimeMillis());
					Uri tempTakePicUri = Uri.fromFile(tempFileSrc);
					// 留意一下这个文件路径是按照怎样的规则转换为一个uri的
					LogRingForu.v(TAG, "根据路径转换的uri为：" + tempTakePicUri.toString());
					intent.putExtra(MediaStore.EXTRA_OUTPUT, tempTakePicUri);

					startActivityForResult(intent, REQUEST_PICKPIC_CAMERA);
				} else {
					MyToast.makeText(ToolsWaterMarkActivity.this, R.string.no_sdcard, MyToast.LENGTH_SHORT, true).show();
				}

			}
		}).create().show();
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri cutFileUri) {

		mJumpOutFromPick = true;

		tempFileCutted = new File(MainCanstants.getsdFileWaterMarkCutDesTemp() + TimeUtil.getCurrentTimeMillis());

		Uri uriTemp = Uri.fromFile(tempFileCutted);
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
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(cutFileUri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		DisplayMetrics metric = getResources().getDisplayMetrics();
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", metric.widthPixels);
		intent.putExtra("aspectY", metric.heightPixels);
		// // outputX outputY 是裁剪图片宽高
		// intent.putExtra("outputX", metric.widthPixels);
		// intent.putExtra("outputY", metric.hF eightPixels);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTemp);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);

		startActivityForResult(intent, REQUEST_CUTPIC);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		LogRingForu.e(TAG, "onConfigurationChanged");
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogRingForu.e(TAG, "onActivityResult");
		switch (requestCode) {
		case REQUEST_CUTPIC:
			if (tempFileCutted.exists()) {

				try {
					if (MainCanstants.FILE_WATERMARK_IMG.exists()) {
						MainCanstants.FILE_WATERMARK_IMG.delete();
					}
					if (tempFileSrc != null && tempFileSrc.exists()) {
						tempFileSrc.delete();
					}
					FileUtil.copyFileTo(tempFileCutted, MainCanstants.FILE_WATERMARK_IMG);
					tempFileCutted.delete();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				MyToast.makeText(ToolsWaterMarkActivity.this, "剪裁图片失败！", MyToast.LENGTH_SHORT, true).show();
			}
//			refreshViews();
			break;
		case REQUEST_PICKPIC_CAMERA:

			if (tempFileSrc.exists()) {

				try {
					FileUtil.copyFileTo(tempFileSrc, MainCanstants.FILE_WATERMARK_IMG);
					tempFileSrc.delete();
//					refreshViews();
				} catch (Exception e) {
					e.printStackTrace();
					MyToast.makeText(ToolsWaterMarkActivity.this, "获取图片失败！", MyToast.LENGTH_SHORT, true).show();
//					refreshViews();
				}
			} else {
				MyToast.makeText(ToolsWaterMarkActivity.this, "获取图片失败！", MyToast.LENGTH_SHORT, true).show();
//				refreshViews();
			}
			break;

		case REQUEST_PICKPIC_GALLERY:
			if (data != null) {
				Uri picPath = data.getData();
				LogRingForu.e(TAG, "PIC uri=" + picPath);
				if (picPath != null) {
					String abPath = FileUtil.getImageAbsolutePath(ToolsWaterMarkActivity.this, picPath);
					if (!StringUtil.isNull(abPath)) {
						File getFileSrc = new File(abPath);
						try {
							FileUtil.copyFileTo(getFileSrc, MainCanstants.FILE_WATERMARK_IMG);
//							refreshViews();
							break;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				}

			}
			MyToast.makeText(ToolsWaterMarkActivity.this, "获取图片失败！", MyToast.LENGTH_SHORT, true).show();
//			refreshViews();

			break;
		default:
			break;
		}

	}

	/**
	 * 清除临时水印
	 */
	private void clearWaterMark() {
		WaterMarkService.show = false;
		WaterMarkUtil.ctrlWaterMarkBackService(ToolsWaterMarkActivity.this, true);
	}

	/**
	 * 刷新当前界面
	 */
	private void refreshViews() {
		LogRingForu.e(TAG, "refreshViews!!");
		mJumpOutFromPick = false;
		// 显示水印到view
		if (MainCanstants.FILE_WATERMARK_IMG.exists()) {
			DisplayMetrics metric = getResources().getDisplayMetrics();
			BitMapInfo info = BitmapUtil.readBitmapAutoSize(MainCanstants.FILE_WATERMARK_IMG.getAbsolutePath(), metric.widthPixels, metric.heightPixels);
			imgShow.setImageBitmap(info.getBitMap());
			imgShow.setScaleType(ScaleType.FIT_XY);
			btnOk.setVisibility(View.VISIBLE);
			btnCut.setVisibility(View.VISIBLE);
			btnDel.setVisibility(View.VISIBLE);
			btnHideApp.setVisibility(View.VISIBLE);
			seekbarAlpha.setVisibility(View.VISIBLE);
			textSeekbar.setVisibility(View.VISIBLE);
			btnChange.setText(R.string.watermark_change);
			mTextChangeBg.setVisibility(View.VISIBLE);
			if(info.isScalechanged()) {
				mTextCutTip.setVisibility(View.VISIBLE);
			} else {
				mTextCutTip.setVisibility(View.GONE);
			}
		} else {
			btnOk.setVisibility(View.GONE);
			btnCut.setVisibility(View.GONE);
			btnDel.setVisibility(View.GONE);
			btnHideApp.setVisibility(View.GONE);
			seekbarAlpha.setVisibility(View.GONE);
			textSeekbar.setVisibility(View.GONE);
			btnChange.setText(R.string.watermark_select_tip);
			mTextChangeBg.setVisibility(View.GONE);
			mTextCutTip.setVisibility(View.GONE);
			pickPic();
		}
		// 控制透明度
		int alpha = mMainConfig.getWaterMarkAlpha();
		imgShow.setAlpha(alpha);
		seekbarAlpha.setProgress(alpha);
		if (WaterMarkUtil.isWaterMarkSeted()) {
			btnTitleClose.setVisibility(View.VISIBLE);
		} else {
			btnTitleClose.setVisibility(View.GONE);
		}
		
		if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			btnOrientation.setText(R.string.watermark_orientation_landscape);
		} else {
			btnOrientation.setText(R.string.watermark_orientation_portrait);
		}
	}

	@Override
	protected void onDestroy() {
		LogRingForu.e(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		LogRingForu.e(TAG, "onResume");
		clearWaterMark();
		refreshViews();
		super.onResume();
	}

	@Override
	protected void onPause() {
		LogRingForu.e(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		LogRingForu.e(TAG, "onStop");
		if (!mJumpOutFromPick) {
			LogRingForu.e(TAG, "!mJumpOutFromPick");
			WaterMarkUtil.checkState(ToolsWaterMarkActivity.this);
		}
		super.onStop();
	}

	@Override
	public void onSlideToRight() {
		super.onSlideToRight();
		if (!mSeekBarOnTouchMove) {
			ViewUtil.onButtonPressedBack(btnCancel);
			PhoneUtil.doVibraterNormal(super.mVb);
			RingForUActivityManager.pop(this);
		}

	}

	@Override
	public void onSlideToLeft() {
		super.onSlideToLeft();
		if (btnOk.getVisibility() == View.VISIBLE && !mSeekBarOnTouchMove) {
			ViewUtil.onButtonPressedBlue(btnOk);
			PhoneUtil.doVibraterNormal(super.mVb);
			open();
			RingForUActivityManager.pop(this);
		}
	}

}
