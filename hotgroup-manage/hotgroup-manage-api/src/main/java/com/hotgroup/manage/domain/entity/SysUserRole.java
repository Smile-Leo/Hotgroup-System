package com.hotgroup.manage.domain.entity;

import lombok.Data;
import lombok.ToString;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author Lzw
 */
@Data
@ToString
public class SysUserRole {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;


}
