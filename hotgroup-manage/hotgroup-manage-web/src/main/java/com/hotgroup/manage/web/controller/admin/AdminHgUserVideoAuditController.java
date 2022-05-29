package com.hotgroup.manage.web.controller.admin;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgUserInfoAuditService;
import com.hotgroup.manage.api.IHgUserVideoAuditService;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import com.hotgroup.manage.domain.entity.HgUserVideo;
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
@RequestMapping("/admin/userVideoAudit")
@Api(tags = "用户资料审核")
public class AdminHgUserVideoAuditController {

    @Autowired
    IHgUserVideoAuditService hgUserVideoAuditService;

    /**
     * 获取列表
     */
    @PreAuthorize("@ss.hasPermi('admin:userVideoAudit:list')")
    @GetMapping("list")
    @ApiOperation("列表")
    public AjaxResult<?> list(HgUserVideo hgUserVideo) {
        return hgUserVideoAuditService.pageList(hgUserVideo);
    }

    /**
     * 审核  auditStatus 状态（0待审核 1审核成功 2审核失败）
     */
    @PreAuthorize("@ss.hasPermi('admin:userVideoAudit:audit')")
    @GetMapping("audit")
    @ApiOperation("审核")
    public AjaxResult<?> audit(HgUserVideo hgUserVideo) throws Exception {
        hgUserVideoAuditService.audit(hgUserVideo);
        return AjaxResult.success();
    }
}