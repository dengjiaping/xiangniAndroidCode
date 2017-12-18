package com.ixiangni.app.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.handongkeji.impactlib.util.ToastUtils;

/**
 * Created by Administrator on 2017/6/16 0016.
 */

public class BaseFragment extends Fragment {


    /**
     *  声明一个进度对话框
     */
    protected ProgressDialog mProgressDialog;

    /**
     * 在一个Fragment中启动一个activity
     * @param clazz  等待启动的activity
     */
    protected void startActivity(Class<? extends Activity> clazz) {
        FragmentActivity activity = getActivity();
        Intent intent = new Intent(activity, clazz);
        activity.startActivity(intent);

    }

    /**
     * 显示进度对话框
     * @param title   进度对话框的标题
     * @param message  进度对话框的提示消息
     * @param cancelable  对话框是不是可以强制退出
     */
    protected void showProgressDialog(String title, String message, boolean cancelable) {
        mProgressDialog = ProgressDialog.show(getContext(), title, message, cancelable);
    }

    /**
     * 显示进度对话框
     * @param message  进度对话框显示的信息
     * @param cancelablei 进度对话框是不是可以强制退出
     */
    protected void showProgressDialog(String message, boolean cancelable) {
        showProgressDialog(null, message, cancelable);
    }

    /**
     * 是进度对话框消失
     */
    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 设置对话框显示的信息
     * @param message
     */
    protected void setProgressMessage(String message) {
        if (mProgressDialog != null) {
            mProgressDialog.setMessage(message);
        }
    }

    /**
     * 日志打印的方法
     * @param string  日志打印的信息
     */
    protected void log(String string) {
        if (BaseActivity.DEBUG) {
            Log.i(getClass().getSimpleName(), "" + string);
        }
    }

    /**
     *  加载圆形图片
     * @param path  加载的图片的地址
     * @param iv     显示图片的控件
     * @param errorid  加载错误的时候的错误id
     */
    protected void loadRoundImage(String path, ImageView iv, int errorid) {
        if (TextUtils.isEmpty(path) || "null".equals(path)) {
            iv.setImageResource(errorid);
            return;
        }
        Glide.with(this).load(path).asBitmap().error(errorid).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                drawable.setCircular(true);
                iv.setImageDrawable(drawable);
            }
        });
    }

    protected void toast(String message) {
        ToastUtils.show(getContext(), message);
    }
}
