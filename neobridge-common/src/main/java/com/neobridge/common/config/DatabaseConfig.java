package com.neobridge.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Database configuration for the NeoBridge platform.
 * Enables JPA auditing and configures database-related beans.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableJpaRepositories(basePackages = "com.neobridge")
public class DatabaseConfig {

    /**
     * Provides the current auditor (user) for JPA auditing.
     * Returns the authenticated user's ID or 'system' if no user is authenticated.
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated() && 
                !"anonymousUser".equals(authentication.getPrincipal())) {
                
                // Try to get user ID from authentication principal
                Object principal = authentication.getPrincipal();
                if (principal instanceof String) {
                    return Optional.of((String) principal);
                } else if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                    return Optional.of(((org.springframework.security.core.userdetails.UserDetails) principal).getUsername());
                }
            }
            
            // Return 'system' for system-generated operations
            return Optional.of("system");
        };
    }
}
