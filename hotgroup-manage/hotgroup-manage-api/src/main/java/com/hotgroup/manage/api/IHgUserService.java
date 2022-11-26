package com.hotgroup.manage.api;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.domain.dto.HgUserExtensionDto;
import com.hotgroup.manage.domain.entity.HgUser;

/**
 * @author aijiaming
 * @date 2022/11/26.
 */
public interface IHgUserService extends IService<HgUser> {

    HgUserExtensionDto getHgUserInfo(String userId);

}
