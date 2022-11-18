package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户ID
     */
    @Id
    private String userId;

    /**
     * 角色ID
     */
    @Id
    private String roleId;

}
