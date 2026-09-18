package org.com.lab.error;

import org.example.javaframework.web.common.InterfaceErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements InterfaceErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED);

    private final HttpStatus httpStatus;

    AuthErrorCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return name();
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
