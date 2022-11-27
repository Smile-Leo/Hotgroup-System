package com.hotgroup.manage.api;

import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.core.domain.model.IUserExt;
import com.hotgroup.manage.domain.entity.HgUser;

/**
 * @author Lzw
 * @date 2022/5/29.
 */
public interface IHotgroupUserLoginService {

    IUser login(HgUser user);

    IUser getUserByUnionId(String unionId);

    IUserExt getUserExtension(String userId);
}
