package com.hotgroup.commons.redis.message;

public interface IRedisMessageProduct {

    /**
     * 推送redis信息
     *
     * @param msg
     */
    void sendMessage(RedisMessage message);

}
