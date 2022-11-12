package com.hotgroup.commons.redis;

import java.util.function.Consumer;

/**
 * @author Lzw
 * @date 2022/10/9.
 */
public interface MqService {

    <T> void accept(String key, Consumer<T> consumer);

    <T> void produce(String key, T event);
}
