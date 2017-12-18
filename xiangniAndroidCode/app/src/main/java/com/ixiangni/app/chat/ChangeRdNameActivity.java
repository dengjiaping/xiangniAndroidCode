package com.ixiangni.app.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.homepage.MessageListFragment;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.util.NotifyHelper;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改备注名
 *
 * @ClassName:ChangeRdNameActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/7/28 0028   16:32
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/28 0028 handongkeji All rights reserved.
 */
public class ChangeRdNameActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.edt_name)
    EditText edtName;
    @Bind(R.id.btn_save)
    Button btnSave;
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_rd_name);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        userid = intent.getStringExtra(XNConstants.USERID);
        String remindName = intent.getStringExtra(XNConstants.REMIND_NAME);
        if(!CommonUtils.isStringNull(remindName)){
            edtName.setText(remindName);
            edtName.setSelection(remindName.length());
        }


        btnSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String name = edtName.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            toast("请输入备注名！");
            return;
        }

        saveName(name);
    }

//    token	是	String	用户token
//    friendid	是	Long	好友id
//    remind	是	String	备注名

    private void saveName(final String name) {


        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("friendid",userid);
        params.put("remind",name);

        showProgressDialog("保存中...",false);
        MyPresenter.request(this, UrlString.URL_SET_REMIND_NAME, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                if(btnSave!=null){
                    dismissProgressDialog();
                    toast("保存成功");



                    String hxid = "ixn"+userid;
                    EaseUser userInfo = DemoHelper.getInstance().getUserInfo(hxid);
                    if(userInfo==null){
                        userInfo=new EaseUser(hxid);
                    }
                    String maskRemindName = CommonUtils.getMaskRemindName(name);
                    Log.i("nickname", "maskRemindName: "+maskRemindName);
                    userInfo.setNickname(maskRemindName);
                    DemoHelper.getInstance().saveContact(userInfo);

                    NotifyHelper.notifyContactList();
                    if(MessageListFragment.instance!=null){
                        MessageListFragment.instance.refresh();
                    }

                    Intent intent = new Intent();
                    intent.putExtra(XNConstants.REMIND_NAME,name);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if(btnSave!=null){
                    dismissProgressDialog();
                    toast(errorMsg);
                }
            }
        });
    }
}
