package com.ixiangni.presenters;

import android.content.Context;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.MyApp;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/8.
 */

public class AddPaywayPresenter extends MyPresenter {

/*

参数	必选	类型	说明
token	    是	String	token
accountType	是	Integer	支付方式 101微信支付，102支付宝，103银行卡
realName	是	String	真实姓名
accountCode	是	String	支付帐户
spare1	    否	String	开户行。accountType=103时必传
 */

    public enum AccountType {

        WEIXIN("101"), ZHIFUBAO("102"), BANKCARD("103");

        private String value;

        AccountType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public void add(Context context, AccountType accountType, String realName, String accountCode, String spare, OnResult<String> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("accountType", accountType.getValue());
        params.put("realName", realName);
        params.put("accountCode", accountCode);
        if (AccountType.BANKCARD.equals(accountType)) {
            params.put("spare1", spare);
        }
        request(context, UrlString.URL_ADD_PAY_WAY, params, callback);

    }


}
