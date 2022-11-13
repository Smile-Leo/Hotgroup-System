package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
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
@TableName
public class SysUserRole {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private String roleId;

}
