package org.egov.tracer.config;

import org.egov.tracer.kafka.LogAwareKafkaTemplate;
import org.springframework.kafka.core.KafkaTemplate;

public class LoggingKafkaTemplateFactory {

    private TracerProperties tracerProperties;

    public LoggingKafkaTemplateFactory(TracerProperties tracerProperties) {
        this.tracerProperties = tracerProperties;
    }

    public <K, V> LogAwareKafkaTemplate<K, V> build(KafkaTemplate<K, V> kafkaTemplate) {
        return new LogAwareKafkaTemplate<K, V>(tracerProperties, kafkaTemplate);
    }
}
