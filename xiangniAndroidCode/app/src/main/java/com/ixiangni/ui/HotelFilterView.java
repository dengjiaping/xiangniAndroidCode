package com.ixiangni.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handongkeji.impactlib.dialog.XpopupWindow;
import com.ixiangni.app.R;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

public class HotelFilterView extends FrameLayout implements View.OnClickListener {
    @Bind(R.id.rl_star)
    RelativeLayout rlStar;
    @Bind(R.id.divider)
    View divider;
    @Bind(R.id.rl_price)
    RelativeLayout rlPrice;
    private List<HotelCondition> starConditionList;
    private List<HotelCondition> priceCondition;
    private XpopupWindow xpopupWindow;
    private FlowLayout flStar;
    private FlowLayout flPrice;
    private TextView tvCancel;
    private TextView tvComplete;
    private ConditionAdapter adapterStar;
    private ConditionAdapter adapterPrice;
    private int selectedPoi = 0;
    private int selectedPoi1 = 0;

    public HotelFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.tag_select_layout, this);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_pop_hotel_condition, null);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvComplete = (TextView) view.findViewById(R.id.tv_complete);

        tvCancel.setOnClickListener(this);
        tvComplete.setOnClickListener(this);

        flStar = (FlowLayout) view.findViewById(R.id.fl_star);
        flPrice = (FlowLayout) view.findViewById(R.id.fl_price);

        adapterStar = new ConditionAdapter(getContext(), starConditionList);
        adapterPrice = new ConditionAdapter(getContext(), priceCondition);

        flStar.setAdapter(adapterStar);
        flPrice.setAdapter(adapterPrice);


        xpopupWindow = new XpopupWindow(getContext(), view);
        xpopupWindow.setOutsideTouchable(false);
        xpopupWindow.setFocusable(false);
        xpopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        xpopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                rlStar.setSelected(false);
                rlPrice.setSelected(false);
            }
        });
    }

    private void initData() {
        starConditionList = new ArrayList<>();
        starConditionList.add(new HotelCondition("不限", null));
        starConditionList.add(new HotelCondition("二星/其他", "2"));
        starConditionList.add(new HotelCondition("三星/舒适", "3"));
        starConditionList.add(new HotelCondition("四星/高档", "4"));
        starConditionList.add(new HotelCondition("五星/豪华", "5"));

        String rmb = getResources().getString(R.string.rmb);
        priceCondition = new ArrayList<>();

        priceCondition.add(new HotelCondition("不限", null));
        priceCondition.add(new HotelCondition(rmb + "150以下", "1"));
        priceCondition.add(new HotelCondition(rmb + "150-300", "2"));
        priceCondition.add(new HotelCondition(rmb + "300-600", "3"));
        priceCondition.add(new HotelCondition(rmb + "600-1000", "4"));
        priceCondition.add(new HotelCondition(rmb + "1000以上", "5"));

    }

    @OnClick({R.id.rl_star, R.id.rl_price})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_star:
                if (rlStar.isSelected()) {
                    rlStar.setSelected(false);
                    xpopupWindow.dismiss();
                } else {
                    rlStar.setSelected(true);
                    rlPrice.setSelected(false);
                    flStar.setVisibility(VISIBLE);
                    flPrice.setVisibility(View.GONE);
                    xpopupWindow.showAsDropDown(this);
                }
                break;
            case R.id.rl_price:
                if (rlPrice.isSelected()) {
                    rlStar.setSelected(false);
                    xpopupWindow.dismiss();
                } else {
                    rlPrice.setSelected(true);
                    rlStar.setSelected(false);
                    flPrice.setVisibility(View.VISIBLE);
                    flStar.setVisibility(View.GONE);
                    xpopupWindow.showAsDropDown(this);
                }
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                adapterStar.selectPoi(selectedPoi);
                adapterPrice.selectPoi(selectedPoi1);
                xpopupWindow.dismiss();
                break;
            case R.id.tv_complete:
                xpopupWindow.dismiss();
                rlPrice.setSelected(false);
                rlPrice.setSelected(false);

                if (conditionSelected != null) {
                    selectedPoi = adapterStar.getSelectedPoi();
                    selectedPoi1 = adapterPrice.getSelectedPoi();

                    conditionSelected.onCondition(starConditionList.get(selectedPoi).getParams(), priceCondition.get(selectedPoi1).getParams());
                }
                break;
        }
    }

    private class HotelCondition {
        private String word;
        private String params;

        public HotelCondition(String word, String params) {
            this.word = word;
            this.params = params;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }
    }

    public interface OnConditionSelected {
        void onCondition(String star, String price);
    }

    private OnConditionSelected conditionSelected;

    public OnConditionSelected getConditionSelected() {
        return conditionSelected;
    }

    public void setConditionSelected(OnConditionSelected conditionSelected) {
        this.conditionSelected = conditionSelected;
    }

    private class ConditionAdapter extends QuickAdapter<HotelCondition> {

        private int selectedPoi = 0;

        public int getSelectedPoi() {
            return selectedPoi;
        }

        public void setSelectedPoi(int selectedPoi) {
            this.selectedPoi = selectedPoi;
        }

        public ConditionAdapter(Context context) {
            super(context, R.layout.item_tag);
        }

        public ConditionAdapter(Context context, List<HotelCondition> data) {
            super(context, R.layout.item_tag, data);
        }

        private TextView lastSelectTextView;

        @Override
        protected void convert(BaseAdapterHelper helper, HotelCondition hotelCondition) {
            int position = helper.getPosition();
            final TextView tv = helper.getView(R.id.tv_tag);
            tv.setText(hotelCondition.getWord());
            tv.setSelected(position == getSelectedPoi());
            if(position==getSelectedPoi()){
                lastSelectTextView=tv;
            }

            tv.setOnClickListener(v -> {
                setSelectedPoi(position);
                if(lastSelectTextView!=null){
                    lastSelectTextView.setSelected(false);
                }
                tv.setSelected(true);
                lastSelectTextView=tv;
            });
        }

        public void selectPoi(int selectedPoi){
            setSelectedPoi(selectedPoi);
            notifyDataSetChanged();
        }


    }
}
