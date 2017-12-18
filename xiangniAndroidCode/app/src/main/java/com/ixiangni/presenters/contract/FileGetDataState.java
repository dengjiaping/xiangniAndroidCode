package com.ixiangni.presenters.contract;

import java.util.List;

/**
 * Created by Administrator on 2017/7/13 0013.
 */

public interface FileGetDataState<T> {
    void loadPicData(List<T>mlist);
    void loadTextData(List<T>mlist);
    void loadVideoData(List<T>mlist);
    void loadAudioData(List<T>mlist);
    void noData(String msg);
}
