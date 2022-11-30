package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotgroup.commons.core.domain.model.IUserExt;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Null;

/**
 * 用户动态
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "hg_dynamic")
@Entity
@TableName
public class HgDynamic extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "userId自动生成", groups = InsertGroup.class)
    @TableId(type = IdType.ASSIGN_ID)
    @Id
    private String id;

    /**
     * 动态内容
     */
    @Column
    private String content;

    /**
     * 用户id
     */
    @Column(length = 20)
    private String userId;

    /**
     * 动态数
     */
    @Column(length = 11)
    private Integer commentNum;

    /**
     * 点赞数
     */
    @Column(length = 11)
    private Integer likeNum;

    /**
     * 浏览数
     */
    @Column(length = 11)
    private Integer viewNum;

    /**
     * 转发数
     */
    @Column(length = 11)
    private Integer forwardNum;

    /**
     * 图书json数组
     */
    @Column
    private String imgs;
}
