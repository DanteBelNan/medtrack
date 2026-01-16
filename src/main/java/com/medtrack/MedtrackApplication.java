package com.medtrack; // Debe coincidir con la carpeta src/main/java/com/medtrack

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MedtrackApplication {
    public static void main(String[] args) {
        SpringApplication.run(MedtrackApplication.class, args);
    }
}