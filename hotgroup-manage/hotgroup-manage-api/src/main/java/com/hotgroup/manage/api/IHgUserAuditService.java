package com.hotgroup.manage.api;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;

import java.util.List;

/**
 * 用户审核 业务层
 *
 * @author ajm
 */
public interface IHgUserAuditService {


    /**
     * 根据条件分页查询用户列表
     *
     * @param hgUserInfoAudit 用户审核信息
     * @return 用户审核信息集合信息
     */
    AjaxResult<List<HgUserInfoAudit>> pageUserList(HgUserInfoAudit hgUserInfoAudit);
}
