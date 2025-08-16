package com.neobridge.investment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main Spring Boot application class for the NeoBridge Investment Service.
 * Provides investment management, trading, and portfolio analytics features.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
public class NeoBridgeInvestmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgeInvestmentApplication.class, args);
    }
}
