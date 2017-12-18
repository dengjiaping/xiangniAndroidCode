package com.ixiangni.app.orders;

import android.support.v7.widget.RecyclerView;

import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class OrderHelper implements SmartPullableLayout.OnPullListener {

    private SmartPullableLayout smartPullableLayout;
    private StateLayout stateLayout;
    private RecyclerView.Adapter adapter;

    private int currentPage =1;
    private int pageSize = 20;



    public OrderHelper(SmartPullableLayout smartPullableLayout, StateLayout stateLayout, RecyclerView.Adapter adapter) {
        this.smartPullableLayout = smartPullableLayout;
        this.stateLayout = stateLayout;
        this.adapter = adapter;

    }

    public void init(){
        stateLayout.setUpwihtRecyclerAdapter(adapter,"暂无订单");
        smartPullableLayout.setOnPullListener(this);
        onPullDown();

    }
    public void init(String noDatamsg){
        stateLayout.setUpwihtRecyclerAdapter(adapter,noDatamsg);
        smartPullableLayout.setOnPullListener(this);
        onPullDown();

    }

    @Override
    public void onPullDown() {
        currentPage=1;
        if(onCurrentPageChange!=null){
            onCurrentPageChange.onChange(currentPage);
        }
    }

    @Override
    public void onPullUp() {

        currentPage++;
        if(onCurrentPageChange!=null){
            onCurrentPageChange.onChange(currentPage);
        }
    }

    public interface OnCurrentPageChange{
        void onChange(int currentPage);
    }

    private OnCurrentPageChange onCurrentPageChange;

    public OnCurrentPageChange getOnCurrentPageChange() {
        return onCurrentPageChange;
    }

    public void setOnCurrentPageChange(OnCurrentPageChange onCurrentPageChange) {
        this.onCurrentPageChange = onCurrentPageChange;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 若返回数据条数小于pagesiz，则没有更多了
     * @param count
     */
    public void loadDataFinish(int count){
        smartPullableLayout.stopPullBehavior();
        if(count<pageSize){
            smartPullableLayout.setPullUpEnabled(false);
        }else {
            smartPullableLayout.setPullUpEnabled(true);
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
