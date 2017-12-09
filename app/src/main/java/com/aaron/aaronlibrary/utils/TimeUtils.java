package com.aaron.aaronlibrary.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>类名称: TimeUtils</p>
 * <p>类描述: 时间操作</p>
 * <p>所属模块: utils</p>
 * <p>创建时间: 2015-3-13  16:54</p> 
 * <p>作者: 王鹏程</p>
 * <p>版本: 1.0</p>
 */
public class TimeUtils {
    
    /**
     * <p>方法描述： long date转换成 yyyy年MM月dd日 字符串</p>
     * @param longdate  要转换的时间（毫秒）
     * @return  返回转换的日期格式字符串
     */
    public static String dateToyyyyMMdd(long longdate) {
        try {
            Date date = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy年MM月dd日");
            date.setTime(longdate);
            return formatDate.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * <p>方法描述： long time转换成 HH:mm:ss 字符串</p>
     * @param longtime  要转换的时间（毫秒）
     * @return  返回转换的时间格式字符串
     */
    public static String timeToHHmmss(long longtime) {
        try {
            Date date = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
            date.setTime(longtime);
            return formatTime.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * <p>方法描述： long time转换成 yyyyMMddHHmmss 字符串</p>
     * @param longtime  要转换的时间（毫秒）
     * @return  返回转换的时间格式字符串
     */
    public static String timeToyyyyMMddHHmmss(long longtime) {
        try {
            Date date = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("yyyyMMddHHmmss");
            date.setTime(longtime);
            return formatTime.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>方法描述： long time转换成 yyyy/MM/dd HH:mm:ss 字符串</p>
     * @param longtime  要转换的时间（毫秒）
     * @return  返回转换的时间格式字符串
     */
    public static String timeToyyyyMMddHHmmssSLASH(long longtime) {
        try {
            Date date = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            date.setTime(longtime);
            return formatTime.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
