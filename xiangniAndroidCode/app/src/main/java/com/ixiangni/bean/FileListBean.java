package com.ixiangni.bean;

import com.handongkeji.interfaces.Stringable;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14 0014.
 */

public class FileListBean {

    /**
     * message : 操作成功!
     * data : [{"recordid":16,"folderid":8,"recordname":"20170703152858","recordtype":3,"memortydir":"fileoutputJFVideo-2017-07-03-15:28:40.mov","createtime":1499224351000,"mediapic":null,"medianame":"outputJFVideo-2017-07-03-15:28:40.mov","mediatime":null,"mediaid":null,"delmark":1,"textcotent":null},{"recordid":15,"folderid":8,"recordname":"20170703152858","recordtype":3,"memortydir":"fileoutputJFVideo-2017-07-03-15:28:40.mov","createtime":1499224343000,"mediapic":null,"medianame":"outputJFVideo-2017-07-03-15:28:40.mov","mediatime":null,"mediaid":null,"delmark":1,"textcotent":null},{"recordid":14,"folderid":8,"recordname":"","recordtype":2,"memortydir":"fileA633bd08e-99a4-42e6-ba84-3d477ae13a63.mp3","createtime":1499073352000,"mediapic":null,"medianame":"A633bd08e-99a4-42e6-ba84-3d477ae13a63.mp3","mediatime":"2\u201d","mediaid":null,"delmark":1,"textcotent":null},{"recordid":13,"folderid":8,"recordname":"","recordtype":2,"memortydir":"fileA83a76902-f4cb-4142-b7fd-a72211a2882f.mp3","createtime":1499071743000,"mediapic":null,"medianame":"A83a76902-f4cb-4142-b7fd-a72211a2882f.mp3","mediatime":"3\u201d","mediaid":null,"delmark":1,"textcotent":null},{"recordid":12,"folderid":8,"recordname":"","recordtype":2,"memortydir":"fileAb6455479-68a7-4b88-a488-10bcedd111a4.mp3","createtime":1499071630000,"mediapic":null,"medianame":"Ab6455479-68a7-4b88-a488-10bcedd111a4.mp3","mediatime":"3\u201d","mediaid":null,"delmark":1,"textcotent":null},{"recordid":11,"folderid":8,"recordname":"","recordtype":2,"memortydir":"fileA00d046f9-0c5a-4a70-96a9-b825e3fb09a7.mp3","createtime":1499071452000,"mediapic":null,"medianame":"A00d046f9-0c5a-4a70-96a9-b825e3fb09a7.mp3","mediatime":"3\u201d","mediaid":null,"delmark":1,"textcotent":null},{"recordid":10,"folderid":8,"recordname":"20170703152858","recordtype":3,"memortydir":"fileoutputJFVideo-2017-07-03-15:28:40.mov","createtime":1499068139000,"mediapic":null,"medianame":"outputJFVideo-2017-07-03-15:28:40.mov","mediatime":null,"mediaid":null,"delmark":1,"textcotent":null},{"recordid":9,"folderid":8,"recordname":"20170703154810","recordtype":3,"memortydir":"fileoutputJFVideo-2017-07-03-15:47:46.mov","createtime":1499068123000,"mediapic":null,"medianame":"outputJFVideo-2017-07-03-15:47:46.mov","mediatime":null,"mediaid":null,"delmark":1,"textcotent":null},{"recordid":8,"folderid":8,"recordname":"20170703152858","recordtype":3,"memortydir":"fileoutputJFVideo-2017-07-03-15:28:40.mov","createtime":1499066995000,"mediapic":null,"medianame":"outputJFVideo-2017-07-03-15:28:40.mov","mediatime":null,"mediaid":null,"delmark":1,"textcotent":null},{"recordid":7,"folderid":8,"recordname":"20170703152858","recordtype":3,"memortydir":"fileoutputJFVideo-2017-07-03-15:28:40.mov","createtime":1499066938000,"mediapic":null,"medianame":"outputJFVideo-2017-07-03-15:28:40.mov","mediatime":null,"mediaid":null,"delmark":1,"textcotent":null}]
     * status : 1
     */

    private String message;
    private int status;
    private List<DataBean> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Stringable{
        /**
         * recordid : 16
         * folderid : 8
         * recordname : 20170703152858
         * recordtype : 3
         * memortydir : fileoutputJFVideo-2017-07-03-15:28:40.mov
         * createtime : 1499224351000
         * mediapic : null
         * medianame : outputJFVideo-2017-07-03-15:28:40.mov
         * mediatime : null
         * mediaid : null
         * delmark : 1
         * textcotent : null
         */

        private int recordid;
        private int folderid;
        private String recordname;
        private int recordtype;
        private String memortydir;
        private long createtime;
        private String mediapic;
        private String medianame;
        private String mediatime;
        private String mediaid;
        private int delmark;
        private String textcotent;

        public int getRecordid() {
            return recordid;
        }

        public void setRecordid(int recordid) {
            this.recordid = recordid;
        }

        public int getFolderid() {
            return folderid;
        }

        public void setFolderid(int folderid) {
            this.folderid = folderid;
        }

        public String getRecordname() {
            return recordname;
        }

        public void setRecordname(String recordname) {
            this.recordname = recordname;
        }

        public int getRecordtype() {
            return recordtype;
        }

        public void setRecordtype(int recordtype) {
            this.recordtype = recordtype;
        }

        public String getMemortydir() {
            return memortydir;
        }

        public void setMemortydir(String memortydir) {
            this.memortydir = memortydir;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getMediapic() {
            return mediapic;
        }

        public void setMediapic(String mediapic) {
            this.mediapic = mediapic;
        }

        public String getMedianame() {
            return medianame;
        }

        public void setMedianame(String medianame) {
            this.medianame = medianame;
        }

        public String getMediatime() {
            return mediatime;
        }

        public void setMediatime(String mediatime) {
            this.mediatime = mediatime;
        }

        public String getMediaid() {
            return mediaid;
        }

        public void setMediaid(String mediaid) {
            this.mediaid = mediaid;
        }

        public int getDelmark() {
            return delmark;
        }

        public void setDelmark(int delmark) {
            this.delmark = delmark;
        }

        public String getTextcotent() {
            return textcotent;
        }

        public void setTextcotent(String textcotent) {
            this.textcotent = textcotent;
        }

        @Override
        public String getString() {
            return memortydir;
        }
    }
}
