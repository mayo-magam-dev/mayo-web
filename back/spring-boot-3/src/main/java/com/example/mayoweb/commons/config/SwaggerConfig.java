package com.example.mayoweb.commons.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("mayo API 서버")
                        .description("mayo 서버 API 명세서 입니다.")
                        .version("v0.0.1"));
    }
}