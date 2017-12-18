package com.ixiangni.app.missyouservice;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.RoomInfo;
import com.ixiangni.bean.RoomYuding;
import com.ixiangni.common.XNConstants;
import com.ixiangni.presenters.HotelPresenter;
import com.ixiangni.util.StateLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.aigestudio.datepicker.bizs.calendars.DPCManager;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.bizs.themes.DPBaseTheme;
import cn.aigestudio.datepicker.bizs.themes.DPTManager;
import cn.aigestudio.datepicker.utils.DataUtils;
import cn.aigestudio.datepicker.views.DatePicker;
import cn.aigestudio.datepicker.views.MonthView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 选择入住时间
 *
 * @ClassName:SelectTimeActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/27 0027   19:03
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/27 0027 handongkeji All rights reserved.
 */
public class SelectTimeActivity extends BaseActivity {

    @Bind(R.id.date_picker)
    DatePicker datePicker;
    @Bind(R.id.btn_sure)
    Button btnSure;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.tv_yuding_time)
    TextView tvYudingTime;
    private RoomInfo info;


    //满房日期
    private List<String> filledDate = null;
    //可预订的全部日期
    private List<String> rangeDate = null;
    private HotelPresenter presenter;
    private MonthView monthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        ButterKnife.bind(this);
        DPTManager.getInstance().initCalendar(new TimeTheme());


        info = (RoomInfo) getIntent().getSerializableExtra(XNConstants.HOTEL_INFO);

        filledDate = new ArrayList<>();
        rangeDate = new ArrayList<>();
        initView();


        presenter = new HotelPresenter();

        initDate();


    }

    private void initDate() {
        stateLayout.showLoadViewNoContent("加载中");
        presenter.getRoomYudingInfo(this, info, null, null, new OnResult<RoomYuding.DataBean>() {
            @Override
            public void onSuccess(RoomYuding.DataBean dataBean) {
                if (stateLayout == null) {
                    return;
                }


                RoomYuding.DataBean.PricesBean prices = dataBean.getPrices();

                List<RoomYuding.DataBean.PricesBean.DaillBean> daill = prices.getDaill();

                Observable.from(daill)
                        .map(new Func1<RoomYuding.DataBean.PricesBean.DaillBean, String>() {
                            @Override
                            public String call(RoomYuding.DataBean.PricesBean.DaillBean daillBean) {

                                return daillBean.getDate();
                            }
                        })
                        .toList()
                        .subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<String>>() {
                            @Override
                            public void call(List<String> list) {
                                rangeDate = list;
                                if (rangeDate != null && rangeDate.size() >= 2) {

                                    tvYudingTime.setText("可预订时间:"+rangeDate.get(0)+"至"+rangeDate.get(rangeDate.size()-1));
                                }
                            }
                        });
                Observable.from(daill)
                        .filter(new Func1<RoomYuding.DataBean.PricesBean.DaillBean, Boolean>() {
                            @Override
                            public Boolean call(RoomYuding.DataBean.PricesBean.DaillBean daillBean) {
                                if (daillBean.getPrice().equals("满房")) {

                                    return true;
                                }
                                return false;
                            }
                        })
                        .map(new Func1<RoomYuding.DataBean.PricesBean.DaillBean, String>() {
                            @Override
                            public String call(RoomYuding.DataBean.PricesBean.DaillBean daillBean) {

                                return daillBean.getDate();
                            }
                        })
                        .toList()
                        .subscribeOn(Schedulers.from(RemoteDataHandler.threadPool))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<List<String>>() {
                            @Override
                            public void call(List<String> list) {

                                log(list.toString());
                                filledDate = list;
                                DPCManager.getInstance().setDecorT(list);
                                monthView.notifyUI();

                            }
                        });

                stateLayout.showContentView(true);

            }

            @Override
            public void onFailed(String errorMsg) {

                if (datePicker == null) {
                    return;
                }
                dismissProgressDialog();
                toast(errorMsg);
            }
        });
    }

    /**
     * 判断两个日期之前是否存在满房
     * @param startDate
     * @param endDate
     * @return
     */
    private boolean isContainFull(String startDate,String endDate){

        long start = getTime(startDate);

        long end = getTime(endDate);


        for (int i = 0; i < filledDate.size(); i++) {
            String date = filledDate.get(i);
            long time = getTime(date);
            if(time>start&&time<end){
                return true;
            }
        }
        return false;
    }


    private void initView() {
        Calendar instance = Calendar.getInstance();


        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH) + 1;

        log("year:" + year);
        log("month:" + month);
        datePicker.setDate(year, month);
        datePicker.setFestivalDisplay(false);
        datePicker.setTodayDisplay(false);


        DPCManager.getInstance().setUseInfoCache(false);
        monthView = datePicker.getMonthView();
        monthView.setVerticalScrollEnable(false);


        //绘制标记物
        monthView.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorT(Canvas canvas, Rect rect, Paint paint, String data) {
                drawFull(canvas, rect, paint);
            }


        });

        monthView.setOnDateClick(new MonthView.OnDateClick() {
            @Override
            public void beforeCancel(String date) {
                String selectedDate = DataUtils.toStandardDate(date);
                if (selectedDate.equals(startDate)) {
                    startDate = null;
                }
                if (selectedDate.equals(endDate)) {
                    endDate = null;
                }

            }

            @Override
            public boolean beforeSelect(String date) {
                return false;
            }

            @Override
            public boolean isDateCanSelect(String date) {
                String selectedDate = DataUtils.toStandardDate(date);


                if (monthView.getDateSelected().size() >= 2) {
//                    toast("日期不能超过两个");
                    return false;
                }
                //满房都不能选
                if (filledDate.contains(selectedDate)) {
                    if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
                        toast("已满房");
                    }
                    return false;
                }

                if (!rangeDate.contains(selectedDate)) {
                    toast("超出预订范围");
                    return false;
                }

                //入住
                if (TextUtils.isEmpty(startDate)) {
                    if (TextUtils.isEmpty(endDate)) {
                        startDate = selectedDate;
                        return true;
                    } else {
                        if (getTime(selectedDate) > getTime(endDate)) {
//                           toast("住店时间大于离店时间");
                            return false;
                        } else {//入住时间必须在离店时间前

                            if(isContainFull(selectedDate,endDate)){
                                toast("时间段包含满房，不可预订");
                                return false;
                            }else {
                                startDate = selectedDate;
                                return true;
                            }
                        }
                    }
                    //离店
                } else {
                    if (getTime(selectedDate) > getTime(startDate)) {

                        if(isContainFull(startDate,selectedDate)){
                            toast("时间段包含满房，不可预订");
                            return false;
                        }else {

                            endDate = selectedDate;
                            return true;
                        }

                    } else {
//                        toast("离店时间小于入店时间");
                        return false;
                    }

                }

            }
        });

        //被选中日期背景颜色
        monthView.setCircleBG(new MonthView.CircleBG() {
            @Override
            public int getCircleBg(String date) {
//                if (monthView.getDateSelected().size() > 1) {
//                    return 0xffcb302b;
//                }
                if (DataUtils.toStandardDate(date).equals(startDate)) {
                    return COLOR_BLUE;
                }

                if (DataUtils.toStandardDate(date).equals(endDate)) {
                    return COLOR_RED;
                }

                if (TextUtils.isEmpty(startDate)) {
                    return COLOR_BLUE;
                }

                if (TextUtils.isEmpty(endDate)) {
                    return COLOR_RED;
                }

                return COLOR_BLUE;
            }
        });

        btnSure.setOnClickListener(v -> {

            if (TextUtils.isEmpty(startDate)) {
                toast("请选择入住时间");
                return;
            }

            if (TextUtils.isEmpty(endDate)) {
                toast("请选择离店时间");
                return;
            }

            Intent intent = new Intent();
            intent.putExtra(XNConstants.startDate, startDate);
            intent.putExtra(XNConstants.endDate, endDate);
            setResult(RESULT_OK, intent);
            finish();

        });
    }

    private final int COLOR_BLUE = 0xff00a0e9;
    private final int COLOR_RED = 0xffcb302b;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


    /**
     * 将2017-08-23格式的转换对应的long 时间值
     * @param date
     * @return
     */
    public long getTime(String date) {

        try {
            Date parse = format.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private String startDate = null;
    private String endDate = null;

    /**
     * 绘制满房标记
     *
     * @param canvas
     * @param rect
     * @param paint
     */
    private void drawFull(Canvas canvas, Rect rect, Paint paint) {
        paint.setColor(Color.RED);
//                    canvas.drawCircle(rect.centerX(),rect.centerY(),rect.width()/2,paint);
        canvas.drawRect(rect, paint);
        String str = "满";

        float v = paint.measureText(str);
        paint.setTextSize(rect.height() * 0.8f);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        float v1 = (fontMetrics.descent + fontMetrics.ascent) / 2;

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.WHITE);
        canvas.drawText(str, rect.centerX(), rect.centerY() - v1, paint);
    }


    class TimeTheme extends DPBaseTheme {
        @Override
        public int colorWeekend() {
            return ContextCompat.getColor(SelectTimeActivity.this, R.color.colorPrice);
        }

        @Override
        public int colorTitleBG() {
            return ContextCompat.getColor(SelectTimeActivity.this, R.color.themeRed);
        }

        @Override
        public int colorBGCircle() {
            return 0xff00a0e9;
        }
    }
}
