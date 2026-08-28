package com.nazlicanguner.careerflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI careerFlowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CareerFlow API")
                        .description(
                                "REST API for tracking companies, job applications, interviews, and follow-up tasks."
                        )
                        .version("1.0.0"));
    }
}