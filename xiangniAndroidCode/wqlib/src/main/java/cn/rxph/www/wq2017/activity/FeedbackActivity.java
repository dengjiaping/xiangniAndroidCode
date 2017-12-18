package cn.rxph.www.wq2017.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.utils.HttpUtil;
import cn.rxph.www.wq2017.utils.UrlUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class FeedbackActivity extends BaseActivity {
    private EditText etYourTel, etYourFeed;
    private ImageView ivBack;
    private Button btnSubmit;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
        initView();
        setListner();

    }

    private void setListner() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity.this.finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = etYourTel.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(FeedbackActivity.this, "请输入联系方式", Toast.LENGTH_SHORT).show();
                    return;
                }

                String content = etYourFeed.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(FeedbackActivity.this, "请填写反馈内容", Toast.LENGTH_SHORT).show();
                    return;
                }

                feedBack(phoneNum, content);
            }
        });
    }

    private void initView() {
        etYourTel = (EditText) findViewById(R.id.et_your_tel);
        etYourFeed = (EditText) findViewById(R.id.et_your_feed);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        btnSubmit = (Button) findViewById(R.id.btn_ques_submit);
    }

    /**
     * 反馈
     *
     * @param phoneNum
     * @param content
     */
    private void feedBack(String phoneNum, String content) {
        showProgressDialog();
        RequestBody requestBody = new FormBody.Builder()
                .add("token", token)
                .add("phone", phoneNum)
                .add("content", content)
                .build();


        HttpUtil.sendOkHttpPostRequest(UrlUtil.URL_FEEDBACK, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();

                        Toast.makeText(FeedbackActivity.this, "网络开小差了，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String text = response.body().string();

                String t = null;
                try {
                    JSONObject jsonText = new JSONObject(text);
                    t = jsonText.getString("message");
                    if ("操作成功!".equals(t)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(FeedbackActivity.this, "操作成功！", Toast.LENGTH_SHORT).show();
                                finish();
                                new Intent(FeedbackActivity.this, WQMainActivity.class);
                            }
                        });

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FeedbackActivity.this, "操作失败！", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //加载中方法
    private  ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(FeedbackActivity.this);
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
