package com.ixiangni.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handongkeji.impactlib.util.ToastUtils;
import com.ixiangni.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class SearchView extends LinearLayout {


    public EditText edtSearch;
    public ImageView ivSearch;

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.search_content, this);
        initView();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private void initView() {
        edtSearch= (EditText) findViewById(R.id.edt_search);
        ivSearch= (ImageView) findViewById(R.id.iv_search);

//        ivSearch.setOnTouchListener(new OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        ivSearch.setColorFilter(Color.RED);
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        ivSearch.setColorFilter(Color.TRANSPARENT);
//                        break;
//
//                }
//
//                return true;
//            }
//        });


        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    ivSearch.performClick();
                    return true;
                }

                return false;
            }
        });
    }

    public void setListener(OnClickListener listener){
        ivSearch.setOnClickListener(listener);
    }

    public EditText getEdtiText(){
        return edtSearch;
    }

    public interface OnSearchListener{
        void search(String content);
    }

    private OnSearchListener listener;


//    public void setOnSearchListener(OnSearchListener listener) {
//        this.listener = listener;
//    }
}
