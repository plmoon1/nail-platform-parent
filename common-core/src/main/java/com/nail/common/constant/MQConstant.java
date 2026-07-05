package com.nail.common.constant;

/**
 * RocketMQ主题、标签常量
 * 统一管理消息队列的主题和标签
 *
 * @author nail-platform
 */
public class MQConstant {

    /**
     * ========== 消费者组 ==========
     */
    /** 预约消费者组 */
    public static final String APPOINTMENT_CONSUMER_GROUP = "appointment-consumer-group";
    /** 通知消费者组 */
    public static final String NOTICE_CONSUMER_GROUP = "notice-consumer-group";
    /** 短信消费者组 */
    public static final String SMS_CONSUMER_GROUP = "sms-consumer-group";

    /**
     * ========== 预约相关主题 ==========
     */
    /** 预约创建主题 */
    public static final String APPOINTMENT_CREATED_TOPIC = "appointment_created_topic";
    /** 预约取消主题 */
    public static final String APPOINTMENT_CANCELLED_TOPIC = "appointment_cancelled_topic";
    /** 预约完成主题 */
    public static final String APPOINTMENT_COMPLETED_TOPIC = "appointment_completed_topic";
    /** 预约提醒主题 */
    public static final String APPOINTMENT_REMINDER_TOPIC = "appointment_reminder_topic";

    /**
     * ========== 通知相关主题 ==========
     */
    /** 系统通知主题 */
    public static final String SYSTEM_NOTICE_TOPIC = "system_notice_topic";
    /** 用户通知主题 */
    public static final String USER_NOTICE_TOPIC = "user_notice_topic";

    /**
     * ========== 短信相关主题 ==========
     */
    /** 短信发送主题 */
    public static final String SMS_SEND_TOPIC = "sms_send_topic";

    /**
     * ========== 标签（Tags） ==========
     */
    /** 预约创建标签 */
    public static final String TAG_APPOINTMENT_CREATED = "appointment_created";
    /** 预约取消标签 */
    public static final String TAG_APPOINTMENT_CANCELLED = "appointment_cancelled";
    /** 预约完成标签 */
    public static final String TAG_APPOINTMENT_COMPLETED = "appointment_completed";
    /** 预约提醒标签 */
    public static final String TAG_APPOINTMENT_REMINDER = "appointment_reminder";
    /** 短信验证码标签 */
    public static final String TAG_SMS_CODE = "sms_code";
    /** 短信通知标签 */
    public static final String TAG_SMS_NOTICE = "sms_notice";

    /**
     * ========== 消息Key ==========
     */
    /** 消息Key前缀 */
    public static final String MESSAGE_KEY_PREFIX = "msg_key";

    /**
     * 构建消息Key
     *
     * @param businessType 业务类型
     * @param businessId   业务ID
     * @return 消息Key
     */
    public static String buildMessageKey(String businessType, Long businessId) {
        return MESSAGE_KEY_PREFIX + ":" + businessType + ":" + businessId;
    }

    /**
     * 构建预约消息Key
     *
     * @param appointmentId 预约ID
     * @return 消息Key
     */
    public static String buildAppointmentMessageKey(Long appointmentId) {
        return buildMessageKey("appointment", appointmentId);
    }

    /**
     * 构建用户消息Key
     *
     * @param userId 用户ID
     * @return 消息Key
     */
    public static String buildUserMessageKey(Long userId) {
        return buildMessageKey("user", userId);
    }
}