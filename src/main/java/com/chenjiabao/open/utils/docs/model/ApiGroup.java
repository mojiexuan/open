package com.chenjiabao.open.utils.docs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口组
 * @author ChenJiaBao
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiGroup {
    // 接口组名
    private String name;
    // 接口组描述
    private String description = "";
    // 接口组路径
    private String path = "/";
    // 接口组下的接口
    private List<ApiDefinition> apis = new ArrayList<>();
    // 接口组版本
    private String version = "1";

    /**
     * 添加接口
     * @param api 接口定义
     */
    public void addApi(ApiDefinition api) {
        apis.add(api);
    }
}
