package com.zgy.ringforu.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * 用于添加联系人时搜索过滤
 * @author ZGY
 *
 */
public class TextEditor extends EditText {

	private boolean mModify = false;
	private OnTextChangeListener mTextChangeListner = null;

	// //////////////////////////////////////////////
	// Constructor/Destructor

	// create dynamically, for hard code mode
	public TextEditor(Context context) {
		super(context);
		addTextChangedListener(mTwTextChanged); // to receive text change event
	}

	// create via XML
	public TextEditor(Context context, AttributeSet attrs) {
		super(context, attrs);
		addTextChangedListener(mTwTextChanged); // to receive text change event
	}

	@Override
	public void finalize() {
		removeTextChangedListener(mTwTextChanged);
	}

	// //////////////////////////////////////////////
	// Operations
	public void setModify(boolean modify) {
		mModify = modify;
	}

	public boolean isModified() {
		return mModify;
	}

	public void setOnTextChangeListener(OnTextChangeListener listener) {
		mTextChangeListner = listener;
	}

	// //////////////////////////////////////////////
	// interfaces

	public interface OnTextChangeListener {

		public void onTextChanged(View v, String s);
	}

	// //////////////////////////////////////////////
	// listeners

	private TextWatcher mTwTextChanged = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {
			setModify(true);
			if (mTextChangeListner != null) {
				final TextEditor thiz = TextEditor.this;
				mTextChangeListner.onTextChanged(thiz, s.toString());
			}
		}
	};
}
