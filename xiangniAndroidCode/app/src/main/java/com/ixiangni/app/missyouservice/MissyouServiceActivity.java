package com.ixiangni.app.missyouservice;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.baidumap.service.LocationService;
import com.ixiangni.common.XNConstants;
import com.ixiangni.dialog.MyAlertdialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 想你服务
 *
 * @ClassName:MissyouServiceActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/6/21 0021   11:27
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class MissyouServiceActivity extends BaseActivity implements BDLocationListener {

    @Bind(R.id.ll_hotel)
    LinearLayout llHotel;
    @Bind(R.id.ll_airplane)
    LinearLayout llAirplane;
    @Bind(R.id.ll_train)
    LinearLayout llTrain;
    private LocationService service;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missyou_service);
        ButterKnife.bind(this);

        sharedPreferences = getSharedPreferences(LoginHelper.SYS_INFO, MODE_PRIVATE);
        String cityName = sharedPreferences.getString(XNConstants.CITY_NAME, null);
//        若之前没定位过则定位
        if (TextUtils.isEmpty(cityName)) {
            init();
            checkPermission();
        }
    }

    private void init() {
        service = new LocationService(this);
        service.registerListener(this);
    }

    private void checkPermission() {
        List<String> requestPermissions = new ArrayList<>();
        int i0 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int i1 = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (i0 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (i1 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (requestPermissions.size() > 0) {
            ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), 100);
        } else {
            startLocation();
        }
    }

    private void startLocation() {
        showProgressDialog("定位中...", false);
        service.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    toast("为了更方便使用，请打开定位权限");
                    return;
                }
            }
            startLocation();

        }

    }


    @OnClick({R.id.ll_hotel, R.id.ll_airplane, R.id.ll_train,R.id.ll_tel_cz,R.id.ll_game,R.id.ll_live})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_hotel:
                startActivity(HotelListActivity.class);
                break;
            case R.id.ll_airplane:
//                Toast.makeText(mContext, "该应用暂未开放", Toast.LENGTH_SHORT).show();
                startActivity(AirplaneTicketActivity.class);
                break;
            case R.id.ll_train:
                startActivity(TrainTicketActivity.class);
                break;
            case R.id.ll_tel_cz:
                Toast.makeText(mContext, "该应用暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_game:
                Toast.makeText(mContext, "该应用暂未开放", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_live:
                Toast.makeText(mContext, "该应用暂未开放", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onReceiveLocation(final BDLocation bdLocation) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bdLocation != null) {
                    dismissProgressDialog();
                    Address address = bdLocation.getAddress();
                    log(bdLocation.getCity());
                    log(address.city);
                    log(address.address);
                    log(address.province);
                    log(address.country);
                    if (!TextUtils.isEmpty(address.city)) {
                        service.stop();
                        String cityName = address.city.replace("市", "");
                        sharedPreferences.edit().putString(XNConstants.CITY_NAME, cityName).commit();
                        toast("定位成功:当前城市" + cityName);
                    }else {
                        service.stop();
                        toast("定位失败，请检查是否打开GPS及相关权限");
                    }
                } else {
                    service.stop();
                    dismissProgressDialog();
                    toast("定位失败，请检查是否打开GPS及相关权限");
                }
            }
        });
    }

    private void showSettingDialog() {
        MyAlertdialog alertdialog = new MyAlertdialog(this);
        alertdialog.setTitle("定位失败");

    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
