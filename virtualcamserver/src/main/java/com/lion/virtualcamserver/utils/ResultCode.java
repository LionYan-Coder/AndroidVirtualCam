package com.lion.virtualcamserver.utils;

public enum ResultCode implements IErrorCode {
    SUCCESS(200L, "操作成功"),
    FAILED(500L, "操作失败");

    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}