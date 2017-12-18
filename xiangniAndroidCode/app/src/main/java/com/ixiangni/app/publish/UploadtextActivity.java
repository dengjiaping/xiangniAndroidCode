package com.ixiangni.app.publish;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.handongkeji.interfaces.OnResult;
import com.handongkeji.upload.UpLoadPresenter;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.interfaces.OnRefreshFile;
import com.ixiangni.ui.TopBar;
import com.nevermore.oceans.ob.SuperObservableManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 上传文字
 *
 * @ClassName:UploadtextActivity
 * @PackageName:com.ixiangni.app.publish
 * @Create On 2017/6/21 0021   17:51
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/21 0021 handongkeji All rights reserved.
 */
public class UploadtextActivity extends BaseActivity implements View.OnClickListener, OnResult<String> {

    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.edt_content)
    EditText edtContent;
    private String folderid;
    private UploadPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadtext);
        ButterKnife.bind(this);
        topBar.setOnRightClickListener(this);
        folderid = getIntent().getStringExtra(XNConstants.folderid);
        presenter = new UploadPresenter();
    }

    @Override
    public void onClick(View v) {


        String content = edtContent.getText().toString().trim();
        if(TextUtils.isEmpty(content)){
            toast("内容不能为空！");
            return;
        }

        showProgressDialog("上传中...",true);
        presenter.upload(this,folderid,UploadPresenter.UP_TEXT,null,content,null,null,this);

    }

    @Override
    public void onSuccess(String s) {
        if(edtContent!=null){
            dismissProgressDialog();
            toast("上传成功！");
            SuperObservableManager
                    .getInstance()
                    .getObservable(OnRefreshFile.class)
                    .notifyMethod(onRefreshFile -> onRefreshFile.refreshFile(UploadPresenter.UP_TEXT));
            finish();
        }
    }

    @Override
    public void onFailed(String errorMsg) {

        if(edtContent!=null){
            dismissProgressDialog();
            toast(errorMsg);
        }
    }
}
