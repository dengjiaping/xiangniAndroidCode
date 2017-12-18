package com.ixiangni.app.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 投诉
 *
 * @ClassName:TousuActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/7/28 0028   09:50
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/28 0028 handongkeji All rights reserved.
 */
public class TousuActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.edt_content)
    EditText edtContent;
    @Bind(R.id.btn_complain)
    Button btnComplain;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tousu);
        ButterKnife.bind(this);

        userid = getIntent().getStringExtra(XNConstants.USERID);
        btnComplain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String content = edtContent.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            toast("请输入投诉内容！");
        }else if(content.length()>500){
            toast("投诉内容不能超过500字！");
        }else {
            complain(content);
        }

    }

    /**
     * 投诉
     * @param content
     */
    private void complain(String content) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("id",userid);
        params.put("Content",content);

        showProgressDialog("投诉中...",true);
        MyPresenter.request(this, UrlString.URL_COMPLAIN, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                if(btnComplain!=null){
                    dismissProgressDialog();
                    toast("投诉成功");
                    finish();
                }

            }

            @Override
            public void onFailed(String errorMsg) {
                if(btnComplain!=null){
                    dismissProgressDialog();
                    toast(errorMsg);
                }

            }
        });
    }
}
