package cn.rxph.www.wq2017.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.bean.QyRecord;
import cn.rxph.www.wq2017.bean.RegisteInfo;
import cn.rxph.www.wq2017.registActivity.RegistWordActivity;
import cn.rxph.www.wq2017.utils.HttpUtil;
import cn.rxph.www.wq2017.utils.UrlUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QYFaileActivity extends BaseActivity {

    private TextView tvWhy;
    private RegisteInfo registInfoAgain;

    private List<QyRecord> qyInfolist = new ArrayList<>();
    private String applyId;
    private QyRecord qyInfo;
    private ImageView ivBack;
    private Button btnKnow, btnSubmitAgain;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qyfaile);
        tvWhy = (TextView) findViewById(R.id.tv_why);

        btnKnow = (Button) findViewById(R.id.btn_know);
        btnSubmitAgain = (Button) findViewById(R.id.btn_submit_again);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        setListener();
        Intent intent = getIntent();
        String why = intent.getStringExtra("why");
        applyId = intent.getStringExtra("apply_id");
        userId = intent.getStringExtra("userId");

        tvWhy.setText("【失败原因:】" + why);


    }

    private void setListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QYFaileActivity.this.finish();
            }
        });
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QYFaileActivity.this.finish();
            }
        });
        btnSubmitAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registInfoAgain = new RegisteInfo();
                showProgressDialog();
                requestData();
            }
        });

    }


    private void requestData() {
        RequestBody requestBody = new FormBody.Builder()
                .add("applyid", applyId)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlUtil.URL_cxtijiaohuoquyuaninfo, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(QYFaileActivity.this, "网络开小差了，请稍后再试！", Toast.LENGTH_SHORT).show();
                        closeProgressDialog();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    int status = jo.getInt("status");
                    if (status == 1) {
                        Toast.makeText(QYFaileActivity.this, "未查询到数据", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (status == -1) {
                        Toast.makeText(QYFaileActivity.this, "必传参数不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (status == -2) {
                        Toast.makeText(QYFaileActivity.this, "后台异常...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (status == -4) {
                        Toast.makeText(QYFaileActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String data = jo.getString("data");
                    parseJsonWithGson(data);
                    //把数据拿出来赋值给签约信息对象
                    dataGetRegistInfo();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(QYFaileActivity.this, RegistWordActivity.class);
                intent.putExtra("registeInfoAgain", registInfoAgain);
                intent.putExtra("userId", userId);
                startActivity(intent);


            }
        });


    }


    private void dataGetRegistInfo() {
        registInfoAgain.setApply_id(applyId);


        registInfoAgain.setUserName(qyInfo.getApply_oneself_Name());
        registInfoAgain.setUserSfz(qyInfo.getApply_oneself_IDCardNumber());
        registInfoAgain.setZllm(qyInfo.getApply_Cooperation_Company());

        registInfoAgain.setZllmId(qyInfo.getApply_Cooperation_id());


        registInfoAgain.setUserAddress(qyInfo.getApply_Family_Site());
        registInfoAgain.setUserTel(qyInfo.getApply_oneself_Phone());

        registInfoAgain.setTjrName(qyInfo.getApply_Referrer_Name());
        registInfoAgain.setTjrSfz(qyInfo.getApply_Referrer_IDcardNumber());
        registInfoAgain.setTjrTel(qyInfo.getApply_Referrer_Phone());

        registInfoAgain.setContactName(qyInfo.getApply_FamilyMemberNameOnt());
        registInfoAgain.setContactShgx(qyInfo.getApply_FamilyMemberSocialRelationsOnt());
        registInfoAgain.setContactTel(qyInfo.getApply_FamilyMemberPhoneOnt());

        registInfoAgain.setContactName1(qyInfo.getApply_FamilyMemberNameTwo());
        registInfoAgain.setContactShgx1(qyInfo.getApply_FamilyMemberSocialRelationsTwo());
        registInfoAgain.setContactTel1(qyInfo.getApply_FamilyMemberPhoneTwo());

        //身份证正面照片等等
        String sfzzmUrl = qyInfo.getApply_IdentityCardFrontPictureUrl();
        String sfzfmUrl = qyInfo.getApply_IdentityCardVersoPictureUrl();
        String scsfzUrl = qyInfo.getApply_IdentityCardPictureUrl();
        String hyUrl = qyInfo.getApply_TogetherPictureUrl();


        File sfzzmFile = downloadPic(sfzzmUrl);
        File sfzfmFile = downloadPic(sfzfmUrl);
        File scsfzFile = downloadPic(scsfzUrl);
        File hyFile = downloadPic(hyUrl);
        registInfoAgain.setSfzzm(sfzzmFile);
        registInfoAgain.setSfzfm(sfzfmFile);
        registInfoAgain.setScsfz(scsfzFile);
        registInfoAgain.setHy(hyFile);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();

            }
        });


    }

    private File downloadPic(String url) {
        File imageFile = null;
        try {
            FutureTarget<File> future = Glide
                    .with(QYFaileActivity.this)
                    .load(url)
                    .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);

            imageFile = future.get();
        } catch (Exception e) {

        }
        return imageFile;

    }


    //加载中方法
    private ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(QYFaileActivity.this);
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


    /**
     * 解析Json
     *
     * @param jsonData
     * @return
     */
    private void parseJsonWithGson(String jsonData) {
        Gson gson = new Gson();

        qyInfo = gson.fromJson(jsonData, QyRecord.class);

    }


}
