package com.hotgroup.commons.core.domain.model;

/**
 * @author Lzw
 * @date 2022/4/16.
 */
public interface IUser {

    String getId();

    String getPassword();

    String getUserName();

    boolean isEnabled();

    boolean isAdmin();
}
