package com.chenjiabao.open.utils.callback;

import com.chenjiabao.open.utils.model.HttpResponse;

/**
 * 网络请求回调接口
 * @author ChenJiaBao
 */
public interface RequestCallback<T> {
    void onSuccess(HttpResponse<T> response);
    void onFail(HttpResponse<String> response);
    default void complete(){}
}
