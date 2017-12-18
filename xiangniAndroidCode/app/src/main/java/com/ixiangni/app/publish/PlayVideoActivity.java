package com.ixiangni.app.publish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.ixiangni.app.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 播放视频
 *
 * @ClassName:PlayVideoActivity
 * @PackageName:com.ixiangni.app.publish
 * @Create On 2017/6/30 0030   17:01
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/30 0030 handongkeji All rights reserved.
 */
public class PlayVideoActivity extends AppCompatActivity {

    @Bind(R.id.rl_root)
    RelativeLayout rlRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);


    }
}
