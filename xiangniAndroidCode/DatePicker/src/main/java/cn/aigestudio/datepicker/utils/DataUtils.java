package cn.aigestudio.datepicker.utils;

/**
 * 数组操作工具类
 * 
 * Utils of data operation
 *
 * @author AigeStudio 2015-07-22
 */
public final class DataUtils {
    /**
     * 一维数组转换为二维数组
     *
     * @param src    ...
     * @param row    ...
     * @param column ...
     * @return ...
     */
    public static String[][] arraysConvert(String[] src, int row, int column) {
        String[][] tmp = new String[row][column];
        for (int i = 0; i < row; i++) {
            tmp[i] = new String[column];
            System.arraycopy(src, i * column, tmp[i], 0, column);
        }
        return tmp;
    }


    public static String toAigeDate(String date){

        String[] split = date.split("-");

        if(split.length>1){
            if(split[1].startsWith("0")){
                split[1]=split[1].replace("0","");
            }
        }
        if(split.length>2){
            if(split[2].startsWith("0")){
                split[2]=split[2].replace("0","");
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            sb.append(split[i]);
            if(i!=split.length-1){
                sb.append("-");
            }
        }
        return sb.toString();
    }

    public static String toStandardDate(String date){
        String[] split = date.split("-");

        if(split.length>1){
            if(split[1].length()==1){
                split[1]="0"+split[1];
            }
        }
        if(split.length>2){
            if(split[2].length()==1){
                split[2]="0"+split[2];
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            sb.append(split[i]);
            if(i!=split.length-1){
                sb.append("-");
            }
        }

        return sb.toString();
    }
}
