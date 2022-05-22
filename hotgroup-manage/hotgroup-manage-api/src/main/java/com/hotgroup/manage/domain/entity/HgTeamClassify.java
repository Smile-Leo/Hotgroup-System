package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 分类
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "Hg_team_classify")
@Entity
public class HgTeamClassify extends BaseEntity {

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
     * 用户id⽗级id 第⼀级为0
     */
    @Column(name = "parent_id", length = 11)
    private Long parentId;

    /**
     * 封面
     */
    @Column(name = "cover_img", length = 255)
    private String coverImg;

    /**
     * 名称
     */
    @Column(name = "name", length = 20)
    private String name;

    /**
     * 状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "状态有误")
    @Column(name = "status", length = 1)
    private Integer status;
}
