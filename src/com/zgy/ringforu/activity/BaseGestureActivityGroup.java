package com.zgy.ringforu.activity;

import android.app.ActivityGroup;
import android.app.Service;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;

import com.zgy.ringforu.LogRingForu;
import com.zgy.ringforu.MainCanstants;
import com.zgy.ringforu.RingForU;

public class BaseGestureActivityGroup extends ActivityGroup implements OnGestureListener {

	private boolean mGetResult;

	public Vibrator mVb = null;

	public GestureDetector mGestureDetector;

	public void onSlideToRight() {
		mGetResult = true;
		LogRingForu.e("", "--------onSlideToRight-------------");
	}

	public void onSlideToLeft() {
		mGetResult = true;
		LogRingForu.e("", "--------onSlideToLeft-------------");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this);
		mVb = (Vibrator) getApplication().getSystemService(Service.VIBRATOR_SERVICE);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (RingForU.getInstance().isbIsGestureOn()) {
			if (ev.getAction() == MotionEvent.ACTION_UP) {
				mGetResult = false;
			}
			if (!mGetResult) {
				mGestureDetector.onTouchEvent(ev);
			}
		}

		return super.dispatchTouchEvent(ev);
	}

	/*** ÊÖÊÆ ************************************/

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		LogRingForu.v("", "e1.getY() - e2.getY()=" + (e1.getY() - e2.getY()));
		LogRingForu.v("", "e1.getx() - e2.getx()=" + (e1.getX() - e2.getX()));
		if ((Math.abs(e1.getY() - e2.getY()) - MainCanstants.INT_ONFLING_LEN[1]) < 0) {

			if (e1.getX() - e2.getX() - MainCanstants.INT_ONFLING_LEN[0] > 0) {
				// ×ó»¬
				onSlideToLeft();
			} else if (e1.getX() - e2.getX() + MainCanstants.INT_ONFLING_LEN[0] < 0) {
				// ÓÒ»¬
				onSlideToRight();
			}
		}

		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

}
