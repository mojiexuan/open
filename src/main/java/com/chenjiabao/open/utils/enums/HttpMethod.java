package com.chenjiabao.open.utils.enums;

/**
 * 请求方式
 */
public enum HttpMethod {
    HTTP_METHOD_GET("GET"),
    HTTP_METHOD_POST("POST");

    private final String name;

    HttpMethod(String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }
}
