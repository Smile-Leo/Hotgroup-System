package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.model.UserType;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.commons.database.page.PageHelper;
import com.hotgroup.manage.api.ISysConfigService;
import com.hotgroup.manage.api.ISysUserRoleService;
import com.hotgroup.manage.api.ISysUserService;
import com.hotgroup.manage.core.mapper.SysRoleMapper;
import com.hotgroup.manage.core.mapper.SysUserMapper;
import com.hotgroup.manage.domain.entity.SysRole;
import com.hotgroup.manage.domain.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 用户 业务层处理
 *
 * @author Lzw
 */
@Service
@Slf4j
public class SysUserServiceImpl implements ISysUserService, CommandLineRunner {

    @Resource
    private SysUserMapper userMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private ISysConfigService configService;
    @Resource
    private ISysUserRoleService userRoleService;

    @Override
    public void run(String... args) throws Exception {
        SysUser sysUser = userMapper.selectUserById("1L");
        if(Objects.isNull(sysUser)){
            SysUser sysUser1 = new SysUser();
            sysUser1.setUserId("1L");
            sysUser1.setUserType(UserType.SYS);
            sysUser1.setUserName("admin");
            sysUser1.setNickName("超管admin");
            sysUser1.setPassword("123456");
            insertUser(sysUser1);
        }
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    @Override
    public AjaxResult<List<SysUser>> pageUserList(SysUser user) {

        Page<SysUser> userPage = userMapper.selectPage(PageHelper.getPage(user), Wrappers.lambdaQuery(user));

        return AjaxResult.page(userPage);
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserByUserName(String userName) {
        return userMapper.selectOne(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getUserName, userName));
    }

    @Override
    public SysUser selectUserByPhone(String phone) {
        return userMapper.selectOne(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getPhone, phone));
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @Override
    public SysUser selectUserById(String userId) {
        return userMapper.selectById(userId);
    }


    /**
     * 校验用户名称是否唯一
     *
     * @param userName 用户名称
     * @return 结果
     */
    @Override
    public String checkUserNameUnique(String userName) {
        int count = userMapper.checkUserNameUnique(userName);
        if (count > 0) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return
     */
    @Override
    public String checkPhoneUnique(SysUser user) {
        if (StringUtils.isEmpty(user.getPhone())) {
            return UserConstants.UNIQUE;
        }
        String userId = Objects.isNull(user.getUserId()) ? "1" : user.getUserId();
        SysUser info = userMapper.checkPhoneUnique(user.getPhone());
        if (Objects.nonNull(info) && !info.getUserId().equals(userId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


    /**
     * 校验用户是否允许操作
     *
     * @param user 用户信息
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (Objects.nonNull(user.getUserId()) && user.isAdmin()) {
            throw new IllegalArgumentException("不允许操作超级管理员用户");
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertUser(SysUser user) {
        if (StringUtils.isBlank(user.getPassword())) {
            String password = configService.selectConfigByKey("sys.user.initPassword");
            user.setPassword(password);
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));

        // 新增用户信息
        int rows = userMapper.insert(user);
        // 新增用户与角色管理
        insertUserRole(user);

        return rows;
    }

    @Override
    public int updateSysUser(SysUser user) {
        String userId = user.getUserId();

        // 删除用户与角色关联
        userRoleService.removeUserId(userId);
        // 新增用户与角色管理
        insertUserRole(user);

        return userMapper.updateById(user);
    }


    /**
     * 修改用户状态
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserStatus(SysUser user) {
        return userMapper.update(null, Wrappers.lambdaUpdate(SysUser.class)
                .eq(SysUser::getUserId, user.getUserId())
                .set(SysUser::getStatus, user.getStatus())
        );
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateUserProfile(SysUser user) {
        return userMapper.updateById(user);
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户名
     * @param avatar   头像地址
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserAvatar(String userId, String avatar) {
        userMapper.update(null, Wrappers.lambdaUpdate(SysUser.class)
                .eq(SysUser::getNickName, userId)
                .set(SysUser::getAvatar, avatar)
        );
        return true;
    }

    /**
     * 重置用户密码
     *
     * @param user 用户信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetPwd(SysUser user) {
        String password = configService.selectConfigByKey("sys.user.initPassword");
        user.setPassword(SecurityUtils.encryptPassword(password));
        userMapper.update(null, Wrappers.lambdaUpdate(SysUser.class)
                .eq(SysUser::getUserId, user.getUserId())
                .set(SysUser::getAvatar, user.getPassword())
        );
        return 1;
    }

    /**
     * 重置用户密码
     *
     * @param userId 用户名
     * @param password 密码
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resetUserPwd(String userId, String password) {
        password = (SecurityUtils.encryptPassword(password));
        userMapper.update(null, Wrappers.lambdaUpdate(SysUser.class)
                .eq(SysUser::getUserId, userId)
                .set(SysUser::getAvatar, password)
        );
        return 1;
    }

    /**
     * 新增用户角色信息
     *
     * @param user 用户对象
     */
    public void insertUserRole(SysUser user) {
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return;
        }
        String[] roleIds = user.getRoles().stream()
                .map(SysRole::getRoleId).toArray(String[]::new);
        userRoleService.add(user.getUserId(), roleIds);

    }


    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserById(String userId) {
        // 删除用户与角色关联
        userRoleService.removeUserId(userId);
        return userMapper.deleteById(userId);
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteUserByIds(String[] userIds) {
        for (String userId : userIds) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            checkUserAllowed(user);
            userRoleService.removeUserId(userId);
        }

        userMapper.deleteBatchIds(Arrays.asList(userIds));
        return userIds.length;
    }



}
