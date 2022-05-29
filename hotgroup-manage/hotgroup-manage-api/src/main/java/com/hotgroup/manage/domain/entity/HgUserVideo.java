package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 视频
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "Hg_user_video")
@Entity
public class HgUserVideo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "Id自动生成", groups = InsertGroup.class)
    @TableId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column(name = "user_id", length = 11)
    private Long userId;

    /**
     * url
     */
    @Column(name = "url", length = 50)
    private String url;

    /**
     * 视频名称
     */
    @Column(name = "name", length = 50)
    private String name;

    /**
     * 视频封面
     */
    @Column(name = "cover_img", length = 255)
    private String coverImg;

    /**
     * 视频播放量
     */
    @Column(name = "pv_count", length = 11)
    private String pvCount;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    @Column(name = "status", length = 1)
    private Integer status;

    /**
     * 状态（0待审核 1审核成功 2审核失败）
     */
    @Column(name = "audit_status", length = 1)
    private Integer auditStatus;
}
