package com.hotgroup.commons.framework.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * @author Lzw
 * @date 2022/4/17.
 */
@Configuration
@ConditionalOnProperty(value = "springfox.documentation.enabled", havingValue = "true", matchIfMissing = true)
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("Hotgroup API")
                        .description("Lion SpringDoc")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://hotgroup.cn")))
                .schemaRequirement(HttpHeaders.AUTHORIZATION,securityScheme())
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION));
    }

    private SecurityScheme securityScheme() {
        SecurityScheme securityScheme = new SecurityScheme();
        //类型
        securityScheme.setType(SecurityScheme.Type.APIKEY);
        //请求头的name
        securityScheme.setName(HttpHeaders.AUTHORIZATION);
        //token所在未知
        securityScheme.setIn(SecurityScheme.In.HEADER);
        return securityScheme;
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("app")
                .packagesToScan("com.hotgroup.web.customer.controller")
                .build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .packagesToScan("com.hotgroup.web.manage.controller")
                .build();
    }
}
