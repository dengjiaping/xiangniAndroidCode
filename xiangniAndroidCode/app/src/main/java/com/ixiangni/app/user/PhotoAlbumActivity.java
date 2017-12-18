package com.ixiangni.app.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.cloud.media.player.BDCloudMediaPlayer;
import com.baidu.cloud.videoplayer.demo.info.VideoInfo;
import com.clcus.EmojiInpuLayout;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.adapter.AlbumAdapter;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.publish.AdvancedPlayActivity;
import com.ixiangni.bean.CircleListBean;
import com.ixiangni.bean.CommentBean;
import com.ixiangni.bean.LikeBean;
import com.ixiangni.bean.PersonBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.presenters.CirclePresenter;
import com.ixiangni.presenters.CommentConfig;
import com.ixiangni.presenters.DeleteCirclePresenter;
import com.ixiangni.presenters.contract.CircleContract;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.BDMediaPlayerHelper;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.HuanXinHelper;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.utils.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 个人相册
 *
 * @ClassName:PhotoAlbumActivity
 * @PackageName:com.ixiangni.app.user
 * @Create On 2017/6/22 0022   16:47
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class PhotoAlbumActivity extends BaseActivity implements RemoteDataHandler.Callback, SmartPullableLayout.OnPullListener, CircleContract.View, AlbumAdapter.OnHandEnvent  {

    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.emoji_input_layout)
    EmojiInpuLayout emojiInputLayout;
    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.tv_person_name)
    TextView tvPersonName;
    @Bind(R.id.iv_gender)
    ImageView ivGender;
    @Bind(R.id.tv_focus)
    TextView tvFocus;


    private DeleteCirclePresenter deleteCirclePresenter;
    private ProgressDialog mProgressdialog;

    private boolean isSelf;
    private String userid;

    private int currentPage = 1;
    private int pageSize = 20;
    private AlbumAdapter mAdapter;
    private CirclePresenter presenter;
    private CommentConfig commentConfig;
    private String reply;
    private InputMethodManager im;

    private BDMediaPlayerHelper helper;
    private BDCloudMediaPlayer bdCloudMediaPlayer;
    private String lastAudioPath = "";
    private View lastVoiceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userid = intent.getStringExtra(XNConstants.USERID);
        isSelf = intent.getBooleanExtra("isSelf", true);

        im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        initView();
        if (LoginHelper.getInstance().isLogin(this)) {
            getCircledata();
            getSimpleInfo();
        }

    }

    private static final String TAG = "PhotoAlbumActivity";

    private void getSimpleInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        if (CommonUtils.isStringNull(userid)) {
            userid = LoginHelper.getInstance().getUserid(this);
        }
        params.put("goalid", userid);

        Log.i(TAG, "userid: " + userid);
        RemoteDataHandler.asyncPost(UrlString.URL_GET_OTHER_INFO, params, this, true, response -> {

            if (tvPersonName == null) {
                return;
            }
            String json = response.getJson();
            log(json);
            if (smartPullLayout != null) {
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    PersonBean personInfoBean = new Gson().fromJson(json, PersonBean.class);
                    if (1 == personInfoBean.getStatus()) {
                        setPersonInfo(personInfoBean.getData());
                    } else {
                        toast(personInfoBean.getMessage());
                    }
                }

            }
        });
    }

    private void setPersonInfo(PersonBean.DataBean data) {

        String userpic = data.getUserpic();
        int usersex = data.getUsersex();


        String usernick = data.getUsernick();
        int focusnum = data.getFocusnum();
        GlideUtil.loadRoundImage(getApplicationContext(), userpic, ivIcon, R.mipmap.touxiangmoren);
        tvPersonName.setText(usernick);

        String userid = data.getUserid() + "";

        String hxid = HuanXinHelper.getHuanXinidbyUseid(userid);
        EaseUser userInfo = DemoHelper.getInstance().getUserInfo(hxid);

        if (userInfo != null) {
            String nickname = userInfo.getNickname();
            if (CommonUtils.isRemindName(nickname)) {
                tvPersonName.setText(CommonUtils.getParseName(nickname));
            }
        }


        ivGender.setImageResource(usersex == 1 ? R.mipmap.man : R.mipmap.woman);
        String text = "%d人关注";
        tvFocus.setText(String.format(Locale.CHINA, text, focusnum));

    }


    private void initView() {

        presenter = new CirclePresenter(this, this);
        mAdapter = new AlbumAdapter(this);
        mAdapter.setCirclePresenter(presenter);
        mAdapter.setEnvent(this);
        listView.setAdapter(mAdapter);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getCount() == 0) {
                    stateLayout.showNoDataView("暂无动态");
                } else {
                    stateLayout.showContenView();
                }
            }
        });
        smartPullLayout.setOnPullListener(this);

        emojiInputLayout.setListener(view1 -> {
            String content = emojiInputLayout.getText().toString().trim();
            if (TextUtils.isEmpty(content)) {
                toast("内容不能为空!");
            } else {
                presenter.addComment(content, commentConfig);
                emojiInputLayout.toggleEmojiMenu(false);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideEmojiInput();
                return false;
            }
        });

        //删除朋友圈动态
        deleteCirclePresenter = new DeleteCirclePresenter();

        initPlayer();
    }

    private void hideEmojiInput() {

        emojiInputLayout.hideInput();
    }

    private void initPlayer() {
        helper = new BDMediaPlayerHelper();
        bdCloudMediaPlayer = helper.get(this);
        helper.setOnComplete(new BDMediaPlayerHelper.OnComplete() {
            @Override
            public void onComplete() {

                //释放当前播放语音状态
                if (lastVoiceView != null) {
                    lastVoiceView.setSelected(false);
                    //reset
                    mAdapter.setPlayingAudioPosition(-1);
                }
            }
        });
    }


    public static void start(Context context, String userid, boolean isSelf) {
        Intent intent = new Intent(context, PhotoAlbumActivity.class);
        intent.putExtra(XNConstants.USERID, userid);
        intent.putExtra("isSelf", isSelf);
        context.startActivity(intent);
    }


