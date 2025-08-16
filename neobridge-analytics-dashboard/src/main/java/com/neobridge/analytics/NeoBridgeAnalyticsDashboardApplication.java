package com.neobridge.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

/**
 * Main Spring Boot application class for the NeoBridge Advanced Analytics Dashboard Service.
 * Provides comprehensive analytics, reporting, business intelligence, and real-time insights.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
@EnableAsync
@EnableMethodSecurity
public class NeoBridgeAnalyticsDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgeAnalyticsDashboardApplication.class, args);
    }
}
