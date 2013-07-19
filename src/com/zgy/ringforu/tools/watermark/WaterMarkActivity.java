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
import com.zgy.ringforu.RToolsActivity;
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

	// private File FILE_MARK = WaterMarkUtil.FILEPATH_WATERMARK;
	// private File FILE_MARK_TEMP = new
	// File(WaterMarkUtil.FILEPATH_WATERMARK_TEMP);
	// private File FILE_ALPHA = new
	// File(WaterMarkUtil.FILEPATH_WATERMARK_ALPHA);

	private Vibrator vb = null;
	private String[] arrayBbColors;
	private String[] arrayTextColors;
	private int currentBgColor = 0;

	private Uri tempTakePicUri = null;
	private File tempTakePicFile = null;

	private boolean fromNotifBar;

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
		// seekbarAlpha.setBackgroundResource(R.drawable.seekbar_line_back);
		// seekbarAlpha.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_line_top));
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		btnCut.setOnClickListener(this);
		btnChange.setOnClickListener(this);
		btnDel.setOnClickListener(this);
		btnTitleClose.setOnClickListener(this);
		btnOrientation.setOnClickListener(this);
		layoutChangeBg.setOnClickListener(this);

		Intent intent = getIntent();
		String action = intent.getAction();
		// �����õ�ͼƬ
		if (Intent.ACTION_SEND.equals(action) && intent.hasExtra(Intent.EXTRA_STREAM)) {
			String type = intent.getType();
			Uri stream = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
			if (stream != null && type != null) {
				startPhotoZoom(stream);
				return;
			}
		}
		// else {
		// Bundle b = intent.getExtras();
		// if (b != null && b.containsKey("fromnotifybar") && b.getBoolean("fromnotifybar")) {
		// // ��֪ͨ����ת������
		// fromNotifBar = true;
		// btnTitleClose.setVisibility(View.VISIBLE);
		// btnOk.setVisibility(View.GONE);
		// }
		// }
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
			open();
			finish();
			break;

		case R.id.btn_watermark_delete:
			// �Ƴ�
			if (WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.exists()) {
				WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.delete();
			}

			if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
				WaterMarkUtil.FILE_WATERMARK_IMG.delete();
			}
			WaterMarkUtil.setSwitchOnOff(false);
			WaterMarkUtil.checkWaterMarkState(WaterMarkActivity.this);
			MyToast.makeText(WaterMarkActivity.this, R.string.watermark_del_success, Toast.LENGTH_SHORT, false).show();
			finish();
			break;
		case R.id.btn_watermark_title_close:
			// �ر�
			WaterMarkUtil.setSwitchOnOff(false);
			WaterMarkUtil.checkWaterMarkState(WaterMarkActivity.this);
			MyToast.makeText(WaterMarkActivity.this, R.string.watermark_close_finish, Toast.LENGTH_SHORT, false).show();
			finish();
			break;
		case R.id.btn_watermark_change:
			pickPic();
			break;
		case R.id.btn_watermark_cancel:
			finish();
			break;
		case R.id.btn_watermark_cut:

			File tempFile = getOutputImageFile();
			if (tempFile != null) {
				try {
					FileUtil.copyFileTo(WaterMarkUtil.FILE_WATERMARK_IMG, tempFile);
					Uri uriMark = Uri.fromFile(tempFile);
					startPhotoZoom(uriMark);
				} catch (IOException e) {
					e.printStackTrace();
					MyToast.makeText(WaterMarkActivity.this, R.string.watermark_cannot_pick, Toast.LENGTH_SHORT, true).show();
				}

			} else {
				MyToast.makeText(WaterMarkActivity.this, R.string.watermark_cannot_pick, Toast.LENGTH_SHORT, true).show();
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

	private void open() {
		if (WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.exists()) {
			WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.delete();
		}
		if (FileUtil.save(WaterMarkUtil.FILE_WATERMARK_ALPHA, seekbarAlpha.getProgress() + "", WaterMarkActivity.this)) {
			MyToast.makeText(WaterMarkActivity.this, R.string.watermark_set_success, Toast.LENGTH_SHORT, false).show();
		}
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
	 * ѡȡͼƬ
	 */
	private void pickPic() {
		// ѡ��ͼ��ͻ����
		// �����Ҫ��ϵ��
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
				startActivityForResult(intent, REQUEST_PICKPIC_GALLERY);
			}
		}).setNegativeButton(R.string.watermark_camera, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				if (PhoneUtil.bIsVerbOn) {
					vb.vibrate(new long[] { 0, 20 }, -1);
				}
				clearWaterMark();
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				getOutputImageFile();
				if (tempTakePicFile != null) {
					tempTakePicUri = Uri.fromFile(tempTakePicFile);
					// ����һ������ļ�·���ǰ��������Ĺ���ת��Ϊһ��uri��
					Log.v(TAG, "����·��ת����uriΪ��" + tempTakePicUri.toString());
					intent.putExtra(MediaStore.EXTRA_OUTPUT, tempTakePicUri);

					startActivityForResult(intent, REQUEST_PICKPIC_CAMERA);
				} else {
					MyToast.makeText(WaterMarkActivity.this, R.string.watermark_cannot_pick, Toast.LENGTH_SHORT, true).show();
				}

			}
		}).create().show();
	}

	/**
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri cutFileUri) {
		Uri uriTemp = Uri.fromFile(WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CUT);
		if (WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CUT.exists()) {
			WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CUT.delete();
		}
		Log.e(TAG, "after cut uriTemp =" + uriTemp.toString() + "  src uri = " + cutFileUri.toString());
		Intent intent = new Intent("com.android.camera.action.CROP");
		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setDataAndType(cutFileUri, "image/*");
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		DisplayMetrics metric = getResources().getDisplayMetrics();
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", metric.widthPixels);
		intent.putExtra("aspectY", metric.heightPixels);
		// outputX outputY �ǲü�ͼƬ���
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
			if (WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CUT.exists()) {

				try {
					if (WaterMarkUtil.FILE_WATERMARK_IMG.exists()) {
						WaterMarkUtil.FILE_WATERMARK_IMG.delete();
					}
					FileUtil.copyFileTo(WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CUT, WaterMarkUtil.FILE_WATERMARK_IMG);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(WaterMarkActivity.this, "����ͼƬʧ�ܣ�", Toast.LENGTH_SHORT).show();
			}
			refreshViews();
			break;
		case REQUEST_PICKPIC_CAMERA:

			if (tempTakePicFile != null && tempTakePicFile.exists()) {
				startPhotoZoom(tempTakePicUri);
				// Bundle extras = data.getExtras();
				// if (extras != null) {
				// // ��������Щ���պ��ͼƬ��ֱ�Ӵ�ŵ�Bundle�е��������ǿ��Դ��������ȡ BitmapͼƬ
				// Bitmap image = extras.getParcelable("data");
			} else {
				Toast.makeText(WaterMarkActivity.this, "��ȡͼƬʧ�ܣ�", Toast.LENGTH_SHORT).show();
				refreshViews();
			}
			break;

		case REQUEST_PICKPIC_GALLERY:
			if (data != null) {
				Uri picPath = data.getData();
				Log.e(TAG, "PIC uri=" + picPath);
				if (picPath != null) {
					startPhotoZoom(picPath);
				} else {
					Log.e(TAG, "ERROR!!!" + "   null ");
				}

			} else {
				Toast.makeText(WaterMarkActivity.this, "��ȡͼƬʧ�ܣ�", Toast.LENGTH_SHORT).show();
				refreshViews();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * ��ȡͼƬ�ľ���·��
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

	// �����ļ�·��
	private File getOutputImageFile() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// File mediaDir = new
			// File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
			// + File.separator + "myCamara");
			File mediaDir = WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CAMERA.getParentFile();
			if (!mediaDir.exists()) {
				mediaDir.mkdirs();
			}
			if (WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CAMERA.exists()) {
				WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CAMERA.delete();
			}
			// Log.v(TAG, "�洢·��Ŀ¼��" + mediaDir.getAbsolutePath());
			// ����ʱ�����Ϊ�ļ���
			// String timeStamp = (new
			// SimpleDateFormat("yyyyMMdd_HHmmss")).format(new Date());
			// tempTakePicFile = new File(mediaDir.getAbsoluteFile() +
			// File.separator + "IMG_" + timeStamp + ".jpg");
			tempTakePicFile = WaterMarkUtil.FILE_WATERMARK_IMG_TEMP_CAMERA;
			Log.v(TAG, "�ļ��洢·��Ϊ��" + tempTakePicFile.getAbsolutePath());
		}

		return tempTakePicFile;
	}

	/**
	 * �����ʱˮӡ
	 */
	private void clearWaterMark() {
		WaterMarkService.show = false;
		WaterMarkUtil.ctrlWaterMarkBackService(WaterMarkActivity.this, true);
	}

	/**
	 * ˢ�µ�ǰ����
	 */
	private void refreshViews() {
		// ��ʾˮӡ��view
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
		// ����͸����
		if (WaterMarkUtil.FILEPATH_WATERMARK_ALPHA.exists()) {
			int alpahSaved = Integer.parseInt(FileUtil.load(WaterMarkUtil.FILE_WATERMARK_ALPHA, WaterMarkActivity.this, false));
			seekbarAlpha.setProgress(alpahSaved);
			imgShow.setAlpha(alpahSaved);
		} else {
			imgShow.setAlpha(50);
			seekbarAlpha.setProgress(50);
		}
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
	protected void onPause() {

		WaterMarkService.show = true;
		WaterMarkUtil.ctrlWaterMarkBackService(WaterMarkActivity.this, true);

		super.onPause();
	}

}
