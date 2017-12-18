package com.ixiangni.app.missyouservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.DisplayUtil;
import com.handongkeji.utils.LogHelper;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.StarLayout;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.HotelBean;
import com.ixiangni.bean.HotelListBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.ui.HotelFilterView;
import com.ixiangni.util.LinearDecoration;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 预订酒店->酒店列表
 *
 * @ClassName:HotelListActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/21 0021   17:50
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/21 0021 handongkeji All rights reserved.
 */
public class HotelListActivity extends BaseActivity implements SmartPullableLayout.OnPullListener {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smartPullLayout;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.tv_current_city)
    TextView tvCurrentCity;
    @Bind(R.id.hotel_filter_view)
    HotelFilterView hotelFilterView;
    private String currentCity = "北京";

    private String hotelName = null;
    private HotelAdapter mAdapter;
    private final int REQUEST_CODE_CITY = 11;

    private String starType = null;
    private String priceType = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);
        ButterKnife.bind(this);
        //选择城市

        mAdapter = new HotelAdapter(this);

        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(HotelListActivity.this, HotelDetailActivity.class);
                HotelBean item = mAdapter.getItem(position);

                String id1 = item.getID();
                intent.putExtra(XNConstants.HOTEL_ID, id1);
                startActivity(intent);
            }
        });

        LinearLayoutManager ma = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(ma);
        recyclerView.addItemDecoration(new LinearDecoration(DisplayUtil.dip2px(this, 5), 0xfff3f3f4, LinearDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

//        stateLayout.setUpwihtRecyclerAdapter(mAdapter, "暂无酒店...");
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (mAdapter.getItemCount() == 0) {
                    stateLayout.showNoDataView("暂无酒店");
                } else {
                    stateLayout.showContenView();
                }
            }
        });

        smartPullLayout.setOnPullListener(this);

//        tvCurrentCity.setText(currentCity);
//        tvCurrentCity.setOnClickListener(v -> {
//            Intent intent = new Intent(HotelListActivity.this, CityList0Activity.class);
//            startActivityForResult(intent, REQUEST_CODE_CITY);
//        });
//
//        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//
//                    hotelName = edtSearch.getText().toString().trim();
//
//                    hideSoftKeyboard();
//                    isAutoRefresh = true;
//                    getHotelList();
//                    return true;
//                }
//                return false;
//            }
//        });

        hotelFilterView.setConditionSelected(new HotelFilterView.OnConditionSelected() {
            @Override
            public void onCondition(String star, String price) {
//                toast(star+"-"+price);
                starType=star;
                priceType=price;
                isAutoRefresh=true;

                onPullDown();
            }
        });

        tvCurrentCity.setOnClickListener(v -> {
            Intent intent = new Intent(HotelListActivity.this,CityList0Activity.class);
            startActivityForResult(intent,REQUEST_CODE_CITY);
        });

        SharedPreferences sharedPreferences = getSharedPreferences(LoginHelper.SYS_INFO, MODE_PRIVATE);
        String cityName = sharedPreferences.getString(XNConstants.CITY_NAME, null);
        if(!TextUtils.isEmpty(cityName)){
            currentCity= cityName;
            tvCurrentCity.setText(currentCity);
        }
        getHotelList();
    }


//    token	是	String	用户token
//    city	是	String	城市名称
//    pgindex	是	int	当前页
//    pgsize	是	int	每页记录数
//    hn	否	String	酒店名称
//        startype
//    pricetype


    private int pgindex = 1;
    private int pgsize = 20;

    private boolean isAutoRefresh = true;

    private void getHotelList() {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("city", currentCity);
        params.put("pgindex", "" + pgindex);
        params.put("pgsize", "" + pgsize);

        if(!TextUtils.isEmpty(starType)){

            params.put("startype",starType);
        }
        if(!TextUtils.isEmpty(priceType)){

            params.put("pricetype",priceType);
        }

        if (!CommonUtils.isStringNull(hotelName)) {
            params.put("hn", hotelName);
        }
        if (isAutoRefresh) {
            stateLayout.showLoadView("加载中...");
        }

        RemoteDataHandler.asyncPost(UrlString.URL_HOTEL_LIST, params, this, true, response -> {

            if (stateLayout != null) {
                String json = response.getJson();
                LogHelper.log(TAG, json);

                smartPullLayout.stopPullBehavior();
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    HotelListBean hotelListBean = new Gson().fromJson(json, HotelListBean.class);
                    if (1 == hotelListBean.getStatus()) {
                        HotelListBean.DataBean data = hotelListBean.getData();
                        if (1 == pgindex) {
                            mAdapter.replaceAll(data.getReqdata());
                            recyclerView.scrollToPosition(0);
                        } else {
                            mAdapter.addAll(data.getReqdata());
                        }
                        if (pgindex < data.getTotalpg()) {
                            smartPullLayout.setPullUpEnabled(true);
                        } else {
                            smartPullLayout.setPullUpEnabled(false);
                        }

                    } else {
                        toast(hotelListBean.getMessage());
                    }
                }
                if (isAutoRefresh) {
                    isAutoRefresh = false;
                }
            }


        });


    }

    private static final String TAG = "HotelListActivity";


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CITY && resultCode == RESULT_OK) {
            String cityName = data.getStringExtra(XNConstants.CITY_NAME);
            isAutoRefresh = true;
//            edtSearch.setText("");
            hotelName = null;
            currentCity = cityName;
            tvCurrentCity.setText(currentCity);
//            tvCurrentCity.setText(cityName);
            onPullDown();
        }
    }

    @Override
    public void onPullDown() {
        pgindex = 1;
        getHotelList();
    }

    @Override
    public void onPullUp() {
        pgindex++;
        getHotelList();
    }

    private class HotelAdapter extends MyRvAdapter<HotelBean> {


        public HotelAdapter(Context context) {
            super(context, R.layout.item_new_hotel);
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, HotelBean data) {
            ImageView ivHotel = holder.getView(R.id.iv_hotel_pic);
            Glide.with(context).load(data.getPicture()).into(ivHotel);
            //酒店名称
            holder.setText(R.id.tv_hotel_name, data.getName());

            String star = data.getStar();
            int count = 0;
            try {
                count = Integer.parseInt(star);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            StarLayout starLayout = holder.getView(R.id.star_layout);
            starLayout.setStarCount(count);

            holder.setText(R.id.tv_hotel_location, data.getAddress());
            holder.setText(R.id.tv_hotel_price, "¥"+data.getPrice());

        }


    }


}
