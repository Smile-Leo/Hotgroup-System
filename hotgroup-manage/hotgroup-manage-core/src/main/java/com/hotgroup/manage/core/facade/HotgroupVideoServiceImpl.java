package com.hotgroup.manage.core.facade;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.api.IHgVideoService;
import com.hotgroup.manage.api.IHotgroupVideoService;
import com.hotgroup.manage.domain.entity.HgUser;
import com.hotgroup.manage.domain.entity.HgVideo;
import com.hotgroup.manage.domain.vo.HgIndexVo;
import com.hotgroup.manage.domain.vo.HomepageVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Lzw
 * @date 2022/6/19.
 */
@Component
public class HotgroupVideoServiceImpl implements IHotgroupVideoService {
    @Resource
    IHgVideoService hgVideoService;
    @Resource
    IHgUserService hgUserService;


    @Override
    public HgVideo getSuggerVideo() {
        return hgVideoService.getOne(Wrappers.lambdaQuery(HgVideo.class).last("ORDER BY RAND() limit 1"));
    }

    @Override
    public HgIndexVo index() {
        HgVideo video = getSuggerVideo();
        String userId = video.getUserId();
        HgUser user = hgUserService.getById(userId);
        HgIndexVo vo = new HgIndexVo();
        vo.setUserId(user.getId());
        vo.setPhoto(user.getPhoto());
        vo.setNickName(user.getNickName());
        vo.setVideoId(video.getId());
        vo.setVideoUrl(video.getUrl());
        vo.setHeat(0);
        return vo;
    }

    @Override
    public Page<HomepageVo> homepage(Integer pageNo, Integer pageSize) {
        Page<HgVideo> page = hgVideoService.page(Page.of(pageNo, pageSize), Wrappers.lambdaQuery(HgVideo.class).last("ORDER BY RAND()"));
        Set<String> userIds = page.getRecords().stream().map(HgVideo::getUserId).collect(Collectors.toSet());

        Map<String, HgUser> userMap = hgUserService.listByIds(userIds)
                .stream().collect(Collectors.toMap(HgUser::getId, Function.identity()));

        List<HomepageVo> voList = page.getRecords().stream().map(hgVideo -> {
            HomepageVo vo = new HomepageVo();
            vo.setNickName(userMap.get(hgVideo.getUserId()).getNickName());
            vo.setCoverImg(hgVideo.getCoverImg());
            vo.setVideoId(hgVideo.getId());
            return vo;
        }).collect(Collectors.toList());

        Page<HomepageVo> of = Page.of(pageNo, pageSize);
        return of.setRecords(voList);


    }
}
