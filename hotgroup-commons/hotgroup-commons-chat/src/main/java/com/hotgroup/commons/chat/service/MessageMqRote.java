package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import javax.annotation.PreDestroy;
import java.util.Optional;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
@Slf4j
public class MessageMqRote implements MessageRoteStrategy {


    private static final String EXCHANGE_NAME = "hotgroup.chats";
    private final ReplyService replyService;
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;

    public MessageMqRote(ReplyService replyService, RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
        this.replyService = replyService;
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;

        FanoutExchange exchange = new FanoutExchange(EXCHANGE_NAME, false, false);
        amqpAdmin.declareExchange(exchange);
    }

    @PreDestroy
    public void destory() {
        Optional.ofNullable(amqpAdmin.declareQueue()).ifPresent(queue -> amqpAdmin.deleteQueue(queue.getName()));
    }


    @RabbitListener(bindings = @QueueBinding(value = @Queue(durable = "false", autoDelete = "true"),
            exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.FANOUT, durable = "false")))
    public void listener(ChatMessageDto dto) {

        try {
            replyService.send(dto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    @Override
    public void send(ChatMessageDto dto) {
        try {
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", dto);
        } catch (AmqpException e) {
            log.error(e.getMessage(), e);
        }
    }
}
