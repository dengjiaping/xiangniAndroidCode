package com.ixiangni.app.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.impactlib.dialog.XDialog;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.mine.MyRedPackageUtil;
import com.ixiangni.app.user.ModifyPasswordActivity;
import com.ixiangni.bean.BaseBean;
import com.ixiangni.bean.PassSetBean;
import com.ixiangni.bean.RedPacketBean;
import com.ixiangni.bean.UsableCoinBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.util.GsonUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 发红包
 *
 * @ClassName:RedPackageActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/22 0022   15:58
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class RedPackageActivity extends BaseActivity implements View.OnClickListener{

    @Bind(R.id.edt_money)
    EditText edtMoney;
    @Bind(R.id.edt_extra_message)
    EditText edtExtraMessage;
    @Bind(R.id.tv_usable_coin)
    TextView tvUsableCoin;
    @Bind(R.id.btn_send)
    Button btnSend;
    private RedPacketInfo info;
    private double usableCoin;
    private String message;
    private XDialog dialog;


    private int redPacketType = 0;          //  红包类型0:单个1:多个平均分2:多个随机分
    private String redPacketNumber = "1";   //  红包数量 ，单聊时默认为1

    private HashMap<String, String> params = new HashMap<>();
    private SpannableString ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_package);
        ButterKnife.bind(this);
        CommonUtils.setPoint(edtMoney,2);

        info = getIntent().getParcelableExtra(RPConstant.EXTRA_RED_PACKET_INFO);
        getUsableCoin();
        checkSetPayPassword();
        btnSend.setOnClickListener(this);
    }

    private void checkSetPayPassword() {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        RemoteDataHandler.asyncPost(UrlString.URL_CHECK_PAY_SET,params,this,false, response -> {
            String json = response.getJson();
            log(json);
            if(btnSend!=null){
                if(TextUtils.isEmpty(json)){
                    toast(Constants.CONNECT_SERVER_FAILED);
                }else {
                    PassSetBean passSetBean = new Gson().fromJson(json, PassSetBean.class);
                    if(1==passSetBean.getStatus()){
                    int data = passSetBean.getData();
                        if(0==data){
                            MyAlertdialog alertdialog = new MyAlertdialog(RedPackageActivity.this);
                            alertdialog.setCancelable(false);
                            alertdialog.setMessage("您还未设置支付密码，\n是否去设置？")
                                    .setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                                        @Override
                                        public void onLeftClick(View view) {
                                            finish();
                                        }

                                        @Override
                                        public void onRightClick(View view) {

                                            Intent intent = new Intent(RedPackageActivity.this, ModifyPasswordActivity.class);
                                            intent.putExtra(ModifyPasswordActivity.isLoginPassword,false);
                                            startActivity(intent);

                                        }
                                    });

                            alertdialog.show();
                        }

                    }else {
                        toast(passSetBean.getMessage());
                    }

                }


            }

        });

    }

    private void getUsableCoin() {
        //可用余额查询
        showProgressDialog("加载中...",true);
        HashMap<String, String> params = new HashMap<>();
        params.put("token", LoginHelper.getInstance().getToken(this));
        RemoteDataHandler.asyncPost(UrlString.URL_AVAILABLE_MONEY, params, this, false, callback -> {

            String json = callback.getJson();
            if (mContext != null) {
                dismissProgressDialog();
                if (CommonUtils.isStringNull(json)) {
                    toast(Constants.CONNECT_SERVER_FAILED);
                } else {
                    UsableCoinBean usableCoinBean = new Gson().fromJson(json, UsableCoinBean.class);
                    if (1 == usableCoinBean.getStatus()) {
                        usableCoin = usableCoinBean.getData();
                        String str = "可用银信币:" + String.format(Locale.CHINA,"%.2f",usableCoinBean.getData());
                        tvUsableCoin.setText(str);
                    } else {
                        tvUsableCoin.setText("可用银信币:0.0");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        String money = edtMoney.getText().toString().trim();
        if(TextUtils.isEmpty(money)){
            toast("请输入红包金额！");
            return;
        }

        double v1 = Double.parseDouble(money);
        if(v1<=0){
            toast("红包金额必须大于0");
            return;
        }
        if(v1>200){
            toast("红包金额最多为200");
            return;
        }
        if(v1>usableCoin){
            toast("超出可用银信币");
            return;
        }
        message = edtExtraMessage.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            message = XNConstants.defaultMessage;
        }

        int chatType = info.chatType;

        dialog = new XDialog(this, R.layout.layout_mima);
        EditText editText = (EditText) dialog.findViewById(R.id.edt_password);
        dialog.setOnClickListener(R.id.tv_left, v2 -> {
            dialog.dismiss();
        });

        dialog.setOnClickListener(R.id.tv_right, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editText.getText().toString().trim();
                if(TextUtils.isEmpty(password)){
                    toast("请输入支付密码！");
                    return;
                }

                //验证支付密码
                showProgressDialog("验证密码中...",false);
                HashMap<String, String> params = new HashMap<>();
                params.put("token",MyApp.getInstance().getUserTicket());
                params.put("paymentPass",password);

                MyPresenter.request(RedPackageActivity.this, UrlString.URL_CHEK_PAYPASWORD, params, new OnResult<String>() {
                    @Override
                    public void onSuccess(String s) {
                        dialog.dismiss();
                        mProgressDialog.setMessage("发送中...");
                        sendRedPacket();
                    }

                    @Override
                    public void onFailed(String errorMsg) {

                        dismissProgressDialog();
                        toast(errorMsg);
                    }
                });
            }
        });

        dialog.show();
    }

    private boolean checkParams() {
        String totalMoney = edtMoney.getText().toString().trim(); //  红包金额
        String message = edtExtraMessage.getText().toString().trim();      // 留言
//        redPacketNumber = redNumber.getText().toString().trim();

        if (CommonUtils.isStringNull(totalMoney)) {
            return false;
        }

        if (!(info.chatType == EaseConstant.CHATTYPE_SINGLE) && CommonUtils.isStringNull(redPacketNumber)) {
            return false;
        }


        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("moneynum", totalMoney);     //  总钱数
        params.put("lucktype", redPacketType + "");   //  红包类型

        if (info.chatType == EaseConstant.CHATTYPE_SINGLE) {
            redPacketNumber = "1";
        }

        params.put("number", redPacketNumber);     //  红包个数
        params.put("chattype", (info.chatType == EaseConstant.CHATTYPE_SINGLE ? "0" : "1"));  //  聊天类型

        String friendid = null;
        if (info.chatType == RPConstant.CHATTYPE_SINGLE) {
            friendid = info.toUserId.replace("ixn", "");
        } else {
            friendid = info.toGroupId;
        }
        params.put("friendid", friendid);   //  对方id 或者当前群Id
        params.put("message", message);    //  发送的消息

        return true;
    }



    private void sendRedPacket() {
        checkParams();

        String url = UrlString.URL_SEND_REDPACKET;
        RemoteDataHandler.asyncTokenPost(url, this, false, params, data -> {
            String json = data.getJson();
            if (CommonUtils.isStringNull(json)) {
                toast("服务器错误，无法连接到服务器");
                return;
            }
            Type type = new TypeToken<BaseBean<RedPacketBean>>(){}.getType();
//            Log.d("aaa", "sendRedPacket: " + json);
            BaseBean<RedPacketBean> baseBean = (BaseBean<RedPacketBean>) GsonUtils.fromJson(json,type);
            if (baseBean.getStatus() == 1){
                int luckmoneyid = baseBean.getData().getLuckmoneyid();  //  红包id;
                Intent intent = new Intent();

                info.redPacketGreeting = params.get("message");
                info.redPacketId=luckmoneyid+"";

                EaseUser currentUserInfo = DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo();

                info.fromUserId= currentUserInfo.getUsername();
                info.fromAvatarUrl= currentUserInfo.getAvatar();

                info.toUserId=params.get("friendid");
                info.redPacketType=""+redPacketType;

//                intent.putExtra(RPConstant.EXTRA_RED_PACKET_GREETING,params.get("message"));
//                intent.putExtra(RPConstant.EXTRA_RED_PACKET_ID,luckmoneyid+"");
//                intent.putExtra(RPConstant.EXTRA_RED_PACKET_RECEIVER_ID,info.toUserId);
//                intent.putExtra(RPConstant.EXTRA_RED_PACKET_TYPE,""+redPacketType);

                intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO,info);
                setResult(Activity.RESULT_OK,intent);

                RedPackageActivity.this.finish();
            }
        });
    }


}
