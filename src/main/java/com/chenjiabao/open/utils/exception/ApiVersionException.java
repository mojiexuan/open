package com.chenjiabao.open.utils.exception;

/**
 * Api 版本异常
 * @author ChenJiaBao
 */
public class ApiVersionException extends RuntimeException {
    public ApiVersionException(String message) {
        super(message);
    }
}
