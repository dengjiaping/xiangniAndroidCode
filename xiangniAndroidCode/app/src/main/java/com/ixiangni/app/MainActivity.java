package com.ixiangni.app;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.clcus.EmojiGetter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handongkeji.autoupdata.CheckVersion;
import com.handongkeji.common.Constants;
import com.handongkeji.common.QFragmentPagerAdapter;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.widget.NoScrollViewPager;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.Constant;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.db.InviteMessgeDao;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.contactlist.ContactListFragment;
import com.ixiangni.app.homepage.MessageListFragment;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.mine.JiNengRenZhengActivity;
import com.ixiangni.app.mine.MineFragment;
import com.ixiangni.app.missyoucircle.CircleFragment;
import com.ixiangni.app.user.ModifyPasswordActivity;
import com.ixiangni.baidumap.service.LocationService;
import com.ixiangni.bean.CircleRemindBean;
import com.ixiangni.bean.GroupInfoBean;
import com.ixiangni.bean.JiNengBean;
import com.ixiangni.bean.UserInfoBean;
import com.ixiangni.common.EmotionManager;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.interfaces.OnHideBottomTab;
import com.ixiangni.presenters.UserInfoPresenter;
import com.ixiangni.util.Constant1;
import com.ixiangni.util.HuanXinHelper;
import com.ixiangni.util.NotifyHelper;
import com.mydemo.yuanxin.util.HttpUtil;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.widget.BottomTabLayout;
import com.nevermore.oceans.widget.BottomTabView;

