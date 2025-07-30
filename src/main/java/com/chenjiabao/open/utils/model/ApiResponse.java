package com.chenjiabao.open.utils.model;

import com.chenjiabao.open.utils.TimeUtils;
import com.chenjiabao.open.utils.enums.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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
    public static Builder builder(ResponseCode code) {
        return new Builder().code(code);
    }

    /**
     * 使用指定的状态码和消息创建一个Builder
     * @param message 消息
     * @return Builder实例
     */
    public static ResponseEntity<ApiResponse> success(String message){
        return new Builder().message(message).build();
    }

    /**
     * 使用成功状态码和数据创建一个Builder
     * @param data 响应数据
     * @return Builder实例
     */
    public static ResponseEntity<ApiResponse> success(Map<String, Object> data) {
        return new Builder().data(data).build();
    }

    /**
     * 使用成功状态码创建一个Builder
     * @return Builder实例
     */
    public static ResponseEntity<ApiResponse> success() {
        return new Builder().build();
    }

    /**
     * 使用错误状态码创建一个Builder
     * @param code 错误状态码
     * @return Builder实例
     */
    public static ResponseEntity<ApiResponse> error(ResponseCode code) {
        return new Builder().code(code).build();
    }

    /**
     * 使用错误状态码和自定义消息创建一个Builder
     * @param code 错误状态码
     * @param message 自定义错误消息
     * @return Builder实例
     */
    public static ResponseEntity<ApiResponse> error(ResponseCode code, String message) {
        return new Builder().code(code).message(message).build();
    }

    /**
     * ApiResponse的Builder类
     */
    public static class Builder {
        private ResponseCode code = ResponseCode.CODE_200;
        private String message = null;
        private Map<String, Object> data = null;

        private Builder() {
        }

        /**
         * 设置状态码
         * @param code 状态码
         * @return Builder实例
         */
        public Builder code(ResponseCode code) {
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
         * 获取ApiResponse实例
         * @return ApiResponse实例
         */
        public ApiResponse get(){
            return new ApiResponse(code.getValue(), message, data);
        }

        /**
         * 构建ResponseEntity<ApiResponse>
         * @return ResponseEntity<ApiResponse>
         */
        public ResponseEntity<ApiResponse> build() {
            int codeValue = code.getValue();
            String msg = message != null ? message : code.getMessage();
            return build(new ApiResponse(codeValue, msg, data));
        }

        /**
         * 构建ResponseEntity<ApiResponse>
         * @param apiResponse ApiResponse实例
         * @return ResponseEntity<ApiResponse>
         */
        public ResponseEntity<ApiResponse> build(ApiResponse apiResponse){
            return ResponseEntity
                    .status(apiResponse.getCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Allow-Origin", "*")
                    .body(apiResponse);
        }
    }
}
