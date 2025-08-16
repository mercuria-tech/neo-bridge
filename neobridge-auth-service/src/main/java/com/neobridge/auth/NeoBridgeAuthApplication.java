package com.neobridge.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * Main Spring Boot application class for the NeoBridge Authentication Service.
 * Provides user authentication, authorization, and session management.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
public class NeoBridgeAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgeAuthApplication.class, args);
    }
}
