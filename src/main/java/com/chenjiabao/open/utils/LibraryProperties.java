package com.chenjiabao.open.utils;

import com.chenjiabao.open.utils.model.property.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author ChenJiaBao
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "chenjiabao.config")
public class LibraryProperties {
    // 分布式机器信息
    @NestedConfigurationProperty
    private Machine machine = new Machine();

    // 胡椒值
    @NestedConfigurationProperty
    private Hash hash = new Hash();

    // 邮件配置
    @NestedConfigurationProperty
    private Mail mail = new Mail();

    // JWT配置
    @NestedConfigurationProperty
    private Jwt jwt = new Jwt();

    // 文件上传配置
    @NestedConfigurationProperty
    private File file = new File();
}
