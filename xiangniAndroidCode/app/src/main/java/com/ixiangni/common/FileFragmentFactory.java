package com.ixiangni.common;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;

import com.ixiangni.app.mine.AudioFragment;
import com.ixiangni.app.mine.PictureFragment;
import com.ixiangni.app.mine.VideoFragment;
import com.ixiangni.app.mine.WordFragment;

/**
 * Created by Administrator on 2017/6/21 0021.
 */

public class FileFragmentFactory {

    private static FileFragmentFactory instance;

    public static final int FRAGMENT_PICTURE = 0;
    public static final int FRAGMENT_WROD = 1;
    public static final int FRAGMENT_VIDEO = 2;
    public static final int FRAGMENT_AUDIO = 3;

    private static SparseArray<Fragment> sparseArray;
    public static FileFragmentFactory getInstance(){
        if(instance==null){
            instance = new FileFragmentFactory();
            sparseArray = new SparseArray<>();
        }
        return instance;
    }


    public Fragment getFragment(int type){
        Fragment fragment = sparseArray.get(type);
        if(fragment==null){
            switch (type){
                case 0:
                    fragment = new PictureFragment();
                    break;
                case 1:
                    fragment = new WordFragment();
                    break;
                case 2:
                    fragment = new VideoFragment();
                    break;
                case 3:
                    fragment = new AudioFragment();
                    break;
            }

            sparseArray.put(type,fragment);
        }

        return fragment;
    }
}
