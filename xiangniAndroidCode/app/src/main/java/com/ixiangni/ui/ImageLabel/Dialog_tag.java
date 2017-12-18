package com.ixiangni.ui.ImageLabel;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;


public class Dialog_tag extends Dialog {
	private EditText editText;
	private Button commit;

	Context context;

	public void setListener(OnOkClickListener listener) {
		this.listener = listener;
	}

	private OnOkClickListener listener;

	public Dialog_tag(Context context, boolean cancelable,
                      OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init(context);
	}

	public Dialog_tag(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public Dialog_tag(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		Window w = getWindow();
		ViewGroup decorView = (ViewGroup) w.getDecorView();
		decorView.setBackgroundResource(R.color.transparent);
		View view = LayoutInflater.from(context).inflate(
				R.layout.dialog_tag_image, decorView, false);
		LayoutParams params = view.getLayoutParams();
		editText= (EditText) view.findViewById(R.id.editText);
		commit= (Button) view.findViewById(R.id.commit);
		params.width = (int) (MyApp.getScreenWidth() * 0.7);
		 params.height = (int) (MyApp.getScreenHeight()*0.2);
		view.setLayoutParams(params);
		decorView.removeAllViews();
		decorView.addView(view);
		commit.setEnabled(false);
		commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String text = editText.getText().toString().trim();
				if (listener != null) {
					listener.onClick(view,text);
				}
//				EventBus.getDefault().post(text, "editTagInfo");//给ImageLebelActivity发送标签的内容消息
				dismiss();
			}
		});
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
				int len = s.length();
				if (len > 0) {
					commit.setEnabled(true);
					commit.setTextColor(Color.parseColor("#011A38"));
				} else if (len <= 0) {
					commit.setEnabled(false);
					commit.setTextColor(Color.parseColor("#e0e0e0"));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	public interface OnOkClickListener{
		public void onClick(View view, String text);
	}

}
