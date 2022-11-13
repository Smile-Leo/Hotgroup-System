package com.hotgroup.manage.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hotgroup.commons.core.constant.UserConstants;
import com.hotgroup.manage.api.ISysConfigService;
import com.hotgroup.manage.core.mapper.SysConfigMapper;
import com.hotgroup.manage.domain.entity.SysConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 参数配置 服务层实现
 *
 * @author Lzw
 */
@Service
public class SysConfigServiceImpl implements ISysConfigService {
    @Resource
    private SysConfigMapper configMapper;


    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @Override
    public SysConfig selectConfigById(String configId) {

        return configMapper.selectById(configId);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Override
    @Cacheable(key = "#configKey")
    public String selectConfigByKey(String configKey) {
        SysConfig retConfig = configMapper.selectOne(Wrappers.lambdaQuery(SysConfig.class)
                .eq(SysConfig::getConfigKey, configKey));
        return StringUtils.trimToNull(retConfig.getConfigValue());
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {

        return configMapper.selectList(Wrappers.query(config));
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int insertConfig(SysConfig config) {
        return configMapper.insert(config);
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    @CacheEvict(key = "#config.configKey")
    public int updateConfig(SysConfig config) {
        return configMapper.updateById(config);
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     * @return 结果
     */
    @Override
    @CacheEvict(allEntries = true)
    public int deleteConfigByIds(String[] configIds) {
        if (ArrayUtils.isEmpty(configIds)) {
            return 1;
        }
        for (String configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (StringUtils.endsWithIgnoreCase(UserConstants.YES, config.getConfigType())) {
                throw new IllegalArgumentException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
        }
        return configMapper.deleteBatchIds(Arrays.stream(configIds).collect(Collectors.toList()));
    }

    /**
     * 清空缓存数据
     */
    @Override
    @CacheEvict(allEntries = true)
    public void clearCache() {
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public String checkConfigKeyUnique(SysConfig config) {
        String configId = Objects.isNull(config.getConfigId()) ? "1" : config.getConfigId();
        SysConfig info = configMapper.selectOne(Wrappers.lambdaQuery(SysConfig.class)
                .eq(SysConfig::getConfigKey, config.getConfigKey()));
        if (Objects.nonNull(info) && !info.getConfigId().equals(configId)) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }


}
