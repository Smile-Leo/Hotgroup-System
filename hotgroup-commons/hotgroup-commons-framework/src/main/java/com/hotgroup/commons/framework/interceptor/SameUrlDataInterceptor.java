package com.hotgroup.commons.framework.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotgroup.commons.core.constant.Constants;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

/**
 * 判断请求url和数据是否和上一次相同，
 * 如果和上次相同，则是重复提交表单。 有效时间为10秒内。
 *
 * @author Lzw
 */
@Component
public class SameUrlDataInterceptor extends RepeatSubmitInterceptor {

    @Resource
    private Redisson redisson;


    /**
     * 间隔时间，单位:秒 默认2秒
     * <p>
     * 两次相同参数的请求，如果间隔时间大于该参数，系统不会认定为重复提交的数据
     */
    public SameUrlDataInterceptor(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public boolean isRepeatSubmit(HttpServletRequest request, long intervalTime) throws IOException {

        RMap<String, Long> map = redisson.getMap(Constants.REPEAT_SUBMIT_KEY + request.getSession().getId());
        long now = System.currentTimeMillis();
        Long last = map.get(request.getRequestURI());
        map.put(request.getRequestURI(), now);
        map.expire(Duration.ofSeconds(intervalTime));
        if (Objects.isNull(last)) {
            return false;
        }
        if ((now - last) < (intervalTime * 1000L)) {
            return true;
        }
        return false;
    }

}
