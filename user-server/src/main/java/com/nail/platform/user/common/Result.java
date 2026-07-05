package com.nail.platform.user.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结构。后续可抽取到公共 common 模块。
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_FAIL = 500;

    private int code;
    private String message;
    private T data;

    public Result() {
    }

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Result<T> ok() {
        return new Result<>(CODE_SUCCESS, "success", null);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(CODE_SUCCESS, "success", data);
    }

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(CODE_SUCCESS, message, data);
    }

    public static <T> Result<T> fail() {
        return new Result<>(CODE_FAIL, "fail", null);
    }

    public static <T> Result<T> fail(String message) {
        return new Result<>(CODE_FAIL, message, null);
    }

    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    public boolean isSuccess() {
        return CODE_SUCCESS == code;
    }
}