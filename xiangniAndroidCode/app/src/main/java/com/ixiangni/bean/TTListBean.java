package com.ixiangni.bean;

import java.util.List;

/**
 * 火车票列表
 * @ClassName:TTListBean

 * @PackageName:com.ixiangni.bean

 * @Create On 2017/8/7 0007   10:09

 * @Site:http://www.handongkeji.com

 * @author:xuchuanting

 * @Copyrights 2017/8/7 0007 handongkeji All rights reserved.
 */

public class TTListBean {

    /**
     * message : 操作成功!
     * data : [{"ToStation":"","FromStation":"","Seatfeature":"","TrainCode":"Z107","StationS":"北京西","StationE":"深圳","SFType":"始-终","TrainType":"新空直达","TrainID":"Z107","Distance":"","RunTime":"1336","ETime":"18:11","GoTime":"19:55","YuDing":"True","SeatList":[{"type":"无座","price":"254.5","shengyu":"100"}]},{"ToStation":"","FromStation":"","Seatfeature":"","TrainCode":"D901","StationS":"北京西","StationE":"深圳北","SFType":"始-终","TrainType":"动车组","TrainID":"D901","Distance":"","RunTime":"656","ETime":"07:06","GoTime":"20:10","YuDing":"False","SeatList":[]},{"ToStation":"","FromStation":"","Seatfeature":"","TrainCode":"D903","StationS":"北京西","StationE":"深圳北","SFType":"始-终","TrainType":"动车组","TrainID":"D903","Distance":"","RunTime":"656","ETime":"07:11","GoTime":"20:15","YuDing":"False","SeatList":[]},{"ToStation":"","FromStation":"","Seatfeature":"","TrainCode":"D909","StationS":"北京西","StationE":"深圳北","SFType":"始-终","TrainType":"动车组","TrainID":"D909","Distance":"","RunTime":"656","ETime":"07:16","GoTime":"20:20","YuDing":"False","SeatList":[]},{"ToStation":"","FromStation":"","Seatfeature":"","TrainCode":"D927","StationS":"北京西","StationE":"深圳北","SFType":"始-终","TrainType":"动车组","TrainID":"D927","Distance":"","RunTime":"660","ETime":"07:25","GoTime":"20:25","YuDing":"False","SeatList":[]},{"ToStation":"","FromStation":"","Seatfeature":"","TrainCode":"K105","StationS":"北京西","StationE":"深圳","SFType":"始-终","TrainType":"新空快速 ","TrainID":"K105","Distance":"","RunTime":"1741","ETime":"04:20","GoTime":"23:19","YuDing":"True","SeatList":[{"type":"无座","price":"254.5","shengyu":"100"}]}]
     * status : 1
     */

    private String message;
    private int status;
    private List<TTicketBean> data;

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

    public List<TTicketBean> getData() {
        return data;
    }

    public void setData(List<TTicketBean> data) {
        this.data = data;
    }
}
