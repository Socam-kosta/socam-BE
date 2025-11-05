package org.example.socam_be.exception;

public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND"),
    INVALID_PASSWORD("INVALID_PASSWORD"),
    ORG_NOT_APPROVED("ORG_NOT_APPROVED");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}