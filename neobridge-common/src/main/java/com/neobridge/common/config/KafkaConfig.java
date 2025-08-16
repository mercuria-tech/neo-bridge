package com.neobridge.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka configuration for the NeoBridge platform.
 * Provides Kafka producer configuration and topic definitions.
 */
@Configuration
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.acks:all}")
    private String acks;

    @Value("${spring.kafka.producer.retries:3}")
    private int retries;

    @Value("${spring.kafka.producer.batch-size:16384}")
    private int batchSize;

    @Value("${spring.kafka.producer.buffer-memory:33554432}")
    private int bufferMemory;

    @Value("${spring.kafka.producer.key-serializer:org.apache.kafka.common.serialization.StringSerializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer:org.springframework.kafka.support.serializer.JsonSerializer}")
    private String valueSerializer;

    /**
     * Configures Kafka producer factory with custom settings.
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 30000);
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Creates Kafka template for sending messages.
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    /**
     * Defines core platform topics.
     */
    @Bean
    public NewTopic userEventsTopic() {
        return TopicBuilder.name("neobridge.user.events")
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "604800000")) // 7 days
                .build();
    }

    @Bean
    public NewTopic transactionEventsTopic() {
        return TopicBuilder.name("neobridge.transaction.events")
                .partitions(5)
                .replicas(1)
                .configs(Map.of("retention.ms", "2592000000")) // 30 days
                .build();
    }

    @Bean
    public NewTopic cryptoEventsTopic() {
        return TopicBuilder.name("neobridge.crypto.events")
                .partitions(3)
                .replicas(1)
                .configs(Map.of("retention.ms", "604800000")) // 7 days
                .build();
    }

    @Bean
    public NewTopic complianceEventsTopic() {
        return TopicBuilder.name("neobridge.compliance.events")
                .partitions(2)
                .replicas(1)
                .configs(Map.of("retention.ms", "31536000000")) // 1 year
                .build();
    }

    @Bean
    public NewTopic notificationEventsTopic() {
        return TopicBuilder.name("neobridge.notification.events")
                .partitions(2)
                .replicas(1)
                .configs(Map.of("retention.ms", "86400000")) // 1 day
                .build();
    }

    @Bean
    public NewTopic auditEventsTopic() {
        return TopicBuilder.name("neobridge.audit.events")
                .partitions(2)
                .replicas(1)
                .configs(Map.of("retention.ms", "31536000000")) // 1 year
                .build();
    }
}
