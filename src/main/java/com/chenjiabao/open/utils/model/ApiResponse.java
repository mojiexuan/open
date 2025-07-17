package com.chenjiabao.open.utils.model;

import com.chenjiabao.open.utils.TimeUtils;
import com.chenjiabao.open.utils.enums.RequestCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回类
 * @author 陈佳宝 mail@chenjiabao.com
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // 不序列化空属性
public class ApiResponse {
    private int code = 200;
    private String message = "成功";
    private Map<String, Object> data = null;
    private String time = new TimeUtils().getNowTime();

    private ApiResponse() {
    }

    private ApiResponse(int code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 创建一个ApiResponse的Builder
     * @return Builder实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 使用指定的状态码创建一个Builder
     * @param code 状态码
     * @return Builder实例
     */
    public static Builder builder(RequestCode code) {
        return new Builder().code(code);
    }

    /**
     * 使用指定的状态码和消息创建一个Builder
     * @param message 消息
     * @return Builder实例
     */
    public static Builder success(String message){
        return new Builder().message(message);
    }

    /**
     * 使用成功状态码和数据创建一个Builder
     * @param data 响应数据
     * @return Builder实例
     */
    public static Builder success(Map<String, Object> data) {
        return new Builder().data(data);
    }

    /**
     * 使用成功状态码创建一个Builder
     * @return Builder实例
     */
    public static Builder success() {
        return new Builder();
    }

    /**
     * 使用错误状态码创建一个Builder
     * @param code 错误状态码
     * @return Builder实例
     */
    public static Builder error(RequestCode code) {
        return new Builder().code(code);
    }

    /**
     * 使用错误状态码和自定义消息创建一个Builder
     * @param code 错误状态码
     * @param message 自定义错误消息
     * @return Builder实例
     */
    public static Builder error(RequestCode code, String message) {
        return new Builder().code(code).message(message);
    }

    /**
     * ApiResponse的Builder类
     */
    public static class Builder {
        private RequestCode code = RequestCode.CODE_200;
        private String message = null;
        private Map<String, Object> data = null;

        private Builder() {
        }

        /**
         * 设置状态码
         * @param code 状态码
         * @return Builder实例
         */
        public Builder code(RequestCode code) {
            this.code = code;
            return this;
        }

        /**
         * 设置消息
         * @param message 消息
         * @return Builder实例
         */
        public Builder message(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置数据
         * @param data 数据Map
         * @return Builder实例
         */
        public Builder data(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        /**
         * 添加单个数据项
         * @param key 键
         * @param value 值
         * @return Builder实例
         */
        public Builder addData(String key, Object value) {
            if (data == null) {
                data = new HashMap<>();
            }
            data.put(key, value);
            return this;
        }

        /**
         * 构建ApiResponse实例
         * @return ApiResponse实例
         */
        public ApiResponse build() {
            int codeValue = code.getValue();
            String msg = message != null ? message : code.getMessage();
            return new ApiResponse(codeValue, msg, data);
        }
    }
}
