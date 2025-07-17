package com.chenjiabao.open.utils;

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
    // 分布式ID机器ID
    private Long machineId = 1L;

    // 胡椒值
    private String hashPepper;

    private String mailHost;
    private Integer mailPort = 465;
    private Boolean mailSsl = false;
    private Boolean mailAuth = false;
    private String mailUsername;
    private String mailPassword;
    private String mailProtocol = "smtp";

    private String jwtSecret;
    private Integer jwtExpires = 7200;
}
