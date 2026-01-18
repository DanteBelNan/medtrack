package com.medtrack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import software.amazon.awssdk.services.sqs.SqsClient;

@SpringBootApplication
@EnableScheduling
public class MedtrackApplication {

    private static final Logger log = LoggerFactory.getLogger(MedtrackApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MedtrackApplication.class, args);
    }
}