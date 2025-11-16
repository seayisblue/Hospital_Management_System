package com.template.common;

/**
 * 统一响应状态码枚举
 *
 * @author template
 */
public enum ResultCode {

    /* 成功状态码 */
    SUCCESS(200, "操作成功"),

    /* 失败状态码 */
    FAIL(400, "操作失败"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999 */
    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_HAS_EXISTED(20005, "用户已存在"),
    USER_PASSWORD_ERROR(20006, "密码错误"),

    /* 权限错误：30001-39999 */
    PERMISSION_NO_ACCESS(30001, "无访问权限"),
    PERMISSION_TOKEN_INVALID(30002, "Token无效"),
    PERMISSION_TOKEN_EXPIRED(30003, "Token已过期"),

    /* 业务错误：40001-49999 */
    BUSINESS_ERROR(40001, "业务处理失败"),
    DATA_NOT_EXIST(40002, "数据不存在"),
    DATA_IS_WRONG(40003, "数据有误"),
    DATA_ALREADY_EXISTED(40004, "数据已存在"),

    /* 系统错误：50001-59999 */
    SYSTEM_INNER_ERROR(50001, "系统内部错误"),
    SYSTEM_TIMEOUT(50002, "系统超时"),
    SYSTEM_BUSY(50003, "系统繁忙，请稍后重试"),

    /* 数据库错误：60001-69999 */
    DATABASE_ERROR(60001, "数据库操作失败"),
    DATABASE_INSERT_ERROR(60002, "新增数据失败"),
    DATABASE_UPDATE_ERROR(60003, "更新数据失败"),
    DATABASE_DELETE_ERROR(60004, "删除数据失败"),

    /* 接口错误：70001-79999 */
    INTERFACE_INNER_INVOKE_ERROR(70001, "内部系统接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR(70002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(70003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(70004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(70005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(70006, "接口负载过高"),

    /* 文件错误：80001-89999 */
    FILE_UPLOAD_ERROR(80001, "文件上传失败"),
    FILE_DOWNLOAD_ERROR(80002, "文件下载失败"),
    FILE_TYPE_ERROR(80003, "文件类型不支持"),
    FILE_SIZE_EXCEED(80004, "文件大小超出限制"),
    FILE_NOT_EXIST(80005, "文件不存在");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 消息
     */
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultCode{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}

