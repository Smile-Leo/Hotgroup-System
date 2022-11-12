package com.hotgroup.commons.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lzw
 * @date 2022/10/9.
 */
@Configuration
public class MqConfiguration {


    @Bean
    @ConditionalOnMissingBean(MqService.class)
    MqService mqService(RedissonManager redissonManager, ObjectMapper objectMapper) {
        return new RedisMqServiceImpl(redissonManager, objectMapper);
    }

    @Configuration
    @ConditionalOnClass(RabbitTemplate.class)
    static class rabbitMqConfig {
        @Bean
        MqService rabbitMqService(AmqpAdmin admin, RabbitTemplate template, SimpleRabbitListenerContainerFactory container) {
            return new RabbitMqServiceImpl(admin, template, container);
        }
    }


}
