package com.ixiangni.app.emotion;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
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
import com.ixiangni.bean.CollectedListBean;
import com.ixiangni.common.EmotionManager;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.ui.RecyclerViewDivider;
import com.ixiangni.ui.SpaceItemDecoration;
import com.ixiangni.util.GridDecoration;
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
public class EmCollectionFragment extends BaseFragment implements MyEmotionActivity.OnEditChange {


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
    private CollectAdapter adapter;

    public EmCollectionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_em_collection, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(manager);
//        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
//        recyclerView.addItemDecoration(new RecyclerViewDivider(getContext(),LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new GridDecoration(5,2, 0xffaaaaaa));
//        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//
//                super.getItemOffsets(outRect, view, parent, state);
//
//                int childAdapterPosition = parent.getChildAdapterPosition(view);
//
//                int colum = childAdapterPosition % 5;
//                if (colum != 0) {
//
//                    outRect.right = 1;
//                }
//                int row = childAdapterPosition / 5;
//                if (row < parent.getChildCount() / 5) {
//
//                    outRect.bottom = 1;
//                }
//            }
//
//            @Override
//            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//                super.onDraw(c, parent, state);
//                int childCount = parent.getChildCount();
//
//                Paint paint = new Paint();
//                paint.setStyle(Paint.Style.FILL);
//                paint.setAntiAlias(true);
//                paint.setColor(0xff000000);
//                for (int i = 0; i < childCount; i++) {
//                    View childAt = parent.getChildAt(i);
//                    int left = childAt.getLeft();
//                    int right = childAt.getRight();
//                    int top = childAt.getTop();
//                    int bottom = childAt.getBottom();
//
//                    float rightX = childAt.getLeft() + childAt.getMeasuredWidth();
//                    float bottomY = childAt.getTop() + childAt.getMeasuredHeight();
//
//
//
//                    if (parent.getChildAdapterPosition(childAt)%5 != 0) {
//                        c.drawLine(rightX, childAt.getTop(), rightX + 1, bottomY, paint);
//                    }
//
//                    int row = parent.getChildAdapterPosition(childAt) / 5;
//                    if (row < parent.getChildCount() / 5) {
//
//                        c.drawLine(childAt.getLeft(), bottomY, rightX, bottomY + 1, paint);
//                    }
//
//                }
//            }
//        });


        adapter = new CollectAdapter(getContext());
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
        getCollectedList();

        initDelete();
    }

    private void initDelete() {
        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            adapter.selectAll(isChecked);
        });
        btnDelete.setOnClickListener(v -> {
            List<CollectedListBean.DataBean> selectedItems = adapter.getSelectedItems();
            if(ListUtil.isEmptyList(selectedItems)){
                toast("您没有选择任何表情！");
            }else {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < selectedItems.size(); i++) {
                    CollectedListBean.DataBean dataBean = selectedItems.get(i);
                    int browuserid = dataBean.getBrowuserid();
                    sb.append(browuserid);
                    if(i!=selectedItems.size()-1){
                        sb.append(",");
                    }
                }
                deleteCollectedEmtoions(sb.toString());

            }
        });

    }

    /**
     * 删除收藏的表情
     * @param string
     */
    private void deleteCollectedEmtoions(String string) {
        HashMap<String, String> params = new HashMap<>();
        params.put("ides",string);
        params.put(Constants.token,MyApp.getInstance().getUserTicket());
        showProgressDialog("删除中...",false);
        MyPresenter.request(getContext(), UrlString.URL_DELETE_COLLECTED_EMOTIONS, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                toast("删除成功!");
                EmotionManager.getInstance().notifyEmotionChange();
                getCollectedList();

            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);
            }
        });


    }


    /*token	是	String	用户token
     currentPage	否	int	当前页，默认为1
     pageSize	否	int	每页记录数，默认为10*/

    private void getCollectedList() {

        stateLayout.showLoadView();
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.pageSize, "1000");
        params.put(XNConstants.currentPage, "1");
        log(params.toString());
        RemoteDataHandler.asyncPost(UrlString.URL_COLLECTED_EMOTION_LIST, params, getContext(), true, response -> {
            if (stateLayout != null) {
                String json = response.getJson();
                log(json);
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    CollectedListBean collectedListBean = new Gson().fromJson(json, CollectedListBean.class);
                    if (collectedListBean.getStatus() == 1) {
                        adapter.replaceAll(collectedListBean.getData());
                    } else {
                        toast(collectedListBean.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void mode(boolean isEdit) {
        rlEditLayout.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        adapter.setEdit(isEdit);
    }

    private class CollectAdapter extends MyRvAdapter<CollectedListBean.DataBean> {

        private SparseArray<Boolean> selectedPoiArray = new SparseArray<>();
        private boolean isEdit = false;

        @Override
        public void replaceAll(List<CollectedListBean.DataBean> newDataList) {
            selectedPoiArray = new SparseArray<>();
            super.replaceAll(newDataList);
        }

        public void selectAll(boolean flag) {
            for (int i = 0; i < getItemCount(); i++) {
                selectedPoiArray.put(i, flag);
            }
            notifyDataSetChanged();
        }

        public CollectAdapter(Context context) {
            super(context, R.layout.item_emtion_edit);
        }

        /**
         * 切换编辑模式
         *
         * @param flag
         */
        public void setEdit(boolean flag) {
            if (flag) {
                isEdit = true;
                notifyDataSetChanged();
            } else {
                isEdit = false;
                selectAll(false);
                notifyDataSetChanged();
            }
        }

        public List<CollectedListBean.DataBean> getSelectedItems(){
            List<CollectedListBean.DataBean> list = new ArrayList<>();

            for (int i = 0; i < selectedPoiArray.size(); i++) {
                int position = selectedPoiArray.keyAt(i);
                boolean isSelected = selectedPoiArray.get(position)==null?false:selectedPoiArray.get(position);
                if(isSelected){
                    list.add(getItem(position));
                }
            }

            return list;
        }


        @Override
        protected void bindItemView(MyRvViewHolder holder, int position, CollectedListBean.DataBean data) {
            CheckBox cb = holder.getView(R.id.cb_select);
            cb.setVisibility(isEdit ? View.VISIBLE : View.GONE);
            //是否被选中
            boolean isSelected = selectedPoiArray.get(position) == null ? false : selectedPoiArray.get(position);
            if (isEdit) {
                cb.setChecked(isSelected);
                //选中监听
                cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    selectedPoiArray.put(position, isChecked);
                });
            }

            ImageView iv = holder.getView(R.id.iv_emotion_icon);
            String browinfo = data.getBrowinfo();
            Glide.with(context).load(browinfo).placeholder(com.hyphenate.easeui.R.drawable.ease_default_image)
                    .into(iv);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
