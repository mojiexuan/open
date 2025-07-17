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
    public HashUtils hashUtils(LibraryProperties properties){
        HashUtils hashUtils = new HashUtils();
        if(properties.getHashPepper() != null){
            hashUtils.setHashPepper(properties.getHashPepper());
        }
        return hashUtils;
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtUtils jwtUtils(LibraryProperties properties){
        JwtUtils jwtUtils = new JwtUtils();
        if(properties.getJwtSecret() != null){
            jwtUtils.setJwtSecret(properties.getJwtSecret());
        }
        jwtUtils.setExpires(properties.getJwtExpires());
        return jwtUtils;
    }

    @Bean
    @ConditionalOnMissingBean
    public DelayedTaskExecutor delayedTaskExecutor(){
        return new DelayedTaskExecutor();
    }

    @Bean
    @ConditionalOnMissingBean
    public SnowflakeUtils snowflakeUtils(LibraryProperties properties){
        return new SnowflakeUtils(properties.getMachineId());
    }

    @Bean
    @ConditionalOnMissingBean
    public SensitiveWordUtils sensitiveWordUtils(){
        return SensitiveWordUtils.builder();
    }

    @Bean
    @ConditionalOnMissingBean
    public MailUtils.Builder mailUtilsBuilder(CheckUtils checkUtils,LibraryProperties properties){
        MailUtils.Builder builder = new MailUtils.Builder(checkUtils);
        if(!properties.getMailHost().isEmpty()){
            builder = builder.setHost(properties.getMailHost());
        }
        builder = builder.setPort(properties.getMailPort());
        builder = builder.setSsl(properties.getMailSsl());
        builder = builder.setAuth(properties.getMailAuth());
        if(!properties.getMailUsername().isEmpty()){
            builder = builder.setUsername(properties.getMailUsername());
        }
        if(!properties.getMailPassword().isEmpty()){
            builder = builder.setPassword(properties.getMailPassword());
        }
        if(!properties.getMailProtocol().isEmpty()){
            builder = builder.setProtocol(properties.getMailProtocol());
        }

        return builder;
    }

    @Bean
    @ConditionalOnMissingBean
    public CheckUtils checkUtils(){
        return new CheckUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public TimeUtils timeUtils(){
        return new TimeUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public StringUtils stringUtils(){
        return new StringUtils();
    }

}
