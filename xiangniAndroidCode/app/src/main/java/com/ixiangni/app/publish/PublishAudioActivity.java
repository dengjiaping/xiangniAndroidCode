package com.ixiangni.app.publish;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.OnResult;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsResultAction;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.interfaces.OnNewsPublish;
import com.ixiangni.records.PublishAllPresenter;
import com.ixiangni.records.RecordPlayer;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.BaiduUpLoad;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发布语音
 *
 * @ClassName:PublishAudioActivity
 * @PackageName:com.ixiangni.app.publish
 * @Create On 2017/6/21 0021   10:11
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class PublishAudioActivity extends BaseActivity {

    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.tv_audio_duration)
    TextView tvAudioDuration;
    @Bind(R.id.ll_voice)
    LinearLayout llVoice;
    @Bind(R.id.tv_delete)
    TextView tvDelete;
    @Bind(R.id.rb_all_can_see)
    RadioButton rbAllCanSee;
    @Bind(R.id.rb_friend_can_see)
    RadioButton rbFriendCanSee;
    @Bind(R.id.tv_select_position)
    TextView tvSelectPosition;
    @Bind(R.id.iv_circle)
    ImageView ivCircle;
    @Bind(R.id.tv_speak)
    Button tvSpeak;
    @Bind(R.id.rl_audio)
    RelativeLayout rlAudio;
    @Bind(R.id.record_view)
    EaseVoiceRecorderView recordView;


    private RecordPlayer recordPlayer = new RecordPlayer();
    private boolean first;
    private String recordPath;
    private String ObjectKey;

    long recordTime = 0;
    private String fileName;


    private static final int RECORD_NO = 0; // 不在录音
    private static final int RECORD_ING = 1; // 正在录音
    private static final int RECORD_ED = 2; // 完成录音
    private int mRecord_State = RECORD_NO; // 录音的状态
    private SimpleDateFormat sdf;
    private boolean recorded;
    private PoiInfo poiInfo;

    private File lastAudioFile = null;

    private MediaRecorder mediarecorder;
    private MediaPlayer mediaPlayer;

    private SimpleDateFormat format = new SimpleDateFormat("ss\"SS", Locale.getDefault());
    private Timer timer;
    private int mvoiceTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_audio);
        ButterKnife.bind(this);

        initAction();

        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new PermissionsResultAction() {
                    @Override
                    public void onGranted() {

                    }

                    @Override
                    public void onDenied(String permission) {

                        toast("您拒绝了" + permission + ",导致无法录音，请到设置中进行修改.");
                    }
                });
    }


    private void initAction() {
        //播放录制的音频
        llVoice.setOnClickListener(v -> {

            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                stopPlayAudio();
            } else {
                playAudio();
            }
        });


        //触摸录音按钮 开始录音 松开停止录音

        tvDelete.setOnClickListener(v -> {
            tvSelectPosition.setText("");
            poiInfo = null;
            rlAudio.setVisibility(View.GONE);
            if (lastAudioFile != null && lastAudioFile.exists()) {
                lastAudioFile.delete();
            }
        });

        tvSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!PermissionsManager.getInstance().hasAllPermissions(PublishAudioActivity.this, new String[]{Manifest.permission.RECORD_AUDIO})) {
                    toast("你拒绝了录音权限，无法录音，请到设置中修改.");
                    return false;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        stopPlayAudio();
                        rlAudio.setVisibility(View.GONE);
                        break;

                }

                return recordView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {
                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        rlAudio.setVisibility(View.VISIBLE);

                        lastAudioFile = new File(voiceFilePath);
                        mvoiceTime = voiceTimeLength;
                        tvAudioDuration.setText(voiceTimeLength+"\"");
                    }
                });
            }
        });


        //发布录音
        topBar.setOnRightClickListener(v -> {
            //上传发布
            upLoadAudio();

        });

        //选择地点
        tvSelectPosition.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, BaiduMapActivity.class), 100);

        });
    }

    //上传录音
    private void upLoadAudio() {
        if (lastAudioFile != null && lastAudioFile.exists()) {
            showProgressDialog("上传中...", false);

            final String objectKey = "a" + UUID.randomUUID() + ".mp3";
            BaiduUpLoad baiduUpLoad = new BaiduUpLoad(lastAudioFile.getAbsolutePath(), BaiduUpLoad.BUCKET_AUDIO, objectKey);
            baiduUpLoad.upload(new OnResult<String>() {
                @Override
                public void onSuccess(String s) {
                    mProgressDialog.setMessage("上传中...");
                    releashAudio(objectKey);
                }

                @Override
                public void onFailed(String errorMsg) {
                    dismissProgressDialog();
                    toast(errorMsg);
                }
            });


        } else {
            toast("您还没有录音哦~");
        }

    }


    private OnResult<String> ca = new OnResult<String>() {
        @Override
        public void onSuccess(String s) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            toast("发布成功！");
            //更新想你圈
            SuperObservableManager.getInstance().getObservable(OnNewsPublish.class).notifyMethod(OnNewsPublish::onNewpublish);
            finish();
        }

        @Override
        public void onFailed(String errorMsg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            toast(errorMsg);
        }
    };


    /**
     * 发布音频
     *
     * @param audioPath momentlng	否	String
     *                  momentlat	否	String
     *                  momentnewsaddress	否	String
     */
    private void releashAudio(String audioPath) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());

        params.put("inserttype", "2");
        params.put("inserturl", audioPath);
        if (poiInfo != null) {
            params.put("momentlng", poiInfo.location.longitude + "");
            params.put("momentlat", poiInfo.location.latitude + "");
            params.put("momentnewsaddress", tvSelectPosition.getText().toString().trim());
        }
        params.put("showtype", rbAllCanSee.isChecked() ? "0" : "1");

        String timtStr = mvoiceTime + "\"";
        params.put("mediatime", timtStr);
        PublishAllPresenter.publish(this, params, ca);

    }


    /**
     * 停止播放
     */
    private void stopPlayAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        stopPlayAudio();
    }

    /**
     * 播放录制的音频
     */
    private void playAudio() {
        if (lastAudioFile != null && lastAudioFile.exists()) {
            try {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(lastAudioFile.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                log("播放录音");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            poiInfo = data.getParcelableExtra(BaiduMapActivity.POIINFO);
            tvSelectPosition.setText(TextUtils.isEmpty(poiInfo.address.trim()) ? poiInfo.name : poiInfo.address);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }


}
