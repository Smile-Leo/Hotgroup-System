package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.core.mapper.HgUserExtensionMapper;
import com.hotgroup.manage.core.mapper.HgUserMapper;
import com.hotgroup.manage.core.mapping.HgUserExtensionMapping;
import com.hotgroup.manage.domain.dto.HgUserExtensionDto;
import com.hotgroup.manage.domain.entity.HgUser;
import com.hotgroup.manage.domain.entity.HgUserExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author Lzw
 * @date 2022/6/1.
 */
@Service
public class HgUserServiceImpl extends ServiceImpl<HgUserMapper, HgUser> implements IHgUserService {

    @Resource
    HgUserExtensionMapper hgUserExtensionMapper;

    @Override
    public HgUserExtensionDto getHgUserInfo(String userId) {
        HgUserExtensionDto dto = new HgUserExtensionDto();
        HgUser hgUser = baseMapper.selectById(userId);
        if (Objects.nonNull(hgUser)) {
            HgUserExtension hgUserExtension = hgUserExtensionMapper.selectById(hgUser.getId());
            if (Objects.nonNull(hgUserExtension)) {
                dto = HgUserExtensionMapping.INSTANCE.toDTO(hgUserExtension);
            }
            dto.setAccount(hgUser.getUserName());
            dto.setUserName(hgUser.getNickName());
            dto.setHeadImg(hgUser.getAvatar());
        }
        return dto;
    }

}
