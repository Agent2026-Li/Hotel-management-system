package com.hrms.common;

/**
 * 业务异常，携带前端可识别的错误码和错误消息。
 */
public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
