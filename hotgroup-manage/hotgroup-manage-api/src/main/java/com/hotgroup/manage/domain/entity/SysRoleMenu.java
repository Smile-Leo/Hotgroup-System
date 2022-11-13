package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * 角色和菜单关联 sys_role_menu
 *
 * @author Lzw
 */
@Data
@ToString
@TableName
public class SysRoleMenu {
    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 菜单ID
     */
    private String menuId;


}
