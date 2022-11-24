package com.hotgroup.manage.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotgroup.manage.domain.entity.SysUser;

import java.util.List;

/**
 * 用户表 数据层
 *
 * @author Lzw
 */
public interface SysUserMapper extends BaseMapper<SysUser> {


    /**
     * 分页查询用户
     *
     * @param sysUser
     * @return
     */
    List<SysUser> pageUserList(SysUser sysUser);

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    SysUser selectUserByUserName(String userName);

    /**
     * 通过手机查询用户
     */
    SysUser selectUserByPhone(String phone);

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */

    SysUser selectUserById(String userId);


    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    int checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     *
     * @param phonenumber 手机号码
     * @return 结果
     */
    SysUser checkPhoneUnique(String phonenumber);


}
