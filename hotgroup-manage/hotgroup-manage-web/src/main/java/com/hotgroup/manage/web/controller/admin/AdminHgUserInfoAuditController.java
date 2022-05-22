package com.hotgroup.manage.web.controller.admin;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgUserInfoAuditService;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户审核
 *
 * @author ajm
 */
@RestController
@RequestMapping("/admin/userInfoAudit")
@Api(tags = "用户资料审核")
public class AdminHgUserInfoAuditController {

    @Autowired
    IHgUserInfoAuditService hgUserInfoAuditService;

    /**
     * 获取审核列表
     */
    @PreAuthorize("@ss.hasPermi('admin:userInfoAudit:list')")
    @GetMapping("list")
    @ApiOperation("列表")
    public AjaxResult<?> list(HgUserInfoAudit hgUserInfoAudit) {
        return hgUserInfoAuditService.pageList(hgUserInfoAudit);
    }

}