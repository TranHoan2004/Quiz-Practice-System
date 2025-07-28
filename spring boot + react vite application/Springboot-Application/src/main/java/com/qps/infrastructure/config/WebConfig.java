package com.qps.infrastructure.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Component
public class WebConfig implements WebMvcConfigurer {
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("WebConfig.addCorsMappings");
        registry.addMapping("/**")
                .allowedMethods(ALLOWED_METHODS)
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:5173")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
