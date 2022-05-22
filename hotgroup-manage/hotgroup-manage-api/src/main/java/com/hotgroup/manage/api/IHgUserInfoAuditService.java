package com.hotgroup.manage.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import com.hotgroup.manage.domain.entity.SysUser;

import java.util.List;

/**
 * 用户资料审核 业务层
 *
 * @author ajm
 */
public interface IHgUserInfoAuditService {
    /**
     * 根据条件分页查询用户列表
     *
     * @param hgUserInfoAudit 用户审核信息
     * @return 用户审核信息集合信息
     */
    AjaxResult<List<HgUserInfoAudit>> pageList(HgUserInfoAudit hgUserInfoAudit);

    /**
     * 审核用户资料
     *
     * @param hgUserInfoAudit 用户信息
     * @return 结果
     */
    void audit(HgUserInfoAudit hgUserInfoAudit) throws JsonProcessingException;
}
