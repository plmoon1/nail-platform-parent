package com.nail.common.annotation;

import java.lang.annotation.*;

/**
 * 接口操作日志注解
 * 标注在Controller方法上，自动记录接口操作日志
 *
 * 使用示例：
 * <pre>
 * {@code
 * @PostMapping("/user/update")
 * @LogRecord(module = "用户管理", operation = "更新用户信息", description = "更新用户基本信息")
 * public Result<?> updateUser(@RequestBody UserDTO userDTO) {
 *     return Result.success();
 * }
 * }
 * </pre>
 *
 * @author nail-platform
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogRecord {

    /**
     * 模块名称
     * 如：用户管理、预约管理、服务管理等
     *
     * @return 模块名称
     */
    String module();

    /**
     * 操作类型
     * 如：新增、修改、删除、查询、导入、导出等
     *
     * @return 操作类型
     */
    String operation();

    /**
     * 操作描述
     * 详细描述本次操作的具体内容
     *
     * @return 操作描述
     */
    String description() default "";

    /**
     * 是否记录请求参数
     * 默认为true，记录请求参数
     *
     * @return 是否记录请求参数
     */
    boolean logParams() default true;

    /**
     * 是否记录响应结果
     * 默认为false，不记录响应结果（响应结果可能较大）
     *
     * @return 是否记录响应结果
     */
    boolean logResult() default false;

    /**
     * 是否记录耗时
     * 默认为true，记录接口执行耗时
     *
     * @return 是否记录耗时
     */
    boolean logCostTime() default true;

    /**
     * 日志级别
     * 默认为INFO
     *
     * @return 日志级别
     */
    LogLevel level() default LogLevel.INFO;

    /**
     * 日志级别枚举
     */
    enum LogLevel {
        /** 调试级别 */
        DEBUG,
        /** 信息级别 */
        INFO,
        /** 警告级别 */
        WARN,
        /** 错误级别 */
        ERROR
    }
}
