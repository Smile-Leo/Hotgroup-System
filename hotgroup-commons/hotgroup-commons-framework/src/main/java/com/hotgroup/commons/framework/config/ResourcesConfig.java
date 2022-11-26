package com.hotgroup.commons.framework.config;

import com.hotgroup.commons.framework.interceptor.AbstractHandlerInterceptorAdapterChian;
import com.hotgroup.commons.framework.interceptor.RepeatSubmitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通用配置
 *
 * @author Lzw
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
    @Resource
    private RepeatSubmitInterceptor repeatSubmitInterceptor;
    @Autowired(required = false)
    private List<AbstractHandlerInterceptorAdapterChian> chians;


    /**
     * 自定义拦截规则
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(repeatSubmitInterceptor).addPathPatterns("/**");

        if (!CollectionUtils.isEmpty(chians)) {
            for (AbstractHandlerInterceptorAdapterChian chian : chians) {
                registry.addInterceptor(chian).addPathPatterns(chian.pathPatterns());
            }
        }
    }


}