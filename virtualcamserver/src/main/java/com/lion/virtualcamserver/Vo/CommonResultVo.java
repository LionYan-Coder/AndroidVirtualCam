package com.lion.virtualcamserver.Vo;

import com.lion.virtualcamserver.utils.IErrorCode;
import com.lion.virtualcamserver.utils.ResultCode;

public class CommonResultVo<T> {
    private long code;
    private String message;
    private T data;

    protected CommonResultVo() {
    }

    protected CommonResultVo(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> CommonResultVo<T> success(T data) {
        return new CommonResultVo(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> CommonResultVo<T> success() {
        return new CommonResultVo(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), (Object)null);
    }

    public static <T> CommonResultVo<T> handle(T data) {
        return data != null ? new CommonResultVo(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data) : failed();
    }

    public static CommonResultVo handle(int result) {
        return result == 1 ? success((Object)null) : failed();
    }

    public static CommonResultVo handle(boolean result) {
        return result ? success((Object)null) : failed();
    }

    public static <T> CommonResultVo<T> success(T data, String message) {
        return new CommonResultVo(ResultCode.SUCCESS.getCode(), message, data);
    }

    public static <T> CommonResultVo<T> failed(IErrorCode errorCode) {
        return new CommonResultVo(errorCode.getCode(), errorCode.getMessage(), (Object)null);
    }

    public static <T> CommonResultVo<T> failed(String message) {
        return new CommonResultVo(ResultCode.FAILED.getCode(), message, (Object)null);
    }

    public static <T> CommonResultVo<T> failed() {
        return failed((IErrorCode)ResultCode.FAILED);
    }


    public static <T> CommonResultVo<T> badResponse(IErrorCode errorCode) {
        return new CommonResultVo(errorCode.getCode(), errorCode.getMessage(), (Object)null);
    }

    public long getCode() {
        return this.code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

