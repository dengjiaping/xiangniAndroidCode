package com.ixiangni.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.ixiangni.app.R;
import com.ixiangni.app.publish.PublishAudioActivity;
import com.ixiangni.app.publish.PublishPictActivity;
import com.ixiangni.app.publish.PublishVideoActivity;
import com.ixiangni.app.publish.PublishWordActivity;
import com.ixiangni.app.publish.RecordVideoActivity;
import com.ixiangni.common.BusAction;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class Publishdialog extends XDialog {
    @Bind(R.id.tv_publish_picture)
    TextView tvPublishPicture;
    @Bind(R.id.tv_publish_voice)
    TextView tvPublishVoice;
    @Bind(R.id.tv_publish_text)
    TextView tvPublishText;
    @Bind(R.id.tv_publish_video)
    TextView tvPublishVideo;
    @Bind(R.id.tv_cancel)
    TextView tvCancel;

    public Publishdialog(Context context) {
        super(context, R.layout.dialog_publish);
        ButterKnife.bind(this);
        Window window = getWindow();
        setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomInStyle);
    }

    @OnClick({R.id.tv_cancel,R.id.tv_publish_picture, R.id.tv_publish_voice, R.id.tv_publish_text, R.id.tv_publish_video})
    public void onViewClicked(View view) {
        dismiss();
        switch (view.getId()) {
            case R.id.tv_publish_picture:
                getContext().startActivity(new Intent(getContext(), PublishPictActivity.class).putExtra(BusAction.PIC_TAG,BusAction.PIC_TAG_CIRCLE));
                break;
            case R.id.tv_publish_voice:
                getContext().startActivity(new Intent(getContext(), PublishAudioActivity.class));

                break;
            case R.id.tv_publish_text:
                startActivity(PublishWordActivity.class);
                break;
            case R.id.tv_publish_video:
                startActivity(PublishVideoActivity.class);
                break;
        }
    }
}
