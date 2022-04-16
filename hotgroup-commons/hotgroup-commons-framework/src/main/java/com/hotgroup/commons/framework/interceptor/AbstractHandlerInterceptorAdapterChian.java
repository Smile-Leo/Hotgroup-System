package com.hotgroup.commons.framework.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * @author Lzw
 * @date 2021/5/18.
 */
public abstract class AbstractHandlerInterceptorAdapterChian extends HandlerInterceptorAdapter {
    public abstract String pathPatterns();
}
