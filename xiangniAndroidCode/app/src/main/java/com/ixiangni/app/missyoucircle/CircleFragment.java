package com.ixiangni.app.missyoucircle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.baidu.cloud.videoplayer.demo.info.VideoInfo;
import com.clcus.EmojiInpuLayout;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.DemoHelper;
import com.ixiangni.adapter.CircleAdapter;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.login.LoginState;
import com.ixiangni.app.publish.AdvancedPlayActivity;
import com.ixiangni.bean.CircleListBean;
import com.ixiangni.bean.CommentBean;
import com.ixiangni.bean.LikeBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.EmojiDialog;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.dialog.Publishdialog;
import com.ixiangni.interfaces.OnHideBottomTab;
import com.ixiangni.interfaces.OnNewsPublish;
import com.ixiangni.presenters.CirclePresenter;
import com.ixiangni.presenters.CommentConfig;
import com.ixiangni.presenters.DeleteCirclePresenter;
import com.ixiangni.presenters.contract.CircleContract;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.BDMediaPlayerHelper;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.ob.SuperObservable;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class CircleFragment extends BaseFragment implements SmartPullableLayout.OnPullListener,
        CircleAdapter.OnHandEnvent, OnResult<String>, CircleContract.View {


    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.list_view_circle)
    ListView listViewCircle;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;

    @Bind(R.id.emoji_input_layout)
    EmojiInpuLayout emojiInputLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.bodyLayout)
    RelativeLayout bodyLayout;


    private Publishdialog pd;
    private Handler handler = new Handler();

    //    token
    private int currentPage = 1;
    private int pageSize = 15;
    private CircleAdapter mCircleAdapter;
    private DeleteCirclePresenter deleteCirclePresenter;
    private ProgressDialog mProgressdialog;
    private BDMediaPlayerHelper helper;
    private BDCloudMediaPlayer bdCloudMediaPlayer;
    private EditText editText;
    private TextView sendTv;
    private CirclePresenter presenter;
    private EmojiDialog emoDialog;

    private int currentKeyboardH;

    private CommentConfig commentConfig;
    private int screenHeight;
    private int editTextBodyHeight;
    private String reply;
    private InputMethodManager im;
    private View lastVoiceView;

    public CircleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        SuperObservableManager instance = SuperObservableManager.getInstance();
        instance.getObservable(OnNewsPublish.class)
                .registerObserver(newPublishObserver);
        instance.getObservable(LoginState.class).registerObserver(loginState);
    }

    private LoginState loginState = new LoginState() {
        @Override
        public void onLogin() {
            currentPage = 1;
            getMyCircleList();
        }

        @Override
        public void onLogout() {

            mCircleAdapter.clear();
        }
    };

    private OnNewsPublish newPublishObserver = new OnNewsPublish() {
        @Override
        public void onNewpublish() {
            currentPage = 1;
            autoRefresh = true;
            getMyCircleList();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_circle, container, false);
        ButterKnife.bind(this, view);

        initAdapter();

        presenter = new CirclePresenter(this, getContext());
        mCircleAdapter.setCirclePresenter(presenter);


        pd = new Publishdialog(getContext());
        topBar.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
            }
        });

        emojiInputLayout.setListener(view1 -> {
            String content = emojiInputLayout.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                toast("内容不能为空!");
            } else {
                hideCommentUI();
                presenter.addComment(content, commentConfig);
            }
        });

        return view;
    }


    /**
     * 获取状态栏高度
     *
     * @param
     */
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        im = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        listViewCircle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (emojiInputLayout.getVisibility() == View.VISIBLE) {
                    hideCommentUI();
                }

                return false;
            }
        });
    }


    private void initAdapter() {
        smartPullLayout.setOnPullListener(this);


        mCircleAdapter = new CircleAdapter(getContext());


        mCircleAdapter.setEnvent(this);

        listViewCircle.setAdapter(mCircleAdapter);
        stateLayout.setUpwithBaseAdapter(mCircleAdapter, "暂无动态");


        //删除朋友圈动态
        deleteCirclePresenter = new DeleteCirclePresenter();


        //第一次加载想你圈动态

        if (LoginHelper.getInstance().isLogin(getContext())) {

            getMyCircleList();
        }

        initPlayer();
    }

    private void initPlayer() {
        helper = new BDMediaPlayerHelper();
        bdCloudMediaPlayer = helper.get(getContext());
        helper.setOnComplete(new BDMediaPlayerHelper.OnComplete() {
            @Override
            public void onComplete() {

                //释放当前播放语音状态
                if (lastVoiceView != null) {
                    lastVoiceView.setSelected(false);
                    //充值
                    mCircleAdapter.setPlayingAudioPosition(-1);
                }
            }
        });

    }

    private static final String TAG = "CircleFragment";


    private boolean isLoading = false;

    public void refresh() {
        if (!isLoading) {
            onPullDown();
        }
    }

    private boolean autoRefresh = true;

    private void getMyCircleList() {

        if (isLoading) {
            return;
        }

        if (autoRefresh) {
            if(stateLayout!=null){
            stateLayout.showLoadView();
            }
        }

        isLoading = true;

        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("currentPage", currentPage + "");
        params.put("pageSize", pageSize + "");

        RemoteDataHandler.asyncPost(UrlString.URL_MY_CIRCRLE, params, getContext(), true, callback -> {
            if (smartPullLayout == null) {
                return;
            }
            smartPullLayout.stopPullBehavior();
            isLoading = false;
            autoRefresh = false;

            String json = callback.getJson();
            if (TextUtils.isEmpty(json)) {
                toast(Constants.CONNECT_SERVER_FAILED);
            } else {
                CircleListBean circleListBean = new Gson().fromJson(json, CircleListBean.class);
                if (1 == circleListBean.getStatus()) {
                    List<CircleListBean.DataBean> dataBeanList = circleListBean.getData();
                    if (ListUtil.isEmptyList(dataBeanList) || dataBeanList.size() < pageSize) {
                        smartPullLayout.setPullUpEnabled(false);
                    }
                    if (1 == currentPage) {
                        mCircleAdapter.replaceAll(dataBeanList);
                        smartPullLayout.setPullUpEnabled(true);
                        listViewCircle.setSelection(0);
                        getActivity().sendBroadcast(new Intent(XNConstants.refresh_complete));
                    } else {
                        mCircleAdapter.addAll(dataBeanList);
                    }

                } else {
                    toast(circleListBean.getMessage());
                }
            }

            Log.i(TAG, "getMyCircleList: " + callback.getJson());

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    //刷新
    @Override
    public void onPullDown() {
        currentPage = 1;
        if (bdCloudMediaPlayer != null && bdCloudMediaPlayer.isPlaying()) {
            bdCloudMediaPlayer.stop();
        }
        getMyCircleList();


    }

    @Override
    public void onPullUp() {

        currentPage++;
        getMyCircleList();
    }


    private int deletePoi = -1;

    @Override
    public void onDeleteCircle(int position, final String momentnewsid) {
        deletePoi = position;
        MyAlertdialog alertdialog = new MyAlertdialog(getContext());
        alertdialog.setTitle("提示")
                .setMessage("您确定要删除该条动态？")
                .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                    @Override
                    public void onLeftClick(View view) {

                    }

                    @Override
                    public void onRightClick(View view) {
                        mProgressdialog = ProgressDialog.show(getContext(), null, "删除中", true, false);
                        deleteCirclePresenter.delete(getContext(), momentnewsid, CircleFragment.this);
                    }
                });
        alertdialog.show();
    }


    private String lastAudioPath = "";

    /**
     * 播放音频
     *
     * @param audioPath
     */
    @Override
    public void onPlayAudio(int position, View voiceView, String audioPath) {


        //同一个音频
        if (lastAudioPath.equals(audioPath)) {
            if (bdCloudMediaPlayer != null && bdCloudMediaPlayer.isPlaying()) {
                bdCloudMediaPlayer.stop();
                voiceView.setSelected(false);
            } else {
                //标记当前语音播放状态
                mCircleAdapter.setPlayingAudioPosition(position);
                voiceView.setSelected(true);

                lastVoiceView = voiceView;
                helper.prepare(audioPath);
                lastAudioPath = audioPath;
            }

        } else {//不是同一个音频

            //取消上个语音的选中状态
            if (lastVoiceView != null) {
                lastVoiceView.setSelected(false);
            }

            //标记当前语音播放状态
            mCircleAdapter.setPlayingAudioPosition(position);
            voiceView.setSelected(true);

            lastVoiceView = voiceView;
            helper.prepare(audioPath);
            lastAudioPath = audioPath;

        }

    }

    /**
     * 播放视频
     *
     * @param videoPath
     */
    @Override
    public void onPlayVideo(String videoPath) {

        Intent intent = new Intent(getContext(), AdvancedPlayActivity.class);
        VideoInfo info = new VideoInfo("视频", videoPath);
        intent.putExtra("videoInfo", info);
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);

    }

    @Override
    public void onStop() {
        super.onStop();
        hideCommentUI();
        if (bdCloudMediaPlayer != null) {
            bdCloudMediaPlayer.stop();
        }
    }

    @Override
    public void onSuccess(String s) {
        if (mProgressdialog != null) {
            mProgressdialog.dismiss();
            toast("删除成功！");
            if (deletePoi >= 0 && deletePoi < mCircleAdapter.getCount()) {
                mCircleAdapter.remove(deletePoi);
            }
        }
    }

    @Override
    public void onFailed(String errorMsg) {
        if (mProgressdialog != null) {
            mProgressdialog.dismiss();
            toast(errorMsg);
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        SuperObservableManager instance = SuperObservableManager.getInstance();
        instance.getObservable(OnNewsPublish.class).unregisterObserver(newPublishObserver);
        instance.getObservable(LoginState.class).unregisterObserver(loginState);
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String errorMsg) {

    }


    @Override
    public void update2DeleteCircle(String circleId) {

    }

    /**
     * 点赞取消点赞
     *
     * @param adapterPosition
     * @param flag            true 点赞，反之
     */
    @Override
    public void update2AddFavorite(int adapterPosition, boolean flag) {
        CircleListBean.DataBean itemData = mCircleAdapter.getItemData(adapterPosition);
        List<LikeBean> likeList = itemData.getLikeList();
        if (ListUtil.isEmptyList(likeList)) {
            likeList = new ArrayList<>();
        }
        int userid = Integer.parseInt(LoginHelper.getInstance().getUserid(getContext()));

        if (flag) {
            itemData.setIslike(1);
            itemData.setLikenum(itemData.getLikenum() + 1);

            LikeBean likeBean = new LikeBean();
            likeBean.setUsernick(DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo().getNickname());
            likeBean.setMomentnewsid(itemData.getMomentnewsid());
            likeBean.setUserid(userid);
            likeList.add(likeBean);

        } else {
            itemData.setIslike(0);
            itemData.setLikenum(itemData.getLikenum() - 1);

            for (int i = 0; i < likeList.size(); i++) {
                LikeBean likeBean = likeList.get(i);
                if (likeBean.getUserid() == userid) {
                    likeList.remove(i);
                    break;
                }
            }
        }
        itemData.setLikeList(likeList);
        mCircleAdapter.notifyDataSetChanged();

    }


    /**
     * 添加评论
     *
     * @param circlePosition
     * @param addItem
     */
    @Override
    public void update2AddComment(int circlePosition, CommentBean addItem) {

        if (addItem != null) {
            CircleListBean.DataBean item = (CircleListBean.DataBean) mCircleAdapter.getDatas().get(circlePosition);//刷新评论的集合
            List<CommentBean> commentList = item.getCommentList();
            if (commentList == null) {
                commentList = new ArrayList<>();
                item.setCommentList(commentList);
            }
            commentList.add(addItem);
            item.setCommentnum(item.getCommentnum() + 1);
            mCircleAdapter.notifyDataSetChanged();

        }
        emojiInputLayout.getEditText().setText("");
        hideCommentUI();

    }

    private void hideCommentUI() {
        //隐藏评论界面

        hideComment(SuperObservableManager.getInstance().getObservable(OnHideBottomTab.class));
    }

    /**
     * 删除评论
     *
     * @param circlePosition
     * @param commentId
     */
    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {

        CircleListBean.DataBean item = (CircleListBean.DataBean) mCircleAdapter.getDatas().get(circlePosition);//这个是朋友圈的评论的position
        List<CommentBean> commentList = item.getCommentList();
        if (commentList != null) {
            for (int i = 0; i < commentList.size(); i++) {
                CommentBean commentBean = commentList.get(i);
                if (commentId.equals(commentBean.getCommentid())) {
                    commentList.remove(i);
                    item.setCommentnum(item.getCommentnum() - 1);
                    mCircleAdapter.notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    @Override
    public void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig) {

        boolean sameLastComment = true;//是同一个评论则关闭
        if (this.commentConfig == null) {
            sameLastComment = false;
        } else {
            String momentnewsid = this.commentConfig.momentnewsid;
            if (!momentnewsid.equals(commentConfig.momentnewsid)) {
                sameLastComment = false;
            }
        }

        this.commentConfig = commentConfig;

        SuperObservable<OnHideBottomTab> hideTabObservable = SuperObservableManager.getInstance().getObservable(OnHideBottomTab.class);
        boolean isShowing = emojiInputLayout.getVisibility() == View.VISIBLE;

        if (isShowing) {
            if (sameLastComment) {
                hideComment(hideTabObservable);
            }
        } else {

            emojiInputLayout.setVisibility(View.VISIBLE);
            if (commentConfig != null) {
                if (commentConfig.commentType.equals(CommentConfig.Type.REPLY)) {
                    commentConfig.plusernick = CommonUtils.isStringNull(commentConfig.plusernick) ? "" : commentConfig.plusernick;
                    reply = "回复 " + commentConfig.plusernick + ":";
                } else {
                    reply = "";
                }
            }
            EditText emojiInputEditText = emojiInputLayout.getEditText();
            emojiInputEditText.requestFocus();
            emojiInputEditText.setHint(reply);


            emojiInputLayout.showInput();


            //隐藏mianActivity的bottomTabLayout
            hideTabObservable.notifyMethod(onHideBottomTab -> {
                onHideBottomTab.isHide(false);
            });
        }


////        measureCircleItemHighAndCommentItemOffset(commentConfig);
//        if (View.VISIBLE == visibility) {
//            editText.requestFocus();
//            //弹出键盘
//            CommonUtils_jp.showSoftInput(editText.getContext(), editText);
//            EventBus.getDefault().post("Gone", "MainSetVisible");//让mainActivity里面的底部消失
//        } else if (View.GONE == visibility) {
//            //隐藏键盘
//            CommonUtils_jp.hideSoftInput(editText.getContext(), editText);
//            EventBus.getDefault().post("Visible", "MainSetVisible");//让mainActivity里面的底部显示
//        }
    }

    private void hideComment(SuperObservable<OnHideBottomTab> hideTabObservable) {
//        emojiInputLayout.toggleEmojiMenu(false);
        //清除评论
        emojiInputLayout.getEditText().setText("");

        emojiInputLayout.hideInput();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //显示mianActivity的bottomTabLayout
                hideTabObservable.notifyMethod(onHideBottomTab -> {
                    onHideBottomTab.isHide(true);
                });
            }
        }, 80);


    }

    @Override
    public void update2loadData(int loadType, List<CircleListBean.DataBean> list) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void updateForward(int adapterPosition) {
        onPullDown();
    }

    @Override
    public void onLoading(String loadingmessage) {
        showProgressDialog(loadingmessage, true);
    }

    @Override
    public void loadingFinish() {

        dismissProgressDialog();
    }

    private void hideSoftKey() {
        im.hideSoftInputFromWindow(getActivity().getWindow().peekDecorView().getWindowToken(), 0);
    }


    /**
     * 隐藏软键盘和表情框
     */
//    private void dismissInput() {
//        if (emoImg.isSelected()) {
//            emoDialog.dissmiss();
//            edittextbody.setVisibility(View.GONE);
//            emoImg.setSelected(false);
//        } else {
//            CommonUtils_jp.hideSoftInput(getContext(), editText);
//        }
//    }
}
