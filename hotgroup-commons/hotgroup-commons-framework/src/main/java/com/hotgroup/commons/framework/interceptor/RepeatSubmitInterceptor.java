package com.hotgroup.commons.framework.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.ServletUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author Lzw
 */
@RequiredArgsConstructor
public abstract class RepeatSubmitInterceptor implements HandlerInterceptor {

    public static final AjaxResult<String> ERROR = AjaxResult.error("您操作太快了，请稍后再试");
    public static final long DEFAULT_INTERVAL_TIME = 2L;
    final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            if (!HttpMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
                RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
                long intervalTime = DEFAULT_INTERVAL_TIME;
                if (annotation != null) {
                    if (annotation.disable()) {
                        return true;
                    }
                    intervalTime = annotation.intervalTime();
                }
                if (this.isRepeatSubmit(request, intervalTime)) {
                    ServletUtils.renderJson(response, objectMapper.writeValueAsString(ERROR));
                    return false;
                }
            }


        }
        return true;
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     *
     * @param request
     * @return
     * @throws Exception
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request, long intervalTime) throws IOException;
}
