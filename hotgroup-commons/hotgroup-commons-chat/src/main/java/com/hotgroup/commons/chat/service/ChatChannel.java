package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatDTO;
import com.hotgroup.commons.chat.dto.MessageDTO;
import com.hotgroup.commons.chat.dto.MessageEnum;
import com.hotgroup.commons.chat.util.JsonUtil;

import javax.websocket.Session;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Lzw
 * @date 2022/11/27.
 */
public class ChatChannel {

    private static final Map<String, List<Session>> CHATS = new ConcurrentHashMap<>();

    public static void join(String chatId, Session session) {
        exit(session);
        CHATS.putIfAbsent(chatId, new CopyOnWriteArrayList<>());
        CHATS.get(chatId).add(session);
    }

    public static void exit(Session session) {
        CHATS.values().removeIf(sessions -> {
            sessions.removeIf(s -> s.getId().equals(session.getId()));
            return sessions.isEmpty();
        });
    }

    public static void sendToChat(String chatId, String message) {
        for (Session session : CHATS.getOrDefault(chatId, Collections.emptyList())) {
            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setId(chatId);
            chatDTO.setMsg(message);
            session.getAsyncRemote().sendText(
                    JsonUtil.toJson(MessageDTO.success(MessageEnum.SEND_CHAT, chatDTO)));
        }
    }
}
