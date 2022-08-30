package com.hotgroup.manage.web.controller.admin;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgUserVideoAuditService;
import com.hotgroup.manage.domain.entity.HgUserVideoAudit;
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
@RequestMapping("/admin/hg/userVideoAudit")
@Api(tags = "用户视频审核")
public class HgUserVideoAuditController {

    @Autowired
    IHgUserVideoAuditService hgUserVideoAuditService;

    /**
     * 获取列表
     */
    @PreAuthorize("@ss.hasPermi('admin:userVideoAudit:list')")
    @GetMapping("list")
    @ApiOperation("列表")
    public AjaxResult<?> list(HgUserVideoAudit hgUserVideoAudit) {
        return hgUserVideoAuditService.pageList(hgUserVideoAudit);
    }

    /**
     * 审核  auditStatus 状态（0待审核 1审核成功 2审核失败）
     */
    @PreAuthorize("@ss.hasPermi('admin:userVideoAudit:audit')")
    @GetMapping("audit")
    @ApiOperation("审核")
    public AjaxResult<?> audit(HgUserVideoAudit hgUserVideoAudit) throws Exception {
        hgUserVideoAuditService.audit(hgUserVideoAudit);
        return AjaxResult.success();
    }
}