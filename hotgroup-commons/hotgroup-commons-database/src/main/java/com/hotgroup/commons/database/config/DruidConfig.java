package com.hotgroup.commons.database.config;

import ch.qos.logback.classic.Level;
import com.hotgroup.commons.core.enums.DataSourceType;
import com.hotgroup.commons.core.spring.SpringUtils;
import com.hotgroup.commons.database.datasource.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.support.RetryTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * druid 配置多数据源
 *
 * @author Lzw
 */
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "srping.datasource")
public class DruidConfig {
//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.master")
    public DataSource masterDataSource() {
        return null;
    }

//    @Bean
//    @ConfigurationProperties("spring.datasource.druid.slave")
//    @ConditionalOnProperty(prefix = "spring.datasource.druid.slave", name = "enabled", havingValue = "true")
    public DataSource slaveDataSource() {
        return null;
    }

//    @Bean(name = "dynamicDataSource")
//    @Primary
    public DynamicDataSource dataSource(DataSource masterDataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.MASTER.name(), masterDataSource);
        setDataSource(targetDataSources, DataSourceType.SLAVE.name(), "slaveDataSource");
        DynamicDataSource dynamicDataSource = new DynamicDataSource(masterDataSource, targetDataSources);

        testDataSource(dynamicDataSource);

        return dynamicDataSource;
    }

    private void testDataSource(DynamicDataSource dynamicDataSource) {
        final RetryTemplate retryTemplate = RetryTemplate.builder()
                .retryOn(RuntimeException.class)
                .exponentialBackoff(500L, 2.0d, 10000L)
                .infiniteRetry()
                .build();

        ch.qos.logback.classic.LoggerContext loggerContext = (ch.qos.logback.classic.LoggerContext) LoggerFactory.getILoggerFactory();
        Level level = loggerContext.getLogger("com.alibaba").getLevel();
        loggerContext.getLogger("com.alibaba").setLevel(Level.valueOf("off"));
        retryTemplate.execute(retryContext -> {
            try {
                dynamicDataSource.getConnection();
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("master数据库连接失败,正在重试... 第{}次", retryContext.getRetryCount());
                throw new RuntimeException();
            }
            return null;
        });
        loggerContext.getLogger("com.alibaba").setLevel(level);
    }

    /**
     * 设置数据源
     *
     * @param targetDataSources 备选数据源集合
     * @param sourceName        数据源名称
     * @param beanName          bean名称
     */
    public void setDataSource(Map<Object, Object> targetDataSources, String sourceName, String beanName) {
        try {
            DataSource dataSource = SpringUtils.getBean(beanName);
            targetDataSources.put(sourceName, dataSource);
        } catch (Exception ignored) {
        }
    }


}
