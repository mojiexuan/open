package com.chenjiabao.open.utils;

import com.chenjiabao.open.utils.callback.RequestCallback;
import com.chenjiabao.open.utils.enums.HttpMethod;
import com.chenjiabao.open.utils.model.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import okhttp3.*;
import okio.BufferedSource;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 网络请求工具
 */
public class RequestUtils {

    private static final MediaType JsonMediaType = MediaType.parse("application/json");
    private static final MediaType XmlMediaType = MediaType.parse("application/xml; charset=utf-8");

    private static final Gson gson = new Gson();

    private final OkHttpClient okHttpClient;
    private final Request.Builder requestBuilder;
    private String url;
    private HttpMethod method = HttpMethod.HTTP_METHOD_GET;
    private HashMap<String, String> headers = null;
    private HashMap<String, Object> params = null;
    private Request request;

    private RequestUtils(String url) {
        this.url = url;
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true) // 设置失败重试
                .build();
        requestBuilder = new Request.Builder();
    }

    /**
     * 设置请求方式
     *
     * @param method 请求方式，默认GET
     * @return RequestUtils
     */
    public RequestUtils setMethod(HttpMethod method) {
        if (method == null) {
            return this;
        }
        this.method = method;
        return this;
    }

    /**
     * 请求头
     *
     * @param headers 请求头
     * @return RequestUtils
     */
    public RequestUtils setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param params 参数
     * @return RequestUtils
     */
    public RequestUtils setParams(HashMap<String, Object> params) {
        this.params = params;
        return this;
    }

    /**
     * 开始同步请求，返回data为请求结果字符串
     * @return code==200时请求成功，请求结果在data中
     */
    public HttpResponse<String> start(){
        HttpResponse<String> httpResponse = new HttpResponse<>();
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        try {
            Response response = this.okHttpClient.newCall(this.request).execute();
            if (!response.isSuccessful() || response.body() == null) {
                httpResponse.setCode(response.code());
                httpResponse.setMsg("失败");
            }else {
                httpResponse.setCode(200);
                httpResponse.setMsg("成功");
                httpResponse.setData(response.body().string());
            }
        } catch (IOException e) {
            httpResponse.setCode(1001);
            httpResponse.setMsg("请求错误");
        }
        return httpResponse;
    }

    /**
     * 开始同步请求,并尝试解析json格式字符串
     * @return code==200时请求成功，请求结果在data中，若解析字符串失败code==1004
     */
    public <T> HttpResponse<T> start(Class<T> clazz) {
        HttpResponse<T> httpResponse = new HttpResponse<>();
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        try {
            Response response = this.okHttpClient.newCall(this.request).execute();
            if (!response.isSuccessful() || response.body() == null) {
                httpResponse.setCode(response.code());
                httpResponse.setMsg("失败");
            }else {
                try {
                    httpResponse.setData(gson.fromJson(response.body().string(), clazz));
                    httpResponse.setCode(200);
                    httpResponse.setMsg("成功");
                } catch (JsonSyntaxException | IOException e) {
                    httpResponse.setCode(1004);
                    httpResponse.setMsg("解析字符串失败");
                }
            }
        } catch (IOException e) {
            httpResponse.setCode(1001);
            httpResponse.setMsg("请求错误");
        }
        return httpResponse;
    }

    /**
     * 开始异步请求,code==200时请求成功，请求结果在data中
     * @param callback 回调类
     */
    public void start(RequestCallback<String> callback) {
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        this.okHttpClient.newCall(this.request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onSuccess(new HttpResponse<>(1001, "失败"));
                }else {
                    callback.onSuccess(new HttpResponse<>(200, "成功",response.body().string()));
                }
                callback.complete();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFail(
                        new HttpResponse<>(1001,"请求错误")
                );
                callback.complete();
            }
        });
    }

    /**
     * 开始异步请求,code==200时请求成功，请求结果在data中
     * @param callback 回调类
     */
    public <T> void start(Class<T> clazz,RequestCallback<T> callback) {
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        this.okHttpClient.newCall(this.request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onSuccess(new HttpResponse<>(1002,"失败"));
                }else {
                    try {
                        callback.onSuccess(new HttpResponse<>(
                                200,
                                "成功",
                                gson.fromJson(response.body().string(),clazz)
                        ));
                    }catch (JsonSyntaxException | IOException e) {
                        callback.onSuccess(new HttpResponse<>(1004, "解析字符串失败"));
                    }
                }
                callback.complete();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFail(
                        new HttpResponse<>(1001,"请求错误")
                );
                callback.complete();
            }
        });
    }

    /**
     * 流式请求，内容在data中，字符串格式
     */
    public void stream(RequestCallback<String> callback){
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        this.okHttpClient.newCall(this.request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onSuccess(new HttpResponse<>(1002,"失败"));
                }else {
                    try(BufferedSource source = response.body().source()){
                        String line;
                        while ((line = source.readUtf8Line()) != null) {
                            callback.onSuccess(new HttpResponse<>(
                                    200,
                                    "成功",
                                    line
                            ));
                        }
                    }
                }
                response.close();
                callback.complete();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFail(
                        new HttpResponse<>(1001,"请求错误")
                );
                callback.complete();
            }
        });
    }

    /**
     * 开始添加参数
     */
    private void startAddHeaders() {
        if (this.headers != null) {
            this.headers.forEach(requestBuilder::addHeader);
        }
    }

    /**
     * 开始添加参数及请求方式
     */
    private void startAddParamsAndMethod() {
        if (this.params != null) {
            switch (this.method) {
                case HTTP_METHOD_GET:
                    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(this.url))
                            .newBuilder();
                    this.params.forEach((k, v) -> {
                        urlBuilder.addQueryParameter(k, String.valueOf(v));
                    });
                    this.url = urlBuilder.build().toString();
                    break;
                case HTTP_METHOD_POST:
                    RequestBody body = RequestBody.create(
                            JsonMediaType,
                            gson.toJson(this.params)
                    );
                    requestBuilder.method("POST", body);
                    break;
                default:
                    break;
            }
            requestBuilder.url(this.url);
            request = requestBuilder.build();
        }
    }

    /**
     * 构建
     * @param url 地址
     * @return RequestUtils
     */
    public static RequestUtils builder(String url) {
        return new RequestUtils(url);
    }

}
