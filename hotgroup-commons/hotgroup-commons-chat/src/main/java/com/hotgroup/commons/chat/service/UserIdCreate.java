package com.hotgroup.commons.chat.service;

import com.hotgroup.commons.core.domain.model.LoginUser;

/**
 * @author Lzw
 * @date 2022/11/27.
 */
public class UserIdCreate {
    public static String getId(LoginUser user) {
        return user.getType().name() + ":" + user.getUser().getId();
    }
}
