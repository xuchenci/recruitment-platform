package com.recruitment.common.enums;

import lombok.Getter;

/**
 * 业务错误码枚举类
 */
@Getter
public enum ResultCode {

    // ========== 成功状态 ==========
    SUCCESS(200, "操作成功"),

    // ========== 参数错误 (400) ==========
    PARAM_INVALID(400, "参数无效"),
    PARAM_MISSING(400, "缺少必需参数"),

    // ========== 未授权 (401) ==========
    UNAUTHORIZED(401, "未登录或登录已过期"),
    TOKEN_INVALID(401, "Token无效"),
    TOKEN_EXPIRED(401, "Token已过期"),

    // ========== 权限不足 (403) ==========
    FORBIDDEN(403, "没有权限访问该资源"),
    ROLE_FORBIDDEN(403, "当前角色无权访问"),

    // ========== 资源不存在 (404) ==========
    NOT_FOUND(404, "资源不存在"),
    USER_NOT_FOUND(404, "用户不存在"),
    JOB_NOT_FOUND(404, "职位不存在"),

    // ========== 业务逻辑错误 (411) ==========

    // 用户相关
    USER_ALREADY_EXISTS(411, "该用户已存在"),
    PHONE_ALREADY_EXISTS(411, "该手机号已被注册"),
    USERNAME_ALREADY_EXISTS(411, "该用户名已被使用"),
    PASSWORD_ERROR(411, "密码错误"),
    PASSWORD_OR_ACCOUNT_ERROR(411, "账号或密码错误"),
    ACCOUNT_DISABLED(411, "账号已被禁用"),
    ACCOUNT_NOT_ENABLED(411, "账号未激活"),
    USER_STATUS_ABNORMAL(411, "用户状态异常，请联系管理员"),

    // 验证码相关
    VERIFICATION_CODE_INVALID(411, "验证码无效或已过期"),
    VERIFICATION_CODE_EXPIRED(411, "验证码已过期"),
    VERIFICATION_CODE_ERROR(411, "验证码错误"),
    VERIFICATION_CODE_SEND_TOO_FAST(411, "发送过于频繁，请稍后再试"),

    // 二维码登录相关
    QR_CODE_EXPIRED(411, "二维码已过期"),
    QR_CODE_STATUS_ABNORMAL(411, "二维码状态异常"),
    QR_CODE_PLEASE_SCAN_FIRST(411, "请先扫码"),

    // 职位申请相关
    JOB_APPLY_ALREADY_EXISTS(411, "您已经申请过该职位"),
    JOB_APPLY_NOT_ALLOWED(411, "该职位暂不接受申请"),

    // 企业相关
    ENTERPRISE_NOT_CERTIFIED(411, "企业未认证"),
    ENTERPRISE_INFO_INCOMPLETE(411, "企业信息不完整"),

    // ========== 服务器内部错误 (500) ==========
    INTERNAL_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(500, "服务暂不可用"),
    DATABASE_ERROR(500, "数据库操作失败"),

    // ========== 第三方服务错误 (503) ==========
    SMS_SERVICE_ERROR(503, "短信服务暂时不可用"),
    EXTERNAL_API_ERROR(503, "第三方接口调用失败"),

    // ========== 其他 ==========
    UNKNOWN_ERROR(999, "未知错误");

    /**
     * 错误码
     */
    private final int code;

    /**
     * 错误消息
     */
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
