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

public class Icondialog extends XDialog {
    @Bind(R.id.tv_local_img)
    TextView tvLocalImg;
    @Bind(R.id.tv_take_picture)
    TextView tvTakePicture;

    public Icondialog(Context context) {
        super(context, R.layout.dialog_icon);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_local_img, R.id.tv_take_picture,R.id.tv_look_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_local_img:
                if(mListener!=null){
                    mListener.onLocalClick();
                }
                break;
            case R.id.tv_take_picture:
                if(mListener!=null){
                    mListener.onTakepictureClick();
                }
                break;
            case R.id.tv_look_picture:
                if(mListener!=null){
                    mListener.onLookpictureClick();
                }
                break;
        }
        dismiss();
    }

    private OnClickDialogListener mListener;

    public void setClickListener(OnClickDialogListener mListener) {
        this.mListener = mListener;
    }

    public interface OnClickDialogListener{
        void onLocalClick();
        void onTakepictureClick();
        void onLookpictureClick();
    }
}
