package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
@Component
@Slf4j
public class ReplyServiceImpl implements ReplyService {


    @Override
    public <T> void send(MessageDTO<T> dto) {

        switch (dto.getType()) {
            case CHAT_JOIN:
                break;
            case CHAT_EXIT:
                break;
            case SEND_USER:
                break;
            case SEND_CHAT:
                break;
            default:

        }

    }
}
