package com.nail.common.result;

/**
 * 全局状态码枚举
 *
 * @author nail-platform
 */
public enum ResultCode {

    /** ========== 成功 ========== */
    SUCCESS(200, "操作成功"),

    /** ========== 客户端错误 4xx ========== */
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权，请先登录"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    /** ========== 业务错误 5xx ========== */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务暂不可用"),

    /** ========== 自定义业务错误码 6xxx ========== */
    USER_NOT_EXIST(6001, "用户不存在"),
    USER_ALREADY_EXIST(6002, "用户已存在"),
    PASSWORD_ERROR(6003, "密码错误"),
    ACCOUNT_DISABLED(6004, "账号已禁用"),
    TOKEN_EXPIRED(6005, "Token已过期"),
    TOKEN_INVALID(6006, "Token无效"),

    /** ========== 参数错误 6xxx ========== */
    PARAM_ERROR(7001, "参数错误"),
    PARAM_MISSING(7002, "缺少必要参数"),
    PARAM_TYPE_ERROR(7003, "参数类型错误"),

    /** ========== 资源错误 8xxx ========== */
    RESOURCE_NOT_FOUND(8001, "资源不存在"),
    RESOURCE_ALREADY_EXISTS(8002, "资源已存在"),
    RESOURCE_DELETED(8003, "资源已删除"),

    /** ========== 业务操作错误 9xxx ========== */
    OPERATION_FAILED(9001, "操作失败"),
    OPERATION_NOT_ALLOWED(9002, "不允许的操作"),
    DUPLICATE_OPERATION(9003, "重复操作");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}