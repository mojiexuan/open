package com.chenjiabao.open.utils.model;

/**
 * HTTP请求结果
 */
public class HttpResponse<T> {
    private int code;
    private String msg;
    private T data;

    public HttpResponse() {
    }

    public HttpResponse(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public HttpResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
