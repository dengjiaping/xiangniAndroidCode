package com.ixiangni.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.ixiangni.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class MyAlertdialog extends XDialog {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_message)
    TextView tvMessage;
    @Bind(R.id.tv_left)
    TextView tvLeft;
    @Bind(R.id.tv_right)
    TextView tvRight;

    private boolean dismissWhenClick = true;

    public MyAlertdialog(Context context) {
        super(context, R.layout.dialog_alert);
        ButterKnife.bind(this);
    }

    public MyAlertdialog setTitle(String title) {
        tvTitle.setText(title);
        return this;
    }

    public MyAlertdialog setMessage(String message) {
        tvMessage.setText(message);
        return this;
    }

    public MyAlertdialog setLeftText(String leftText) {
        tvLeft.setText(leftText);
        return this;
    }

    public MyAlertdialog setRightText(String rightText) {
        tvRight.setText(rightText);
        return this;
    }

    @OnClick({R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        if (dismissWhenClick) {
            dismiss();
        }
        switch (view.getId()) {
            case R.id.tv_left:
                if (mListener != null) {
                    mListener.onLeftClick(tvLeft);
                }
                break;
            case R.id.tv_right:
                if (mListener != null) {
                    mListener.onRightClick(tvRight);
                }
                break;
        }
    }

    public void setDismissWhenClick(boolean dismissWhenClick) {
        this.dismissWhenClick = dismissWhenClick;
    }

    public void setSingLineMsg(){
        tvMessage.setSingleLine();
    }

    private OnMyClickListener mListener;

    public void setMyClickListener(OnMyClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnMyClickListener {
        void onLeftClick(View view);

        void onRightClick(View view);
    }
}
