package com.ixiangni.presenters;

import android.app.Activity;
import android.content.Context;

import com.baidu.platform.comapi.map.A;
import com.ixiangni.interfaces.IBaseUI;
import com.ixiangni.interfaces.IPresenter;

/**
 * Created by Administrator on 2017/6/22 0022.
 */

public abstract class BasePresenter implements IPresenter {

    protected IBaseUI mBaseUI;
    protected Context mContext;

    public BasePresenter(IBaseUI baseUI) {
        this.mBaseUI = baseUI;
        if(baseUI instanceof Activity){
            mContext = (Context) baseUI;
        }
    }

}
