package com.neobridge.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main Spring Boot application class for the NeoBridge Account Service.
 * Provides account management, balance tracking, and transaction history.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableCaching
public class NeoBridgeAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgeAccountApplication.class, args);
    }
}
