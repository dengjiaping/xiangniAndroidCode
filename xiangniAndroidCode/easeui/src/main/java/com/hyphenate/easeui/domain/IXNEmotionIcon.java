package com.hyphenate.easeui.domain;

import java.io.File;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class IXNEmotionIcon extends EaseEmojicon {

    private File iconFile;

    public IXNEmotionIcon(File iconFile,String netPath) {
        super();
        this.iconFile = iconFile;
        setType(Type.BIG_EXPRESSION);
        setIconPath(netPath);
    }

    public File getIconFile() {
        return iconFile;
    }

    public void setIconFile(File iconFile) {
        this.iconFile = iconFile;
    }
}
