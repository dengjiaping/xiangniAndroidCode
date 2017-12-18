package com.ixiangni.app.publish;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class VideoViewActivity extends BaseActivity {

    @Bind(R.id.video_view)
    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_view);
        ButterKnife.bind(this);


        videoView.post(new Runnable() {
            @Override
            public void run() {

                init();
            }
        });
    }

    private void init() {
        String path = getIntent().getStringExtra(XNConstants.videoPath);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);

        Bitmap frameAtTime = retriever.getFrameAtTime();
        int height = frameAtTime.getHeight();
        int width = frameAtTime.getWidth();

        log("height" + height);
        log("width" + width);

        View parent = (View) videoView.getParent();
        int height1 = parent.getHeight();
        int width1 = parent.getWidth();

        float scal = Math.min((float) height1 / height, (float) width1 / width);

        log("" + scal);

        ViewGroup.LayoutParams layoutParams = videoView.getLayoutParams();
        layoutParams.height = (int) (height * scal);
        layoutParams.width = (int) (width * scal);
        log("layoutParams.height" + layoutParams.height);
        log("layoutParams.width" + layoutParams.width);

        videoView.setLayoutParams(layoutParams);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);

        videoView.setVideoPath(path);
        videoView.start();
    }
}
