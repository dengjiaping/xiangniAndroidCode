package com.ixiangni.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ixiangni.app.R;


public class CallDialog implements OnClickListener {

	Context activity;
	Dialog dialog;
	private TextView cancel;
	private TextView ok;
	private TextView content;

	public CallDialog(Context activity) {
		this.activity = activity;
		init();
	}



	private void init() {
		dialog = new Dialog(activity);
		dialog.setCanceledOnTouchOutside(true);
		ViewGroup decorView = (ViewGroup) dialog.getWindow().getDecorView();
		decorView.removeAllViews();
		decorView.setBackgroundColor(Color.TRANSPARENT);
		LayoutInflater.from(activity).inflate(R.layout.call_dialog_layout,
				decorView, true);

		cancel = (TextView) decorView.findViewById(R.id.cancel);
		ok = (TextView) decorView.findViewById(R.id.ok);
		content = (TextView) decorView.findViewById(R.id.content);

		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);
	}

	public void showDialog(){
		dialog.show();
	}

	public void dismissDialog(){
		dialog.dismiss();
	}

	public void setMessage(CharSequence cont){

	}

	public void setCancelText(CharSequence text){
		cancel.setText(text);
	}
	public void setOkText(CharSequence text){
		ok.setText(text);
	}
	public void setMessage(int  resId){
		content.setText(resId);
	}

	public void setMsg(CharSequence text){
		content.setText(text);
	}

	public void setNegativeButtonListener(OnClickListener l){
		mOnClickListener = l;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.cancel:
				this.dialog.dismiss();
				break;

			case R.id.ok:
			if (mOnClickListener != null){
				mOnClickListener.onClick(v);
			}
				dialog.dismiss();
				break;
		}
	}

	private OnClickListener mOnClickListener;
}
