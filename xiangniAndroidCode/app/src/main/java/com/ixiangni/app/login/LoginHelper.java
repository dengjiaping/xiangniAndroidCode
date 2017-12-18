package com.ixiangni.app.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ixiangni.util.HuanXinHelper;
import com.nevermore.oceans.ob.Dispatcher;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

/**
 * Created by Administrator on 2017/6/16 0016.
 * 添加注释
 */

public class LoginHelper {
    public static final String SYS_INFO = "app_info";
//    创建LoginHelper对象
    private static final LoginHelper instance = new LoginHelper();
    private final String loginState = "loginState";//登陆状态的字符串
    private final String token = "token";
    private final String userName = "userName";//用户名
    private final String password = "password";//密码
    private final String userid = "userid";//用户id
//    LoginHelper对象的初始化
    public static LoginHelper getInstance() {
        return instance;
    }


    /**
     * SharedPreferences
     * @param context   上下文对象
     * @return  返回创建的sharedPreference
     */
    public SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(SYS_INFO, Context.MODE_PRIVATE);
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean isLogin(Context context) {
        return getSharedPreferences(context).getBoolean(loginState, false);
    }

    /**
     * 获取token
     *
     * @return
     */
    public String getToken(Context context) {
        return getSharedPreferences(context).getString(this.token, "");
    }


    /**
     * 获取用户id
     *
     * @return
     */
    public String getUserid(Context context) {
        return getSharedPreferences(context).getString(this.userid, "");
    }

    public String getHuanxinid(Context context) {
        return "ixn" + getSharedPreferences(context).getString(this.userid, "");
    }

    public String getPassword(Context context) {
        return getSharedPreferences(context).getString(this.password, "");
    }

    public String getUserName(Context context) {
        return getSharedPreferences(context).getString(this.userName, "");
    }


    /**
     * 登录并保存用户信息
     *
     * @param token
     * @param userName
     * @param password
     */
    public void login(Context context, String token, String userid, String userName, String password) {

        getSharedPreferences(context).edit()
                .putBoolean(this.loginState, true)
                .putString(this.token, token)
                .putString(this.userName, userName)
                .putString(this.password, password)
                .putString(this.userid, userid)
                .commit();
        //通知观察者已登录
        SuperObservableManager.getInstance().getObservable(LoginState.class).notifyMethod(new Dispatcher<LoginState>() {
            @Override
            public void call(LoginState loginState) {

                loginState.onLogin();
            }
        });


//        loginHuanxi(userid);
        registerAlias(context, userid);

    }


    private void registerAlias(Context context, String userid) {
        final PushAgent pushAgent = PushAgent.getInstance(context);

        String alias = "ixn" + userid;
        String aliaType = "ixiangni_type";

        pushAgent.addExclusiveAlias(alias, aliaType, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
                Log.i("Umeng", "绑定addAlias: " + b + "," + s);

                if (b) {
                    pushAgent.getTagManager().update(new TagManager.TCallBack() {
                        @Override
                        public void onMessage(boolean b, ITagManager.Result result) {


                        }
                    }, "1");
                }

            }
        });

    }

    private static final String TAG = "LoginHelper";

    public void loginHuanxi(String userid) {
        EMClient.getInstance().login(HuanXinHelper.getHuanXinidbyUseid(userid), "123456", new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "onSuccess: ");
            }

            @Override
            public void onError(int i, String s) {

                Log.e(TAG, "onError: " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }


    /**
     * 退出登录
     */
    public void logout(Context context) {
        getSharedPreferences(context).edit()
                .putString(this.token, null)
                .putBoolean(this.loginState, false)
                .apply();
        //通知观察者已退出登录
        SuperObservableManager.getInstance().getObservable(LoginState.class).notifyMethod(new Dispatcher<LoginState>() {
            @Override
            public void call(LoginState loginState) {
                loginState.onLogout();
            }
        });

        logOutHuanxin();


        String userid = getUserid(context);
        String alias = "ixn" + userid;
        String aliaType = "ixiangni_type";


        PushAgent.getInstance(context).removeAlias(alias, aliaType, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
                Log.i("Umeng", "onMessage:解绑友盟 " + b + s);
            }
        });

        PushAgent.getInstance(context).getTagManager().update(new TagManager.TCallBack() {
            @Override
            public void onMessage(boolean b, ITagManager.Result result) {

            }
        }, "2");
    }

    private void logOutHuanxin() {

        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "onSuccess: 环信退出");
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "onError: 环信退出");
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });

    }


}
