package com.nail.platform.reserve.common.handler;

import com.nail.common.dto.OperationLogDTO;
import com.nail.common.handler.OperationLogHandler;
import com.nail.platform.reserve.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 默认操作日志处理器实现
 * 将操作日志保存到数据库
 *
 * @author nail-platform
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultOperationLogHandler implements OperationLogHandler {

    private final SysOperationLogService sysOperationLogService;

    @Override
    public void handle(OperationLogDTO logDTO) {
        // 异步保存，避免影响接口响应时间
        try {
            sysOperationLogService.save(logDTO);
            log.debug("操作日志保存成功: module={}, operation={}",
                logDTO.getModule(), logDTO.getOperation());
        } catch (Exception e) {
            log.error("操作日志保存失败: module={}, operation={}",
                logDTO.getModule(), logDTO.getOperation(), e);
            // 这里可以添加降级处理，比如写入日志文件或发送到消息队列
        }
    }
}