package com.chenjiabao.open.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${chenjiabao.config.machine.id}")
    private Long machineId = 1L;

    // 胡椒值
    @Value("${chenjiabao.config.hash.pepper}")
    private String hashPepper;

    @Value("${chenjiabao.config.mail.host}")
    private String mailHost;
    @Value("${chenjiabao.config.mail.port}")
    private Integer mailPort = 465;
    @Value("${chenjiabao.config.mail.ssl}")
    private Boolean mailSsl = false;
    @Value("${chenjiabao.config.mail.auth}")
    private Boolean mailAuth = false;
    @Value("${chenjiabao.config.mail.username}")
    private String mailUsername;
    @Value("${chenjiabao.config.mail.password}")
    private String mailPassword;
    @Value("${chenjiabao.config.mail.protocol}")
    private String mailProtocol = "smtp";

    @Value("${chenjiabao.config.jwt.secret}")
    private String jwtSecret;
    @Value("${chenjiabao.config.jwt.expires}")
    private Integer jwtExpires = 7200;
}
