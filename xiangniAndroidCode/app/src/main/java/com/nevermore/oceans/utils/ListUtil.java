package com.nevermore.oceans.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class ListUtil {


    /**
     * list是否为空
     * @param list
     * @return
     */
    public static boolean isEmptyList(List list){
        if(list==null||list.size()==0){
            return true;
        }
        return false;
    }

    public interface OnGetResult<Sourse,Result>{
        Result filter(Sourse sourse);
    }

    public interface OnMatch<T>{
        boolean match(T t);
    }

    public static <Sourse,Result> List<Result> getFilterList(List<Sourse> sourseList,OnGetResult<Sourse,Result> filter){
        List<Result> resultList = new ArrayList<>();
        if(isEmptyList(sourseList)){
            return resultList;
        }
        for (int i = 0; i < sourseList.size(); i++) {
            resultList.add(filter.filter(sourseList.get(i)));
        }
        return resultList;
    }

    public static <T> List<T> getMathcList(List<T> sourse,OnMatch<T> match){
        List<T> list = new ArrayList<>();
        if(isEmptyList(sourse)){
            return list;
        }

        for (int i = 0; i < sourse.size(); i++) {
            T t = sourse.get(i);
            if(match.match(t)){
                list.add(t);
            }
        }

        return list;
    }


    /**
     * 将list里的String用逗号拼接成字符串
     * @param list
     * @return
     */
    public static String toCommaString(List<String> list){
        String result="";
        if(ListUtil.isEmptyList(list)){
            return result;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String s = list.get(i);
            sb.append(s);
            if(i!=list.size()-1){
                sb.append(",");
            }
        }

        result=sb.toString();


        return result;
    }




}
