package com.nevermore.oceans.ob;

import android.database.Observable;
import android.util.Log;

/**
 *
 * author XuNeverMore
 * create on 2017/6/15 0015
 * github https://github.com/XuNeverMore
 */


public class SuperObservable<T> extends Observable<T> {

    public  static final String TAG = "SuperObservable";
    public void notifyMethod(Dispatcher<T> dispatcher){

        Log.i(TAG, "notifyMethod: "+mObservers.size());
        synchronized (mObservers){
            for (int i = mObservers.size()-1; i >= 0; i--) {
                T t = mObservers.get(i);
                Log.i(TAG, t.getClass().getSimpleName()+"notifyMethod: ");
                    if(dispatcher!=null){
                        dispatcher.call(t);
                    }
            }
        }
    }


}
