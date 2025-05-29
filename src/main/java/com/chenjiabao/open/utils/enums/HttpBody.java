package com.chenjiabao.open.utils.enums;

/**
 * @author ChenJiaBao
 */

public enum HttpBody {

    JSON("json"),
    XML("xml"),
    FORM_DATA("form-data"),
    ;

    private final String value;

    HttpBody(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
