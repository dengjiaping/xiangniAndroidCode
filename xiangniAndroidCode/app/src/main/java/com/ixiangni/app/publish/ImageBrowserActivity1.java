package com.ixiangni.app.publish;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.handongkeji.common.HttpHelper;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.hyphenate.easeui.widget.photoview.EasePhotoView;
import com.hyphenate.easeui.widget.photoview.PhotoViewAttacher;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.CircleListBean.DataBean.PicListBean;
import com.ixiangni.common.FileHelper;
import com.ixiangni.ui.ImageLabel.TagInfo;
import com.ixiangni.ui.ImageLabel.TagView;
import com.ixiangni.ui.ImageLabel.TagViewLeft;
import com.ixiangni.ui.ImageLabel.TagViewRight;
import com.ixiangni.util.Constant1;
import com.ixiangni.util.NormalUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 图片浏览
 */
public class ImageBrowserActivity1 extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "ImageBrowserActivity1";

    public static List<PicListBean> mList;
    public static int position = 0;
    private final static int TEXTSIZE = 12;        //Tagview的字体大小

    private ViewPager mPager;
    private LinearLayout ll_back;

    private TextView textView_ImageBrowser, textView_ImageBrowser_download;
    private int maxWidth;
    private int maxHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imagebrowser);

        textView_ImageBrowser = (TextView) findViewById(R.id.textView_ImageBrowser);
        textView_ImageBrowser.setText((position % mList.size()) + 1 + "/" + mList.size());
        Constant1.scale = getResources().getDisplayMetrics().density;

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageBrowserActivity1.position = position;
                textView_ImageBrowser.setText((position % mList.size()) + 1 + "/" + mList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPager.setOffscreenPageLimit(mList.size());
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                maxWidth = mPager.getWidth();
                maxHeight = mPager.getHeight();
                mPager.setAdapter(new PagerAdapter() {
                    @Override
                    public int getCount() {
                        return mList.size();
                    }

                    @Override
                    public boolean isViewFromObject(View view, Object object) {
                        return view == object;
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position) {

                        View inflate = LayoutInflater.from(ImageBrowserActivity1.this).inflate(R.layout.item_image_browser_layout, container, false);
                        EasePhotoView photoView = (EasePhotoView) inflate.findViewById(R.id.photoView);
                        RelativeLayout labelContainer = (RelativeLayout) inflate.findViewById(R.id.label_container);
                        FrameLayout root = (FrameLayout) inflate.findViewById(R.id.root_layout);
                        ProgressBar progressBar = (ProgressBar) inflate.findViewById(R.id.progress_bar);

//                        photoView.enable();
                        //点击图片退出当前activity
                        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                            @Override
                            public void onPhotoTap(View view, float x, float y) {
                                finish();
                            }
                        });
                        photoView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                AlertDialog.Builder builder  = new AlertDialog.Builder(ImageBrowserActivity1.this);
                                String[] items = new String[]{"保存","取消"};
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch(which) {
                                            case 0:
                                            {
                                                Toast.makeText(ImageBrowserActivity1.this, "保存图片", Toast.LENGTH_SHORT).show();

                                                int currentItem = mPager.getCurrentItem();
                                                //            获取集合中存储的图片数据bean
                                                PicListBean picListBean = mList.get(currentItem);
                                                //            获取图片的url
                                                String inserturl = picListBean.getInserturl();
                                                //            截取/后边的数据
                                                String[] split = inserturl.split("/");
                                                //            创建存放图片的文件夹
                                                File dir = new File(getExternalCacheDir(), "images");
                                                //
                                                final File file = new File(dir, split[split.length - 1]);
                                                //            存放图片的文件夹是不是存在
                                                if (file.exists()) {
                                                    toast("已保存过");
                                                    return;
                                                }
                                                log(file.getAbsolutePath());

                                                //            toast("开始下载...");
                                                RemoteDataHandler.threadPool.execute(() -> {
                                                    try {

                                                        boolean b = FileHelper.creatDirIfNotExist(dir);

                                                        HttpHelper.downloadFile(inserturl, file, new OnResult<String>() {
                                                            @Override
                                                            public void onSuccess(String s) {
                                                                if (textView_ImageBrowser != null) {
                                                                    runOnUiThread(new Runnable() {
                                                                        @Override
                                                                        public void run() {
                                                                            if (mPager != null) {
                                                                                toast("已保存到" + file.getAbsolutePath());
                                                                                try {
                                                                                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), null, null);
                                                                                } catch (Exception e) {
                                                                                    e.printStackTrace();
                                                                                }

                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailed(String errorMsg) {

                                                            }
                                                        });
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }
                                                });


                                            }
                                            break;
                                            case 1:
                                                Toast.makeText(ImageBrowserActivity1.this, "取消", Toast.LENGTH_SHORT).show();
                                                break;
                                        }
                                    }
                                });
                                builder.show();
                                return false;
                            }
                        });

                        PicListBean picListBean = mList.get(position);
                        String inserturl = picListBean.getInserturl();

                        inserturl = inserturl.replace("_mid", "");


                        Glide.with(ImageBrowserActivity1.this).load(inserturl).asBitmap().placeholder(R.mipmap.loading_rect).into(new SimpleTarget<Bitmap>() {

                            @Override
                            public void onResourceReady(Bitmap loadedImage, GlideAnimation<? super Bitmap> glideAnimation) {

                                progressBar.setVisibility(View.GONE);
                                int width = loadedImage.getWidth();
                                int height = loadedImage.getHeight();

                                float scaleX = maxWidth * 1f / width;
                                float scaleY = maxHeight * 1f / height;

                                float scale = Math.min(scaleX, scaleY);
                                Matrix matrix = new Matrix();
                                matrix.postScale(scale, scale);

                                Bitmap bitmap = Bitmap.createBitmap(loadedImage, 0, 0, width, height, matrix, true);

                                int width1 = bitmap.getWidth();
                                int height1 = bitmap.getHeight();
                                ViewGroup.LayoutParams params = root.getLayoutParams();
                                params.width = width1;
                                params.height = height1;
                                root.setLayoutParams(params);

                                photoView.setImageBitmap(bitmap);

                                List<PicListBean.PicLabelListBean> labelList = picListBean.getPicLabelList();
                                for (PicListBean.PicLabelListBean bean : labelList) {
                                    String labelx = bean.getLabelx();
                                    String labely = bean.getLabely();
                                    String labelcontent = bean.getLabelcontent();
                                    float pointX = Float.valueOf(labelx) * width1;
                                    float pointY = Float.valueOf(labely) * height1;
                                    editTagInfo(labelcontent, pointX, pointY, labelContainer, width1, height1);
                                }
                            }
                        });


                        container.addView(inflate);
                        return inflate;
                    }

                    @Override
                    public void destroyItem(ViewGroup container, int position, Object object) {
                        container.removeView((View) object);
                    }
                });
                mPager.setCurrentItem(position);
            }
        });
    }

    /**
     * 编辑标签信息
     */
    private void editTagInfo(String text, float mPointX, float mPointY, ViewGroup labelContainer, int width, int height) {
        TagInfo tagInfo = new TagInfo();
        tagInfo.bid = 2L;
        tagInfo.bname = text;
        tagInfo.direct = getDirection(tagInfo.bname, mPointX, width);
        tagInfo.type = TagInfo.Type.CustomPoint;
        switch (tagInfo.direct) {
            case Left:
                tagInfo.leftMargin = (int) (mPointX - 15 * Constant1.scale);    //根据屏幕密度计算使动画中心在点击点，15dp是margin
                tagInfo.topMargin = (int) (mPointY - 15 * Constant1.scale);
                tagInfo.rightMargin = 0;
                tagInfo.bottomMargin = 0;
                break;
            case Right:
                tagInfo.leftMargin = 0;
                tagInfo.topMargin = (int) (mPointY - 15 * Constant1.scale);
                tagInfo.rightMargin = width - (int) mPointX - (int) (15 * Constant1.scale);
                tagInfo.bottomMargin = 0;
                break;
        }
        TagView tagView = null;
        switch (tagInfo.direct) {
            case Left:
                tagView = new TagViewLeft(this, null);
                break;
            case Right:
                tagView = new TagViewRight(this, null);
                break;
        }
        tagView.setData(tagInfo);
//        tagView.setTagViewListener(this);
        tagView.setTag(tagInfo);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(tagInfo.leftMargin, tagInfo.topMargin, tagInfo.rightMargin, tagInfo.bottomMargin);
        labelContainer.addView(tagView, params);

    }

    /**
     * 获取TagView的方向
     */
    private TagInfo.Direction getDirection(String str, float mPointX, int width) {
        float showSize = NormalUtils.GetTextWidth(str, TEXTSIZE * Constant1.scale);
        showSize += (32 * Constant1.scale);
        if ((mPointX + showSize) > width) {
            return TagInfo.Direction.Right;
        } else {
            return TagInfo.Direction.Left;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onClick(View v) {
        Log.v("reading", "" + v.getId());
    }


}
