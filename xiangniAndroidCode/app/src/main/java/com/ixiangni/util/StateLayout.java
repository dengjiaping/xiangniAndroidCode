package com.ixiangni.util;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ixiangni.app.R;

/**
 * Created by Administrator on 2017/6/27 0027.
 */

public class StateLayout extends FrameLayout {

    private View noDataView;
    private View loadingView;
    private View contentView;
    private TextView tv;
    private TextView tvLoadingMessage;

    public StateLayout(@NonNull Context context) {
        this(context, null);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            contentView = getChildAt(0);
        }
        noDataView = LayoutInflater.from(getContext()).inflate(R.layout.layout_no_data, this, false);
        addView(noDataView);
        noDataView.setVisibility(GONE);

        tv = (TextView) noDataView.findViewById(R.id.tv_no_data_message);

        loadingView = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading, this, false);
        ViewCompat.setElevation(loadingView,12);

        addView(loadingView);

        tvLoadingMessage = (TextView) findViewById(R.id.tv_loading_message);
        loadingView.setVisibility(GONE);

        Log.i(TAG, "onFinishInflate: " + getChildCount());
    }

    public void setNodataMessage(String message) {
        tv.setText(message);
    }

    public void setLoaingMessage(String message) {
        tvLoadingMessage.setText(message);
    }

    public void showNoDataView() {
        if (contentView != null) {
            contentView.setVisibility(GONE);
        }
        loadingView.setVisibility(GONE);
        noDataView.setVisibility(VISIBLE);
    }

    public void showNoDataView(String nodataMessage) {
        setNodataMessage(nodataMessage);
        showNoDataView();
    }

    private static final String TAG = "StateLayout";

    public void showContenView() {
        if (contentView != null) {
            contentView.setVisibility(VISIBLE);
            if (playAlphaAnim) {
                ObjectAnimator alpha = ObjectAnimator.ofFloat(contentView, "alpha", 0f, 1f);
                alpha.setDuration(700).start();
            }
        }
        noDataView.setVisibility(GONE);
        loadingView.setVisibility(GONE);
    }

    public void showContentView(boolean playAlphaAnim){
        if (contentView != null) {
            contentView.setVisibility(VISIBLE);
            if (playAlphaAnim) {
                ObjectAnimator alpha = ObjectAnimator.ofFloat(contentView, "alpha", 0f, 1f);
                alpha.setDuration(700).start();
            }
        }
        noDataView.setVisibility(GONE);
        loadingView.setVisibility(GONE);

    }

    public boolean playAlphaAnim = false;

    public void setPlayAlphaAnim(boolean playAlphaAnim) {
        this.playAlphaAnim = playAlphaAnim;
    }

    public void loadingFinish() {
        loadingView.setVisibility(GONE);
    }

    public void showLoadView() {
        loadingView.setVisibility(VISIBLE);
        noDataView.setVisibility(GONE);
    }

    public void showLoadView(String loadingMessage) {
        showLoadView();
        setLoaingMessage(loadingMessage);
    }

    public void showLoadViewNoContent(String loadingMessage) {
        contentView.setVisibility(GONE);
        showLoadView(loadingMessage);
    }

    public void setUpwithBaseAdapter(BaseAdapter adapter,String nodataMessage){
        if(adapter!=null){
            adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if(adapter.getCount()==0){
                        showNoDataView(nodataMessage);
                    }else {
                        showContenView();
                    }
                }
            });
        }
    }

    public void setUpwihtRecyclerAdapter(RecyclerView.Adapter adapter,String nodataMessage){
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(adapter.getItemCount()<=0){
                    showNoDataView(nodataMessage);
                }else {
                    showContenView();
                }
            }

        });
    }
}
