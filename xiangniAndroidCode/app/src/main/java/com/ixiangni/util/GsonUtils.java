package com.ixiangni.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Administrator on 2016/11/1.
 */
public class GsonUtils {

    private static Gson gson = new Gson();

    public static Object fromJson(String json, Class clazz) {
        return fromJson(json, (Type) clazz);
    }

    public static Object fromJson(String json, Type type) {
        Object o = gson.fromJson(json, type);
        return o;
    }

}
