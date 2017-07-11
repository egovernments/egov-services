package org.egov.tracer.config;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.kafka.core.KafkaTemplate;

public class LoggingKafkaTemplateFactory {

    private TracerProperties tracerProperties;
    private ObjectMapperFactory objectMapperFactory;

    public LoggingKafkaTemplateFactory(TracerProperties tracerProperties,
                                       ObjectMapperFactory objectMapperFactory) {
        this.tracerProperties = tracerProperties;
        this.objectMapperFactory = objectMapperFactory;
    }

    public <K, V> LogAwareKafkaTemplate<K, V> build(KafkaTemplate<K, V> kafkaTemplate) {
        return new LogAwareKafkaTemplate<>(tracerProperties, kafkaTemplate, objectMapperFactory);
    }
}
