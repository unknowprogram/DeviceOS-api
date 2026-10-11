package com.example.deviceosapi.common;

import lombok.Getter;

/**
 * 统一业务异常
 * */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public BusinessException( String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(
            ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }
}
