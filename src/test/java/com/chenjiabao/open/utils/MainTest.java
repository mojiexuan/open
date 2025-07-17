package com.chenjiabao.open.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MainTest {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MainTest.class);
        app.run(args);
    }
}
