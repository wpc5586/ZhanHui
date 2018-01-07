package com.aaron.aaronlibrary.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
     *
     * @param longdate 要转换的时间（毫秒）
     * @return 返回转换的日期格式字符串
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
     *
     * @param longtime 要转换的时间（毫秒）
     * @return 返回转换的时间格式字符串
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
     *
     * @param longtime 要转换的时间（毫秒）
     * @return 返回转换的时间格式字符串
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
     * <p>方法描述： long time转换成 yyyy-MM-dd HH:mm:ss 字符串</p>
     *
     * @param longtime 要转换的时间（毫秒）
     * @return 返回转换的时间格式字符串
     */
    public static String timeToyyyyMMddHHmmssSLASH(long longtime) {
        try {
            Date date = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date.setTime(longtime);
            return formatTime.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>方法描述： long time转换成 yyyy.MM.dd HH:mm 字符串</p>
     *
     * @param longtime 要转换的时间（毫秒）
     * @return 返回转换的时间格式字符串
     */
    public static String timeToyyyyMMddHHmmSLASH(long longtime) {
        try {
            Date date = new Date();
            SimpleDateFormat formatTime = new SimpleDateFormat("yyyy.MM.dd HH:mm");
            date.setTime(longtime);
            return formatTime.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimestampString(long messageTime) {
        String format = null;
        String language = Locale.getDefault().getLanguage();
        boolean isZh = language.startsWith("zh");
        Date messageDate = new Date(messageTime);
        if (isSameDay(messageTime)) {
            if (isZh) {
                format = "aa hh:mm";
            } else {
                format = "hh:mm aa";
            }
        } else if (isYesterday(messageTime)) {
            if (isZh) {
                format = "昨天aa hh:mm";
            } else {
                return "Yesterday " + new SimpleDateFormat("hh:mm aa", Locale.ENGLISH).format(messageDate);
            }
        } else {
            if (isZh) {
                format = "M月d日aa hh:mm";
            } else {
                format = "MMM dd hh:mm aa";
            }
        }
        if (isZh) {
            return new SimpleDateFormat(format, Locale.CHINESE).format(messageDate);
        } else {
            return new SimpleDateFormat(format, Locale.ENGLISH).format(messageDate);
        }
    }

    private static boolean isSameDay(long inputTime) {

        TimeInfo tStartAndEndTime = getTodayStartAndEndTime();
        if (inputTime > tStartAndEndTime.getStartTime() && inputTime < tStartAndEndTime.getEndTime())
            return true;
        return false;
    }

    private static boolean isYesterday(long inputTime) {
        TimeInfo yStartAndEndTime = getYesterdayStartAndEndTime();
        if (inputTime > yStartAndEndTime.getStartTime() && inputTime < yStartAndEndTime.getEndTime())
            return true;
        return false;
    }
    /**
     *
     * @param timeLength second
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String toTimeBySecond(int timeLength) {
//		timeLength /= 1000;
        int minute = timeLength / 60;
        int hour = 0;
        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        int second = timeLength % 60;
        // return String.format("%02d:%02d:%02d", hour, minute, second);
        return String.format("%02d:%02d", minute, second);
    }



    public static TimeInfo getYesterdayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, -1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -1);
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    public static TimeInfo getTodayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    public static TimeInfo getBeforeYesterdayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, -2);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -2);
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    public static class TimeInfo {
        private long startTime;
        private long endTime;

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }
    }
}
