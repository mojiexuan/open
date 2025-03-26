package com.chenjiabao.open.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具
 */
public class TimeUtils {

    private static final String ZONE_ID = "Asia/Shanghai";

    /**
     * 获取当前时间戳
     * @return 秒级时间戳
     */
    public static long getNowTimeStamp(){
        return ZonedDateTime.now(ZoneId.of(ZONE_ID)).toInstant().getEpochSecond();
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
                .withZone(ZoneId.of(ZONE_ID)).format(Instant.ofEpochSecond(time));
    }

    /**
     * 秒级时间戳转指定格式（yyyy-MM-dd HH:mm:ss）字符串（东八区）
     * @param time 时间戳
     * @return 转换后的字符串
     */
    public static String getTime(long time){
        return getTime(time,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取东八区当日凌晨（00:00）时间戳（秒级）
     * @return 时间戳
     */
    public static long getNowStartDayTimeStamp(){
        // 获取当前日期的午夜时间（LocalDateTime）
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        // 转换为时间戳（毫秒）
        return startOfDay.atZone(ZoneId.of(ZONE_ID)).toInstant().getEpochSecond();
    }

}
