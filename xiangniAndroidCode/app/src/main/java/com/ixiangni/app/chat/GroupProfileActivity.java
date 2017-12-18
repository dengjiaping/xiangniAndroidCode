package com.ixiangni.app.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.handongkeji.interfaces.OnResult;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.presenters.GroupSetPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 群简介
 *
 * @ClassName:GroupProfileActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/6/19 0019   11:12
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/19 0019 handongkeji All rights reserved.
 */
public class GroupProfileActivity extends BaseActivity {

    @Bind(R.id.btn_submit)
    Button btnSubmit;
    @Bind(R.id.edt_group_profile)
    EditText edtGroupProfile;
    private String groupno;
    private GroupSetPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {

        groupno = getIntent().getStringExtra(XNConstants.groupchatno);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupno);
        if (group != null) {

            //群简介
            String groupName = group.getDescription();
            edtGroupProfile.setText(groupName);
            if (!TextUtils.isEmpty(groupName)) {
                edtGroupProfile.requestFocus();
                edtGroupProfile.setSelection(groupName.length());
            }

            btnSubmit.setOnClickListener(v -> {
                hideSoftKeyboard();
                submit();
            });

            mPresenter = new GroupSetPresenter();

        } else {
            toast("未找到该群");
        }

    }

    private void submit() {
        String groubDescribe = edtGroupProfile.getText().toString().trim();
        if(TextUtils.isEmpty(groubDescribe)){
            toast("请填写群简介!");
        }else {
            showProgressDialog("修改中...",false);
            EMClient.getInstance().groupManager().asyncChangeGroupDescription(groupno, groubDescribe, new EMCallBack() {
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            saveDescribeOnserver(groubDescribe);
                        }
                    });

                }

                @Override
                public void onError(int i, String s) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgressDialog();
                            toast(s);
                        }
                    });
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }


    }

    /**
     * 保存群简介到服务器
     * @param groubDescribe
     */
    private void saveDescribeOnserver(String groubDescribe) {

        mPresenter.modify(this, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                toast("修改成功!");
                Intent intent = new Intent();
                intent.putExtra(XNConstants.groupdescirbe,groubDescribe);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void onFailed(String errorMsg) {
                toast(errorMsg);

            }
        },groupno,null,groubDescribe,null,null);
    }


    public static void startForResult(Activity activity, String groupno, int requestCode) {

        Intent intent = new Intent(activity, GroupProfileActivity.class);
        intent.putExtra(XNConstants.groupchatno, groupno);
        activity.startActivityForResult(intent, requestCode);
    }
}
