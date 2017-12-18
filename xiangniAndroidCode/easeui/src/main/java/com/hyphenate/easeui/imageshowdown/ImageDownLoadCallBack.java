package com.hyphenate.easeui.imageshowdown;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by xhh on 2017/12/7.
 * 图片下载的回调的接口
 */

public interface ImageDownLoadCallBack {
    void onDownLoadSuccess(File file);
    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
