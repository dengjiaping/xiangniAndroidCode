package com.ixiangni.app.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handongkeji.common.Constants;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.ixiangni.app.MainActivity;
import com.ixiangni.app.MyApp;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.app.contactlist.BlackListActivity;
import com.ixiangni.common.XNConstants;
import com.ixiangni.constants.UrlString;
import com.ixiangni.dialog.MyAlertdialog;
import com.ixiangni.interfaces.OnDeleteContact;
import com.ixiangni.interfaces.OnRefreshContactList;
import com.ixiangni.presenters.BlackListPresenter;
import com.ixiangni.presenters.contract.ContactPresenter;
import com.ixiangni.presenters.contract.MyPresenter;
import com.ixiangni.util.HuanXinHelper;
import com.ixiangni.util.NotifyHelper;
import com.nevermore.oceans.ob.SuperObservableManager;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 资料设置
 *
 * @ClassName:DataSettingActivity
 * @PackageName:com.ixiangni.app.chat
 * @Create On 2017/6/20 0020   11:26
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/20 0020 handongkeji All rights reserved.
 */
public class DataSettingActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    @Bind(R.id.cb_bukanta)
    CheckBox cbBukanta;
    @Bind(R.id.cb_burangkan)
    CheckBox cbBurangkan;
    @Bind(R.id.cb_add_to_black_list)
    TextView cbAddToBlackList;
    @Bind(R.id.tv_complain)
    TextView tvComplain;
    @Bind(R.id.btn_delete_friend)
    Button btnDeleteFriend;
    @Bind(R.id.ll_only_firend)
    LinearLayout llOnlyFirend;
    @Bind(R.id.tv_remark_name)
    TextView tvRemarkName;
    private String userid;
    private BlackListPresenter mPresenter;
    private int isFriend;
    private ContactPresenter contactPresenter;

    private final int REQUEST_CODE_NAME = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_setting);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userid = intent.getStringExtra(XNConstants.USERID);
        isFriend = intent.getIntExtra(XNConstants.ISFRIEND, 0);
        int seehemoment = intent.getIntExtra(XNConstants.seehemoment, 0);
        int seememoment = intent.getIntExtra(XNConstants.seememoment, 0);

        cbBukanta.setChecked(seehemoment==1);
        cbBurangkan.setChecked(seememoment==1);

        cbBukanta.setOnCheckedChangeListener(this);
        cbBurangkan.setOnCheckedChangeListener(this);


        String remindName = intent.getStringExtra(XNConstants.REMIND_NAME);
        tvRemarkName.setText(CommonUtils.isStringNull(remindName)?"未填写":remindName);
        tvRemarkName.setOnClickListener(v -> {

            Intent intent1 = new Intent(DataSettingActivity.this,ChangeRdNameActivity.class);
            intent1.putExtra(XNConstants.USERID,userid);
            intent1.putExtra(XNConstants.REMIND_NAME,remindName);
            startActivityForResult(intent1,REQUEST_CODE_NAME);

        });

        initView();

        cbAddToBlackList.setOnClickListener(this);
        tvComplain.setOnClickListener(this);

        mPresenter = new BlackListPresenter();
        contactPresenter = new ContactPresenter();
    }

    private void initView() {
        //好友状态 0陌生人1好友2黑名单

        if (isFriend == 0) {
            llOnlyFirend.setVisibility(View.GONE);
            btnDeleteFriend.setVisibility(View.GONE);
        } else {
            btnDeleteFriend.setOnClickListener(this);
        }
        if (isFriend == 2) {
            cbAddToBlackList.setSelected(true);
        }


    }




    private void remBlackList() {
        Intent intent = new Intent(DataSettingActivity.this, BlackListActivity.class);
        intent.putExtra(XNConstants.USERID, userid);
        startActivityForResult(intent, REQUEST_CODE_REMOVE_BLACKLIST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_REMOVE_BLACKLIST) {
            cbAddToBlackList.setSelected(false);
            SuperObservableManager.notify(OnDeleteContact.class, OnDeleteContact::delete);
        }

        if(resultCode==RESULT_OK&&requestCode==REQUEST_CODE_NAME){
            SuperObservableManager.notify(OnDeleteContact.class, OnDeleteContact::delete);
            tvRemarkName.setText(data.getStringExtra(XNConstants.REMIND_NAME));
        }
    }

    private int REQUEST_CODE_REMOVE_BLACKLIST = 88;

    private void addToBlackList() {
        showProgressDialog("稍等...", false);
        mPresenter.addToBlackList(this, userid, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                try {
                    EMClient.getInstance().contactManager().addUserToBlackList(HuanXinHelper.getHuanXinidbyUseid(userid), true);
                    cbAddToBlackList.setSelected(true);
                    log("移除黑名单陈宫");

                } catch (HyphenateException e) {
                    log("移除黑名单失败");
                    e.printStackTrace();
                }
                toast(s);
                NotifyHelper.notifyContactList();
                SuperObservableManager.notify(OnDeleteContact.class, OnDeleteContact::delete);

            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);
            }
        });
    }

    private void removeFromBlackList(int blackListid, int friendid) {


        showProgressDialog("移除中...", false);
        mPresenter.deleteFromBlackList(this, blackListid, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {

                dismissProgressDialog();
                toast("移除成功!");
                try {
                    EMClient.getInstance().contactManager().removeUserFromBlackList(HuanXinHelper.getHuanXinidbyUseid(friendid + ""));
                    log("一处成功");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    log("一处shibai");

                }

                NotifyHelper.notifyContactList();
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete_friend:
//                showProgressDialog("删除中...", false);
                MyAlertdialog alertdialog = new MyAlertdialog(this);
                alertdialog.setMessage("确定要删除该好友?");
                alertdialog.setMyClickListener(new MyAlertdialog.OnMyClickListener() {
                    @Override
                    public void onLeftClick(View view) {

                    }

                    @Override
                    public void onRightClick(View view) {
                        deleteFriend();
                    }
                });
                alertdialog.show();


                break;
            case R.id.cb_add_to_black_list:
                if (cbAddToBlackList.isSelected()) {
                    remBlackList();
                } else {
                    addToBlackList();
                }

                break;
            case R.id.tv_complain://投诉
                Intent intent = new Intent(this, TousuActivity.class);
                intent.putExtra(XNConstants.USERID, userid);
                startActivity(intent);
                break;


        }
    }

    private void deleteFriend() {
        showProgressDialog("删除中...", false);
        contactPresenter.deleteFriend(this, userid, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                EMClient.getInstance().contactManager().aysncDeleteContact(XNConstants.ixn + userid, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        btnDeleteFriend.post(new Runnable() {
                            @Override
                            public void run() {
                                SuperObservableManager instance = SuperObservableManager.getInstance();

                                instance.getObservable(OnRefreshContactList.class)
                                        .notifyMethod(OnRefreshContactList::onRefresh);
                                instance.getObservable(OnDeleteContact.class)
                                        .notifyMethod(OnDeleteContact::delete);

                                dismissProgressDialog();
                                toast("删除成功");
                                startActivity(MainActivity.class);
                            }
                        });
                    }

                    @Override
                    public void onError(int i, String s) {

                        btnDeleteFriend.post(new Runnable() {
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

            @Override
            public void onFailed(String errorMsg) {

                dismissProgressDialog();
                toast(errorMsg);
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int seehemoment = cbBukanta.isChecked()?1:0;
        int seemehoment = cbBurangkan.isChecked()?1:0;
        savePermission(seehemoment,seemehoment);
    }



//    token	是	String	用户token
//    friendid	是	Long	好友id
//    seeHeMoment	否	Integer	不看他的想你圈0：看 1： 不看
//    seeMeMoment	否	Integer	不让他看我的极圈0：看 1： 不看

    private void savePermission(int seehemoment, int seemehoment) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put(XNConstants.friendid,userid);
        params.put("seeHeMoment",""+seehemoment);
        params.put("seeMeMoment",""+seemehoment);

        showProgressDialog("设置中...",false);
        MyPresenter.request(this, UrlString.URL_CIRCLE_PERMISSION, params, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                SuperObservableManager.notify(OnDeleteContact.class,onDeleteContact -> onDeleteContact.delete());
                toast("设置成功");
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);

            }
        });

    }

}
