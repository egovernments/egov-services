package org.egov.eis.producers;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@EnableKafka
public class AttendanceProducerConfig {
    @Value("${kafka.config.bootstrap_server_config}")
    private String serverConfig;

    @Value("${kafka.producer.config.retries_config}")
    private Integer retriesConfig;

    @Value("${kafka.producer.config.batch_size_config}")
    private Integer batchSizeConfig;

    @Value("${kafka.producer.config.linger_ms_config}")
    private Integer lingerMsConfig;

    @Value("${kafka.producer.config.buffer_memory_config}")
    private Integer bufferMemoryConfig;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        final Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfig);
        props.put(ProducerConfig.RETRIES_CONFIG, retriesConfig);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSizeConfig);
        props.put(ProducerConfig.LINGER_MS_CONFIG, lingerMsConfig);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemoryConfig);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public AttendanceProducer sender() {
        return new AttendanceProducer();
    }
}