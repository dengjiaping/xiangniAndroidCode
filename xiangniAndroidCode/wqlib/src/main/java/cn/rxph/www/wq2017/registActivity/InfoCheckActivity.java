package cn.rxph.www.wq2017.registActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.base.BaseActivity;
import cn.rxph.www.wq2017.bean.RegisteInfo;
import cn.rxph.www.wq2017.payDialog.BottomDialogOnclickListener;
import cn.rxph.www.wq2017.payDialog.BottomDialogView;
import cn.rxph.www.wq2017.payDialog.DialogUtil;
import cn.rxph.www.wq2017.utils.FileUtils;
import cn.rxph.www.wq2017.utils.HttpUtil;
import cn.rxph.www.wq2017.utils.PictureUtil;
import cn.rxph.www.wq2017.utils.SignView;
import cn.rxph.www.wq2017.utils.UrlUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class InfoCheckActivity extends BaseActivity {


//    private static final String[] PERMISSION_EXTERNAL_STORAGE = new String[]{
//            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//    private static final int REQUEST_EXTERNAL_STORAGE = 100;

    private ImageView ivScsfz, ivSfzzm, ivSfzfm, ivHy;
    private TextView tvUserName, tvUserSfz, tvUserHome, tvUserCompany, tvUserTel, tvContactName,
            tvContactTel, tvShehuiguanxi, tvContactName1, tvContactTel1, tvShehuiguanxi1, tvTime,
            tvTjrSfz, tvTjrName, tvTjrTel;

    private LinearLayout llContact1;

    private Button btnReset, btnSubmit, btnSave;
    private ImageView back;
    private FrameLayout autographFrameLayout;
    private RegisteInfo registeInfo;
    private SignView autograph;
    private LinearLayout ll;


    private Bitmap mSignBitmap;
    private String signPath;
    private String fileName;
    private String userId;
    private WebView mWebView;

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_check);


        initView();
        //动态sd权限申请
