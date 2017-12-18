package com.ixiangni.app.missyouservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.loopviewpager.AutoLoopViewPager;
import com.github.loopviewpager.ImgPagerAdapter;
import com.github.viewpagerindicator.CirclePageIndicator;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.handongkeji.utils.LogHelper;
import com.handongkeji.widget.NoScrollListView;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.HotelDetailBean;
import com.ixiangni.bean.RoomBean;
import com.ixiangni.bean.RoomInfo;
import com.ixiangni.bean.RoomYuding;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.HotelPresenter;
import com.ixiangni.util.StateLayout;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.nevermore.oceans.utils.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 酒店详情
 *
 * @ClassName:HotelDetailActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/24 0024   13:10
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/24 0024 handongkeji All rights reserved.
 */
public class HotelDetailActivity extends BaseActivity {

    @Bind(R.id.tv_hotel_name)
    TextView tvHotelName;
    @Bind(R.id.tv_start_level)
    TextView tvStartLevel;
    @Bind(R.id.tv_lower_price)
    TextView tvLowerPrice;
    @Bind(R.id.tv_hotel_location)
    TextView tvHotelLocation;
    @Bind(R.id.tv_hotel_introduce)
    TextView tvHotelIntroduce;
    @Bind(R.id.iv_hotel_pic)
    ImageView ivHotelPic;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.view_pager)
    AutoLoopViewPager viewPager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    @Bind(R.id.list_view_room)
    NoScrollListView listViewRoom;
    @Bind(R.id.tv_no_room)
    TextView tvNoRoom;
    @Bind(R.id.scroll_view)
    ScrollView scrollView;
    private String hotelid;
    private String hotelname;
    private String picture;
    private HotelPresenter hp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);
        ButterKnife.bind(this);

        stateLayout.setPlayAlphaAnim(true);
        hotelid = getIntent().getStringExtra(XNConstants.HOTEL_ID);

        hp = new HotelPresenter();
        getHotelInfo();
    }

    private static final String TAG = "HotelDetailActivity";

    private void getHotelInfo() {

        stateLayout.showLoadViewNoContent("加载中...");
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("hid", hotelid);
        RemoteDataHandler.asyncPost(UrlString.URL_HOTEL_DETAIL, params, this, true, response -> {
            if (stateLayout != null) {
                stateLayout.showContenView();
                String json = response.getJson();
                LogHelper.log(TAG, json);
                if (!CommonUtils.isStringNull(json)) {
                    HotelDetailBean hotelDetailBean = new Gson().fromJson(json, HotelDetailBean.class);
                    if (1 == hotelDetailBean.getStatus()) {
                        setHotelInfo(hotelDetailBean.getData());
                    } else {
                        toast(hotelDetailBean.getMessage());
                    }
                } else {
                    toast(Constants.CONNECT_SERVER_FAILED);
                }
            }

        });
    }

    /**
     * 设置酒店信息
     *
     * @param data
     */
    private void setHotelInfo(HotelDetailBean.DataBean data) {
        HotelDetailBean.DataBean.DetailsBean details = data.getDetails();
        hotelname = details.getHotelname();

        picture = details.getPicture();
        tvHotelName.setText(hotelname);
        String format = "%s星级";
        String starLevel = String.format(Locale.CHINA, format, details.getStar());
        //星级
        tvStartLevel.setText(starLevel);
        //位置
        tvHotelLocation.setText(details.getAddress());
        //最低价
        String min_price = details.getMin_price();


        //烦人的最低价ui,最低价¥字体大小颜色设置
        String price = "最低价¥" + min_price;
        SpannableString ss = new SpannableString(price);
        ForegroundColorSpan fs = new ForegroundColorSpan(0xff999999);
        ss.setSpan(fs, 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan rs1 = new RelativeSizeSpan(0.8f);
        ss.setSpan(rs1, 3, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        RelativeSizeSpan rs2 = new RelativeSizeSpan(1.5f);
        ss.setSpan(rs2, 4, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        tvLowerPrice.setText(ss);

        //酒店简介
        tvHotelIntroduce.setText(details.getContent());

//        Glide.with(this).load(details.getPicture()).into(ivHotelPic);

        List<HotelDetailBean.DataBean.PicListBean> picList = data.getPicList();

        List<HotelDetailBean.DataBean.PicListBean> list = new ArrayList<>();
        if (!ListUtil.isEmptyList(picList)) {
            if (picList.size() > 10) {
                for (int i = 0; i < 10; i++) {
                    list.add(picList.get(i));
                }
            } else {
                list.addAll(picList);
            }
        }
        HotelPicAdapter adapter = new HotelPicAdapter(this, list);

        viewPager.setAdapter1(adapter)
                .setIndicator(indicator)
                .startAutoScroll();
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    viewPager.startAutoScroll();
                }
                return false;
            }
        });

        List<RoomBean> roomList = data.getRoomList();
        if (ListUtil.isEmptyList(roomList)) {
            tvNoRoom.setVisibility(View.VISIBLE);
            listViewRoom.setVisibility(View.GONE);
        } else {
            listViewRoom.setAdapter(new RoomAdapter(this, roomList));
        }

    }


    /**
     * 轮播图适配器
     */
    private class HotelPicAdapter extends ImgPagerAdapter<HotelDetailBean.DataBean.PicListBean> {

        public HotelPicAdapter(Context context, List<HotelDetailBean.DataBean.PicListBean> dataList) {
            super(context, dataList);
        }

        @Override
        public void loadImage(ImageView imageView, int position, HotelDetailBean.DataBean.PicListBean picListBean) {

            Glide.with(context).load(picListBean.getPic500()).placeholder(R.mipmap.loading_rect).into(imageView);
        }
    }


    private class RoomAdapter extends QuickAdapter<RoomBean> {

        private final String sizeFormat;
        private final String priceFormat;

        public RoomAdapter(Context context, List<RoomBean> data) {
            super(context, R.layout.item_yuding, data);
            sizeFormat = "%s | %s平方米";
            priceFormat = "¥%s";
        }

        @Override
        protected void convert(BaseAdapterHelper helper, RoomBean roomBean) {

            List<RoomBean.ImgBean> img = roomBean.getImg();

            ImageView ivRoom = helper.getView(R.id.iv_room);

            RoomInfo info = new RoomInfo();

            info.setOrderpic(picture);
            //hotelid
            info.setHotelid(hotelid);
            //roomid
            info.setRoomid(roomBean.getRid());

            info.setHotelName(hotelname);

            //房间照片
            if (!ListUtil.isEmptyList(img)) {
                String imgurl = img.get(0).getImgurl();
                Glide.with(context).load(imgurl).placeholder(R.mipmap.loading_rect).into(ivRoom);
                //roompic
                info.setRoomPic(imgurl);
            } else {
                ivRoom.setImageResource(R.mipmap.loading_rect);
            }
            //房间类型
            String title = roomBean.getTitle();
            helper.setText(R.id.tv_room_type, title);

            //roomtype
            info.setRoomType(title);
            //房间规格 大床房1.8m  |  18平方米

            String area = roomBean.getArea();
            String bed = roomBean.getBed();
            String size = String.format(Locale.CHINA, sizeFormat, bed, area);
            helper.setText(R.id.tv_room_size, size);

            //roomsize
            info.setRoomSize(size);

            info.setArea(area);
            info.setBed(bed);

            String basePrice = "150";
            List<RoomBean.PlansBean> plans = roomBean.getPlans();
            if (!ListUtil.isEmptyList(plans)) {
                RoomBean.PlansBean plansBean = plans.get(0);
                basePrice = plansBean.getTotalprice();
                String planid = plansBean.getPlanid();
                //plainid
                info.setPlainid(planid);

                //suppleid
                info.setSuppid(plansBean.getHotelsupplier());

            }


            //价格
            String price = String.format(priceFormat, basePrice);
            SpannableString ss = new SpannableString(price);
            RelativeSizeSpan rs = new RelativeSizeSpan(0.6f);
            ss.setSpan(ss, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.tv_price, ss);

            helper.setOnClickListener(R.id.tv_yuding, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ListUtil.isEmptyList(plans)) {
                        toast("房间整理中,请选择其他房间...");
                    } else {


                        showProgressDialog("加载中...", true);
                        hp.getRoomYudingInfo(HotelDetailActivity.this, info, null, null, new OnResult<RoomYuding.DataBean>() {
                            @Override
                            public void onSuccess(RoomYuding.DataBean dataBean) {
                                if (listViewRoom != null) {

                                    dismissProgressDialog();
                                    Intent intent = new Intent(HotelDetailActivity.this, FillHotelOrdActivity.class);

                                    intent.putExtra(XNConstants.HOTEL_INFO, info);

                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailed(String errorMsg) {
                                if (listViewRoom != null) {
                                    dismissProgressDialog();
                                    toast("此房将不可预订");
                                }

                            }
                        });

                    }
                }
            });

        }
    }

}
