package com.hotgroup.web.manage.controller.admin;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgUserInfoAuditService;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户审核
 *
 * @author ajm
 */
@RestController
@RequestMapping("/admin/hg/userInfoAudit")
@Tag(name = "用户资料审核")
public class HgUserInfoAuditController {

    @Resource
    IHgUserInfoAuditService hgUserInfoAuditService;

    /**
     * 获取审核列表
     */
    @PreAuthorize("@ss.hasPermi('admin:userInfoAudit:list')")
    @GetMapping("list")
    @Operation(summary ="列表")
    public AjaxResult<?> list(HgUserInfoAudit hgUserInfoAudit) {
        return hgUserInfoAuditService.pageList(hgUserInfoAudit);
    }

    /**
     * 审核  auditStatus 状态（0待审核 1审核成功 2审核失败）
     */
    @PreAuthorize("@ss.hasPermi('admin:userInfoAudit:audit')")
    @GetMapping("audit")
    @Operation(summary ="审核")
    public AjaxResult<?> audit(HgUserInfoAudit hgUserInfoAudit) throws Exception {
        hgUserInfoAuditService.audit(hgUserInfoAudit);
        return AjaxResult.success();
    }
}