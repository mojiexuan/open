package com.chenjiabao.open.utils;

import com.chenjiabao.open.utils.enums.ResponseCode;
import com.chenjiabao.open.utils.model.property.Time;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 接口响应类
 * @author 陈佳宝 mail@chenjiabao.com
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL) // 不序列化空属性
public class BaoServerResponse {
    private int code = 200;
    private String message = "成功";
    private Map<String, Object> data = null;
    private final String time = new TimeUtils(new Time()).getNowTime();

    private BaoServerResponse() {}

    private BaoServerResponse(int code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 构造
     * @return Builder实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 构建响应类
     * @author 陈佳宝 mail@chenjiabao.com
     */
    public static class Builder{
        private int code = ResponseCode.CODE_200.getValue();
        private String message = ResponseCode.CODE_200.getMessage();
        private Map<String, Object> data = null;

        /**
         * 设置响应码
         * @param code 响应码
         * @return Builder实例
         */
        public Builder setCode(ResponseCode code) {
            this.code = code.getValue();
            this.message = code.getMessage();
            return this;
        }

        /**
         * 设置响应消息
         * @param message 响应消息
         * @return Builder实例
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * 设置响应数据
         * @param data 响应数据
         * @return Builder实例
         */
        public Builder setData(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        /**
         * 添加响应数据
         * @param key 键
         * @param value 值
         * @return Builder实例
         */
        public Builder addData(String key, Object value) {
            if (this.data == null) {
                data = new HashMap<>();
            }
            data.put(key, value);
            return this;
        }

        /**
         * 构建失败响应
         * @return Builder实例
         */
        public Builder fail() {
            return fail(ResponseCode.CODE_500);
        }

        /**
         * 构建失败响应
         * @param responseCode 响应码
         * @return Builder实例
         */
        public Builder fail(ResponseCode responseCode) {
            this.code = responseCode.getValue();
            this.message = responseCode.getMessage();
            return this;
        }

        /**
         * 构建BaoServerResponse实例
         * @return BaoServerResponse实例
         */
        public BaoServerResponse build() {
            BaoServerResponse response = new BaoServerResponse();
            response.setCode(code);
            response.setMessage(message);
            if (data != null && !data.isEmpty()) {
                response.setData(data);
            }
            // 重置默认值
            this.code = ResponseCode.CODE_200.getValue();
            this.message = ResponseCode.CODE_200.getMessage();
            this.data = null;
            return response;
        }

        /**
         * 构建ResponseEntity实例
         * @return ResponseEntity实例
         */
        public ResponseEntity<BaoServerResponse> buildResponseEntity() {
            BaoServerResponse response = build();
            return new ResponseEntity<>(
                    response,
                    HttpStatus.valueOf(response.getCode()));
        }

        /**
         * 构建Mono实例
         * @return Mono实例
         */
        public Mono<ResponseEntity<BaoServerResponse>>  buildMonoResponseEntity() {
            return Mono.just(buildResponseEntity());
        }
    }
}
