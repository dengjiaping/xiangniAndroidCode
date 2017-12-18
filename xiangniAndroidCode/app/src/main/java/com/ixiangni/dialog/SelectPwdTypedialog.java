package com.ixiangni.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.ixiangni.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public class SelectPwdTypedialog extends XDialog {
    @Bind(R.id.tv_common_password)
    TextView tvCommonPassword;
    @Bind(R.id.tv_thumb_password)
    TextView tvThumbPassword;

    public SelectPwdTypedialog(Context context,OnPasswordTypeChangeListener listener) {
        super(context, R.layout.layout_password_type);
        ButterKnife.bind(this);
        this.mListener = listener;
        setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.bottomInStyle);
    }

    @OnClick({R.id.tv_common_password, R.id.tv_thumb_password})
    public void onViewClicked(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tv_common_password:
                if(mListener!=null){
                    mListener.onTypeChange("普通密码");
                }
                break;
            case R.id.tv_thumb_password:
                if(mListener!=null){
                    mListener.onTypeChange("指纹密码");
                }
                break;
        }
    }



    private OnPasswordTypeChangeListener mListener;

    public void setmListener(OnPasswordTypeChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface OnPasswordTypeChangeListener{
        void onTypeChange(String type);
    }
}
