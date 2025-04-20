package com.chenjiabao.open.utils.model;

import com.chenjiabao.open.utils.TimeUtils;
import com.chenjiabao.open.utils.enums.RequestCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回类
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // 不序列化空属性
public class ApiResponse {
    private int code = 200;
    private String message = "成功";
    private Map<String,Object> data = null;
    private String time = TimeUtils.getNowTime();

    public ApiResponse() {
    }

    public ApiResponse(RequestCode code, String message) {
        this.code = code.getValue();
        this.message = message;
    }

    public ApiResponse(RequestCode code, String message, HashMap<String,Object> data) {
        this.code = code.getValue();
        this.message = message;
        this.data = data;
    }

    public void setCode(RequestCode code) {
        this.code = code.getValue();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
