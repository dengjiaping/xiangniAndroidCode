package com.ixiangni.app.mine;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.handongkeji.interfaces.OnResult;
import com.ixiangni.adapter.FilePicAdapter;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseFragment;
import com.ixiangni.bean.FileListBean;
import com.ixiangni.presenters.FileListPresenter;
import com.ixiangni.presenters.contract.FileGetDataState;
import com.ixiangni.presenters.contract.FileRefreshState;
import com.ixiangni.util.SmartPullableLayout;
import com.ixiangni.util.StateLayout;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PictureFragment extends BaseFragment {

    @Bind(R.id.smart_pull_layout)
    SmartPullableLayout smart_pull_layout;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.state_layout)
    StateLayout state_layout;
    List<FileListBean.DataBean>allMlist;
    private FilePicAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, view);
        allMlist=new ArrayList<>();
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter=new FilePicAdapter(getActivity(),R.layout.file_pic_layout);
        recycler_view.setAdapter(adapter);
        return view;
    }




}
