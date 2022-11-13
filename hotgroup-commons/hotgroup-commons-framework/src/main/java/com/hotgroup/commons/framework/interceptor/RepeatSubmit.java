package com.hotgroup.commons.framework.interceptor;

import java.lang.annotation.*;

/**
 * 自定义注解防止表单重复提交
 *
 * @author Lzw
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RepeatSubmit {
    long intervalTime() default 2L;
    boolean disable() default false;
}
