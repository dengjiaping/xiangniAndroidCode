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
import cn.rxph.www.wq2017.bean.Company;

/**
 * Created by Administrator on 2017/7/2.
 */

public class MyAdapter extends ArrayAdapter<Company> {
    private int resource;


    public MyAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Company> objects) {
        super(context, resource, objects);
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;


        Company zllm = getItem(position);
        if (convertView == null) {

            view = LayoutInflater.from(getContext()).inflate(resource, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tv_zllm = (TextView) view.findViewById(R.id.tv_company);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }


        viewHolder.tv_zllm.setText(zllm.getBranchName());


        return view;
    }

    class ViewHolder {
        TextView tv_zllm;
    }
}
