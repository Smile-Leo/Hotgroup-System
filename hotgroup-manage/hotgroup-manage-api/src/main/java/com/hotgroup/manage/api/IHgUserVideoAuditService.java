package com.hotgroup.manage.api;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.domain.entity.HgUserInfoAudit;
import com.hotgroup.manage.domain.entity.HgUserVideo;

import java.util.List;

/**
 * 用户资料审核 业务层
 *
 * @author ajm
 */
public interface IHgUserVideoAuditService {
    /**
     * 根据条件分页查询视频列表
     *
     * @param hgUserVideo 上传视频信息
     * @return 审核信息集合信息
     */
    AjaxResult<List<HgUserVideo>> pageList(HgUserVideo hgUserVideo);

    /**
     * 审核视频
     *
     * @param hgUserVideo 上传视频信息
     * @return 结果
     */
    void audit(HgUserVideo hgUserVideo) throws Exception;
}
