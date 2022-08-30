package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.manage.api.ISysDictTypeService;
import com.hotgroup.manage.domain.entity.SysDictType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author Lzw
 */
//@RestController
//@RequestMapping("/system/dict/type")
@Api(tags = "字典类型信息")
public class SysDictTypeController {
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("list")
    @ApiOperation("列表")
    public AjaxResult<?> list(SysDictType dictType) {
        return dictTypeService.selectDictTypeList(dictType);
    }


    /**
     * 查询字典类型详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "info")
    @ApiOperation("信息")
    public AjaxResult<?> getInfo(Long dictId) {
        return AjaxResult.success(dictTypeService.selectDictTypeById(dictId));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping("add")
    @ApiOperation("新增")
    public AjaxResult<?> add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return AjaxResult.error("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        dictTypeService.insertDictType(dict);
        return AjaxResult.success();
    }

    /**
     * 修改字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PostMapping("edit")
    @ApiOperation("修改")
    public AjaxResult<?> edit(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return AjaxResult.error("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        dictTypeService.updateDictType(dict);
        return AjaxResult.success();
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @PostMapping("remove")
    @ApiOperation("删除")
    public AjaxResult<?> remove(Long[] dictIds) {
        int i = dictTypeService.deleteDictTypeByIds(dictIds);
        return AjaxResult.success();
    }

    /**
     * 清空缓存
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @PostMapping("clearCache")
    @ApiOperation("清空字典缓存")
    public AjaxResult<?> clearCache() {
        dictTypeService.clearCache();
        return AjaxResult.success();
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("optionselect")
    @ApiOperation("所有类型")
    public AjaxResult<?> optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return AjaxResult.success(dictTypes);
    }
}
