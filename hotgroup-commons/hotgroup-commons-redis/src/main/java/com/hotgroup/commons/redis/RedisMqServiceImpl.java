package com.hotgroup.commons.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.RedissonShutdownException;
import org.redisson.api.RBlockingQueue;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author Lzw
 * @date 2022/10/9.
 */
@Slf4j
public class RedisMqServiceImpl implements MqService {

    private final RedissonManager redissonManager;
    private final Executor executor;
    private final ObjectMapper objectMapper;
    private final RetryTemplate retryTemplate = RetryTemplate.builder().retryOn(RuntimeException.class).fixedBackoff(10000L).infiniteRetry().build();

    public RedisMqServiceImpl(RedissonManager redissonManager, ObjectMapper objectMapper) {
        this.redissonManager = redissonManager;
        this.objectMapper = objectMapper;
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(50);
        executor.setMaxPoolSize(50);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("RedisMQThread-");
        executor.setRejectedExecutionHandler((r, executor1) -> {
            throw new RuntimeException("RedisMQService 线程池不够用了");
        });
        executor.initialize();
        this.executor = executor;
    }

    @Override
    public <T> void accept(String key, Consumer<T> consumer) {
        CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    Redisson redisson = retryTemplate.execute(
                            retryContext -> {
                                return Optional.ofNullable(redissonManager.getRedisson()).orElseThrow(RuntimeException::new);
                            },
                            retryContext -> {
                                log.debug("redis connect error! retry {}", retryContext.getRetryCount());
                                return null;
                            });
                    T value = redisson.<T>getBlockingQueue(key).take();
                    if (log.isDebugEnabled()) {
                        log.debug("MQbusMsg->{}", objectMapper.writeValueAsString(value));
                    }
                    consumer.accept(value);
                } catch (RedissonShutdownException ignored) {
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            }
        }, executor);
    }

    @Override
    public <T> void produce(String key, T event) {
        try {
            RBlockingQueue<Object> blockingQueue = redissonManager.getRedisson().getBlockingQueue(key);
            blockingQueue.put(event);
            blockingQueue.expire(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }
}
