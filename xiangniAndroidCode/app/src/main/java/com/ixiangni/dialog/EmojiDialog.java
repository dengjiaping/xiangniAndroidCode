package com.ixiangni.dialog;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.clcus.EmojiFragment;
import com.ixiangni.app.R;

public class EmojiDialog extends LinearLayout{

	public EmojiDialog(Context context, EditText comment_content_txt, ViewGroup parent) {
		super(context);
		this.context = context;
		this.comment_content_txt = comment_content_txt;
		this.parent = parent;
		init();
	}

	private Context context;
	
	EditText comment_content_txt;
	
	ViewGroup parent;

	private EmojiFragment _fragment;
	
	private void init() {
		this.setVisibility(View.GONE);
		View view = LayoutInflater.from(context).inflate(R.layout.emoji_dialog_layout, this,true);
		
		_fragment = (EmojiFragment)((FragmentActivity)context).getSupportFragmentManager().findFragmentById(R.id.emoji_fragment);
		_fragment.setEditTextHolder(comment_content_txt);
		
		this.parent.addView(this);
	}
	
	public void show(){
		this.setVisibility(View.VISIBLE);
	}
	
	public void dissmiss(){
		this.setVisibility(View.GONE);
	}
	
	public void showAndDissmis(){
		if(this.getVisibility() == View.VISIBLE){
			dissmiss();
		}else{
			show();
		}
	}
	
	public boolean isShowing(){
		return this.getVisibility() == View.VISIBLE;
	}
}
