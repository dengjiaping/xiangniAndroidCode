package com.ixiangni.app.money;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.upload.UpLoadPresenter;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.Icondialog;
import com.ixiangni.presenters.contract.MyPresenter;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.nevermore.oceans.utils.ListUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我要充值
 *
 * @ClassName:IWantChargeActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/22 0022   10:48
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class IWantChargeActivity extends BaseActivity implements Icondialog.OnClickDialogListener {

    @Bind(R.id.iv_image)
    ImageView ivImage;
    @Bind(R.id.edt_money)
    EditText edtMoney;
    @Bind(R.id.btn_charge)
    Button btnCharge;
    private Icondialog icondialog;
    private final int REQUEST_CODE_SELECT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iwant_charge);
        ButterKnife.bind(this);
        icondialog = new Icondialog(this);
        icondialog.setClickListener(this);
        CommonUtils.setPoint(edtMoney, 2);
    }


    @OnClick({R.id.iv_image, R.id.btn_charge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_image:
                icondialog.show();
                break;
            case R.id.btn_charge:

                if (TextUtils.isEmpty(imgUrl)) {
                    toast("请上传充值凭证");
                    return;
                }
                String money = edtMoney.getText().toString().trim();

                double m = Double.parseDouble(money);
                if (m <= 0) {
                    toast("充值金额必须大于0");
                    return;
                }

                charge(imgUrl, money);

                break;
        }
    }

//    token	是	String	用户token
//    refillapplypic	是	String	图片
//    refillapplyprice	是	Double	金额

    private void charge(String imgUrl, String money) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("refillapplypic", imgUrl);
        params.put("refillapplyprice", money);
        showProgressDialog("充值中", false);
        MyPresenter.request(this, UrlString.URL_CHARGE, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                toast("充值成功");
                dismissProgressDialog();
                onBackPressed();
            }

            @Override
            public void onFailed(String errorMsg) {

                toast(errorMsg);
                dismissProgressDialog();
            }
        });


    }

    @Override
    public void onLocalClick() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, false); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    public void onTakepictureClick() {
        Intent intent = new Intent(this, ImageGridActivity.class);
        intent.putExtra(ImageGridActivity.EXTRAS_TAKE_PICKERS, true); // 是否是直接打开相机
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    public void onLookpictureClick() {

    }

    private static final String TAG = "IWantChargeActivity";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (!ListUtil.isEmptyList(images)) {
                    String path = images.get(0).path;
                    Log.i(TAG, "onActivityResult: " + path);

                    Glide.with(this).load(path).into(ivImage);
                    File file = new File(path);
                    log("filesize" + file.length());

                    upLoadImage(file);

                }
            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String imgUrl;

    private void upLoadImage(File file) {

        UpLoadPresenter.upLoadImage(UrlString.URL_UPLOAD_IMAGE, file, "1", new OnResult<String>() {


            @Override
            public void onSuccess(String s) {

                if (btnCharge != null) {

//                    toast("上传成功");
//                    if (!s.contains("http://")) {
//                        s = "http://www." + s;
//                    }
                    log(s);
                    imgUrl = s;


                }
            }

            @Override
            public void onFailed(String errorMsg) {

                toast(errorMsg);
            }
        });
    }
}
