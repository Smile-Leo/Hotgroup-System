package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.MessageDTO;

/**
 * @author Lzw
 * @date 2022/5/28.
 */
public interface MessageRoteStrategy {

    <T>  void send(MessageDTO<T> dto);
}
