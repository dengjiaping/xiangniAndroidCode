/**
 * / *  ClassName: TopicsDataHandler.java
 * created on 2012-3-3
 * Copyrights 2011-2012 qjyong All rights reserved.
 * site: http://blog.csdn.net/qjyong
 * email: qjyong@gmail.com
 */
package com.handongkeji.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.handongkeji.common.Constants;
import com.handongkeji.common.HttpHelper;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.ConfigCacheUtil;
import com.handongkeji.widget.NetUtils;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 用于发送HTTP请求并处理响应返回的数据的Handler
 *
 * @author qjyong
 */
public class RemoteDataHandler {
    public static final String TAG = "RemoteDataHandler";
    private static final String _CODE = "status";
    private static final String _DATAS = "Json";
    private static final String _HASMORE = "haveMore";
    private static final String _COUNT = "count";
    private static final String _RESULT = "result";

    public static final String STATE_REMOTE_LOGIN = "state_remote_login";
    public static final String STATE_ACCOUNT_FREEZE = "state_account_freeze";
    public static final String STATE_ACCOUNT_NOT_EXIST = "state_account_not_exist";


    // 线程池
    // private ExecutorService pool = Executors.newCachedThreadPool();
    static int coreSize = Math.max(2, Math.min(4, Runtime.getRuntime().availableProcessors()));
    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(coreSize, 50, 15L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    private RemoteDataHandler() {
        throw new AssertionError("No instances.");
    }

    public interface Callback {
        /**
         * HTTP响应完成的回调方法
         *
         * @param response 响应返回的数据对象
         */
        void dataLoaded(ResponseData response);
    }

    public interface StringCallback {

        public void dataLoaded(String str);
    }

    /**
     * 异步GET请求封装
     *
     * @param url
     * @param callback
     */
    public static void asyncGet(final String url, final Callback callback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                data.setJson((String) msg.obj);
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));

