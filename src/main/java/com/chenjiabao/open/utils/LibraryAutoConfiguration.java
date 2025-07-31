package com.chenjiabao.open.utils;

import com.chenjiabao.open.utils.aspect.ApiVersionAspect;
import com.chenjiabao.open.utils.aspect.VersionedRequestMappingHandlerMapping;
import com.chenjiabao.open.utils.controller.JiaBaoDocController;
import com.chenjiabao.open.utils.controller.JiaBaoAssetsController;
import com.chenjiabao.open.utils.docs.scanner.ApiScanner;
import com.chenjiabao.open.utils.resolver.RequestAttributeParamArgumentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

/**
 * @author ChenJiaBao
 */
@Configuration
@ConditionalOnWebApplication
@EnableConfigurationProperties(LibraryProperties.class)
public class LibraryAutoConfiguration implements WebMvcConfigurer {

    @Bean
    @ConditionalOnMissingBean
    public JiaBaoDocController jiaBaoDocController(){
        return new JiaBaoDocController();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApiScanner apiScanner(){
        return new ApiScanner();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chenjiabao.config.assets", name = "enabled", havingValue = "true")
    public JiaBaoAssetsController jiabaoAssetsController(LibraryProperties properties) {
        return new JiaBaoAssetsController(properties);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requestAttributeParamArgumentResolver());
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestAttributeParamArgumentResolver requestAttributeParamArgumentResolver() {
        return new RequestAttributeParamArgumentResolver();
    }


    @Bean
    @ConditionalOnMissingBean
    public VersionedRequestMappingHandlerMapping versionedRequestMappingHandlerMapping() {
        VersionedRequestMappingHandlerMapping handlerMapping = new VersionedRequestMappingHandlerMapping();
        handlerMapping.setOrder(0);
        return handlerMapping;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chenjiabao.config.api", name = "enabled", havingValue = "true")
    public ApiVersionAspect apiVersionAspect(
            LibraryProperties properties,
            ApplicationContext applicationContext,
            VersionedRequestMappingHandlerMapping versionedRequestMappingHandlerMapping){
        return new ApiVersionAspect(properties, applicationContext, versionedRequestMappingHandlerMapping);
    }

    @Bean
    @ConditionalOnMissingBean // 仅当不存在该类型的bean时，才会创建该bean
    @ConditionalOnProperty(prefix = "chenjiabao.config.hash", name = "enabled", havingValue = "true")
    public HashUtils hashUtils(LibraryProperties properties) {
        HashUtils hashUtils = new HashUtils();
        if (properties.getHash().getPepper() != null) {
            hashUtils.setHashPepper(properties.getHash().getPepper());
        }
        return hashUtils;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chenjiabao.config.jwt", name = "enabled", havingValue = "true")
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
    @ConditionalOnProperty(prefix = "chenjiabao.config.mail", name = "enabled", havingValue = "true")
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
    @ConditionalOnProperty(prefix = "chenjiabao.config.mail", name = "enabled", havingValue = "true")
    public MailUtils mailUtils(MailUtils.Builder builder,LibraryProperties properties) {
        if(properties.getMail().getUsername() != null){
            return builder.build().setFrom(properties.getMail().getUsername());
        }
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chenjiabao.config.check", name = "enabled", havingValue = "true")
    public CheckUtils checkUtils(LibraryProperties properties) {
        return new CheckUtils(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "chenjiabao.config.file", name = "enabled", havingValue = "true")
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
