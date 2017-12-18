package com.handongkeji.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

public class CommonUtils {

    public static boolean isStringNull(String string){
        if(TextUtils.isEmpty(string)||"null".equals(string)){
            return true;
        }
        return false;
    }


	private static long lastClickTime;
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if ( 0 < timeD && timeD < 500) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
    
    /** 
     * ����ֻ�ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  

    /** 
     * ����ֻ�ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    public static byte[] bitmap2Bytes(Bitmap bm){   
        ByteArrayOutputStream baos = new ByteArrayOutputStream();     
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);     
        return baos.toByteArray();   
   }   
    
    /*
     * 判断是否有网�?
     * */
    public static boolean isNetworkConnected(Context context) { 
    	if (context != null) { 
	    	ConnectivityManager mConnectivityManager = (ConnectivityManager) context 
	    	.getSystemService(Context.CONNECTIVITY_SERVICE); 
	    	NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo(); 
	    	if (mNetworkInfo != null) { 
	    		return mNetworkInfo.isAvailable(); 
	    	} 
    	} 
    	return false; 
    } 

    public static int px2sp(Context context, float pxValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (pxValue / fontScale + 0.5f); 
    } 
    
    public static int sp2px(Context context, float spValue) { 
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 

    private static Toast toast;
    public static void showToast(Context context , CharSequence text){
    	if (toast == null) {
    		toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
    	toast.setText(text);
    	toast.show();
    }

    /**
     * 设置editText最多输入的小数位数
     * inputType设为：numberDemical
     * @param editText
     * @param pointCount
     */
    public static void setPoint(final EditText editText, final int pointCount) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > pointCount) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + pointCount+1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public static boolean isRemindName(String name){
        if(TextUtils.isEmpty(name)){
            return false;
        }
        if(name.contains("[")&&name.contains("]")){
            return true;
        }
        return false;
    }

    public static String getMaskRemindName(String remind){

        return String.format(Locale.CHINA,"[%s]",remind);
    }

    public static String getParseName(String name){
        return name.replace("[","").replace("]","");
    }

}
