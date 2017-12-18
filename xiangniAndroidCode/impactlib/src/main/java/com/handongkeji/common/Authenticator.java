
package com.handongkeji.common;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

/**
 * 进行身份认证的工具类
 */
class Authenticator extends AbstractAccountAuthenticator {
//    一个参数的构造器
    public Authenticator(Context context) {
        super(context);
    }

    /**
     * 添加账户的操作的方法
     * @param response  记录账户信息的对象
     * @param accountType 标识账户类型的字符串
     * @param authTokenType  用户的标识的类型
     * @param requiredFeatures
     * @param options
     * @return
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) {
        final Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(
            AccountAuthenticatorResponse response, Account account, Bundle options) {
        return null;
    }

    /**
     * 编辑属性的方法
     * @param response  标识账户的对象
     * @param accountType  账户的类型
     * @return
     */
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取当前的用户的token
     * @param response
     * @param account
     * @param authTokenType
     * @param loginOptions
     * @return
     * @throws NetworkErrorException
     */
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                               String authTokenType, Bundle loginOptions) throws NetworkErrorException {

        return null;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        // null means we don't support multiple authToken types
        return null;
    }

    @Override
    public Bundle hasFeatures(
            AccountAuthenticatorResponse response, Account account, String[] features) {
        // This call is used to query whether the Authenticator supports
        // specific features. We don't expect to get called, so we always
        // return false (no) for any queries.
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                                    String authTokenType, Bundle loginOptions) {
        return null;
    }
}
