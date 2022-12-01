package com.hotgroup.web.customer.controller.index;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.manage.api.IHotgroupVideoService;
import com.hotgroup.manage.domain.vo.HgIndexVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lzw
 * @date 2022/6/19.
 */
@Tag(name = "首页")
@RestController
@RequestMapping("public/index")
@Slf4j
public class PublicMediaController {

    @Resource
    IHotgroupVideoService videoService;

    @GetMapping("media")
    @Operation(summary = "视频推荐")
    public AjaxResult<?> getMedia() {
        return AjaxResult.success(videoService.getSuggerVideo());
    }

    @GetMapping("suggest")
    @Operation(summary = "推荐接口")
    public AjaxResult<HgIndexVo> index() {
        HgIndexVo index = videoService.index();
        return AjaxResult.success(index);
    }

    @GetMapping("homepage")
    @Operation(summary = "首页接口")
    public AjaxResult<?> homepage(@RequestParam(defaultValue = "0") Integer pageNo,
                                  @RequestParam(defaultValue = "10") Integer pageSize) {
        return AjaxResult.success(videoService.homepage(pageNo, pageSize));
    }
}
