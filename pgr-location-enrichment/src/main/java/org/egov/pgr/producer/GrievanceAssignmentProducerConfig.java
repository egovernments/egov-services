package org.egov.pgr.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class GrievanceAssignmentProducerConfig {

    @Value("kafka.producer.config.bootstrap_server_config")
    private String serverConfig;

    @Value("kafka.producer.config.buffer_memory_config")
    private String bufferMemoryConfig;

    @Value("kafka.producer.config.linger_ms_config")
    private String lingerMsConfig;

    @Value("kafka.producer.config.batch_size_config")
    private String batchSizeConfig;

    @Value("kafka.producer.config.retries_config")
    private String retiresConfig;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfig);
        props.put(ProducerConfig.RETRIES_CONFIG, retiresConfig);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSizeConfig);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMsConfig);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemoryConfig);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public GrievanceAssignmentProducer sender() {
        return new GrievanceAssignmentProducer();
    }
}
