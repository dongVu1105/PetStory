package com.dongVu1105.identity_service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1000, "this exception is not categorized", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "user not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED (1003, "unauthenticad exception", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED (1004, "unauthorized exception", HttpStatus.FORBIDDEN),
    USER_EXISTED(1005, "user existed", HttpStatus.BAD_REQUEST),
    ROLE_NOT_EXISTED(1006, "role not existed", HttpStatus.NOT_FOUND)

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
