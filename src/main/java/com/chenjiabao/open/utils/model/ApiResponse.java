package com.chenjiabao.open.utils.model;

import com.chenjiabao.open.utils.TimeUtils;
import com.chenjiabao.open.utils.enums.RequestCode;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;

/**
 * 接口返回类
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // 不序列化空属性
public class ApiResponse {
    private int code = 200;
    private String message = "成功";
    private HashMap<String,Object> data = null;
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
}
