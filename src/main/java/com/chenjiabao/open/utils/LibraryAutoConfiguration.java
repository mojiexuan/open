package com.chenjiabao.open.utils;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ChenJiaBao
 */
@Configuration
@EnableConfigurationProperties(LibraryProperties.class)
public class LibraryAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean // 仅当不存在该类型的bean时，才会创建该bean
    public HashUtils hashUtils(LibraryProperties properties) {
        HashUtils hashUtils = new HashUtils();
        if (properties.getHash().getPepper() != null) {
            hashUtils.setHashPepper(properties.getHash().getPepper());
        }
        return hashUtils;
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUtils jwtUtils(LibraryProperties properties) {
        JwtUtils jwtUtils = new JwtUtils();
        if (properties.getJwt().getSecret() != null) {
            jwtUtils.setJwtSecret(properties.getJwt().getSecret());
        }
        jwtUtils.setExpires(properties.getJwt().getExpires());
        return jwtUtils;
    }

    @Bean
    @ConditionalOnMissingBean
    public DelayedTaskExecutor delayedTaskExecutor() {
        return new DelayedTaskExecutor();
    }

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeUtils snowflakeUtils(LibraryProperties properties) {
        return new SnowflakeUtils(properties.getMachine().getId());
    }

    @Bean
    @ConditionalOnMissingBean
    public SensitiveWordUtils sensitiveWordUtils() {
        return SensitiveWordUtils.builder();
    }

    @Bean
    @ConditionalOnMissingBean
    public MailUtils.Builder mailUtilsBuilder(CheckUtils checkUtils, LibraryProperties properties) {
        MailUtils.Builder builder = new MailUtils.Builder(checkUtils);
        if (properties.getMail().getHost() != null) {
            builder = builder.setHost(properties.getMail().getHost());
        }
        builder = builder.setPort(properties.getMail().getPort());
        builder = builder.setSsl(properties.getMail().getSsl());
        builder = builder.setAuth(properties.getMail().getAuth());
        if (properties.getMail().getUsername() != null) {
            builder = builder.setUsername(properties.getMail().getUsername());
        }
        if (properties.getMail().getPassword() != null) {
            builder = builder.setPassword(properties.getMail().getPassword());
        }
        builder = builder.setProtocol(properties.getMail().getProtocol());

        return builder;
    }

    @Bean
    @ConditionalOnMissingBean
    public CheckUtils checkUtils() {
        return new CheckUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilesUtils filesUtils(LibraryProperties properties,
                                 TimeUtils timeUtils,
                                 StringUtils stringUtils){
        return  new FilesUtils(properties,timeUtils,stringUtils);
    }

    @Bean
    @ConditionalOnMissingBean
    public TimeUtils timeUtils() {
        return new TimeUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public StringUtils stringUtils() {
        return new StringUtils();
    }

}
