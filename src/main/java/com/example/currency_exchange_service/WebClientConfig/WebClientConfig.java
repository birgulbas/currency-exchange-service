package com.example.currency_exchange_service.WebClientConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder customWebClientBuilder() {
        return WebClient.builder();
    }
}
