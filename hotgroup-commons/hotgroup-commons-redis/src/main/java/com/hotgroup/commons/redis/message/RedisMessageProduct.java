package com.hotgroup.commons.redis.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lzw
 * @date 2021/03/03
 */
@Component
public class RedisMessageProduct implements IRedisMessageProduct {

    @Autowired
    private RedisMessageHelper messageHelper;

    @Override
    public void sendMessage(RedisMessage msg) {
        String channel = messageHelper.genChannel(msg.channelName());
        messageHelper.getRedisTemplate().convertAndSend(channel, msg.message());
    }

}
