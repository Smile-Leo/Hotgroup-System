package com.hotgroup.customer.core.facade;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotgroup.customer.api.IHotgroupVideoService;
import com.hotgroup.manage.api.IHgVideoService;
import com.hotgroup.manage.domain.entity.HgVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lzw
 * @date 2022/6/19.
 */
@Component
public class HotgroupVideoServiceImpl implements IHotgroupVideoService {


    @Autowired
    IHgVideoService hgVideoService;

    @Override
    public HgVideo getSuggerVideo() {
        return hgVideoService.getOne(Wrappers.lambdaQuery(HgVideo.class).last("ORDER BY RAND() limit 1"));
    }
}
