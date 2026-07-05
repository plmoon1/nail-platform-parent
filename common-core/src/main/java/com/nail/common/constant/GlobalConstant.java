package com.nail.common.constant;

/**
 * 通用常量
 * 全局通用常量定义
 *
 * @author nail-platform
 */
public class GlobalConstant {

    /**
     * ========== 字符串相关 ==========
     */
    /** 空字符串 */
    public static final String EMPTY_STRING = "";
    /** 字符串编码 */
    public static final String CHARSET_UTF8 = "UTF-8";
    /** 字符串编码（GBK） */
    public static final String CHARSET_GBK = "GBK";

    /**
     * ========== 数字相关 ==========
     */
    /** 数字0 */
    public static final int ZERO = 0;
    /** 数字1 */
    public static final int ONE = 1;
    /** 数字-1 */
    public static final int NEGATIVE_ONE = -1;

    /**
     * ========== 布尔相关 ==========
     */
    /** 是（True） */
    public static final int TRUE = 1;
    /** 否（False） */
    public static final int FALSE = 0;
    /** 布尔值：是 */
    public static final String YES = "yes";
    /** 布尔值：否 */
    public static final String NO = "no";

    /**
     * ========== 数据库相关 ==========
     */
    /** 逻辑删除：未删除 */
    public static final int DELETED_NO = 0;
    /** 逻辑删除：已删除 */
    public static final int DELETED_YES = 1;

    /** 状态：禁用 */
    public static final int STATUS_DISABLED = 0;
    /** 状态：启用 */
    public static final int STATUS_ENABLED = 1;

    /** 性别：未知 */
    public static final int GENDER_UNKNOWN = 0;
    /** 性别：男 */
    public static final int GENDER_MALE = 1;
    /** 性别：女 */
    public static final int GENDER_FEMALE = 2;

    /**
     * ========== 分页相关 ==========
     */
    /** 默认页码 */
    public static final long DEFAULT_PAGE_NUM = 1;
    /** 默认每页条数 */
    public static final long DEFAULT_PAGE_SIZE = 10;
    /** 最大每页条数 */
    public static final long MAX_PAGE_SIZE = 100;

    /**
     * ========== 时间相关 ==========
     */
    /** 一天的毫秒数 */
    public static final long ONE_DAY_MILLIS = 24 * 60 * 60 * 1000L;
    /** 一小时的毫秒数 */
    public static final long ONE_HOUR_MILLIS = 60 * 60 * 1000L;
    /** 一分钟的毫秒数 */
    public static final long ONE_MINUTE_MILLIS = 60 * 1000L;
    /** 一秒的毫秒数 */
    public static final long ONE_SECOND_MILLIS = 1000L;

    /**
     * ========== 日期格式 ==========
     */
    /** 默认日期时间格式 */
    public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /** 日期格式 */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    /** 时间格式 */
    public static final String TIME_FORMAT = "HH:mm:ss";
    /** 年月格式 */
    public static final String YEAR_MONTH_FORMAT = "yyyy-MM";
    /** 月日格式 */
    public static final String MONTH_DAY_FORMAT = "MM-dd";
    /** 时间戳格式（精确到毫秒） */
    public static final String TIMESTAMP_FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * ========== HTTP相关 ==========
     */
    /** HTTP请求方法：GET */
    public static final String HTTP_METHOD_GET = "GET";
    /** HTTP请求方法：POST */
    public static final String HTTP_METHOD_POST = "POST";
    /** HTTP请求方法：PUT */
    public static final String HTTP_METHOD_PUT = "PUT";
    /** HTTP请求方法：DELETE */
    public static final String HTTP_METHOD_DELETE = "DELETE";

    /** Content-Type: JSON */
    public static final String CONTENT_TYPE_JSON = "application/json";
    /** Content-Type: 表单 */
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";

    /**
     * ========== 用户相关 ==========
     */
    /** 默认头像 */
    public static final String DEFAULT_AVATAR = "/default-avatar.png";
    /** 超级管理员ID */
    public static final Long SUPER_ADMIN_ID = 1L;

    /**
     * ========== 文件相关 ==========
     */
    /** 文件大小单位：KB */
    public static final long FILE_SIZE_KB = 1024L;
    /** 文件大小单位：MB */
    public static final long FILE_SIZE_MB = 1024L * 1024;
    /** 文件大小单位：GB */
    public static final long FILE_SIZE_GB = 1024L * 1024 * 1024;

    /** 图片最大大小（10MB） */
    public static final long MAX_IMAGE_SIZE = 10 * FILE_SIZE_MB;
    /** 文件最大大小（50MB） */
    public static final long MAX_FILE_SIZE = 50 * FILE_SIZE_MB;

    /**
     * ========== 业务相关 ==========
     */
    /** 预约状态：待确认 */
    public static final int APPOINTMENT_STATUS_PENDING = 0;
    /** 预约状态：已确认 */
    public static final int APPOINTMENT_STATUS_CONFIRMED = 1;
    /** 预约状态：已完成 */
    public static final int APPOINTMENT_STATUS_COMPLETED = 2;
    /** 预约状态：已取消 */
    public static final int APPOINTMENT_STATUS_CANCELLED = 3;

    /** 支付状态：未支付 */
    public static final int PAYMENT_STATUS_UNPAID = 0;
    /** 支付状态：已支付 */
    public static final int PAYMENT_STATUS_PAID = 1;
    /** 支付状态：已退款 */
    public static final int PAYMENT_STATUS_REFUNDED = 2;

    /**
     * ========== 正则表达式 ==========
     */
    /** 手机号正则 */
    public static final String REGEX_PHONE = "^1[3-9]\\d{9}$";
    /** 邮箱正则 */
    public static final String REGEX_EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /** 身份证号正则 */
    public static final String REGEX_ID_CARD = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[\\dXx]$";
    /** URL正则 */
    public static final String REGEX_URL = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    /** IP地址正则 */
    public static final String REGEX_IP = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    /**
     * ========== 其他 ==========
     */
    /** 加密算法：MD5 */
    public static final String ALGORITHM_MD5 = "MD5";
    /** 加密算法：SHA-256 */
    public static final String ALGORITHM_SHA256 = "SHA-256";
    /** 加密算法：BCrypt */
    public static final String ALGORITHM_BCRYPT = "BCrypt";

    /** JWT Token前缀 */
    public static final String TOKEN_PREFIX = "Bearer ";
    /** JWT Token在Header中的Key */
    public static final String TOKEN_HEADER = "Authorization";
}