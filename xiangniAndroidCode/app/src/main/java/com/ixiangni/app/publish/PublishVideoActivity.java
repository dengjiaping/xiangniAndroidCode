package com.ixiangni.app.publish;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.XNFileProvider;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.mine.FileBrwseActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.interfaces.OnNewsPublish;
import com.ixiangni.records.PublishAllPresenter;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.BaiduUpLoad;
import com.ixiangni.util.NotifyHelper;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发布视频
 *
 * @ClassName:PublishVideoActivity
 * @PackageName:com.ixiangni.app.publish
 * @Create On 2017/6/21 0021   14:22
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class PublishVideoActivity extends BaseActivity {

    @Bind(R.id.iv_thumb_video)
    ImageView ivThumbVideo;
    @Bind(R.id.rb_all_can_see)
    RadioButton rbAllCanSee;
    @Bind(R.id.rb_friend_can_see)
    RadioButton rbFriendCanSee;
    @Bind(R.id.tv_select_position)
    TextView tvSelectPosition;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private String playurl;
    private Bitmap bitmap;
    private PoiInfo poiInfo;
    private File videoFile;
    private File outFile;
    private FFmpeg fFmpeg;
    private int totalSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_video);
        ButterKnife.bind(this);


        fFmpeg = FFmpeg.getInstance(this);
        startRecord();

    }

    /**
     * 录制视频
     */
    private void startRecord() {

        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED) {
            record();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 11);
        }
    }

    private void record() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            videoFile = new File(getExternalCacheDir(), UUID.randomUUID().toString() + ".mp4");

            outFile = new File(getExternalCacheDir(),"out.mp4");
            if(outFile.exists()){
                outFile.delete();
            }


            Uri videoUrl;
            if (Build.VERSION.SDK_INT >= 24) {
                videoUrl = XNFileProvider.getUriForFile(this, "com.ixiangni.app.fileprovider", videoFile);
            } else {
                videoUrl = Uri.fromFile(videoFile);
            }

            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUrl);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0); // set the video image quality to high
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
            startActivityForResult(intent, 80);

        } else {
            toast("没有SD卡");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                record();
            } else {
                toast("您拒绝了相机权限，无法录制视频");
            }
        }
    }

    private void initVideo() {
        playurl = videoFile.getAbsolutePath();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //获取网络视频
        // retriever.setDataSource(url, new HashMap<String, String>());
        //获取本地视频
        retriever.setDataSource(playurl);
        String s2 = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        try{

        totalSecond = Integer.parseInt(s2)/1000;
        }catch (Exception e){

        }

        String s = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        log("METADATA_KEY_DURATION" + s);
        String s1 = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        log("METADATA_KEY_VIDEO_ROTATION" + s1);


        bitmap = retriever.getFrameAtTime();
        ivThumbVideo.setImageBitmap(bitmap);
        ivThumbVideo.setOnClickListener(v -> {
            Intent intent1 = new Intent(this, VideoViewActivity.class);
            intent1.putExtra(XNConstants.videoPath, playurl);
            startActivity(intent1);
        });


        tvSelectPosition.setOnClickListener(v -> {
            startActivityForResult(new Intent(PublishVideoActivity.this, BaiduMapActivity.class), 100);
        });

        topBar.setOnRightClickListener(v -> {

            //如果支持压缩，先压缩，
            showProgressDialog("准备中...", false);
            mProgressDialog.setCanceledOnTouchOutside(false);

            try {
                fFmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                    @Override
                    public void onFailure() {
                        uploadVieo();
                    }

                    @Override
                    public void onSuccess() {

                        mProgressDialog.setMessage("压缩中...");
                        compressVideo();

                    }

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFinish() {
                    }
                });
            } catch (FFmpegNotSupportedException e) {
                e.printStackTrace();
                uploadVieo();
            }


        });

    }

    //压缩
    private void compressVideo() {

        String cmd1 ="-y -i "+videoFile.getAbsolutePath()+" -crf 20 "+outFile.getAbsolutePath();

        mProgressDialog.setMessage("压缩中...");
        try {
            fFmpeg.execute(cmd1.split(" "), new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    playurl=outFile.getAbsolutePath();

                }

                @Override
                public void onProgress(String message) {
                    log(message);
                    if (message.startsWith("frame")) {
                        int second = FileBrwseActivity.getSecond(message);
                        if (second > 0) {
                            second -= 1;
                        }
                        if (totalSecond > 0) {

                            int i = second * 100 / totalSecond;

                            String text = String.format(Locale.CHINA, "压缩中 %d", i);
                            text += "%";

                            mProgressDialog.setMessage(text);
                        }

                    }

                }

                @Override
                public void onFailure(String message) {

                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                    uploadVieo();

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            uploadVieo();
        }


    }

    /**
     * 上传视频到百度云
     */
    private void uploadVieo() {

        mProgressDialog.setMessage("上传中...");
        final String inserturl = UUID.randomUUID() + ".mp4";
        BaiduUpLoad load = new BaiduUpLoad(playurl, BaiduUpLoad.BUCKET_VIDEO, inserturl);
        load.setCallback(new BaiduUpLoad.OnProgressCallback() {
            @Override
            public void onProgress(int completePart, int totalpart) {
                if(topBar==null){
                    return;
                }
                String text = "上传中 ";
                int progress = completePart * 100 / totalpart;
                String pro = progress + "%";
                text += pro;
                mProgressDialog.setMessage(text);
            }
        });

        load.upload(new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                if (topBar != null) {

                    mProgressDialog.setMessage("发布中...");
                    releaseVideo(inserturl);
                }
            }

            @Override
            public void onFailed(String errorMsg) {

                if (topBar != null) {

                    dismissProgressDialog();
                    toast(errorMsg);
                }
            }
        });

    }

    private OnResult<String> ca = new OnResult<String>() {
        @Override
        public void onSuccess(String s) {
            if (topBar != null) {

                dismissProgressDialog();
                SuperObservableManager.notify(OnNewsPublish.class, OnNewsPublish::onNewpublish);
                finish();
            }
        }

        @Override
        public void onFailed(String errorMsg) {
            if (topBar != null) {

                dismissProgressDialog();
                toast(errorMsg);
            }
        }
    };


    private void releaseVideo(String inserturl) {

        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("inserttype", "3");
        params.put("inserturl", inserturl);
        if (poiInfo != null) {
            params.put("momentlng", poiInfo.location.longitude + "");
            params.put("momentlat", poiInfo.location.latitude + "");
            params.put("momentnewsaddress", TextUtils.isEmpty(poiInfo.address.trim()) ? poiInfo.name : poiInfo.address);
        }
        params.put("showtype", rbAllCanSee.isChecked() ? "0" : "1");
        PublishAllPresenter.publish(this, params, ca);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            poiInfo = data.getParcelableExtra(BaiduMapActivity.POIINFO);
            tvSelectPosition.setText(TextUtils.isEmpty(poiInfo.address.trim()) ? poiInfo.name : poiInfo.address);
        } else if (requestCode == 80 && resultCode == RESULT_OK) {
            initVideo();
        }
        if (requestCode == 80 && resultCode == RESULT_CANCELED) {
            onBackPressed();
        }

    }
}
