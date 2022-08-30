package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * @author Lzw
 * @date 2022/5/31.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity
public class HgVideo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    @Id
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 外键
     */
    private String userVideoId;
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
     * 视频播放量
     */
    private Long pvCount;


    private Long likesCount;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    private Integer status;

    /**
     * 状态（0待审核 1审核成功 2审核失败）
     */
    private Integer auditStatus;

}
