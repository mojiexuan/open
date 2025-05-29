package com.chenjiabao.open.utils.enums;

/**
 * 请求状态
 * @author ChenJiaBao
 */
public enum RequestCode {

    CODE_200(200,"成功"),
    CODE_304(304,"发生错误，请求资源未被修改"),
    CODE_401(401,"请求未授权，需要身份验证"),
    CODE_403(403,"服务器拒绝请求"),
    CODE_404(404,"请求资源不存在"),
    CODE_405(405,"请求方法不被允许"),
    CODE_406(406,"不接受的请求内容"),
    CODE_429(429,"请求频率受限"),
    CODE_500(500,"服务器内部错误");

    private final int code;
    private final String message;

    RequestCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getValue() {
        return code;
    }
    public int getCode(){
        return code;
    }
    public String getMessage() {
        return message;
    }
}
