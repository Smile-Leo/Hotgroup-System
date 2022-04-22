package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.manage.api.ISysDictDataService;
import com.hotgroup.manage.api.ISysDictTypeService;
import com.hotgroup.manage.domain.entity.SysDictData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 数据字典信息
 *
 * @author Lzw
 */
@RestController
@RequestMapping("/system/dict/data")
@Api(tags = "数据字典")
public class SysDictDataController {
    @Autowired
    private ISysDictDataService dictDataService;
    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    @ApiOperation("字典列表")
    public AjaxResult<?> list(SysDictData dictData) {
        return dictDataService.selectDictDataList(dictData);
    }


    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping("info")
    @ApiOperation("字典详情")
    public AjaxResult<?> getInfo(Long dictCode) {
        return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "type")
    @ApiOperation("查询类型字典")
    public AjaxResult<?> dictType(String dictType) {
        return AjaxResult.success(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping("add")
    @ApiOperation("新增")
    public AjaxResult<?> add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        int i = dictDataService.insertDictData(dict);
        return AjaxResult.success();
    }

    /**
     * 修改保存字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @PostMapping("edit")
    @ApiOperation("修改")
    public AjaxResult<?> edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        dictDataService.updateDictData(dict);
        return AjaxResult.success();
    }

    /**
     * 删除字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @PostMapping("remove")
    @ApiOperation("删除")
    public AjaxResult<?> remove(Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return AjaxResult.success();
    }
}
