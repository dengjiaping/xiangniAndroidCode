package com.ixiangni.app.chat;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.ui.TopBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchGroupActivity extends BaseActivity {

    @Bind(R.id.edt_group_name)
    EditText edtGroupName;
    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.tv_no_data_message)
    TextView tvNoDataMessage;
    @Bind(R.id.rl_no_data)
    RelativeLayout rlNoData;
    @Bind(R.id.list_view)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);
        ButterKnife.bind(this);


    }
}
