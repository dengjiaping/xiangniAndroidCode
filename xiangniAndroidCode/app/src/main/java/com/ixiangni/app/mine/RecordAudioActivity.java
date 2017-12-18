package com.ixiangni.app.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsResultAction;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.ui.TopBar;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecordAudioActivity extends BaseActivity implements View.OnTouchListener, EaseVoiceRecorderView.EaseVoiceRecorderCallback, View.OnClickListener {

    @Bind(R.id.record_view)
    EaseVoiceRecorderView recordView;
    @Bind(R.id.btn_record)
    Button btnRecord;
    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.tv_audio_duration)
    TextView tvAudioDuration;
    @Bind(R.id.ll_voice)
    LinearLayout llVoice;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.rl_voice)
    RelativeLayout rlVoice;
    @Bind(R.id.iv_circle)
    ImageView ivCircle;
    private MediaPlayer mediaPlayer;
    private String audioPath;
    private String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        ButterKnife.bind(this);
        btnRecord.setOnTouchListener(this);
        tvDelete.setOnClickListener(this);
        llVoice.setOnClickListener(this);
        topBar.setOnRightClickListener(v -> {
            if(TextUtils.isEmpty(audioPath)){
                toast("请录音");
                return;
            }

            Intent intent = new Intent();
            intent.putExtra(XNConstants.audio_path,audioPath);
            intent.putExtra(XNConstants.audio_duration,duration);
            setResult(RESULT_OK,intent);
            finish();
        });

        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionsResultAction() {
            @Override
            public void onGranted() {

            }

            @Override
            public void onDenied(String permission) {

                toast("您已拒绝了"+permission+",录音功能无法使用.");
            }
        });

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(MotionEvent.ACTION_DOWN==event.getAction()){
            rlVoice.setVisibility(View.GONE);
            stopPlay();
        }

        if(!PermissionsManager.getInstance().hasAllPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO})){
            toast("您已拒绝了录音权限，无法使用此功能，请到设置中进行修改.");
            return false;
        }
        return recordView.onPressToSpeakBtnTouch(v, event, this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions,grantResults);
    }

    @Override
    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
        this.audioPath = voiceFilePath;
        this.duration = voiceTimeLength + "\"";
        rlVoice.setVisibility(View.VISIBLE);
        tvAudioDuration.setText(duration);
    }

    private void startPlay(String audioPath){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioPath);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlay(){
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete://删除
                stopPlay();
                rlVoice.setVisibility(View.GONE);
                break;
            case R.id.ll_voice://开始播放
                startPlay(audioPath);
                break;
        }
    }
}
