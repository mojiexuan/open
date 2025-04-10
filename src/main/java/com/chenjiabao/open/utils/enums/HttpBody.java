package com.chenjiabao.open.utils.enums;

public enum HttpBody {

    JSON("json"),
    XML("xml"),
    FORM_DATA("form-data"),
    ;

    private final String name;

    HttpBody(String name) {
        this.name = name;
    }

    public String getValue() {
        return name;
    }
}
