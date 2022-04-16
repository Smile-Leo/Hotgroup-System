package com.hotgroup.commons.redis.config;

import com.hotgroup.commons.redis.RedissonLock;
import com.hotgroup.commons.redis.RedissonManager;
import com.hotgroup.commons.redis.props.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Redisson自动化配置
 *
 * @author pangu
 * @date 2020-10-20
 */
@Slf4j
@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @Order(value = 1)
    public RedissonManager redissonManager(RedissonProperties redissonProperties) {
        RedissonManager redissonManager = new RedissonManager(redissonProperties);
        log.debug("[RedissonManager]组装完毕");
        log.debug("[RedissonManager]当前连接方式:" + redissonProperties.getType());
        log.debug("[RedissonManager]连接地址:" + redissonProperties.getAddress());
        return redissonManager;
    }


    @Bean
    @ConditionalOnMissingBean
    @Order(value = 2)
    public RedissonLock redissonLock(RedissonManager redissonManager) {
        RedissonLock redissonLock = new RedissonLock();
        redissonLock.setRedissonManager(redissonManager);
        log.debug("[RedissonLock]组装完毕");
        return redissonLock;
    }

}
