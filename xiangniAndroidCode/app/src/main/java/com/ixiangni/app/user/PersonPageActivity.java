package com.ixiangni.app.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.ui.ChatActivity;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.chat.DataSettingActivity;
import com.ixiangni.app.contactlist.BlackListActivity;
import com.ixiangni.bean.PersonBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.interfaces.OnDeleteContact;
import com.ixiangni.presenters.AddFriendPresenter;
import com.ixiangni.presenters.FriendCirclePresenter;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.NotifyHelper;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 个人主页
 *
 * @ClassName:PersonPageActivity
 * @PackageName:com.ixiangni.app.user
 * @Create On 2017/6/20 0020   17:07
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class PersonPageActivity extends BaseActivity implements OnResult<String>, OnDeleteContact {

    @Bind(R.id.iv_person_bg)
    ImageView ivPersonBg;
    @Bind(R.id.iv_person_icon)
    ImageView ivPersonIcon;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.tv_place)
    TextView tvPlace;

    @Bind(R.id.tv_job)
    TextView tvJob;

    @Bind(R.id.tv_focus)
    TextView tvFocus;
    @Bind(R.id.btn_follow)
    Button btnFollow;
    @Bind(R.id.tv_phone_num)
    TextView tvPhoneNum;
    @Bind(R.id.tv_area)
    TextView tvArea;
    @Bind(R.id.tv_sign_title)
    TextView tvSignTitle;
    @Bind(R.id.tv_sign)
    TextView tvSign;
    @Bind(R.id.iv_thumb_1)
    ImageView ivThumb1;
    @Bind(R.id.iv_thumb_2)
    ImageView ivThumb2;
    @Bind(R.id.iv_thumb_3)
    ImageView ivThumb3;
    @Bind(R.id.btn_add_to_contact_list)
    Button btnAddToContactList;
    @Bind(R.id.iv_finish)
    ImageView ivFinish;
    @Bind(R.id.iv_more)
    ImageView ivMore;

    public static final String CONSTANTS_USERID = "constants_userid";
    public static final String CONSTANTS_USERNICK = "constants_usernick";
    public static final String CONSTANTS_USERPIC = "constants_userpic";
    @Bind(R.id.rl_phone)
    RelativeLayout rlPhone;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.iv_gender)
    ImageView ivGender;
    @Bind(R.id.rl_ablum)
    RelativeLayout rlAblum;
    private String userid;
    private AddFriendPresenter mAddFriendPresenter;
    private PersonBean.DataBean personData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_page);
        ButterKnife.bind(this);

        SuperObservableManager
                .getInstance()
                .getObservable(OnDeleteContact.class)
                .registerObserver(this);

        mAddFriendPresenter = new AddFriendPresenter();

        Intent intent = getIntent();
        userid = intent.getStringExtra(CONSTANTS_USERID);
