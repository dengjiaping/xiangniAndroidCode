package com.mydemo.yuanxin.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mydemo.yuanxin.R;
import com.mydemo.yuanxin.bean.JYRecord;

import java.util.List;

/**
 * Created by Administrator on 2017/7/2.
 */

public class MyAdapter extends ArrayAdapter<JYRecord> {
    private int resource;


    public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<JYRecord> objects) {
        super(context, resource, objects);
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;


        JYRecord record = getItem(position);
        if (convertView == null) {

            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tv_record_style = (TextView) view.findViewById(R.id.tv_record_style);
            viewHolder.tv_record_time = (TextView) view.findViewById(R.id.tv_record_time);
            viewHolder.tv_money_change = (TextView) view.findViewById(R.id.tv_money_change);
            viewHolder.tv_state = (TextView) view.findViewById(R.id.tv_state);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        /**
         * 交易类型 1代表转账  2提现    3充值
         *
         */
        String type = record.getTradingrecordCondition();


        /**
         * 交易对象
         */
        String jyPerson = record.getSideMemberName();
        /**
         * 充值方式
         */
        String czType = record.getBank_Name();
        switch (Integer.parseInt(type)) {
            case 1:
                viewHolder.tv_record_style.setText("与" + jyPerson + "进行转账交易");
                break;
            case 2:
                viewHolder.tv_record_style.setText("从圆信卡提现到" + czType);

                break;
            case 3:
                viewHolder.tv_record_style.setText("通过" + czType + "给企业一卡通充值");

                break;
        }

        /**
         * 审核状态  0审核中 1审核成功2审核失败
         */
        String state = record.getAudit_status();
        switch (Integer.parseInt(state)) {
            case 0:
                viewHolder.tv_state.setText("审核中..");
                break;
            case 1:
                viewHolder.tv_state.setText("审核成功");
                break;
            case 2:
                viewHolder.tv_state.setText("审核失败");
                break;

        }


        viewHolder.tv_record_time.setText(record.getSetupDate());
        viewHolder.tv_money_change.setText(record.getYinxincoinNumber());


        return view;
    }

    class ViewHolder {
        private TextView tv_record_style;
        private TextView tv_record_time;
        private TextView tv_state;
        private TextView tv_money_change;

    }
}
