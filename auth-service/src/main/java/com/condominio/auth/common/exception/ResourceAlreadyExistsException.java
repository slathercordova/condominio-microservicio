package com.condominio.auth.common.exception;

import com.condominio.auth.common.enums.ErrorCode;

public class ResourceAlreadyExistsException extends RuntimeException {
    private final ErrorCode errorCode;

    public ResourceAlreadyExistsException(String message) {
        super(message);
        this.errorCode = ErrorCode.DATA_ALREADY_EXIST_ERROR;
    }

    public ResourceAlreadyExistsException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
