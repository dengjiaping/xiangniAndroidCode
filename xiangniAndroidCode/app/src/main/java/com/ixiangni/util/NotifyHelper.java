package com.ixiangni.util;

import android.util.Log;

import com.ixiangni.interfaces.OnGroupNameChange;
import com.ixiangni.interfaces.OnRefreshContactList;
import com.nevermore.oceans.ob.Dispatcher;
import com.nevermore.oceans.ob.SuperObservable;
import com.nevermore.oceans.ob.SuperObservableManager;

/**
 * Created by Administrator on 2017/7/3 0003.
 */

public class NotifyHelper {

    //刷新联系人列表
    public static void notifyContactList(){
        SuperObservable<OnRefreshContactList> observable = SuperObservableManager.getInstance().
                getObservable(OnRefreshContactList.class);

        observable.notifyMethod(new Dispatcher<OnRefreshContactList>() {
                    @Override
                    public void call(OnRefreshContactList onRefreshContactList) {
                        onRefreshContactList.onRefresh();
                    }
                });
    }

    /**
     * 通知观察者群名称发生改变
     * @param groupno
     * @param groupname
     */
    public static void notifyGroupChange(String groupno,String groupname){
        SuperObservableManager.getInstance().getObservable(OnGroupNameChange.class)
                .notifyMethod(new Dispatcher<OnGroupNameChange>() {
                    @Override
                    public void call(OnGroupNameChange onGroupNameChange) {
                        onGroupNameChange.onNameChange(groupno,groupname);
                    }
                });

    }
}
