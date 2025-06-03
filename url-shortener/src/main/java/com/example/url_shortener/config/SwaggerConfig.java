package com.example.url_shortener.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {


    @Bean
    public OpenAPI urlShortenerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("URL Shortening API")
                        .version("1.0")
                        .description("A service for shortening URLs with expiration and authentication features."));
    }
}