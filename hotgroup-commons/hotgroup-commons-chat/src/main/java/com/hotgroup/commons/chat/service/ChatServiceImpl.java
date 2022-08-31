package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
@Component
public class ChatServiceImpl implements ChatService {

    @Autowired
    MessageRoteStrategy messageRote;

    private String createChatId() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    @Override
    public void send(ChatMessageDto dto) {
        switch (dto.getType()) {
            case CHAT_JOIN:
                for (List<Session> value : CHAT_LIST.values()) {
                    value.removeIf(val -> val.getId().equals(dto.getSession().getId()));
                }
                CHAT_LIST.computeIfPresent(dto.getChatId(), (k, v) -> {
                    v.add(dto.getSession());
                    return v;
                });
                break;
            case CHAT_CREATE:
                if (CHAT_LIST.containsKey(dto.getChatId())) {
                    for (List<Session> value : CHAT_LIST.values()) {
                        value.removeIf(val -> val.getId().equals(dto.getSession().getId()));
                    }
                    CHAT_LIST.computeIfPresent(dto.getChatId(), (k, v) -> {
                        v.add(dto.getSession());
                        return v;
                    });
                    break;
                }
                CHAT_LIST.put(dto.getChatId(), new CopyOnWriteArrayList<>(Collections.singleton(dto.getSession())));
                break;
            case CHAT_EXIT:
                Optional.ofNullable(ChatService.CHAT_LIST.get(dto.getChatId()))
                        .ifPresent(lst -> lst.removeIf(session -> session.getId().equals(dto.getSession().getId())));
                break;
            case CHAT_DESTORY:
                ChatService.CHAT_LIST.remove(dto.getChatId());

            default:

        }

        messageRote.send(dto);
    }

}
