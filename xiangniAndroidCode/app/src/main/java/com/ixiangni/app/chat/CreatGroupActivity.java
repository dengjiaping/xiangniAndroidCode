package com.ixiangni.app.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.modle.ResponseData;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.constants.UrlString;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建群
 *
 * @ClassName:CreatGroupActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/6/20 0020   13:03
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class CreatGroupActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.edt_group_name)
    EditText edtGroupName;
    @Bind(R.id.edt_group_profile)
    EditText edtGroupProfile;
    @Bind(R.id.btn_create)
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_group);
        ButterKnife.bind(this);



        btnCreate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard();
        String groupName = edtGroupName.getText().toString().trim();
        String desc = edtGroupProfile.getText().toString().trim();
        if (TextUtils.isEmpty(groupName)) {
            toast("请填写群名称！");
        }
        String[] me = new String[]{};
        buildGroupFromHuanXin(groupName, desc, me, "");
    }

    /**
     * 请求环信 新建群
     *
     * @param groupName
     * @param desc
     * @param members
     * @param reason
     */
    /**
     * 创建群组
     *
     * @param groupName 群组名称
     * @param desc      群组简介
     * @param reason    邀请成员加入的reason
     * @return 创建好的group
     * @throws HyphenateException EMGroupStylePrivateOnlyOwnerInvite——私有群，只有群主可以邀请人；
     *                            EMGroupStylePrivateMemberCanInvite——私有群，群成员也能邀请人进群；
     *                            EMGroupStylePublicJoinNeedApproval——公开群，加入此群除了群主邀请，只能通过申请加入此群；
     *                            EMGroupStylePublicOpenJoin ——公开群，任何人都能加入此群。
     * @ allMembers 群组初始成员，如果只有自己传空数组即可
     * @ option     群组类型选项，可以设置群组最大用户数(默认200)及群组类型@see
     */
    private void buildGroupFromHuanXin(String groupName, String desc, String[] members, String reason) {


        showProgressDialog("创建中...", false);
        EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
        option.maxUsers = 1000;
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;

        EMClient.getInstance().groupManager().asyncCreateGroup(
                groupName,
                desc,
                new String[]{LoginHelper.getInstance().getHuanxinid(this)},
                "join the group", option,
                new EMValueCallBack<EMGroup>() {

                    @Override
                    public void onSuccess(EMGroup emGroup) {
                        Log.e("成功", "陈宫");

                        String groupId = emGroup.getGroupId();
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("token", MyApp.getInstance().getUserTicket());
                        params.put("groupchatname", groupName);
                        params.put("groupchatno", groupId);
                        if (!TextUtils.isEmpty(desc)) {
                            params.put("groupchatdesc", desc);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                buildGroup(params);
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
                });
    }

    /**
     * 请求服务器 创建群
     *
     * @param params
     */
    private void buildGroup(HashMap<String, String> params) {

        String url = UrlString.URL_BUILD_GROUPCHAT;
        RemoteDataHandler.asyncPost(url, params,mContext, false, new RemoteDataHandler.Callback() {

            @Override
            public void dataLoaded(ResponseData data) {

                dismissProgressDialog();
                String json = data.getJson();
                log("json:"+json);
                if (null != json && !json.equals("")) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        String status = obj.getString("status");
                        String message = obj.getString("message");
                        if (status.equals("1")) {
                            toast("创建成功！");
                            finish();
                        } else {
                            EMClient.getInstance().groupManager().asyncDestroyGroup(params.get("groupchatno"), new EMCallBack() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(int i, String s) {

                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                            toast("创建失败!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
