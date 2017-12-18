package com.ixiangni.app.mine;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.JiNengBean;
import com.ixiangni.constants.UrlString;
import com.mydemo.yuanxin.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JiNengRenZhengActivity extends BaseActivity {

    @Bind(R.id.iv_water)
    ImageView ivWater;
    @Bind(R.id.iv_rice)
    ImageView ivRice;
    @Bind(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @Bind(R.id.fl_icon)
    LinearLayout flIcon;
    @Bind(R.id.bt_lighten_water)
    Button btLightenWater;
    @Bind(R.id.bt_lighten_rice)
    Button btLightenRice;
    @Bind(R.id.ll_water)
    LinearLayout llWater;
    @Bind(R.id.ll_rice)
    LinearLayout llRice;
    private String userId;
    //    private String userpic2;
    private int riceRelevanceId;
    private int waterRelevanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ji_neng_ren_zheng);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        Log.w("zcq", "myid: " + userId);
//        userpic2 = intent.getStringExtra("userpic2");
//        showTouXiang();
        showJiNeng();
    }

    private void showJiNeng() {
        RequestBody requsetBody = new FormBody.Builder()
                .add("relevanceUserId", userId)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlString.URL_QUERY_JINENG, requsetBody, new Callback() {


            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast("网络开小差了");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    String jinenglist = jo.getString("data");
                    int status = jo.getInt("status");
                    if (status == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                llWater.setVisibility(View.INVISIBLE);
                                toast("您暂无技能可点亮");
                            }
                        });

                        return;
                    }
                    Gson gson = new Gson();
                    List<JiNengBean> jineng = gson.fromJson(jinenglist, new TypeToken<List<JiNengBean>>() {
                    }.getType());
                    JiNengBean water = jineng.get(0);
//                    JiNengBean rice = jineng.get(0);
                    waterRelevanceId = water.getRelevanceId();
//                    riceRelevanceId = rice.getRelevanceId();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            llWater.setVisibility(View.VISIBLE);

                            //水出现
                            llWater.setVisibility(View.VISIBLE);
                            if (water.getRelevanceSign() == 1) {
                                btLightenWater.setText("已点亮");
                                btLightenWater.setEnabled(false);
                            } else {
                                btLightenWater.setText("点亮");
                                btLightenWater.setEnabled(true);
                            }

//                            if (rice.getRelevanceSign() == 1) {
//                                btLightenRice.setText("已点亮");
//                                btLightenRice.setEnabled(false);
//
//                            } else {
//                                btLightenRice.setEnabled(true);
//                                btLightenRice.setText("点亮");
//                            }
                            Glide.with(JiNengRenZhengActivity.this).load(water.getProductPicture()).into(ivWater);
//                            Glide.with(JiNengRenZhengActivity.this).load(rice.getProductPicture()).into(ivRice);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }

//    private void showTouXiang() {
//        loadRoundImage(userpic2, ivUserIcon, R.mipmap.touxiangmoren);
//        UserProfileManager userProfileManager = DemoHelper.getInstance().getUserProfileManager();
//        if (!CommonUtils.isStringNull(userpic2)) {
//            userProfileManager.setCurrentUserAvatar(userpic2);
//        }
//    }

    @OnClick({R.id.bt_lighten_water, R.id.bt_lighten_rice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_lighten_water:
                lighten(waterRelevanceId + "", 2);

                break;
            case R.id.bt_lighten_rice:
                toast("该商品暂无法点亮");

                //点亮大米
//                lighten(riceRelevanceId + "", 1);
                break;
        }
    }

    private void lighten(String relevanceId, int what) {
        RequestBody requsetBody = new FormBody.Builder()
                .add("relevanceId", relevanceId)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlString.URL_JINENG_LIGHTEN, requsetBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast("网络开小差了");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                JSONObject jo = null;
                try {
                    jo = new JSONObject(body);
                    String pic = jo.getString("data");
//                    Log.w("zcq", "喝水图2017.11.3"+pic );

                    int status = jo.getInt("status");
                    if (status == 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toast("参数错误");
                            }
                        });
                        return;
                    }
                    if (what == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toast("点亮成功");
                                Glide.with(JiNengRenZhengActivity.this).load(pic).into(ivWater);
                                btLightenWater.setEnabled(false);
                                btLightenWater.setText("已点亮");
                            }
                        });

                        return;
                    }
                    Toast.makeText(mContext, "未知错误", Toast.LENGTH_SHORT).show();
//                    if (what == 1) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                toast("点亮成功");
//                                Glide.with(JiNengRenZhengActivity.this).load(pic).into(ivRice);
//                                btLightenRice.setEnabled(false);
//                                btLightenRice.setText("已点亮");
//                            }
//                        });
//
//                        return;
//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

    }
}
