package com.medtrack;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public SqsTemplate sqsTemplate() {
        return Mockito.mock(SqsTemplate.class);
    }
}