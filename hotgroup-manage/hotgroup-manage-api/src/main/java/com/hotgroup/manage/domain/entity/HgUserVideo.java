package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 视频
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HgUserVideo extends BaseEntity {

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
    private String pv_count;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    private Integer status;
}
