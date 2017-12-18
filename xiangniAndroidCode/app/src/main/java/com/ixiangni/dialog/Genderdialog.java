package com.ixiangni.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.ixiangni.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/19 0019.
 */

public class Genderdialog extends XDialog {
    @Bind(R.id.tv_boy)
    TextView tvBoy;
    @Bind(R.id.tv_girl)
    TextView tvGirl;

    public Genderdialog(Context context) {
        super(context, R.layout.layout_gender);
        ButterKnife.bind(this);

        setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomInStyle);


    }

    @OnClick({R.id.tv_boy, R.id.tv_girl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_boy:
                if(listener!=null){
                    listener.onGender("男",1);
                }

                break;
            case R.id.tv_girl:
                if(listener!=null){
                    listener.onGender("女",2);
                }
                break;
        }
        dismiss();
    }

    private OnGenderChangeListener listener;

    public void setListener(OnGenderChangeListener listener) {
        this.listener = listener;
    }

    public interface OnGenderChangeListener{
        void onGender(String gender,int gn);
    }
}
