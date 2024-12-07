package com.payment_service.error.errorCode;

import org.springframework.http.HttpStatus;

public enum MemberErrorCode implements ErrorCode {

    NOT_VALID_PASSWORD_LENGTH("MEC0001", HttpStatus.BAD_REQUEST.value()),
    ALREADY_JOIN_ID("MFC0002", HttpStatus.BAD_REQUEST.value()),
    NOT_FOUND_MEMBER("MFC0003", HttpStatus.BAD_REQUEST.value());

    private final String code;
    private final int httpStatus;

    MemberErrorCode(String code, int httpStatus) {
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
