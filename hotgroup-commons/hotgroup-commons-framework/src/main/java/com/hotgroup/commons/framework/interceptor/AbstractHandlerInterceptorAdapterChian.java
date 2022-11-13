package com.hotgroup.commons.framework.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author Lzw
 * @date 2021/5/18.
 */
public abstract class AbstractHandlerInterceptorAdapterChian implements HandlerInterceptor {
    public abstract String pathPatterns();
}
