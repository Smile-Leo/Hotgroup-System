package com.hotgroup.commons.core.domain.model;

/**
 * @author Lzw
 * @date 2022/4/16.
 */
public interface IUser {

    Long getId();

    String getPassword();

    String getUsername();

    boolean isEnabled();

    boolean isAdmin();
}
