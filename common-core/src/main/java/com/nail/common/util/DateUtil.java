package com.nail.common.util;


import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 日期工具类
 * 提供日期格式化、时段计算等功能（预约核心业务依赖）
 *
 * @author nail-platform
 */
public class DateUtil {

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式（不含时间）
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 时间格式（不含日期）
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * 日期时间格式化器（预定义，线程安全）
     */
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    /**
     * 动态格式化器缓存（避免重复创建相同格式的DateTimeFormatter）
     */
    private static final Map<String, DateTimeFormatter> FORMATTER_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取缓存的格式化器（避免重复创建）
     *
     * @param pattern 日期格式
     * @return DateTimeFormatter
     */
    private static DateTimeFormatter getCachedFormatter(String pattern) {
        return FORMATTER_CACHE.computeIfAbsent(pattern, DateTimeFormatter::ofPattern);
    }

    /**
     * 格式化日期时间（默认格式）- 推荐
     *
     * @param localDateTime 本地日期时间
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * 格式化日期时间（指定格式）
     *
     * @param localDateTime 本地日期时间
     * @param pattern       格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(getCachedFormatter(pattern));
    }

    /**
     * 格式化日期（默认格式）
     *
     * @param localDate 本地日期
     * @return 格式化后的字符串
     */
    public static String format(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return localDate.format(DATE_FORMATTER);
    }

    /**
     * 格式化日期（指定格式）
     *
     * @param localDate 本地日期
     * @param pattern   格式
     * @return 格式化后的字符串
     */
    public static String format(LocalDate localDate, String pattern) {
        if (localDate == null) {
            return null;
        }
        return localDate.format(getCachedFormatter(pattern));
    }

    /**
     * 格式化时间（默认格式）
     *
     * @param localTime 本地时间
     * @return 格式化后的字符串
     */
    public static String format(LocalTime localTime) {
        if (localTime == null) {
            return null;
        }
        return localTime.format(TIME_FORMATTER);
    }

    /**
     * 格式化时间（指定格式）
     *
     * @param localTime 本地时间
     * @param pattern   格式
     * @return 格式化后的字符串
     */
    public static String format(LocalTime localTime, String pattern) {
        if (localTime == null) {
            return null;
        }
        return localTime.format(getCachedFormatter(pattern));
    }

