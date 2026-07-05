package com.nail.common.exception;

import com.nail.common.result.Result;
import com.nail.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局统一异常拦截器
 * 统一处理所有异常，返回规范的 Result 格式
 *
 * @author nail-platform
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 自定义业务异常
     */
    @ExceptionHandler(GlobalException.class)
    public Result<?> handleGlobalException(GlobalException e, HttpServletRequest request) {
        log.warn("业务异常: uri={}, code={}, message={}, detail={}",
                request.getRequestURI(), e.getCode(), e.getMessage(), e.getDetail());

        Result<Object> result = Result.error(e.getCode(), e.getMessage());
        result.setPath(request.getRequestURI());

        // 如果有错误详情，可以在开发环境返回
        if (e.getDetail() != null) {
            log.debug("异常详情: {}", e.getDetail());
        }

        return result;
    }

    /**
     * 参数校验异常（RequestBody @Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        log.warn("参数校验异常: uri={}, errors={}", request.getRequestURI(), errorMsg);

        Result<Object> result = Result.error(ResultCode.PARAM_ERROR.getCode(), errorMsg);
        result.setPath(request.getRequestURI());
        return result;
    }

    /**
     * 参数绑定异常（表单提交 @Valid）
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e, HttpServletRequest request) {
        String errorMsg = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        log.warn("参数绑定异常: uri={}, errors={}", request.getRequestURI(), errorMsg);

        Result<Object> result = Result.error(ResultCode.PARAM_ERROR.getCode(), errorMsg);
        result.setPath(request.getRequestURI());
        return result;
    }

    /**
     * 参数约束违反异常（PathVariable @RequestParam @Validated）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        log.warn("参数约束异常: uri={}, errors={}", request.getRequestURI(), errorMsg);

        Result<Object> result = Result.error(ResultCode.PARAM_ERROR.getCode(), errorMsg);
        result.setPath(request.getRequestURI());
        return result;
    }

    /**
     * 参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String errorMsg = String.format("参数类型错误: %s", e.getName());

        log.warn("参数类型异常: uri={}, param={}", request.getRequestURI(), e.getName());

        Result<Object> result = Result.error(ResultCode.PARAM_TYPE_ERROR.getCode(), errorMsg);
        result.setPath(request.getRequestURI());
        return result;
    }

    /**
     * 404 异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("404异常: uri={}, method={}", request.getRequestURI(), e.getHttpMethod());

        Result<Object> result = Result.error(ResultCode.NOT_FOUND);
        result.setPath(request.getRequestURI());
        return result;
    }

    /**
     * 其他未处理的异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: uri={}, message={}", request.getRequestURI(), e.getMessage(), e);

        Result<Object> result = Result.error(ResultCode.INTERNAL_SERVER_ERROR);
        result.setPath(request.getRequestURI());
        return result;
    }

    /**
     * 运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: uri={}, message={}", request.getRequestURI(), e.getMessage(), e);

        Result<Object> result = Result.error(ResultCode.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
        result.setPath(request.getRequestURI());
        return result;
    }
}