package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户对象 user
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HgUser extends BaseEntity implements IUser {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Null(message = "userId自动生成", groups = InsertGroup.class)
    @TableId(type = IdType.ASSIGN_UUID)
    @Id
    private String id;

    /**
     * 用户账号
     */
    @Size(max = 64, message = "用户账号长度不能超过64个字符")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "非法参数,账号不能含有中文及特殊字符")
    @Column(unique = true)
    private String account;

    /**
     * 密码
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(groups = {InsertGroup.class}, min = 6, max = 32, message = "密码使用6-32位字母")
    @Column(length = 20)
    private String password;

    /**
     * 用户昵称
     */
    @Size(max = 32, message = "用户名称长度不能超过32个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9~！@#￥%…&*（）—+\\-=·【】、；‘，。{}|：《》？!$^()_+-=`:\";'<>?,./]+$", message = "非法参数,用户昵称不能含有特殊字符")
    private String userName;

    /**
     * 用户性别 1男 0女
     */
    @Size(min = 1, max = 1, message = "用户性别有误")
    @Column(length = 1)
    private Integer gender;

    /**
     * 手机号码
     */
    @Size(min = 11, max = 11, message = "手机号码使用11位数字")
    @Column(length = 20, unique = true)
    private String phone;

    /**
     * 用户头像
     */
    @Size(max = 255, message = "头像长度不能超过255个字符")
    private String headImg;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 64, message = "邮箱长度不能超过64个字符")
    @Column(length = 100)
    private String email;

    /**
     * 身份证号
     */
    @Size(min = 18, max = 18, message = "身份证号长度使用18位数字")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "身份证号长度使用18位数字")
    @Column(length = 40)
    private String idNumber;

    /**
     * 真实名称
     */
    @Size(max = 32, message = "用户真实名称长度不能超过32个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9~！@#￥%…&*（）—+\\-=·【】、；‘，。{}|：《》？!$^()_+-=`:\";'<>?,./]+$", message = "非法参数,用户真实名称不能含有特殊字符")
    @Column(length = 20)
    private String realName;

    /**
     * 用户类型 1主播 0普通⽤户
     */
    @Size(min = 1, max = 1, message = "用户类型有误")
    @Column(length = 1)
    private Integer type;

    /**
     * 用户标签
     */
    @Size(max = 255, message = "用户标签不能超过255个字符")
    private String tags;

    /**
     * 地址
     */
    @Size(max = 128, message = "地址长度不能超过128个字符")
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9_ .-]+$", message = "非法参数,地址只能由字母,数字,空格._-组成")
    private String address;

    @Column(unique = true)
    private String openId;

    @Column(unique = true)
    private String unionId;

    /**
     * 帐号状态（1正常 0停用）
     */
    @Size(min = 1, max = 1, message = "用户状态有误")
    @Column(length = 1)
    private Integer status;

    public static boolean isAdmin(String userId) {
        return "1L".equals(userId);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin(this.id);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
