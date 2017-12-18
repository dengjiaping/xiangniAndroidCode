package com.ixiangni.app.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.ui.TopBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAddressActivity extends BaseActivity {

    @Bind(R.id.edt_address)
    EditText edtAddress;
    @Bind(R.id.btn_save)
    Button btnSave;
    @Bind(R.id.top_bar)
    TopBar topBar;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        title = intent.getStringExtra("title");

        if (!TextUtils.isEmpty(content)) {
            edtAddress.setText(content);
            edtAddress.setSelection(content.length());
        }
        if (!TextUtils.isEmpty(title)) {
            topBar.setCenterText(title);
        }

        if("我的地址".equals(title)){
            edtAddress.setHint("请输入地址");
        }else {
            edtAddress.setHint("请输入"+ title);
        }

        if("姓名".equals(title)){

        }

    }


    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        String address = edtAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            toast("内容不能为空！");
            return;
        }
        if("姓名".equals(title)&&address.length()>10){
            toast("字符长度不能超过10位！");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("content", address);
        setResult(RESULT_OK, intent);
        finish();
    }
}
