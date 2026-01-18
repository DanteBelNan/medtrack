package com.medtrack.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient; // Cambiar a Async
import java.net.URI;

@Configuration
public class SqsConfig {

    @Value("${spring.cloud.aws.sqs.endpoint}")
    private String endpoint;

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create(endpoint))
                .build();
    }
}