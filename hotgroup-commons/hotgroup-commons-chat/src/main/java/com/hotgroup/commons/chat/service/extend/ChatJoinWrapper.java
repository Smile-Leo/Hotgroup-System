package com.hotgroup.commons.chat.service.extend;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.dto.MessageEnum;
import com.hotgroup.commons.chat.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.websocket.Session;
import java.util.List;

/**
 * @author Lzw
 * @date 2022/11/29.
 */
@Component
public class ChatJoinWrapper {

    private static List<ChatJoinInterceptor> interceptors;
    private final static DefaultChatJoin CHAT_JOIN = new DefaultChatJoin();

    @Autowired
    public ChatJoinWrapper(List<ChatJoinInterceptor> interceptors) {
        interceptors.sort((i1, i2) -> {
            Order order1 = i1.getClass().getAnnotation(Order.class);
            Order order2 = i2.getClass().getAnnotation(Order.class);
            int o1 = order1 == null ? 0 : order1.value();
            int o2 = order2 == null ? 0 : order2.value();
            return Integer.compare(o1, o2);
        });
        ChatJoinWrapper.interceptors = interceptors;
    }

    public static void handle(MessageEnum type, String message, Session session) throws Exception {
        MessageDTO<ChatDTO> messageDTO = JsonUtil.toObject(message, new TypeReference<MessageDTO<ChatDTO>>() {
        });
        Assert.notNull(messageDTO.getData(), "数据格式有误");
        String chatId = messageDTO.getData().getId();
        Assert.hasText(chatId, "id不能为空");
        preHandle(type, messageDTO, session);
        CHAT_JOIN.preHandle(type, messageDTO, session);
        postHandle(type, messageDTO, session);
    }

    private static void preHandle(MessageEnum type, MessageDTO<ChatDTO> message, Session session) throws Exception {
        for (ChatJoinInterceptor interceptor : interceptors) {
            interceptor.preHandle(type, message, session);
        }
    }

    private static void postHandle(MessageEnum type, MessageDTO<ChatDTO> message, Session session) throws Exception {
        for (ChatJoinInterceptor interceptor : interceptors) {
            interceptor.postHandle(type, message, session);
        }
    }
}
