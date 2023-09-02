package com.aug.common.result;

import lombok.Getter;

/**
 * @author querkecor
 * @date 2023/4/23
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201,"失败"),
    LOGIN_MOBLE_ERROR(204,"认证失败，请重新登录"),
    PERMISSION(208,"用户未登录，请先登录再访问");

    private final Integer code;
    private final String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
