package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 团队用户关联表
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HgTeamUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "Id自动生成", groups = InsertGroup.class)
    @TableId
    private Long id;

    /**
     * 团队id
     */
    private Long teamId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    private Integer status;
}
