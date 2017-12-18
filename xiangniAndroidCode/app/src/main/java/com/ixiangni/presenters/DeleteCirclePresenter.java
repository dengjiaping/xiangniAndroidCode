package com.ixiangni.presenters;

import android.content.Context;
import android.support.annotation.NonNull;

import com.handongkeji.common.Constants;
import com.handongkeji.handler.RemoteDataHandler;
import com.handongkeji.interfaces.OnResult;
import com.handongkeji.modle.ResponseData;
import com.handongkeji.utils.CommonUtils;
import com.hyphenate.chatuidemo.Constant;
import com.ixiangni.app.MyApp;
import com.ixiangni.constants.UrlString;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/6/29 0029.
 */

public class DeleteCirclePresenter {

    public void delete(Context context, String momentnewsid, @NonNull OnResult<String> callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("token", MyApp.getInstance().getUserTicket());
        params.put("momentnewsid", momentnewsid);
        RemoteDataHandler.asyncPost(UrlString.URL_DELETE_CIRCLE_ITEM, params, context, false, response -> {
            String json = response.getJson();
            if (CommonUtils.isStringNull(json)) {

                callback.onFailed(Constants.CONNECT_SERVER_FAILED);

            } else {

                try {
                    JSONObject object = new JSONObject(json);
                    if (1 == object.getInt(Constants.status)) {
                        callback.onSuccess(json);
                    } else {
                        callback.onFailed(object.getString(Constants.message));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        });

    }
}
