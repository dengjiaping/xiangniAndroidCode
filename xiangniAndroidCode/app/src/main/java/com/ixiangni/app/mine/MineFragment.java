package com.ixiangni.app.mine;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.zxing.encoding.EncodingHandler;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.DemoModel;
import com.hyphenate.chatuidemo.parse.UserProfileManager;
import com.ixiangni.app.LoginActivity;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.SettingActivity;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.login.LoginState;
import com.ixiangni.app.message.MessageCenterActivity;
import com.ixiangni.app.missyouservice.MissyouServiceActivity;
import com.ixiangni.app.money.MineWalletActivity;
import com.ixiangni.app.user.PersonalInfoActivity;
import com.ixiangni.app.user.PhotoAlbumActivity;
import com.ixiangni.bean.MsgCountbean;
import com.ixiangni.bean.UserInfoBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.QrCodedialog;
import com.ixiangni.interfaces.IMsgCountChang;
import com.ixiangni.interfaces.OnUserInfoChange;
import com.ixiangni.presenters.UserInfoPresenter;
import com.ixiangni.util.UserInfoUtil;
import com.mydemo.yuanxin.activity.YXCardActivity;
import com.mydemo.yuanxin.util.HttpUtil;
import com.mydemo.yuanxin.util.UrlUtil;
import com.nevermore.oceans.ob.SuperObservableManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.rxph.www.wq2017.activity.WQMainActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements LoginState, OnResult<UserInfoBean.DataBean>, OnUserInfoChange {


    @Bind(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @Bind(R.id.fl_icon)
    FrameLayout flIcon;
    @Bind(R.id.iv_erweima)
    ImageView ivErweima;
    @Bind(R.id.rl_login)
    RelativeLayout rlLogin;
    @Bind(R.id.iv_setting)
    ImageView ivSetting;
    @Bind(R.id.iv_not_login)
    ImageView ivNotLogin;
    @Bind(R.id.rl_not_login)
    RelativeLayout rlNotLogin;
    @Bind(R.id.tv_my_publish)
    TextView tvMyPublish;
    @Bind(R.id.tv_my_wallet)
    TextView tvMyWallet;
    @Bind(R.id.tv_my_file)
    TextView tvMyFile;
    @Bind(R.id.tv_my_expression)
    TextView tvMyExpression;
    @Bind(R.id.tv_missyou_service)
    TextView tvMissyouService;
    @Bind(R.id.tv_online)
    TextView tvOnline;
    @Bind(R.id.tv_setting)
    TextView tvSetting;
    @Bind(R.id.btn_login_now)
    Button btnLoginNow;
    @Bind(R.id.tv_message_center)
    TextView tvMessageCenter;
    @Bind(R.id.rl_personal_info)
    RelativeLayout rlPersonalInfo;
    @Bind(R.id.tv_person_name)
    TextView tvPersonName;
    @Bind(R.id.iv_gender)
    ImageView ivGender;
    @Bind(R.id.tv_yuanxinbi)
    TextView tvYuanxinbi;
    @Bind(R.id.tv_follow_count)
    TextView tvFollowCount;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private QrCodedialog qrCodedialog;
    private UserInfoPresenter userInfoPresenter;

    //身份证号
    public String userName;
    private String userId;
    private double yxbYUEE;
    private String userpic2;

    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SuperObservableManager sm = SuperObservableManager.getInstance();
        sm.getObservable(LoginState.class).registerObserver(this);
        sm.getObservable(OnUserInfoChange.class).registerObserver(this);
        SuperObservableManager.registerObserver(IMsgCountChang.class, msgCountChang);
    }

    private IMsgCountChang msgCountChang = new IMsgCountChang() {
        @Override
        public void onMSgCountChange() {
            getMessageCount();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);

        userInfoPresenter = new UserInfoPresenter();
        //登录
        boolean login = LoginHelper.getInstance().isLogin(getContext());
        if (login) {
            onLogin();
        } else {
            onLogout();
        }

        getMessageCount();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({
            R.id.tv_bbyx, R.id.tv_wq, R.id.tv_jn,
            R.id.iv_user_icon,
            R.id.btn_login_now,
            R.id.tv_orders,
            R.id.tv_message_center,
            R.id.rl_personal_info,
            R.id.fl_icon, R.id.iv_erweima, R.id.rl_login, R.id.iv_setting, R.id.iv_not_login,
            R.id.rl_not_login, R.id.tv_my_publish, R.id.tv_my_wallet, R.id.tv_my_file, R.id.tv_my_expression,
            R.id.tv_missyou_service, R.id.tv_online, R.id.tv_setting})
    public void onViewClicked(View view) {
        boolean isLogin = LoginHelper.getInstance().isLogin(getContext());

        switch (view.getId()) {

            case R.id.tv_bbyx:
                Intent intentYx = new Intent(getActivity(), YXCardActivity.class);
                try {
                    sendTurnForgetRequset(userName, intentYx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_wq:
//                Intent intentWq = new Intent(getActivity(), WQMainActivity.class);
//                sendTurnForgetRequset(userName, intentWq);

                String token = MyApp.getInstance().getUserTicket();

                if (!TextUtils.isEmpty(userId)) {
                    Intent intentWq = new Intent(getActivity(), WQMainActivity.class);
                    intentWq.putExtra("userId", userId);
                    intentWq.putExtra("token", token);
                    startActivity(intentWq);
                } else {
                    Toast.makeText(getActivity(), "userId获取失败，无法进入", Toast.LENGTH_SHORT).show();
                }

                //获取级别
//                sendJiBieRequset();

                break;
            case R.id.tv_jn:
                if (!TextUtils.isEmpty(userId)) {
                    Intent intentJn = new Intent(getActivity(), JiNengRenZhengActivity.class);
                    intentJn.putExtra("userId", userId);
                    intentJn.putExtra("userpic2", userpic2);
                    startActivity(intentJn);
                } else {
                    Toast.makeText(getActivity(), "userId获取失败，无法进入", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.iv_user_icon:
                startActivity(PersonalInfoActivity.class);

                break;
            case R.id.rl_personal_info:
                startActivity(PersonalInfoActivity.class);
                break;
            case R.id.fl_icon:
                break;
            case R.id.iv_erweima:
                if (qrCodedialog != null) {
                    qrCodedialog.show();
                }
                break;
            case R.id.rl_login:
                break;
            case R.id.iv_setting:
                break;
            case R.id.rl_not_login:
                break;
            case R.id.tv_my_publish://我的发布
                if (isLogin) {
                    PhotoAlbumActivity.start(getContext(), "", true);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_message_center://消息中心
                if (isLogin) {
                    startActivity(MessageCenterActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_my_wallet://我的钱包
                if (isLogin) {
                    Intent intent = new Intent(getActivity(), MineWalletActivity.class);
                    intent.putExtra("userIdCard", userName);
                    startActivity(intent);

//                    startActivity(MineWalletActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_my_file://我的文件
                if (isLogin) {
                    startActivity(MyFilesActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_my_expression://我的表情
                if (isLogin) {

                    startActivity(EmotionActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_missyou_service://想你服务
                if (isLogin) {

                    startActivity(MissyouServiceActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_online://直播
//                startActivity(LiveActivity.class);
                startActivity(ThingActivity.class);
                break;
            case R.id.tv_orders:
                startActivity(MyOrderActivity.class);
                break;
            case R.id.tv_setting://设置
                startActivity(SettingActivity.class);
                break;
            case R.id.btn_login_now:
                startActivity(LoginActivity.class);
                break;
        }
    }


    @Override
    public void onLogin() {
        rlLogin.setVisibility(View.VISIBLE);
        rlNotLogin.setVisibility(View.GONE);

        getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LoginHelper.getInstance().isLogin(getContext())) {
            userInfoPresenter.getUserInfo(getContext(), this);
        }
    }

    /**
     * 获取消息数量
     */
    private void getMessageCount() {

        if (!LoginHelper.getInstance().isLogin(getContext())) {
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_MESSAGE_COUNT, params, getContext(), true, response -> {
            String json = response.getJson();
            log(json);
            if (!CommonUtils.isStringNull(json) && tvMessageCenter != null) {
                MsgCountbean msgCountbean = new Gson().fromJson(json, MsgCountbean.class);
                if (1 == msgCountbean.getStatus()) {
                    MsgCountbean.DataBean data = msgCountbean.getData();
                    tvMessageCenter.setText(data.getWeidu() == 0 ? "" : "" + data.getWeidu());
                }
            }

        });

    }


    /**
     * 获取用户信息
     */
    private void getUserInfo() {

        String erCodeContent = UrlString.URL_QCODE + "?" + LoginHelper.getInstance().getUserid(getContext());
        try {
            Bitmap qrCodeBitmap = EncodingHandler.createQRCode(erCodeContent, CommonUtils.dip2px(getContext(), 75));
            if (qrCodeBitmap != null) {
                Bitmap bitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(), qrCodeBitmap.getHeight(), Bitmap.Config.ARGB_4444);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(qrCodeBitmap, 0, 0, new Paint());
                ivErweima.setImageBitmap(bitmap);
            } else {
                toast("二维码生成错误");
            }


        } catch (WriterException e) {
            e.printStackTrace();
        }


        progressBar.setVisibility(View.VISIBLE);
        userInfoPresenter.getUserInfo(getContext(), this);
    }

    @Override
    public void onLogout() {
        rlNotLogin.setVisibility(View.VISIBLE);
        rlLogin.setVisibility(View.GONE);

        tvPersonName.setText("");
        ivUserIcon.setImageResource(R.mipmap.touxiangmoren);
    }


    @Override
    public void onDetach() {
        SuperObservableManager.getInstance().getObservable(LoginState.class).unregisterObserver(this);
        SuperObservableManager.getInstance().getObservable(OnUserInfoChange.class).unregisterObserver(this);
        SuperObservableManager.unregisterObserver(IMsgCountChang.class, msgCountChang);
        super.onDetach();
    }


    @Override
    public void onSuccess(UserInfoBean.DataBean dataBean) {
        progressBar.setVisibility(View.GONE);
        //是否接受消息推送

        //想你圈消息提醒
        int remind = dataBean.getRemind();
        if (0 == remind) {
            getActivity().sendBroadcast(new Intent(XNConstants.start_circle_remind));
        }


        int getsysmessage = dataBean.getGetsysmessage();
        SharedPreferences sharedPreferences = LoginHelper.getInstance().getSharedPreferences(getContext());
        sharedPreferences.edit().putBoolean(SettingActivity.getSysMsg, getsysmessage == 0).commit();
        DemoHelper.getInstance().getModel().setSettingMsgNotification(getsysmessage == 0);
        DemoModel model = DemoHelper.getInstance().getModel();
        boolean settingMsgNotification = model.getSettingMsgNotification();
        boolean settingMsgSound = model.getSettingMsgSound();
        boolean settingMsgVibrate = model.getSettingMsgVibrate();

        log("settingMsgNotification" + settingMsgNotification);
        log("settingMsgSound" + settingMsgSound);
        log("settingMsgVibrate" + settingMsgVibrate);


        //头像
        String userpic = dataBean.getUserpic();
        userpic2 = userpic;
        loadRoundImage(userpic, ivUserIcon, R.mipmap.touxiangmoren);

        UserProfileManager userProfileManager = DemoHelper.getInstance().getUserProfileManager();
        if (!CommonUtils.isStringNull(userpic)) {
            userProfileManager.setCurrentUserAvatar(userpic);
        }

        //昵称
        String usernick = dataBean.getUsernick();
        tvPersonName.setText(usernick);
        //修改当前昵称
        EMClient.getInstance().updateCurrentUserNick(usernick);
        userProfileManager.setCurrentUserNick(usernick);


        //二维码弹框
        qrCodedialog = new QrCodedialog(getContext(), userpic, usernick);


        //性别
        int usersex = dataBean.getUsersex();
        UserInfoUtil.setGenderIcon(usersex, ivGender);

        //元信币
        double useraccount = dataBean.getUseraccount();
        yxbYUEE = useraccount;
        String yxb = "银信币:" + String.format(Locale.CHINA, "%.2f", useraccount);
//        tvYuanxinbi.setText(yxb);

//        //关注
//        int focusnum = dataBean.getFocusnum();
//        tvFollowCount.setText(focusnum + "人关注");

        //职位
        String jobName = dataBean.getTaobaotoken();
        tvYuanxinbi.setText(jobName);


        //部门
        String departmentName = dataBean.getTaobaoid();
        tvFollowCount.setText(departmentName);
        //用户userid 身份证
        userId = dataBean.getUserid();
        userName = dataBean.getUsernumber();


    }

    @Override
    public void onFailed(String errorMsg) {
        progressBar.setVisibility(View.GONE);
        toast(errorMsg);

    }

    @Override
    public void change() {
        getUserInfo();
    }

    /**
     * 获取用户级别
     */
    private void sendJiBieRequset() {

        String token = MyApp.getInstance().getUserTicket();

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(getActivity(), "userId获取失败，无法进入", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();


        final RequestBody requsetBody = new FormBody.Builder()
                .add("userId", userId)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlString.URL_USER_LEVEL, requsetBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络错误,暂无法连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                closeProgressDialog();
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    int jibie = jo.getInt("data");
                    int status = jo.getInt("status");
                    if (status != 1) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toast("操作失败");
                            }
                        });
                        return;
                    }
                    if (jibie == 1 || jibie == 2 || jibie == 3 || jibie == 4 || jibie == 5 || jibie == 665) {

                        Intent intentWq = new Intent(getActivity(), WQMainActivity.class);
                        intentWq.putExtra("userId", userId);
                        intentWq.putExtra("token", token);
                        startActivity(intentWq);
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toast("因为级别原因，您暂不能签约他人");
                            }
                        });
                    }


                } catch (JSONException e) {


                }

            }
        });
    }

    /**
     * 圆信跳转请求
     *
     * @param userName
     * @param intent
     */

    private void sendTurnForgetRequset(final String userName, Intent intent) {
        showProgressDialog();

        final RequestBody requsetBody = new FormBody.Builder()
                .add("strIdentity", userName)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlUtil.ForgetPW_URL(), requsetBody, new Callback() {

            public String superId;
            public String userTel;

            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "网络错误,暂无法连接到服务器", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                closeProgressDialog();
                String responseText = response.body().string();
                try {
                    JSONObject jo = new JSONObject(responseText);
                    superId = jo.getString("userId");
//                    userTel = jo.getString("Phone");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if ("false".equals(superId)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(getActivity(), "您输入的账号错误或者此账号没有绑定手机号", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(), "无法进入，原因:未绑定您的企业一卡通", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    if (!TextUtils.isEmpty(superId)) {
//                        intent.putExtra("userTel", userTel);
                        intent.putExtra("superId", superId);
                        startActivity(intent);

                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "网络获取账号错误，建议重新登录", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    //加载中方法
    private ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("正在加载...");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