import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements OnHideBottomTab, BDLocationListener {

    @Bind(R.id.bottom_tab)
    BottomTabLayout bottomTab;
    @Bind(R.id.viewpager)
    NoScrollViewPager viewpager;
    @Bind(R.id.blank)//底边栏的父控件
            TextView blank;
    @Bind(R.id.btvHome)
    BottomTabView btvHome;
    @Bind(R.id.btv_contact)
    BottomTabView btvContact;
    @Bind(R.id.btv_circle)
    BottomTabView btvCircle;

    private long lastbacktime = 0;
    private StateReceiver mreceiver;
    private InviteMessgeDao messgeDao;
    private MessageListFragment messageListFragment;
    private CircleFragment circleFragment;
    private boolean shaohoulight = false;
    private LocationService locationService;
    private boolean isFirst = true;
    private SharedPreferences sharedPreferences;
    private String cityNameBefore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(LoginHelper.SYS_INFO, MODE_PRIVATE);
        cityNameBefore = sharedPreferences.getString(XNConstants.CITY_NAME, null);
        EmojiGetter.getInstance().init(this);
        initYoumeng();
        initView();
        initLocation();
        checkPermission();

        EventBus.getDefault().register(this);

        if (LoginHelper.getInstance().isLogin(this)) {
            EmotionManager.getInstance().loadEmotions();
        }
        SuperObservableManager.getInstance().getObservable(OnHideBottomTab.class)
                .registerObserver(this);

        mreceiver = new StateReceiver();
        IntentFilter filter = new IntentFilter(RemoteDataHandler.STATE_REMOTE_LOGIN);
        filter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        filter.addAction(XNConstants.RECEIVE_NEW_MESSAGE);
        filter.addAction(XNConstants.NewInviteMessage);
        filter.addAction(XNConstants.start_circle_remind);
        filter.addAction(XNConstants.stop_circle_remind);
        filter.addAction(XNConstants.refresh_complete);
        filter.addAction(EaseConstant.get_group_info);

        registerReceiver(mreceiver, filter);


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Constant1.scale = displayMetrics.density;
        Constant1.displayHeight = displayMetrics.heightPixels;
        Constant1.displayWidth = displayMetrics.widthPixels;
        CheckVersion.update(this);
        if (!LoginHelper.getInstance().isLogin(this)) {
            startActivity(LoginActivity.class);
        }


        messgeDao = new InviteMessgeDao(this);

        updateInviteMessage();
        //        //获取手机号和原密码进行比较，是否为原始密码
        //        getPhoneNum();
        //        if(!shaohoulight){
        //            huoquidkanheshui();
        //        }

    }

    private void initYoumeng() {


    }

    private void updateInviteMessage() {
        int unreadMessagesCount = messgeDao.getUnreadMessagesCount();
        btvContact.setCount(unreadMessagesCount);
    }


    @Override
    public void isHide(boolean flag) {
        bottomTab.setVisibility(flag ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onReceiveLocation(final BDLocation bdLocation) {

        String latitude = "" + bdLocation.getLatitude();
        String longitude = "" + bdLocation.getLongitude();

        //给服务器上传自己的位置
        FormBody requestBody = new FormBody.Builder()
                .add("token", MyApp.getInstance().getUserTicket())
                .add("lng", latitude)
                .add("lat", longitude)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlString.URL_USER_LOCATION, requestBody, new Callback() {

            //            status	String	1:操作成功 0:操作失败 -1参数不能为空
            // -2:后台异常 -3:提交失败 -4参数错误
            //            message	String	提示信息

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
//                Log.w("zcq", "onResponse:定位给服务器传成功没 "+body );
            }
        });


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bdLocation != null) {
                    dismissProgressDialog();
                    Address address = bdLocation.getAddress();
                    log(bdLocation.getCity());
                    log(address.city);
                    log(address.address);
                    log(address.province);
                    log(address.country);
                    if (!TextUtils.isEmpty(address.city)) {
                        locationService.stop();
                        String cityName = address.city.replace("市", "");
                        //判断之前定位城市和现在定位城市是否一致,不一致在toast提醒用户定位到哪里了
                        if (!cityName.equals(cityNameBefore)) {
                            toast("当前城市" + cityName);
                            sharedPreferences.edit().putString(XNConstants.CITY_NAME, cityName).commit();
                        }
                    } else {
                        locationService.stop();
                        toast("定位失败，请检查是否打开GPS及相关权限");
                    }
                } else {
                    locationService.stop();
                    dismissProgressDialog();
                    toast("定位失败，请检查是否打开GPS及相关权限");
                }
            }
        });
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    toast("定位失败，请打开定位权限！");
                    return;
                }
            }
            startLocation();
        }

    }

    private void startLocation() {
//        showProgressDialog("定位中...", true);
        locationService.start();
    }


    private class StateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case EaseConstant.get_group_info:
                    String groupID = intent.getStringExtra(EaseConstant.EXTRA_USER_ID);
                    getGroupInfo(groupID);

                    break;
                case XNConstants.refresh_complete://隐藏想你圈提示红点
                    btvCircle.setPointVisiable(false);
                    break;
                case XNConstants.start_circle_remind://开始想你圈更新提醒
                    if (remind_status == status_started) {
                        return;
                    }
                    remind_status = status_started;
                    startGetRemind();

                    log("开始提醒");
                    break;
                case XNConstants.stop_circle_remind://停止想你圈更新提醒
                    stopGetRemind();
                    log("停止提醒");

                    break;
                case XNConstants.NewInviteMessage:
                    updateInviteMessage();

                    break;
                case XNConstants.RECEIVE_NEW_MESSAGE:
                    messageListFragment.refresh();
                    upDateMessageCount();

                    break;
                case RemoteDataHandler.STATE_REMOTE_LOGIN:

                    remoteLogin();

                    break;
                case Constant.ACTION_CONTACT_CHANAGED:

                    NotifyHelper.notifyContactList();


                    String huanxinid = intent.getStringExtra(XNConstants.huanxinid);
                    EaseUser userInfo = DemoHelper.getInstance().getUserInfo(huanxinid);
                    String nickname = userInfo.getNickname();
                    nickname = CommonUtils.getParseName(nickname);
                    int intExtra = intent.getIntExtra(XNConstants.hxmsg_type, 0);


                    log("ACTION_CONTACT_CHANAGED" + intExtra);
                    switch (intExtra) {
                        case XNConstants.hxmsg_onContactAdded:
                            //                            toast(nickname + "已添加到你的联系人列表...");
                            NotifyHelper.notifyContactList();
                            break;
                        case XNConstants.hxmsg_onContactDeleted:
                            //                            toast(nickname + "已从你的联系人列表删除...");
                            NotifyHelper.notifyContactList();
                            break;
                        case XNConstants.hxmsg_onContactInvited:
                            //                            toast(nickname + "请求申请你为好友...");
                            NotifyHelper.notifyContactList();
                            break;

                        case XNConstants.hxmsg_onFriendRequestAccepted:
                            //                            toast(nickname + "同意了您的好友申请...");
                            NotifyHelper.notifyContactList();
                            break;
                        case XNConstants.hxmsg_onFriendRequestDeclined:
                            break;
                        default:
                            NotifyHelper.notifyContactList();
                            break;
                    }

                    break;
            }

        }
    }

    //查询群昵称和头像并保存
    private void getGroupInfo(String groupchatno) {
        HashMap<String, String> params = new HashMap<>();
        params.put("groupchatno", groupchatno);
        RemoteDataHandler.asyncPost(UrlString.URL_GET_GROUP_INFO, params, this, false, response -> {

            String json = response.getJson();
            if (!CommonUtils.isStringNull(json)) {
                GroupInfoBean groupInfoBean = new Gson().fromJson(json, GroupInfoBean.class);
                if (1 == groupInfoBean.getStatus()) {
                    GroupInfoBean.DataBean data = groupInfoBean.getData();

                    String groupchatno1 = data.getGroupchatno();
                    String groupchatname = data.getGroupchatname();
                    String groupchatpic = data.getGroupchatpic();
                    EaseUser group = new EaseUser(groupchatno1);
                    group.setAvatar(groupchatpic);
                    group.setNickname(groupchatname);
                    DemoHelper.getInstance().saveContact(group);
                    if (MessageListFragment.instance != null) {
                        MessageListFragment.instance.refresh();
                    }
                }
            }
        });

    }


    private int getUnReadMessageCount() {
        return EMClient.getInstance().chatManager().getUnreadMessageCount();
    }

    private void remoteLogin() {
        if (LoginHelper.getInstance().isLogin(this)) {
            LoginHelper.getInstance().logout(this);
            changeTab(3);
            MyAlertdialog alertdialog = new MyAlertdialog(MainActivity.this);
            alertdialog.setTitle("是否重新登录？")
                    .setMessage("您的账号在异地登录，\n如非本人操作，密码可能已泄露")
                    .setLeftText("否")
                    .setRightText("是")
                    .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                        @Override
                        public void onLeftClick(View view) {
                            startActivity(LoginActivity.class);
                        }

                        @Override
                        public void onRightClick(View view) {
                            startActivity(LoginActivity.class);
                        }
                    });
            alertdialog.setCancelable(false);
            alertdialog.show();
            startActivity(MainActivity.class);
        }

    }

    @Subscriber(tag = "MainSetVisible")
    public void MainSetVisible(String visible) {
        //如果是visible
        if ("Gone".equals(visible)) {
            bottomTab.setVisibility(View.GONE);
            blank.setVisibility(View.GONE);
        } else if ("Visible".equals(visible)) {
            bottomTab.setVisibility(View.VISIBLE);
            blank.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {

        upDateMessageCount();
        //设置viewpager
        List<Fragment> fragmentList = new ArrayList<>();

        //会话列表
        messageListFragment = new MessageListFragment();
        fragmentList.add(messageListFragment);


        fragmentList.add(new ContactListFragment());


        //想你圈
        circleFragment = new CircleFragment();
        fragmentList.add(circleFragment);
        fragmentList.add(new MineFragment());

        QFragmentPagerAdapter fra = new QFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewpager.setOffscreenPageLimit(fragmentList.size());
        viewpager.setAdapter(fra);


        //底部导航切换viewpager
        bottomTab.setOnItemTabClickListener(new BottomTabLayout.OnItemTabClickListener() {
            @Override
            public void onItemTabClick(int position, View itemView) {
                changeTab(position);
                if (position == 0) {
                    messageListFragment.refresh();
                }

                if (position == 2) {//刷新想你圈

                    //                    circleFragment.refresh();

                }
            }
        });

        ViewCompat.setElevation(bottomTab, 6);
        bottomTab.selectItem(0);
    }

    /**
     * 未读消息数量
     */
    private void upDateMessageCount() {
        btvHome.setCount(getUnReadMessageCount());

    }

    private void changeTab(int position) {
        bottomTab.selectItem(position);
        viewpager.setCurrentItem(position, false);
    }

    @Override
    public void onBackPressed() {

        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastbacktime > 2000) {
            lastbacktime = currentTimeMillis;
            toast("再按一次退出i想你");
        } else {
            super.onBackPressed();
        }
    }

    private static final String TAG = "MainActivity";

    @Override
    protected void onResume() {
        super.onResume();
        upDateMessageCount();
        updateInviteMessage();

        DemoHelper demoHelper = DemoHelper.getInstance();

        LoginHelper loginHelper = LoginHelper.getInstance();

        if (!demoHelper.isLoggedIn() && loginHelper.isLogin(this)) {
            //环信断开连接，自动重连
            //            toast("您已断开聊天服务器，正在帮您重连");
            showProgressDialog("连接中...", true);
            EMClient.getInstance().login(HuanXinHelper.getHuanXinidbyUseid(loginHelper.getUserid(this)), "123456", new EMCallBack() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                            Log.i(TAG, "onSuccess: ");
                            toast("连接成功");
                        }
                    });


                }

                @Override
                public void onError(int i, String s) {


                    if ("User dosn't exist".equals(s)) {
                        String id = loginHelper.getUserid(MainActivity.this);
                        //                        Log.e("zcq", "onError：id是啥: " + id);


                        registerHuanXin(id);
                    }

                    runOnUiThread(() -> {
                        dismissProgressDialog();
                        //                        Log.e(TAG, "onError: " + s);
                        //                        Log.e("zcq", "onError：s是啥: " + s);
                        //                        Log.e("zcq", "onError：i是啥: " + i);
                        toast(s);
                    });
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }
        //强制修改原始密码
        try {
            getPhoneNum();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断有水没，有水的话喝没


    }

    /**
     * 如果用户没在环信注册了，再次注册环信
     *
     * @param userId
     */

    private void registerHuanXin(String userId) {
        //id
        RequestBody requsetBody = new FormBody.Builder()
                .add("userid", userId)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlString.URL_HUANXINUSER_ISNOTEXIST, requsetBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    int status = jo.getInt("status");
                    String message = jo.getString("message");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    //开始获取想你圈提醒
    private final int MESSAGE_START_GET_REMIND = 11;
    //停止获取想你圈提醒
    private final int MESSAGE_STOP_GET_REMIND = 12;


    private final int status_prefare = 0;
    private final int status_stop = 1;
    private final int status_started = 2;

    private int remind_status = 0;

    private boolean isRemind = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MESSAGE_START_GET_REMIND) {
                log("MESSAGE_START_GET_REMIND");
                if (remind_status != status_stop) {
                    getCircleRemind();
                }
            } else if (msg.what == MESSAGE_STOP_GET_REMIND) {
                log("MESSAGE_STOP_GET_REMIND");
                btvCircle.setPointVisiable(false);
                remind_status = status_stop;
                removeMessages(MESSAGE_START_GET_REMIND);
            }
        }
    };

    private void getCircleRemind() {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());

        RemoteDataHandler.asyncPost(UrlString.URL_NEW_CIRCLE_MESSAGE, params, this, false, response -> {
            String json = response.getJson();
            log("想你圈提醒" + json);

            if (bottomTab != null && !CommonUtils.isStringNull(json)) {
                CircleRemindBean circleRemindBean = new Gson().fromJson(json, CircleRemindBean.class);
                if (circleRemindBean.getStatus() == 1) {
                    CircleRemindBean.DataBean data = circleRemindBean.getData();
                    int momentnewnum = data.getMomentnewnum();
                    btvCircle.setPointVisiable(momentnewnum != 0);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startGetRemind();
                    }
                }, 60000);
            } else {
                stopGetRemind();
            }

        });
    }

    private void startGetRemind() {
        handler.sendEmptyMessage(MESSAGE_START_GET_REMIND);
    }

    private void stopGetRemind() {
        handler.sendEmptyMessage(MESSAGE_STOP_GET_REMIND);
    }

    @Override
    protected void onDestroy() {
        stopGetRemind();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(mreceiver);
        SuperObservableManager.getInstance().getObservable(OnHideBottomTab.class)
                .unregisterObserver(this);
        super.onDestroy();

    }

    /**
     * 获取用户手机号  获取身份证号
     */
    private void getPhoneNum() {
        UserInfoPresenter presenter = new UserInfoPresenter();
        //        showProgressDialog("加载中...", true);
        presenter.getUserInfo(this, new OnResult<UserInfoBean.DataBean>() {
            @Override
            public void onSuccess(UserInfoBean.DataBean dataBean) {
                dismissProgressDialog();
                String usermobile = dataBean.getUsermobile();
                //身份证
                String userId = dataBean.getUserid();
                if (!TextUtils.isEmpty(userId)) {
                    if (!shaohoulight) {
                        heshuimei(userId);
                    }

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("id获取错误");
                        }
                    });
                }
                //获取密码
                String pw = LoginHelper.getInstance().getPassword(MainActivity.this);
                //判断密码是否为手机号
                if (pw.equals(usermobile)) {
                    //                    AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
                    //                    alertdialog.setCancelable(false);
                    //                    alertdialog.setTitle("提示");
                    //                    alertdialog.setMessage("您的登录密码是原始密码，为了账户的资金等安全，请前往设置");
                    //                    alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    //                        @Override
                    //                        public void onClick(DialogInterface dialog, int which) {
                    //                            Intent intent = new Intent(MainActivity.this, ModifyPasswordActivity.class);
                    //                            intent.putExtra(ModifyPasswordActivity.isLoginPassword, true);
                    //                            startActivity(intent);
                    //                        }
                    //                    });
                    //
                    //                    alertdialog.show();


                    MyAlertdialog alertdialog = new MyAlertdialog(MainActivity.this);
                    alertdialog.setTitle("提示")
                            .setMessage("您的登录密码是原始密码，为了账户的资金等安全，请前往设置")
                            .setLeftText("取消")
                            .setRightText("确定")
                            .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                                @Override
                                public void onLeftClick(View view) {
                                }

                                @Override
                                public void onRightClick(View view) {
                                    Intent intent = new Intent(MainActivity.this, ModifyPasswordActivity.class);
                                    intent.putExtra(ModifyPasswordActivity.isLoginPassword, true);
                                    startActivity(intent);
                                }
                            });
                    alertdialog.setCancelable(false);
                    alertdialog.show();
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);
            }
        });
    }

    //获取id   看喝水没
    private void huoquidkanheshui() {
        UserInfoPresenter presenter = new UserInfoPresenter();
        //        showProgressDialog("加载中...", true);
        presenter.getUserInfo(this, new OnResult<UserInfoBean.DataBean>() {
            @Override
            public void onSuccess(UserInfoBean.DataBean dataBean) {
                dismissProgressDialog();
                //id
                String userId = dataBean.getUserid();

                if (!TextUtils.isEmpty(userId)) {
                    //看喝水没
                    heshuimei(userId);

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast("id获取错误");
                        }
                    });
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);
            }
        });
    }

    private void heshuimei(String userId) {
        RequestBody requsetBody = new FormBody.Builder()
                .add("relevanceUserId", userId)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlString.URL_QUERY_JINENG, requsetBody, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast("网络开小差了");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String jinenglist = jo.getString("data");
                    int status = jo.getInt("status");
                    if (status == 1) {//等于1说明有水
                        Gson gson = new Gson();
                        List<JiNengBean> jineng = gson.fromJson(jinenglist, new TypeToken<List<JiNengBean>>() {
                        }.getType());
                        JiNengBean water = jineng.get(0);
                        int relevanceSign = water.getRelevanceSign();
                        if (relevanceSign == 0) {//说明有水但没喝
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //                                    toast("你有一瓶水没喝，请前往技能认证去点亮");
                                    MyAlertdialog alertdialog = new MyAlertdialog(MainActivity.this);
                                    alertdialog.setTitle("小提示")
                                            .setMessage("您已成功签约，融信新企将赠送您一瓶信心源泉，如已领取赠品请点击我/技能认证点亮")
                                            .setLeftText("以后再说")
                                            .setRightText("去点亮")
                                            .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                                                @Override
                                                public void onLeftClick(View view) {
                                                    shaohoulight = true;
                                                }

                                                @Override
                                                public void onRightClick(View view) {

                                                    Intent intentJn = new Intent(MainActivity.this, JiNengRenZhengActivity.class);
                                                    intentJn.putExtra("userId", userId);
                                                    startActivity(intentJn);

                                                }
                                            });
                                    alertdialog.setCancelable(false);
                                    alertdialog.show();
                                }
                            });

                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void checkPermission() {
        List<String> requestPermissions = new ArrayList<>();
        int i0 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int i1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (i0 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (i1 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (requestPermissions.size() > 0) {
            requestPermission(requestPermissions);
        } else {
            startLocation();
        }
    }

    private void requestPermission(List<String> requestPermissions) {
        ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), 100);
    }

    private void initLocation() {
        locationService = new LocationService(this);
        locationService.registerListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationService.stop();
    }

}
