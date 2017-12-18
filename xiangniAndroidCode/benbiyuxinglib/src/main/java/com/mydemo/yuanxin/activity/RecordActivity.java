package com.mydemo.yuanxin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mydemo.yuanxin.R;
import com.mydemo.yuanxin.adapter.MyAdapter;
import com.mydemo.yuanxin.base.BaseActivity;
import com.mydemo.yuanxin.bean.JYRecord;
import com.mydemo.yuanxin.util.HttpUtil;
import com.mydemo.yuanxin.util.UrlUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 交易记录界面
 */

public class RecordActivity extends BaseActivity {

    private List<JYRecord> records = new ArrayList<>();
    private ListView lvRecord;
    private MyAdapter adapter;
    private ImageView back;
    private String superId;
    private Intent intentUserId;
    private SwipeRefreshLayout refreshRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        //初始化控件
        initView();
        //从网络获取交易记录
        recordForNet();
        //加点击监听
        setListener();
        adapter = new MyAdapter(this, R.layout.record_list_item, records);
        lvRecord.setAdapter(adapter);


    }




    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.finish();
            }
        });
        refreshRecord.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recordForNet();
            }
        });

    }


    private void initView() {
        back = (ImageView) findViewById(R.id.iv_back);
        lvRecord = (ListView) findViewById(R.id.lv_record);
        refreshRecord= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_record);
        //swipeRefresh= (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

    }


    /**
     * 从网络获取数据
     */
    private void recordForNet() {
        showProgressDialog();
        intentUserId = getIntent();
        superId = intentUserId.getStringExtra("superId_record");

        final RequestBody requestBody = new FormBody.Builder()
                .add("userId", superId)
                .build();

        HttpUtil.sendOkHttpPostRequest(UrlUtil.JYRecord_URL(), requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //设置刷新结束
                        refreshRecord.setRefreshing(false);
                        Toast.makeText(RecordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                parseJsonWithGson(responseText);
                closeProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(records.size()==0){
                            Toast.makeText(RecordActivity.this, "你没有交易记录或交易记录获取失败", Toast.LENGTH_SHORT).show();
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                        //设置刷新结束
                        refreshRecord.setRefreshing(false);
                    }

                });
            }
        });

    }

    /**
     * 解析Json
     *
     * @param jsonData
     * @return
     */
    private void parseJsonWithGson(String jsonData) {
        Gson gson = new Gson();
        List<JYRecord> jyRecords;
        jyRecords = gson.fromJson(jsonData, new TypeToken<List<JYRecord>>() {
        }.getType());
        records.clear();
        records.addAll(jyRecords);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    //加载中方法
    private  ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(RecordActivity.this);
                progressDialog.setMessage("正在加载...");
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
