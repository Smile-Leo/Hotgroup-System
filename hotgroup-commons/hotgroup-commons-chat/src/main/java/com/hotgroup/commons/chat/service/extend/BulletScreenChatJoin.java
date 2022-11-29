package com.hotgroup.commons.chat.service.extend;

import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.dto.MessageEnum;
import com.hotgroup.commons.chat.util.JsonUtil;
import com.hotgroup.commons.redis.RedissonManager;
import org.redisson.Redisson;
import org.redisson.api.RBoundedBlockingQueue;
import org.redisson.api.RDeque;
import org.redisson.client.codec.StringCodec;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.websocket.Session;

/**
 * @author Lzw
 * @date 2022/11/29.
 */
@Component
@Order(10)
@ConditionalOnClass(Redisson.class)
public class BulletScreenChatJoin implements ChatJoinInterceptor {

    private final Redisson redisson;

    public BulletScreenChatJoin(RedissonManager manager) {
        this.redisson = manager.getRedisson();
    }

    @Override
    public void preHandle(MessageEnum type, MessageDTO<ChatDTO> message, Session session) throws Exception {
        String chatId = message.getData().getId();

        RDeque<ChatDTO> deque = redisson.getDeque("CHAT:" + chatId);
        for (ChatDTO msg : deque) {
            session.getAsyncRemote().sendText(
                    JsonUtil.toJson(MessageDTO.success(MessageEnum.SEND_CHAT, msg)));
        }

    }
}
