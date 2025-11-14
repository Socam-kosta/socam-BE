package org.example.socam_be.exception;

public enum ErrorCode {
    USER_NOT_FOUND("USER_NOT_FOUND"),
    INVALID_PASSWORD("INVALID_PASSWORD"),
    ORG_NOT_APPROVED("ORG_NOT_APPROVED"),
    ORG_REJECTED("ORG_REJECTED"),

    //lecture 관련 에러 추가
    LECTURE_NOT_FOUND("LECTURE_NOT_FOUND"),
    INVALID_LECTURE_STATUS("INVALID_LECTURE_STATUS"),
    ALREADY_APPROVED("ALREADY_APPROVED"),
    ALREADY_REJECTED("ALREADY_REJECTED");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
