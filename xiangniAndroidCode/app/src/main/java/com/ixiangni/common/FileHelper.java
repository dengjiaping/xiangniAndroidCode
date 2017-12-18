package com.ixiangni.common;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class FileHelper {

    public static boolean  creatFileIfNotExist(File file) throws IOException {
        boolean result = true;
        if(!file.exists()){
            result = file.createNewFile();
        }
        return result;
    }

    public static boolean  creatDirIfNotExist(File file) throws IOException {
        boolean result = true;
        if(!file.exists()){
            result = file.mkdir();
        }
        return result;
    }
}
