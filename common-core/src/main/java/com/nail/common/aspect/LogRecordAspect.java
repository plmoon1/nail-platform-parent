package com.nail.common.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nail.common.annotation.LogRecord;
import com.nail.common.dto.OperationLogDTO;
import com.nail.common.handler.OperationLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

/**
 * 操作日志切面
 * 拦截标注了 @LogRecord 注解的方法，自动记录操作日志
 *
 * @author nail-platform
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogRecordAspect {

    private final ObjectMapper objectMapper;
    private final OperationLogHandler operationLogHandler;

    /**
     * 环绕通知：拦截 @LogRecord 注解的方法
     */
    @Around("@annotation(com.nail.common.annotation.LogRecord)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 开始时间
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 获取方法签名和注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LogRecord logRecord = method.getAnnotation(LogRecord.class);

        // 构建日志DTO
        OperationLogDTO logDTO = new OperationLogDTO();
        logDTO.setTraceId(UUID.randomUUID().toString().replace("-", ""));
        logDTO.setModule(logRecord.module());
        logDTO.setOperation(logRecord.operation());
        logDTO.setDescription(logRecord.description());
        logDTO.setLogLevel(logRecord.level().name());

        if (request != null) {
            logDTO.setRequestUrl(request.getRequestURI());
            logDTO.setRequestMethod(request.getMethod());
            logDTO.setRequestIp(getIpAddress(request));
        }

        // 记录请求参数
        if (logRecord.logParams()) {
            try {
                Object[] args = joinPoint.getArgs();
                String paramsJson = args.length > 0 ? objectMapper.writeValueAsString(args) : "";
                // 限制参数长度，避免过长
                logDTO.setRequestParams(paramsJson.length() > 2000 ?
                    paramsJson.substring(0, 2000) + "..." : paramsJson);
            } catch (Exception e) {
                log.warn("序列化请求参数失败", e);
                logDTO.setRequestParams("参数序列化失败");
            }
        }

        Object result = null;
        Throwable exception = null;

        try {
            // 执行目标方法
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            exception = e;
            logDTO.setLogLevel(LogRecord.LogLevel.ERROR.name());
            logDTO.setExceptionInfo(getStackTrace(e));
            throw e;
        } finally {
            // 计算耗时
            long costTime = System.currentTimeMillis() - startTime;
            if (logRecord.logCostTime()) {
                logDTO.setCostTime(costTime);
            }

            // 记录响应结果
            if (logRecord.logResult() && exception == null) {
                try {
                    String resultJson = result != null ? objectMapper.writeValueAsString(result) : "";
                    // 限制结果长度，避免过长
                    logDTO.setResponseResult(resultJson.length() > 2000 ?
                        resultJson.substring(0, 2000) + "..." : resultJson);
                } catch (Exception e) {
                    log.warn("序列化响应结果失败", e);
                    logDTO.setResponseResult("结果序列化失败");
                }
            }

            logDTO.setCreateTime(LocalDateTime.now());

            // 异步保存日志
            saveOperationLog(logDTO);

            log.debug("操作日志记录成功: module={}, operation={}, costTime={}ms",
                logDTO.getModule(), logDTO.getOperation(), costTime);
        }
    }

    /**
     * 保存操作日志
     * 通过接口抽象，由各个服务模块自行实现
     */
    private void saveOperationLog(OperationLogDTO logDTO) {
        try {
            operationLogHandler.handle(logDTO);
        } catch (Exception e) {
            log.error("保存操作日志失败: module={}, operation={}",
                logDTO.getModule(), logDTO.getOperation(), e);
        }
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多次反向代理后会有多个IP值，第一个才是真实IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }

    /**
     * 获取异常堆栈信息
     */
    private String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(throwable.getClass().getName()).append(": ").append(throwable.getMessage()).append("\n");
        Arrays.stream(throwable.getStackTrace())
            .forEach(element -> sb.append("\tat ").append(element).append("\n"));

        // 限制堆栈长度
        String stackTrace = sb.toString();
        return stackTrace.length() > 2000 ? stackTrace.substring(0, 2000) + "..." : stackTrace;
    }
}
