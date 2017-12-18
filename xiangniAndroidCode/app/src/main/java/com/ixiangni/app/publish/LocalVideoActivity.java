package com.ixiangni.app.publish;

import android.os.Bundle;
import android.widget.GridView;

import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocalVideoActivity extends BaseActivity {

    @Bind(R.id.gridview)
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_video);
        ButterKnife.bind(this);
    }
}
