package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import com.hotgroup.commons.validator.annotation.IntSelection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户审核
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HgUserInfoAudit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "Id自动生成", groups = InsertGroup.class)
    @TableId
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 审核人用户信息
     */
    private String userInfoJson;

    /**
     * 状态（0待审核 1审核成功 2审核失败）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    private Integer status;
}
