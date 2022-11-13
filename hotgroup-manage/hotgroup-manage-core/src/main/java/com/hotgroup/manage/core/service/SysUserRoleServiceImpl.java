package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotgroup.manage.api.ISysUserRoleService;
import com.hotgroup.manage.core.mapper.SysUserRoleMapper;
import com.hotgroup.manage.domain.entity.SysUserRole;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Lzw
 * @date 2022/11/13.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRoleServiceImpl implements ISysUserRoleService {
    @Resource
    SysUserRoleMapper userRoleMapper;

    @Override
    public void removeUserId(String userId) {
        userRoleMapper.delete(Wrappers.lambdaUpdate(SysUserRole.class)
                .eq(SysUserRole::getUserId, userId)
        );
    }

    @Override
    public void removeRoleId(String roleId) {
        userRoleMapper.delete(Wrappers.lambdaUpdate(SysUserRole.class)
                .eq(SysUserRole::getRoleId, roleId)
        );
    }

    @Override
    public void add(String userId, String[] roleIds) {
        if (ArrayUtils.isEmpty(roleIds) || Objects.isNull(userId)) {
            return;
        }
        List<SysUserRole> collect = Arrays.stream(roleIds)
                .map(role -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(role);
                    return ur;
                }).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            userRoleMapper.insertBatchSomeColumn(collect);
        }
    }

    @Override
    public void add(String[] userIds, String roleId) {
        if (ArrayUtils.isEmpty(userIds) || Objects.isNull(roleId)) {
            return;
        }
        List<SysUserRole> collect = Arrays.stream(userIds)
                .map(userId -> {
                    SysUserRole ur = new SysUserRole();
                    ur.setUserId(userId);
                    ur.setRoleId(roleId);
                    return ur;
                }).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            userRoleMapper.insertBatchSomeColumn(collect);
        }
    }
}
