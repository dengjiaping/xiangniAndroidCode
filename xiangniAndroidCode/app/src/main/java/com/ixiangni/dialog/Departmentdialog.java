package com.ixiangni.dialog;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.dialog.XDialog;
import com.ixiangni.app.R;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.bean.DepartmentBean;
import com.ixiangni.constants.UrlString;
import com.ixiangni.ui.Wheel.WheelView;
import com.ixiangni.ui.wheeladapter.WheelViewAdapter;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class Departmentdialog extends XDialog {

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
    private List<DepartmentBean.DataBean> list;

    public Departmentdialog(Context context) {
        super(context, R.layout.dialog_select_item);
        ButterKnife.bind(this);
        setWidthAndHeight(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        getWindow().setWindowAnimations(R.style.bottomInStyle);
        getWindow().setGravity(Gravity.BOTTOM);
    }

    private static final String TAG = "Departmentdialog";

    public void initData() {

        pb.setVisibility(View.VISIBLE);
        HashMap<String, String> params = new HashMap<>();
        params.put("token", LoginHelper.getInstance().getToken(getContext()));
        RemoteDataHandler.asyncPost(UrlString.URL_DEPARTMENT_LIST, params, getContext(), true, response -> {

            try {
                pb.setVisibility(View.GONE);
                String json = response.getJson();
                Log.i(TAG, "initData: " + json);

                if (TextUtils.isEmpty(json)) {
                    tvNoData.setVisibility(View.VISIBLE);
                    tvNoData.setError("暂无部门信息...");
                } else {
                    DepartmentBean departmentBean = new Gson().fromJson(json, DepartmentBean.class);
                    if (1 == departmentBean.getStatus()) {

                        list = departmentBean.getData();

                        DepartmentAdapter departmentAdapter = new DepartmentAdapter(list);
                        wheel.setVisibleItems(5);
                        wheel.setCyclic(true);
                        wheel.setViewAdapter(departmentAdapter);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public interface OnDepartmentSelectListener {
        void onGetDepartment(DepartmentBean.DataBean department);
    }


    private OnDepartmentSelectListener mListener;


    public void setDepartmentSelectListener(OnDepartmentSelectListener mListener) {
        this.mListener = mListener;
    }

    @OnClick({R.id.cancle, R.id.confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                break;
            case R.id.confirm:
                if (mListener != null) {
                    if(wheel.getVisibility()!=View.VISIBLE){
                        return;
                    }
                    int currentItem = wheel.getCurrentItem();
                    DepartmentBean.DataBean dataBean = list.get(currentItem);
                    mListener.onGetDepartment(dataBean);
                }

                break;
        }
        dismiss();

    }

    private class DepartmentAdapter implements WheelViewAdapter {
        private List<DepartmentBean.DataBean> mList;

        public DepartmentAdapter(List<DepartmentBean.DataBean> mList) {
            this.mList = mList;
        }

        @Override
        public int getItemsCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            View view = View.inflate(getContext(), R.layout.list_litem, null);
            DepartmentBean.DataBean dataBean = mList.get(index);
            ((TextView) view.findViewById(R.id.listite)).setText(dataBean.getBranchname());
            return view;
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
    }


}
