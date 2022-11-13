package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * 分类
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity
@TableName
public class HgTeamClassify extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "Id自动生成", groups = InsertGroup.class)
    @TableId(type = IdType.ASSIGN_ID)
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
