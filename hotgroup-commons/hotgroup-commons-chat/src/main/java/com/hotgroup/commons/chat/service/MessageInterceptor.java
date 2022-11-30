package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.chat.dto.MessageEnum;

import javax.websocket.Session;

/**
 * @author Lzw
 * @date 2022/11/29.
 */
public interface MessageInterceptor<T> {


    default void preHandle(MessageEnum type, T message, Session session) throws Exception {

    }

    default void postHandle(MessageEnum type, T message, Session session) throws Exception {

    }

}