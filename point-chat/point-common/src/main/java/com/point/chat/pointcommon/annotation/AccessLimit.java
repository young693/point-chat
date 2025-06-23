package com.point.chat.pointcommon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 访问限制注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AccessLimit {
    
    /**
     * 秒值
     **/
    int seconds() default 1;
    
    /**
     * 秒值中最大访问次数
     **/
    int maxCount() default 6;
}
