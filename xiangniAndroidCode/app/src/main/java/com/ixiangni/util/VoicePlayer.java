//package com.ixiangni.util;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.Looper;
//import android.os.Message;
//import android.os.Process;
//import android.util.Log;
//import android.widget.Button;
//import android.widget.RelativeLayout;
//
//import com.baidu.cyberplayer.core.BVideoView;
//import com.baidu.cyberplayer.subtitle.SubtitleManager;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// * Created by Administrator on 2016/12/7.
// */
//public class VoicePlayer implements BVideoView.OnPreparedListener, BVideoView.OnCompletionListener, BVideoView.OnCompletionWithParamListener, BVideoView.OnErrorListener, BVideoView.OnInfoListener {
//
//    private String AK = "5d15553712d4421098b34bbf1439bbb4"; // 请录入您的AK !!!
//
//    private String mVideoSource = null;
//
//    private BVideoView mVV = null;
////    private BMediaController mVVCtl = null;
//
//    private EventHandler mEventHandler;
//    private HandlerThread mHandlerThread;
//
//    private final Object SYNC_Playing = new Object();
//
//    private static final int UI_EVENT_PLAY = 0;
//    private static final int UI_EVENT_TAKESNAPSHOT = 2;
//
//
//    /**
//     * 播放状态
//     */
//    private enum PLAYER_STATUS {
//        PLAYER_IDLE, PLAYER_PREPARING, PLAYER_PREPARED,
//    }
//
//    private PLAYER_STATUS mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
//
//    /**
//     * 记录播放位置
//     */
//    private int mLastPos = 0;
//
//    // add for subtitle
//    private Button mSubtitleButton;
//    private SubtitleSettingPopupWindow mSubtitleSettingWindow;
//    private RelativeLayout mRoot;
//    private SubtitleManager mSubtitleManager;
//
//    private Context context;
//    private String[] mAvailableResolution = null;
//
//    private BVideoView.OnPositionUpdateListener updateListener;
//
//    public void setListener(OnPlayCompleteListener listener) {
//        this.tempListener = listener;
//    }
//
//    private OnPlayCompleteListener listener;
//    private OnPlayCompleteListener tempListener;
//
//    class EventHandler extends Handler {
//        public EventHandler(Looper looper) {
//            super(looper);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case UI_EVENT_PLAY:
//                    /**
//                     * 如果已经播放了，等待上一次播放结束
//                     */
//                    if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) {
//                        synchronized (SYNC_Playing) {
//                            try {
//                                SYNC_Playing.wait();
//                            } catch (InterruptedException e) {
//                                // TODO Auto-generated catch block
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//
//                    /**
//                     * 设置播放url
//                     */
//                    Log.d("aaa", "mVideoSource : " + mVideoSource);
//                    // mVV.setVideoPath(mVideoSource, drmToken);
//                    mVV.setVideoPath(mVideoSource);
//                    // mVV.setVideoScalingMode(BVideoView.VIDEO_SCALING_MODE_SCALE_TO_FIT);
//                    /**
//                     * 续播，如果需要如此
//                     */
//                    if (mLastPos > 0) {
//
//                        mVV.seekTo(mLastPos);
//                        mLastPos = 0;
//                    }
//
//                    /**
//                     * 显示或者隐藏缓冲提示
//                     */
////                    mVV.showCacheInfo(true);
//                    /**
//                     * 开始播放
//                     */
//                    mVV.start();
//                    Log.d("aaa", "start 开始播放");
//
//                    mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARING;
//                    break;
//                case UI_EVENT_TAKESNAPSHOT:
//                    boolean sdCardExist = isExternalStorageWritable();
//                    File sdDir = null;
//                    String strpath = null;
//                    Bitmap bitmap = null;
//                    Date date = new Date();
//                    SimpleDateFormat formatter = new SimpleDateFormat(
//                            "yyyy-MM-dd-HH-mm-ss-SSSS");
//                    String time = formatter.format(date);
//                    if (sdCardExist) {
//                        sdDir = context.getExternalCacheDir();
//                        // check the dir is existed or not!.
//                        // File file = new File(sdDir.toString());
//                        strpath = sdDir.toString() + "/" + time + ".jpg";
//                    } else {
//                        strpath = context.getCacheDir().getAbsolutePath() + "/" + time + ".jpg";
//                    }
//
//                    bitmap = mVV.takeSnapshot();
//                    if (bitmap != null) {
//                        FileOutputStream os = null;
//                        try {
//                            os = new FileOutputStream(strpath, false);
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
//                            os.flush();
//                            os.close();
//                        } catch (Throwable t) {
//                            t.printStackTrace();
//                        } finally {
//                            if (os != null) {
//                                try {
//                                    os.close();
//                                } catch (Throwable t) {
//                                }
//                            }
//                        }
//                        os = null;
//                    }
//
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    /* Checks if external storage is available for read and write */
//    public boolean isExternalStorageWritable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            return true;
//        }
//        return false;
//    }
//
//    public void startPlay() {
//        // 发起一次播放任务,当然您不一定要在这发起
////        if (!mVV.isPlaying() && (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
////            mVV.resume();
////        } else {
//////            mEventHandler.sendEmptyMessage(UI_EVENT_PLAY);
////        }
//        if ((mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE) || mVV.isPlaying()) {
//            mLastPos = (int) mVV.getCurrentPosition();
//            mVV.stopPlayback();
//        } else {
//            mEventHandler.sendEmptyMessage(UI_EVENT_PLAY);
//        }
//    }
//
//    public void startWithSource(Uri uri){
//        Uri uriPath = uri;
//        String source = null;
//        if (null != uriPath) {
//            String scheme = uriPath.getScheme();
//            if (null != scheme) {
//                source = uriPath.toString();
//            } else {
//                source = uriPath.getPath();
//            }
//        }
//        if (mVideoSource != null && mVideoSource.equals(source)) {
//            startPlay();
//            return;
//        }
//        mVideoSource = source;
//
//        if (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE || mVV.isPlaying()) {
//            mVV.stopPlayback();
//            mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
//            mLastPos = 0;
//        }
//        mVV.post(() -> {
//            mLastPos = 0;
//            listener = tempListener;
//            mEventHandler.sendEmptyMessage(UI_EVENT_PLAY);
//        });
//    }
//
//    public void setVideoSource(Uri uri) {
//        Uri uriPath = uri;
//        if (null != uriPath) {
//            String scheme = uriPath.getScheme();
//            if (null != scheme) {
//                mVideoSource = uriPath.toString();
//            } else {
//                mVideoSource = uriPath.getPath();
//            }
//        }
//    }
//
//    public void onResume() {
//        if (!mVV.isPlaying() && (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
//            mVV.resume();
//        }
//    }
//
//    public void onPause() {
//        if (mVV.isPlaying() && (mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
//            mLastPos = (int) mVV.getCurrentPosition();
//            // when scree lock,paus is good select than stop
//            // don't stop pause
//            // mVV.stopPlayback();
////            mVV.pause();
//            mVV.stopPlayback();
//        }
//    }
//
//    public void onDestory() {
//        if ((mPlayerStatus != PLAYER_STATUS.PLAYER_IDLE)) {
//            mLastPos = (int) mVV.getCurrentPosition();
//            mVV.stopPlayback();
//        }
////        if (toast != null) {
////            toast.cancel();
////        }
//        /**
//         * 结束后台事件处理线程
//         */
//        mHandlerThread.quit();
//    }
//
//    public VoicePlayer(Context context, Uri uri, BVideoView videoView, BVideoView.OnPositionUpdateListener updateListener) {
//        this.context = context;
//        mVV = videoView;
////        this.updateListener = updateListener;
//        init();
//    }
//
//
//    public VoicePlayer(Context context, BVideoView videoView, BVideoView.OnPositionUpdateListener updateListener) {
//        this.context = context;
//        mVV = videoView;
//        this.updateListener = updateListener;
//        init();
//    }
//
//    public VoicePlayer(Context context, BVideoView videoView) {
//        this.context = context;
//        mVV = videoView;
//        init();
//    }
//
//    private void init() {
//        initUI();
//        /**
//         * 开启后台事件处理线程
//         */
//        mHandlerThread = new HandlerThread("event handler thread",
//                Process.THREAD_PRIORITY_BACKGROUND);
//        mHandlerThread.start();
//        mEventHandler = new EventHandler(mHandlerThread.getLooper());
//    }
//
//    private void initUI() {
//        /**
//         * 设置ak
//         */
//        BVideoView.setAK(AK);
////        mVV.setLogLevel(4);
////        mVVCtl = new BMediaController(context);
//
//        // Getting media-info, as well as the supported resolutions
////        new Thread(new Runnable() {
////            @Override
////            public void run() {
////                Looper.prepare();
////                // BVideoView.getMediaInfo(VideoViewPlayingActivity.this,
////                // mVideoSource, drmToken);
////                BVideoView.getMediaInfo(context, mVideoSource);
////                mAvailableResolution = BVideoView.getSupportedResolution();
//////                AndroidSchedulers.mainThread().createWorker().schedule(() -> startPlay());
////            }
////        }).start();
//
//        /**
//         * 注册listener
//         */
//        mVV.setOnPreparedListener(this);
//        mVV.setOnCompletionListener(this);
//        mVV.setOnCompletionWithParamListener(this);
//        mVV.setOnErrorListener(this);
//        mVV.setOnInfoListener(this);
//        if (updateListener != null) {
//            mVV.setOnPositionUpdateListener(updateListener);
//
//        }
////        mVVCtl.setPreNextListener(mPreListener, mNextListener);
////        mVVCtl.setSnapshotListener(mSnapshotListener);
////        mVVCtl.setResolutionListener(mSelectResolutionListener);
//        /**
//         * 关联BMediaController
//         */
////        mVV.setMediaController(mVVCtl);
//        // disable dolby audio effect
//        // mVV.setEnableDolby(false);
//        /**
//         * 设置解码模式
//         */
//        mVV.setDecodeMode(BVideoView.DECODE_SW);
//        mVV.selectResolutionType(BVideoView.RESOLUTION_TYPE_AUTO);
////        initSubtitleSetting();
//    }
//
//    @Override
//    public void onPrepared() {
//        Log.d("aaa", "onPrepared: 准备播放");
//        mPlayerStatus = PLAYER_STATUS.PLAYER_PREPARED;
//    }
//
//    @Override
//    public void onCompletion() {
//        Log.d("aaa", "onCompletion: 播放结束");
//        synchronized (SYNC_Playing) {
//            SYNC_Playing.notify();
//        }
//        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
//        if (listener != null) {
//            listener.onComplete();
//        }
//    }
//
//    @Override
//    public void OnCompletionWithParam(int i) {
//
//    }
//
//    @Override
//    public boolean onError(int what, int extra) {
//        Log.d("aaa", "onError: 播放发生错误   what = " + what + "   extra = " + extra);
//        synchronized (SYNC_Playing) {
//            SYNC_Playing.notify();
//        }
//        mPlayerStatus = PLAYER_STATUS.PLAYER_IDLE;
//        if (listener != null) {
//            listener.onError();
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onInfo(int what, int extra) {
//        switch (what) {
//            /**
//             * 开始缓冲
//             */
//            case BVideoView.MEDIA_INFO_BUFFERING_START:
//
//                break;
//            /**
//             * 结束缓冲
//             */
//            case BVideoView.MEDIA_INFO_BUFFERING_END:
//
//                break;
//            default:
//                break;
//        }
//        return false;
//    }
//
//    public interface OnPlayCompleteListener{
//        public void onComplete();
//        public void onError();
//    }
//
//}
