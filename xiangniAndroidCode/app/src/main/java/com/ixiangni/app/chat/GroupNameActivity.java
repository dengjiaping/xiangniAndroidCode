package com.ixiangni.app.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.handongkeji.interfaces.OnResult;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.presenters.GroupSetPresenter;
import com.ixiangni.util.NotifyHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 修改群名称
 *
 * @ClassName:GroupNameActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/7/5 0005   17:22
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/5 0005 handongkeji All rights reserved.
 */
public class GroupNameActivity extends BaseActivity {

    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.edt_group_name)
    EditText edtGroupName;
    private String groupno;
    private GroupSetPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_name);
        ButterKnife.bind(this);


        initView();
    }

    private void initView() {

        groupno = getIntent().getStringExtra(XNConstants.groupchatno);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupno);
        if (group != null) {
            String groupName = group.getGroupName();
            edtGroupName.setText(groupName);
            if (!TextUtils.isEmpty(groupName)) {
                edtGroupName.requestFocus();
                edtGroupName.setSelection(groupName.length());
            }

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideSoftKeyboard();
                    submit();
                }
            });

            mPresenter = new GroupSetPresenter();

        } else {
            toast("未找到该群");
        }





    }

    private void submit() {
        String groupName = edtGroupName.getText().toString().trim();

        if(TextUtils.isEmpty(groupName)){

            toast("请填写群名称！");
        }else {

            showProgressDialog("修改中...",false);

            EMClient.getInstance().groupManager().asyncChangeGroupName(groupno, groupName, new EMCallBack() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeGroupname(groupName);
                        }
                    });
                }

                @Override
                public void onError(int i, String s) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            toast(s);
                    dismissProgressDialog();
                        }
                    });
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });



        }


    }

    private void changeGroupname(final String groupName) {
        mPresenter.modify(this, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                toast("修改成功！");
                //通知观察者改变群名称
                EaseUser user = new EaseUser(groupno);
                user.setNickname(groupName);

                DemoHelper.getInstance().saveContact(user);

                NotifyHelper.notifyGroupChange(groupno,groupName);


                Intent intent = new Intent();
                intent.putExtra(XNConstants.groupname,groupName);
                setResult(RESULT_OK,intent);

                Intent intent1 = new Intent(EaseConstant.get_group_info);
                intent1.putExtra(EaseConstant.EXTRA_USER_ID,groupno);
                sendBroadcast(intent1);

                finish();
            }

            @Override
            public void onFailed(String errorMsg) {

                dismissProgressDialog();
                toast(errorMsg);
            }
        },groupno,groupName,null,null,null);
    }


    public static void startForResult(Activity activity, String groupno, int requestCode) {


        Intent intent = new Intent(activity, GroupNameActivity.class);
        intent.putExtra(XNConstants.groupchatno, groupno);
        activity.startActivityForResult(intent, requestCode);
    }

}
