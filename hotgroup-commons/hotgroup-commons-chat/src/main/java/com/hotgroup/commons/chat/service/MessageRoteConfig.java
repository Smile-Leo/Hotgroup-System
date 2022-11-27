package com.hotgroup.commons.chat.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
@Configuration
public class MessageRoteConfig {


    @ConditionalOnClass({RabbitTemplate.class, Channel.class})
    @Configuration
    static class MqMessageRoteConfig {
        @Bean
        MessageRoteStrategy mqMessageRote(ReplyService replyService, RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
            return new MessageMqRote(replyService, rabbitTemplate, amqpAdmin);
        }
    }

    @Bean
    @ConditionalOnMissingBean(value = MessageRoteStrategy.class)
    MessageRoteStrategy localMessageRote() {
        return new MessageLocalRote();
    }
}
