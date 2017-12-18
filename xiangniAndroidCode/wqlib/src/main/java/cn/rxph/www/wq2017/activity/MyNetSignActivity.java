package cn.rxph.www.wq2017.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.adapter.QyAdapter;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.bean.QyRecord;
import cn.rxph.www.wq2017.utils.HttpUtil;
import cn.rxph.www.wq2017.utils.UrlUtil;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MyNetSignActivity extends BaseActivity {

    /**
     * Called when the activity is first created.
     */
    private List<String> list = new ArrayList<String>();
    private Spinner qySpinner;
    private TextView tvQyRecord;
    private ArrayAdapter<String> adapter;
    private ImageView back;
    private ListView lvQyRecord;
    private List<QyRecord> qyRecords = new ArrayList<>();
    private QyAdapter qyAdapter;
    private SwipeRefreshLayout refresh;

    //用户选择查询的类型
    private int type;
    private RequestBody requestBody = new FormBody.Builder().build();
    private String userId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_net_sign);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        spinnerQY();

        back = (ImageView) findViewById(R.id.iv_back);
        refresh = (SwipeRefreshLayout) findViewById(R.id.rf_refresh);

        lvQyRecord = (ListView) findViewById(R.id.lv_qy_record);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyNetSignActivity.this.finish();
            }
        });
        //添加adapter
        qyRecord();


        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RecordsData(type);
            }
        });

        //获取签约记录
        try {
            RecordsData(type);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void RecordsData(int type) {
        showProgressDialog();

        switch (type) {
            case 0:
                requestBody = new FormBody.Builder()
                        .add("apply_SignAContractId", userId)
                        .build();
                break;
            case 1:
                requestBody = new FormBody.Builder()
                        .add("apply_SignAContractId", userId)
                        .add("apply_AuditStatus", "1")
                        .build();
                break;
            case 2:
                requestBody = new FormBody.Builder()
                        .add("apply_SignAContractId", userId)
                        .add("apply_AuditStatus", "2")
                        .build();
                break;
            case 3:
                requestBody = new FormBody.Builder()
                        .add("apply_SignAContractId", userId)
                        .add("apply_AuditStatus", "3")
                        .build();
                break;


        }

        HttpUtil.sendOkHttpPostRequest(UrlUtil.URL_QY_RECORD, requestBody, new Callback() {


            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();

                        refresh.setRefreshing(false);
                        Toast.makeText(MyNetSignActivity.this, "网络开小差了，请稍后再试！", Toast.LENGTH_SHORT).show();

                    }
                });
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {


                closeProgressDialog();
                String text = response.body().string();
                try {
                    final JSONObject jsonText = new JSONObject(text);
                    final int status = jsonText.getInt("status");



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (status == 1) {
                                qyRecords.clear();
                                closeProgressDialog();
                                refresh.setRefreshing(false);
                                qyAdapter.notifyDataSetChanged();
                                Toast.makeText(MyNetSignActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (status == -1) {
                                closeProgressDialog();
                                refresh.setRefreshing(false);
                                qyAdapter.notifyDataSetChanged();
                                Toast.makeText(MyNetSignActivity.this, "必传参数不能为空", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (status == -2) {
                                closeProgressDialog();
                                refresh.setRefreshing(false);
                                qyAdapter.notifyDataSetChanged();
                                Toast.makeText(MyNetSignActivity.this, "后台异常", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (status == -4) {
                                closeProgressDialog();
                                refresh.setRefreshing(false);
                                qyAdapter.notifyDataSetChanged();
                                Toast.makeText(MyNetSignActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String data = null;
                            try {
                                data = jsonText.getString("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            parseJsonWithGson(data);
                            closeProgressDialog();
                            refresh.setRefreshing(false);
                            qyAdapter.notifyDataSetChanged();

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        });
    }

    private void qyRecord() {
        qyAdapter = new QyAdapter(MyNetSignActivity.this, R.layout.qy_record_lv_item, qyRecords);
        lvQyRecord.setAdapter(qyAdapter);


        lvQyRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QyRecord qr = qyRecords.get(position);
                String result = qr.getApply_AuditStatus();
                switch (Integer.parseInt(result)) {
                    case 1:
                        Toast.makeText(MyNetSignActivity.this, "请耐心等待，审核时间在1到3天", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MyNetSignActivity.this, "恭喜你，签约已成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Intent intent = new Intent(MyNetSignActivity.this, QYFaileActivity.class);
                        intent.putExtra("name", qr.getApply_oneself_Name());
                        intent.putExtra("why", qr.getApply_content());
                        intent.putExtra("apply_id", qr.getApply_id());
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void spinnerQY() {
        //第一步：添加一个下拉列表项的list，这里添加的项就是下拉列表的菜单项
        list.add("全部签约");
        list.add("审核中..");
        list.add("签约成功");
        list.add("签约失败");

        qySpinner = (Spinner) findViewById(R.id.spinner_qy);
        tvQyRecord = (TextView) findViewById(R.id.tv_qy_record);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<String>(this, R.layout.adapter_mytopactionbar_spinner, list);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        qySpinner.setAdapter(adapter);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        qySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                /* 将所选mySpinner 的值带入myTextView 中*/
//                tvQyRecord.setText(adapter.getItem(arg2));
                /* 将mySpinner 显示*/

//                Log.w("zcq", "onItemSelected: " + arg2);
                type = arg2;
                RecordsData(type);


                arg0.setVisibility(View.VISIBLE);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
//                tvQyRecord.setText("NONE");
                arg0.setVisibility(View.VISIBLE);
            }
        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
        qySpinner.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                /**
                 *
                 */
                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        qySpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

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
        List<QyRecord> qyr;
        qyr = gson.fromJson(jsonData, new TypeToken<List<QyRecord>>() {
        }.getType());
        qyRecords.clear();
        qyRecords.addAll(qyr);

    }

    //加载中方法
    private ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(MyNetSignActivity.this);
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
