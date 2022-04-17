package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 团队
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HgTeam extends BaseEntity {

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
     * 分类id
     */
    private Long classifyId;

    /**
     * 是否免费 1是 0否
     */
    @Size(min = 1, max = 1, message = "免费类型有误")
    private Integer isFree;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 封面
     */
    private String coverImg;

    /**
     * 标题
     */
    private String title;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 用户数量
     */
    private Integer peopleNum;

    /**
     * 玩法简介
     */
    private String playingIntroduction;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    private Integer status;
}
