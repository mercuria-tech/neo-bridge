package com.neobridge.custody;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Main Spring Boot application class for the NeoBridge Institutional Custody Service.
 * Provides institutional-grade custody, multi-signature wallets, and white-label platform features.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
@EnableAsync
@EnableMethodSecurity
public class NeoBridgeInstitutionalCustodyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgeInstitutionalCustodyApplication.class, args);
    }
}
