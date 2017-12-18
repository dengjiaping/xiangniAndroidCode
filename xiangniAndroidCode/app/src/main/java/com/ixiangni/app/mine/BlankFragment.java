package com.ixiangni.app.mine;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.baidu.cloud.media.player.IMediaPlayer;
import com.baidu.cloud.videoplayer.widget.BDCloudVideoView;
import com.bumptech.glide.Glide;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.interfaces.Stringable;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.app.publish.AdvancedPlayActivity;
import com.ixiangni.app.publish.UploadPresenter;
import com.ixiangni.bean.FileListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.dialog.FileEditDialog;
import com.ixiangni.dialog.PhotoBroswdialog;
import com.ixiangni.interfaces.OnRefreshFile;
import com.ixiangni.presenters.FilPresenter;
import com.ixiangni.presenters.FileEdit;
import com.ixiangni.presenters.FileListPresenter;
import com.ixiangni.ui.SpaceItemDecoration;
import com.ixiangni.util.BDMediaPlayerHelper;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.GridDecoration;
import com.ixiangni.util.LinearDecoration;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 文件fragment
 *
 * @ClassName:BlankFragment
 * @PackageName:com.ixiangni.app.mine
 * @Create On 2017/7/17 0017   10:34
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/17 0017 handongkeji All rights reserved.
 */
