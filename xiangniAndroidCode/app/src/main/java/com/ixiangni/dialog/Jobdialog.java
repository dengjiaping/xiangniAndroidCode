package com.ixiangni.dialog;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.OnSelectListener;
import com.ixiangni.app.R;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.JobListBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.ui.Wheel.WheelView;
import com.ixiangni.ui.wheeladapter.WheelViewAdapter;
import com.nevermore.oceans.utils.ListUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class Jobdialog extends XDialog {

    @Bind(R.id.cancle)
    TextView cancle;
    @Bind(R.id.confirm)
    TextView confirm;
    @Bind(R.id.wheel)
    WheelView wheel;
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.tv_no_data)
    TextView tvNoData;
    private List<JobListBean.DataBean> jobList;

    public Jobdialog(Context context) {
        super(context, R.layout.dialog_select_item);
        ButterKnife.bind(this);
        setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setWindowAnimations(R.style.bottomInStyle);
        getWindow().setGravity(Gravity.BOTTOM);

    }

    private static final String TAG = "Jobdialog";

    public void initJob(String branchid) {
        wheel.removeAllViews();
        pb.setVisibility(View.VISIBLE);
        wheel.setVisibility(View.GONE);

        HashMap<String, String> params = new HashMap<>();
        params.put("token", LoginHelper.getInstance().getToken(getContext()));
        params.put("branchid", branchid);

        RemoteDataHandler.asyncPost(UrlString.URL_JOB_LIST, params, getContext(), true, response -> {
            pb.setVisibility(View.GONE);
            try {
                String json = response.getJson();
                if (!TextUtils.isEmpty(json)) {

                    Log.i(TAG, "initData: " + json);
                    JobListBean jobListBean = new Gson().fromJson(json, JobListBean.class);
                    if (1 == jobListBean.getStatus()) {

                        jobList = jobListBean.getData();
                        if(ListUtil.isEmptyList(jobList)){
                            tvNoData.setVisibility(View.VISIBLE);
                            tvNoData.setText("该部门暂无职位...");

                        }else {
                            tvNoData.setVisibility(View.GONE);
                            wheel.setVisibility(View.VISIBLE);
                            wheel.setVisibleItems(5);
                            wheel.setCyclic(true);
                            wheel.setViewAdapter(new JobAdapter());
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    public class JobAdapter implements WheelViewAdapter {

        @Override
        public int getItemsCount() {
            return jobList == null ? 0 : jobList.size();
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView != null) {
                holder= (ViewHolder) convertView.getTag();

            } else {
                convertView = View.inflate(getContext(), R.layout.list_litem, null);
                convertView.setTag(holder = new ViewHolder(convertView));
            }

            holder.listite.setText(jobList.get(index).getJobname());

            return convertView;
        }

        @Override
        public View getEmptyItem(View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

         public class ViewHolder {
            @Bind(R.id.listite)
            TextView listite;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    private OnSelectListener<JobListBean.DataBean> mListener;

    public void setOnJobSelectListener(OnSelectListener<JobListBean.DataBean> mListener) {
        this.mListener = mListener;
    }

    @OnClick({R.id.cancle, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                break;
            case R.id.confirm:
                if(mListener!=null){

                    if(wheel.getVisibility()!=View.VISIBLE){
                        return;
                    }
                    mListener.onSelected(jobList.get(wheel.getCurrentItem()));
                }
                break;
        }
        dismiss();
    }
}
