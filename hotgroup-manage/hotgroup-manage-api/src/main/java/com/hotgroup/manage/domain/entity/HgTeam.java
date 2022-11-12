package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
import java.math.BigDecimal;

/**
 * 团队
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "hg_team")
@Entity
public class HgTeam extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "userId自动生成", groups = InsertGroup.class)
    @TableId(type = IdType.ASSIGN_UUID)
    @Id
    private String id;

    /**
     * 用户id
     */
    @Column(name = "user_id", length = 11)
    private String userId;

    /**
     * 分类id
     */
    @Column(name = "classify_id", length = 11)
    private Long classifyId;

    /**
     * 是否免费 1是 0否
     */
    @Size(min = 1, max = 1, message = "免费类型有误")
    @Column(name = "is_free", length = 11)
    private Integer isFree;

    /**
     * 价格
     */
    @Column(name = "price", length = 50)
    private BigDecimal price;

    /**
     * 封面
     */
    @Column(name = "cover_img", length = 255)
    private String coverImg;

    /**
     * 标题
     */
    @Column(name = "title", length = 100)
    private String title;

    /**
     * 开始时间
     */
    @Column(name = "start_time", length = 20)
    private String startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time", length = 20)
    private String endTime;

    /**
     * 用户数量
     */
    @Column(name = "people_num", length = 11)
    private Integer peopleNum;

    /**
     * 玩法简介
     */
    @Column(name = "playing_introduction", length = 255)
    private String playingIntroduction;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    @Column(name = "status", length = 1)
    private Integer status;
}
