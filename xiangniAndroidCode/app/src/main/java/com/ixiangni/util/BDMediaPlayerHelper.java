package com.ixiangni.util;

import android.content.Context;
import android.util.Log;

import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.baidu.cloud.media.player.IMediaPlayer;

import java.io.IOException;

/**
 * Created by Administrator on 2017/6/30 0030.
 */

public class BDMediaPlayerHelper implements BDCloudMediaPlayer.OnPreparedListener,
BDCloudMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener {

    private BDCloudMediaPlayer mMediaPlayer;
    private Context context;

    public BDCloudMediaPlayer get(Context context){

        this.context = context;
        initPlayer(context);

        return mMediaPlayer;
    }

    private void initPlayer(Context context) {
        mMediaPlayer = new BDCloudMediaPlayer(context.getApplicationContext());
        // 播放器已经解析出播放源格式时回调
        mMediaPlayer.setOnPreparedListener(this);
// 视频宽高变化时回调, 首次解析出播放源的宽高时也会回调
//        mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
// 播放完成时回调
        mMediaPlayer.setOnCompletionListener(this);
// 播放出错时回调
        mMediaPlayer.setOnErrorListener(this);
// 播放器信息回调，如缓冲开始、缓冲结束
//        mMediaPlayer.setOnInfoListener(mInfoListener);
// 总体加载进度回调，返回为已加载进度占视频总时长的百分比
//        mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
// seek快速调节播放位置，完成后回调
//        mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
    }

    public void prepare(String strVideoUrl){
        Log.i(TAG, "strVideoUrl: "+strVideoUrl);


        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
        }
        initPlayer(context);
//        if(!mMediaPlayer.isPlaying()){
            // 若想设置headers，需使用setDataSource(String url, Map<String, String> headers) 方法
            try {
                mMediaPlayer.setDataSource(strVideoUrl);
                // 播放器仅支持异步准备，在onPrepared回调后方可调用start()启动播放
                mMediaPlayer.prepareAsync();
                Log.i(TAG, "prepare: ");
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }

    }


    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        Log.i(TAG, "onPrepared: ");
        iMediaPlayer.start();
    }

    private static final String TAG = "BDMediaPlayerHelper";
    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {

        Log.i(TAG, "onCompletion: ");
        initPlayer(context);
        if(onComplete!=null){
            onComplete.onComplete();
        }
    }

    private OnComplete onComplete;

    public void setOnComplete(OnComplete onComplete) {
        this.onComplete = onComplete;
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        Log.i(TAG, "onError: "+i+","+i1);
        return false;
    }

    public interface OnComplete{
        void onComplete();
    }
}
