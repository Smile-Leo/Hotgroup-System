package com.hotgroup.web.manage.controller.admin;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgUserAuditService;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户信息
 *
 * @author Lzw
 */
@RestController
@RequestMapping("/admin/hg/user/")
@Api(tags = "用户管理")
public class HgUserInfoController {

    @Resource
    IHgUserAuditService iHgUserAuditService;


    /**
     * 获取用户列表
     */
    @PreAuthorize("@ss.hasPermi('admin:user:audit:list')")
    @GetMapping("list")
    public AjaxResult<?> list(HgUserInfoAudit hgUserInfoAudit) {
        return iHgUserAuditService.pageUserList(hgUserInfoAudit);
    }

}
