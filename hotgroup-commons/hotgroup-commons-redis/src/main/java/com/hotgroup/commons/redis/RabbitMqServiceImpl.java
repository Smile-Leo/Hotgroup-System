package com.hotgroup.commons.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.DisposableBean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Lzw
 * @date 2022/10/9.
 */
@RequiredArgsConstructor
public class RabbitMqServiceImpl implements MqService, DisposableBean {

    final AmqpAdmin admin;
    final RabbitTemplate template;
    final SimpleRabbitListenerContainerFactory listenerContainerFactory;
    final List<SimpleMessageListenerContainer> containerList = new ArrayList<>();

    @Override
    public <T> void accept(String key, Consumer<T> consumer) {
        Queue queue = QueueBuilder.durable(key).build();
        DirectExchange exchange = ExchangeBuilder.directExchange(key).build();
        admin.declareQueue(queue);
        admin.declareExchange(exchange);
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).withQueueName());
        SimpleMessageListenerContainer container = listenerContainerFactory.createListenerContainer();
        container.addQueues(queue);
        container.setupMessageListener(message ->
                consumer.accept((T) template.getMessageConverter().fromMessage(message)));
        container.start();
        containerList.add(container);
    }

    @Override
    public <T> void produce(String key, T event) {
        template.convertAndSend(key, key, event);
    }

    @Override
    public void destroy() throws Exception {
        for (SimpleMessageListenerContainer listenerContainer : containerList) {
            listenerContainer.stop();
            listenerContainer.destroy();
        }
    }
}