public class BlankFragment extends BaseFragment implements OnResult<List<FileListBean.DataBean>>,
        SmartPullableLayout.OnPullListener, OnRefreshFile,
        IMediaPlayer.OnPreparedListener, FileEdit.IFileView {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    private String folderid;
    private int fileType;
    private FileListPresenter presenter;
    private int pageSize = 25;
    private int currentPage = 1;
    private MyRvAdapter<FileListBean.DataBean> mAdapter;
    private BDCloudMediaPlayer mediaPlayer;
    private PhotoBroswdialog broswdialog;
    private FilPresenter filPresenter;


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SuperObservableManager
                .getInstance()
                .getObservable(OnRefreshFile.class)
                .registerObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SuperObservableManager
                .getInstance()
                .getObservable(OnRefreshFile.class)
                .unregisterObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        //文件夹id
        folderid = arguments.getString(XNConstants.folderid);
        //文件类型
        fileType = arguments.getInt(XNConstants.FILE_TYPE, 4);
        presenter = new FileListPresenter(getContext(), this);
        smartPullLayout.setOnPullListener(this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        if (fileType == UploadPresenter.UP_IMG) {
            manager = new GridLayoutManager(getContext(), 4);
            recyclerView.addItemDecoration(new GridDecoration(4, 5, 0xffffff));
        }
        if (fileType == UploadPresenter.UP_VIDEO) {
            manager = new GridLayoutManager(getContext(), 2);
            recyclerView.addItemDecoration(new GridDecoration(2, 5, 0xffffff));
        }
        if (fileType == UploadPresenter.UP_AUDIO) {
            recyclerView.addItemDecoration(new LinearDecoration(2, 0xaaaaaa, LinearDecoration.VERTICAL));
        }
        if (fileType == UploadPresenter.UP_TEXT) {
            recyclerView.addItemDecoration(new LinearDecoration(2, 0xaaaaaa, LinearDecoration.VERTICAL));
        }

        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = createAdapter();

        setEditListener(mAdapter);

        recyclerView.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    stateLayout.showNoDataView("暂无文件");
                } else {
                    stateLayout.showContenView();
                }
            }
        });

        //初始化数据
        stateLayout.showLoadView("加载中...");
        onPullDown();


        filPresenter = new FilPresenter(getContext(), this);

    }


    /**
     * 长按点击
     *
     * @param mAdapter
     */
    private void setEditListener(MyRvAdapter<FileListBean.DataBean> mAdapter) {
        mAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                showDeletedialog(position);

                return true;
            }
        });
    }

    private void showDeletedialog(final int position) {
        FileEditDialog fileEditDialog = new FileEditDialog(getContext());
        fileEditDialog.setEditListener(new FileEditDialog.OnEditListener() {

            @Override
            public void onFileDelete() {
                FileListBean.DataBean item = mAdapter.getItem(position);

                String recordid = "" + item.getRecordid();
                filPresenter.deleteFile(position, recordid);

            }
        });

        fileEditDialog.show();
    }


    public void play(String audiourl) {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = new BDCloudMediaPlayer(getContext());
            mediaPlayer.setDataSource(audiourl);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(IMediaPlayer iMediaPlayer) {
                    AudioAdapter mAdapter = (AudioAdapter) BlankFragment.this.mAdapter;

                    mAdapter.setPlayPoi(-1);
                    mAdapter.notifyDataSetChanged();

                }
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private MyRvAdapter<FileListBean.DataBean> createAdapter() {

        MyRvAdapter<FileListBean.DataBean> adapter = null;
        adapter = new TextAdapter(getContext());

        switch (fileType) {
            case UploadPresenter.UP_IMG:
                adapter = new ImageAdapter(getContext());
                break;
            case UploadPresenter.UP_TEXT:
                adapter = new TextAdapter(getContext());
                break;
            case UploadPresenter.UP_VIDEO:
                adapter = new VideoAdapter(getContext());
                break;
            case UploadPresenter.UP_AUDIO:
                adapter = new AudioAdapter(getContext());
                break;
        }

        return adapter;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onSuccess(List<FileListBean.DataBean> dataBeen) {
        if (stateLayout == null) {
            return;
        }
        Observable.from(dataBeen)
                .filter(new Func1<FileListBean.DataBean, Boolean>() {
                    @Override
                    public Boolean call(FileListBean.DataBean dataBean) {
                        if (dataBean.getRecordtype() == fileType) {
                            return true;
                        }
                        return false;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FileListBean.DataBean>>() {


                    @Override
                    public void call(List<FileListBean.DataBean> dataBeen) {

                        smartPullLayout.stopPullBehavior();
                        if (currentPage == 1) {
                            mAdapter.replaceAll(dataBeen);
                        } else {
                            mAdapter.addAll(dataBeen);
                        }
                        if (fileType == UploadPresenter.UP_IMG) {
                            List<FileListBean.DataBean> dataList = mAdapter.getDataList();
                            broswdialog = new PhotoBroswdialog(getContext(), dataList);
                        }
                    }
                });
    }

    @Override
    public void onFailed(String errorMsg) {

        stateLayout.showNoDataView("暂无数据");

    }

//    token	是	String	token
//    folderid	是	Long	文件夹id
//    pageSize	是	Int	每页条数
//    currentPage	是	Int	当前页数

    private HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.folderid, folderid);
        params.put(XNConstants.currentPage, currentPage + "");
        params.put(XNConstants.pageSize, "" + pageSize);
        return params;
    }

    @Override
    public void onPullDown() {

        if (fileType == UploadPresenter.UP_AUDIO) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
        currentPage = 1;
        presenter.initFileData(getParams());

    }


    @Override
    public void onPullUp() {

        currentPage++;
        presenter.initFileData(getParams());
    }

    @Override
    public void refreshFile(int fileType) {
        if (fileType == this.fileType) {
            onPullDown();
        }
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        iMediaPlayer.start();
    }

    @Override
    public void showLoading(String message) {
        showProgressDialog(message, true);
    }

    @Override
    public void loadingFinish() {

        dismissProgressDialog();
    }

    @Override
    public void showToast(String message) {
        toast(message);
    }

    @Override
    public void onDeleteSuccess(int posiion) {
        mAdapter.remove(posiion);
    }


    private class TextAdapter extends MyRvAdapter<FileListBean.DataBean> {

        public TextAdapter(Context context) {
            super(context, R.layout.item_word);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, FileListBean.DataBean data) {

            String textcotent = data.getTextcotent();
            holder.setText(R.id.tv_content, textcotent);
            long createtime = data.getCreatetime();
            String ymd = DateUtil.getYmd(createtime);
            holder.setText(R.id.tv_create_time, ymd);
        }
    }

    private static final String TAG = "BlankFragment";

    private class ImageAdapter extends MyRvAdapter<FileListBean.DataBean> {


        public ImageAdapter(Context context) {
            super(context, R.layout.file_pic_layout);
            Log.i(TAG, "fileType: " + fileType);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, FileListBean.DataBean data) {
            ImageView iv = holder.getView(R.id.iv_head);
            String mediapic = data.getMemortydir();
            Log.i(TAG, "bindItemView: " + mediapic);
            Glide.with(context).load(mediapic).placeholder(R.mipmap.loading_rect).into(iv);
            holder.setText(R.id.tv_time, DateUtil.getYmd(data.getCreatetime()));
            iv.setOnClickListener(v -> broswdialog.show(position));
            iv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeletedialog(position);
                    return true;
                }
            });
        }
    }

    /**
     * 视频adapter
     */
    private class VideoAdapter extends MyRvAdapter<FileListBean.DataBean> {

        public VideoAdapter(Context context) {
            super(context, R.layout.item_video);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, FileListBean.DataBean data) {
            Glide.with(context)
                    .load(data.getMediapic())
                    .placeholder(R.mipmap.loading_rect)
                    .into((ImageView) holder.getView(R.id.iv_head));
            long createtime = data.getCreatetime();//上传时间
            String ymd = DateUtil.getYmd(createtime);
            holder.setText(R.id.tv_time, ymd);
            View view = holder.getView(R.id.iv_play);
            view.setOnClickListener(v -> {
                if (1 == data.getDelmark()) {
                    toast("视频转码中,请稍后刷新重试");
                    return;
                }
                AdvancedPlayActivity.start(getActivity(), data.getMemortydir());
            });
        }
    }

    /**
     * 语音
     */
    private class AudioAdapter extends MyRvAdapter<FileListBean.DataBean> {


        public AudioAdapter(Context context) {
            super(context, R.layout.item_audio);
        }


        public int getPlayPoi() {
            return playPoi;
        }

        public void setPlayPoi(int playPoi) {
            this.playPoi = playPoi;
        }

        private int playPoi = -1;

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, FileListBean.DataBean data) {
            long createtime = data.getCreatetime();
            String ymd = DateUtil.getYmd(createtime);
            String mediatime = data.getMediatime();
            holder.setText(R.id.tv_time, ymd);
            holder.setText(R.id.tv_audio_time, mediatime);

            View view = holder.getView(R.id.iv_arrow);
            view.setSelected(playPoi == position);

            view.setOnClickListener(v -> {

                int delmark = data.getDelmark();
                if (1 == delmark) {
                    toast("音频转码中,请稍后刷新重试");
                    return;
                }
                play(data.getMemortydir());
                playPoi = position;
                notifyDataSetChanged();

            });

        }
    }


}
