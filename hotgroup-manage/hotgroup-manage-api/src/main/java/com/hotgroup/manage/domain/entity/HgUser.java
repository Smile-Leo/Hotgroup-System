package com.hotgroup.manage.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 用户对象 user
 *
 * @author ajm
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "sys_user")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class HgUser extends SysUser {

    private static final long serialVersionUID = 1L;

    @Size(min = 18, max = 18, message = "身份证号长度使用18位数字")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "身份证号长度使用18位数字")
    @Column(length = 40)
    private String idNumber;

    @Size(max = 32, message = "用户真实名称长度不能超过32个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z0-9~！@#￥%…&*（）—+\\-=·【】、；‘，。{}|：《》？!$^()_+-=`:\";'<>?,./]+$", message = "非法参数,用户真实名称不能含有特殊字符")
    @Column(length = 20)
    private String realName;

    @Size(max = 255, message = "用户标签不能超过255个字符")
    private String tags;

    @Size(max = 128, message = "地址长度不能超过128个字符")
    @Pattern(regexp = "^$|^[\\u4E00-\\u9FA5A-Za-z0-9_ .-]+$", message = "非法参数,地址只能由字母,数字,空格._-组成")
    private String address;

    @Column(unique = true)
    private String openId;

    @Column(unique = true)
    private String unionId;

    private String qrCode;
}
