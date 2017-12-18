package com.ixiangni.app.money;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.handongkeji.common.ValidateHelper;
import com.handongkeji.interfaces.OnResult;
import com.ixiangni.app.R;
import com.ixiangni.app.base.BaseActivity;
import com.ixiangni.presenters.AddPaywayPresenter;
import com.nevermore.oceans.ob.SuperObservableManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加银行卡
 *
 * @ClassName:AddBankCardActivity
 * @PackageName:com.ixiangni.app.money
 * @Create On 2017/6/22 0022   15:35
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/6/22 0022 handongkeji All rights reserved.
 */
public class AddBankCardActivity extends BaseActivity {

    @Bind(R.id.edt_bank_name)
    EditText edtBankName;
    @Bind(R.id.edt_bank_card_num)
    EditText edtBankCardNum;
    @Bind(R.id.edt_name)
    EditText edtName;
    @Bind(R.id.btn_add)
    Button btnAdd;
    private AddPaywayPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        ButterKnife.bind(this);
        mPresenter = new AddPaywayPresenter();
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        tryAddBankCard();
    }

    /**
     * 银行卡变化
     */
    public interface OnBankCardChange{
        void onChange();
    }

    private void tryAddBankCard() {

        String bankName = edtBankName.getText().toString().trim();

        if(TextUtils.isEmpty(bankName)){
            toast("请填写开户银行名称！");
            return;
        }

        String bankNum = edtBankCardNum.getText().toString().trim();
        if(TextUtils.isEmpty(bankNum)){
            toast("请填写银行卡号！");
            return;
        }
        if(!ValidateHelper.isBankCardValid(bankNum)){
            toast("银行卡号不正确！");
            return;
        }

        String personName = edtName.getText().toString().trim();
        if(TextUtils.isEmpty(personName)){
            toast("请填写持卡人姓名！");
            return;
        }

        //添加银行卡
        showProgressDialog("添加中...",false);
        mPresenter.add(this, AddPaywayPresenter.AccountType.BANKCARD, personName, bankNum, bankName, new OnResult<String>() {
            @Override
            public void onSuccess(String s) {
                dismissProgressDialog();
                toast("添加成功！");
                SuperObservableManager.getInstance().getObservable(OnBankCardChange.class)
                        .notifyMethod(OnBankCardChange::onChange);
                finish();
            }

            @Override
            public void onFailed(String errorMsg) {
                dismissProgressDialog();
                toast(errorMsg);
            }
        });

    }
}
