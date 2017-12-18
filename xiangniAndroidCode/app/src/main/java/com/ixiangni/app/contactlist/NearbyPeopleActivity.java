package com.ixiangni.app.contactlist;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.user.PersonPageActivity;
import com.ixiangni.baidumap.service.LocationService;
import com.ixiangni.bean.NearBean;
import com.ixiangni.bean.NearListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.util.GlideUtil;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NearbyPeopleActivity extends BaseActivity implements BDLocationListener, SmartPullableLayout.OnPullListener {

    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    private LocationService locationService;
    private String latitude;
    private String longitude;
    private NearAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_people);
        ButterKnife.bind(this);
        initView();
        initLocation();
        checkPermission();
    }

    private void initView() {
        mAdapter = new NearAdapter(this);
        listView.setAdapter(mAdapter);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getCount() == 0) {
                    stateLayout.showNoDataView("没有附近的人");
                } else {
                    stateLayout.showContenView();
                }
            }
        });
        smartPullLayout.setOnPullListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NearBean item = mAdapter.getItem(position);
                String userid = "" + item.getUserid();
                if (userid.equals(LoginHelper.getInstance().getUserid(NearbyPeopleActivity.this))) {
                    toast("自己");
                } else {
                    PersonPageActivity.start(NearbyPeopleActivity.this, userid);

                }
            }
        });
    }

    private void initLocation() {
        locationService = new LocationService(this);
        locationService.registerListener(this);
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
            requestPermission(requestPermissions);
        } else {
            startLocation();
        }
    }

    private void startLocation() {
        showProgressDialog("定位中...", true);
        locationService.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationService.stop();
    }

    private void requestPermission(List<String> requestPermissions) {
        ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), 100);
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation != null) {
            latitude = "" + bdLocation.getLatitude();
            longitude = "" + bdLocation.getLongitude();

            stateLayout.post(() -> {
                if (isFirst) {
                    toast("定位成功！");
                    getNearByPeople();
                    isFirst = false;
                }
            });
        }
    }

//    token	是	String	用户token
//    lng	是	String	经度
//    lat	是	String	纬度
//    currentPage	否	Int	当前页数
//    pageSize	否	Int	每页显示数

    private int currentPage = 1;
    private int pageSize = 30;


    private boolean isFirst = true;
    private boolean isFirst1 = true;

    private void getNearByPeople() {
        if (CommonUtils.isStringNull(latitude) || CommonUtils.isStringNull(longitude)) {
            smartPullLayout.stopPullBehavior();
            return;
        }

        if (isFirst1) {
            if (mProgressDialog != null) {
                mProgressDialog.setMessage("搜索中...");
            }
        }

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("lng", longitude);
        params.put("lat", latitude);
        params.put(XNConstants.pageSize, "" + pageSize);
        params.put(XNConstants.currentPage, "" + currentPage);

        log(params.toString());
        RemoteDataHandler.asyncPost(UrlString.url_nearby_people, params, this, true, response -> {
            if (stateLayout != null) {
                String json = response.getJson();
//                LogHelper.log(TAG, json);
                if (isFirst1) {
                    isFirst1 = false;
                    dismissProgressDialog();
                }
                smartPullLayout.stopPullBehavior();
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    NearListBean nearListBean = new Gson().fromJson(json, NearListBean.class);
                    if (1 == nearListBean.getStatus()) {
                        List<NearBean> data = nearListBean.getData();
                        if (1 == currentPage) {
                            mAdapter.replaceAll(data);
                        } else {
                            mAdapter.addAll(data);
                        }

                    } else {
                        toast(nearListBean.getMessage());
                    }

                }

            }

        });
    }

    @Override
    public void onPullDown() {

        currentPage = 1;
        getNearByPeople();

    }

    @Override
    public void onPullUp() {
        currentPage++;
        getNearByPeople();

    }

    private class NearAdapter extends QuickAdapter<NearBean> {

        public NearAdapter(Context context) {
            super(context, R.layout.item_nearby);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, NearBean nearBean) {
            ImageView iv = helper.getView(R.id.iv_icon);
            String userpic = nearBean.getUserpic();
            if (CommonUtils.isStringNull(userpic)) {
                iv.setImageResource(R.mipmap.touxiangmoren);
            } else {

                GlideUtil.loadRoundImage(context, userpic, iv, R.mipmap.touxiangmoren);
            }
            helper.setText(R.id.tv_nick_name, nearBean.getUsernick());
            String jl = nearBean.getJuli();
            if (Integer.parseInt(jl) <= 100) {
                helper.setText(R.id.tv_distance, "100米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 200) {
                helper.setText(R.id.tv_distance, "200米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 300) {
                helper.setText(R.id.tv_distance, "300米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 400) {
                helper.setText(R.id.tv_distance, "400米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 500) {
                helper.setText(R.id.tv_distance, "500米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 600) {
                helper.setText(R.id.tv_distance, "600米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 700) {
                helper.setText(R.id.tv_distance, "700米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 800) {
                helper.setText(R.id.tv_distance, "800米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 900) {
                helper.setText(R.id.tv_distance, "900米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 1000) {
                helper.setText(R.id.tv_distance, "1000米以内");
                return;
            }
            if (Integer.parseInt(jl) <= 2000) {
                helper.setText(R.id.tv_distance, "2公里以内");
                return;
            }
            if (Integer.parseInt(jl) <= 3000) {
                helper.setText(R.id.tv_distance, "3公里以内");
                return;
            }
            if (Integer.parseInt(jl) <= 4000) {
                helper.setText(R.id.tv_distance, "4公里以内");
                return;
            }
            if (Integer.parseInt(jl) <= 5000) {
                helper.setText(R.id.tv_distance, "5公里以内");
                return;
            }
            if (Integer.parseInt(jl) <= 10000) {
                helper.setText(R.id.tv_distance, "10公里以内");
                return;
            }
            if (Integer.parseInt(jl) <= 20000) {
                helper.setText(R.id.tv_distance, "20公里以内");
                return;
            }
            if (Integer.parseInt(jl) <= 30000) {
                helper.setText(R.id.tv_distance, "30公里以内");
                return;
            }
            helper.setText(R.id.tv_distance, "30公里以外" + "(" + jl + "米)");

//            String juli = "距离"+nearBean.getJuli()+"米";
//            helper.setText(R.id.tv_distance, juli);
        }
    }

    private static final String TAG = "NearbyPeopleActivity";

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    toast("定位失败，请打开定位权限！");
                    return;
                }
            }
            startLocation();
        }

    }
}
