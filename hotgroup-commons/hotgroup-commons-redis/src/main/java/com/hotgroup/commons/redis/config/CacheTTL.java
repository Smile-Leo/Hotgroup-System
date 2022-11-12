package com.hotgroup.commons.redis.config;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Lzw
 * @date 2022/10/18.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheTTL {

    long value() default 1;

    TimeUnit unit() default TimeUnit.HOURS;
}
