package com.handongkeji.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class AutoListView extends ListView {


	public AutoListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public AutoListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoListView(Context context) {
		super(context);
	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
	   super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
