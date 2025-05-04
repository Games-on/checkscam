package com.example.checkscam.exception;


import lombok.Getter;
import com.example.checkscam.constant.ErrorCodeEnum;

@Getter
public class EPTException extends Exception {
    private final int code;
    private final String message;

    public EPTException(ErrorCodeEnum errorEnum) {
        this.code = errorEnum.getErrorCode();
        this.message = errorEnum.getMessage();
    }

    public EPTException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
