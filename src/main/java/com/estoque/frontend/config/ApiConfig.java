package com.estoque.frontend.config;

import com.estoque.frontend.service.ApiService;
import com.estoque.frontend.service.SpringApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

@Configuration
public class ApiConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApiConfig.class);
    private static final String API_URL = "http://localhost:8082/api";

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
            .setConnectTimeout(Duration.ofSeconds(5))
            .setReadTimeout(Duration.ofSeconds(5))
            .build();
    }

    @Bean
    public ApiService apiService(RestTemplate restTemplate) {
        logger.info("Configurando API Service com URL: {}", API_URL);
        return new SpringApiService(API_URL, restTemplate);
    }
} 