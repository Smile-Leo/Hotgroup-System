package com.hotgroup.manage.domain.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户和角色关联 sys_user_role
 *
 * @author Lzw
 */
@Data
@ToString
@Table
@Entity
public class SysUserRole {

    /**
     * 用户ID
     */
    @Id
    private String userId;

    /**
     * 角色ID
     */
    private Long roleId;

}
