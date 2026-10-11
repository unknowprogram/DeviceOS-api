package com.example.deviceosapi.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 统一业务状态吗
 * */
@Getter
@RequiredArgsConstructor
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "请求参数错误"),
    UNAUTHORIZED(401, "请先登录"),
    FORBIDDEN(403, "没有操作权限"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "数据冲突"),
    BUSINESS_ERROR(422, "业务处理失败"),
    INTERNAL_ERROR(500, "服务器内部错误");
    private final int code;
    private final String message;
}