    /**
     * 格式化日期时间- Date类型（默认格式）
     * 注意：推荐使用 LocalDateTime 类型，此方法仅为兼容性保留
     *
     * @param date 日期
     * @return 格式化后的字符串
     */
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        return format(toLocalDateTime(date));
    }

    /**
     * 格式化日期时间- Date类型（指定格式）
     * 注意：推荐使用 LocalDateTime 类型，此方法仅为兼容性保留
     *
     * @param date    日期
     * @param pattern 格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        return format(toLocalDateTime(date), pattern);
    }

    /**
     * 解析字符串为 LocalDateTime（默认格式）
     *
     * @param dateStr 日期字符串
     * @return LocalDateTime对象
     */
    public static LocalDateTime parse(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, DEFAULT_FORMATTER);
        } catch (Exception e) {
            // 尝试其他格式
            try {
                LocalDate localDate = LocalDate.parse(dateStr, DATE_FORMATTER);
                return localDate.atStartOfDay();
            } catch (Exception ex) {
                throw new RuntimeException("日期解析失败: " + dateStr + ", 支持的格式: " + DEFAULT_FORMAT + ", " + DATE_FORMAT, ex);
            }
        }
    }

    /**
     * 解析字符串为 LocalDateTime（指定格式）
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return LocalDateTime对象
     */
    public static LocalDateTime parse(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(dateStr, getCachedFormatter(pattern));
        } catch (Exception e) {
            throw new RuntimeException("日期解析失败: " + dateStr + ", 格式: " + pattern, e);
        }
    }

    /**
     * 解析字符串为 LocalDate（默认格式）
     *
     * @param dateStr 日期字符串
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (Exception e) {
            throw new RuntimeException("日期解析失败: " + dateStr + ", 格式: " + DATE_FORMAT, e);
        }
    }

    /**
     * 解析字符串为 LocalDate（指定格式）
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return LocalDate对象
     */
    public static LocalDate parseDate(String dateStr, String pattern) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr, getCachedFormatter(pattern));
        } catch (Exception e) {
            throw new RuntimeException("日期解析失败: " + dateStr + ", 格式: " + pattern, e);
        }
    }

    /**
     * 解析字符串为 LocalTime（默认格式）
     *
     * @param timeStr 时间字符串
     * @return LocalTime对象
     */
    public static LocalTime parseTime(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, TIME_FORMATTER);
        } catch (Exception e) {
            throw new RuntimeException("时间解析失败: " + timeStr + ", 格式: " + TIME_FORMAT, e);
        }
    }

    /**
     * 解析字符串为 LocalTime（指定格式）
     *
     * @param timeStr 时间字符串
     * @param pattern 格式
     * @return LocalTime对象
     */
    public static LocalTime parseTime(String timeStr, String pattern) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        try {
            return LocalTime.parse(timeStr, getCachedFormatter(pattern));
        } catch (Exception e) {
            throw new RuntimeException("时间解析失败: " + timeStr + ", 格式: " + pattern, e);
        }
    }

    /**
     * 解析字符串为 Date（默认格式）- 兼容方法
     * 注意：推荐使用 parse() 返回 LocalDateTime
     *
     * @param dateStr 日期字符串
     * @return Date对象
     */
    public static Date parseToDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        LocalDateTime localDateTime = parse(dateStr);
        return localDateTime != null ? toDate(localDateTime) : null;
    }

    /**
     * 获取当前日期时间
     *
     * @return 当前日期时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获取当前日期
     *
     * @return 当前日期
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * 获取当前时间戳
     *
     * @return 时间戳（毫秒）
     */
    public static long currentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * Date 转 LocalDateTime
     *
     * @param date Date对象
     * @return LocalDateTime对象
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime 转 Date
     *
     * @param localDateTime LocalDateTime对象
     * @return Date对象
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 计算两个日期之间的天数差
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 天数差
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * 计算两个日期时间之间的小时差
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 小时差
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * 计算两个日期时间之间的分钟差
     *
     * @param startDateTime 开始日期时间
     * @param endDateTime   结束日期时间
     * @return 分钟差
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            return 0;
        }
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * 判断是否为同一天
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return 是否为同一天
     */
    public static boolean isSameDay(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.equals(date2);
    }

    /**
     * 判断是否为同一时刻
     *
     * @param dateTime1 日期时间1
     * @param dateTime2 日期时间2
     * @return 是否为同一时刻
     */
    public static boolean isSameTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        if (dateTime1 == null || dateTime2 == null) {
            return false;
        }
        return dateTime1.equals(dateTime2);
    }

    /**
     * 获取一天的开始时间（00:00:00）
     *
     * @param date 日期
     * @return 当天开始时间
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }

    /**
     * 获取一天的结束时间（23:59:59）
     *
     * @param date 日期
     * @return 当天结束时间
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(23, 59, 59);
    }

    /**
     * 判断是否为工作日（周一到周五）
     *
     * @param date 日期
     * @return 是否为工作日
     */
    public static boolean isWeekday(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    /**
     * 判断是否为周末
     *
     * @param date 日期
     * @return 是否为周末
     */
    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }

    /**
     * 增加天数
     *
     * @param date      日期
     * @param daysToAdd 增加的天数
     * @return 新的日期
     */
    public static LocalDate plusDays(LocalDate date, long daysToAdd) {
        if (date == null) {
            return null;
        }
        return date.plusDays(daysToAdd);
    }

    /**
     * 增加小时
     *
     * @param dateTime       日期时间
     * @param hoursToAdd 增加的小时数
     * @return 新的日期时间
     */
    public static LocalDateTime plusHours(LocalDateTime dateTime, long hoursToAdd) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hoursToAdd);
    }

    /**
     * 增加分钟
     *
     * @param dateTime       日期时间
     * @param minutesToAdd 增加的分钟数
     * @return 新的日期时间
     */
    public static LocalDateTime plusMinutes(LocalDateTime dateTime, long minutesToAdd) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusMinutes(minutesToAdd);
    }

    /**
     * 获取当前时间戳字符串
     *
     * @return 时间戳字符串
     */
    public static String timestamp() {
        return String.valueOf(System.currentTimeMillis());
    }
}
