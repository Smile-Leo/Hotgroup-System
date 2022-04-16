package com.hotgroup.manage.web.controller.system;

import com.lead.cloud.web.controller.tool.LicenseUserCountValidated;
import com.lead.common.annotation.Log;
import com.lead.common.constant.ApiConstants;
import com.lead.common.constant.UserConstants;
import com.lead.common.core.controller.BaseController;
import com.lead.common.core.domain.AjaxResult;
import com.lead.common.core.domain.entity.SysDept;
import com.lead.common.core.domain.entity.SysRole;
import com.lead.common.core.domain.entity.SysUser;
import com.lead.common.core.domain.entity.SysUserDept;
import com.lead.common.core.domain.model.LoginUser;
import com.lead.common.core.domain.vo.ComboBoxVo;
import com.lead.common.core.domain.vo.SysUserVo;
import com.lead.common.core.page.TableDataInfo;
import com.lead.common.enums.BusinessType;
import com.lead.common.event.bus.BusEventRote;
import com.lead.common.event.bus.BusEventService;
import com.lead.common.event.bus.WecomBusEvent;
import com.lead.common.utils.CollectionUtils;
import com.lead.common.utils.SecurityUtils;
import com.lead.common.utils.ServletUtils;
import com.lead.common.utils.StringUtils;
import com.lead.common.utils.poi.ExcelUtil;
import com.lead.common.validator.annotation.InsertGroup;
import com.lead.framework.web.service.TokenService;
import com.lead.system.service.ISysDeptService;
import com.lead.system.service.ISysPostService;
import com.lead.system.service.ISysRoleService;
import com.lead.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户信息
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/user")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysPostService postService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private TokenService tokenService;
    @Autowired(required = false)
    private LicenseUserCountValidated licenseUserCountValidated;

    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUser user) {
        startPage();
        List<SysUser> list = userService.pageUserList(user);
        return getDataTable(list);
    }

    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:user:export')")
    @GetMapping("/export")
    public AjaxResult export(SysUser user) {
        List<SysUserVo> list = userService.importSelectUserList(user);
        ExcelUtil<SysUserVo> util = new ExcelUtil<SysUserVo>(SysUserVo.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:user:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        Optional.ofNullable(licenseUserCountValidated).ifPresent(v -> v.valid(userList.size()));

        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String operName = loginUser.getUsername();
        AjaxResult message = userService.importUser(userList, updateSupport, operName);

        //发布总线通知
        List<SysUser> users = (List<SysUser>) message.get(AjaxResult.DATA_TAG);
        if (CollectionUtils.isNotEmpty(users)) {
            for (SysUser user : users) {
                BusEventService.sendAsync(WecomBusEvent.builder()
                        .type(BusEventRote.LOCAL_ROLE_ADD_USER)
                        .data(user)
                        .build());
            }
        }
        return AjaxResult.success(message.get(AjaxResult.MSG_TAG));
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream()
                .filter(r -> !r.isAdmin())
                .collect(Collectors.toList()));
        if (StringUtils.isNotNull(userId)) {
            SysUser sysUser = userService.selectUserByAuth(userId);
            Assert.notNull(sysUser, "用户不存在");
            List<SysUserDept> sysUserDepts = sysUser.getSysUserDepts();
            for (SysUserDept sysUserDept : sysUserDepts) {
                if (sysUserDept.getPostId().equals(0L)) {
                    sysUserDept.setPostId(null);
                }
                if (sysUserDept.getIsLeader() == null) {
                    sysUserDept.setIsLeader(0);
                }
            }

            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", roleService.selectRoleListByUserId(userId));
            ajax.put("deptIds", deptService.selectDeptListByUserId(userId));
        }
        return ajax;
    }

    /**
     * 新增用户检索手机 邮箱是否已存在
     */
    @PostMapping("/checkUser")
    public AjaxResult checkUser(@RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("登录账号已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("手机号码已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("邮箱账号已存在");
        }
        return AjaxResult.success();
    }

    /**
     * 新增用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated(value = {InsertGroup.class}) @RequestBody SysUser user) {
        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(user.getUserName()))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        if (StringUtils.isEmpty(user.getPassword())) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，密码不能为空");
        }
        if (CollectionUtils.isEmpty(user.getSysUserDepts())) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，部门信息不能为空");
        }
        for (SysUserDept sysUserDept : user.getSysUserDepts()) {
            SysDept sysDept = deptService.selectDeptById(sysUserDept.getDeptId());
            Assert.isTrue(sysDept != null, "所选部门无效");
            Assert.isTrue(user.getSyncSwitch().equals(0) || user.getSyncSwitch().equals(1) && sysDept.getSyncSwitch()
                    .equals(1), "所选部门没有同步");
        }
        user.setUserId(null);
        user.setCreateBy(SecurityUtils.getUsername());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        AjaxResult ajaxResult = toAjax(userService.insertUser(user));

        SysUser data = userService.selectUserById(user.getUserId());
        BusEventService.sendAsync(WecomBusEvent.builder().type(BusEventRote.LOCAL_ROLE_ADD_USER).data(data).build());

        return ajaxResult;
    }

    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        if (StringUtils.isEmpty(user.getPhonenumber()) && StringUtils.isEmpty(user.getEmail())) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码和邮箱不能同时为空");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        if (UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }

        //防止修改账号
        SysUser sysUser = userService.selectUserById(user.getUserId());
        if (sysUser == null) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，账号不存在");
        }
        user.setUserName(sysUser.getUserName());
        user.setUpdateBy(SecurityUtils.getUsername());

        //用户没权限的部门先加上
        List<SysUserDept> sysUserDepts = userService.selectUserDeptByNoAuth(user.getUserId());
        if (CollectionUtils.isNotEmpty(sysUserDepts)) {
            sysUserDepts.addAll(user.getSysUserDepts());
            ArrayList<SysUserDept> collect = sysUserDepts.stream().collect(
                    Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SysUserDept::getDeptId))),
                            ArrayList::new));
            user.setSysUserDepts(collect);
        }
        if (CollectionUtils.isEmpty(user.getSysUserDepts())) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，部门信息不能为空");
        }

        for (SysUserDept sysUserDept : user.getSysUserDepts()) {
            Assert.isTrue(sysUserDept.getOrderNum() >= 0 && sysUserDept.getOrderNum() <= ApiConstants.MAX_ORDERNUM,
                    "排序超过最大值");
            SysDept sysDept = deptService.selectDeptById(sysUserDept.getDeptId());
            Assert.isTrue(sysDept != null, "所选部门无效");
            Assert.isTrue(user.getSyncSwitch().equals(0) || user.getSyncSwitch().equals(1) && sysDept.getSyncSwitch()
                    .equals(1), "所选部门没有同步");
        }

        AjaxResult ajaxResult = toAjax(userService.updateSysUser(user));

        //重新查询信息
        SysUser id = userService.selectUserById(user.getUserId());
        BusEventService.sendAsync(WecomBusEvent.builder().type(BusEventRote.LOCAL_ROLE_UPDATA_USER).data(id).build());

        return ajaxResult;
    }

    /**
     * 删除用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds) {

        List<SysUser> users = Stream.of(userIds).map(userService::selectUserById).collect(Collectors.toList());

        users.stream().filter(Objects::nonNull)
                .forEach(sysUser -> {
                    if (userService.deleteUserById(sysUser.getUserId()) > 0) {
                        BusEventService.sendAsync(WecomBusEvent.builder()
                                .type(BusEventRote.LOCAL_ROLE_DEL_USER)
                                .data(sysUser)
                                .build());
                    }
                });

        return AjaxResult.success();
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysUser user) {
        userService.checkUserAllowed(user);
        user.setUpdateBy(SecurityUtils.getUsername());
        AjaxResult ajaxResult = toAjax(userService.updateUserStatus(user));

        SysUser sysUser = userService.selectUserById(user.getUserId());
        sysUser.setStatus(user.getStatus());
        BusEventService.sendAsync(WecomBusEvent.builder()
                .type(BusEventRote.LOCAL_ROLE_UPDATA_USER)
                .data(sysUser)
                .build());

        return ajaxResult;
    }

    /**
     * 获取部门下的人员
     */
    @PreAuthorize("@ss.hasPermi('system:user:dept-users')")
    @GetMapping("dept-users")
    public AjaxResult deptUsers(Long deptId) {
        List<ComboBoxVo> data = userService.deptUsers(deptId);
        return AjaxResult.success(data);
    }

}
