package com.ixiangni.app.money;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easemob.redpacketsdk.bean.RedPacketInfo;
import com.easemob.redpacketsdk.bean.TokenData;
import com.easemob.redpacketsdk.constant.RPConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.ICallback;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.easeui.domain.EaseUser;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.login.LoginHelper;
import com.ixiangni.app.setting.HelpCenterActivity;
import com.ixiangni.bean.BaseBean;
import com.ixiangni.bean.GroupMLBean;
import com.ixiangni.bean.RedPacketBean;
import com.ixiangni.bean.UsableCoinBean;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.Paydialog;
import com.ixiangni.presenters.GetTextPresenter;
import com.ixiangni.presenters.contract.GroupMemberPresenter;
import com.ixiangni.ui.TopBar;
import com.ixiangni.util.GsonUtils;
import com.nevermore.oceans.utils.ListUtil;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GroupRPActivity extends BaseActivity {

    @Bind(R.id.top_bar)
    TopBar topBar;
    @Bind(R.id.edt_total_money)
    EditText edtTotalMoney;
    @Bind(R.id.tv_current_rp)
    TextView tvCurrentRp;
    @Bind(R.id.tv_change_to)
    TextView tvChangeTo;
    @Bind(R.id.edt_rp_count)
    EditText edtRpCount;
    @Bind(R.id.tv_member_count)
    TextView tvMemberCount;
    @Bind(R.id.edt_leave_message)
    EditText edtLeaveMessage;
    @Bind(R.id.tv_usable_coin)
    TextView tvUsableCoin;
    @Bind(R.id.btn_ok)
    Button btnOk;
    private HashMap<String, String> params = new HashMap<>();
    private int redPacketType =2;          //  红包类型0:单个1:多个平均分2:多个随机分
    private String redPacketNumber = "1";   //  红包数量 ，单聊时默认为1

    private String currentRP1="当前为拼手气红包,";
    private String currentRP2 ="当前为普通红包,";

    private String changTo1 ="改为普通红包";
    private String changTo2 ="改为拼手气红包";


    private int RP_type = 0;//0凭手气,1普通
    private RedPacketInfo redPacketInfo;
    private int memberCount = -1;
    private Paydialog paydialog;
    private double usableCoin =-1;
    private String money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_rp);
        ButterKnife.bind(this);


        check();
        initData();
        init();
    }

    /**
     * 检查
     */
    private void check() {
        paydialog = new Paydialog(this);
//        showProgressDialog("加载中...",true);
        paydialog.checkSetPayPassword(this, btnOk, new ICallback<String>() {
            @Override
            public void call(String s) {
//                if(btnOk!=null){
//                    dismissProgressDialog();
//                }
            }
        });




    }

    private void initData() {
        Intent intent = getIntent();
        redPacketInfo = intent.getParcelableExtra(RPConstant.EXTRA_RED_PACKET_INFO);
        TokenData tokenData = intent.getParcelableExtra(RPConstant.EXTRA_TOKEN_DATA);
        getGroupMember(redPacketInfo.toGroupId);
        getUsableCoin();
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

    private void getGroupMember(String toGroupId) {


        GroupMemberPresenter groupMemberPresenter = new GroupMemberPresenter();
        groupMemberPresenter.getMembers(this, toGroupId, new OnResult<List<GroupMLBean.DataBean>>() {
            @Override
            public void onSuccess(List<GroupMLBean.DataBean> dataBeen) {
                if (btnOk != null) {
                    //群成员数量
                    memberCount = 0;
                    if (!ListUtil.isEmptyList(dataBeen)) {
                        memberCount = dataBeen.size();
                    }

                    tvMemberCount.setText(String.format(Locale.CHINA,"本群共%d人", memberCount));

                }
            }

            @Override
            public void onFailed(String errorMsg) {
                if (btnOk != null) {

                    toast(errorMsg);
                }
            }
        });
    }

    private void init() {


        CommonUtils.setPoint(edtTotalMoney,2);

        tvChangeTo.setOnClickListener(v -> {
            if(RP_type==0){
                tvCurrentRp.setText(currentRP1);
                tvChangeTo.setText(changTo1);
                edtTotalMoney.setHint("单个金额");
                RP_type=1;
            }else {
                edtTotalMoney.setHint("总金额");

                tvCurrentRp.setText(currentRP2);
                tvChangeTo.setText(changTo2);
                RP_type=0;
            }
        });

        topBar.setOnRightClickListener(v -> {
            Intent intent = new Intent(this, HelpCenterActivity.class);
            intent.putExtra("title", "支付说明");
            intent.putExtra(XNConstants.textflag, GetTextPresenter.TEXT_PAYMENT_INTRODUCE);
            startActivity(intent);
        });
        btnOk.setOnClickListener(v -> {
            checkParams();
        });
    }

    private void checkParams() {

        final String leaveMessage = edtLeaveMessage.getText().toString().trim();
        final String rpCount = edtRpCount.getText().toString().trim();
        money = edtTotalMoney.getText().toString().trim();


        if(TextUtils.isEmpty(rpCount)){
            toast("请输入红包个数");
            return;
        }
        int count = Integer.parseInt(rpCount);
        if(count>memberCount){
            toast("红包个数不能大于群人数");
            return;
        }
        if(count<=0){
            toast("红包个数必须大于0");
            return;
        }
        if(TextUtils.isEmpty(money)){
            toast("请输入红包金额");
            return;
        }
        double v = Double.parseDouble(money);
        if(v<=0){
            toast("红包金额必须大于0");
            return;
        }
        if(v>200){
            toast("红包金额最多为200");
            return;
        }

        if(RP_type==1){
            double v1 = count * v;
            if(v1>usableCoin&&usableCoin!=-1){
                toast("银信币不足");
                return;
            }
            money = "" + v1;
        }else {
            if(v>usableCoin&&usableCoin!=-1){
                toast("银信币不足");
                return;
            }
        }


//        if(TextUtils.isEmpty(leaveMessage)){
//            toast("请输入留言");
//            return;
//        }

        paydialog.show();
        paydialog.setPasswordCallback(new ICallback<String>() {
            @Override
            public void call(String s) {
                params.put("token", MyApp.getInstance().getUserTicket());


                params.put("moneynum", money);
                params.put("lucktype", RP_type==0?"2":"1");
                params.put("number",rpCount);
                params.put("chattype","1");
                params.put("friendid", redPacketInfo.toGroupId);   //  对方id 或者当前群Id
                params.put("message", leaveMessage);
                params.put("paypassword",s);

                sendRedPacket();

            }
        });


    }

    private void sendRedPacket() {

        String url = UrlString.URL_SEND_REDPACKET;
        showProgressDialog("发送中",true);
        RemoteDataHandler.asyncTokenPost(url, this, false, params, data -> {
            if(btnOk==null){
                return;
            }
            dismissProgressDialog();
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

                redPacketInfo.redPacketGreeting = params.get("message");
                redPacketInfo.redPacketId=luckmoneyid+"";

                EaseUser currentUserInfo = DemoHelper.getInstance().getUserProfileManager().getCurrentUserInfo();

                redPacketInfo.fromUserId= currentUserInfo.getUsername();
                redPacketInfo.fromAvatarUrl= currentUserInfo.getAvatar();

                redPacketInfo.toUserId=params.get("friendid");
                redPacketInfo.redPacketType=RP_type==0?"2":"1";
//                intent.putExtra(RPConstant.EXTRA_RED_PACKET_GREETING,params.get("message"));
//                intent.putExtra(RPConstant.EXTRA_RED_PACKET_ID,luckmoneyid+"");
//                intent.putExtra(RPConstant.EXTRA_RED_PACKET_RECEIVER_ID,info.toUserId);
//                intent.putExtra(RPConstant.EXTRA_RED_PACKET_TYPE,""+redPacketType);

                intent.putExtra(RPConstant.EXTRA_RED_PACKET_INFO,redPacketInfo);
                setResult(Activity.RESULT_OK,intent);
                onBackPressed();
            }else {
                toast(baseBean.getMessage());
            }
        });
    }


}
