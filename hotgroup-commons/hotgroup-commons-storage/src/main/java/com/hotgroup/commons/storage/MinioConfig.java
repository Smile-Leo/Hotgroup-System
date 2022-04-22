package com.hotgroup.commons.storage;

import io.minio.MinioClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Data
@ConfigurationProperties(prefix = "minio")
@Slf4j
@Configuration
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;

    private String defaultBucket;


    /**
     * 注入minio 客户端
     *
     * @return
     */
    @Bean
    @Lazy
    public MinioClient minioClient() {
        log.info("endpoint:{},accessKey:{},secretKey:{}", endpoint, accessKey, secretKey);
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Bean
    @Lazy
    public FileStorageService minio(MinioClient client) {
        return new MinioImpl(client, defaultBucket, new FileInfoFactory(defaultBucket));
    }
}
