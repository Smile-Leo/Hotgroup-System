package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotgroup.commons.database.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户扩展字段表
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "hg_user_extension")
@Entity
@TableName
public class HgUserExtension extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.INPUT)
    @Id
    private String userId;

    /**
     * 关注数
     */
    @Column(length = 11)
    private Integer concernNum;

    /**
     * 动态数
     */
    @Column(length = 11)
    private Integer dynamicNum;

    /**
     * 粉丝数
     */
    @Column(length = 11)
    private Integer followersNum;

    /**
     * 积分
     */
    @Column(length = 11)
    private Integer points;

    public HgUserExtension getEmptyInstance() {
        this.userId = "";
        this.concernNum = 0;
        this.dynamicNum = 0;
        this.followersNum = 0;
        this.points = 0;
        return this;
    }
}
