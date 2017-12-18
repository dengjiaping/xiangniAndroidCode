package cn.rxph.www.wq2017.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cn.rxph.www.wq2017.R;
import cn.rxph.www.wq2017.bean.QyRecord;

/**
 * Created by Administrator on 2017/8/2.
 */

public class QyAdapter extends ArrayAdapter<QyRecord> {
    private int resource;


    public QyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<QyRecord> objects) {
        super(context, resource, objects);
        this.resource = resource;

    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;


        QyRecord qyR = getItem(position);
        if (convertView == null) {

            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_qy_name);
            viewHolder.tv_tel = (TextView) view.findViewById(R.id.tv_qy_tel);
            viewHolder.tv_result = (TextView) view.findViewById(R.id.tv_qy_result);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        switch (Integer.parseInt(qyR.getApply_AuditStatus())){
            case 1:
                viewHolder.tv_result.setText("签约中..");
                break;
            case 2:
                viewHolder.tv_result.setText("签约成功");
                break;
            case 3:
                viewHolder.tv_result.setText("签约失败");
                break;
        }


        viewHolder.tv_name.setText(qyR.getApply_oneself_Name());
        viewHolder.tv_tel.setText(qyR.getApply_oneself_Phone());



        return view;
    }

    class ViewHolder {
        TextView tv_name;
        TextView tv_tel;
        TextView tv_result;
    }
}

