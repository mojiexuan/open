package com.chenjiabao.open.utils;

import com.chenjiabao.open.utils.model.property.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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
    private Machine machine = new Machine();

    // 胡椒值
    private Hash hash = new Hash();

    // 邮件配置
    private Mail mail = new Mail();

    // JWT配置
    private Jwt jwt = new Jwt();

    // 文件上传配置
    private File file = new File();
}
