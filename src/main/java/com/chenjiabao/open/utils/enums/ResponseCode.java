package com.chenjiabao.open.utils.enums;

import lombok.Getter;

/**
 * 请求状态
 * @author ChenJiaBao
 */
@Getter
public enum ResponseCode {

    CODE_200(200,"成功"),
    CODE_201(201, "资源创建成功"),
    CODE_202(202, "请求已接受但未处理完成"),
    CODE_204(204, "请求成功，但无返回内容"),
    CODE_303(303, "请查看其他URI"),
    CODE_304(304,"发生错误，请求资源未被修改"),
    CODE_400(400, "请求参数错误"),
    CODE_401(401,"请求未授权，需要身份验证"),
    CODE_402(402, "需要付费"),
    CODE_403(403,"服务器拒绝请求"),
    CODE_404(404,"请求资源不存在"),
    CODE_405(405,"请求方法不被允许"),
    CODE_406(406,"不接受的请求内容"),
    CODE_408(408, "请求超时"),
    CODE_412(412, "前置条件不满足"),
    CODE_413(413, "请求体过大"),
    CODE_415(415, "不支持的媒体类型"),
    CODE_422(422, "请求语义正确但包含逻辑错误"),
    CODE_429(429,"请求频率受限"),
    CODE_500(500,"服务器内部错误"),
    CODE_501(501, "功能未实现"),
    CODE_502(502, "网关错误"),
    CODE_503(503, "服务不可用"),
    CODE_504(504, "网关超时");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getValue() {
        return code;
    }
}
