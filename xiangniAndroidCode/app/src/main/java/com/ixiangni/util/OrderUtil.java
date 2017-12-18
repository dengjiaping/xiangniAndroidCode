package com.ixiangni.util;

import com.handongkeji.utils.DateUtil;
import com.ixiangni.bean.PlaneBaseBean;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/5 0005.
 */

public class OrderUtil {

    /**
     * 2017-08-05 14:45:00 -> 14:45
     * @param time
     * @return
     */
    public static String getHourMin(String time){

        String[] split = time.split(" ");

        int i = split[1].lastIndexOf(":");

        return split[1].substring(0,i);
    }

    /**
     * 2017-08-05 14:45:00 -> 2017-08-05
     * @param time
     * @return
     */
    public static String getYMD(String time){
        return time.split(" ")[0];
    }

    /**
     * 航班
     * @param d
     * @return
     */
    public static String getFlight(PlaneBaseBean d){
        return d.getCarrinerName()+" "+d.getFlightNo();
    }

    public static String getMd(String date){
        String[] split = date.split("-");
        String result ="";

        if(split.length>=3){
            return split[1]+"月"+split[2]+"日";
        }

        return "";
    }

    public static String changeDay(String date,int var){
        try {
            Date parse = DateUtil.DF_CENTER_LINE.parse(date);
            Calendar instance = Calendar.getInstance();
            instance.setTime(parse);
            instance.add(Calendar.DAY_OF_MONTH,var);

            return DateUtil.getYmd(instance.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}
