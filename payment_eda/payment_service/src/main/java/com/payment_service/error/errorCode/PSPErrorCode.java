package com.payment_service.error.errorCode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PSPErrorCode implements ErrorCode {
    CLIENT_ERROR("1400001", HttpStatus.BAD_REQUEST.value()),
    SERVER_ERROR("1400002", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    ;

    private final String code;
    private final int httpStatus;

    PSPErrorCode(String code, int httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getCodePath() {
        return "error.psp." + code;
    }

    @Override
    public int getHttpStatus() {
        return httpStatus;
    }
}