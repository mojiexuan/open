package com.chenjiabao.open.utils.docs.annotation;

import java.lang.annotation.*;

/**
 * Api分组标签
 * @author ChenJiaBao
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tag {
    String name();
    String description() default "";
}
