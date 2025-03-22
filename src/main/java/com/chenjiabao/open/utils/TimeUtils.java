package com.chenjiabao.open.utils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具
 */
public class TimeUtils {

    /**
     * 获取当前时间戳
     * @return 秒级时间戳
     */
    public static long getNowTimeStamp(){
        return Instant.now().getEpochSecond();
    }

    /**
     * 获取当前时间字符串
     * @return yyyy-MM-dd HH:mm:ss格式
     */
    public static String getNowTime(){
        return getTime(getNowTimeStamp());
    }

    /**
     * 秒级时间戳转指定格式字符串（东八区）
     * @param time 时间戳
     * @param format 格式，例如：yyyy-MM-dd HH:mm:ss
     * @return 转换后的字符串
     */
    public static String getTime(long time,String format){
        // 转换为东八区时间
        return DateTimeFormatter.ofPattern(format)
                .withZone(ZoneId.of("Asia/Shanghai")).format(Instant.ofEpochSecond(time));
    }

    /**
     * 秒级时间戳转指定格式（yyyy-MM-dd HH:mm:ss）字符串（东八区）
     * @param time 时间戳
     * @return 转换后的字符串
     */
    public static String getTime(long time){
        return getTime(time,"yyyy-MM-dd HH:mm:ss");
    }

}
