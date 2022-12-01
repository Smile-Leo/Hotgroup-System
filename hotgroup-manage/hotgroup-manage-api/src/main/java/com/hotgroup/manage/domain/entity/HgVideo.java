package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotgroup.commons.core.domain.model.UserType;
import com.hotgroup.commons.core.enums.StatusEnum;
import com.hotgroup.commons.database.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Lzw
 * @date 2022/5/31.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity
@TableName
public class HgVideo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Id
    private String id;

    @Schema(title = "用户类型")
    private UserType userType;

    @Schema(title = "用户Id")
    private String userId;

    @Schema(title = "视频地址")
    private String url;

    @Schema(title = "视频标题")
    private String name;

    @Schema(title = "视频封面")
    private String coverImg;

    @Schema(title = "状态（1正常 0停用）")
    private StatusEnum status;


}
