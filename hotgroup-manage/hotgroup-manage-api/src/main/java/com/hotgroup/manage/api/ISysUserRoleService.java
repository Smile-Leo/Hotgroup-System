package com.hotgroup.manage.api;

/**
 * @author Lzw
 * @date 2022/11/13.
 */
public interface ISysUserRoleService {

    void removeUserId(String userId);

    void removeRoleId(String roleId);

    void add(String userId, String[] roleIds);

    void add(String[] userIds, String roleId);

}
