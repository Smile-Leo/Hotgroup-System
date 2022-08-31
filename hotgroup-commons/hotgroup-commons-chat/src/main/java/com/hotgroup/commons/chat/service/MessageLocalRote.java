package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatMessageDto;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
public class MessageLocalRote implements MessageRoteStrategy {

    @Autowired
    ReplyService replyService;

    @Override
    public void send(ChatMessageDto dto) {
        replyService.send(dto);
    }
}
