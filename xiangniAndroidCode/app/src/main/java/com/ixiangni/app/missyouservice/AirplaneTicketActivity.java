package com.ixiangni.app.missyouservice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.handongkeji.interfaces.ICallback;
import com.handongkeji.utils.DateUtil;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.bean.CityBean;
import com.ixiangni.bean.DateParams;
import com.ixiangni.common.XNConstants;
import com.ixiangni.dialog.DPdialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 飞机票
 *
 * @ClassName:AirplaneTicketActivity
 * @PackageName:com.ixiangni.app.missyouservice
 * @Create On 2017/7/21 0021   17:54
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/21 0021 handongkeji All rights reserved.
 */
public class AirplaneTicketActivity extends BaseActivity {

    @Bind(R.id.tv_start)
    TextView tvStart;

    @Bind(R.id.tv_end)
    TextView tvEnd;

    @Bind(R.id.tv_start_city)
    TextView tvStartCity;
    @Bind(R.id.tv_end_city)
    TextView tvEndCity;

    @Bind(R.id.tv_time_info)
    TextView tvTimeInfo;

    @Bind(R.id.btn_search)
    Button btnSearch;


//    star	是	String	出发城市三字码
//    end	是	String	结束城市三字码
//    date	是	String	时间(例：2017-07-14)

    private String date = null;

    private String star = null;
    private String end = null;

    private final int REQUEST_CODE_START_CITY = 11;
    private final int REQUEST_CODE_END_CITY = 12;
    private DPdialog dPdialog;
    private String todayString;
    private String tomorrowString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airplane_ticket);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        Calendar instance = Calendar.getInstance();
        Date time = instance.getTime();
        todayString = DateUtil.getDateString(time);
        instance.add(Calendar.DAY_OF_MONTH, 1);
        tomorrowString = DateUtil.getDateString(instance.getTime());

        date = DateUtil.getYmd(time);
        tvTimeInfo.setText(todayString + " 今天");


        dPdialog = new DPdialog(this);

        dPdialog.setDataCallback(new ICallback<String>() {
            @Override
            public void call(String s) {

                date = s;

                s= convertDate(s);

                if (s.equals(todayString)) {
                    s += " 今天";
                }

                if (s.equals(tomorrowString)) {
                    s += " 明天";
                }
                tvTimeInfo.setText(s);
            }
        });
    }

    private String convertDate(String date){
        long timeLong = DateUtil.getTimeLong(date);

        return DateUtil.getDateString(new Date(timeLong));
    }

    @OnClick({R.id.tv_start_city, R.id.tv_end_city, R.id.tv_time_info, R.id.btn_search})
    public void onViewClicked(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.tv_start_city://选择出发地

                intent = new Intent(this, CityListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_START_CITY);
                break;
            case R.id.tv_end_city://选择目的地
                intent = new Intent(this, CityListActivity.class);
                startActivityForResult(intent, REQUEST_CODE_END_CITY);

                break;
            case R.id.tv_time_info:
                dPdialog.show();
                break;
            case R.id.btn_search:
                searchTicket();
                break;
        }
    }

    private void searchTicket() {
        if(TextUtils.isEmpty(star)){
            toast("请选择出发地");
            return;
        }

        if(TextUtils.isEmpty(end)){
            toast("请选择目的地");
            return;
        }

        if (TextUtils.isEmpty(date)){
            toast("请选择出发日期");
            return;
        }

        if(star.equals(end)){
            toast("出发地和目的地不能相同");
            return;
        }

        DateParams params = new DateParams();
        params.setDate(date);
        params.setEnd(end);
        params.setStar(star);
        params.setStartCity(tvStartCity.getText().toString());
        params.setEndCity(tvEndCity.getText().toString());

        Intent intent = new Intent(this,PTListActivity.class);
        intent.putExtra(XNConstants.plane_list_params,params);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_START_CITY://回显出发地城市
                    CityBean startBean = (CityBean) data.getSerializableExtra(XNConstants.ari_city);
                    star = startBean.getCitycode();
                    tvStartCity.setText(startBean.getCityname());

                    break;
                case REQUEST_CODE_END_CITY://回显目的地城市
                    CityBean endBean = (CityBean) data.getSerializableExtra(XNConstants.ari_city);
                    end = endBean.getCitycode();
                    tvEndCity.setText(endBean.getCityname());

                    break;
            }
        }
    }
}
