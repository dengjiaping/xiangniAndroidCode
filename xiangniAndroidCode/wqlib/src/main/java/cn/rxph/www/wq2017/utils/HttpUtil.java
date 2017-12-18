package cn.rxph.www.wq2017.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {

//    public static void sendOkHttpRequest(String address, okhttp3.Callback callback) {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(address).build();
//        client.newCall(request).enqueue(callback);
//    }

    public static void sendOkHttpPostRequest(String address, RequestBody requsetBody, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .post(requsetBody)
                .build();
        // Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
