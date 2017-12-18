package com.ixiangni.app.mine;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.handongkeji.common.QFragmentPagerAdapter;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.upload.UpLoadPresenter;
import com.handongkeji.utils.Bimp;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsManager;
import com.hyphenate.chatuidemo.runtimepermissions.PermissionsResultAction;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.publish.PublishAudioActivity;
import com.ixiangni.app.publish.PublishPictActivity;
import com.ixiangni.app.publish.PublishVideoActivity;
import com.ixiangni.app.publish.PublishWordActivity;
import com.ixiangni.app.publish.RecordVideoActivity;
import com.ixiangni.app.publish.UploadPresenter;
import com.ixiangni.app.publish.UploadtextActivity;
import com.ixiangni.bean.FileListBean;
import com.ixiangni.common.BusAction;
import com.ixiangni.common.FileFragmentFactory;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnRefreshFile;
import com.ixiangni.interfaces.OnUserInfoChange;
import com.ixiangni.presenters.FileListPresenter;
import com.ixiangni.presenters.contract.FileGetDataState;
import com.ixiangni.presenters.contract.FileRefreshState;
import com.ixiangni.util.BaiduUpLoad;
import com.ixiangni.util.UriUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.internal.operators.OnSubscribeDetach;
import rx.schedulers.Schedulers;

