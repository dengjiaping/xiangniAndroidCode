package com.handongkeji.impactlib.util;


import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.handongkeji.impactlib.R;

public class ToastUtils {

    public static Toast toast = null;



    public static void show(Context context, String info) {

        cancel();

        View view = getTextView(context, info);
        toast = new Toast(context);
        if(!TextUtils.isEmpty(info)&&info.length()>=10){
            toast.setDuration(Toast.LENGTH_LONG);
        }
        toast.setView(view);
        toast.show();
    }

    private static void cancel() {
        if(toast!=null){
            toast.cancel();
        }
    }


    public static void showLong(Context context,String info){
        cancel();
        View view = getTextView(context, info);
        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public static void showSuccess(Context context,String message){

        cancel();
        View view = LayoutInflater.from(context).inflate(R.layout.toast_success, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_message);
        tv.setText(message);
        toast = getToast(view);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

    }


    public static Toast getToast(View view){

        Toast toast = new Toast(view.getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        return toast;
    }


    public static void showAlert(Context context,String title,String message){

        cancel();
        View view = LayoutInflater.from(context).inflate(R.layout.toast_alert, null);
        ViewHelper helper = new ViewHelper(view);
        helper.setText(R.id.tv_title,title);
        helper.setText(R.id.tv_message,message);

        toast = new Toast(context);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static void showNetError(Context context){
        showAlert(context,"网络不给力","请检查网络...");
    }

    private static View getTextView(Context context,String info){
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tv_toast, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_toast);
        tv.setText(info);
        return view;
    }

    public static void show(Context context, String info, final long duration) {
        cancel();
        if (context == null)
        {
            return;
        }
        toast = Toast.makeText(context, info, Toast.LENGTH_SHORT);
        toast.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, duration);
    }

}