                callback.dataLoaded(data);
            }
        };
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean(_HASMORE, false);

                try {
                    String json = HttpHelper.get(url);
                    // 注意:目前服务器返回的JSON数据串中会有特殊字符（换行、回车）。需要处理一下
                    json = json.replaceAll("\\x0a|\\x0d", "");
                    JSONObject obj = new JSONObject(json);
                    if (null != obj && obj.has(_CODE)) {
                        msg.what = Integer.valueOf(obj.getString(_CODE));
                        if (obj.has(_DATAS)) {
                            JSONArray array = obj.getJSONArray(_DATAS);
                            msg.obj = array.toString();
                        }
                        if (obj.has(_HASMORE)) {
                            msg.getData().putBoolean(_HASMORE, obj.getBoolean(_HASMORE));
                        }
                        if (obj.has(_COUNT)) {
                            msg.getData().putLong(_COUNT, obj.getLong(_COUNT));
                        }

                        if (obj.has(_RESULT)) {
                            msg.getData().putString(_RESULT, obj.getString(_RESULT));
                        }
                    }
                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                }

                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 异步GET请求封装
     *
     * @param url
     * @param callback
     */
    public static void asyncGet2(final String url, final Callback callback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                data.setJson((String) msg.obj);
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));

                callback.dataLoaded(data);
            }
        };
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean(_HASMORE, false);

                try {
                    String json = HttpHelper.get(url);
                    // 注意:目前服务器返回的JSON数据串中会有特殊字符（换行、回车）。需要处理一下
                    json = json.replaceAll("\\x0a|\\x0d", "");
                    json = json.replaceAll("\\\\t", "");
                    json = json.replaceAll("\\\\n", "");
                    json = json.replaceAll("\\\\r", "");
                    json = json.replaceAll("<br\\\\/>", "");
                    JSONObject obj = new JSONObject(json);
                    if (null != obj && obj.has(_CODE)) {
                        msg.what = Integer.valueOf(obj.getString(_CODE));

                        if (obj.has(_DATAS)) {
                            // JSONArray array = obj.getJSONArray(_DATAS);
                            msg.obj = obj.getJSONObject(_DATAS).toString();
                        }
                        if (obj.has(_HASMORE)) {
                            msg.getData().putBoolean(_HASMORE, obj.getBoolean(_HASMORE));
                        }
                        if (obj.has(_COUNT)) {
                            msg.getData().putLong(_COUNT, obj.getLong(_COUNT));
                        }

                        if (obj.has(_RESULT)) {
                            msg.getData().putString(_RESULT, obj.getString(_RESULT));
                        }
                    }
                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                }

                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 异步GET请求封装
     *
     * @param url
     * @param callback
     */
    public static void asyncGet3(final String url, final Callback callback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                data.setJson((String) msg.obj);
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));

                callback.dataLoaded(data);
            }
        };
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean(_HASMORE, false);

                try {
                    String json = HttpHelper.get(url);
                    // 注意:目前服务器返回的JSON数据串中会有特殊字符（换行、回车）。需要处理一下
                    json = json.replaceAll("\\x0a|\\x0d", "");

                    JSONObject obj = new JSONObject(json);
                    if (null != obj && obj.has(_CODE)) {
                        msg.what = Integer.valueOf(obj.getString(_CODE));

                        if (obj.has(_DATAS)) {
                            // JSONArray array = obj.getJSONArray(_DATAS);
                            msg.obj = obj.getString(_DATAS).toString();
                        }
                        if (obj.has(_HASMORE)) {
                            msg.getData().putBoolean(_HASMORE, obj.getBoolean(_HASMORE));
                        }
                        if (obj.has(_COUNT)) {
                            msg.getData().putLong(_COUNT, obj.getLong(_COUNT));
                        }

                        if (obj.has(_RESULT)) {
                            msg.getData().putString(_RESULT, obj.getString(_RESULT));
                        }
                    }
                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                }

                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 异步GET请求封装
     *
     * @param url
     * @param callback
     */
    public static void asyncCountGet(final String url, final Callback callback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setJson((String) msg.obj);
                callback.dataLoaded(data);
            }
        };
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                Log.d(TAG, url);
                try {
                    String json = HttpHelper.get(url);

                    // 注意:目前服务器返回的JSON数据串中会有特殊字符（换行、回车）。需要处理一下
                    json = json.replaceAll("\\x0a|\\x0d", "");
                    msg.obj = json;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 异步get分页数据请求封装
     *
     * @param url
     * @param pagesize
     * @param pageno
     * @param callback
     */
    public static void asyncGet(final String url, final int pagesize, final int pageno, final Callback callback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                data.setJson((String) msg.obj);
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));

                callback.dataLoaded(data);
            }
        };
        threadPool.execute(new Runnable() {
            @Override
            public void run() {

                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean(_HASMORE, false);

                String realUrl = "";// url + "&" + Constants.PARAM_PAGESIZE +
                // "=" + pagesize
                // + "&" + Constants.PARAM_PAGENO + "=" + pageno;

                try {
                    Thread.sleep(1000);

                    String json = HttpHelper.get(realUrl);

                    // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
                    json = json.replaceAll("\\x0a|\\x0d", "");

                    JSONObject obj = new JSONObject(json);
                    if (null != obj && obj.has(_CODE)) {
                        msg.what = Integer.valueOf(obj.getString(_CODE));

                        if (obj.has(_DATAS)) {
                            JSONArray array = obj.getJSONArray(_DATAS);
                            msg.obj = array.toString();

                            if (pagesize == array.length()) {
                                msg.getData().putBoolean(_HASMORE, true);
                            }
                        }
                        if (obj.has(_COUNT)) {
                            msg.getData().putLong(_COUNT, Long.valueOf(obj.getString(_COUNT)));
                        }

                        if (obj.has(_RESULT)) {
                            msg.getData().putString(_RESULT, obj.getString(_RESULT));
                        }
                    }
                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                }

                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 同步GET请求封装
     *
     * @param url
     * @return
     */
    public static ResponseData get(final String url) {
        ResponseData rd = new ResponseData();

        try {
            String json = HttpHelper.get(url);

            // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
            json = json.replaceAll("\\x0a|\\x0d", "");

            JSONObject obj = new JSONObject(json);
            if (null != obj && obj.has(_CODE)) {
                rd.setCode(obj.getInt(_CODE));

                if (obj.has(_DATAS)) {
                    JSONArray array = obj.getJSONArray(_DATAS);
                    rd.setJson(array.toString());
                }
                if (obj.has(_HASMORE)) {
                    rd.setHasMore(obj.getBoolean(_HASMORE));
                }

                if (obj.has(_RESULT)) {
                    rd.setResult(obj.getString(_RESULT));
                }

                if (obj.has(_COUNT)) {
                    rd.setCount(obj.getLong(_COUNT));
                }
            }
        } catch (IOException e) {
            rd.setCode(HttpStatus.SC_REQUEST_TIMEOUT);
            e.printStackTrace();
        } catch (JSONException e) {
            rd.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            rd.setCode(HttpStatus.SC_SERVICE_UNAVAILABLE);
            e.printStackTrace();
        }

        return rd;
    }

    /**
     * 同步GET请求封装
     *
     * @param url
     * @param pagesize
     * @param pageno
     * @return
     */
    public static ResponseData get(final String url, final int pagesize, final int pageno) {
        ResponseData rd = new ResponseData();

        String realUrl = "";// url + "&" + Constants.PARAM_PAGESIZE + "=" +
        // pagesize
        // + "&" + Constants.PARAM_PAGENO + "=" + pageno;

        try {
            String json = HttpHelper.get(realUrl);

            // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
            json = json.replaceAll("\\x0a|\\x0d", "");

            JSONObject obj = new JSONObject(json);
            if (null != obj && obj.has(_CODE)) {
                rd.setCode(obj.getInt(_CODE));

                if (obj.has(_DATAS)) {
                    JSONArray array = obj.getJSONArray(_DATAS);
                    rd.setJson(array.toString());

                    if (pagesize == array.length()) {
                        rd.setHasMore(true);
                    }
                }
                if (obj.has(_HASMORE)) {
                    rd.setHasMore(obj.getBoolean(_HASMORE));
                }

                if (obj.has(_RESULT)) {
                    rd.setResult(obj.getString(_RESULT));
                }

                if (obj.has(_COUNT)) {
                    rd.setCount(obj.getLong(_COUNT));
                }
            }
        } catch (IOException e) {
            rd.setCode(HttpStatus.SC_REQUEST_TIMEOUT);
            e.printStackTrace();
        } catch (JSONException e) {
            rd.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            rd.setCode(HttpStatus.SC_SERVICE_UNAVAILABLE);
            e.printStackTrace();
        }

        return rd;
    }

    public static ResponseData post(final String url, final HashMap<String, String> params) {
        ResponseData rd = new ResponseData();
        try {
            String json = HttpHelper.post(url, params);

            // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
            json = json.replaceAll("\\x0a|\\x0d", "");

            JSONObject obj = new JSONObject(json);
            if (null != obj && obj.has(_CODE)) {
                rd.setCode(obj.getInt(_CODE));

                if (obj.has(_DATAS)) {
                    JSONArray array = obj.getJSONArray(_DATAS);
                    rd.setJson(array.toString());
                }
                if (obj.has(_HASMORE)) {
                    rd.setHasMore(obj.getBoolean(_HASMORE));
                }

                if (obj.has(_RESULT)) {
                    rd.setResult(obj.getString(_RESULT));
                }

                if (obj.has(_COUNT)) {
                    rd.setCount(obj.getLong(_COUNT));
                }
            }
        } catch (IOException e) {
            rd.setCode(HttpStatus.SC_REQUEST_TIMEOUT);
            e.printStackTrace();
        } catch (JSONException e) {
            rd.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            rd.setCode(HttpStatus.SC_SERVICE_UNAVAILABLE);
            e.printStackTrace();
        }

        return rd;
    }

    public static ResponseData LonginPost(final String url, final HashMap<String, String> params) {
        ResponseData rd = new ResponseData();
        try {
            String json = HttpHelper.post(url, params);
            // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
            if (json != null && !json.equals("")) {
                json = json.replaceAll("\\x0a|\\x0d", "");
                JSONObject obj = new JSONObject(json);
                if (null != obj && obj.has(_CODE)) {
                    rd.setCode(obj.getInt(_CODE));

                    if (obj.has(_DATAS)) {
                        rd.setJson(obj.getString(_DATAS));
                    }
                    if (obj.has(_HASMORE)) {
                        rd.setHasMore(obj.getBoolean(_HASMORE));
                    }

                    if (obj.has(_RESULT)) {
                        rd.setResult(obj.getString(_RESULT));
                    }

                    if (obj.has(_COUNT)) {
                        rd.setCount(obj.getLong(_COUNT));
                    }
                }
            } else {
                rd.setCode(HttpStatus.SC_ACCEPTED);
            }
        } catch (IOException e) {
            rd.setCode(HttpStatus.SC_REQUEST_TIMEOUT);
            e.printStackTrace();
        } catch (JSONException e) {
            rd.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            rd.setCode(HttpStatus.SC_SERVICE_UNAVAILABLE);
            e.printStackTrace();
        }

        return rd;
    }

    /**
     * 异步的POST请求登录
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void asyncLoginPost(final String url, final HashMap<String, String> params, final Callback callback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                data.setJson((String) msg.obj);
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));

                callback.dataLoaded(data);
            }
        };
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean("hasMore", false);
                try {
                    String json = HttpHelper.post(url, params);
                    if (json != null && !json.equals("")) {
                        // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
                        json = json.replaceAll("\\x0a|\\x0d", "");
                        JSONObject obj = new JSONObject(json);
                        if (null != obj && obj.has(_CODE)) {
                            msg.what = Integer.valueOf(obj.getString(_CODE));
                            msg.obj = json;
                            // if(obj.has(_DATAS)){
                            // //JSONObject array = obj.getJSONObject(_DATAS);
                            // msg.obj = obj.getString(_DATAS).toString();
                            // }
                            if (obj.has(_RESULT)) {
                                msg.getData().putString(_RESULT, obj.getString(_RESULT));
                            }
                        }
                    } else {
                        msg.what = HttpStatus.SC_ACCEPTED;
                    }
                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                }

                handler.sendMessage(msg);
            }
        });
    }

    public static String getJsonFromCache(final String url,
                                          final HashMap<String, String> params,
                                          final Context context) {
        String pkey = "";
        if (params != null) {
            for (String key : params.keySet()) {
                pkey += key + "=" + params.get(key) + "&";
            }
        }
        final String ukey = url + "?" + pkey;
        String json = ConfigCacheUtil.getUrlCache(ukey, ConfigCacheUtil.ConfigCacheModel.CONFIG_CACHE_MODEL_ML);
        return json;
    }


    /**
     * 这个是有网络的时候优先从网络获取，并且缓存
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param context  上下文对象
     * @param cache    是否缓存 true or false
     * @param callback 回调
     * @Description 异步的POST请求
     * @Create On 2015-12-9上午9:49:07
     * @Site http://www.handongkeji.com
     * @author chaiqs
     * @Copyrights 2015-12-9 handongkeji All rights reserved.
     */
    public static void asyncPost(final String url, final HashMap<String, String> params, final Context context, final boolean cache, final Callback callback) {
        String pkey = "";
        if (params != null) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (!TextUtils.isEmpty(value)) {
                    value = value.replace("[\ue000-\uefff]", "");
                    //替换掉表情
                    params.put(key, value);
                }

                pkey += key + "=" + value + "&";

            }
        }
        final String ukey = url + "?" + pkey;
        Log.i(TAG, "asyncPost: " + ukey);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                // 如果是选择缓存，则从缓存中读取json数据并返回 TODO
                if (cache) {

                    String json = ConfigCacheUtil.getUrlCache(ukey, ConfigCacheUtil.ConfigCacheModel.CONFIG_CACHE_MODEL_ML);
                    if (!"".equals(json) && json != null) {
                        data.setJson(json);
                    } else {
                        data.setJson((String) msg.obj);
                    }

//                    Log.i(TAG, "从缓存获取" + data.getJson());

                } else {
                    data.setJson((String) msg.obj);
                }
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));
                if (callback != null) {
                    callback.dataLoaded(data);
                }
            }
        };


        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean("hasMore", false);
                try {
                    boolean isNetFlag = NetUtils.isNet(context);
                    if (!isNetFlag && cache) {
                        String json = ConfigCacheUtil.getUrlCache(ukey, ConfigCacheUtil.ConfigCacheModel.CONFIG_CACHE_MODEL_LONG);

                        if (json != null) {
                            JSONObject object = new JSONObject(json);
                            int status = object.getInt("status");

                            msg.what = status;

                            String message = object.getString("message");
                            msg.obj = json;

                            if (609 == status) {//账户冻结
                                Intent intent = new Intent(STATE_ACCOUNT_FREEZE);
                                intent.putExtra("json", json);
                                context.sendBroadcast(intent);
                                msg.obj = null;
                            }
                            if (602 == status) {

                                Intent otherlogin = new Intent(STATE_REMOTE_LOGIN);
                                otherlogin.putExtra("json", json);
                                Log.i("MainActivity", "RemoteHandler:602 ");
                                context.sendBroadcast(otherlogin);
                                msg.obj = null;

                            }

                        }

                    } else {
                        String json = HttpHelper.post(url, params);


                        // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
                        if (json != null) {
                            json = json.replaceAll("\\x0a|\\x0d", "");

                            JSONObject object = new JSONObject(json);
                            int status = object.getInt("status");
                            String message = object.getString("message");

                            msg.what = status;
                            msg.obj = json;

                            if (1 == status) {
                                // 在此处添加缓存
                                ConfigCacheUtil.setUrlCache(ukey, json);
                            }

                            if (602 == status) {
                                JSONObject dat = object.getJSONObject("data");
                                String currentip = dat.getString("currentip");//ip
                                String apptype = dat.getString("apptype");//1安卓2ios
                                if (apptype.equals("1")) {
                                    apptype = "安卓";
                                } else {
                                    apptype = "苹果";
                                }

                                JSONObject obj = new JSONObject();
                                obj.put("status", 602);
                                obj.put("message", message);
                                obj.put("ip", currentip);
                                obj.put("apptype", apptype);


                                Intent otherlogin = new Intent(STATE_REMOTE_LOGIN);
                                otherlogin.putExtra("json", obj.toString());
                                context.sendBroadcast(otherlogin);
                                msg.obj = null;
                            }

                            if (609 == object.getInt("status")) {
                                Intent intent = new Intent(STATE_ACCOUNT_FREEZE);
                                intent.putExtra("dongjie", 1);
                                context.sendBroadcast(intent);
                                msg.obj = null;
                            }

                            if (403 == status) {
                                Intent noexist = new Intent(STATE_ACCOUNT_NOT_EXIST);
                                noexist.putExtra("json", json);
                                context.sendBroadcast(noexist);
                                msg.obj = null;
                            }

                        }
                    }


                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(msg);

            }
        });
    }

    /**
     * syncPostReques,must excute in work thread
     *
     * @param context
     * @param url
     * @param params
     * @param callback
     * @author xuchaunting
     */
    public static void syncPost(Context context, String url, HashMap<String, String> params, JsonCallback callback) {
        try {
            String json = HttpHelper.post(url, params);
            if (TextUtils.isEmpty(json) || "null".equals(json)) {
                callback.onFailed(new Throwable(Constants.CONNECT_SERVER_FAILED));
                return;
            }
            JSONObject object = new JSONObject(json);

            int status = object.getInt(Constants.status);
            String message = object.getString(Constants.message);

            if (602 == status) {
                json = null;
                JSONObject dat = object.getJSONObject("data");
                String currentip = dat.getString("currentip");//ip
                String apptype = dat.getString("apptype");//1安卓2ios
                if (apptype.equals("1")) {
                    apptype = "安卓";
                } else {
                    apptype = "苹果";
                }
                JSONObject obj = new JSONObject();
                obj.put("status", 602);
                obj.put("message", message);
                obj.put("ip", currentip);
                obj.put("apptype", apptype);
                Intent otherlogin = new Intent(STATE_REMOTE_LOGIN);
                otherlogin.putExtra("json", obj.toString());
                context.sendBroadcast(otherlogin);
            }

            if (609 == object.getInt("status")) {
                json = null;

                Intent intent = new Intent(STATE_ACCOUNT_FREEZE);
                intent.putExtra("dongjie", 1);
                context.sendBroadcast(intent);
            }

            if (403 == status) {
                json = null;
                Intent noexist = new Intent(STATE_ACCOUNT_NOT_EXIST);
                noexist.putExtra("json", json);
                context.sendBroadcast(noexist);
            }

            callback.onSuccess(json);

        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailed(e);
        }
    }

    /**
     * 获取该url对应缓存文件名称
     *
     * @param url
     * @param params
     * @return
     */
    public static String getCacheFileName(final String url,
                                          final HashMap<String,
                                                  String> params) {
        String pkey = "";
        if (params != null) {
            for (String key : params.keySet()) {
                pkey += key + "=" + params.get(key) + "&";
            }
        }
        final String ukey = url + "?" + pkey;
        return ukey;
    }

    /**
     * 默认缓存，可以设置是否优先从缓存获取
     *
     * @param url
     * @param params
     * @param context
     * @param cacheFirst
     * @param callback
     */
    public static void asyncPostCacheFirst(final String url,
                                           final HashMap<String,
                                                   String> params,
                                           final Context context,
                                           final boolean cacheFirst,//是否优先从缓存获取
                                           final Callback callback) {
        //获取该url对应缓存文件名称
        final String ukey = getCacheFileName(url, params);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                data.setJson((String) msg.obj);
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));
                if (callback != null) {
                    callback.dataLoaded(data);
                }
            }
        };

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean("hasMore", false);
                String json = null;
                try {
                    //若优先从缓存获取
                    if (cacheFirst) {
                        json = ConfigCacheUtil.getUrlCache(ukey, ConfigCacheUtil.ConfigCacheModel.CONFIG_CACHE_MODEL_LONG);
                    }
                    if (json == null) {
                        boolean hasNet = NetUtils.isNet(context);
                        if (!hasNet) {
                            json = ConfigCacheUtil.getUrlCache(ukey, ConfigCacheUtil.ConfigCacheModel.CONFIG_CACHE_MODEL_LONG);
                            Log.i(TAG, "从缓存取: " + json);
                        } else {
                            json = HttpHelper.post(url, params);
                            // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
                            if (json != null) {
                                json = json.replaceAll("\\x0a|\\x0d", "");

                                //只缓存正常数据
                                // 在此处添加缓存

                            }
                        }
                        JSONObject jsonObject = new JSONObject(json);
                        int status = jsonObject.getInt("status");
                        if (status == 1) {
                            ConfigCacheUtil.setUrlCache(ukey, json);
                            Log.i(TAG, "asyncPostCacheFirst: 缓存" + json);
                        }
                        if (status == 609) {
                            Intent intent = new Intent("dongjie");
                            intent.putExtra("dongjie", 1);
                            context.sendBroadcast(intent);

                        }


                    }
                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                msg.obj = json;

                handler.sendMessage(msg);

            }
        });
    }


    /**
     * @param url      请求地址
     * @param params   请求参数
     * @param context  上下文对象
     * @param cache    是否缓存 true or false
     * @param callback 回调
     * @Description 异步的POST带Token请求
     * @Create On 2015-12-9上午9:49:07
     * @Site http://www.handongkeji.com
     * @author wmm
     * @Copyrights 2015-12-9 handongkeji All rights reserved.
     */
    public static void asyncTokenPost(final String url, final Context context, final boolean cache, final HashMap<String, String> params, final Callback callback) {
        String pkey = "";
        if (params != null) {
            for (String key : params.keySet()) {
                pkey += key + "=" + params.get(key) + "&";
            }
        }
        final String ukey = url + "?" + pkey;
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                // 如果是选择缓存，则从缓存中读取json数据并返回 TODO
                if (cache) {
                    String json = ConfigCacheUtil.getUrlCache(ukey, ConfigCacheUtil.ConfigCacheModel.CONFIG_CACHE_MODEL_SO_SHORT);
                    if (!"".equals(json) && json != null) {
                        data.setJson(json);
                    } else {
                        data.setJson((String) msg.obj);
                    }
                } else {
                    data.setJson((String) msg.obj);
                }
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));
                callback.dataLoaded(data);
            }
        };

        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean("hasMore", false);
                try {
                    boolean isNetFlag = NetUtils.isNet(context);
                    if (!isNetFlag && cache) {
                        String json = ConfigCacheUtil.getUrlCache(ukey, ConfigCacheUtil.ConfigCacheModel.CONFIG_CACHE_MODEL_SO_SHORT);
                        msg.obj = json;
                    } else {
                        String json = HttpHelper.post(url, params);
                        // 注意:目前服务器返回的JSON数据串中会有特殊字符（如换行）。需要处理一下
                        if (json != null) {
                            json = json.replaceAll("\\x0a|\\x0d", "");
                            JSONObject jso = new JSONObject(json);

                            msg.obj = json;


                            int status = jso.getInt("status");
                            String message = jso.getString("message");
                            if (602 == status) {
                                JSONObject dat = jso.getJSONObject("data");
                                String currentip = dat.getString("currentip");//ip
                                String apptype = dat.getString("apptype");//1安卓2ios
                                if (apptype.equals("1")) {
                                    apptype = "安卓";
                                } else {
                                    apptype = "苹果";
                                }

                                msg.obj = null;
                                Intent otherlogin = new Intent(STATE_REMOTE_LOGIN);
                                otherlogin.putExtra("json", dat.toString());
                                context.sendBroadcast(otherlogin);

                            } else if (609 == status) {
                                msg.obj = null;
                                Intent intent = new Intent(STATE_ACCOUNT_FREEZE);
                                intent.putExtra("json", json);
                                context.sendBroadcast(intent);

                            }
                            if (status == 1) {
                                // 在此处添加缓存
                                ConfigCacheUtil.setUrlCache(ukey, json);
                            }

                        }
                    }
                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = HttpStatus.SC_INTERNAL_SERVER_ERROR;
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                }
                handler.sendMessage(msg);
            }
        });

    }

    /**
     * 异步的多消息体POST请求封装
     *
     * @param url
     * @param params
     * @param fileMap
     * @param callback
     */
    public static void asyncMultipartPost(final String url, final HashMap<String, String> params, final HashMap<String, File> fileMap, final Callback callback) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                ResponseData data = new ResponseData();
                data.setCode(msg.what);
                data.setHasMore(msg.getData().getBoolean(_HASMORE));
                data.setJson((String) msg.obj);
                data.setResult(msg.getData().getString(_RESULT));
                data.setCount(msg.getData().getLong(_COUNT));

                callback.dataLoaded(data);
            }
        };
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage(HttpStatus.SC_OK);
                msg.getData().putBoolean("hasMore", false);

                try {
                    String json = HttpHelper.multipartPost(url, params, fileMap);
                    if (json != null) {
                        json = json.replaceAll("\\x0a|\\x0d", "");
                        msg.obj = json;

                        JSONObject object = new JSONObject(json);

                        int status = object.getInt("status");
                        if (609 == status) {
                            Intent intent = new Intent("dongjie");
                            intent.putExtra("dongjie", 1);
//                            MyApp.getInstance().sendBroadcast(intent);
                        }

                    }
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    msg.what = HttpStatus.SC_REQUEST_TIMEOUT;
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    msg.what = HttpStatus.SC_SERVICE_UNAVAILABLE;
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
