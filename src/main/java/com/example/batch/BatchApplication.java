package com.example.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Spring Boot application.
 * Also enables Spring scheduling.
 *- Uses @EnableScheduling to activate Spring's scheduled task execution.
 */
@SpringBootApplication
@EnableScheduling
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}
