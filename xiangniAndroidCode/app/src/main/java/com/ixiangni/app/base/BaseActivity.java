package com.ixiangni.app.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.handongkeji.common.Constants;
import com.handongkeji.impactlib.util.ToastUtils;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.RegexUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/6/16 0016.
 */

public class BaseActivity extends AppCompatActivity {
    /*标识是否开启调试模式的boolean变量*/
    public static final boolean DEBUG = true;
    /*一个上下文对象的声明*/
    protected BaseActivity mContext;
    /*应用程序之间进行调度和当前输入法交互*/
    protected InputMethodManager inputMethodManager;
    /*进度对话框对象*/
    protected ProgressDialog mProgressDialog;
    /*FrameLayout对象的声明*/
    protected FrameLayout mContent;
    /*进度条对象的声明*/
    private ProgressBar progressBar;

    /**
     * @param savedInstanceState 存储意外关闭的时候的数据
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;//context的初始化
        mContent = (FrameLayout) findViewById(android.R.id.content);//context的初始化
        progressBar = new ProgressBar(this);
        progressBar.setIndeterminate(true);
        //设置启动的时候手机的屏幕的方向
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*程序调度的对象的初始化*/
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /*设置强制打开输入法*/
    protected void showKeyBoard(View view) {
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /*隐藏软键盘的方法*/
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * log日志打印的方法
     * @param string  打印到控制台的日志信息
     */
    protected void log(String string) {
        if (DEBUG) {
            Log.i(getClass().getSimpleName(), "" + string);
        }
    }

    /**
     * 显示进度对话框
     * @param title  进度对话框的标题
     * @param message  显示的信息
     * @param cancelable  是否可以关闭
     */
    protected void showProgressDialog(String title, String message, boolean cancelable) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(message);
        if (!TextUtils.isEmpty(title)) {
            mProgressDialog.setTitle(title);
        }
        mProgressDialog.show();
    }

    /**
     * 显示进度对话框
     * @param message  显示的进度信息
     * @param cancelable  对话框是否可以取消
     */
    protected void showProgressDialog(String message, boolean cancelable) {
        showProgressDialog(null, message, true);
    }

    /**
     * 去除对话框
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 设置进度条提示的信息
     * @param message   提示的信息
     */
    protected void setProgressMessage(String message) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(message);
        }
    }

    protected boolean pbShowing = false;
//    判断进度条是不是正在显示
    public boolean isProgressBarShowing() {

        return pbShowing;
    }
//  app暂定的操作 禁止一切加载的操作
    @Override
    protected void onPause() {
        super.onPause();
        Glide.with(this).pauseRequests();
    }
//  展示进度条
    public void showProgressBar() {
        if (!pbShowing) {

            pbShowing = true;
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams
                    (FrameLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            mContent.addView(progressBar, layoutParams);
        }
    }
//  隐藏进度条
    public void hideProgressBar() {
        if (isProgressBarShowing()) {
            mContent.removeView(progressBar);
            pbShowing = false;
        }
    }

    /**
     * 土司提示信息的工具方法
     * param 土司提示的信息
     */
    protected void toast(String message) {
        ToastUtils.show(this, message);
    }

    /**
     * 从当前的activity跳转到指定的activity
     * param 待跳转的activity
     */
    protected void startActivity(Class<? extends Activity> clzz) {
        startActivity(new Intent(this, clzz));
    }

    /**
     * 加载圆形图片的工具方法
     * param 1 等待加载的图片的地址
     * param 2 显示加载的图片的控件
     * param 3 无法加载图片的时候显示的图片的id
     */
    protected void loadRoundImage(String path, ImageView iv, int errorid) {
        if (CommonUtils.isStringNull(path)) {
            iv.setImageResource(errorid);
        } else {
            /*图片的加载框架的使用*/
            Glide.with(this).load(path).asBitmap().error(errorid).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                    drawable.setCircular(true);
                    iv.setImageDrawable(drawable);
                }
            });
        }

    }

    /**
     * 处理服务器返回的json数据
     * param1 等待处理的服务器返回来的json数据
     * param2 返回来的数据的处理的结果
     */
    protected void handJson(String json, String successMessage) {
        if (TextUtils.isEmpty(json)) {
            toast(Constants.CONNECT_SERVER_FAILED);
        } else {
            try {
                JSONObject object = new JSONObject(json);
                int anInt = object.getInt(Constants.status);
                if (1 == anInt) {
                    toast(successMessage);
                    finish();
                } else {
                    toast(object.getString(Constants.message));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断数据的字符串（身份证）是不是为空同时是不是符合指定的正则表达式
     */
    protected boolean checkIdNum(String idnum) {
        if (TextUtils.isEmpty(idnum)) {
            toast("请输入身份证号");
            return false;
        }

        if (!RegexUtils.checkIdCard(idnum)) {
            toast("身份证号格式不正确");
            return false;
        }

        return true;
    }

    /**
     * 判断数据的字符串（电话号码）是不是为空同时是不是符合指定的正则表达式
     */
    protected boolean checkPhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            toast("请输入手机号");
            return false;
        }

        if (!RegexUtils.checkMobile(phoneNum)) {
            toast("手机号格式不正确");
            return false;
        }

        return true;
    }

    /*当按返回键的时候的处理逻辑*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismissProgressDialog();
    }

    /*activity销毁的时候的处理逻辑*/
    @Override
    protected void onDestroy() {
        mContext = null;
        super.onDestroy();
    }
}