//        verifyStoragePermissions(this);
        //签名实例对象
        //autograph = new LinePathView(this);
        autograph = new SignView(this);
        autographFrameLayout.addView(autograph);
        autograph.requestFocus();
        setListener();
        try {
            mWebView = (WebView) findViewById(R.id.wv_tiaokaung);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.setWebViewClient(new WebViewClient());
            mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
            mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
            mWebView.loadUrl(UrlUtil.CLAUSE_URL());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //把所有注册信息显示出来
        registeInfo = (RegisteInfo) getIntent().getSerializableExtra("registInfo");
        long time = System.currentTimeMillis();
        String qyTime = getDateTimeFromMillisecond(time);
        registeInfo.setAgreementTime(qyTime);
        showInfo();

    }

    private void showInfo() {


        userId = registeInfo.getQianyuerenId();
        tvUserName.setText(registeInfo.getUserName());
        tvUserSfz.setText(registeInfo.getUserSfz());
        tvUserHome.setText(registeInfo.getUserAddress());
        tvUserCompany.setText(registeInfo.getZllm());
        tvUserTel.setText(registeInfo.getUserTel());

        tvContactName.setText(registeInfo.getContactName());
        tvContactTel.setText(registeInfo.getContactTel());
        tvShehuiguanxi.setText(registeInfo.getContactShgx());

        tvContactName1.setText(registeInfo.getContactName1());
        tvContactTel1.setText(registeInfo.getContactTel1());
        tvShehuiguanxi1.setText(registeInfo.getContactShgx1());

        tvTjrSfz.setText(registeInfo.getTjrSfz());
        tvTjrName.setText(registeInfo.getTjrName());
        tvTjrTel.setText(registeInfo.getTjrTel());

        tvTime.setText("合同日期：" + registeInfo.getAgreementTime());


        if (TextUtils.isEmpty(registeInfo.getContactName1()) && TextUtils.isEmpty(registeInfo.getContactShgx1()) && TextUtils.isEmpty(registeInfo.getContactTel1())) {
            llContact1.setVisibility(View.GONE);
        } else {
            llContact1.setVisibility(View.VISIBLE);

        }


//        ivSfzzm.setImageBitmap(BitmapFactory.decodeFile(registeInfo.getSfzzm().getPath()));
//        ivSfzfm.setImageBitmap(BitmapFactory.decodeFile(registeInfo.getSfzfm().getPath()));
//        ivScsfz.setImageBitmap(BitmapFactory.decodeFile(registeInfo.getScsfz().getPath()));
//        ivHy.setImageBitmap(BitmapFactory.decodeFile(registeInfo.getHy().getPath()));
        Glide.with(this).load(registeInfo.getSfzzm()).into(ivSfzzm);
        Glide.with(this).load(registeInfo.getSfzfm()).into(ivSfzfm);
        Glide.with(this).load(registeInfo.getScsfz()).into(ivScsfz);
        Glide.with(this).load(registeInfo.getHy()).into(ivHy);


    }

    private void setListener() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autograph.clear();

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll.setDrawingCacheEnabled(true);

                saveSign(ll.getDrawingCache());
                ll.setDrawingCacheEnabled(false);

                File fQM = new File(signPath);
                registeInfo.setQm(fQM);
                if (autograph.getTouched()) {

                    if (registeInfo.getAgain() == 1) {
                        try {
                            tijaoData(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //提交时提示弹窗
//                        notice();
                        try {
                            tijaoData(0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Toast.makeText(InfoCheckActivity.this, "您没有签名~", Toast.LENGTH_SHORT).show();
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoCheckActivity.this.finish();
            }
        });


    }

    private void initView() {

        autographFrameLayout = (FrameLayout) findViewById(R.id.fl_qmview);
        btnReset = (Button) findViewById(R.id.btn_reset);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        back = (ImageView) findViewById(R.id.iv_back);
        ivSfzzm = (ImageView) findViewById(R.id.iv_sfzzm);
        ivSfzfm = (ImageView) findViewById(R.id.iv_sfzfm);
        ivScsfz = (ImageView) findViewById(R.id.iv_scsfz);
        ivHy = (ImageView) findViewById(R.id.iv_hy);
        ll = (LinearLayout) findViewById(R.id.ll);

        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserSfz = (TextView) findViewById(R.id.tv_user_sfz);
        tvUserHome = (TextView) findViewById(R.id.tv_user_home);
        tvUserCompany = (TextView) findViewById(R.id.tv_user_company);
        tvUserTel = (TextView) findViewById(R.id.tv_user_tel);
        tvContactName = (TextView) findViewById(R.id.tv_contact_name);
        tvContactTel = (TextView) findViewById(R.id.tv_contact_tel);
        tvShehuiguanxi = (TextView) findViewById(R.id.tv_shehuiguanxi);
        tvContactName1 = (TextView) findViewById(R.id.tv_contact_name1);
        tvContactTel1 = (TextView) findViewById(R.id.tv_contact_tel1);
        tvShehuiguanxi1 = (TextView) findViewById(R.id.tv_shehuiguanxi1);
        tvTime = (TextView) findViewById(R.id.tv_time);
        llContact1 = (LinearLayout) findViewById(R.id.ll_contact1);
        tvTjrSfz = (TextView) findViewById(R.id.tv_tjr_sfz);
        tvTjrName = (TextView) findViewById(R.id.tv_tjr_name);
        tvTjrTel = (TextView) findViewById(R.id.tv_tjr_tel);


    }


    private void showDialog() {
        DialogUtil.showBottomDialog(InfoCheckActivity.this, R.id.check_ll, new BottomDialogOnclickListener() {
            @Override
            public void onPositiveClick(String contentStr, final BottomDialogView dialogView) {
                //contentStr是用户输入的密码
//                InfoCheckActivity.this.finish();
//                startActivity(new Intent(InfoCheckActivity.this, ResultActivity.class));
                //压缩图片
                //yasuoPic();

                //校验支付密码
                showProgressDialog();
                final RequestBody requestBody = new FormBody.Builder()
                        .add("token", registeInfo.getQianyuerentoken())
                        .add("paymentpass", contentStr)
                        .build();
                HttpUtil.sendOkHttpPostRequest(UrlUtil.URL_CHEK_PAYPASWORD, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                closeProgressDialog();
                                Toast.makeText(InfoCheckActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String body = response.body().string();
                        try {
                            JSONObject jo = new JSONObject(body);
                            String content = jo.getString("content");
                            String data = jo.getString("data");
                            String message = jo.getString("message");
                            String status = jo.getString("status");
                            String type = jo.getString("type");

                            //1成功  0失败
                            if ("1".equals(status)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogView.dismiss();
                                        closeProgressDialog();

                                        Toast.makeText(InfoCheckActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                tijaoData(0);
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogView.dismiss();
                                        closeProgressDialog();

                                        Toast.makeText(InfoCheckActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

//                if ("...".equals(contentStr)) {
//                    dialogView.dismiss();
//
//                    tijaoData(0);
//
//                } else {
//                    Toast.makeText(InfoCheckActivity.this, "由于系统升级，暂无法支付", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void yasuoPic() {
        //Bitmap b = PictureUtil.getSmallBitmap(registeInfo.getSfzzm().getPath(), 1280, 720);
        File fzm = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(registeInfo.getSfzzm().getPath(), 1280, 720), "ys_sfzzm_image");
        registeInfo.setSfzzm(fzm);
        File ffm = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(registeInfo.getSfzfm().getPath(), 1280, 720), "ys_sfzfm_image");
        registeInfo.setSfzzm(ffm);
        File fsc = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(registeInfo.getScsfz().getPath(), 1280, 720), "ys_scsfz_image");
        registeInfo.setSfzzm(fsc);
        File fhy = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(registeInfo.getHy().getPath(), 1280, 720), "ys_hy_image");
        registeInfo.setSfzzm(fhy);
    }

    private void tijaoData(int again) {
        showProgressDialog();

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM)
                .addFormDataPart("strSignAContractId", userId)
                .addFormDataPart("strApply_id", registeInfo.getApply_id())
                .addFormDataPart("strMemberName", registeInfo.getUserName())
                .addFormDataPart("strCardNumber", registeInfo.getUserSfz())
                .addFormDataPart("strCooperationCompany", registeInfo.getZllm())
                .addFormDataPart("strFamilySite", registeInfo.getUserAddress())
                .addFormDataPart("strMemberPhone", registeInfo.getUserTel())
                .addFormDataPart("strReferrerIDcardNumber", registeInfo.getTjrSfz())
                .addFormDataPart("strReferrerName", registeInfo.getTjrName())
                .addFormDataPart("strReferrerPhone", registeInfo.getTjrTel())
                .addFormDataPart("strFamilyMemberNameOnt", registeInfo.getContactName())
                .addFormDataPart("strFamilyMemberPhoneOnt", registeInfo.getContactTel())
                .addFormDataPart("strFamilyMemberSocialRelationsOnt", registeInfo.getContactShgx())
                .addFormDataPart("strFamilyMemberNameTwo", registeInfo.getContactName1())
                .addFormDataPart("strFamilyMemberPhoneTwo", registeInfo.getContactTel1())
                .addFormDataPart("strFamilyMemberSocialRelationsTwo", registeInfo.getContactShgx1())
                .addFormDataPart("apply_contractDate", registeInfo.getAgreementTime())
                .addFormDataPart("strapply_Cooperation_id", registeInfo.getZllmId())

                .addFormDataPart("strIdentityCardFrontPictureUrl", registeInfo.getSfzzm().getName(), RequestBody.create(MEDIA_TYPE_PNG, registeInfo.getSfzzm()))
                .addFormDataPart("strIdentityCardVersoPictureUrl", registeInfo.getSfzfm().getName(), RequestBody.create(MEDIA_TYPE_PNG, registeInfo.getSfzfm()))
                .addFormDataPart("strIdentityCardPictureUrl", registeInfo.getScsfz().getName(), RequestBody.create(MEDIA_TYPE_PNG, registeInfo.getScsfz()))
                .addFormDataPart("strTogetherPictureUrl", registeInfo.getHy().getName(), RequestBody.create(MEDIA_TYPE_PNG, registeInfo.getHy()))
                .addFormDataPart("strSignaturePictureUrl", registeInfo.getQm().getName(), RequestBody.create(MEDIA_TYPE_PNG, registeInfo.getQm()));


//                .addFormDataPart("strIdentityCardFrontPictureUrl", registeInfo.getSfzzm().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), registeInfo.getSfzzm()))//第一个参数是key
//                .addFormDataPart("strIdentityCardVersoPictureUrl", registeInfo.getSfzfm().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), registeInfo.getSfzfm()))
//                .addFormDataPart("strIdentityCardPictureUrl", registeInfo.getScsfz().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), registeInfo.getScsfz()))
//                .addFormDataPart("strTogetherPictureUrl", registeInfo.getHy().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), registeInfo.getHy()))
//                .addFormDataPart("strSignaturePictureUrl", registeInfo.getQm().getName(), RequestBody.create(MediaType.parse("multipart/form-data"), registeInfo.getQm()));


        RequestBody requestBody = multipartBodyBuilder.build();

        //根据是否是第一次提交来用不同的提交接口
        String url = null;
        if (again == 1) {
            url = UrlUtil.URL_AGAIN_SHANGCHUAN;

        } else {
            url = UrlUtil.URL_SHANGCHUAN;
//            url = "http://192.168.0.106:8080/RxphMember/RxphMemberApply?method=applySign";
        }

        HttpUtil.sendOkHttpPostRequest(url, requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(InfoCheckActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                closeProgressDialog();

                String result = response.body().string();
                try {
                    JSONObject jo = new JSONObject(result);
                    final int status = jo.getInt("status");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status == 0) {
                                Intent intent = new Intent(InfoCheckActivity.this, ResultActivity.class);
                                intent.putExtra("userId", userId);
                                startActivity(intent);
                                return;
                            }
                            if (status == -1) {
                                Toast.makeText(InfoCheckActivity.this, "必传参数不能为空", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (status == -2) {
                                Toast.makeText(InfoCheckActivity.this, "后台异常", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (status == -3) {
                                Toast.makeText(InfoCheckActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (status == 5) {
                                Toast.makeText(InfoCheckActivity.this, "身份已注册", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Toast.makeText(InfoCheckActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    /**
     * signPath是图片保存路径
     *
     * @param bit
     */
    public void saveSign(Bitmap bit) {
        fileName = "qm";
        mSignBitmap = bit;
        signPath = createFile();
    }

    /**
     * @return
     */
    private String createFile() {
        ByteArrayOutputStream baos = null;
        String _path = null;
        try {
            String sign_dir = Environment.getExternalStorageDirectory()
                    .getPath() + "/" + "xiangniQM" + "/";

            File dir = new File(sign_dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            _path = sign_dir + fileName + ".jpg";
            baos = new ByteArrayOutputStream();
            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] photoBytes = baos.toByteArray();
            if (photoBytes != null) {
                new FileOutputStream(new File(_path)).write(photoBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return _path;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mSignBitmap != null) {
            mSignBitmap.recycle();
        }
        destroyWebView();
    }

    /**
     * 将毫秒转化成固定格式的时间
     * 时间格式: yyyy-MM-dd HH:mm:ss
     *
     * @param millisecond
     * @return
     */
    public static String getDateTimeFromMillisecond(Long millisecond) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(millisecond);
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }


    //加载中方法
    private ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(InfoCheckActivity.this);
                progressDialog.setMessage("正在提交...");
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

//    private void verifyStoragePermissions(Activity activity) {
//        int permissionWrite = ActivityCompat.checkSelfPermission(activity,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        if (permissionWrite != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity, PERMISSION_EXTERNAL_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE);
//        }
//    }

    /**
     * 签约费用弹窗提醒
     */
    private void notice() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("丙方加入平台分享经济联盟成员费用1800元，此费用为平台创新创富定项培训费用(一次从“想你钱包”扣除不予退还)。");
        builder.setCancelable(true);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showDialog();


            }
        });
        builder.show();
    }


    private void destroyWebView() {
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            mWebView.freeMemory();
            mWebView.pauseTimers();
            mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }

}