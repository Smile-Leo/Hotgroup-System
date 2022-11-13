package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotgroup.commons.database.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * 视频
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity
@TableName
public class HgUserVideoAudit extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @Id
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * url
     */
    private String url;

    /**
     * 视频名称
     */
    private String name;

    /**
     * 视频封面
     */
    private String coverImg;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    private Integer status;

    /**
     * 处理进度
     */
    private Integer process;

    /**
     * 处理消息
     */
    private String processMsg;
    /**
     * 状态（0待审核 1审核成功 2审核失败）
     */
    private Integer auditStatus;
}
