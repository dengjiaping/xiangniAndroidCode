package com.ixiangni.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;

public class PinyinUtil {

    /**
     * 根据指定的汉字字符串, 返回其对应的拼音
     *
     * @param string
     * @return
     */
    public static String getPinyin(String string) {
        if (string.equals("重庆")) {
            return "C";
        }
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        // 不需要音标
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        // 设置转换出大写字母
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        char[] charArray = string.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            // 如果是空格, 跳过当前循环
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c >= -128 && c < 127) {
                // 不可能是汉字, 直接拼接
                sb.append(c);
            } else {
                try {
                    // 获取某个字符对应的拼音. 可以获取到多音字. 单 ->DAN, SHAN
                    String s = PinyinHelper.toHanyuPinyinStringArray(c, format)[0];
                    sb.append(s);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /**
     * 获取汉字串拼音首字母，英文字符不�??
     *
     * @param chinese 汉字�??
     * @return 汉语拼音首字�??
     */
    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字�??
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese) {


        if("长春".equals(chinese)){
            return "cc";
        }

        if("重庆".equals(chinese)){
            return "cq";
        }

        if("长沙".equals(chinese)){
            return "cs";
        }

        if("长治".equals(chinese)){
            return "cz";
        }

        StringBuffer pybf = new StringBuffer();

        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] strings = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (strings != null && strings.length > 0) {
                        pybf.append(strings[0]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();
    }

    /**
     * 把汉字转行成拼音，获取首字母
     *
     * @param chinese 汉字�??
     * @return 汉语拼音
     */
    public static String getFrist(String chinese) {
        String ru = "#";
        String str = getFullSpell(chinese);
        if (str != null && str.length()>=1) {
            ru = str.subSequence(0, 1).toString();
        }

        if(!isABC(ru.toUpperCase())){
            return "#";
        }

        return ru.toLowerCase();
    }

    private static List<String> letters= new ArrayList<>();
    static {
        for (int i = 0; i < 26; i++) {
            char a='A';
            int letter = a+i;
            letters.add(String.valueOf((char) letter));
        }
    }
    public static boolean isABC(String string){
        if(letters.contains(string)){
            return true;
        }
        return false;
    }
}
