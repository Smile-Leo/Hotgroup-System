package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hotgroup.commons.core.domain.model.IUserExt;
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
public class HgUserExtension extends BaseEntity implements IUserExt {

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

    /**
     * 背景
     */
    @Column(length = 255)
    private String background;

    /**
     * 等级
     */
    @Column(length = 11)
    private Integer level;

    /**
     * 经验值
     */
    @Column(length = 11)
    private Long currentExperience;

    /**
     * 星钻数
     */
    @Column(length = 11)
    private Integer starDiamondNum;

    public static HgUserExtension getEmptyInstance() {
        HgUserExtension hgUserExtension = new HgUserExtension();
        hgUserExtension.setUserId("");
        hgUserExtension.setConcernNum(0);
        hgUserExtension.setDynamicNum(0);
        hgUserExtension.setFollowersNum(0);
        hgUserExtension.setPoints(0);
        hgUserExtension.setBackground("");
        hgUserExtension.setLevel(0);
        hgUserExtension.setCurrentExperience(0L);
        hgUserExtension.setStarDiamondNum(0);
        return hgUserExtension;
    }
}
