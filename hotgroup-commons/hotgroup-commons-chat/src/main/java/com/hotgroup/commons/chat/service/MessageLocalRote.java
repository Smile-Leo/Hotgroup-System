package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.MessageDTO;

import javax.annotation.Resource;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
public class MessageLocalRote implements MessageRoteStrategy {

    @Resource
    ReplyService replyService;

    @Override
    public <T> void send(MessageDTO<T> dto) {
        replyService.send(dto);
    }
}
