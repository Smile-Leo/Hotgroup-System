package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.util.Optional;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
@Component
@Slf4j
public class ReplyServiceImpl implements ReplyService {


    @Override
    public void send(ChatMessageDto dto) {

        switch (dto.getType()) {
            case CHAT_DESTORY:
                ChatService.CHAT_LIST.remove(dto.getChatId());
                break;
            case CHAT_EXIT:
                Optional.ofNullable(ChatService.CHAT_LIST.get(dto.getChatId()))
                        .ifPresent(sessions -> {
                            dto.setMessage("用户:" + dto.getUserId() + "退出了");
                            for (Session session : sessions) {
                                session.getAsyncRemote().sendText(dto.getMessage());
                            }
                        });
                break;
            case SEND_USER:
                Optional.ofNullable(ChatService.USER_LIST.get(dto.getUserId()))
                        .ifPresent(sessions ->
                                sessions.forEach(session -> session.getAsyncRemote().sendText(dto.getMessage())));
                break;
            case SEND_CHAT:
                Optional.ofNullable(ChatService.CHAT_LIST.get(dto.getChatId()))
                        .ifPresent(sessions -> {
                            sessions.stream()
                                    .filter(session -> !session.getId().equals(dto.getUserId()))
                                    .forEach(session -> session.getAsyncRemote().sendText(dto.getMessage()));
                        });
                break;
            default:

        }

    }
}
