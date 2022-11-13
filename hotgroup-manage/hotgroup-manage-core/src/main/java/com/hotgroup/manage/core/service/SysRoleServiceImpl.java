package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.database.page.PageHelper;
import com.hotgroup.manage.api.ISysRoleService;
import com.hotgroup.manage.core.mapper.SysRoleMapper;
import com.hotgroup.manage.core.mapper.SysRoleMenuMapper;
import com.hotgroup.manage.core.mapper.SysUserRoleMapper;
import com.hotgroup.manage.domain.entity.SysRole;
import com.hotgroup.manage.domain.entity.SysRoleMenu;
import com.hotgroup.manage.domain.entity.SysUserRole;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色 业务层处理
 *
 * @author Lzw
 */
@Service
public class SysRoleServiceImpl implements ISysRoleService {
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysRoleMenuMapper roleMenuMapper;
    @Resource
    private SysUserRoleMapper userRoleMapper;


    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    @Override
    public AjaxResult<List<SysRole>> selectRoleList(SysRole role) {

        Page<SysRole> rolePage = roleMapper.selectPage(PageHelper.getPage(role), Wrappers.lambdaQuery(role));
        return AjaxResult.page(rolePage);
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(String userId) {
        Set<String> roleIds = userRoleMapper.selectList(Wrappers.lambdaQuery(SysUserRole.class)
                        .eq(SysUserRole::getUserId, userId))
                .stream().map(SysUserRole::getRoleId).collect(Collectors.toSet());
        return roleMapper.selectBatchIds(roleIds)
                .stream()
                .map(SysRole::getRoleKey)
                .collect(Collectors.toSet());

    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    @Override
    public List<String> selectRoleListByUserId(String userId) {
        return userRoleMapper.selectList(Wrappers.lambdaQuery(SysUserRole.class)
                        .eq(SysUserRole::getUserId, userId))
                .stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }


    public SysRole selectRoleByRoleKey(String roleKey) {
        return roleMapper.selectOne(Wrappers.lambdaQuery(SysRole.class)
                .eq(SysRole::getRoleKey, roleKey));
    }

    public SysRole selectRoleByRoleName(String roleName) {
        return roleMapper.selectOne(Wrappers.lambdaQuery(SysRole.class)
                .eq(SysRole::getRoleName, roleName));
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    @Override
    public SysRole selectRoleById(String roleId) {
        return roleMapper.selectById(roleId);
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleNameUnique(SysRole role) {
        String roleId = Objects.isNull(role.getRoleId()) ? "1" : role.getRoleId();
        SysRole info = selectRoleByRoleName(role.getRoleName());
        if (Objects.nonNull(info) && !info.getRoleId().equals(roleId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public String checkRoleKeyUnique(SysRole role) {
        String roleId = Objects.isNull(role.getRoleId()) ? "1" : role.getRoleId();
        SysRole info = selectRoleByRoleKey(role.getRoleKey());
        if (Objects.nonNull(info) && !info.getRoleId().equals(roleId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    @Override
    public void checkRoleAllowed(SysRole role) {
        if (Objects.nonNull(role.getRoleId()) && role.isAdmin()) {
            throw new IllegalArgumentException("不允许操作超级管理员角色");
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int countUserRoleByRoleId(String roleId) {
        Long count = userRoleMapper.selectCount(Wrappers.lambdaUpdate(SysUserRole.class)
                .eq(SysUserRole::getRoleId, roleId));
        return Math.toIntExact(count);
    }

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int insertRole(SysRole role) {
        // 新增角色信息
        roleMapper.insert(role);
        return insertRoleMenu(role);
    }

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int updateRole(SysRole role) {
        // 修改角色信息
        roleMapper.updateById(role);
        // 删除角色与菜单关联
        deleteRoleMenuByRoleId(role.getRoleId());
        return insertRoleMenu(role);
    }

    public void deleteRoleMenuByRoleId(String roleId) {
        roleMenuMapper.delete(Wrappers.lambdaQuery(SysRoleMenu.class)
                .eq(SysRoleMenu::getRoleId, roleId)
        );
    }

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    public int updateRoleStatus(SysRole role) {
        return roleMapper.update(null, Wrappers.lambdaUpdate(SysRole.class)
                .eq(SysRole::getRoleId, role.getRoleId())
                .set(SysRole::getStatus, role.getStatus())
        );
    }

    /**
     * 修改数据权限信息
     *
     * @param role 角色信息
     * @return 结果
     */
    @Override
    @Transactional
    public int authDataScope(SysRole role) {
        // 修改角色信息
        roleMapper.updateById(role);
        return 1;
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public int insertRoleMenu(SysRole role) {
        // 新增角色与菜单管理
        List<SysRoleMenu> list = new ArrayList<>();
        for (String menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        if (list.size() > 0) {
            roleMenuMapper.insertBatchSomeColumn(list);
        }
        return 1;
    }


    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleById(String roleId) {
        deleteRoleMenuByRoleId(roleId);
        return roleMapper.deleteById(roleId);
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Override
    public int deleteRoleByIds(String[] roleIds) {

        if (ArrayUtils.isEmpty(roleIds)) {
            return 1;
        }

        for (String roleId : roleIds) {
            checkRoleAllowed(new SysRole(roleId));
            SysRole role = selectRoleById(roleId);
            if (countUserRoleByRoleId(roleId) > 0) {
                throw new IllegalArgumentException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
            deleteRoleMenuByRoleId(roleId);
        }
        return roleMapper.deleteBatchIds(Arrays.asList(roleIds));
    }
}
