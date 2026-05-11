package com.condominio.persona.common.exception;

import com.condominio.persona.common.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public ExternalServiceException(String message){
        super(message);
        this.errorCode = ErrorCode.EXTERNAL_RESOURCE_ERROR;
    }

    public ExternalServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
