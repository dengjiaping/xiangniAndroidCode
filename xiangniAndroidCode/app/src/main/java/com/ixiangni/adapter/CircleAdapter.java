package com.ixiangni.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handongkeji.impactlib.util.ToastUtils;
import com.handongkeji.impactlib.util.ViewHelper;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DateUtil;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.app.R;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.publish.ImageBrowserActivity1;
import com.ixiangni.app.user.PersonPageActivity;
import com.ixiangni.bean.CircleListBean;
import com.ixiangni.bean.CommentBean;
import com.ixiangni.bean.LikeBean;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.presenters.CirclePresenter;
import com.ixiangni.presenters.CommentConfig;
import com.ixiangni.ui.CommentDialog;
import com.ixiangni.ui.CommentListView;
import com.ixiangni.ui.FlowLayout;
import com.ixiangni.ui.MultiImageView;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.HuanXinHelper;
import com.nevermore.oceans.utils.ListUtil;

import java.util.List;

import static com.ixiangni.app.R.id.tv_content;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class CircleAdapter extends MultiItemAdapter<CircleListBean.DataBean> {

    private CirclePresenter presenter;
    private boolean zhankai = false;

    public interface OnHandEnvent {
        //删除朋友圈
        void onDeleteCircle(int position, String momentnewsid);

        //播放音频
        void onPlayAudio(int position, View voiceView, String audioPath);

        //播放视频
        void onPlayVideo(String videoPath);
    }

    private OnHandEnvent envent;

    public OnHandEnvent getEnvent() {
        return envent;
    }

    public void setEnvent(OnHandEnvent envent) {
        this.envent = envent;
    }

    //  插入类别
    // 0：不插入
    // 1：插入图片
    // 2：插入语音
    // 3：插入视频

    public static int Type_text = 0;
    public static int Type_img = 1;

    public CircleAdapter(Context mContext) {
        super(mContext);
    }


    public void setCirclePresenter(CirclePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public int getDataType(CircleListBean.DataBean dataBean) {
        return dataBean.getInserttype();
    }

    private int playingAudioPosition = -1;//正在播放音频的位置

    public void setPlayingAudioPosition(int playingAudioPosition) {
        this.playingAudioPosition = playingAudioPosition;
    }

    private static final String TAG = "CircleAdapter";

    @Override
    protected void bindView(int position, CircleListBean.DataBean dataBean, ViewHelper helper, ViewGroup parent) {

        Log.i(TAG, "position: " + position);
        List<CircleListBean.DataBean.UserinfoBean> userinfo = dataBean.getUserinfo();

        String userid = dataBean.getUserid() + "";

        String currentUserid = LoginHelper.getInstance().getUserid(mContext);

        LinearLayout digCommentBody = helper.getView(R.id.digCommentBody);
        FlowLayout flowLayout = helper.getView(R.id.flow_layout);

        ImageView iv = helper.getView(R.id.iv_user_icon);


        //点击头像跳转个人相册
        //        iv.setOnClickListener(v -> PhotoAlbumActivity.start(mContext,userid,LoginHelper.getInstance().getUserid(mContext).equals(userid)));
        if (!currentUserid.equals(userid)) {

            Log.i(TAG, "userid: " + userid);
            Log.i(TAG, "currentUserid: " + currentUserid);
            iv.setOnClickListener(v -> PersonPageActivity.start(mContext, userid));
        } else {
            iv.setOnClickListener(null);
        }


        if (!ListUtil.isEmptyList(userinfo)) {
            CircleListBean.DataBean.UserinfoBean userinfoBean = userinfo.get(0);

            //用户名
            String usernick = userinfoBean.getUsernick();
            helper.setText(R.id.tv_user_name, usernick);

            String hxid = HuanXinHelper.getHuanXinidbyUseid(userid);
            EaseUser userInfo = DemoHelper.getInstance().getUserInfo(hxid);
            if (userInfo != null) {
                String nickname = userInfo.getNickname();
                if (CommonUtils.isRemindName(nickname)) {
                    helper.setText(R.id.tv_user_name, CommonUtils.getParseName(nickname));
                }
            }


            //头像
            GlideUtil.loadRoundImage(parent.getContext(), userinfoBean.getUserpic(), iv, R.mipmap.touxiangmoren);
        }

        int itemViewType = getItemViewType(position);

        //内容 文字 图片 类型才有
        if (itemViewType == CIRCLE_IMAGE || itemViewType == CIRCLE_TEXT) {
            View view = helper.getView(tv_content);

            String momentnewscontent = dataBean.getMomentnewscontent();
            if (!CommonUtils.isStringNull(momentnewscontent)) {
                helper.setText(tv_content, momentnewscontent);
                view.setVisibility(View.VISIBLE);
                //
                //                view.setOnLongClickListener(new View.OnLongClickListener() {
                //                    @Override
                //                    public boolean onLongClick(View v) {
                //
                //                        ClipboardManager cm =(ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                //                        cm.setText(view.getText().toString());
                //                        //ToastUtil.toastSth(getContext() , "订单号已复制到剪切板，快去粘贴吧~");
                //                        return false;
                //                    }
                //                });

            } else {
                view.setVisibility(View.GONE);
            }
        }


        //播放音频
        if (itemViewType == CIRCLE_AUDIO) {
            //时长

            View voice = helper.getView(R.id.ll_voice);
            voice.setSelected(position == playingAudioPosition);

            helper.setText(R.id.tv_audio_duration, dataBean.getMediatime());

            helper.setOnClickListener(R.id.ll_voice, v -> {
                if (dataBean.getDel() == 1) {
                    ToastUtils.show(mContext, "音频转码中,请稍后刷新重试...");
                    return;
                }
                if (envent != null) {
                    envent.onPlayAudio(position, voice, dataBean.getInserturl());
                }

            });
        }

        //视频
        if (itemViewType == CIRCLE_VIDEO) {
            String videopic = dataBean.getVideopic();
            ImageView ivThubb = helper.getView(R.id.iv_thumb_video);
            Glide.with(parent.getContext())
                    .load(videopic)
                    .placeholder(R.mipmap.loading_rect)
                    .into(ivThubb);
            ivThubb.setOnClickListener(v -> {
                if (dataBean.getDel() == 1) {
                    ToastUtils.show(mContext, "视频转码中,请稍后刷新重试...");
                    return;
                }

                if (envent != null) {
                    envent.onPlayVideo(dataBean.getInserturl());
                }
            });
        }


        int momentnewsid1 = dataBean.getMomentnewsid();

        //dianzan数
        int likenum = dataBean.getLikenum();
        helper.setText(R.id.tv_like_count, likenum + "");

        int islike = dataBean.getIslike();


        TextView tvLike = helper.getView(R.id.tv_like_count);

        if (islike == 1) {
            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.dianan_red);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);
        } else {

            Drawable drawable = ContextCompat.getDrawable(mContext, R.mipmap.like);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            tvLike.setCompoundDrawables(drawable, null, null, null);


        }

        tvLike.setOnClickListener(v -> {
            presenter.addFavort(position, momentnewsid1, dataBean.getIslike() == 0);
        });


        List<LikeBean> likeList = dataBean.getLikeList();
        LikeAdapter likeAdapter = new LikeAdapter(mContext, likeList);
        setLikeList(flowLayout, likeAdapter);

        //评论数
        int commentnum = dataBean.getCommentnum();
        helper.setText(R.id.tv_comment_count, commentnum + "");

        TextView tvComment = helper.getView(R.id.tv_comment_count);
        tvComment.setOnClickListener(v -> {
            if (presenter != null) {
                CommentConfig config = new CommentConfig();
                config.circlePosition = position;
                config.commentType = CommentConfig.Type.PUBLIC;
                config.userid = dataBean.getUserid() + "";//该条朋友圈发布者的id
                config.momentnewsid = dataBean.getMomentnewsid() + "";//极有圈id
                presenter.showEditTextBody(config);
            }
        });
        if (commentnum == 0 && likenum == 0) {
            digCommentBody.setVisibility(View.GONE);
        } else {
            digCommentBody.setVisibility(View.VISIBLE);
        }
        View layoutComment = helper.getView(R.id.ll_comment_layout);

        if (commentnum == 0) {
            layoutComment.setVisibility(View.GONE);
        } else {
            layoutComment.setVisibility(View.VISIBLE);
        }


        //转发数量
        helper.setText(R.id.tv_share_count, dataBean.getForwardnum() + "");

        if (!currentUserid.equals(userid)) {

            helper.setOnClickListener(R.id.tv_share_count, v -> {

                MyAlertdialog alertdialog = new MyAlertdialog(mContext);

                alertdialog.setMessage("确定将这条内容分享到我的想你圈吗");


                alertdialog.setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                    @Override
                    public void onLeftClick(View view) {

                    }

                    @Override
                    public void onRightClick(View view) {
                        presenter.forwardCircle(position, momentnewsid1);

                    }
                });

                alertdialog.show();

            });

        } else {
            helper.setOnClickListener(R.id.tv_share_count, v -> {
                ToastUtils.show(mContext, "不可以转发自己的朋友圈");
            });

        }

        //删除该条朋友圈动态

        View viewDelete = helper.getView(R.id.tv_delete);
        //非自己的动态不让删
        if (!LoginHelper.getInstance().getUserid(parent.getContext()).equals(userid)) {
            viewDelete.setVisibility(View.GONE);
        } else {
            viewDelete.setVisibility(View.VISIBLE);
            String momentnewsid = dataBean.getMomentnewsid() + "";
            helper.setOnClickListener(R.id.tv_delete, v -> {
                if (envent != null) {
                    envent.onDeleteCircle(position, momentnewsid);
                }
            });
        }


        //发布时间
        long releasetime = dataBean.getReleasetime();
        String time = DateUtil.formatDateTime(releasetime);
        helper.setText(R.id.tv_time, time);

        //插入图片
        if (getItemViewType(position) == 1) {
            //            CellLayout cellLayout = helper.getView(R.id.cell_picture);
            //            List<CircleListBean.DataBean.PicListBean> picList = dataBean.getPicList();
            //            CellPictureAdapter adapter = new CellPictureAdapter(parent.getContext());
            //            adapter.addAll(picList);
            //            cellLayout.setAdapter(adapter);
            List<CircleListBean.DataBean.PicListBean> picList = dataBean.getPicList();
            List<String> filterList = ListUtil.getFilterList(picList, picListBean -> picListBean.getInserturl());

            MultiImageView multiImageView = helper.getView(R.id.multi_image);

            if (filterList != null && filterList.size() > 0) {
                multiImageView.setVisibility(View.VISIBLE);
                multiImageView.setList(filterList);
                multiImageView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ImageBrowserActivity1.position = position;
                        ImageBrowserActivity1.mList = picList;
                        mContext.startActivity(new Intent(mContext, ImageBrowserActivity1.class));
                    }
                });
            } else {
                multiImageView.setVisibility(View.GONE);
            }
        }

        //地址
        String momentnewsaddress = dataBean.getMomentnewsaddress();
        if (!CommonUtils.isStringNull(momentnewsaddress)) {
            helper.setText(R.id.tv_address, momentnewsaddress);
        } else {
            helper.setText(R.id.tv_address, "");
        }

        CircleListBean.DataBean circleItem = dataBean;
        //评论
        List<CommentBean> commentList = dataBean.getCommentList();
        CommentListView commentListView = helper.getView(R.id.commentList);
        //评论
        if (commentList != null && commentList.size() > 0) {
            commentListView.setOnItemClickListener(commentPosition -> {
                CommentBean commentItem = commentList.get(commentPosition);
                if (LoginHelper.getInstance().getUserid(mContext).equals(commentItem.getUserid())) {//
                    CommentDialog dialog = new CommentDialog(mContext, presenter, commentItem, circleItem, position);
                    dialog.show();
                } else {//回复别人的评论
                    if (presenter != null) {
                        CommentConfig config = new CommentConfig();
                        config.circlePosition = position;
                        config.commentPosition = commentPosition;
                        config.commentType = CommentConfig.Type.REPLY;
                        config.userid = circleItem.getUserid() + "";//该条朋友圈发布者的id
                        config.momentnewsid = circleItem.getMomentnewsid() + "";//极有圈id
                        config.pluserid = circleItem.getCommentList().get(commentPosition).getUserid();//评论者的userid
                        config.replyUser = ListUtil.isEmptyList(userinfo) ? null : userinfo.get(0);//自己的信息
                        config.plusernick = circleItem.getCommentList().get(commentPosition).getUsernick();//评论者昵称
                        presenter.showEditTextBody(config);
                    }
                }
            });
            commentListView.setOnItemLongClickListener(commentPosition -> {
                //长按进行复制或者删除
                CommentBean entity = circleItem.getCommentList().get(commentPosition);
                CommentDialog dialog = new CommentDialog(mContext, presenter, entity, circleItem, position);
                dialog.show();
            });
            commentListView.setDatas(circleItem.getCommentList());//评论的数据
            commentListView.setVisibility(View.VISIBLE);
        } else {
            commentListView.setVisibility(View.GONE);
        }

        //发送评论的监听

    }

    public void setLikeList(FlowLayout flowLayout, LikeAdapter adapter) {
        flowLayout.removeAllViews();
        View heartView = LayoutInflater.from(mContext).inflate(R.layout.like_heart, null);
        flowLayout.addView(heartView);
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                flowLayout.addView(view);
            }
            int dianzanNum = adapter.getCount();

            //点击进行点赞展开缩放
            //            flowLayout.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View view) {
            //                    if (!zhankai) {
            //                        flowLayout.removeAllViews();
            //                        View heartView = LayoutInflater.from(mContext).inflate(R.layout.like_heart, null);
            //                        flowLayout.addView(heartView);
            //                        for (int i = 0; i < adapter.getCount(); i++) {
            //                            View view1 = adapter.getView(i, null, null);
            //                            flowLayout.addView(view1);
            //                        }
            //                        zhankai = true;
            //                    } else {
            //                        flowLayout.removeAllViews();
            //                        View heartView = LayoutInflater.from(mContext).inflate(R.layout.like_heart, null);
            //                        flowLayout.addView(heartView);
            //                        for (int i = 0; i < 20; i++) {
            //                            View view1 = adapter.getView(i, null, null);
            //                            flowLayout.addView(view1);
            //                        }
            //                        zhankai = false;
            //                    }
            //
            //                }
            //            });

            //点赞列表缩放
            //            if (dianzanNum >= 20) {
            //                for (int i = 0; i < 20; i++) {
            //                    View view = adapter.getView(i, null, null);
            //                    flowLayout.addView(view);
            //                }
            //
            //            } else {
            //                for (int i = 0; i < adapter.getCount(); i++) {
            //                    View view = adapter.getView(i, null, null);
            //                    flowLayout.addView(view);
            //                }
            //            }
        }
    }

    public CircleListBean.DataBean getItemData(int position) {

        CircleListBean.DataBean dataBean = mList.get(position);
        return dataBean;
    }

    @Override
    protected void initItemLayout() {
        addTypeLayoutRes(CIRCLE_TEXT, R.layout.item_circle);

        //插入图片
        addTypeLayoutRes(CIRCLE_IMAGE, R.layout.item_circle_img);
        //插入语音
        addTypeLayoutRes(CIRCLE_AUDIO, R.layout.item_circle_audio);
        //插入视频
        addTypeLayoutRes(CIRCLE_VIDEO, R.layout.item_circle_vide);


    }

    public static final int CIRCLE_TEXT = 0;
    public static final int CIRCLE_IMAGE = 1;
    public static final int CIRCLE_AUDIO = 2;
    public static final int CIRCLE_VIDEO = 3;
}
