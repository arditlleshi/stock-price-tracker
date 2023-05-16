package com.stockpricetracker.stockpricetracker.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Getter
public class AlphaVantageConfig {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Value("${alphavantage.api.base-url}")
    private String baseUrl;

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
