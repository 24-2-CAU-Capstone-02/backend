package com.capstone.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Backend API Document",
                description = "Swagger로 자동 생성된 백엔드 API 문서입니다.",
                version = "v0.1"
        )
)
@Configuration
public class SwaggerConfig {
}