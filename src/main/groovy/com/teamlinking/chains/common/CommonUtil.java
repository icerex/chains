package com.teamlinking.chains.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 16/1/19.
 */
public class CommonUtil {

    /**
     * 计算有中文字符串的长度,中文算2个
     * @param value
     * @return
     */
    public static int length(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 匹配日期,格式#2013-01-30#,#2013年01月31日#
     * @param str
     * @return
     */
    public static Date matcherDate(String str){
        String dataStr = matcherGroup(str,Constants.DATE_SET_EL);
        while(dataStr != null){
            dataStr = dataStr.replaceAll("#","");
            try{
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date date = format.parse(dataStr);
                if (date != null){
                    return date;
                }
            }catch (Exception e){
                try{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
                    Date date = format.parse(dataStr);
                    if (date != null){
                        return date;
                    }
                }catch (Exception e2){
                    try{
                        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
                        Date date = format.parse(dataStr);
                        if (date != null){
                            return date;
                        }
                    }catch (Exception e3){
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取匹配的字符串
     * @param str
     * @param el
     * @return
     */
    public static String matcherGroup(String str,String el){
        Pattern p = Pattern.compile(el);
        Matcher m = p.matcher(str);
        while(m.find()){
            return m.group();
        }
        return null;
    }

    /**
     * 5分钟内可用
     * @param date
     * @return
     */
    public static boolean isFiveMinuteable(Date date){
        return (new Date()).getTime() - date.getTime() < 5*60*1000;
    }
}
