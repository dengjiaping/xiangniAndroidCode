package com.handongkeji.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * 共享参数据工具类
 * @author apple
 *
 */
public class SPUtils {

	public static void saveLoc(Context context, double x, double y){
		SharedPreferences pres=context.getSharedPreferences("loc", Context.MODE_PRIVATE);
		Editor editor=pres.edit();
		editor.putString("x", String.valueOf(x));
		editor.putString("y", String.valueOf(y));
		
		editor.commit();
	}
	
	public static HashMap<String,String> getLoc(Context context){
		SharedPreferences pres=context.getSharedPreferences("loc", Context.MODE_PRIVATE);
		
		
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("x",pres.getString("x", null));
		map.put("y",pres.getString("y", null));
		
		return map;
	}
	
}
