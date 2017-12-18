package com.ixiangni.app.emotion;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.adapter.MyRvAdapter;
import com.ixiangni.adapter.MyRvViewHolder;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.app.mine.MyEmotionActivity;
import com.ixiangni.bean.BuiedEmListBean;
import com.ixiangni.common.EmotionManager;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.ob.SuperObservableManager;
import com.nevermore.oceans.utils.ListUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class BroughtEmFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener, MyEmotionActivity.OnEditChange {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    @Bind(R.id.state_layout)
    StateLayout stateLayout;
    @Bind(R.id.rl_edit_layout)
    RelativeLayout rlEditLayout;
    private MyAdapter adapter;

    public BroughtEmFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        SuperObservableManager
                .getInstance()
                .getObservable(MyEmotionActivity.OnEditChange.class)
                .registerObserver(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SuperObservableManager
                .getInstance()
                .getObservable(MyEmotionActivity.OnEditChange.class)
                .unregisterObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_brought_em, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyAdapter(getContext());
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "获取表情的详细内容", Toast.LENGTH_SHORT).show();
                //                传送具体是那个表情包的具体数据
                startActivity(new Intent(getContext(), BroughtEmActivity.class));
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if (adapter.getItemCount() == 0) {
                    stateLayout.showNoDataView("暂无表情");
                } else {
                    stateLayout.showContenView();
                }
            }
        });

        recyclerView.setAdapter(adapter);
        getEmotionList();

        cbSelectAll.setOnCheckedChangeListener(this);
        btnDelete.setOnClickListener(v -> {
            deleteEmtions();

        });
    }

    /**
     * 删除中
     */
    private void deleteEmtions() {
        List<BuiedEmListBean.DataBean> selectedItems = adapter.getSelectedItems();
        if (ListUtil.isEmptyList(selectedItems)) {
            toast("您没有选择任何表情");
        } else {
//                browbaguserid
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < selectedItems.size(); i++) {
                BuiedEmListBean.DataBean dataBean = selectedItems.get(i);
                int browbaguserid = dataBean.getBrowbaguserid();
                sb.append(browbaguserid);
                if (i != selectedItems.size() - 1) {
                    sb.append(",");
                }
            }
            String ids = sb.toString();

            HashMap<String, String> params = new HashMap<>();
//            token	是	String	用户token
//            browbaguserid	是	Long	用户表情包id

            showProgressDialog("删除中...", false);
            params.put(Constants.token, MyApp.getInstance().getUserTicket());
            params.put("ides", ids);
            MyPresenter.request(getContext(), UrlString.URL_DELETE_EMOTIONS, params, new OnResult<String>() {
                @Override
                public void onSuccess(String s) {
                    dismissProgressDialog();
                    EmotionManager.getInstance().notifyEmotionChange();
                    getEmotionList();
                    toast("删除成功！");
                }

                @Override
                public void onFailed(String errorMsg) {
                    dismissProgressDialog();
                    toast(errorMsg);
                }
            });


        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        adapter.selectAll(isChecked);

    }

    private void getEmotionList() {

        stateLayout.showLoadView();
        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_BROUGHT_EMOTION_LIST, params, getContext(), true, response -> {
            if (stateLayout != null) {
                String json = response.getJson();
                log("getEmotionList: " + json);
                if (!CommonUtils.isStringNull(json)) {
                    BuiedEmListBean buiedEmListBean = new Gson().fromJson(json, BuiedEmListBean.class);
                    if (1 == buiedEmListBean.getStatus()) {
                        List<BuiedEmListBean.DataBean> emotionList = buiedEmListBean.getData();
                        adapter.replaceAll(emotionList);
                    }
                }
            }

        });
    }

    @Override
    public void mode(boolean isEdit) {

        rlEditLayout.setVisibility(isEdit?View.VISIBLE:View.GONE);
        adapter.setEdit(isEdit);
    }


    class MyAdapter extends MyRvAdapter<BuiedEmListBean.DataBean> {


        private SparseArray<Boolean> selectedPoiArray = new SparseArray<>();

        private boolean isEdit = false;

        public void setEdit(boolean flag) {
            isEdit = flag;
            notifyDataSetChanged();
        }

        @Override
        public void replaceAll(List<BuiedEmListBean.DataBean> newDataList) {
            selectedPoiArray = new SparseArray<>();
            super.replaceAll(newDataList);
        }

        public void selectAll(boolean flag) {
            for (int i = 0; i < getItemCount(); i++) {
                selectedPoiArray.put(i, flag);
            }
            notifyDataSetChanged();
        }

        public MyAdapter(Context context) {
            super(context, R.layout.item_emtion_edit);
        }


        public List<BuiedEmListBean.DataBean> getSelectedItems() {
            List<BuiedEmListBean.DataBean> list = new ArrayList<>();
            for (int i = 0; i < selectedPoiArray.size(); i++) {
                int position = selectedPoiArray.keyAt(i);

                boolean isSelected = selectedPoiArray.get(position) == null ? false : selectedPoiArray.get(position);

                if (isSelected) {
                    list.add(getItem(position));
                }
            }
            return list;
        }

        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, BuiedEmListBean.DataBean data) {

            String browbaginfo = data.getBrowbaginfo();
            String emotionname = data.getBrowbagname();
            Double emtionprice = data.getBuyprice();
            List<BuiedEmListBean.DataBean.UserBrowListBean> beans = data.getUserBrowList();
            for (int i = 0; i < beans.size(); i++) {
                BuiedEmListBean.DataBean.UserBrowListBean userBrowListBean = beans.get(i);
                String s = userBrowListBean.getBrowinfo();
            }
            CheckBox cb = holder.getView(R.id.cb_select);

            ImageView iv = holder.getView(R.id.iv_emotion_icon);
            ((TextView) holder.getView(R.id.tv_emotion_price)).setText(emtionprice + "");
            ((TextView) holder.getView(R.id.tv_emotion_name)).setText(emotionname);
            cb.setVisibility(isEdit ? View.VISIBLE : View.GONE);

            if (isEdit) {
                boolean isSelected = selectedPoiArray.get(position) == null ? false : selectedPoiArray.get(position);
                cb.setChecked(isSelected);
            }

            cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                selectedPoiArray.put(position, isChecked);

            });

            Glide.with(context).load(browbaginfo)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.drawable.ease_default_expression)
                    .into(iv);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
