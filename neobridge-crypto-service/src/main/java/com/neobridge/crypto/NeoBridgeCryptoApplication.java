package com.neobridge.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for the NeoBridge Crypto Service.
 * Provides cryptocurrency wallet management, trading, and DeFi features.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
public class NeoBridgeCryptoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgeCryptoApplication.class, args);
    }
}
