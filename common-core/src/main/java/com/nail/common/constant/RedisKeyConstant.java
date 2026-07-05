package com.nail.common.constant;

/**
 * Redis缓存Key统一前缀
 * 所有Redis Key必须使用这些前缀，避免Key冲突
 *
 * @author nail-platform
 */
public class RedisKeyConstant {

    /**
     * Key前缀分隔符
     */
    public static final String KEY_SEPARATOR = ":";

    /**
     * ========== 用户相关 ==========
     */
    /** 用户信息缓存前缀 */
    public static final String USER_INFO = "user:info";
    /** 用户Token前缀 */
    public static final String USER_TOKEN = "user:token";
    /** 用户登录状态前缀 */
    public static final String USER_LOGIN_STATUS = "user:login";

    /**
     * ========== 预约相关 ==========
     */
    /** 预约信息前缀 */
    public static final String APPOINTMENT = "appointment";
    /** 预约时间缓存前缀（用于时间段占用检查） */
    public static final String APPOINTMENT_TIME_SLOT = "appointment:slot";
    /** 美甲师预约列表前缀 */
    public static final String APPOINTMENT_MANICURIST = "appointment:manicurist";

    /**
     * ========== 服务相关 ==========
     */
    /** 服务信息前缀 */
    public static final String SERVICE_INFO = "service:info";
    /** 服务分类前缀 */
    public static final String SERVICE_CATEGORY = "service:category";
    /** 热门服务前缀 */
    public static final String SERVICE_HOT = "service:hot";

    /**
     * ========== 营销相关 ==========
     */
    /** 优惠券前缀 */
    public static final String COUPON = "coupon";
    /** 用户优惠券前缀 */
    public static final String USER_COUPON = "user:coupon";
    /** 活动信息前缀 */
    public static final String ACTIVITY = "activity";

    /**
     * ========== 通知相关 ==========
     */
    /** 通知信息前缀 */
    public static final String NOTICE = "notice";
    /** 用户通知列表前缀 */
    public static final String USER_NOTICE = "user:notice";

    /**
     * ========== 验证码相关 ==========
     */
    /** 短信验证码前缀 */
    public static final String SMS_CODE = "sms:code";
    /** 邮箱验证码前缀 */
    public static final String EMAIL_CODE = "email:code";

    /**
     * ========== 限流相关 ==========
     */
    /** 接口限流前缀 */
    public static final String RATE_LIMIT = "rate:limit";
    /** 用户操作限流前缀 */
    public static final String USER_RATE_LIMIT = "user:rate:limit";

    /**
     * ========== 分布式锁相关 ==========
     */
    /** 分布式锁前缀 */
    public static final String LOCK = "lock";

    /**
     * ========== 其他 ==========
     */
    /** 配置信息前缀 */
    public static final String CONFIG = "config";
    /** 字典数据前缀 */
    public static final String DICT = "dict";

    /**
     * 构建完整的Redis Key
     *
     * @param prefix 前缀
     * @param params 参数
     * @return 完整Key
     */
    public static String buildKey(String prefix, String... params) {
        StringBuilder key = new StringBuilder(prefix);
        for (String param : params) {
            key.append(KEY_SEPARATOR).append(param);
        }
        return key.toString();
    }

    /**
     * 构建用户信息Key
     *
     * @param userId 用户ID
     * @return Redis Key
     */
    public static String buildUserInfoKey(Long userId) {
        return buildKey(USER_INFO, String.valueOf(userId));
    }

    /**
     * 构建用户Token Key
     *
     * @param token Token
     * @return Redis Key
     */
    public static String buildUserTokenKey(String token) {
        return buildKey(USER_TOKEN, token);
    }

    /**
     * 构建预约时间槽Key
     *
     * @param manicuristId 美甲师ID
     * @param date         日期
     * @return Redis Key
     */
    public static String buildAppointmentTimeSlotKey(Long manicuristId, String date) {
        return buildKey(APPOINTMENT_TIME_SLOT, String.valueOf(manicuristId), date);
    }

    /**
     * 构建验证码Key
     *
     * @param phone 手机号
     * @return Redis Key
     */
    public static String buildSmsCodeKey(String phone) {
        return buildKey(SMS_CODE, phone);
    }

    /**
     * 构建分布式锁Key
     *
     * @param resource 资源标识
     * @return Redis Key
     */
    public static String buildLockKey(String resource) {
        return buildKey(LOCK, resource);
    }
}
