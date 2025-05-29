package com.chenjiabao.open.utils.enums;

/**
 * 请求方式
 * @author ChenJiaBao
 */
public enum HttpMethod {
    HTTP_METHOD_GET("GET"),
    HTTP_METHOD_POST("POST");

    private final String value;

    HttpMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
