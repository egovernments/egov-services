package org.egov.pgr.employee.enrichment.producer;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.egov.pgr.employee.enrichment.config.PropertiesManager;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ComplaintPersisterConfig {

    @Autowired
    private PropertiesManager propertiesManager;

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, propertiesManager.getServerConfig());
        props.put(ProducerConfig.RETRIES_CONFIG, propertiesManager.getRetriesConfig());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, propertiesManager.getBatchSizeConfig());
        props.put(ProducerConfig.LINGER_MS_CONFIG, propertiesManager.getLingerMsConfig());
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, propertiesManager.getBufferMemoryConfig());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ComplaintPersister sender() {
        return new ComplaintPersister();
    }
}
