package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.core.domain.model.LoginUser;
import org.apache.commons.lang3.StringUtils;

import javax.websocket.Session;
import java.security.Principal;
import java.util.Objects;

/**
 * @author Lzw
 * @date 2022/11/29.
 */
public class MessageCreate {

    public static final String ME = "我";
    public static final String ANONYMOUS = "游客";

    public static String to(Session send, String msg, Session to) {
        Principal userPrincipal = send.getUserPrincipal();
        Integer level = userPrincipal instanceof LoginUser ?
                ((LoginUser) userPrincipal).getUser().getLevel() : 0;
        String name = Objects.nonNull(to) && StringUtils.equals(send.getId(), to.getId()) ? ME :
                userPrincipal instanceof LoginUser ?
                        userPrincipal.getName() :
                        ANONYMOUS + send.getId().substring(0, 5);

        return MessageCreate.formatMessage(level, name, msg);
    }

    private static String formatMessage(Integer level, String name, String message) {
        return new StringBuilder()
                .append(level)
                .append(":")
                .append(name)
                .append(":")
                .append(message)
                .toString();
    }


}
