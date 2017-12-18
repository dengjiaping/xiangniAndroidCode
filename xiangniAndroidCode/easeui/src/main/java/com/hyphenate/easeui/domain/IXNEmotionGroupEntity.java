package com.hyphenate.easeui.domain;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/7/11 0011.
 */

public class IXNEmotionGroupEntity extends EaseEmojiconGroupEntity {

    private String iconNetPath;

    private File iconFile;

    public IXNEmotionGroupEntity( String iconNetPath, File iconFile,List<EaseEmojicon> emojiconList) {
        super(0, emojiconList);
        this.iconNetPath = iconNetPath;
        this.iconFile = iconFile;
        //大表情实际是图片
        this.setType(EaseEmojicon.Type.BIG_EXPRESSION);
    }

    public String getIconNetPath() {
        return iconNetPath;
    }

    public void setIconNetPath(String iconNetPath) {
        this.iconNetPath = iconNetPath;
    }

    public File getIconFile() {
        return iconFile;
    }

    public void setIconFile(File iconFile) {
        this.iconFile = iconFile;
    }
}