//        if (!TextUtils.isEmpty(userid)) {
//            String userNick = intent.getStringExtra(CONSTANTS_USERNICK);
//            String userPic = intent.getStringExtra(CONSTANTS_USERPIC);
//            initUserInfo(userNick, userPic);
//        }

        getSimpleUserInfo();

    }

    public static void start(Context context, String userid) {
        Intent intent = new Intent(context, PersonPageActivity.class);
        intent.putExtra(CONSTANTS_USERID, userid);
        context.startActivity(intent);
    }


    //获取该用户简介的信息

    private static final String TAG = "PersonPageActivity";

    private void getSimpleUserInfo() {

        stateLayout.showLoadView();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("goalid", userid);

        Log.i(TAG, "userid: " + userid);
        RemoteDataHandler.asyncPost(UrlString.URL_GET_OTHER_INFO, params, this, true, response -> {

            String json = response.getJson();
            log(json);
            if (tvNickName != null) {
                stateLayout.showContenView();
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

    /**
     * 设置用户信息
     *
     * @param data
     */
    private void setPersonInfo(PersonBean.DataBean data) {

        this.personData = data;


        //userid
        int userid = data.getUserid();


        rlAblum.setOnClickListener(v -> {
            PhotoAlbumActivity.start(this, "" + userid, false);
        });

        //昵称
        String usernick = data.getUsernick();
        String remindname = data.getRemindname();
        if (!CommonUtils.isStringNull(remindname)) {

            tvNickName.setText(remindname);
        } else {
            tvNickName.setText(usernick);

        }


        //性别
        int usersex = data.getUsersex();
        ivGender.setImageResource(usersex == 1 ? R.mipmap.man : R.mipmap.woman);

        //头像
        String userpic = data.getUserpic();
        GlideUtil.loadRoundImage(this, userpic, ivPersonIcon, R.mipmap.touxiangmoren);


        ivPersonIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PersonPageActivity.this,BigTouxiangActivity.class);
                intent.putExtra("touxiang",userpic);
                startActivity(intent);
            }
        });



        //缓存用户昵称和头像
        EaseUser user = new EaseUser("ixn" + userid);
        user.setAvatar(userpic);
        user.setNickname(usernick);
        DemoHelper.getInstance().saveContact(user);


        //职位互换了下
        String jobname = (String) data.getJobname();
        String branchname = data.getBranchname();
        if (CommonUtils.isStringNull(jobname)) {
            tvPlace.setText("未填写");
        } else {
//            tvPlace.setText(branchname + " " + jobname);
            tvPlace.setText(jobname);
        }
        if (CommonUtils.isStringNull(branchname)) {
            tvJob.setText("未填写");
        } else {
            tvJob.setText(branchname);
        }


        //地区
        String areainfo = (String) data.getAreainfo();
        tvArea.setText(CommonUtils.isStringNull(areainfo) ? getString(R.string.Not_Set) : areainfo);

        //个性签名
        String charactersing = (String) data.getCharactersing();
        tvSign.setText(CommonUtils.isStringNull(charactersing) ? getString(R.string.Not_Set) : charactersing);

        int focusnum = data.getFocusnum();

        String focusString = "%d人关注";
        tvFocus.setText(String.format(Locale.CANADA, focusString, focusnum));

        //关注状态
        boolean isfollow = data.getIsfollow() == 1;
        btnFollow.setText(isfollow ? "已关注" : "关注");
        btnFollow.setOnClickListener(v -> {
                    showProgressDialog(personData.getIsfollow() == 1 ? "取消中..." : "关注中...", true);
                    FriendCirclePresenter.followOrNot(this, "" + userid, personData.getIsfollow() == 0, new OnResult<String>() {
                        @Override
                        public void onSuccess(String s) {
                            if (btnFollow != null) {
                                dismissProgressDialog();

                                if (personData.getIsfollow() == 1) {
                                    toast("取消成功");
                                    btnFollow.setText("关注");
                                    personData.setIsfollow(0);
                                    personData.setFocusnum(personData.getFocusnum() - 1);
                                    tvFocus.setText(String.format(Locale.CHINA, focusString, personData.getFocusnum()));

                                } else {
                                    toast("关注成功");
                                    btnFollow.setText("已关注");
                                    personData.setIsfollow(1);
                                    personData.setFocusnum(personData.getFocusnum() + 1);
                                    tvFocus.setText(String.format(Locale.CHINA, focusString, personData.getFocusnum()));
                                }
                                setButtonUI();

                            }
                        }

                        @Override
                        public void onFailed(String errorMsg) {
                            if (btnFollow != null) {
                                dismissProgressDialog();
                                toast(errorMsg);
                            }
                        }
                    });
                }
        );


        //好友状态 0陌生人1好友2黑名单

        int isfriend = data.getIsfriend();

        /*****************************右上角三个点的点击事件******************************/
        if (isfriend != 0) {
            ivMore.setVisibility(View.VISIBLE);
            ivMore.setOnClickListener(v -> {

                Intent intent = new Intent(PersonPageActivity.this, DataSettingActivity.class);
                intent.putExtra(XNConstants.USERID, "" + userid);
                intent.putExtra(XNConstants.ISFRIEND, isfriend);
                intent.putExtra(XNConstants.REMIND_NAME, data.getRemindname());
                intent.putExtra(XNConstants.seehemoment, data.getSeehemoment());
                intent.putExtra(XNConstants.seememoment, data.getSeememoment());

                startActivity(intent);
            });
        } else {
            ivMore.setVisibility(View.GONE);
        }

        //个人相册图片展示
        List<PersonBean.DataBean.NewsBean> newsVoList = data.getNewsVoList();
        if (!ListUtil.isEmptyList(newsVoList)) {
            Observable.from(newsVoList)
                    .flatMap(new Func1<PersonBean.DataBean.NewsBean, Observable<PersonBean.DataBean.NewsBean.PicListBean>>() {

                        @Override
                        public Observable<PersonBean.DataBean.NewsBean.PicListBean> call(PersonBean.DataBean.NewsBean newsBean) {
                            return Observable.from(newsBean.getPicList());
                        }
                    })
                    .toList()
                    .subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(picListBeen -> {
                        if (!ListUtil.isEmptyList(picListBeen)) {
                            String inserturl = picListBeen.get(0).getInserturl();
                            loadImage(inserturl, ivThumb1);
                            if (picListBeen.size() > 1) {
                                loadImage(picListBeen.get(1).getInserturl(), ivThumb2);
                            }
                            if (picListBeen.size() > 2) {
                                loadImage(picListBeen.get(2).getInserturl(), ivThumb3);
                            }
                        }
                    });

        }

        setButtonUI();


    }

    //好友状态 0陌生人1好友2黑名单
    private void setButtonUI() {
        int isfollow = personData.getIsfollow();
        int isfriend = personData.getIsfriend();
        String usermobile = personData.getUsermobile();

        if (isfollow == 1) {//关注
            if (isfriend != 2) {//并且不在黑名单开始聊天

                btnAddToContactList.setText("开始聊天");
                btnAddToContactList.setOnClickListener(v -> {
                    ChatActivity.startChat(PersonPageActivity.this, XNConstants.ixn + userid, EaseConstant.CHATTYPE_SINGLE);
                });
            }
            tvPhoneNum.setText(CommonUtils.isStringNull(usermobile) ? getString(R.string.Not_Set) : usermobile);
        }

        if (isfriend == 1) {
            btnAddToContactList.setText("开始聊天");
            btnAddToContactList.setOnClickListener(v ->
                    ChatActivity.startChat(PersonPageActivity.this, XNConstants.ixn + userid, EaseConstant.CHATTYPE_SINGLE));
            tvPhoneNum.setText(CommonUtils.isStringNull(usermobile) ? getString(R.string.Not_Set) : usermobile);
        } else if (isfriend == 0 && isfollow == 0) {

            tvPhoneNum.setText(getString(R.string.Not_Set));
            btnAddToContactList.setText("添加到通讯录");
            btnAddToContactList.setOnClickListener(v -> showApplyDialog());

        } else if (isfriend == 2) {//加入黑名单

            tvPhoneNum.setText(getString(R.string.Not_Set));
            btnAddToContactList.setText("已加入黑名单");
            btnAddToContactList.setOnClickListener(v -> startActivity(BlackListActivity.class));
        }


    }

    private void loadImage(String url, ImageView iv) {
        Glide.with(this).load(url).placeholder(R.mipmap.loading).into(iv);
    }

    /**
     * 显示申请好友dialog
     */
    private void showApplyDialog() {
        XDialog dialog = new XDialog(this, R.layout.dialog_add_friend);
        EditText edt = dialog.getView(R.id.edt);
        dialog.setOnClickListener(R.id.btn_send, v -> {
            String message = edt.getText().toString().trim();
            sentFriendApply(message);
            dialog.dismiss();
        });
        dialog.setOnClickListener(R.id.iv_close, v -> dialog.dismiss());
        dialog.show();
        edt.requestFocus();
        showKeyBoard(edt);
    }

    /**
     * 发送好友申请
     */
    private void sentFriendApply(String message) {
        showProgressDialog("发送申请中...", false);
        mAddFriendPresenter.addFriend(PersonPageActivity.this, userid, 0, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                if (btnAddToContactList != null) {
                    try {
                        EMClient.getInstance().contactManager().addContact(XNConstants.ixn + userid, message);
//                        EMClient.getInstance().contactManager().
                        mAddFriendPresenter.reply(PersonPageActivity.this, userid, userid, message, new OnResult<String>() {
                            @Override
                            public void onSuccess(String s) {
                                dismissProgressDialog();
                                NotifyHelper.notifyContactList();
                                toast("发送成功");

                            }

                            @Override
                            public void onFailed(String errorMsg) {

                                dismissProgressDialog();
                                toast(errorMsg);
                            }
                        });

                    } catch (HyphenateException e) {
                        dismissProgressDialog();
                        toast(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if (btnAddToContactList != null) {
                    dismissProgressDialog();
                    toast(errorMsg);
                }
            }
        });
    }


    public static void start(Context context, String userid, String usernick, String userpic) {
        Intent intent = new Intent(context, PersonPageActivity.class);
        intent.putExtra(CONSTANTS_USERID, userid);
        intent.putExtra(CONSTANTS_USERNICK, usernick);
        intent.putExtra(CONSTANTS_USERPIC, userpic);
        context.startActivity(intent);
    }

    private boolean isFriend = true;

    @OnClick({R.id.iv_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish://关闭
                finish();
                break;

        }
    }

    @Override
    public void onSuccess(String s) {
        if (tvNickName != null) {
            dismissProgressDialog();
            toast("发送成功！");
            finish();
        }

    }

    @Override
    public void onFailed(String errorMsg) {
        if (tvNickName != null) {
            dismissProgressDialog();
            toast(errorMsg);
        }
    }

    @Override
    public void delete() {
        getSimpleUserInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        SuperObservableManager
                .getInstance()
                .getObservable(OnDeleteContact.class)
                .unregisterObserver(this);
    }
}
