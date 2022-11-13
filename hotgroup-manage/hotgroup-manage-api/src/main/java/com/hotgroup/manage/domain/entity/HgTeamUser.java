package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 团队用户关联表
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "Hg_team_user")
@Entity
@TableName
public class HgTeamUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "userId自动生成", groups = InsertGroup.class)
    @TableId(type = IdType.ASSIGN_ID)
    @Id
    private String id;
    /**
     * 团队id
     */
    @Column(name = "team_id", length = 11)
    private String teamId;

    /**
     * 用户id
     */
    @Column(name = "user_id", length = 11)
    private String userId;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    @Column(name = "status", length = 1)
    private Integer status;
}
