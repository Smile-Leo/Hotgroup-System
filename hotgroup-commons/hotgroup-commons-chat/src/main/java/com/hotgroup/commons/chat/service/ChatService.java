package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatMessageDto;

import javax.websocket.Session;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
public interface ChatService {


    Map<String, List<Session>> CHAT_LIST = new ConcurrentHashMap<>();
    Map<String, List<Session>> USER_LIST = new ConcurrentHashMap<>();

    void send(ChatMessageDto dto);
}
