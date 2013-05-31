package com.zgy.ringforu.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zgy.ringforu.R;
import com.zgy.ringforu.util.MainUtil;

public class MyDialog extends Dialog {

	public MyDialog(Context context, int theme) {
		super(context, theme);
	}

	public MyDialog(Context context) {
		super(context);
	}

	public static class Builder {

		private Context context;
		private String title;
		private CharSequence message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener, negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;

		}

		public Builder setMessage(CharSequence message) {
			this.message = message;

			return this;

		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);

			return this;

		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);

			return this;

		}

		public Builder setTitle(String title) {
			this.title = title;

			return this;

		}

		public Builder setContentView(View v) {
			this.contentView = v;

			return this;

		}

		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;

			return this;

		}

		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;

			return this;

		}

		public Builder setNegativeButton(int negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;

			return this;

		}

		public Builder setNegativeButton(String negativeButtonText, DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;

			return this;

		}

		public MyDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final MyDialog dialog = new MyDialog(context, R.style.dialog);

			View layout = inflater.inflate(R.layout.dialog, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			TextView textTitle = (TextView) layout.findViewById(R.id.text_dlg_title);
			Button btnPositive = (Button) layout.findViewById(R.id.btn_dlg_positive);
			Button btnNegative = (Button) layout.findViewById(R.id.btn_dlg_negative);
			// …Ë÷√∞ÎÕ∏√˜
			btnPositive.getBackground().setAlpha(MainUtil.DLG_BTN_ALPHA);
			btnNegative.getBackground().setAlpha(MainUtil.DLG_BTN_ALPHA);

			textTitle.setText(title);

			if (positiveButtonText != null) {
				btnPositive.setText(positiveButtonText);

				if (positiveButtonClickListener != null) {
					btnPositive.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			} else {
				layout.findViewById(R.id.btn_dlg_positive).setVisibility(View.GONE);

			}

			if (negativeButtonText != null) {
				btnNegative.setText(negativeButtonText);

				if (negativeButtonClickListener != null) {
					btnNegative.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);

						}
					});
				}
			} else {
				layout.findViewById(R.id.btn_dlg_negative).setVisibility(View.GONE);
			}
			if (positiveButtonText == null && negativeButtonText == null) {
				layout.findViewById(R.id.layout_dlg_positive).setVisibility(View.GONE);
			}
			if (message != null) {
				((TextView) layout.findViewById(R.id.text_dlg_message)).setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.layout_dlg_content)).removeAllViews();
				((LinearLayout) layout.findViewById(R.id.layout_dlg_content)).addView(contentView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

			}
			dialog.setContentView(layout);
			return dialog;

		}

	}

}
