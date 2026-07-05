package com.nail.platform.reserve.service;

import com.nail.common.dto.OperationLogDTO;
import com.nail.platform.reserve.entity.SysOperationLog;

/**
 * 系统操作日志服务接口
 *
 * @author nail-platform
 */
public interface SysOperationLogService {

    /**
     * 保存操作日志
     *
     * @param logDTO 操作日志DTO
     */
    void save(OperationLogDTO logDTO);

    /**
     * 保存操作日志
     *
     * @param sysOperationLog 操作日志实体
     */
    void save(SysOperationLog sysOperationLog);
}