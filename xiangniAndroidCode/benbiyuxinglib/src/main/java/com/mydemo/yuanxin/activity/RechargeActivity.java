package com.mydemo.yuanxin.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mydemo.yuanxin.R;
import com.mydemo.yuanxin.base.BaseActivity;
import com.mydemo.yuanxin.bean.BankInfo;
import com.mydemo.yuanxin.explainActivity.HelpActivity;
import com.mydemo.yuanxin.util.FileUtils;
import com.mydemo.yuanxin.util.HttpUtil;
import com.mydemo.yuanxin.util.MessageEvent;
import com.mydemo.yuanxin.util.PictureUtil;
import com.mydemo.yuanxin.util.UrlUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 充值页面
 */

public class RechargeActivity extends BaseActivity {
    private Button submit;
    private ImageView back, Instructions, scpz;
    private EditText money, czStyle;
    private Intent intentGetId;
    private String superId;
    private TextView tvKhh, tvYhkh, tvHm, tvKhdz;

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private Bitmap bitmap;
    private Uri imageUri;
    private File fYsPz;
    private BankInfo bankInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
        setListener();
        bankInfo = new BankInfo();
        //注册事件
        EventBus.getDefault().register(this);
        //获取用户唯一标识
        intentGetId = getIntent();
        superId = intentGetId.getStringExtra("superId");
        //获取银行等一系列信息
        requestBankInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(MessageEvent messageEvent) {
        BankInfo bi = messageEvent.getBankInfo();
        tvKhh.setText(bi.getCardName());
        tvYhkh.setText(bi.getCardNumber());
        tvHm.setText(bi.getCardUserName());
        tvKhdz.setText(bi.getCardAddress());
//        Log.w("zcq", "onMoonEvent:message "+messageEvent.getMessage() );
        if(!TextUtils.isEmpty(messageEvent.getMessage().trim())){
            Toast.makeText(this, messageEvent.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void requestBankInfo() {
        showProgressDialog();
        /**
         * 给服务器发参数
         */
        final RequestBody requsetBody = new FormBody.Builder()
                //                .add("userId", superId)
                .build();
        HttpUtil.sendOkHttpPostRequest(UrlUtil.BANK_INFO_URL(), requsetBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                closeProgressDialog();
                String body = response.body().string();
                try {
                    JSONObject jo = new JSONObject(body);
                    int status = jo.getInt("status");
                    String message = jo.getString("message");
//                    Log.w("zcq", "messagechu"+message );
                    //0   查询完成    1  暂无数据  2 后台异常
                    //"content": {
                    //                            "cardId": null,
                    //                            "cardStatus": null,
                    //                            "cardType": null,
                    //                            "cardCreationDate": null,
                    //                            "cardModifyDate": null,
                    //                            "cardNumber": "622255555555555444",
                    //                            "cardName": "中国华夏银行",
                    //                            "cardUserName": "张三",
                    //                            "cardAddress": "丰台区",
                    //                            "createUserId": null,
                    //                            "modifyUserId": null
                    //                }
                    if (status == 0) {
                        JSONObject content = jo.getJSONObject("content");
                        String cardNumber = content.getString("cardNumber");
                        bankInfo.setCardNumber(cardNumber);

                        String cardName = content.getString("cardName");
                        bankInfo.setCardName(cardName);
                        String cardUserName = content.getString("cardUserName");
                        bankInfo.setCardUserName(cardUserName);
                        String cardAddress = content.getString("cardAddress");
                        bankInfo.setCardAddress(cardAddress);
                        EventBus.getDefault().post(new MessageEvent(bankInfo));
                        return;
                    }
                    if (status == 1) {
                        EventBus.getDefault().post(new MessageEvent(message));
                        return;
                    }
                    if (status == 2) {
                        EventBus.getDefault().post(new MessageEvent(message));
                        return;
                    }
                    EventBus.getDefault().post(new MessageEvent(message));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.iv_back);
        scpz = (ImageView) findViewById(R.id.iv_scpz);
        submit = (Button) findViewById(R.id.btn_submit);
        Instructions = (ImageView) findViewById(R.id.iv_Instructions);

        tvKhh = (TextView) findViewById(R.id.tv_khh);
        tvYhkh = (TextView) findViewById(R.id.tv_yykh);
        tvHm = (TextView) findViewById(R.id.tv_hm);
        tvKhdz = (TextView) findViewById(R.id.tv_khdz);

        money = (EditText) findViewById(R.id.et_cz_money);
        czStyle = (EditText) findViewById(R.id.et_cz_style);

    }

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RechargeActivity.this.finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                sendWrite();
            }


        });

        Instructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeActivity.this, HelpActivity.class);
                intent.putExtra("type", 3);
                startActivity(intent);
            }
        });


        scpz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListAlertDialog(v);

            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "您无访问权限，原因可能是您拒绝了本软件获取访问相册的权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        scpz.setImageBitmap(bitmap);
                        File filePh = FileUtils.saveBitmap(bitmap, "pz_image");
                        //压缩后的图片
                        fYsPz = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(filePh.getPath(), 1280, 720), "ys_pz_image");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath); // 根据图片路径显示图片
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
            scpz.setImageBitmap(bitmap);

            //            File filePh = FileUtils.saveBitmap(bitmap, "xcpz_image");
            //压缩后的图片
            fYsPz = FileUtils.saveBitmap(PictureUtil.getSmallBitmap(imagePath, 1280, 720), "ys_pz_image");

        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendWrite() {
        String czMoney = money.getText().toString();

        if (TextUtils.isEmpty(czMoney)) {

            Toast.makeText(RechargeActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();

            return;
        }
        if (bitmap == null) {

            Toast.makeText(RechargeActivity.this, "请截图或拍摄转账凭证，并将凭证上传", Toast.LENGTH_SHORT).show();

            return;
        }

        if (Integer.parseInt(czMoney) < 1) {

            Toast.makeText(RechargeActivity.this, "您充值的金额小于1,无法进行充值", Toast.LENGTH_SHORT).show();

            return;
        }

        showProgressDialog();


        /**
         * 给服务器发参数
         */

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM)
                .addFormDataPart("userId", superId)
                .addFormDataPart("yinxinCoinNumber", czMoney)
                .addFormDataPart("rechargePictureUrl", fYsPz.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), fYsPz));//第一个参数是key


        RequestBody requestBody = multipartBodyBuilder.build();


        HttpUtil.sendOkHttpPostRequest(UrlUtil.CZ_URL(), requestBody, new Callback() {
            public String yOrN;

            @Override
            public void onFailure(Call call, IOException e) {
                closeProgressDialog();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RechargeActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                closeProgressDialog();
                String responseText = response.body().string();
                try {
                    JSONObject jo = new JSONObject(responseText);
                    yOrN = jo.getString("returns");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if ("true".equals(yOrN)) {
                    startActivity(new Intent(RechargeActivity.this, SubmitActivity.class));
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RechargeActivity.this, "未发送成功", Toast.LENGTH_SHORT).show();

                        }
                    });
                }

            }
        });


    }


    //加载中方法
    private ProgressDialog progressDialog;

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {

        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(RechargeActivity.this);
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

    /**
     * 信息提示框
     *
     * @param view
     */
    private AlertDialog alertDialog1;

    public void showListAlertDialog(View view) {
        final String[] items = {"拍摄", "相册"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("上传凭证");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                switch (index) {
                    case 0:
                        // 创建File对象，用于存储拍照后的图片
                        File outputImage = new File(getExternalCacheDir(), "output_image.jpeg");
                        try {
                            if (outputImage.exists()) {
                                outputImage.delete();
                            }
                            outputImage.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT < 24) {
                            imageUri = Uri.fromFile(outputImage);
                        } else {
                            imageUri = FileProvider.getUriForFile(RechargeActivity.this, "com.rongxin.www.yuanxin", outputImage);
                        }
                        // 启动相机程序
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, TAKE_PHOTO);

                        break;
                    case 1:
                        if (ContextCompat.checkSelfPermission(RechargeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(RechargeActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        } else {
                            openAlbum();
                        }
                        break;
                }
                //Toast.makeText(RechargeActivity.this, items[index], Toast.LENGTH_SHORT).show();
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }
}
