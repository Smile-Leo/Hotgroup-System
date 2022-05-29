package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Null;

/**
 * 用户审核
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "Hg_user_info_audit")
@Entity
public class HgUserInfoAudit extends BaseEntity {

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
     * 用户账号
     */
    @Column(name = "account", length = 20)
    private String account;

    /**
     * 用户昵称
     */
    @Column(name = "user_name", length = 20)
    private String userName;

    /**
     * 用户性别 1男 0女
     */
    @Column(name = "gender", length = 1)
    private Integer gender;

    /**
     * 手机号码
     */
    @Column(name = "phone", length = 20)
    private String phone;

    /**
     * 用户头像
     */
    @Column(name = "head_img", length = 255)
    private String headImg;

    /**
     * 用户邮箱
     */
    @Column(name = "email", length = 100)
    private String email;

    /**
     * 身份证号
     */
    @Column(name = "id_number", length = 40)
    private String idNumber;

    /**
     * 真实名称
     */
    @Column(name = "real_name", length = 20)
    private String realName;

    /**
     * 用户类型 1主播 0普通⽤户
     */
    @Column(name = "type", length = 1)
    private Integer type;

    /**
     * 用户标签
     */
    @Column(name = "tags", length = 255)
    private String tags;

    /**
     * 地址
     */
    @Column(name = "address", length = 255)
    private String address;

    /**
     * 帐号状态（1正常 0停用）
     */
    @Column(name = "status", length = 1)
    private Integer status;

    /**
     * 状态（0待审核 1审核成功 2审核失败）
     */
    @Column(name = "audit_status", length = 1)
    private Integer auditStatus;

    @Transient
    private transient HgUser oldHgUser;
}
