package com.chenjiabao.open.utils;

import com.chenjiabao.open.utils.callback.RequestCallback;
import com.chenjiabao.open.utils.enums.HttpBody;
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
import java.util.logging.Logger;

/**
 * 网络请求工具
 * @author ChenJiaBao
 * @deprecated 0.3.3中不可用，请降低版本至0.3.2或升级最新版本
 */
@Deprecated
public class RequestUtils {

    private static final MediaType JsonMediaType = MediaType.parse("application/json");
    private static final MediaType XmlMediaType = MediaType.parse("application/xml; charset=utf-8");

    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger("com.chenjiabao.open.utils.RequestUtils");

    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build();
    private final Request.Builder requestBuilder;
    private String url;
    private HttpMethod method = HttpMethod.HTTP_METHOD_GET;
    private HashMap<String, String> headers = null;
    private HashMap<String, Object> params = null;
    private HttpBody body = HttpBody.FORM_DATA;
    private Request request;
    private boolean debug = false;

    private RequestUtils(String url) {
        this.url = url;
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
     * @param key   键
     * @param value 值
     * @return RequestUtils
     */
    public RequestUtils addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        this.headers.put(key, value);
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
     * 设置post请求体类型
     *
     * @param body 类型
     * @return RequestUtils
     */
    public RequestUtils setBody(HttpBody body) {
        this.body = body;
        return this;
    }

    /**
     * 添加调试
     *
     * @return RequestUtils
     */
    public RequestUtils addDebug() {
        this.debug = true;
        return this;
    }

    /**
     * 开始同步请求，返回data为请求结果字符串
     *
     * @return code==200时请求成功，请求结果在data中
     */
    public HttpResponse<String> start() {
        HttpResponse<String> httpResponse = new HttpResponse<>();
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        try {
            Response response = okHttpClient.newCall(this.request).execute();
            if (!response.isSuccessful() || response.body() == null) {
                httpResponse.setCode(response.code());
                httpResponse.setMsg("失败");
            } else {
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
     *
     * @return code==200时请求成功，请求结果在data中，若解析字符串失败code==1004
     */
    public <T> HttpResponse<T> start(Class<T> clazz) {
        HttpResponse<T> httpResponse = new HttpResponse<>();
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        try {
            Response response = okHttpClient.newCall(this.request).execute();
            if (!response.isSuccessful() || response.body() == null) {
                httpResponse.setCode(response.code());
                httpResponse.setMsg("失败");
            } else {
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
     *
     * @param callback 回调类
     */
    public void start(RequestCallback<String> callback) {
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        okHttpClient.newCall(this.request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onSuccess(new HttpResponse<>(1001, "失败"));
                } else {
                    callback.onSuccess(new HttpResponse<>(200, "成功", response.body().string()));
                }
                callback.complete();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.onFail(
                        new HttpResponse<>(1001, "请求错误")
                );
                callback.complete();
            }
        });
    }

    /**
     * 开始异步请求,code==200时请求成功，请求结果在data中
     *
     * @param callback 回调类
     */
    public <T> void start(Class<T> clazz, RequestCallback<T> callback) {
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        okHttpClient.newCall(this.request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    printDebug(response.code() + "失败" + response.message());
                    callback.onSuccess(new HttpResponse<>(1002, "失败"));
                } else {
                    try {
                        callback.onSuccess(new HttpResponse<>(
                                200,
                                "成功",
                                gson.fromJson(response.body().string(), clazz)
                        ));
                    } catch (JsonSyntaxException | IOException e) {
                        printDebug("解析字符串失败");
                        callback.onSuccess(new HttpResponse<>(1004, "解析字符串失败"));
                    }
                }
                callback.complete();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                printDebug(e.getMessage());
                callback.onFail(
                        new HttpResponse<>(1001, "请求错误")
                );
                callback.complete();
            }
        });
    }

    /**
     * 流式请求，内容在data中，字符串格式
     */
    public void stream(RequestCallback<String> callback) {
        this.startAddHeaders();
        this.startAddParamsAndMethod();
        okHttpClient.newCall(this.request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onSuccess(new HttpResponse<>(1002, "失败"));
                } else {
                    try (BufferedSource source = response.body().source()) {
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
                        new HttpResponse<>(1001, "请求错误")
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
        requestBuilder.url(this.url);
        if (this.params != null) {
            switch (this.method) {
                case HTTP_METHOD_GET:
                    HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(this.url))
                            .newBuilder();
                    this.params.forEach((k, v) -> {
                        urlBuilder.addQueryParameter(k, String.valueOf(v));
                    });
                    this.url = urlBuilder.build().toString();
                    requestBuilder.url(this.url);
                    break;
                case HTTP_METHOD_POST:
                    switch (this.body) {
                        case FORM_DATA:
                            FormBody.Builder f = new FormBody.Builder();
                            this.params.forEach((k, v) -> {
                                f.add(k, String.valueOf(v));
                            });
                            requestBuilder.post(f.build());
                            break;
                        case JSON:
                            RequestBody body = RequestBody.create(
                                    gson.toJson(this.params),
                                    JsonMediaType
                            );
                            requestBuilder.post(body);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
        request = requestBuilder.build();
    }

    /**
     * 打印调试信息
     */
    private void printDebug(String msg) {
        if (!this.debug) {
            return;
        }
        logger.info("//========================================");
        logger.info("地址：" + this.url);
        logger.info("方式：" + this.method.getValue());
        logger.info("请求头：" + gson.toJson(this.headers));
        logger.info("参数：" + gson.toJson(this.params));
        logger.info("消息：" + msg);
        logger.info("//========================================");
    }

    /**
     * 构建
     *
     * @param url 地址
     * @return RequestUtils
     */
    public static RequestUtils builder(String url) {
        return new RequestUtils(url);
    }

}
