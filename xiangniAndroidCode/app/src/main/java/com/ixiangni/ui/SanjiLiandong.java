package com.ixiangni.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;


import com.google.gson.Gson;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.Stringable;
import com.ixiangni.adapter.ProvincedWheelAdapter;
import com.ixiangni.adapter.QuWheelAdapter;
import com.ixiangni.adapter.ShiWHeelAdapter;
import com.ixiangni.app.R;
import com.ixiangni.bean.BenDiBean;
import com.ixiangni.ui.Wheel.OnWheelChangedListener;
import com.ixiangni.ui.Wheel.WheelView;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * ClassName: SanjiLiandong
 * PackageName: com.xiaoweiqiye.ui
 * Create On 2017/3/16 0016 14:32
 * Site:http://www.handongkeji.com
 * author:李炳毅
 * Copyrights 2017/3/16 0016 handongkeji All rights reserved.
 */
public class SanjiLiandong implements OnWheelChangedListener {
    private Activity context;
    private BenDiBean bean;
    private View view;
    private WheelView mProvince;
    private WheelView mArea;
    private WheelView mCity;
    private String mCurrentProviceName;
    private int mcurrentprovinecid;
    private Dialog dialog;
    OnCitySelect onCitySelect;
    private ProgressBar mProgressBar;

    public void setOnCitySelect(OnCitySelect onCitySelect) {
        this.onCitySelect = onCitySelect;
    }

    public void hide() {
        dialog.dismiss();
    }

    public SanjiLiandong(Activity context) {
        this.context = context;
        view = initview();
        dialog = new Dialog(context);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottomInStyle);
        WindowManager.LayoutParams attributes = window.getAttributes();
        window.getDecorView().setPadding(0, 0, 0, 0);
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(attributes);
        dialog.setContentView(view);
    }

    private static final String TAG = "SanjiLiandong";

    public void initNetData(String url) {
        mProgressBar.setVisibility(View.VISIBLE);
        RemoteDataHandler.asyncPost(url, null, context, false, new RemoteDataHandler.Callback() {
            @Override
            public void dataLoaded(com.handongkeji.modle.ResponseData data) {
                if (mProgressBar == null) {
                    return;
                }

                mProgressBar.setVisibility(View.GONE);

                String json = data.getJson();

                Log.i(TAG, "dataLoaded: " + json);
                BenDiBean benDiBean = new Gson().fromJson(json, BenDiBean.class);
                if (benDiBean != null && 1 == benDiBean.getStatus()) {
                    bean = benDiBean;
                    mProvince.setViewAdapter(new ProvincedWheelAdapter(bean, context));
                    updateCities();
                    updateAreas();
                }
            }

        });
    }

    public void init() {
        final Handler handler = new Handler(Looper.getMainLooper());

        RemoteDataHandler.threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = context.getResources().getAssets().open("cities.json");
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder sb = new StringBuilder();
                    String info = "";
                    while ((info = bufferedReader.readLine()) != null) {
                        sb.append(info);
                    }
                    Gson gson = new Gson();
                    bean = gson.fromJson(sb.toString(), BenDiBean.class);

                    inputStream.close();
                    inputStreamReader.close();
                    bufferedReader.close();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProvince.setViewAdapter(new ProvincedWheelAdapter(bean, context));
                            updateCities();
                            updateAreas();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void show() {
        dialog.show();
    }

    private View initview() {
        View view1 = View.inflate(context, R.layout.sanjiliandong, null);

        mProgressBar = (ProgressBar) view1.findViewById(R.id.pb);
        mProvince = (WheelView) view1.findViewById(R.id.sheng);
        mArea = (WheelView) view1.findViewById(R.id.cun);
        mCity = (WheelView) view1.findViewById(R.id.shi);
        mProvince.setViewAdapter(new ProvincedWheelAdapter(bean, context));
        // 添加change事件
        mProvince.addChangingListener(this);
        // 添加change事件
        mCity.addChangingListener(this);
        // 添加change事件
        mArea.addChangingListener(this);
        mProvince.setVisibleItems(5);
        mCity.setVisibleItems(5);
        mArea.setVisibleItems(5);

        updateCities();
        updateAreas();
        view1.findViewById(R.id.cancle).setOnClickListener(v -> dialog.dismiss());
        view1.findViewById(R.id.confirm).setOnClickListener(v -> {
            if (bean == null) {
                return;
            }
            int a = mProvince.getCurrentItem();
            int b = mCity.getCurrentItem();
            int c = mArea.getCurrentItem();

            BenDiBean.DataBean province = bean.getData().get(a);//省

            BenDiBean.DataBean.ChildrenBeanXX city = province.getChildren().get(b);//市

            BenDiBean.DataBean.ChildrenBeanXX.ChildrenBeanX area=null;
            if (city.getChildren() != null) {
                area = city.getChildren().get(c);//区
            }

            String pro = bean.getData().get(a).getAreaName();
            String city1 = bean.getData().get(a).getChildren().get(b).getAreaName();

            //具体区

            String area1=null;

            BenDiBean.DataBean.ChildrenBeanXX childrenBeanXX = bean.getData().get(a).getChildren().get(b);
            if(childrenBeanXX!=null&&childrenBeanXX.getChildren()!=null&&childrenBeanXX.getChildren().get(c)!=null){
                area1=childrenBeanXX.getChildren().get(c).getAreaName();
            }
            if (onCitySelect != null) {
                onCitySelect.oncityselect(pro, city1, area1);
            }

            if (this.onSelectArea != null) {
                onSelectArea.onSelectids(province.getAreaId(), city.getAreaId(), area==null?-1:area.getAreaId());
            }
            dialog.dismiss();
        });
        return view1;
    }

    public interface onSelectArea {
        void onSelectids(int provinceid, int cityid, int areaid);
    }

    private onSelectArea onSelectArea;

    public void setOnSelectArea(SanjiLiandong.onSelectArea onSelectArea) {
        this.onSelectArea = onSelectArea;
    }

    private void updateAreas() {
        int pCurrent = mProvince.getCurrentItem();
        int cityposition = mCity.getCurrentItem();
        mArea.setViewAdapter(new QuWheelAdapter(bean, pCurrent, cityposition, context));
        mArea.setCurrentItem(0);
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mProvince.getCurrentItem();
        mCity.setViewAdapter(new ShiWHeelAdapter(bean, pCurrent, context));
        mCity.setCurrentItem(0);
        updateAreas();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {
            updateCities();
            updateAreas();
        } else if (wheel == mCity) {
            updateAreas();
        } else if (wheel == mArea) {

        }
    }

    public interface OnCitySelect {
        public void oncityselect(String province, String city, String areanem);
    }
}
