package com.hotgroup.manage.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.domain.entity.HgUserVideoAudit;

import java.util.List;

/**
 * 用户资料审核 业务层
 *
 * @author ajm
 */
public interface IHgUserVideoAuditService extends IService<HgUserVideoAudit> {
    /**
     * 根据条件分页查询视频列表
     *
     * @param hgUserVideoAudit 上传视频信息
     * @return 审核信息集合信息
     */
    AjaxResult<List<HgUserVideoAudit>> pageList(HgUserVideoAudit hgUserVideoAudit);

    /**
     * 审核视频
     *
     * @param hgUserVideoAudit 上传视频信息
     * @return 结果
     */
    void audit(HgUserVideoAudit hgUserVideoAudit) throws Exception;
}