//    token	是	String	token
//    currentPage	是	Int	当前页数
//    pageSize	是	Int	每页显示数
//    goalid	是	Long	要查看的userid

    private boolean isFirst = true;

    private void getCircledata() {

        if (isFirst) {

            stateLayout.showLoadView("加载中...");
        }
        String url = isSelf ? UrlString.URL_MY_PUBLISH : UrlString.URL_OTHER_CIRCLE;
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        if (!isSelf) {
            params.put("goalid", userid);
        }

        params.put(XNConstants.currentPage, "" + currentPage);
        params.put(XNConstants.pageSize, "" + pageSize);
        RemoteDataHandler.asyncPost(url, params, this, true, this);
    }


    @Override
    public void dataLoaded(ResponseData response) {
        if (topBar != null) {
            smartPullLayout.stopPullBehavior();
            isFirst = false;
            String json = response.getJson();
            log(json);
            if (CommonUtils.isStringNull(json)) {
                toast(Constants.CONNECT_SERVER_FAILED);
            } else {

                CircleListBean circleListBean = new Gson().fromJson(json, CircleListBean.class);
                if (1 == circleListBean.getStatus()) {

                    if (currentPage == 1) {
                        mAdapter.replaceAll(circleListBean.getData());
                    } else {
                        mAdapter.addAll(circleListBean.getData());
                    }
                    if (circleListBean.getData().size() < pageSize) {
                        smartPullLayout.setPullUpEnabled(false);
                    }
                } else {
                    toast(circleListBean.getMessage());
                }

            }

        }
    }

    @Override
    public void onPullDown() {
        if (bdCloudMediaPlayer.isPlaying()) {
            bdCloudMediaPlayer.stop();
        }
        onRefresh();
    }

    private void onRefresh() {
        currentPage = 1;
        smartPullLayout.setPullUpEnabled(true);
        getCircledata();
    }

    @Override
    public void onPullUp() {
        currentPage++;
        getCircledata();
    }

    @Override
    public void showLoading(String msg) {
        showProgressDialog(msg, true);
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

    @Override
    public void update2AddFavorite(int adapterPosition, boolean flag) {

        CircleListBean.DataBean itemData = mAdapter.getItemData(adapterPosition);
        List<LikeBean> likeList = itemData.getLikeList();
        if (ListUtil.isEmptyList(likeList)) {
            likeList = new ArrayList<>();
        }
        int userid = Integer.parseInt(LoginHelper.getInstance().getUserid(this));

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
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void update2AddComment(int circlePosition, CommentBean addItem) {

        if (addItem != null) {
            CircleListBean.DataBean item = (CircleListBean.DataBean) mAdapter.getDatas().get(circlePosition);//刷新评论的集合
            List<CommentBean> commentList = item.getCommentList();
            if (commentList == null) {
                commentList = new ArrayList<>();
                item.setCommentList(commentList);
            }
            commentList.add(addItem);
            item.setCommentnum(item.getCommentnum() + 1);
            mAdapter.notifyDataSetChanged();

        }
        emojiInputLayout.getEditText().setText("");
        hideSoftKey();
    }

    @Override
    public void update2DeleteComment(int circlePosition, String commentId) {

        CircleListBean.DataBean item = mAdapter.getDatas().get(circlePosition);//这个是朋友圈的评论的position
        List<CommentBean> commentList = item.getCommentList();
        if (commentList != null) {
            for (int i = 0; i < commentList.size(); i++) {
                CommentBean commentBean = commentList.get(i);
                if (commentId.equals(commentBean.getCommentid())) {
                    commentList.remove(i);
                    item.setCommentnum(item.getCommentnum() - 1);
                    mAdapter.notifyDataSetChanged();
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

        boolean isShowing = emojiInputLayout.getVisibility() == View.VISIBLE;

        if (isShowing) {
            if (sameLastComment) {
                hideSoftKey();
            }
        } else {

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

        }

    }

    private void hideSoftKey() {
        im.hideSoftInputFromWindow(getWindow().peekDecorView().getWindowToken(), 0);
    }

    @Override
    public void update2loadData(int loadType, List<CircleListBean.DataBean> list) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void updateForward(int adapterPosition) {

    }



    @Override
    public void onFailed(String errorMsg) {


    }

    @Override
    public void onLoading(String loadingmessage) {

        showProgressDialog(loadingmessage, true);
    }

    @Override
    public void loadingFinish() {
        dismissProgressDialog();

    }

    private int deletePoi = -1;

    @Override
    public void onDeleteCircle(int position, String momentnewsid) {
        deletePoi = position;
        MyAlertdialog alertdialog = new MyAlertdialog(this);
        alertdialog.setTitle("提示")
                .setMessage("您确定要删除该条动态？")
                .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                    @Override
                    public void onLeftClick(View view) {

                    }

                    @Override
                    public void onRightClick(View view) {
                        mProgressdialog = ProgressDialog.show(PhotoAlbumActivity.this, null, "删除中", true, false);

                        Log.w("zcq", "onRightClick: "+ momentnewsid );
                        deleteCirclePresenter.delete(PhotoAlbumActivity.this, momentnewsid, new OnResult<String>() {
                            @Override
                            public void onSuccess(String s) {
                                if (mProgressdialog != null) {
                                    mProgressdialog.dismiss();
                                    toast("删除成功！");
                                    if (deletePoi >= 0 && deletePoi < mAdapter.getCount()) {
                                        mAdapter.remove(deletePoi);
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
                        });
                    }
                });
        alertdialog.show();

    }

    /**
     * 播放音频
     *
     * @param audioPath
     */
    @Override
    public void onPlayAudio(int position, View voiceView, String audioPath) {


        //同一个音频
        if (lastAudioPath.equals(audioPath)) {
            if (bdCloudMediaPlayer.isPlaying()) {
                bdCloudMediaPlayer.stop();
                voiceView.setSelected(false);
            } else {
                //标记当前语音播放状态
                mAdapter.setPlayingAudioPosition(position);
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
            mAdapter.setPlayingAudioPosition(position);
            voiceView.setSelected(true);

            lastVoiceView = voiceView;
            helper.prepare(audioPath);
            lastAudioPath = audioPath;

        }

    }

    @Override
    public void onPlayVideo(String videoPath) {

        Intent intent = new Intent(this, AdvancedPlayActivity.class);
        VideoInfo info = new VideoInfo("视频", videoPath);
        intent.putExtra("videoInfo", info);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
