package com.ixiangni.dialog;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.ICallback;
import com.ixiangni.app.R;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.aigestudio.datepicker.bizs.themes.DPBaseTheme;
import cn.aigestudio.datepicker.bizs.themes.DPTManager;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.utils.DataUtils;
import cn.aigestudio.datepicker.views.DatePicker;
import cn.aigestudio.datepicker.views.MonthView;

/**
 * Created by Administrator on 2017/8/5 0005.
 */

public class DPdialog extends XDialog {

    @Bind(R.id.tv_cancel)
    TextView tvCancel;
    @Bind(R.id.tv_ensure)
    TextView tvEnsure;
    @Bind(R.id.date_picker)
    DatePicker datePicker;

    private static final String TAG = "DPdialog";

    private String dateSelected = null;

    public DPdialog(Context context) {
        super(context, R.layout.dialog_select_time);
        ButterKnife.bind(this);

        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.bottomInStyle);

        setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        Calendar instance = Calendar.getInstance();



        DPTManager.getInstance().initCalendar(new TimeTheme());
        datePicker.setDate(instance.get(Calendar.YEAR),instance.get(Calendar.MONTH)+1);
        datePicker.setFestivalDisplay(false);
        datePicker.setTodayDisplay(false);
        MonthView monthView = datePicker.getMonthView();
        monthView.setVerticalScrollEnable(false);
        monthView.setDPMode(DPMode.SINGLE);

        monthView.setInterceptor(date -> {
            String s = DataUtils.toStandardDate(date);

            String[] split = s.split("-");

            if(split.length>=3){
                if(Integer.parseInt(split[0])<instance.get(Calendar.YEAR)){
                    return false;
                }
                if(Integer.parseInt(split[1])<instance.get(Calendar.MONTH)+1){
                    return false;
                }
//                日期小于今天选不了
//                if(Integer.parseInt(split[2])<instance.get(Calendar.DAY_OF_MONTH)){
//                    return false;
//                }
            }
            return true;
        });
        monthView.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
            @Override
            public void onDatePicked(String date) {
                dateSelected = DataUtils.toStandardDate(date);
            }
        });
    }

    private ICallback<String> dataCallback;

    public ICallback<String> getDataCallback() {
        return dataCallback;
    }

    public void setDataCallback(ICallback<String> dataCallback) {
        this.dataCallback = dataCallback;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_ensure:
                if(TextUtils.isEmpty(dateSelected)){
                    toast("请选择出发日期");
                    return;
                }

                if(dataCallback!=null){
                    dataCallback.call(dateSelected);
                }
                dismiss();
                break;
        }
    }
    class TimeTheme extends DPBaseTheme {
        @Override
        public int colorWeekend() {
            return ContextCompat.getColor(getContext(), R.color.colorPrice);
        }

        @Override
        public int colorTitleBG() {
            return ContextCompat.getColor(getContext(), R.color.themeRed);
        }

        @Override
        public int colorBGCircle() {
            return ContextCompat.getColor(getContext(),R.color.colorPrice);
        }
    }
}
