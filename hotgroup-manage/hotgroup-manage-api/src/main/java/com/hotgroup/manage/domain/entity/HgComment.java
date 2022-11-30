package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 用户评论
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "hg_comment")
@Entity
@TableName
public class HgComment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "userId自动生成", groups = InsertGroup.class)
    @TableId(type = IdType.ASSIGN_ID)
    @Id
    private String id;

    /**
     * 评论内容
     */
    @Column
    private String content;

    /**
     * 用户id
     */
    @Column(length = 11)
    private String userId;

    /**
     * 评论类型 1动态 2...
     */
    @Column(length = 1)
    private Integer dataType;

    /**
     * 数据来源id
     */
    @Column(length = 20)
    private String dataId;

    /**
     * 数据来源id
     */
    @Column(length = 20)
    private String parentId;

    /**
     * 转发数
     */
    @Column(length = 11)
    private Integer likeNum;
}
