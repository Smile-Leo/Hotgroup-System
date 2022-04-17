package com.hotgroup.manage.web.controller.system;

import com.hotgroup.commons.core.domain.vo.AjaxResult;
import com.hotgroup.commons.core.utils.SecurityUtils;
import com.hotgroup.manage.api.ISysDictDataService;
import com.hotgroup.manage.api.ISysDictTypeService;
import com.hotgroup.manage.domain.entity.SysDictData;
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
public class SysDictDataController {
    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public AjaxResult<?> list(SysDictData dictData) {
        return dictDataService.selectDictDataList(dictData);
    }


    /**
     * 查询字典数据详细
     */
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping()
    public AjaxResult<?> getInfo(Long dictCode) {
        return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type")
    public AjaxResult<?> dictType(String dictType) {
        return AjaxResult.success(dictTypeService.selectDictDataByType(dictType));
    }

    /**
     * 新增字典类型
     */
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @PostMapping("add")
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
    public AjaxResult<?> remove(Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return AjaxResult.success();
    }
}
