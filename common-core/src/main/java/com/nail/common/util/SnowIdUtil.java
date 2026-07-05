package com.nail.common.util;

/**
 * 雪花算法ID生成器
 * 生成64位Long类型唯一ID，用于所有数据库表主键
 *
 * @author nail-platform
 */
public class SnowIdUtil {

    /**
     * 起始时间戳 (2023-01-01 00:00:00)
     */
    private static final long START_TIMESTAMP = 1672531200000L;

    /**
     * 数据中心ID所占位数
     */
    private static final long DATACENTER_ID_BITS = 5L;

    /**
     * 机器ID所占位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 序列号所占位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 数据中心ID最大值
     */
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    /**
     * 机器ID最大值
     */
    private static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 序列号最大值
     */
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器ID左移位数
     */
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 数据中心ID左移位数
     */
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间戳左移位数
     */
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    /**
     * 数据中心ID
     */
    private final long datacenterId;

    /**
     * 机器ID
     */
    private final long workerId;

    /**
     * 序列号
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间戳
     */
    private long lastTimestamp = -1L;

    /**
     * 单例实例
     */
    private static volatile SnowIdUtil instance;

    /**
     * 私有构造函数
     *
     * @param datacenterId 数据中心ID (0-31)
     * @param workerId     机器ID (0-31)
     */
    private SnowIdUtil(long datacenterId, long workerId) {
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    /**
     * 获取单例实例（默认配置）
     *
     * @return SnowIdUtil实例
     */
    public static SnowIdUtil getInstance() {
        if (instance == null) {
            synchronized (SnowIdUtil.class) {
                if (instance == null) {
                    instance = new SnowIdUtil(1L, 1L);
                }
            }
        }
        return instance;
    }

    /**
     * 获取单例实例（自定义配置）
     *
     * @param datacenterId 数据中心ID
     * @param workerId     机器ID
     * @return SnowIdUtil实例
     */
    public static SnowIdUtil getInstance(long datacenterId, long workerId) {
        if (instance == null) {
            synchronized (SnowIdUtil.class) {
                if (instance == null) {
                    instance = new SnowIdUtil(datacenterId, workerId);
                }
            }
        }
        return instance;
    }

    /**
     * 生成下一个ID（线程安全）
     *
     * @return Long类型ID
     */
    public synchronized long nextId() {
        long timestamp = getCurrentTimestamp();

        // 时钟回拨检查
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                    lastTimestamp - timestamp));
        }

        // 同一毫秒内
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 序列号溢出
            if (sequence == 0) {
                timestamp = waitNextMillis(timestamp);
            }
        } else {
            // 不同毫秒，序列号重置
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        // 生成ID
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence;
    }

    /**
     * 生成字符串ID
     *
     * @return 字符串类型ID
     */
    public String nextIdStr() {
        return String.valueOf(nextId());
    }

    /**
     * 获取当前时间戳
     *
     * @return 当前时间戳（毫秒）
     */
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    /**
     * 等待下一毫秒
     *
     * @param lastTimestamp 上次生成ID的时间戳
     * @return 新的时间戳
     */
    private long waitNextMillis(long lastTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }

    /**
     * 快速生成ID（使用默认配置）
     * 工具方法，直接调用即可
     *
     * @return Long类型ID
     */
    public static Long generate() {
        return getInstance().nextId();
    }

    /**
     * 快速生成字符串ID（使用默认配置）
     * 工具方法，直接调用即可
     *
     * @return 字符串类型ID
     */
    public static String generateStr() {
        return getInstance().nextIdStr();
    }
}
