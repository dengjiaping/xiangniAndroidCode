package com.ixiangni.app.publish;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.baidumap.service.LocationService;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BaiduMapActivity extends BaseActivity {

    @Bind(R.id.btn_send)
    Button btnSend;
    @Bind(R.id.baidu_mapview)
    MapView mMapView;
    @Bind(R.id.list_view)
    ListView listView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private double mLatitude;
    private boolean isFirstIn = true;
    private double mLongtitude;
    private GeoCoder mSearch;
    private LocationClient mLocClient;
    private LocationService mLocationService;
    private boolean firstLocation = true;
    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BMapManager.init();
        setContentView(R.layout.activity_baidu_map);
        ButterKnife.bind(this);

        initView();
        checkPermission();
        btnSend.setOnClickListener(v -> {
            PoiInfo selectPoiInfo = locationAdapter.getSelectPoiInfo();
            if (selectPoiInfo == null || TextUtils.isEmpty(selectPoiInfo.address)) {
                toast("请选择地址");
            } else {
                Intent intent = new Intent();
                intent.putExtra("poiInfo", selectPoiInfo);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    public static final String POIINFO = "poiInfo";

    private void initView() {
        locationAdapter = new LocationAdapter(this);
        listView.setAdapter(locationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                locationAdapter.select(position);
            }
        });

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.setMapStatus(msu);
        mLocationService = new LocationService(this);
        mLocationService.registerListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {

                if (bdLocation != null && mMapView != null) {


                    if (firstLocation) {
                        firstLocation = false;
                        double latitude = bdLocation.getLatitude();
                        double longitude = bdLocation.getLongitude();
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(bdLocation.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(100).latitude(bdLocation.getLatitude())
                                .longitude(bdLocation.getLongitude()).build();
                        mBaiduMap.setMyLocationData(locData);
                        updataMap(latitude, longitude);

                    }

                }
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });


        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

                if (reverseGeoCodeResult != null) {
                    List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
                    locationAdapter.replaceAll(poiList);
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {


            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                updataMap(latLng.latitude, latLng.longitude);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private class LocationAdapter extends QuickAdapter<PoiInfo> {

        private int selectposition = 0;

        public void select(int position) {
            selectposition = position;
            notifyDataSetChanged();
        }

        public PoiInfo getSelectPoiInfo() {
            if (data.size() <= 0) {
                return null;
            }

            return data.get(selectposition);
        }

        @Override
        public void replaceAll(List<PoiInfo> elem) {
            selectposition = 0;
            if (!ListUtil.isEmptyList(elem)) {
                super.replaceAll(elem);
            }
        }

        public LocationAdapter(Context context) {
            super(context, R.layout.item_location);
        }

        @Override
        protected void convert(BaseAdapterHelper helper, PoiInfo poiInfo) {

            helper.setText(R.id.tv_location, CommonUtils.isStringNull(poiInfo.address.trim())?poiInfo.name:poiInfo.address);
            View view = helper.getView(R.id.iv_select);
            view.setVisibility(helper.getPosition() == selectposition ? View.VISIBLE : View.GONE);
        }
    }

    //更新位置
    private void updataMap(double latitude, double longitude) {
        mBaiduMap.clear();

        //添加覆盖物
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.weizhi);
        LatLng latLng = new LatLng(latitude, longitude);
        OverlayOptions overlay = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        mBaiduMap.addOverlay(overlay);

        //移动地图到指定经纬度
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(mapStatusUpdate);

        //搜索周边
        ReverseGeoCodeOption reverseGeoCodeOption = new ReverseGeoCodeOption();
        reverseGeoCodeOption.location(latLng);
        mSearch.reverseGeoCode(reverseGeoCodeOption);

    }


    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private void startService() {
        // 开启定位
        mBaiduMap.setMyLocationEnabled(true);
        mLocationService.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationService.stop();
    }

    @Override
    protected void onDestroy() {
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        mMapView = null;
        BMapManager.init();
        super.onDestroy();

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
            startService();
        }
    }

    private void requestPermission(List<String> requestPermissions) {
        ActivityCompat.requestPermissions(this, requestPermissions.toArray(new String[requestPermissions.size()]), 101);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    toast("定位权限被拒绝，定位失败！");
                    return;
                }
            }
            startService();
        }
    }


}
