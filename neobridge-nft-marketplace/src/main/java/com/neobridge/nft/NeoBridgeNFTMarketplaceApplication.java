package com.neobridge.nft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Main Spring Boot application class for the NeoBridge NFT Marketplace Service.
 * Provides NFT creation, trading, auction, and digital asset management features.
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableKafka
@EnableCaching
@EnableScheduling
@EnableAsync
public class NeoBridgeNFTMarketplaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NeoBridgeNFTMarketplaceApplication.class, args);
    }
}
