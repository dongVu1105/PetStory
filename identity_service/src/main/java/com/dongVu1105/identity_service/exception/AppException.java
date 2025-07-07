package com.dongVu1105.identity_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends Exception {
    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    private ErrorCode errorCode;
}
