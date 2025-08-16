package com.neobridge.airisk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot application class for the NeoBridge AI Risk Assessment Service.
 * Provides AI-powered risk scoring, yield optimization, and predictive analytics.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
@EnableAsync
public class NeoBridgeAIRiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgeAIRiskApplication.class, args);
    }
}
