package com.a55haitao.wwht.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间戳计算
 *
 * @author 陶声
 * @since 2016-10-10
 */
public class HaiTimeUtils {

    private static final long ONE_HOUR = 3600000L;      // 一小时
    private static final long ONE_DAY = 86400000L;      // 一天
    private static final long ONE_WEEK = 604800000L;    // 一周

    private static SimpleDateFormat detailedFormatter;
    private static SimpleDateFormat formatter;
    private static SimpleDateFormat dateFormatter;

    static {
        detailedFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        detailedFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));

        dateFormatter = new SimpleDateFormat("yyyy.MM.dd");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT+8"));
    }

    public static String showPostTime(long time) {

        long duration = System.currentTimeMillis() - time * 1000; // 经过的时间

        String result;

        if (duration < ONE_HOUR) { // 一小时以内
            result = duration / 1000 / 60 + "分钟前";
        } else if (duration < ONE_DAY) { // 今天
            result = duration / 1000 / 60 / 60 + "小时前";
        } else if (duration < ONE_DAY * 2) { // 昨天
            result = "昨天";
        } else if (duration < ONE_WEEK) { // 大于48小时,小于7天
            result = duration / 1000 / 60 / 60 / 24 + "天前";
        } else { // 2015-08-25 18:00
            result = formatter.format(new Date(time * 1000));
//            result = formatter.format(System.currentTimeMillis());
        }

        return result;
    }

    public static String showDetailWSecTime(long time) {
        return detailedFormatter.format(new Date(time * 1000));
    }

    public static String showDetailTime(long time) {
        return formatter.format(new Date(time * 1000));
    }

    public static String showDate(long time) {
        return dateFormatter.format(new Date(time * 1000));
    }

    // 计算时间差，返回时分秒
    public static float[] counterTimeLong(String start, String end) { //  数据源是秒为单位的
        float[] temp = new float[3];
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        long timeStart = 0;
//        long timeEnd = 0;
//
//        try {
//            timeStart = sdf.parse(start).getTime();
//            timeEnd = sdf.parse(end).getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Long diff = timeEnd - timeStart;
//        Long diff = 200000l;
        long diff = Long.parseLong(end) - Long.parseLong(start);

        float hour = 0;
        float minute = 0;
        float second;

        second = diff;
//        second = diff / 1000;
        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }

        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        temp[0] = second;
        temp[1] = minute;
        temp[2] = hour;

        return temp;
    }

    // 计算时间差，返回 天 时 分 秒
    public static float[] counterTimeToDay(String start, String end) { //  数据源是秒为单位的
        float[] temp = new float[4];
        long diff = Long.parseLong(end) - Long.parseLong(start);

        float day = 0;
        float hour = 0;
        float minute = 0;
        float second;

        second = diff;
        //        second = diff / 1000;
        if (second >= 60) {
            minute = second / 60;
            second = second % 60;
        }

        if (minute >= 60) {
            hour = minute / 60;
            minute = minute % 60;
        }

        if (hour >= 24){
            day = hour / 24;
            hour = hour % 24;
        }
        temp[0] = second;
        temp[1] = minute;
        temp[2] = hour;
        temp[3] = day;

        return temp;
    }
}