package com.neobridge.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for the NeoBridge Payment Service.
 * Provides payment processing, settlement, and compliance features.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
public class NeoBridgePaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgePaymentApplication.class, args);
    }
}
