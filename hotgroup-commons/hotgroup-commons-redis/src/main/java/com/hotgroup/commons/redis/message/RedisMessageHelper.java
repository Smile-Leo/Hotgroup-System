package com.hotgroup.commons.redis.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

/**
 * @author Lzw
 * @date 2021/03/03
 */
@Component
public class RedisMessageHelper {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisProperties redisProperties;


    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public RedisProperties getRedisProperties() {
        return redisProperties;
    }

    @SuppressWarnings("unchecked")
    public <T> T getMessage(Message message) {
        return (T) redisTemplate.getValueSerializer().deserialize(message.getBody());
    }

    /**
     * @param channelName
     * @return
     */
    public String genChannel(String channelName) {
        return getMessageChannel(channelName, redisProperties.getDatabase());
    }

    /**
     * @param channelName
     * @return
     */
    public ChannelTopic genChannelTopic(String channelName) {
        return new ChannelTopic(genChannel(channelName));
    }

    /**
     * 根据redis数据库获取消息频道
     *
     * @param database redis数据库
     * @return
     */
    public static String getMessageChannel(String channel, int database) {
        return channel + "_" + database;
    }
}
