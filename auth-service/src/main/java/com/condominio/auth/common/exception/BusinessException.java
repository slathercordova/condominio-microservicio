package com.condominio.auth.common.exception;

import com.condominio.auth.common.enums.ErrorCode;

public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(String message) {
        super(message);
        this.errorCode = ErrorCode.BUSINESS_ERROR;
    }

    public BusinessException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
