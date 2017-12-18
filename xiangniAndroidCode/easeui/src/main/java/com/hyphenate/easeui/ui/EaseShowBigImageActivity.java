/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.imageshowdown.EaseDowmLoadImageService;
import com.hyphenate.easeui.imageshowdown.ImageDownLoadCallBack;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.widget.photoview.EasePhotoView;
import com.hyphenate.easeui.widget.photoview.PhotoViewAttacher;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.ImageUtils;

import java.io.File;

/**
 * download and show original image
 *
 */
public class EaseShowBigImageActivity extends EaseBaseActivity {
    private static final String TAG = "ShowBigImage";
    private ProgressDialog pd;
    private EasePhotoView image;
    private int default_res = R.drawable.ease_default_image;
    private String localFilePath;
    private Bitmap bitmap;
    private boolean isDownloaded;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.ease_activity_show_big_image);
        super.onCreate(savedInstanceState);

        image = (EasePhotoView) findViewById(R.id.image);


        ProgressBar loadLocalPb = (ProgressBar) findViewById(R.id.pb_load_local);

        //		findViewById(R.id.iv_colse).setOnClickListener(new OnClickListener() {
        //			@Override
        //			public void onClick(View v) {
        //				onBackPressed();
        //			}
        //		});


        default_res = getIntent().getIntExtra("default_image", R.drawable.ease_default_avatar);
        final Uri uri = getIntent().getParcelableExtra("uri");
        localFilePath = getIntent().getExtras().getString("localUrl");
        String msgId = getIntent().getExtras().getString("messageId");
        EMLog.d(TAG, "show big msgId:" + msgId);

        //		//show the image if it exist in local path
        //		if (uri != null && new File(uri.getPath()).exists()) {
        //			EMLog.d(TAG, "showbigimage file exists. directly show it");
        //			DisplayMetrics metrics = new DisplayMetrics();
        //			getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //			// int screenWidth = metrics.widthPixels;
        //			// int screenHeight =metrics.heightPixels;
        //			bitmap = EaseImageCache.getInstance().get(uri.getPath());
        //			//.asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE)
        //			// 为其添加缓存策略,其中缓存策略可以为:Source及None,None及为不缓存,Source缓存原型.如果为ALL和Result就不行.然后几个issue的连接:
        ////			https://github.com/bumptech/glide/issues/513
        ////			https://github.com/bumptech/glide/issues/281
        ////			https://github.com/bumptech/glide/issues/600
        //			Glide.with(this).load(uri)
        ////					.diskCacheStrategy(DiskCacheStrategy.SOURCE)
        //					.placeholder(R.drawable.ease_default_image).into(image);
        //			if (bitmap == null) {
        //				EaseLoadLocalBigImgTask task = new EaseLoadLocalBigImgTask(this, uri.getPath(), image, loadLocalPb, ImageUtils.SCALE_IMAGE_WIDTH,
        //						ImageUtils.SCALE_IMAGE_HEIGHT);
        //				if (android.os.Build.VERSION.SDK_INT > 10) {
        //					task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //				} else {
        //					task.execute();
        //				}
        //			} else {
        ////				image.setImageBitmap(bitmap);
        //			}
        //		} else if(msgId != null) {
        //		    downloadImage(msgId);
        //		}else {
        //			image.setImageResource(default_res);
        //		}
        //
        ////		image.setOnClickListener(new OnClickListener() {
        ////			@Override
        ////			public void onClick(View v) {
        ////				finish();
        ////			}
        ////		});
        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });

        Glide.with(EaseShowBigImageActivity.this).load(uri).diskCacheStrategy(DiskCacheStrategy.SOURCE).placeholder(R.drawable.ease_default_image).into(image);
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EaseShowBigImageActivity.this);
                String[] items = new String[]{"保存", "取消"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                //                        String url = localFilePath.substring(localFilePath.lastIndexOf(".") + 1);
                                //                        if (url.equals("gif")){
                                //                            Toast.makeText(EaseShowBigImageActivity.this, "表情包不能保存需要购买", Toast.LENGTH_SHORT).show();
                                //                        }else{
                                //                            onDownLoad(url);
                                //                        }
                                onDownLoad(uri);
                                break;
                            case 1:
                                Toast.makeText(EaseShowBigImageActivity.this, "取消", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                builder.show();
                return false;
            }
        });

    }

    private void onDownLoad(Uri url) {
        EaseDowmLoadImageService service = new EaseDowmLoadImageService(getApplicationContext(),
                url,
                new ImageDownLoadCallBack() {

                    @Override
                    public void onDownLoadSuccess(File file) {
                        //                        Toast.makeText(FristActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
                        //						Log.d("xiaohei","图片保存成功");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EaseShowBigImageActivity.this, "图片保存成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }


                    @Override
                    public void onDownLoadSuccess(Bitmap bitmap) {
                    }

                    @Override
                    public void onDownLoadFailed() {
                        //                        Toast.makeText(FristActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
                        Log.d("xiaohei", "图片保存失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EaseShowBigImageActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        //启动图片下载线程
        new Thread(service).start();
        image.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
    }

    /**
     * download image
     *
     * @param msgId
     */
    @SuppressLint("NewApi")
    private void downloadImage(final String msgId) {
        EMLog.e(TAG, "download with messageId: " + msgId);
        String str1 = getResources().getString(R.string.Download_the_pictures);
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(str1);
        pd.show();
        File temp = new File(localFilePath);
        final String tempPath = temp.getParent() + "/temp_" + temp.getName();
        final EMCallBack callback = new EMCallBack() {
            public void onSuccess() {
                EMLog.e(TAG, "onSuccess");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new File(tempPath).renameTo(new File(localFilePath));

                        DisplayMetrics metrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(metrics);
                        int screenWidth = metrics.widthPixels;
                        int screenHeight = metrics.heightPixels;

                        bitmap = ImageUtils.decodeScaleImage(localFilePath, screenWidth, screenHeight);
                        if (bitmap == null) {
                            image.setImageResource(default_res);
                        } else {
                            image.setImageBitmap(bitmap);
                            EaseImageCache.getInstance().put(localFilePath, bitmap);
                            isDownloaded = true;
                        }
                        if (isFinishing() || isDestroyed()) {
                            return;
                        }
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }
                });
            }

            public void onError(int error, String msg) {
                EMLog.e(TAG, "offline file transfer error:" + msg);
                File file = new File(tempPath);
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (EaseShowBigImageActivity.this.isFinishing() || EaseShowBigImageActivity.this.isDestroyed()) {
                            return;
                        }
                        image.setImageResource(default_res);
                        pd.dismiss();
                    }
                });
            }

            public void onProgress(final int progress, String status) {
                EMLog.d(TAG, "Progress: " + progress);
                final String str2 = getResources().getString(R.string.Download_the_pictures_new);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (EaseShowBigImageActivity.this.isFinishing() || EaseShowBigImageActivity.this.isDestroyed()) {
                            return;
                        }
                        pd.setMessage(str2 + progress + "%");
                    }
                });
            }
        };

        EMMessage msg = EMClient.getInstance().chatManager().getMessage(msgId);
        msg.setMessageStatusCallback(callback);

        EMLog.e(TAG, "downloadAttachement");
        EMClient.getInstance().chatManager().downloadAttachment(msg);
    }

    @Override
    public void onBackPressed() {
        if (isDownloaded)
            setResult(RESULT_OK);
        finish();

    }
}
