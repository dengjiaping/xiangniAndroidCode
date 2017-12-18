package com.ixiangni.dialog;

import android.content.Context;
import android.os.Bundle;
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
 * Created by Administrator on 2017/7/18 0018.
 */

public class FileEditDialog extends XDialog {
    @Bind(R.id.tv_file_detail)
    TextView tvFileDetail;
    @Bind(R.id.tv_delete_file)
    TextView tvDeleteFile;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;

    public FileEditDialog(Context context) {
        super(context, R.layout.dialog_file_edit);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setWindowAnimations(R.style.bottomInStyle);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    @OnClick({R.id.tv_file_detail, R.id.tv_delete_file, R.id.tv_cancel})
    public void onViewClicked(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tv_file_detail:
                if(mListener!=null){
                    mListener.onFileDetail();
                }
                break;
            case R.id.tv_delete_file:
                if(mListener!=null){
                    mListener.onFileDelete();
                }
                break;
            case R.id.tv_cancel:
                if(mListener!=null){
                    mListener.onCancle();
                }
                break;
        }
    }

    private OnEditListener mListener;

    public void setEditListener(OnEditListener listener) {
        this.mListener = listener;
    }

    public static class OnEditListener{
        public void onFileDetail(){

        }

        public void onFileDelete(){

        }

        public void onCancle(){

        }

    }
}
