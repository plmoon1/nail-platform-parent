package com.nail.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 操作日志DTO
 * 用于传输操作日志数据
 *
 * @author nail-platform
 */
@Data
public class OperationLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 追踪ID（用于链路追踪）
     */
    private String traceId;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 操作模块
     * 如：用户管理、预约管理、短信管理
     */
    private String module;

    /**
     * 操作类型
     * 如：新增、修改、删除、查询、导出
     */
    private String operation;

    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求接口地址
     */
    private String requestUrl;

    /**
     * 请求方式 GET/POST/PUT/DELETE
     */
    private String requestMethod;

    /**
     * 操作人IP地址
     */
    private String requestIp;

    /**
     * 接口请求入参
     */
    private String requestParams;

    /**
     * 接口返回结果
     */
    private String responseResult;

    /**
     * 接口执行耗时(ms)
     */
    private Long costTime;

    /**
     * 日志级别 DEBUG/INFO/WARN/ERROR
     */
    private String logLevel;

    /**
     * 异常堆栈信息（报错时记录）
     */
    private String exceptionInfo;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}