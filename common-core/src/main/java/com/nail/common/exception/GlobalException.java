package com.nail.common.exception;

import com.nail.common.result.ResultCode;
import lombok.Getter;

/**
 * 自定义业务异常
 * 业务代码中抛出此异常，会被 ExceptionAdvice 统一拦截处理
 *
 * @author nail-platform
 */
@Getter
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 错误详情（可选，用于调试）
     */
    private final String detail;

    public GlobalException(String message) {
        super(message);
        this.code = ResultCode.INTERNAL_SERVER_ERROR.getCode();
        this.message = message;
        this.detail = null;
    }

    public GlobalException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.detail = null;
    }

    public GlobalException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.detail = null;
    }

    public GlobalException(ResultCode resultCode, String detail) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.detail = detail;
    }

    public GlobalException(Integer code, String message, String detail) {
        super(message);
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public GlobalException(ResultCode resultCode, Throwable cause) {
        super(resultCode.getMessage(), cause);
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.detail = cause != null ? cause.getMessage() : null;
    }

    @Override
    public String toString() {
        return "GlobalException{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
