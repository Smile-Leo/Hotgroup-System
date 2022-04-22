package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.annotation.RepeatSubmit;
import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.manage.api.ISysConfigService;
import com.hotgroup.manage.domain.entity.SysConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 参数配置 信息操作处理
 *
 * @author Lzw
 */
@Api(tags = "参数配置")
@RestController
@RequestMapping("/system/config")
public class SysConfigController {
    @Autowired
    private ISysConfigService configService;

    /**
     * 获取参数配置列表
     */
    @ApiOperation("参数列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("list")
    public AjaxResult<?> list(SysConfig config) {

        return configService.selectConfigList(config);
    }


    /**
     * 根据参数编号获取详细信息
     */
    @ApiOperation("参数信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping("info")
    public AjaxResult<?> getInfo(Long configId) {
        return AjaxResult.success(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @ApiOperation("key查信息")
    @GetMapping(value = "/configKey")
    public AjaxResult<?> getConfigKey(String configKey) {
        return AjaxResult.success(configService.selectConfigByKey(configKey));
    }

    /**
     * 新增参数配置
     */
    @ApiOperation("新增")
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping("add")
    @RepeatSubmit
    public AjaxResult<?> add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return AjaxResult.error("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        configService.insertConfig(config);
        return AjaxResult.success();
    }

    /**
     * 修改参数配置
     */
    @ApiOperation("修改")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PostMapping("edit")
    public AjaxResult<?> edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return AjaxResult.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        configService.updateConfig(config);
        return AjaxResult.success();
    }

    /**
     * 删除参数配置
     */
    @ApiOperation("删除")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @PostMapping("remove")
    public AjaxResult<?> remove(Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return AjaxResult.success();
    }

    /**
     * 清空缓存
     */
    @ApiOperation("清除缓存")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @PostMapping("clearCache")
    public AjaxResult<?> clearCache() {
        configService.clearCache();
        return AjaxResult.success();
    }
}
