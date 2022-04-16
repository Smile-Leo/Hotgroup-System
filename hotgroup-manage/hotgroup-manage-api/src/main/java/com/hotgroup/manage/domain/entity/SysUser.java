package com.hotgroup.manage.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.database.domain.BaseEntity;
import com.hotgroup.commons.validator.annotation.InsertGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户对象 sys_user
 *
 * @author ruoyi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUser extends BaseEntity implements IUser {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Null(message = "userId自动生成", groups = InsertGroup.class)
    @Id
    private Long userId;

    /**
     * 用户账号
     */
    @Size(max = 64, message = "用户账号长度不能超过64个字符")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "非法参数,用户名称不能含有中文及特殊字符")
    private String userName;

    /**
     * 用户昵称
     */
    @Size(max = 32, message = "用户名称长度不能超过32个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9~！@#￥%…&*（）—+\\-=·【】、；‘，。{}|：《》？!$^()_+-=`:\";'<>?,./]+$", message = "非法参数,用户昵称不能含有特殊字符")
    private String nickName;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 64, message = "邮箱长度不能超过64个字符")
    private String email;

    /**
     * 手机号码
     */
    @Size(min = 11, max = 11, message = "手机号码使用11位数字")
    private String phonenumber;

    /**
     * 用户性别
     */
    @Size(min = 1, max = 1, message = "用户性别有误")
    private String sex;

    /**
     * 用户头像
     */
    @Size(max = 255, message = "头像长度不能超过255个字符")
    private String avatar;


    /**
     * 地址
     */
    @Size(max = 128, message = "地址长度不能超过128个字符")
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9_ .-]+$", message = "非法参数,地址只能由字母,数字,空格._-组成")
    private String address;


    /**
     * 密码
     */
    @JsonIgnore
    @JsonProperty
    @Size(groups = {InsertGroup.class}, min = 6, max = 32, message = "密码使用6-32位字母")
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Size(min = 1, max = 1, message = "用户状态有误")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Size(max = 1, message = "删除状态有误")
    @JsonIgnore
    private String delFlag;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 角色对象
     */
    private List<SysRole> roles;


    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
