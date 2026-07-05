package com.nail.common.handler;

import com.nail.common.dto.OperationLogDTO;

/**
 * 操作日志处理器接口
 * 各个服务模块自行实现此接口，决定如何存储操作日志
 *
 * 实现示例：
 * <pre>
 * {@code
 * @Component
 * public class MyOperationLogHandler implements OperationLogHandler {
 *     @Autowired
 *     private SysOperationLogService sysOperationLogService;
 *
 *     @Override
 *     public void handle(OperationLogDTO logDTO) {
 *         sysOperationLogService.save(logDTO);
 *     }
 * }
 * }
 * </pre>
 *
 * @author nail-platform
 */
public interface OperationLogHandler {

    /**
     * 处理操作日志
     * 可以保存到数据库、发送到消息队列、写入日志文件等
     *
     * @param logDTO 操作日志数据
     */
    void handle(OperationLogDTO logDTO);
}