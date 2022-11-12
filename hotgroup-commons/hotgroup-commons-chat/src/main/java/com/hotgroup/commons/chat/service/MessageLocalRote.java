package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.ChatMessageDto;

import javax.annotation.Resource;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
public class MessageLocalRote implements MessageRoteStrategy {

    @Resource
    ReplyService replyService;

    @Override
    public void send(ChatMessageDto dto) {
        replyService.send(dto);
    }
}
