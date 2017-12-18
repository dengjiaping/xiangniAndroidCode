package com.ixiangni.presenters;

/**
 * Created by Administrator on 2017/7/6 0006.
 */

import android.content.Context;
import android.text.TextUtils;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.utils.CommonUtils;
import com.ixiangni.app.MyApp;
import com.ixiangni.constants.UrlString;
import com.ixiangni.presenters.contract.MyPresenter;

import java.util.HashMap;

/**
 * 群设置
 *
 * @ClassName:GroupSetPresenter
 * @PackageName:com.ixiangni.presenters
 * @Create On 2017/7/6 0006   10:54
 * @Site:http://www.handongkeji.com
 * @author:xuchuanting
 * @Copyrights 2017/7/6 0006 handongkeji All rights reserved.
 */
public class GroupSetPresenter extends MyPresenter {

    /**
     * 修改群资料
     * token
     * admin
     * disturb
     * groupchatname
     * groupchatdesc
     * groupchatno
     * <p>
     * admin	否	String	'0 允许加人，1 拒绝加人
     * disturb	否	String	'0 不设置，1免打扰
     */

    public static final String ADMIN_ALLOW = "0";
    public static final String ADMIN_DISALLOW = "1";

    public static final String DISTURB_SILENCE = "1";
    public static final String DISTURB_NOT_SILENCE = "0";

    public void modify(Context context, OnResult<String> callback,
                       String groupchatno, String groupchatname, String groupchatdesc, String disturb, String admin) {

        HashMap<String, String> params = new HashMap<>();
        params.put(Constants.token, MyApp.getInstance().getUserTicket());
        params.put("groupchatno",groupchatno);

        if (!TextUtils.isEmpty(groupchatname)) {

            params.put("groupchatname",groupchatname);
        }
        if (!TextUtils.isEmpty(groupchatdesc)) {

            params.put("groupchatdesc",groupchatdesc);
        }
        if (!TextUtils.isEmpty(disturb)) {

            params.put("disturb",disturb);
        }
        if (!TextUtils.isEmpty(admin)) {

            params.put("admin",admin);
        }

        RemoteDataHandler.asyncPost(UrlString.URL_MODIFY_GROUP_DATA, params, context, false, response -> {
            String json = response.getJson();
            if (CommonUtils.isStringNull(json)) {
                callback.onFailed(Constants.CONNECT_SERVER_FAILED);
            } else {
                handJson(json, callback, "修改成功!");
            }

        });
    }

}
