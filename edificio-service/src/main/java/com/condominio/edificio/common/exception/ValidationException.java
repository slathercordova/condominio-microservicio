package com.condominio.edificio.common.exception;

import com.condominio.edificio.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final ErrorCode errorCode;

    public ValidationException(String message) {
        super(message);
        this.errorCode = ErrorCode.VALIDATION_ERROR;
    }

    public ValidationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