/**
 * 浏览文件夹里的文件
 *
 * @ClassName:FileBrwseActivity
 * @PackageName:com.ixiangni.app.mine
 * @Create On 2017/6/21 0021   16:22
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class FileBrwseActivity extends BaseActivity implements UpLoadPresenter.UpLoadImagesCallback {

    @Bind(R.id.iv_add_file)
    ImageView ivAddFile;
    @Bind(R.id.vp_file)
    ViewPager vpFile;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    private String folderid;
    private int position = 0;
    private int pageSize = 10;
    private int currentPage = 1;
    private List<FileListBean.DataBean> picList;
    private List<FileListBean.DataBean> textList;
    private List<FileListBean.DataBean> videoList;
    private List<FileListBean.DataBean> audioList;
    private List<Fragment> fragmentList;
    private UploadPresenter presenter;
    private AlertDialog alertDialog;
    private FFmpeg fFmpeg;
    private String upLoadPath;
    private String inserturl;
    private int totalSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_brwse);
        ButterKnife.bind(this);
        folderid = getIntent().getStringExtra("folderid");
        initViewPager();

        ImagePicker instance = ImagePicker.getInstance();
        instance.setMultiMode(true);
        instance.setSelectLimit(9);

        presenter = new UploadPresenter();

        fFmpeg = FFmpeg.getInstance(this);
    }

    private void initViewPager() {

        fragmentList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Fragment fragment = new BlankFragment();

            Bundle bundle = new Bundle();
            bundle.putInt(XNConstants.FILE_TYPE, getTypeBypoi(i));
            bundle.putString(XNConstants.folderid, folderid);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }

        QFragmentPagerAdapter adapter = new QFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);

        //设置title
        adapter.setTitles(Arrays.asList(getResources().getStringArray(R.array.filetype)));

        vpFile.setOffscreenPageLimit(fragmentList.size());
        vpFile.setAdapter(adapter);
        //关联tablayout与viewpager
        tabLayout.setupWithViewPager(vpFile);
        UploadPresenter presenter = new UploadPresenter();


    }

    private int getTypeBypoi(int i) {
        int type = 4;

        switch (i) {
            case 0:
                type = UploadPresenter.UP_IMG;
                break;
            case 1:
                type = UploadPresenter.UP_TEXT;
                break;
            case 2:
                type = UploadPresenter.UP_VIDEO;
                break;
            case 3:
                type = UploadPresenter.UP_AUDIO;
                break;
            default:
                break;
        }

        return type;
    }

    private int REQUEST_CODE_SELECT = 100;

    @OnClick(R.id.iv_add_file)
    public void onViewClicked() {
        Intent intent = null;

        switch (vpFile.getCurrentItem()) {
            case 0://图片
//                startActivity(new Intent(this, PublishPictActivity.class).putExtra(BusAction.PIC_TAG,BusAction.PIC_TAG_ADD));
                intent = new Intent(this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, false); // 是否是直接打开相机
                startActivityForResult(intent, REQUEST_CODE_SELECT);

                break;
            case 1://上传文字
//                startActivity(UploadtextActivity.class);
                intent = new Intent(FileBrwseActivity.this, UploadtextActivity.class);
                intent.putExtra(XNConstants.folderid, folderid);
                startActivity(intent);

                break;
            case 2://上传视频
                PermissionsManager manager = PermissionsManager.getInstance();
                if (!manager.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    manager.requestPermissionsIfNecessaryForResult(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            new PermissionsResultAction() {
                                @Override
                                public void onGranted() {
                                    seleLocalVideo();

                                }

                                @Override
                                public void onDenied(String permission) {

                                    toast("您拒绝了" + permission + ",无法上传视频.");
                                }
                            });
                } else {
                    seleLocalVideo();
                }


                break;
            case 3://上传语音

                intent = new Intent(this, RecordAudioActivity.class);
                startActivityForResult(intent, 91);
                break;

        }
    }

    private void seleLocalVideo() {
        Intent intent;//                startActivity(RecordVideoActivity.class);
        intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        startActivityForResult(intent, 99);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                upLoadImages(images);

            }
        } else if (requestCode == 99 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri == null) {
                toast("文件损坏");
                return;
            }

            String path = UriUtils.getPath(FileBrwseActivity.this, uri);
            File file = new File(path);

            log(uri + ":" + uri.toString());

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(this, uri);
            String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            int vd = Integer.parseInt(duration);
            if (vd / 1000 > 10) {
                toast("视频不能超过10秒钟");

            } else {
                upLoadVideo(path);

            }

        } else if (requestCode == 91 && resultCode == RESULT_OK) {
            String audiopath = data.getStringExtra(XNConstants.audio_path);
            upLoadAudio(audiopath, data.getStringExtra(XNConstants.audio_duration));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private void upLoadAudio(String path, String stringExtra) {
        final String inserturl = UUID.randomUUID() + ".amr";
        BaiduUpLoad load = new BaiduUpLoad(path, BaiduUpLoad.BUCKET_AUDIO, inserturl);


        showProgressDialog("上传中...", true);
        load.upload(new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                if (!mProgressDialog.isShowing()) {
                    //用户取消上传
                    return;
                }


                mProgressDialog.setMessage("上传中...");
                presenter.upload(FileBrwseActivity.this, folderid, UploadPresenter.UP_AUDIO, inserturl, null, "name", stringExtra, new OnResult<String>() {
                    @Override
                    public void onSuccess(String s) {

                        SuperObservableManager.notify(OnRefreshFile.class, onRefreshFile -> onRefreshFile.refreshFile(UploadPresenter.UP_AUDIO));
                        dismissProgressDialog();
                    }

                    @Override
                    public void onFailed(String errorMsg) {

                        if (tabLayout != null) {
                            toast(errorMsg);
                            dismissProgressDialog();
                        }
                    }
                });
            }

            @Override
            public void onFailed(String errorMsg) {

                dismissProgressDialog();
                toast(errorMsg);
            }
        });

    }


    private boolean isCancelVideo = false;

    private void upLoadVideo(String path) {

        upLoadPath = path;

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(upLoadPath);
        String duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        totalSecond = Integer.parseInt(duration) / 1000;


        isCancelVideo = false;
        Uri parse = Uri.parse(path);
        String lastPathSegment = parse.getLastPathSegment();
        log(lastPathSegment);
        inserturl = UUID.randomUUID() + lastPathSegment;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("准备中...")
                .setView(new ProgressBar(this))
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isCancelVideo = true;
                    }
                });
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();

        try {
            fFmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    upLoad(upLoadPath, inserturl);
                }

                @Override
                public void onSuccess() {
                    compress();
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
            upLoad(upLoadPath, inserturl);

        }

    }

    public static int getSecond(String progress) {
        int indexOf = progress.indexOf("time");
        String subSequence = progress.substring(indexOf + 5, indexOf + 5 + 8);
        String[] split = subSequence.split(":");
        int result = 0;
        if (split.length >= 3) {
            int hour = Integer.parseInt(split[0]);
            int min = Integer.parseInt(split[1]);
            int second = Integer.parseInt(split[2]);
            result = second + min * 60 + hour * 60 * 60;
        }
        return result;
    }

    /**
     * 压缩视频
     */
    private void compress() {
        alertDialog.setTitle("压缩中...");
        File file = new File(getExternalCacheDir(), "out.mp4");
        if (file.exists()) {
            file.delete();
        }

        final String outPath = file.getAbsolutePath();
        String cmd1 = "-y -i " + upLoadPath + " " + outPath;
        try {
            fFmpeg.execute(cmd1.split(" "), new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String message) {
                    upLoadPath = outPath;
                }

                @Override
                public void onProgress(String message) {

                    log(message);
                    if (message.startsWith("frame")) {
                        int second = getSecond(message);
                        if (second > 0) {
                            second -= 1;
                        }
                        if (totalSecond > 0) {

                            int i = second * 100 / totalSecond;

                            String text = String.format(Locale.CHINA, "压缩中 %d", i);
                            text += "%";

                            alertDialog.setTitle(text);
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
                    upLoad(upLoadPath, inserturl);

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
            upLoad(upLoadPath, inserturl);
        }

    }

    private void upLoad(String path, final String inserturl) {

        //取消
        if (isCancelVideo) {
            return;
        }
        if (alertDialog != null) {
            alertDialog.setTitle("上传中...");
        }
        BaiduUpLoad load = new BaiduUpLoad(path, BaiduUpLoad.BUCKET_VIDEO, inserturl);

        load.setCallback(new BaiduUpLoad.OnProgressCallback() {
            @Override
            public void onProgress(int completePart, int totalpart) {
                if(tabLayout==null){
                    return;
                }
                if (!isCancelVideo) {

                    String text = "上传中 ";
                    int progress = completePart * 100 / totalpart;
                    String pro = progress + "%";
                    text += pro;
                    alertDialog.setTitle(text);
                }
            }
        });
        load.upload(new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                if (isCancelVideo) {
                    alertDialog.dismiss();
                    return;
                }

                log(folderid);
                log(inserturl);

                presenter.upload(FileBrwseActivity.this, folderid, UploadPresenter.UP_VIDEO, inserturl, null, null, null, new OnResult<String>() {
                    @Override
                    public void onSuccess(String s) {
                        SuperObservableManager
                                .getInstance()
                                .getObservable(OnRefreshFile.class)
                                .notifyMethod(onRefreshFile -> onRefreshFile.refreshFile(UploadPresenter.UP_VIDEO));
                        alertDialog.dismiss();
                        toast("上传成功");

                    }

                    @Override
                    public void onFailed(String errorMsg) {
                        alertDialog.dismiss();
                        toast(errorMsg);

                    }
                });
            }

            @Override
            public void onFailed(String errorMsg) {

                alertDialog.dismiss();

                toast(errorMsg);
            }
        });
    }

    private void upLoadImages(ArrayList<ImageItem> images) {
        Observable.from(images)
                .map(new Func1<ImageItem, File>() {
                    @Override
                    public File call(ImageItem imageItem) {


                        return new File(imageItem.path);
                    }
                })
                .toList()
                .subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<File>>() {
                    @Override
                    public void call(List<File> files) {
                        showProgressDialog("上传中...", true);
                        UpLoadPresenter.upLoadMultiImage(FileBrwseActivity.this, UrlString.URL_UPLOAD_IMAGE, files, FileBrwseActivity.this);
                    }
                });


    }

    @Override
    public void onUploadFailed(int position, String imagePath) {

    }

    @Override
    public void onAllUpLoadFinish(List<String> imgUrls) {


        if (tabLayout != null) {
            log(imgUrls.toString());
            String string = ListUtil.toCommaString(imgUrls);
            log(string);
            presenter.upload(this, folderid, UploadPresenter.UP_IMG, string, null, null, null, new OnResult<String>() {
                @Override
                public void onSuccess(String s) {
                    if (tabLayout != null) {
                        toast("上传成功！");
                        dismissProgressDialog();

                        SuperObservableManager
                                .getInstance()
                                .getObservable(OnRefreshFile.class)
                                .notifyMethod(onRefreshFile -> onRefreshFile.refreshFile(UploadPresenter.UP_IMG));
                    }
                }

                @Override
                public void onFailed(String errorMsg) {
                    if (tabLayout != null) {
                        toast(errorMsg);
                        dismissProgressDialog();
                    }
                }
            });


        }

    }
}
