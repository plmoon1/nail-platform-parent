package com.nail.platform.reserve.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 系统操作日志实体
 * 对应数据库表 sys_operation_log
 *
 * @author nail-platform
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_operation_log")
public class SysOperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;

    /**
     * 操作人ID
     */
    @TableField("operator_id")
    private Long operatorId;

    /**
     * 操作人名称
     */
    @TableField("operator_name")
    private String operatorName;

    /**
     * 操作模块
     */
    @TableField("module")
    private String module;

    /**
     * 操作类型
     */
    @TableField("operation")
    private String operation;

    /**
     * 操作描述
     */
    @TableField("description")
    private String description;

    /**
     * 请求接口地址
     */
    @TableField("request_url")
    private String requestUrl;

    /**
     * 请求方式
     */
    @TableField("request_method")
    private String requestMethod;

    /**
     * 操作人IP地址
     */
    @TableField("request_ip")
    private String requestIp;

    /**
     * 请求参数
     */
    @TableField("request_params")
    private String requestParams;

    /**
     * 响应结果
     */
    @TableField("response_result")
    private String responseResult;

    /**
     * 接口执行耗时(ms)
     */
    @TableField("cost_time")
    private Long costTime;

    /**
     * 日志级别
     */
    @TableField("log_level")
    private String logLevel;

    /**
     * 异常信息
     */
    @TableField("exception_info")
    private String exceptionInfo;

    /**
     * 操作时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
}