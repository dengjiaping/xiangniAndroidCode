package com.mydemo.yuanxin.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mydemo.yuanxin.R;
import com.mydemo.yuanxin.base.BaseActivity;
import com.mydemo.yuanxin.util.HttpUtil;
import com.mydemo.yuanxin.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 修改密码界面
 */

public class ModifyPWActivity extends BaseActivity {

    private ImageView back;
    private Button submit;
    private EditText oldPW;
    private EditText newPW;
    private EditText checkPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pw);
        //初始化控件
        initView();

        //加点击监听
        setListener();
    }

    private void setListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNewPWToNet();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyPWActivity.this.finish();
            }
        });

    }

    private void initView() {
        back = (ImageView) findViewById(R.id.iv_back);
        submit = (Button) findViewById(R.id.btn_submit);

        oldPW = (EditText) findViewById(R.id.et_old_pw);
        newPW = (EditText) findViewById(R.id.et_new_pw);
        checkPW = (EditText) findViewById(R.id.et_check_new_pw);
    }

    private void sendNewPWToNet() {
        String oldKey = oldPW.getText().toString();
        String newKey = newPW.getText().toString();
        String checkKey = checkPW.getText().toString();
        if (TextUtils.isEmpty(oldKey) || TextUtils.isEmpty(newKey) || TextUtils.isEmpty(checkKey)) {
            Toast.makeText(this, "请输入完整！！", Toast.LENGTH_SHORT).show();
        } else {
            if (newKey.equals(checkKey)) {
                sendModifyRequset(oldKey, newKey);
                submit.setEnabled(false);

            } else {
                Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();

            }
        }


    }

    /**
     * 给服务器发请求
     */
    private void sendModifyRequset(String oldpw, String newpw) {
        Intent intent = getIntent();
        final String superId = intent.getStringExtra("superId");

        final RequestBody requsetBody = new FormBody.Builder()
                .add("userId", superId)
                .add("oldPassword", oldpw)
                .add("newPassword", newpw)
                .build();


        showProgressDialog();
        HttpUtil.sendOkHttpPostRequest(UrlUtil.Modify_URL(), requsetBody, new Callback() {
            public String result;

            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ModifyPWActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                closeProgressDialog();
                String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        submit.setEnabled(true);
                    }
                });
                try {
                    JSONObject jo = new JSONObject(responseText);
                    result = jo.getString("returns");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result.equals("true")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ModifyPWActivity.this);
                                    builder.setTitle("提示");
                                    builder.setMessage("密码修改成功，请您牢记新密码,点击确定将退出此页面^_^");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ModifyPWActivity.this.finish();

                                        }
                                    });
                                    builder.show();
                                    // Toast.makeText(ModifyPWActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });


                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ModifyPWActivity.this, "修改失败,很可能您输入的原密码错误", Toast.LENGTH_SHORT).show();

                        }
                    });


                }

            }


        });
    }

    //加载中方法
    private static ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(ModifyPWActivity.this);
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
