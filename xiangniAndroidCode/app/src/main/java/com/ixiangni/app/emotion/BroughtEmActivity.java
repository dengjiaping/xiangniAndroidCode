package com.ixiangni.app.emotion;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.emotionadapter.MyEmotionAdapter;
import com.ixiangni.bean.BuiedEmListBean;
import com.ixiangni.constants.UrlString;

import java.util.HashMap;
import java.util.List;

public class BroughtEmActivity extends AppCompatActivity {

    //  以网格的形式显示表情宝内容
    public RecyclerView recyclerView;
    //  表情包适配的模式
    private MyEmotionAdapter adapter;
    //   顶部显示的大图
    private ImageView BigImageView;
    //   顶部的标题
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brought_em);
        //      网格控件的初始化
        recyclerView = (RecyclerView) findViewById(R.id.xh_emotion_recycleview);
        //      顶部的大图的初始化
        BigImageView = (ImageView) findViewById(R.id.bigImage);
        //      标题栏控件的初始化
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //      将加载的标题栏替换原本的标题栏
        setSupportActionBar(toolbar);
        //      判断是否将标题栏设置成功
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //      网格显示的布局管理对象
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        //      设置recycleView的布局管理对象
        recyclerView.setLayoutManager(layoutManager);
        //      创建adapter对象设置表情的显示的方式
        adapter = new MyEmotionAdapter(BroughtEmActivity.this, R.layout.emotionbrowlist);
        //      获取表情的内容同时设置给标题栏
        getEmotionList(BigImageView, toolbar);
        recyclerView.setAdapter(adapter);
    }

    /***
     *
     * @param imageView  此界面显示的大图的控件
     * @param toolbar    顶部的title toolbar
     *
     *
     */
    //    获取全部表情的方法
    private void getEmotionList(ImageView imageView, Toolbar toolbar) {
        //         创建一个map集合
        HashMap<String, String> params = new HashMap<>();
        //        存储数据
        params.put("token", MyApp.getInstance().getUserTicket());
        //        获取已购买的表情包
        RemoteDataHandler.asyncPost(UrlString.URL_BROUGHT_EMOTION_LIST, params, BroughtEmActivity.this, true, response -> {

            //                获取表情的json数据
            String json = response.getJson();
            if (!CommonUtils.isStringNull(json)) {
                //                    解析json数据成对应的对象
                BuiedEmListBean buiedEmListBean = new Gson().fromJson(json, BuiedEmListBean.class);
                if (1 == buiedEmListBean.getStatus()) {
                    //                        获取的是表情包的信息
                    List<BuiedEmListBean.DataBean> emotionList = buiedEmListBean.getData();
                    for (int i = 0; i < emotionList.size(); i++) {
                        BuiedEmListBean.DataBean bean = emotionList.get(i);
                        List<BuiedEmListBean.DataBean.UserBrowListBean> userBrowListBeanlist = bean.getUserBrowList();
                        Glide.with(BroughtEmActivity.this)
                                .load(bean.getBrowbaginfo())
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(imageView);
                        toolbar.setTitle(bean.getBrowbagname());
                        adapter.replaceAll(userBrowListBeanlist);


                    }

                }
            }
        });


    }

    //    返回上一个activity的功能的实现
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
