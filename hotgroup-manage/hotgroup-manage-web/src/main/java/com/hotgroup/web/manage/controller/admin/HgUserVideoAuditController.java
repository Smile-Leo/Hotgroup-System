package com.hotgroup.web.manage.controller.admin;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgUserVideoAuditService;
import com.hotgroup.manage.domain.entity.HgUserVideoAudit;
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
@RequestMapping("/admin/hg/userVideoAudit")
@Tag(name = "用户视频审核")
public class HgUserVideoAuditController {

    @Resource
    IHgUserVideoAuditService hgUserVideoAuditService;

    /**
     * 获取列表
     */
    @PreAuthorize("@ss.hasPermi('admin:userVideoAudit:list')")
    @GetMapping("list")
    @Operation(summary ="列表")
    public AjaxResult<?> list(HgUserVideoAudit hgUserVideoAudit) {
        return hgUserVideoAuditService.pageList(hgUserVideoAudit);
    }

    /**
     * 审核  auditStatus 状态（0待审核 1审核成功 2审核失败）
     */
    @PreAuthorize("@ss.hasPermi('admin:userVideoAudit:audit')")
    @GetMapping("audit")
    @Operation(summary ="审核")
    public AjaxResult<?> audit(HgUserVideoAudit hgUserVideoAudit) throws Exception {
        hgUserVideoAuditService.audit(hgUserVideoAudit);
        return AjaxResult.success();
    }
}