package com.hotgroup.manage.core.facade;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.commons.core.domain.model.IUserExt;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.api.IHotgroupUserLoginService;
import com.hotgroup.manage.core.mapper.HgUserExtensionMapper;
import com.hotgroup.manage.domain.entity.HgUser;
import com.hotgroup.manage.domain.entity.HgUserExtension;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Lzw
 * @date 2022/5/29.
 */
@Component
public class HotgroupUserLoginServiceImpl implements IHotgroupUserLoginService {
    @Resource
    IHgUserService hgUserService;

    @Resource
    HgUserExtensionMapper hgUserExtensionMapper;

    @Override
    public IUser login(HgUser user) {

        hgUserService.saveOrUpdate(user);

        //生成扩展表
        HgUserExtension hgUserExtension = hgUserExtensionMapper.selectById(user.getId());
        if (Objects.isNull(hgUserExtension)) {
            hgUserExtension = HgUserExtension.getEmptyInstance();
            hgUserExtensionMapper.insert(hgUserExtension);
        }

        return user;
    }

    @Override
    public IUser getUserByUnionId(String unionId) {

        return hgUserService.getOne(Wrappers.lambdaQuery(HgUser.class)
                .eq(HgUser::getUnionId, unionId));
    }

    @Override
    public IUser getUserByOpenid(String Openid) {
        return hgUserService.getOne(Wrappers.lambdaQuery(HgUser.class)
                .eq(HgUser::getOpenId, Openid));
    }

    public IUserExt getUserExtension(String userId){
        return hgUserExtensionMapper.selectById(userId);
    }
}
